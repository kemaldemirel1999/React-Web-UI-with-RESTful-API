/**
 * Copyright (c) 2016, STM A.Ş. All rights reserved.
 */

package tr.com.stm.cydecsys.zaprestapi.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

public class WebScanResultItem  {

	private static final long serialVersionUID = -5116443194313500020L;

	public interface Fields  {
		String name = "name";
		String tool = "tool";
		String severity = "severity";
		String confidince = "confidince";
		String request = "request";
		String response = "response";
		String scanDurationInSecond = "scanDurationInSecond";
		String detail = "detail";
		String vulnerabilities = "vulnerabilities";
		String weaknesses = "weaknesses";
		String solutions = "solutions";
		String references = "references";
		String webScanResultGuid = "webScanResultGuid";
		String relativePath = "relativePath";
		String recommendation = "recommendation";
		String targetGuid = "targetGuid";
		String relativeTargets = "relativeTargets";
		String remediationDetail = "remediationDetail";
		String remediationBackground = "remediationBackground";
	}

	public enum Severity  {
		LOW("Low"),
		MEDIUM("Medium"),
		HIGH("High"),
		INFORMATIONAL("Informational"),
		INFORMATION("Information");

		private String value;

		private Severity(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}


	}

	public enum Tool {
		ACUNETIX,
		BURP_SUITE;


	}

	public enum Confidince {
		CERTAIN("Certain"),
		FIRM("Firm"),
		TENTATIVE("Tentative");

		private String value;

