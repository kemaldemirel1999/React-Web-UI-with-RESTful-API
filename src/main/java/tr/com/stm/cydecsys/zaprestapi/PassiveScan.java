package tr.com.stm.cydecsys.zaprestapi;

import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

import java.nio.charset.StandardCharsets;

public class PassiveScan{

    private int ZAP_PORT = 8090;
    private String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private String ZAP_ADDRESS = "localhost";
    // Represents number of records left for scanning.
    private int numberOfRecords = -1;

    public void setZAP_PORT(int port){
        ZAP_PORT = port;
    }
    public void setZAP_ADDRESS(String address){
        this.ZAP_ADDRESS = address;
    }
    public void setZAP_API_KEY(String api_key){
        this.ZAP_API_KEY = api_key;
    }
    public int getNumberOfRecords(){
        return numberOfRecords;
    }

    public String runPassiveScan() {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        try {
            while (true) {
                Thread.sleep(2000);
                api.pscan.recordsToScan();
                numberOfRecords = Integer.parseInt(((ApiResponseElement) api.pscan.recordsToScan()).getValue());
                if (numberOfRecords == 0) {
                    // We cam finish scan.
                    break;
                }
            }
            return ( new String(api.core.xmlreport(), StandardCharsets.UTF_8) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
