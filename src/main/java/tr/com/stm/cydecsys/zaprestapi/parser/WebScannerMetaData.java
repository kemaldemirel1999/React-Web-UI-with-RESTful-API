package tr.com.stm.cydecsys.zaprestapi.parser;

public class WebScannerMetaData {

	private WebScannerType scannerType;
	private String exportTime;
	private String startTime;
	private String endTime;
	private String scanTime;
	private String startUrl;
	private String shortName;

	public WebScannerType getScannerType() {
		return scannerType;
	}

	public void setScannerType(WebScannerType scannerType) {
		this.scannerType = scannerType;
	}

	public String getExportTime() {
		return exportTime;
	}

	public void setExportTime(String exportTime) {
		this.exportTime = exportTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getScanTime() {
		return scanTime;
	}

	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}

}