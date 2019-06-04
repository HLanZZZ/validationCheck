package com.accenture.aesrefund.callback;


import com.amadeus.xml.tcdres_09_1_1a.TicketProcessEDocCCConcealedReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.support.MarshallingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Iterator;

import static org.jvnet.jaxb2_commons.xmlschema.XmlSchemaConstants.DURATION;


public class TicketProcessEDocReplyCallBack implements WebServiceMessageCallback {
  private static final String CONTEXT_PATH = "com.amadeus.xml.tcdres_09_1_1a";

  public static final String HEADER_SESSION_NS = "http://xml.amadeus.com/TCDRES_09_1_1A";
  public static final String HEADER_SESSION_NAME = "Session";
  public static final String HEADER_SESSION_ID_NAME = "SessionId";
  public static final String HEADER_SECURITY_TOKEN_NAME = "SecurityToken";
  public static final String HEADER_SEQUENCE_NUMBER_NAME = "SequenceNumber";
  
  private static Logger logger = LoggerFactory.getLogger(TicketProcessEDocReplyCallBack.class);
  private TicketProcessEDocCCConcealedReply response;

  private String sessionId;
  private BigInteger sequenceNumber;
  private String securityToken;
  private String enableObjConversion;
  
  public TicketProcessEDocReplyCallBack(String enableObjConversion)
  {
	  this.enableObjConversion=enableObjConversion;
  }

  @Override
  public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
    StringWriter output = new StringWriter();
    SaajSoapMessage soapMessage = (SaajSoapMessage) message;
    Document doc = soapMessage.getDocument();
    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));

    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath(CONTEXT_PATH);
    long startTime = System.currentTimeMillis();
   // logger.tag("FREE_TEXT",startTime).info("Before Transform");
    response = (TicketProcessEDocCCConcealedReply)MarshallingUtils.unmarshal(marshaller, message);
    System.out.println(response);
    if(enableObjConversion.equals("TRUE"))
        logger.info(String.valueOf(DURATION), new long[]{System.currentTimeMillis() - startTime});
    ByteArrayOutputStream out = new ByteArrayOutputStream();
	message.writeTo(out);
	logger.info("FREE_TEXT",out.toString(),"TICKET-PROCESS-EDOC RETREIVE RESPONSE");
    SoapHeader soapHeader = soapMessage.getSoapHeader();
    this.extractSessionInformation(soapHeader);
}

  private void extractSessionInformation(SoapHeader soapHeader) {
    Iterator<SoapHeaderElement> soapHeaderElementIterator = soapHeader.examineHeaderElements(new QName(HEADER_SESSION_NS, HEADER_SESSION_NAME));

    if (soapHeaderElementIterator.hasNext()) {
      SoapHeaderElement element = soapHeaderElementIterator.next();
      DOMResult domResult = (DOMResult)element.getResult();

      NodeList nodeList = domResult.getNode().getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        String nodeName = node.getLocalName();
        String nodeValue = node.getTextContent();

        if (HEADER_SESSION_ID_NAME.equals(nodeName)) {
          this.sessionId = nodeValue;
        } else if (HEADER_SECURITY_TOKEN_NAME.equals(nodeName)) {
          this.securityToken = nodeValue;
        } else if (HEADER_SEQUENCE_NUMBER_NAME.equals(nodeName)) {
          this.sequenceNumber = new BigInteger(nodeValue);
        }
      }
    }
  }
  public TicketProcessEDocCCConcealedReply getResponse() {
    return response;
  }

  public void setResponse(TicketProcessEDocCCConcealedReply response) {
    this.response = response;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public BigInteger getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(BigInteger sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getSecurityToken() {
    return securityToken;
  }

  public void setSecurityToken(String securityToken) {
    this.securityToken = securityToken;
  }

}
