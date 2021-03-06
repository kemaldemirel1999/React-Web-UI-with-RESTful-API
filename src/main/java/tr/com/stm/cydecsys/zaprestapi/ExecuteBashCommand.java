package tr.com.stm.cydecsys.zaprestapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
    This class helps to execute Terminal commands from our Java Program.
 */
public class ExecuteBashCommand {


    // This methods takes a parameter and execute that command in Terminal for example: Bash Console
    public String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}