package tr.com.stm.cydecsys.zaprestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.stm.cydecsys.zaprestapi.*;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;
import tr.com.stm.cydecsys.zaprestapi.services.ZAPScanService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ZAPScanResultController {

    @Autowired
    ZAPScanService zapScanService;

    private ZAPDaemon zapDaemon;
    private HashMap<Integer,Spider> spiders = new HashMap<>();
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

    public void addZAPScanResultToDatabase(ZAPScanResult zapScanResult){
        zapScanService.saveOrUpdate(zapScanResult);
        System.out.println("Added to database successfuly");
    }

    @GetMapping(value = "/get-scan/{id}")
    public ResponseEntity<String> getResults(@PathVariable int id){
        ZAPScanResult zapScanResult = null;
        try{
            zapScanResult = zapScanService.getZAPScanResultById(id).get();
            if(zapScanResult.getResult().isEmpty()){
                Spider s = spiders.get(id);
                if(s == null){
                    new NoSuchElementException("Spider is null");
                }
                else if(s.getPassiveScanNumberOfRecords() == -1){
                    return new ResponseEntity<>(
                            "Number of records left for scanning : UNKNOWN", HttpStatus.OK
                    );
                }
                else{
                    return new ResponseEntity<>(
                            "Number of records left for scanning : " + s.getPassiveScanNumberOfRecords()+"\n", HttpStatus.OK
                    );
                }
            }
            else{
                return new ResponseEntity<>(
                        zapScanResult.getResult(), HttpStatus.OK
                );
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        return new ResponseEntity<>(
                "Error!!! There is no scan related to the scanId:"+id+"\n", HttpStatus.OK
        );
    }
    @RequestMapping(value ="/add-database/{id}", method = GET)
    @ResponseBody
    public void addDatabase(@PathVariable int id){
        try{
            ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById(id).get();
            Spider s = spiders.get(id);
            zapScanResult.setResult(s.getPassiveScanResults());
            zapScanResult.setSpider_id(Integer.parseInt( s.getSpiderID() ));
            addZAPScanResultToDatabase(zapScanResult);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/kill-spider/{id}", method = GET)
    @ResponseBody
    public void killSpider(@PathVariable int id){
        Spider s = spiders.get(id);
        if(s != null){
            System.out.println("id:"+id+", spider is going to killed.");
            s.stop();
        }
    }
    @PostMapping(value = "/create-scan")
    public ResponseEntity<String> scan(@RequestBody String newTarget){
        scanIDCounter++;
        Spider spider = new Spider();
        spider.setScanID(scanIDCounter);
        spider.setTARGET(newTarget);
        spiders.put(scanIDCounter, spider);
        addZAPScanResultToDatabase(new ZAPScanResult(spider.getScanID(), scanIDCounter-1, spider.getTARGET(), "") );
        spider.start();
        return new ResponseEntity<>(
                "Id:"+scanIDCounter+",targetURL:"+newTarget+"\n", HttpStatus.OK
        );
    }
}
