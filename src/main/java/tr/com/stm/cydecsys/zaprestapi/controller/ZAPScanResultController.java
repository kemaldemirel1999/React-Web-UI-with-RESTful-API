package tr.com.stm.cydecsys.zaprestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import tr.com.stm.cydecsys.zaprestapi.ExecuteBashCommand;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;
import tr.com.stm.cydecsys.zaprestapi.owaspzap.Spider;
import tr.com.stm.cydecsys.zaprestapi.owaspzap.ZAPDaemon;
import tr.com.stm.cydecsys.zaprestapi.parser.*;
import tr.com.stm.cydecsys.zaprestapi.services.ZAPScanService;

import java.io.PrintWriter;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/*
        This class helps us to manage POST,DELETE,GET requests.
 */
@RestController
@RequestMapping("api/")
@CrossOrigin
public class ZAPScanResultController {

    @Autowired
    ZAPScanService zapScanService;

    // ZAPDaemon thread, runs in the background until program is terminated.
    private ZAPDaemon zapDaemon;
    // This HashMap includes all threads which is created.
    private HashMap<Integer, Spider> spiders = new HashMap<>();
    // This counter represents our next scan's id.
    private int scanIDCounter = 0;
    // When database is clear, new tuple is created with id:0 to save scanIdCounter for later attacks.
    private boolean databaseChecked = false;
    private int ZAP_PORT = 8090;
    private String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private String ZAP_ADDRESS = "localhost";
    private String workingDirectory = "";

    //  Before we doing any operations, OWASPZAP is executed in the background.


