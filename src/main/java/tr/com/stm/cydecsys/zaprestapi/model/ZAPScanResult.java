package tr.com.stm.cydecsys.zaprestapi.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class ZAPScanResult {

    @Id
    @Column
    private int id;
    @Column
    private int spider_id;
    @Column
    private String targetURL;
    @Column
    private String result;

    public ZAPScanResult(int id, int spider_id, String url, String result)
    {
        this.id = id;
        this.spider_id = spider_id;
        this.targetURL = url;
        this.result = result;
    }
    public ZAPScanResult()
    {}

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
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
        return "id:"+id +"\nurl:"+targetURL+"\nspider_id:"+spider_id+"\nresult:"+result+"\n";
    }
}
