package tr.com.stm.cydecsys.zaprestapi.controller;

import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.stm.cydecsys.zaprestapi.*;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;
import tr.com.stm.cydecsys.zaprestapi.services.ZAPScanService;

import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/*
        This class helps us to manage POST,DELETE,GET requests.
 */
@RestController
@RequestMapping("api/")
public class ZAPScanResultController {

    @Autowired
    ZAPScanService zapScanService;

    // ZAPDaemon thread, runs in the background until program is terminated.
    private ZAPDaemon zapDaemon;
    // This HashMap includes all threads which is created.
    private HashMap<Integer,Spider> spiders = new HashMap<>();
    // This counter represents our next scan's id.
    private int scanIDCounter = 0;
    // When database is clear, new tuple is created with id:0 to save scanIdCounter for later attacks.
    private boolean databaseChecked = false;
    private  int ZAP_PORT = 8091;
    private  String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private  String ZAP_ADDRESS = "localhost";

    //  Before we doing any operations, OWASPZAP is executed in the background.
    public ZAPScanResultController() {
        if( !ZAPDaemon.isOwaspZapAlive()){
            zapDaemon = new ZAPDaemon();
            Thread zapDaemonThread = new Thread(zapDaemon, "T1");
            zapDaemonThread.start();

            //If OWASPZAP could not be opened, program is terminated.
            if(zapDaemon.waitOwaspZAP() == false ){
                System.exit(1);
            }
        }
    }


//    public void changeSettings(int port, String api_key, String zap_adress){
//        this.ZAP_PORT = port;
//        this.ZAP_API_KEY = api_key;
//        this.ZAP_ADDRESS = zap_adress;
//    }


//    public void changeSettings(boolean isItActiveScan, String url){
//        ExecuteBashCommand cmd = new ExecuteBashCommand();
//        if(isItActiveScan){
//            String command = "curl -X POST localhost:8080/api/create-scan/active -H 'Content-type:application/json' -d '"+url+"'";
//            cmd.executeCommand(command);
//        }else{
//            String command = "curl -X POST localhost:8080/api/create-scan/passive -H 'Content-type:application/json' -d '"+url+"'";
//            cmd.executeCommand(command);
//        }
//    }

    @GetMapping("scans")
    public void refreshAllScans(){
        List<ZAPScanResult> results = new ArrayList<>();
        results.addAll(zapScanService.findAll());
        PrintWriter outputStream = null;
        PrintWriter lastScanOutputStream = null;
        try{
            outputStream = new PrintWriter("/home/staj/Desktop/OwaspZAP Rest Application/owaspzap/src/main/ui/src/assets/JsonData/scan-list.json");
            lastScanOutputStream = new PrintWriter("/home/staj/Desktop/OwaspZAP Rest Application/owaspzap/src/main/ui/src/assets/JsonData/last-scan-list.json");
            lastScanOutputStream.print("[");
            outputStream.print("[");
            for(int i=0; i<results.size(); i++) {
                if( i == 0) continue;
                ZAPScanResult zapScanResult = results.get(i);
                String addingScan = "{\"id\":"+zapScanResult.getScanId()+",\"scanType\":\""+zapScanResult.getScanType()+"\",\"targetURL\":\""+zapScanResult.getUrl()+"\",\"status\":";
                if(zapScanResult.getResult().isEmpty()){
                    addingScan = addingScan + "\"Continuing\",";
                }else{
                    addingScan = addingScan + "\"Finished\",";
                }
                addingScan = addingScan + "\"highrisks\":0," + "\"middlerisks\":0," + "\"lowrisks\":0}";
                if( i != results.size()-1){
                    addingScan = addingScan + ",";
                }else{
                    lastScanOutputStream.println(addingScan);
                }
                outputStream.println(addingScan);
            }
            outputStream.print("]");
            lastScanOutputStream.print("]");
        }catch (Exception e){
            e.printStackTrace();
        }
        outputStream.close();
        lastScanOutputStream.close();
    }


