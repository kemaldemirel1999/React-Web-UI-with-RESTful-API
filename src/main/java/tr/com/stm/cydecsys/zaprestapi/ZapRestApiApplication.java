package tr.com.stm.cydecsys.zaprestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@SpringBootApplication
public class ZapRestApiApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ZapRestApiApplication.class, args);

        // Before we run spider and passive scan, OwaspZAP must be started.
        ZAPDaemon zapDaemon = new ZAPDaemon();
        Thread thread = new Thread(zapDaemon, "T1");
        thread.start();

        // To make sure OWASP ZAP is started, we should wait
        zapDaemon.waitOwaspZAP();

        Spider spider = new Spider();
        System.out.println("scanID:[" + spider.runSpider()+"]");

        //  After we start Spider, we can start passive scan
        PassiveScan passiveScan = new PassiveScan();
        System.out.println("Alerts:\n"+passiveScan.runPassiveScan());

        // OWASPZAP is running in the background, before we try to terminate the program, we have to terminate the OWASP ZAP
        zapDaemon.terminateZAP();

    }

}