    public ZAPScanResultController() {
        workingDirectory = System.getProperty("user.dir");
        if (!ZAPDaemon.isOwaspZapAlive()) {
            zapDaemon = new ZAPDaemon();
            Thread zapDaemonThread = new Thread(zapDaemon, "T1");
            zapDaemonThread.start();
            //If OWASPZAP could not be opened, program is terminated.
            if (zapDaemon.waitOwaspZAP() == false) {
                System.exit(1);
            }
        }
    }
    @GetMapping("update-failed-scans")
    public void updateFailedScan() {
        List<ZAPScanResult> results = new ArrayList<>();
        results.addAll(zapScanService.findAll());
        for (int i = 1; i < results.size(); i++) {
            ZAPScanResult zapScanResult = results.get(i);
            if (zapScanResult != null && zapScanResult.getResult() != null) {
                if (zapScanResult.getResult().isEmpty()) {
                    Spider spider = null;
                    for (Map.Entry<Integer, Spider> map : spiders.entrySet()){
                        if(map.getValue().getScanID().equals(i)){
                               spider = map.getValue();
                        }
                    }
                    if( ! (spider != null && !spider.isAlive() )){
                        zapScanResult.setResult(null);
                        addZAPScanResultToDatabase(zapScanResult);
                    }
                }
            }
        }
        refreshAllScans();
        refreshStatus();
    }
    public ArrayList<Integer> getRiskLevels(String result) {
        try {
            OwaspXMLParser owaspXMLParser = new OwaspXMLParser();
            WebScannerResult webScannerResult = owaspXMLParser.parseOwaspReport(result);
            List<WebScannerSingleResult> webScannerSingleResults = webScannerResult.getResultItems();
            int lowCounter = 0;
            int middleCounter = 0;
            int highCounter = 0;
            for (WebScannerSingleResult w : webScannerSingleResults) {
                if (w.getSeverity().equals("Low")) {
                    lowCounter++;
                } else if (w.getSeverity().equals("Medium")) {
                    middleCounter++;
                } else if (w.getSeverity().equals("High")) {
                    highCounter++;
                }
            }
            ArrayList<Integer> resultList = new ArrayList<>();
            resultList.add(lowCounter);
            resultList.add(middleCounter);
            resultList.add(highCounter);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("status")
    public void refreshStatus() {
        List<ZAPScanResult> results = new ArrayList<>();
        results.addAll(zapScanService.findAll());
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(workingDirectory + "/src/main/ui/src/assets/JsonData/status-card-data.json");
            outputStream.print("[");
            int numberOfActiveScan = 0, numberOfPassivesScan = 0, total_scan;
            int highRiskCounter = 0;
            int mediumRiskCounter = 0;
            int lowRiskCounter = 0;
            int totalNumberOfRisk;
            for (int i = 1; i < results.size(); i++) {
                ZAPScanResult zapScanResult = results.get(i);
                if (zapScanResult.getScanType().equals("Active")) numberOfActiveScan++;
                else if (zapScanResult.getScanType().equals("Passive")) numberOfPassivesScan++;
                highRiskCounter += zapScanResult.getHighRisk();
                mediumRiskCounter += zapScanResult.getMediumRisk();
                lowRiskCounter += zapScanResult.getLowRisk();
            }
            totalNumberOfRisk = highRiskCounter + mediumRiskCounter + lowRiskCounter;
            double highRiskRate;
            double mediumRiskRate;
            double lowRiskRate;
            if(totalNumberOfRisk == 0){
                highRiskRate = 0;
                mediumRiskRate = 0;
                lowRiskRate = 0;
            }
            else{
                highRiskRate = ( (double)highRiskCounter / (double)totalNumberOfRisk ) * 100;
                mediumRiskRate = ( (double)mediumRiskCounter / (double)totalNumberOfRisk ) * 100;
                lowRiskRate = ( (double)lowRiskCounter / (double)totalNumberOfRisk ) * 100;
            }
            total_scan = numberOfActiveScan + numberOfPassivesScan;
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-caret-right-circle\",\n" +
                    "        \"count\":" + total_scan + ",\n" +
                    "        \"title\": \"Total Scan\"\n" +
                    "    },");
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-meteor\",\n" +
                    "        \"count\":" + numberOfPassivesScan + ",\n" +
                    "        \"title\": \"Number of Passive Scan\"\n" +
                    "    },");
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-meteor\",\n" +
                    "        \"count\":" + numberOfActiveScan + ",\n" +
                    "        \"title\": \"Number of Active Scan\"\n" +
                    "    },");
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-error\",\n" +
                    "        \"count\":\"%" + String.format("%.1f", highRiskRate) + "\",\n" +
                    "        \"title\": \"High Risk Rate\"\n" +
                    "    },");
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-error\",\n" +
                    "        \"count\":\"%" + String.format("%.1f", mediumRiskRate) + "\",\n" +
                    "        \"title\": \"Middle Risk Rate\"\n" +
                    "    },");
            outputStream.print("{\n" +
                    "        \"icon\": \"bx bx-error\",\n" +
                    "        \"count\":\"%" + String.format("%.1f", lowRiskRate) + "\",\n" +
                    "        \"title\": \"Low Risk Rate\"\n" +
                    "    }");
            outputStream.print("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.close();

    }

    @GetMapping("scans")
    public void refreshAllScans() {
        List<ZAPScanResult> results = new ArrayList<>();
        results.addAll(zapScanService.findAll());
        PrintWriter outputStream = null;
        PrintWriter lastScanOutputStream = null;
        try {
            outputStream = new PrintWriter(workingDirectory + "/src/main/ui/src/assets/JsonData/scan-list.json");
            lastScanOutputStream = new PrintWriter(workingDirectory + "/src/main/ui/src/assets/JsonData/last-scan-list.json");
            lastScanOutputStream.print("[");
            outputStream.print("[");
            for (int i = 0; i < results.size(); i++) {
                if (i == 0) continue;
                ZAPScanResult zapScanResult = results.get(i);
                String addingScan = "{\"id\":" + zapScanResult.getScanId() + ",\"scanType\":\"" + zapScanResult.getScanType() + "\",\"targetURL\":\"" + zapScanResult.getUrl() + "\",\"status\":";
                if (zapScanResult.getResult() == null) {
                    addingScan = addingScan + "\"Failed\",";
                }
                else if (zapScanResult.getResult().isEmpty()) {
                    addingScan = addingScan + "\"Continuing\",";
                }
                else {
                    addingScan = addingScan + "\"Finished\",";
                }
                addingScan = addingScan + "\"highrisks\":"+zapScanResult.getHighRisk()+"," + "\"middlerisks\":"+zapScanResult.getMediumRisk()+"," + "\"lowrisks\":"+zapScanResult.getLowRisk()+"}";
                if (i != results.size() - 1) {
                    addingScan = addingScan + ",";
                } else {
                    lastScanOutputStream.println(addingScan);
                }
                outputStream.println(addingScan);
            }
            outputStream.print("]");
            lastScanOutputStream.print("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.close();
        lastScanOutputStream.close();
    }


    // Takes a ZAPScanResult object and saves it to database or updates the database with new zapScanResult object
    public void addZAPScanResultToDatabase(ZAPScanResult zapScanResult) {
        zapScanService.saveOrUpdate(zapScanResult);
    }

    //  Returns PassiveScan's results.
    //  If passive scan is not finished, returns the numberOfRecords which is left.
    @GetMapping(value = "/get-scan/{id}")
    public ResponseEntity<String> getResults(@PathVariable int id) {
        ZAPScanResult zapScanResult;
        try {
            zapScanResult = zapScanService.getZAPScanResultById(String.valueOf(id)).get();
            if (zapScanResult.getResult().isEmpty() && zapScanResult.getScanType().equals("Passive")) {
                Spider s = spiders.get(id);
                if (s == null) {
                    new NoSuchElementException("Spider is null");
                }
                // If passive scan not executed yet, this block is executed
                else if (s.getPassiveScanNumberOfRecords() == -1) {
                    return new ResponseEntity<>(
                            "Number of records left for Passive Scanning : UNKNOWN", HttpStatus.OK
                    );
                }
                // Shows us the number of recoreds left for scanning
                else {
                    return new ResponseEntity<>(
                            "Number of records left for Passive Scanning : " + s.getPassiveScanNumberOfRecords() + "\n", HttpStatus.OK
                    );
                }
            } else if (zapScanResult.getResult().isEmpty() && zapScanResult.getScanType().equals("Active")) {
                Spider s = spiders.get(id);
                if (s == null) {
                    new NoSuchElementException("Spider is null");
                }
                // If passive scan not executed yet, this block is executed
                else if (s.getActiveScanProgress() == -1) {
                    return new ResponseEntity<>(
                            "Progress for Active Scanning : UNKNOWN", HttpStatus.OK
                    );
                }
                // Shows us the number of recoreds left for scanning
                else {
                    return new ResponseEntity<>(
                            "Progress for Active Scanning : " + s.getActiveScanProgress() + "\n", HttpStatus.OK
                    );
                }
            }
            // If this block is executed, it means passive scan is finished and its data's is written to database.
            else {
                return new ResponseEntity<>(
                        zapScanResult.getResult(), HttpStatus.OK
                );
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If there is not any attack related to the given parameter id, this block is executed.
        return new ResponseEntity<>(
                "Error!!! There is no scan related to the scanId:" + id + "\n", HttpStatus.OK
        );
    }


    //  This method is executed when our attack is finished and its results will be written to database.
    //  This method calls related tuple with its id and updates it with PassiveScan's results.
    @RequestMapping(value = "/add-database/{id}", method = GET)
    @ResponseBody
    public void addDatabase(@PathVariable int id) {
        try {
            ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById(String.valueOf(id)).get();
            Spider s = spiders.get(id);
            zapScanResult.setResult(s.getResults());
            zapScanResult.setSpider_id(Integer.parseInt(s.getSpiderID()));
            ArrayList<Integer> riskList = getRiskLevels(s.getResults());
            zapScanResult.setLowRisk(riskList.get(0));
            zapScanResult.setMediumRisk(riskList.get(1));
            zapScanResult.setHighRisk(riskList.get(2));
            addZAPScanResultToDatabase(zapScanResult);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        refreshAllScans();
        refreshStatus();
    }

    // With this DELETE request, database is cleared.
    @RequestMapping(value = "/delete-all", method = GET)
    @ResponseBody
    public void deleteAllDatabase() {
        zapScanService.deleteAllDatabase();
        System.out.println("Everything is deleted from database");
        for (ZAPScanResult z : zapScanService.findAll()) {
            System.out.println(z);
        }
        refreshAllScans();
        refreshStatus();
    }

    // With this GET request, related thread is stopped.
    @RequestMapping(value = "/kill-spider/{id}", method = GET)
    @ResponseBody
    public void killSpider(@PathVariable int id) {
        Spider s = spiders.get(id);
        if (s != null) {
            s.stop();
        }
    }

    @RequestMapping(value = "/delete/{id}", method = GET)
    @ResponseBody
    public void deleteFromDatabase(@PathVariable String id) {
        try {
            zapScanService.delete(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // With the help of this POST request, new attack is started to the given website.
    // This method returns, this attack's id and targetURL
    @PostMapping(value = "/create-scan/passive")
    public ResponseEntity<String> passiveScan(@RequestBody String newTarget) {
        updateFailedScan();
        newTarget = checkUrlValidity(newTarget);
        if (newTarget == null) {
            System.out.println("Wrong Url Format");
            return new ResponseEntity<>(
                    "Wrong Url Format is Given. Try again" + "\n", HttpStatus.OK
            );
        }
        // This block is only executed when first POST request is happened.
        // We can reach scanIdCounter data and updates it's value with our global variable.
        if (!databaseChecked) {
            try {
                ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById("0").get();
                scanIDCounter = Integer.parseInt(String.valueOf(zapScanResult.getSpider_id()));
            } catch (NoSuchElementException e) {
                addZAPScanResultToDatabase(new ZAPScanResult("0", 0, "nothing", "id_counter", "none", 0, 0, 0));
                System.out.println("id_counter is added to database");
            }
            databaseChecked = true;
        }
        scanIDCounter++;// id is incremented for new attack
        // Incremented scanId is saved to database for later attacks.
        try {
            ZAPScanResult id_counter = zapScanService.getZAPScanResultById("0").get();
            id_counter.setSpider_id(scanIDCounter);
            addZAPScanResultToDatabase(id_counter);
        } catch (NoSuchElementException e) {
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
        addZAPScanResultToDatabase(new ZAPScanResult(spider.getScanID(), scanIDCounter - 1, spider.getTARGET(), "", "Passive" , 0, 0, 0));
        refreshAllScans();
        refreshStatus();
        //  Spider thread is started and runs in the background.
        spider.setIsItActiveScan(false);
        spider.start();
        return new ResponseEntity<>(
                "Id:" + scanIDCounter + ", Passive Scan, targetURL:" + newTarget + "\n", HttpStatus.OK
        );
    }

    public String checkSearchIdValidity(String givenId) {
        givenId = givenId.replaceAll("\"", "");
        try {
            Integer.parseInt(givenId);
            return givenId;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String checkUrlValidity(String givenURL) {
        givenURL = givenURL.replaceAll("\"", "");
        if (givenURL.contains("http://") || givenURL.contains("https://")) {
            return givenURL;
        } else {
            return null;
        }
    }

    public ArrayList<String> checkSettingsInputValidity(String givenSettings) {
        int portNo;
        String apikey, address;
        givenSettings = givenSettings.replaceAll("\"", "");
        String[] settings = givenSettings.split(" ");
        try {
            portNo = Integer.parseInt(settings[0]);
            apikey = settings[1];
            address = settings[2];
            ArrayList<String> returnList = new ArrayList<>();
            returnList.add(String.valueOf(portNo));
            returnList.add(apikey);
            returnList.add(address);
            return returnList;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @PostMapping(value = "/search-by-id")
    public void searchById(@RequestBody String searchId) {
        searchId = checkSearchIdValidity(searchId);
        if (searchId == null) {
            System.out.println("Wrong Id Format is Given");
        }
        PrintWriter outputStream;
        try {
            Optional<ZAPScanResult> zapScanResult = zapScanService.getZAPScanResultById(searchId);
            String result = zapScanResult.get().getResult();
            result = result.replaceAll("\"", "\\\\\"");
            result = result.replaceAll("\n", "\\\\n");
            result = result.replaceAll("\t", "    ");
            outputStream = new PrintWriter(workingDirectory + "/src/main/ui/src/assets/JsonData/result-list.json");
            outputStream.print("[{ \"result\": \"" + result + "\"");
            outputStream.print("}]");
            outputStream.close();
        } catch (Exception e) {
            System.out.println("Search Id Not Found");
        }
        updateFailedScan();
    }

    @PostMapping(value = "/change-settings")
    public void changeSettings(@RequestBody String newSettings) {
        String apikey, address;
        int portNo;
        ArrayList<String> settings = checkSettingsInputValidity(newSettings);
        try {
            if (settings == null) {
                throw new Exception("Invalid Settings Input");
            }
            portNo = Integer.parseInt(settings.get(0));
            apikey = settings.get(1);
            address = settings.get(2);
            System.out.println("portNo:" + portNo + "\napiKey:" + apikey + "\naddress:" + address);
            this.ZAP_PORT = portNo;
            this.ZAP_API_KEY = apikey;
            this.ZAP_ADDRESS = address;
        } catch (Exception e) {
            System.out.println("Invalid settings input");
            e.printStackTrace();
        }
        updateFailedScan();
    }

    // With the help of this POST request, new attack is started to the given website.
    // This method returns, this attack's id and targetURL
    @PostMapping(value = "/create-scan/active")
    public ResponseEntity<String> activeScan(@RequestBody String newTarget) {
        updateFailedScan();
        newTarget = checkUrlValidity(newTarget);
        if (newTarget == null) {
            System.out.println("Wrong Url Format");
            return new ResponseEntity<>(
                    "Wrong Url Format is Given. Try again" + "\n", HttpStatus.OK
            );
        }
        // This block is only executed when first POST request is happened.
        // We can reach scanIdCounter data and updates it's value with our global variable.
        if (!databaseChecked) {
            try {
                ZAPScanResult zapScanResult = zapScanService.getZAPScanResultById("0").get();
                scanIDCounter = Integer.parseInt(String.valueOf(zapScanResult.getSpider_id()));
            } catch (NoSuchElementException e) {
                addZAPScanResultToDatabase(new ZAPScanResult("0", 0, "nothing", "id_counter", "Active", 0, 0, 0));
                System.out.println("id_counter is added to database");
            }
            databaseChecked = true;
        }
        scanIDCounter++;// id is incremented for new attack
        // Incremented scanId is saved to database for later attacks.
        try {
            ZAPScanResult id_counter = zapScanService.getZAPScanResultById("0").get();
            id_counter.setSpider_id(scanIDCounter);
            addZAPScanResultToDatabase(id_counter);
        } catch (NoSuchElementException e) {
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
        addZAPScanResultToDatabase(new ZAPScanResult(spider.getScanID(), scanIDCounter - 1, spider.getTARGET(), "", "Active", 0, 0, 0));
        refreshAllScans();
        refreshStatus();
        //  Spider thread is started and runs in the background.
        spider.setIsItActiveScan(true);
        spider.start();
        return new ResponseEntity<>(
                "Id:" + scanIDCounter + ", Active Scan, targetURL:" + newTarget + "\n", HttpStatus.OK
        );
    }

}