    // Takes a ZAPScanResult object and saves it to database or updates the database with new zapScanResult object
    public void addZAPScanResultToDatabase(ZAPScanResult zapScanResult){
        zapScanService.saveOrUpdate(zapScanResult);
    }


    //  Returns PassiveScan's results.
    //  If passive scan is not finished, returns the numberOfRecords which is left.
    @GetMapping(value = "/get-scan/{id}")
    public ResponseEntity<String> getResults(@PathVariable int id){
        ZAPScanResult zapScanResult;
        try{
            zapScanResult = zapScanService.getZAPScanResultById(String.valueOf(id)).get();
            if(zapScanResult.getResult().isEmpty() && zapScanResult.getScanType().equals("Passive") ){
                Spider s = spiders.get(id);
                if(s == null){
                    new NoSuchElementException("Spider is null");
                }
                // If passive scan not executed yet, this block is executed
                else if(s.getPassiveScanNumberOfRecords() == -1){
                    return new ResponseEntity<>(
                            "Number of records left for Passive Scanning : UNKNOWN", HttpStatus.OK
                    );
                }
                // Shows us the number of recoreds left for scanning
                else{
                    return new ResponseEntity<>(
                            "Number of records left for Passive Scanning : " + s.getPassiveScanNumberOfRecords()+"\n", HttpStatus.OK
                    );
                }
            }
            else if(zapScanResult.getResult().isEmpty() && zapScanResult.getScanType().equals("Active")){
                Spider s = spiders.get(id);
                if(s == null){
                    new NoSuchElementException("Spider is null");
                }
                // If passive scan not executed yet, this block is executed
                else if(s.getActiveScanProgress() == -1){
                    return new ResponseEntity<>(
                            "Progress for Active Scanning : UNKNOWN", HttpStatus.OK
                    );
                }
                // Shows us the number of recoreds left for scanning
                else{
                    return new ResponseEntity<>(
                            "Progress for Active Scanning : " + s.getActiveScanProgress()+"\n", HttpStatus.OK
                    );
                }
            }
            // If this block is executed, it means passive scan is finished and its data's is written to database.
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
        // If there is not any attack related to the given parameter id, this block is executed.
        return new ResponseEntity<>(
                "Error!!! There is no scan related to the scanId:"+id+"\n", HttpStatus.OK
        );
    }


    //  This method is executed when our attack is finished and its results will be written to database.
    //  This method calls related tuple with its id and updates it with PassiveScan's results.
    @RequestMapping(value ="/add-database/{id}", method = GET)
    @ResponseBody
    public void addDatabase(@PathVariable int id){
        try{
            ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById(String.valueOf(id)).get();
            Spider s = spiders.get(id);
            zapScanResult.setResult(s.getResults());
            zapScanResult.setSpider_id(Integer.parseInt( s.getSpiderID() ));
            addZAPScanResultToDatabase(zapScanResult);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        refreshAllScans();
    }

    // With this DELETE request, database is cleared.
    @RequestMapping(value = "/delete-all",method = GET)
    @ResponseBody
    public void deleteAllDatabase(){
        zapScanService.deleteAllDatabase();
        System.out.println("Everything is deleted from database");
        for( ZAPScanResult z : zapScanService.findAll()){
            System.out.println(z);
        }
        refreshAllScans();
    }

    // With this GET request, related thread is stopped.
    @RequestMapping(value = "/kill-spider/{id}", method = GET)
    @ResponseBody
    public void killSpider(@PathVariable int id){
        Spider s = spiders.get(id);
        if(s != null){
            s.stop();
        }
    }

    // With the help of this POST request, new attack is started to the given website.
    // This method returns, this attack's id and targetURL
    @PostMapping(value = "/create-scan/passive")
    public ResponseEntity<String> PassiveScan(@RequestBody String newTarget){
        // This block is only executed when first POST request is happened.
        // We can reach scanIdCounter data and updates it's value with our global variable.
        if(!databaseChecked){
            try {
                ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById("0").get();
                scanIDCounter = Integer.parseInt(String.valueOf(zapScanResult.getSpider_id()));
            }catch (NoSuchElementException e){
                addZAPScanResultToDatabase(new ZAPScanResult("0",0,"nothing","id_counter","none"));
                System.out.println("id_counter is added to database");
            }
            databaseChecked = true;
        }
        scanIDCounter++;// id is incremented for new attack
        // Incremented scanId is saved to database for later attacks.
        try{
            ZAPScanResult id_counter = zapScanService.getZAPScanResultById("0").get();
            id_counter.setSpider_id(scanIDCounter);
            addZAPScanResultToDatabase(id_counter);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        Spider spider = new Spider();
        spider.setScanID(String.valueOf(scanIDCounter));
        spider.setZAP_API_KEY(this.ZAP_API_KEY);
        spider.setZAP_ADDRESS(this.ZAP_ADDRESS);
        spider.setZAP_PORT(this.ZAP_PORT);
        spider.setTARGET(newTarget);
        spiders.put(scanIDCounter, spider);
        /*
            Spider and PassiveScan may take time and before we run them, we save all of the data except passiveScan result.
            PassiveScan result will be written when attack is finished.
            Related tuple is called and will be updated.
        */
        addZAPScanResultToDatabase(new ZAPScanResult(spider.getScanID(), scanIDCounter-1, spider.getTARGET(), "", "Passive") );
        refreshAllScans();
        //  Spider thread is started and runs in the background.
        spider.setIsItActiveScan(false);
        spider.start();
        return new ResponseEntity<>(
                "Id:"+scanIDCounter+", Passive Scan, targetURL:"+newTarget+"\n", HttpStatus.OK
        );
    }





    // With the help of this POST request, new attack is started to the given website.
    // This method returns, this attack's id and targetURL
    @PostMapping(value = "/create-scan/active")
    public ResponseEntity<String> ActiveScan(@RequestBody String newTarget){
        // This block is only executed when first POST request is happened.
        // We can reach scanIdCounter data and updates it's value with our global variable.
        if(!databaseChecked){
            try {
                ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById("0").get();
                scanIDCounter = Integer.parseInt(String.valueOf(zapScanResult.getSpider_id()));
            }catch (NoSuchElementException e){
                addZAPScanResultToDatabase(new ZAPScanResult("0",0,"nothing","id_counter","Active"));
                System.out.println("id_counter is added to database");
            }
            databaseChecked = true;
        }
        scanIDCounter++;// id is incremented for new attack
        // Incremented scanId is saved to database for later attacks.
        try{
            ZAPScanResult id_counter = zapScanService.getZAPScanResultById("0").get();
            id_counter.setSpider_id(scanIDCounter);
            addZAPScanResultToDatabase(id_counter);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        Spider spider = new Spider();
        spider.setScanID(String.valueOf(scanIDCounter));
        spider.setZAP_API_KEY(this.ZAP_API_KEY);
        spider.setZAP_ADDRESS(this.ZAP_ADDRESS);
        spider.setZAP_PORT(this.ZAP_PORT);
        spider.setTARGET(newTarget);
        spiders.put(scanIDCounter, spider);
        /*
            Spider and PassiveScan may take time and before we run them, we save all of the data except activeScan result.
            ActiveScan result will be written when attack is finished.
            Related tuple is called and will be updated.
        */
        addZAPScanResultToDatabase(new ZAPScanResult(spider.getScanID(), scanIDCounter-1, spider.getTARGET(), "", "Active") );
        refreshAllScans();
        //  Spider thread is started and runs in the background.
        spider.setIsItActiveScan(true);
        spider.start();
        return new ResponseEntity<>(
                "Id:"+scanIDCounter+", Active Scan, targetURL:"+newTarget+"\n", HttpStatus.OK
        );
    }
}
