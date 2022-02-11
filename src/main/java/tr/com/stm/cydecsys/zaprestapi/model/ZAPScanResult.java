package tr.com.stm.cydecsys.zaprestapi.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class ZAPScanResult {

    @Id
    @Column
    private int scan_id;
    @Column
    private int spider_id;
    @Column
    private String targetURL;
    @Column(length = 999999999)
    private String result;

    public ZAPScanResult(int id, int spider_id, String url, String result)
    {
        this.scan_id = id;
        this.spider_id = spider_id;
        this.targetURL = url;
        this.result = result;
    }
    public ZAPScanResult()
    {}

    public void setScanId(int id) {
        this.scan_id = id;
    }
    public int getScanId() {
        return this.scan_id;
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

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if(!(o instanceof ZAPScanResult)){
            return false;
        }
        ZAPScanResult url = (ZAPScanResult) o;
        return Objects.equals(this.scan_id, url.scan_id) && Objects.equals(this.targetURL, url.targetURL);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.scan_id, this.targetURL);
    }
    @Override
    public String toString() {
        return "id:"+scan_id +"\nurl:"+targetURL+"\nspider_id:"+spider_id+"\nresult:"+result+"\n";
    }
}
