package tr.com.stm.cydecsys.zaprestapi.parser;

import java.util.ArrayList;
import java.util.List;


public class WebScannerSingleResult {

	private String issueName; // Acunetix(ReportItem.Name) and Burp(issue.name) - bulgular paneli
	private String issueDetail;
	private String issueBackground;

	private String severity;
	private List<WebScanResultItem.Reference> references = new ArrayList<>(); // single item in burp, multiple in acunetix
	private List<WebScannerRequestReponse> requestResponse = new ArrayList<>(); // burp has a
	// boolean value
	private String vulnerabilityClassification; // cwe in acunetix

	private String description;
	private String impact;
	private String detail;

	private String confidince;
	private List<String> cveList = new ArrayList<String>(); // multiple in acunetix

	private String relativePath;

	// acunetix
	private String recommendation;

	// burp
	private String remediationBackground;
	private String remediationDetail;

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public List<WebScanResultItem.Reference> getReferences() {
		return references;
	}

	public void setReferences(List<WebScanResultItem.Reference> references) {
		this.references = references;
	}

	public String getIssueDetail() {
		return issueDetail;
	}

	public void setIssueDetail(String issueDetail) {
		this.issueDetail = issueDetail;
	}

	public List<WebScannerRequestReponse> getRequestResponse() {
		return requestResponse;
	}

	public void setRequestResponse(List<WebScannerRequestReponse> requestResponse) {
		this.requestResponse = requestResponse;
	}

	public String getVulnerabilityClassification() {
		return vulnerabilityClassification;
	}

	public void setVulnerabilityClassification(String vulnerabilityClassification) {
		this.vulnerabilityClassification = vulnerabilityClassification;
	}

	public String getIssueBackground() {
		return issueBackground;
	}

	public void setIssueBackground(String issueBackground) {
		this.issueBackground = issueBackground;
	}

	public String getRecommendation() {
		if (recommendation != null || !recommendation.isEmpty()) {
			return recommendation;
		} else {
			return remediationBackground + remediationDetail;
		}
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public void setRemediationBackground(String remediationBackground) {
		this.remediationBackground = remediationBackground;
	}

	public void setRemediationDetail(String remediationDetail) {
		this.remediationDetail = remediationDetail;
	}

	public String getConfidince() {
		return confidince;
	}

	public void setConfidince(String confidince) {
		this.confidince = confidince;
	}

	public List<String> getCveList() {
		return cveList;
	}

	public void setCveList(List<String> cveList) {
		this.cveList = cveList;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getRemediationBackground() {
		return remediationBackground;
	}

	public String getRemediationDetail() {
		return remediationDetail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}

enum Severity {
	HIGH,
	MEDIUM,
	LOW,
	INFORMATION,
	INFORMATIONAL;// acunetix
}
