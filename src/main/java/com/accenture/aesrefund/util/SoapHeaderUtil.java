package com.accenture.aesrefund.util;

import com.accenture.aesrefund.common.ApplicationException;
import com.accenture.aesrefund.common.ErrorResponseException;
import com.accenture.aesrefund.config.AmadeusServiceConfig;
import com.accenture.aesrefund.model.AmadeusSecurityInfo;


import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;


public class SoapHeaderUtil {
	  public static final String TRANSACTION_CODE_START = "Start";
	  public static final String TRANSACTION_CODE_INSERIES = "InSeries";
	  public static final String TRANSACTION_CODE_END = "End";

	  private SoapHeaderUtil() {}
			  
			  public static void generateTicketSoapHeader(SaajSoapMessage soapMessage, AmadeusServiceConfig amadeusServiceConfig) throws ApplicationException {   //, String officeID
				    try {
				      SOAPEnvelope soapEnvelope = soapMessage.getSaajMessage().getSOAPPart().getEnvelope();
				      soapEnvelope.addNamespaceDeclaration("wsa", "http://www.w3.org/2005/08/addressing");
				      soapEnvelope.addNamespaceDeclaration("ses", "http://xml.amadeus.com/2010/06/Session_v3");
				      soapEnvelope.addNamespaceDeclaration("sec", "http://xml.amadeus.com/2010/06/Security_v1");
				      soapEnvelope.addNamespaceDeclaration("typ", "http://xml.amadeus.com/2010/06/Types_v1");
				      soapEnvelope.addNamespaceDeclaration("iat", "http://www.iata.org/IATA/2007/00/IATA2010.1");
				      soapEnvelope.addNamespaceDeclaration("wsse",
				          "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
				      soapEnvelope.addNamespaceDeclaration("wsu",
				          "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
				      SOAPHeader headerElement = soapEnvelope.getHeader();

				      // AMA_SecurityHostedUser
				      SOAPElement securityHostedUserElement = headerElement.addChildElement("AMA_SecurityHostedUser", "sec");
				      SOAPElement userIDElement = securityHostedUserElement.addChildElement("UserID", "sec");
				      userIDElement.setAttribute("POS_Type", amadeusServiceConfig.getPosType());
				      userIDElement.setAttribute("RequestorType", amadeusServiceConfig.getRequestorType());
				      userIDElement.setAttribute("AgentDutyCode", amadeusServiceConfig.getAgentDutyCode());
//				      userIDElement.setAttribute("PseudoCityCode", officeID);
				      SOAPElement requestorIDElement = userIDElement.addChildElement("RequestorID", "typ");
				      requestorIDElement.addChildElement("CompanyName", "iat").setValue(amadeusServiceConfig.getCompanyName());

				      // -------Security Element
				      // get Security Information
				      AmadeusSecurityInfo securityInfo = SecurityUtil.generateSecurityInfo(amadeusServiceConfig.getWssPassword());

				      SOAPElement securityElement = soapEnvelope.getHeader().addChildElement("Security", "wsse");
				      SOAPElement usernameTokenElement = securityElement.addChildElement("UsernameToken", "wsse");

				      // Username
				      usernameTokenElement.addChildElement("Username", "wsse").setValue(amadeusServiceConfig.getWssUsername());

				      // password
				      SOAPElement passwordElement = usernameTokenElement.addChildElement("Password", "wsse");
				      passwordElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
				      passwordElement.setValue(securityInfo.getPasswdDigest());

				      // Nonce
				      SOAPElement nonceElement = usernameTokenElement.addChildElement("Nonce", "wsse");
				      nonceElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
				      nonceElement.setValue(securityInfo.getNonce());
				      // Created
				      usernameTokenElement.addChildElement("Created", "wsu").setValue(securityInfo.getCreated());

				    } catch (Exception e) {
				      throw new ApplicationException(e.getMessage(), ErrorResponseException.ERR_CONSTRUCT_REQUEST_HEADER);
				    }
				  }
			  
	}


