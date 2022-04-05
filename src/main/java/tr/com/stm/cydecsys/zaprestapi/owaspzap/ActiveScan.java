package tr.com.stm.cydecsys.zaprestapi.owaspzap;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import tr.com.stm.cydecsys.zaprestapi.ExecuteBashCommand;

import java.nio.charset.StandardCharsets;

public class ActiveScan {

    private  int ZAP_PORT = 8091;
    private  String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private  String ZAP_ADDRESS = "localhost";
    private  String TARGET = "http://scanme.nmap.org/";
    private int progress = -1;

    public void setZAP_PORT(int port){
        ZAP_PORT = port;
    }
    public void setZAP_ADDRESS(String address){
        ZAP_ADDRESS = address;
    }
    public void setZAP_API_KEY(String api_key){
        ZAP_API_KEY = api_key;
    }
    public void setTARGET(String target){
        TARGET = target;
    }
    public int getActiveScanProgress(){
        return this.progress;
    }

    public String runActiveScan () {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        try {
            System.out.println("Active Scanning target : " + TARGET);
            ApiResponse resp = api.ascan.scan(TARGET, "True", "False", null, null, null);
            String scanid;
            scanid = ((ApiResponseElement) resp).getValue();
            while (true) {
                Thread.sleep(5000);
                progress =
                        Integer.parseInt(
                                ((ApiResponseElement) api.ascan.status(scanid)).getValue());
                if (progress >= 100) {
                    break;
                }
            }
            return new String(api.core.xmlreport(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
