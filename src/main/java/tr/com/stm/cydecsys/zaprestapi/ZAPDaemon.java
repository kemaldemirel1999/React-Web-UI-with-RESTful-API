package tr.com.stm.cydecsys.zaprestapi;

import java.util.Scanner;

public class ZAPDaemon implements Runnable{
    private volatile boolean exit = false;

    public void terminateZAP(){
        ExecuteBashCommand cmd2 = new ExecuteBashCommand();
        String ps_aux = cmd2.executeCommand("ps aux");
        Scanner scanner = new Scanner(ps_aux);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains("/opt/zaproxy/zap")){
                String[] temp = line.trim().split("\\s+");
                String killCommand = "kill -9 "+ temp[1];
                cmd2.executeCommand(killCommand);
                break;
            }
        }
        scanner.close();
        stop();
    }
    public boolean isOwaspZapAlive(){
        ExecuteBashCommand cmd2 = new ExecuteBashCommand();
        String ps_aux = cmd2.executeCommand("ps aux");
        Scanner scanner = new Scanner(ps_aux);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains("/opt/zaproxy/zap")){
                return true;
            }
        }
        return false;
    }
    public void waitOwaspZAP(){
        try{
            long startTime = System.currentTimeMillis();
            while (true){
                boolean isOwaspAlive;
                Thread.sleep(5000);
                isOwaspAlive = isOwaspZapAlive();
                if(isOwaspAlive){
                    System.out.println("OwaspZap is Running in the background.");
                    break;
                }
                long estimatedTime = System.currentTimeMillis() - startTime;
                if (estimatedTime > 15000){
                    System.out.println("Too much time passed while trying to open Owasp Zap. Program automatically terminated");
                    return;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void run() {
        ExecuteBashCommand cmd = new ExecuteBashCommand();
        cmd.executeCommand("zap.sh -daemon");

        while(!exit) {

        }
    }
    public void stop() {
        exit = true;


    }

}
