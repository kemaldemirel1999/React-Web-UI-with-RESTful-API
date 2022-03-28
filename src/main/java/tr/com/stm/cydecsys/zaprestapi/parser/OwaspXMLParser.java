package tr.com.stm.cydecsys.zaprestapi.parser;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OwaspXMLParser {

    private static final String OWASP_DETECTION_STRING = "<OWASPZAPReport";

    private static final String OWASPZAP_REPORT = "OWASPZAPReport";
    private static final String GENERATED = "generated";
    private static final String ALERT_ITEM = "alertitem";
    private static final String SITE = "site";
    private static final String NAME = "name";
    private static final String HOST = "host";
    private static final String DESC = "desc";
    private static final String SOLUTION = "solution";
    private static final String REFERENCE = "reference";
    private static final String CWEID = "cweid";
    private static final String RISKDESC = "riskdesc";
    private static final String INSTANCE = "instance";
    private static final String URI = "uri";

    public WebScannerResult parseOwaspReport(String rawReport) {
        WebScannerResult scannerResult = new WebScannerResult();
        // scannerResult.getMetadata().setScannerType(WebScannerType.OWASP);

        OwaspReportItem reportItem = new OwaspReportItem();
        WebScannerRequestReponse requestResponse = new WebScannerRequestReponse();

        rawReport = xmlSpecialCharRemover(rawReport);

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new StringReader(rawReport));

            boolean nameBool = false;
            boolean descBool = false;
            boolean solutionBool = false;
            boolean referenceBool = false;
            boolean cweBool = false;
            boolean riskDescBool = false;
            boolean uriBool = false;

            StringBuilder sb = new StringBuilder();

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase(OWASPZAP_REPORT)) {
                            @SuppressWarnings("unchecked")
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equalsIgnoreCase(GENERATED)) {
                                scannerResult.getMetadata().setExportTime(attribute.getValue());
                            }
                        } else if (qName.equalsIgnoreCase(ALERT_ITEM)) {
                            reportItem = new OwaspReportItem();
                        } else if (qName.equalsIgnoreCase(SITE)) {
                            @SuppressWarnings("unchecked")
                            Iterator<Attribute> attributes = startElement.getAttributes();
                            while (attributes.hasNext()) {
                                Attribute attribute = attributes.next();
                                if (attribute.getName().toString().equalsIgnoreCase(NAME)) {
                                    scannerResult.getMetadata().setStartUrl(attribute.getValue());
                                } else if (attribute.getName().toString().equalsIgnoreCase(HOST)) {
                                    scannerResult.getMetadata().setShortName(attribute.getValue());
                                }
                            }
                        } else if (qName.equalsIgnoreCase(NAME)) {
                            nameBool = true;
                        } else if (qName.equalsIgnoreCase(DESC)) {
                            descBool = true;
                        } else if (qName.equalsIgnoreCase(SOLUTION)) {
                            solutionBool = true;
                        } else if (qName.equalsIgnoreCase(REFERENCE)) {
                            referenceBool = true;
                        } else if (qName.equalsIgnoreCase(CWEID)) {
                            cweBool = true;
                        } else if (qName.equalsIgnoreCase(RISKDESC)) {
                            riskDescBool = true;
                        } else if (qName.equalsIgnoreCase(INSTANCE)) {
                            requestResponse = new WebScannerRequestReponse();
                        } else if (qName.equalsIgnoreCase(URI)) {
                            uriBool = true;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        String str = helper(characters.toString());
                        if (nameBool) {
                            nameBool = false;
                            reportItem.setIssueName(str);
                        } else if (descBool) {
                            sb.append(str);
                        } else if (solutionBool) {
                            sb.append(str);
                        } else if (referenceBool) {
                            sb.append(str);
                        } else if (cweBool) {
                            cweBool = false;
                            reportItem.setVulnerabilityClassification(str);
                        } else if (riskDescBool) {
                            riskDescBool = false;
                            reportItem.setConfidince(str.substring(0, str.indexOf(" ")));
                            reportItem.setSeverity(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
                        } else if (uriBool) {
                            uriBool = false;
                            requestResponse.setRelaviteURL(scannerResult.getMetadata().getStartUrl());
                            requestResponse.setRequest(str);
                        }

                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String endName = endElement.getName().getLocalPart();

                        if (endName.equalsIgnoreCase(ALERT_ITEM)) {
                            scannerResult.getResultItems().add(reportItem);
                        } else if (endName.equalsIgnoreCase(INSTANCE)) {
                            reportItem.getRequestResponse().add(requestResponse);
                        } else if (endName.equalsIgnoreCase(SOLUTION)) {
                            solutionBool = false;
                            reportItem.setRecommendation(sb.toString());
                            sb = new StringBuilder();
                        } else if (endName.equalsIgnoreCase(REFERENCE)) {
                            referenceBool = false;
                            reportItem.setReferences(extractReferences(sb.toString()));
                            sb = new StringBuilder();
                        } else if (endName.equalsIgnoreCase(DESC)) {
                            descBool = false;
                            reportItem.setDescription(sb.toString());
                            sb = new StringBuilder();
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {

        }

        return scannerResult;
    }

    public static boolean isParsable(String rawReport) {
        return rawReport.contains(OWASP_DETECTION_STRING);
    }

    private List<WebScanResultItem.Reference> extractReferences(String str) {
        List<WebScanResultItem.Reference> list = new ArrayList<>();
        str = str.replaceAll("<p>", "");
        String[] split = str.split("</p>");
        for (String s : split) {
            WebScanResultItem.Reference ref = new WebScanResultItem.Reference();
            ref.setUrl(s);
            list.add(ref);
        }
        return list;
    }

    private static String xmlSpecialCharRemover(String rawReport) {
        rawReport = rawReport.replaceAll("&apos;", "special_char_1"); // '
        rawReport = rawReport.replaceAll("&amp;", "special_char_2"); // &
        rawReport = rawReport.replaceAll("&quot;", "special_char_3"); // "

        return rawReport;
    }

    private static String helper(String str) {
        return str.replaceAll("special_char_1", "\'").replaceAll("special_char_2", "&").replaceAll("special_char_3", "\"");
    }
}