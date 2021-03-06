package tr.com.stm.cydecsys.zaprestapi.model;

import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
import java.util.Objects;

/*
    This is a object that will be saved to database.
*/
@Document(collection = "ZAPScanResults")
public class ZAPScanResult {

    // id is our primary key
    @Id
    private String id;

    private int spider_id;
    private String targetURL;
    private String result;
    private String scanType;
    private int highRisk;
    private int mediumRisk;
    private int lowRisk;

    public ZAPScanResult(String id, int spider_id, String url, String result, String scanType, int highRisk, int mediumRisk, int lowRisk)
    {
        this.id = id;
        this.spider_id = spider_id;
        this.targetURL = url;
        this.result = result;
        this.scanType = scanType;
        this.highRisk = highRisk;
        this.mediumRisk = mediumRisk;
        this.lowRisk = lowRisk;
    }
    public ZAPScanResult(){}
    public void setScanId(String id) {
        this.id = id;
    }
    public String getScanId() {
        return this.id;
    }
    public String getUrl(){
        return targetURL;
    }
    public void setUrl(String url){
        this.targetURL = url;
    }
    public String getResult(){
        return result;
    }
    public void setResult(String result){
        this.result = result;
    }
    public int getSpider_id(){
        return spider_id;
    }
    public void setSpider_id(int spider_id){
        this.spider_id = spider_id;
    }
    public String getScanType(){
        return scanType;
    }
    public void setScanType(String scanType){
        this.scanType = scanType;
    }
    public void setHighRisk(int highRisk){this.highRisk = highRisk;}
    public void setMediumRisk(int mediumRisk){this.mediumRisk = mediumRisk;}
    public void setLowRisk(int lowRisk){this.lowRisk = lowRisk;}
    public int getHighRisk(){return  highRisk;}
    public int getLowRisk(){return lowRisk;}
    public int getMediumRisk(){return mediumRisk;}

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if(!(o instanceof ZAPScanResult)){
            return false;
        }
        ZAPScanResult url = (ZAPScanResult) o;
        return Objects.equals(this.id, url.id) && Objects.equals(this.targetURL, url.targetURL);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.targetURL);
    }
    @Override
    public String toString() {
        return "{\nid:"+id +"\nurl:"+targetURL+"\nspider_id:"+spider_id+"\nresult:"+result+"\n}\n";
    }
}
