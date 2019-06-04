package com.accenture.aesrefund.service.amadeus;



import com.accenture.aesrefund.config.LogConfig;
import com.amadeus.xml.tcdreq_09_1_1a.TicketProcessEDocCCConcealed;
import com.accenture.aesrefund.callback.TicketProcessEDocCallBack;
import com.accenture.aesrefund.callback.TicketProcessEDocReplyCallBack;
import com.accenture.aesrefund.common.ErrorResponseException;
import com.accenture.aesrefund.common.InputParameterException;
import com.accenture.aesrefund.config.AmadeusServiceConfig;
import com.accenture.aesrefund.constant.HttpStatusEnum;
import com.accenture.aesrefund.service.ValidationCheckService;
import com.accenture.aesrefund.util.MessageUtil;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.WebServiceMessageSender;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;


@Service
public class TicketProcessEDocService extends WebServiceGatewaySupport {

	@Autowired
	WebServiceMessageSender oneAwebServiceMessageSender;

	@Autowired
	private ValidationCheckService validationCheckService;

	@Autowired
	private LogConfig logConfig;


	private AmadeusServiceConfig amadeusServiceConfig;
	private RetryTemplate retryTemplate;
//	private static CxLogger logger = CxLogManager.getLogger(TicketProcessEDocService.class);

	@Inject
	public TicketProcessEDocService(WebServiceTemplate webServiceTemplate, AmadeusServiceConfig amadeusServiceConfig, RetryTemplate retryTemplate) {
		super();
		super.setWebServiceTemplate(webServiceTemplate);
		this.amadeusServiceConfig=amadeusServiceConfig;
		this.retryTemplate = retryTemplate;
		this.retryTemplate.setRetryPolicy(retryPolicy());

	}

	private RetryPolicy retryPolicy() {
		// retry if WebService call if IO exception
		return new SimpleRetryPolicy(amadeusServiceConfig.getMaxRetry(),
				Collections.singletonMap(WebServiceIOException.class, true));
	}


	public TicketProcessEDocReplyCallBack getTicketCallBack(List<String> tickets) throws ErrorResponseException, ApplicationException, InputParameterException {  //,officeId

//		ValidationCheckController validationCheckController =new ValidationCheckController();
		TicketProcessEDocCCConcealed ticketProcessEDoc= MessageUtil.constructTicketProcessEDoc(tickets);
		System.out.println("ticketProcessEDoc:"+ticketProcessEDoc);
		TicketProcessEDocReplyCallBack ticketReplyCallBack=this.callWSCallback(ticketProcessEDoc); //,officeId
		System.out.println(ticketReplyCallBack+"----------------------------------------------");
		return ticketReplyCallBack;
	}

	public TicketProcessEDocReplyCallBack callWSCallback(TicketProcessEDocCCConcealed request) throws ErrorResponseException { //,String officeId

		// I don't know what that means here
		setMessageSender(oneAwebServiceMessageSender);
		TicketProcessEDocCallBack requestCallBack = new TicketProcessEDocCallBack(request, amadeusServiceConfig); //, String officeId
		TicketProcessEDocReplyCallBack responseCallBack = new TicketProcessEDocReplyCallBack(logConfig.getEnableObjectConversion()); //

		System.out.println(requestCallBack+"--------------"+responseCallBack);
		try
		{
			makeRequest(requestCallBack, responseCallBack); //
		}
		catch(Exception e)
		{
			String errorCode="";
			throw new ErrorResponseException(HttpStatusEnum.PRICES_PNR_RET_1AWS_ERR.getMessage(), errorCode,e.getMessage());
		}
		return responseCallBack;
	}

	private boolean makeRequest(TicketProcessEDocCallBack requestCallBack, TicketProcessEDocReplyCallBack responseCallBack) throws URISyntaxException  {
		//Lambda 表达式
		    return retryTemplate.execute(retryCallback -> {
		    	 if (retryCallback.getRetryCount() > 0) {

		    	 	System.out.println(retryCallback.getRetryCount()+"count");
					 System.out.println (retryCallback.getLastThrowable().getClass()+"Class");
					 System.out.println(retryCallback.getLastThrowable().getMessage()+"Message") ;
		    	      }
		    	 long startTime = System.currentTimeMillis();
		    	 
		    	 boolean isSuccess=getWebServiceTemplate().sendAndReceive(amadeusServiceConfig.getWsEndPoint(),
				          requestCallBack, responseCallBack);
		    	 logger.debug(isSuccess);
//			      logger.tag(DURATION,System.currentTimeMillis()-startTime).info("PNR RETREIVE DURATION");
		      return isSuccess;
		    });
	}
	  
	 
}
