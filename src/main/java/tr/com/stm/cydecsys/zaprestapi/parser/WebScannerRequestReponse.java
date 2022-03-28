package tr.com.stm.cydecsys.zaprestapi.parser;

public class WebScannerRequestReponse {
	
	private String relaviteURL; // TODO [hidirik] Acunetix(ReportItem.Affect) Burp(issue.path)
	private String request;
	private String response;
	private boolean responseRedirected;

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

	public boolean isResponseRedirected() {
		return responseRedirected;
	}

	public void setResponseRedirected(boolean responseRedirected) {
		this.responseRedirected = responseRedirected;
	}

	public String getRelaviteURL() {
		return relaviteURL;
	}

	public void setRelaviteURL(String relaviteURL) {
		this.relaviteURL = relaviteURL;
	}
}
