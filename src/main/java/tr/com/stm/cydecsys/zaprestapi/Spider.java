package tr.com.stm.cydecsys.zaprestapi;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;
import tr.com.stm.cydecsys.zaprestapi.model.ZAPScanResult;


import java.util.List;

public class Spider extends Thread{

    private String ZAP_ADDRESS = "localhost";
    private int ZAP_PORT = 8090;
    private String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private String TARGET = "http://scanme.nmap.org/";

    private boolean isScanSuccessful= false;
    private String spiderId = null;
    private List<ApiResponse> spiderResults;
    private boolean isPassiveScanFinished = false;
    private String passiveScanResults = "";
    private PassiveScan passiveScan;
    private int id = -1;

    public List<ApiResponse> getSpiderResults(){return spiderResults;}
    public void setTARGET(String target){
        this.TARGET = target;
    }
    public void setZAP_PORT(int port){
        ZAP_PORT = port;
    }
    public boolean IsPassiveScanFinished(){
        return isPassiveScanFinished;
    }
    public void setZAP_ADDRESS(String address){
        this.ZAP_ADDRESS = address;
    }
    public void setZAP_API_KEY(String api_key){
        this.ZAP_API_KEY = api_key;
    }
    public String getPassiveScanResults(){
        return passiveScanResults;
    }
    public void setScanID(int id){
        this.id = id;
    }
    public int getScanID(){
        return id;
    }
    public int getPassiveScanNumberOfRecords(){
        return passiveScan.getNumberOfRecords();
    }
    public String getSpiderID(){
        return spiderId;
    }

    public boolean getScanState(){
        return isScanSuccessful;
    }
    public String runSpider() {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        try {
//            System.out.println("Spidering target : " + TARGET);
            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
            int progress;
            spiderId = ((ApiResponseElement) resp).getValue();
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(spiderId)).getValue());
//                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
//            System.out.println("Spider completed");
            spiderResults = ((ApiResponseList) api.spider.results(spiderId)).getItems();
            isScanSuccessful = true;
            // TODO: Explore the Application more with Ajax Spider or Start scanning the application for vulnerabilities
            return spiderId;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        isScanSuccessful = false;
        return null;
    }

    public void run() {
        runSpider();
        passiveScan = new PassiveScan();
        passiveScanResults = passiveScan.runPassiveScan();
        isPassiveScanFinished = true;
        ZAPScanResult addingZAPScanResult = new ZAPScanResult(getScanID(), Integer.parseInt(getSpiderID()), this.TARGET, getPassiveScanResults());
    }
}