		private Confidince(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	private String name;

	private Tool tool;

	private Severity severity;

	private Confidince confidince;

	private String request;

	private String response;

	private Long scanDurationInSecond;

	private Detail detail;

	private List<Vulnerability> vulnerabilities = new ArrayList<Vulnerability>();

	private List<Weakness> weaknesses = new ArrayList<Weakness>();

	private List<Solution> solutions = new ArrayList<Solution>();

	private List<Reference> references = new ArrayList<Reference>();

	private String webScanResultGuid;

	private String targetGuid;

	private List<RelativeTarget> relativeTargets = new ArrayList<RelativeTarget>();

	private String recommendation;
	private String remediationDetail;
	private String remediationBackground;

	public WebScanResultItem() {
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public Long getScanDurationInSecond() {
		return scanDurationInSecond;
	}

	public void setScanDurationInSecond(Long scanDurationInSecond) {
		this.scanDurationInSecond = scanDurationInSecond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public Confidince getConfidince() {
		return confidince;
	}

	public void setConfidince(Confidince confidince) {
		this.confidince = confidince;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}

	public List<Vulnerability> getVulnerabilities() {
		return vulnerabilities;
	}

	public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
		this.vulnerabilities = vulnerabilities;
	}

	public List<Weakness> getWeaknesses() {
		return weaknesses;
	}

	public void setWeaknesses(List<Weakness> weaknesses) {
		this.weaknesses = weaknesses;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public String getWebScanResultGuid() {
		return webScanResultGuid;
	}

	public void setWebScanResultGuid(String webScanResultGuid) {
		this.webScanResultGuid = webScanResultGuid;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getTargetGuid() {
		return targetGuid;
	}

	public void setTargetGuid(String targetGuid) {
		this.targetGuid = targetGuid;
	}

	public List<RelativeTarget> getRelativeTargets() {
		return relativeTargets;
	}

	public void setRelativeTargets(List<RelativeTarget> relativeTargets) {
		this.relativeTargets = relativeTargets;
	}

	public String getRemediationDetail() {
		return remediationDetail;
	}

	public void setRemediationDetail(String remediationDetail) {
		this.remediationDetail = remediationDetail;
	}

	public String getRemediationBackground() {
		return remediationBackground;
	}

	public void setRemediationBackground(String remediationBackground) {
		this.remediationBackground = remediationBackground;
	}

	public static class Detail  {

		private static final long serialVersionUID = -3840352587522664946L;

		private String details;

		private String impact;

		private String description;

		private String issueDetail;

		private String issueBackground;

//		private WebScanType webScanType;

		public interface Fields {
			String details = "details";
			String impact = "impact";
			String description = "description";
			String issueDetail = "issueDetail";
			String issueBackground = "issueBackground";
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public String getImpact() {
			return impact;
		}

		public void setImpact(String impact) {
			this.impact = impact;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getIssueDetail() {
			return issueDetail;
		}

		public void setIssueDetail(String issueDetail) {
			this.issueDetail = issueDetail;
		}

		public String getIssueBackground() {
			return issueBackground;
		}

		public void setIssueBackground(String issueBackground) {
			this.issueBackground = issueBackground;
		}

//		public WebScanType getWebScanType() {
//			return webScanType;
//		}

//		public void setWebScanType(WebScanType webScanType) {
//			this.webScanType = webScanType;
//		}

	}

	public static class Vulnerability  {

		public Vulnerability(String vulnName, String vulnTitle) {
			super();
			this.vulnName = vulnName;
			this.vulnTitle = vulnTitle;
		}

		private static final long serialVersionUID = 4577830021282961936L;

		private String vulnName;

		private String vulnTitle;

		public interface Fields  {
			String vulnName = "vulnName";
			String vulnTitle = "vulnTitle";
		}

		public String getVulnName() {
			return vulnName;
		}

		public void setVulnName(String vulnName) {
			this.vulnName = vulnName;
		}

		public String getVulnTitle() {
			return vulnTitle;
		}

		public void setVulnTitle(String vulnTitle) {
			this.vulnTitle = vulnTitle;
		}
	}

	public static class Weakness  {

		public Weakness(String weaknessName, String weaknessTitle) {
			super();
			this.weaknessName = weaknessName;
			this.weaknessTitle = weaknessTitle;
		}

		private static final long serialVersionUID = 7746853038735089764L;

		private String weaknessName;

		private String weaknessTitle;

		public interface Fields  {
			String weaknessName = "weaknessName";
			String weaknessTitle = "weaknessTitle";
		}

		public String getWeaknessName() {
			return weaknessName;
		}

		public void setWeaknessName(String weaknessName) {
			this.weaknessName = weaknessName;
		}

		public String getWeaknessTitle() {
			return weaknessTitle;
		}

		public void setWeaknessTitle(String weaknessTitle) {
			this.weaknessTitle = weaknessTitle;
		}
	}

	public static class Solution  {

		private static final long serialVersionUID = 5649894976437959528L;

		private String recommendation;

		private String remediationDetail;

		private String remediationBackground;

		public interface Fields  {
			String recommendation = "recommendation";
			String remediationDetail = "remediationDetail";
			String remediationBackground = "remediationBackground";
		}

		public String getRecommendation() {
			return recommendation;
		}

		public void setRecommendation(String recommendation) {
			this.recommendation = recommendation;
		}

		public String getRemediationDetail() {
			return remediationDetail;
		}

		public void setRemediationDetail(String remediationDetail) {
			this.remediationDetail = remediationDetail;
		}

		public String getRemediationBackground() {
			return remediationBackground;
		}

		public void setRemediationBackground(String remediationBackground) {
			this.remediationBackground = remediationBackground;
		}
	}

	public static class Reference  {

		private static final long serialVersionUID = 7691860555696369813L;

		private String database;

		private String url;

		public interface Fields  {
			String database = "database";
			String url = "url";
		}

		public String getDatabase() {
			return database;
		}

		public void setDatabase(String database) {
			this.database = database;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	public static class RelativeTarget  {

		private static final long serialVersionUID = 4896303961021955070L;

		private String url;

		private String request;

		private String response;

		public interface Fields  {
			String url = "url";
			String request = "request";
			String response = "response";
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getRequest() {
			return request;
		}

		public void setRequest(String request) {
			this.request = request;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

	}

}
