package tr.com.stm.cydecsys.zaprestapi.parser;

import java.util.ArrayList;
import java.util.List;

public class WebScannerResult {

	private WebScannerMetaData metadata = new WebScannerMetaData();
	private List<WebScannerSingleResult> resultItems = new ArrayList<WebScannerSingleResult>();

	public WebScannerMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(WebScannerMetaData metadata) {
		this.metadata = metadata;
	}

	public List<WebScannerSingleResult> getResultItems() {
		return resultItems;
	}

	public void setResultItems(List<WebScannerSingleResult> resultItems) {
		this.resultItems = resultItems;
	}
}
