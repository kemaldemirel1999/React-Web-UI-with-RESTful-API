/**
 * Copyright (c) 2016, STM A.Ş. All rights reserved.
 */

package tr.com.stm.cydecsys.zaprestapi.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

public class WebScanResult  {

	private static final long serialVersionUID = 4296381495829463222L;

	public interface Fields  {
		String status = "status";
		String tool = "tool";
		String importTime = "importTime";
		String exportTime = "exportTime";
		String scanStartTime = "scanStartTime";
		String scanFinishTime = "scanFinishTime";
		String scanTime = "scanTime";
		String startUrl = "startUrl";
		String relativeTargets = "relativeTargets";
		String systemLoadTime = "systemLoadTime";
	}

	public enum Status  {
		SUCCESS,
		FAIL;


	}

	public enum Tool  {
		ACUNETIX,
		BURP_SUITE;


	}

	private Status status;

	private Tool tool;

	private Date importTime;

	private Date exportTime;

	private Date scanStartTime;

	private Date scanFinishTime;

	private Date systemLoadTime;

	private String scanTime;

	private List<Target> targets = new ArrayList<Target>();

	private String startUrl;

	private String shortName;

	@Transient
	private List<WebScanResultItem> webScanResultItems = new ArrayList<WebScanResultItem>();

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public Date getExportTime() {
		return exportTime;
	}

	public void setExportTime(Date exportTime) {
		this.exportTime = exportTime;
	}

	public Date getScanStartTime() {
		return scanStartTime;
	}

	public void setScanStartTime(Date scanStartTime) {
		this.scanStartTime = scanStartTime;
	}

	public Date getScanFinishTime() {
		return scanFinishTime;
	}

	public void setScanFinishTime(Date scanFinishTime) {
		this.scanFinishTime = scanFinishTime;
	}

	public Date getSystemLoadTime() {
		return systemLoadTime;
	}

	public void setSystemLoadTime(Date systemLoadTime) {
		this.systemLoadTime = systemLoadTime;
	}

	public List<WebScanResultItem> getWebScanResultItems() {
		return webScanResultItems;
	}

	public void setWebScanResultItems(List<WebScanResultItem> webScanResultItems) {
		this.webScanResultItems = webScanResultItems;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
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

	public static class Target  {

		private static final long serialVersionUID = -8902432354577209662L;

		public interface Fields  {
			String url = "url";
			String detectionCount = "detectionCount";
			String detectionCountBySeverityHigh = "detectionCountBySeverityHigh";
			String detectionCountBySeverityMedium = "detectionCountBySeverityMedium";
			String ip = "ip";
			String guid = "guid";
		}

		private String url;

		private Integer detectionCount = new Integer(0);

		private Integer detectionCountBySeverityHigh = new Integer(0);

		private Integer detectionCountBySeverityMedium = new Integer(0);

		private String ip;

		public enum Severity  {
			LOW("Low"),
			MEDIUM("Medium"),
			HIGH("High");

			private String value;

			private Severity(String value) {
				this.value = value;
			}

			public String getValue() {
				return this.value;
			}


		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Integer getDetectionCount() {
			return detectionCount;
		}

		public void setDetectionCount(Integer detectionCount) {
			this.detectionCount = detectionCount;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		@Override
		public int hashCode() {
			return this.url.hashCode();
		}


		public Integer getDetectionCountBySeverityHigh() {
			return detectionCountBySeverityHigh;
		}

		public void setDetectionCountBySeverityHigh(Integer detectionCountBySeverityHigh) {
			this.detectionCountBySeverityHigh = detectionCountBySeverityHigh;
		}

		public Integer getDetectionCountBySeverityMedium() {
			return detectionCountBySeverityMedium;
		}

		public void setDetectionCountBySeverityMedium(Integer detectionCountBySeverityMedium) {
			this.detectionCountBySeverityMedium = detectionCountBySeverityMedium;
		}

	}

}
