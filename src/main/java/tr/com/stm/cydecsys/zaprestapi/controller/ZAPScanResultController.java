package tr.com.stm.cydecsys.zaprestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.stm.cydecsys.zaprestapi.*;
import tr.com.stm.cydecsys.zaprestapi.services.ZAPScanService;

import java.util.ArrayList;

@RestController
public class ZAPScanResultController {

    @Autowired
    ZAPScanService zapScanService;

    private ZAPDaemon zapDaemon;
    private static ArrayList<Spider> spiders = new ArrayList<>();
    private int scanIDCounter = 0;

    public ZAPScanResultController() {
        if( !ZAPDaemon.isOwaspZapAlive()){
            zapDaemon = new ZAPDaemon();
            Thread zapDaemonThread = new Thread(zapDaemon, "T1");
            zapDaemonThread.start();
            if(zapDaemon.waitOwaspZAP() == false ){
                System.exit(1);
            }
        }
    }

    @GetMapping(value = "/get-scan/{id}")
    public ResponseEntity<String> getResults(@PathVariable int id){
        for(int i=0; i<spiders.size(); i++){
            if(spiders.get(i).getScanID() == id ){
                if(spiders.get(i).IsPassiveScanFinished() == true){
                    return new ResponseEntity<>(
                            spiders.get(i).getPassiveScanResults()+"\n", HttpStatus.OK
                    );
                }
                else{
                    if(spiders.get(i).getPassiveScanNumberOfRecords() == -1){
                        return new ResponseEntity<>(
                                "Number of records left for scanning : UNKNOWN", HttpStatus.OK
                        );
                    }
                    return new ResponseEntity<>(
                            "Number of records left for scanning : " + spiders.get(i).getPassiveScanNumberOfRecords()+"\n", HttpStatus.OK
                    );
                }
            }
        }
        return new ResponseEntity<>(
                "Error!!! There is no scan related to the scanId:"+id+"\n", HttpStatus.OK
        );
    }
    @PostMapping(value = "/create-scan")
    public ResponseEntity<String> scan(@RequestBody String newTarget){
        scanIDCounter++;
        Spider spider = new Spider();
        spider.setScanID(scanIDCounter);
        spider.setTARGET(newTarget);
        spiders.add(spider);
        spider.start();
        return new ResponseEntity<>(
                "Id:"+scanIDCounter+"\n", HttpStatus.OK
        );
    }
    public static void killThread(int id){
        for(int i=0; i<spiders.size(); i++){
            if(spiders.get(i).getScanID() == id){
                spiders.get(i).stop();
                break;
            }
        }
    }


}
