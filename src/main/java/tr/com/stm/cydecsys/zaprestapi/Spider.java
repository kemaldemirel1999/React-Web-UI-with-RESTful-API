package tr.com.stm.cydecsys.zaprestapi;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;


import java.util.List;

public class Spider {

    private   String ZAP_ADDRESS = "localhost";
    private   int ZAP_PORT = 8090;
    private   String ZAP_API_KEY = "hc9fl5vmd1bsmoc0qo2u8hjn7c";
    private   String TARGET = "http://scanme.nmap.org/";

    private String scanID;
    private List<ApiResponse> spiderResults;
    public List<ApiResponse> getSpiderResults(){
        if (spiderResults != null)  return spiderResults;
        else                        return null;
    }
    public String getScanID(){
        return scanID;
    }

    public String runSpider() {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        try {
            System.out.println("Spidering target : " + TARGET);
            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
            int progress;
            scanID = ((ApiResponseElement) resp).getValue();
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanID)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider completed");
            spiderResults = ((ApiResponseList) api.spider.results(scanID)).getItems();
            // TODO: Explore the Application more with Ajax Spider or Start scanning the application for vulnerabilities
            return scanID;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
