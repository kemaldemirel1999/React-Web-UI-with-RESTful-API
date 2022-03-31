package tr.com.stm.cydecsys.zaprestapi.owaspzap;

import tr.com.stm.cydecsys.zaprestapi.ExecuteBashCommand;

import java.util.Scanner;

/*
    This class does operations such as closing, opening for OWASPZAP
    It must be executed before doing Spider, PassiveScan etc.
*/
public class ZAPDaemon implements Runnable{

    // When "exit" is true, this ZAPDaemon thread is terminated
    private volatile boolean exit = false;

    // This method terminates OWASPZAP.
    public void terminateZAP(){
        ExecuteBashCommand cmd2 = new ExecuteBashCommand();
        String ps_aux = cmd2.executeCommand("ps aux");
        Scanner scanner = new Scanner(ps_aux);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains("/opt/zaproxy/zap") && line.contains("daemon")){
                String[] temp = line.trim().split("\\s+");
                String killCommand = "kill -9 "+ temp[1];
                cmd2.executeCommand(killCommand);
                break;
            }
        }
        scanner.close();
        stop();
    }

    // Check OWASPZAP to see if running in background
    public static boolean isOwaspZapAlive(){
        ExecuteBashCommand cmd2 = new ExecuteBashCommand();
        String ps_aux = cmd2.executeCommand("ps aux");
        Scanner scanner = new Scanner(ps_aux);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains("/opt/zaproxy/zap") && line.contains("daemon")){
                scanner.close();
                return true;
            }
        }
        scanner.close();
        return false;
    }

    // If OWASPZAP cannot executed correctly, return false, otherwise true
    public boolean waitOwaspZAP(){
        try{
            long startTime = System.currentTimeMillis();
            while (true){
                boolean isOwaspAlive;
                Thread.sleep(5000);
                isOwaspAlive = isOwaspZapAlive();
                if(isOwaspAlive){
                    System.out.println("OwaspZap is Running in the background.");
                    return true;
                }
                long estimatedTime = System.currentTimeMillis() - startTime;
                if (estimatedTime > 15000){
                    System.out.println("Too much time passed while trying to open Owasp Zap. Program automatically terminated");
                    return false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void run() {
        ExecuteBashCommand cmd = new ExecuteBashCommand();
        cmd.executeCommand("zap.sh -daemon");
        while(!exit) {}
    }
    public void stop() {
        exit = true;
    }

}
