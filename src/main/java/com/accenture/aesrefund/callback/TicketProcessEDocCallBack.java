package com.accenture.aesrefund.callback;

import com.amadeus.xml.tcdreq_09_1_1a.TicketProcessEDocCCConcealed;
import com.accenture.aesrefund.common.ApplicationException;
import com.accenture.aesrefund.common.ErrorResponseException;
import com.accenture.aesrefund.config.AmadeusServiceConfig;
import com.accenture.aesrefund.util.SoapHeaderUtil;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.addressing.client.ActionCallback;
import org.springframework.ws.soap.addressing.version.Addressing10;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.support.MarshallingUtils;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;


public class TicketProcessEDocCallBack extends FirstTransactionCallBack implements WebServiceMessageCallback {
	private static final String CONTEXT_PATH = "com.amadeus.xml.tcdreq_09_1_1a";
//	private static CxLogger logger = CxLogManager.getLogger(TicketProcessEDocCallBack.class);
	private TicketProcessEDocCCConcealed request;
	private String officeId;

	public TicketProcessEDocCallBack(TicketProcessEDocCCConcealed request, AmadeusServiceConfig amadeusServiceConfig) {//, String officeId
		super(amadeusServiceConfig, amadeusServiceConfig.getTicketProcessEdocAction());
		this.request = request;
//		this.officeId = officeId;
	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(CONTEXT_PATH);

		MarshallingUtils.marshal(marshaller, request, message);
		// set action
		SaajSoapMessage soapMessage = (SaajSoapMessage) message;
		soapMessage.setSoapAction(this.soapAction);
		try {
			SoapHeaderUtil.generateTicketSoapHeader(soapMessage, amadeusServiceConfig);// String officeId
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			message.writeTo(out);
			System.out.println("soapAction"+this.soapAction);
			System.out.println("message"+message);
			System.out.println("oooout"+out);
//			logger.tag(OUTBOUND_HOSTNAME, this.soapAction).tag("FREE_TEXT", out.toString()).info("TICKET-PROCESS-EDOC RETREIVE REQUEST");
			new ActionCallback(new URI(this.soapAction), new Addressing10(), new URI(amadeusServiceConfig.getWsAddressingAction())).doWithMessage(message);

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), ErrorResponseException.ERR_CONSTRUCT_REQUEST_MESSAGE);
		}
	}

	public TicketProcessEDocCCConcealed getRequest() {
		return request;
	}

	public void setRequest(TicketProcessEDocCCConcealed request) {
		this.request = request;
	}
}
