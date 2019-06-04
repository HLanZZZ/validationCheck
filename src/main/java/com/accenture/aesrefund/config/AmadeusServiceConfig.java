package com.accenture.aesrefund.config;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Configuration
@ConfigurationProperties(prefix = "amadeus.ws.client")
public class AmadeusServiceConfig {
	/**
	 * Amadeus end point URI
	 */
	private static final String WSDL_GENERATED_PACKAGE = "com.amadeus.xml.tcdreq_09_1_1a";
	private String wsEndPoint;

	private String posType;
	private String requestorType;
	private String pseudoCityCode;
	private String agentDutyCode;
	private String companyName;

	private String wsAddressingAction;
	private String pnrRetrieveAction;
	private String catalogueRetrieveAction;
	private String pnrAddAction;
	private String pricingRetrieveAction;
	private String pnrCancelAction;
	private String issueEmdAction;
	private String multiElementOptionCode;
	private String createTsmAction;
	private String createFopAction;
	private String queuePlacePnrAction;
	private String ticketProcessEdocAction;
	private int connectionTimeout;
	private int connectionMaxTotal;
	private int maxPreRoute;
	private int maxRetry = 3;
	private int backoffDelay = 100;
	private int backoffMaxDelay = 300;
	
	private String queuePnrOffice;
	/**
	 * SOA user name for use in SOAP WSS security header
	 */
	private String wssUsername;

	/**
	 * SOA header for use in SOAP WSS security header
	 */
	private String wssPassword;
/*
	@Bean
	public WebServiceMessageSender webServiceMessageSender() throws URISyntaxException {
		HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
		  sender.setConnectionTimeout(1);
	      sender.setMaxTotalConnections(800);
	      Map<String, String> map = new HashMap<String, String>();
	      map.put(this.getWsEndPoint(), "200");
	      sender.setMaxConnectionsPerHost(map);
	      sender.setReadTimeout(readTimeout);
		return sender;
	}
	*/

	//ticketProcessEdocAction:{AMADEUS_TICKET_PROCESS_EDOC_ACTION:http://webservices.amadeus.com/1ASIWGENCPA/TCDREQ_09_1_1A}

	@Bean
    public WebServiceMessageSender oneAwebServiceMessageSender() throws URISyntaxException {
           RequestConfig config = RequestConfig.custom()
                          .setConnectTimeout(this.getConnectionTimeout())
				.setConnectionRequestTimeout(this.getConnectionTimeout())
				.setSocketTimeout(this.getConnectionTimeout()).build();

           PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
           connectionManager.setMaxTotal(this.getConnectionMaxTotal());
           connectionManager.setDefaultMaxPerRoute(this.getMaxPreRoute());
       
           HttpClientBuilder httpClientBuilder = HttpClients.custom();

           CloseableHttpClient httpClient = httpClientBuilder
                               .setDefaultRequestConfig(config)
                               .setConnectionManager(connectionManager)
                  //  .setMaxConnPerRoute(getMaxConnectionsPerHost())
                    //.setMaxConnTotal(getMaxConnections())
                               .addInterceptorFirst(new ContentLengthHeaderRemover())
                    .build();
           return new HttpComponentsMessageSender(httpClient);
    }
    
    private static class ContentLengthHeaderRemover implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            request.removeHeaders(HTTP.CONTENT_LEN);
        }
    }



	public int getConnectionMaxTotal() {
		return connectionMaxTotal;
	}

	public void setConnectionMaxTotal(int connectionMaxTotal) {
		this.connectionMaxTotal = connectionMaxTotal;
	}

	public int getMaxPreRoute() {
		return maxPreRoute;
	}

	public void setMaxPreRoute(int maxPreRoute) {
		this.maxPreRoute = maxPreRoute;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getWsEndPoint() {
		return wsEndPoint;
	}

	public void setWsEndPoint(String wsEndPoint) {
		this.wsEndPoint = wsEndPoint;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getWssUsername() {
		return wssUsername;
	}

	public void setWssUsername(String wssUsername) {
		this.wssUsername = wssUsername;
	}

	public String getWssPassword() {
		return wssPassword;
	}

	public void setWssPassword(String wssPassword) {
		this.wssPassword = wssPassword;
	}

	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
	}

	public String getRequestorType() {
		return requestorType;
	}

	public void setRequestorType(String requestorType) {
		this.requestorType = requestorType;
	}

	public String getPseudoCityCode() {
		return pseudoCityCode;
	}

	public void setPseudoCityCode(String pseudoCityCode) {
		this.pseudoCityCode = pseudoCityCode;
	}

	public String getAgentDutyCode() {
		return agentDutyCode;
	}

	public void setAgentDutyCode(String agentDutyCode) {
		this.agentDutyCode = agentDutyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWsAddressingAction() {
		return wsAddressingAction;
	}

	public void setWsAddressingAction(String wsAddressingAction) {
		this.wsAddressingAction = wsAddressingAction;
	}

	public String getPnrRetrieveAction() {
		return pnrRetrieveAction;
	}

	public void setPnrRetrieveAction(String pnrRetrieveAction) {
		this.pnrRetrieveAction = pnrRetrieveAction;
	}

	public String getCatalogueRetrieveAction() {
		return catalogueRetrieveAction;
	}
	
	

	public String getIssueEmdAction() {
		return issueEmdAction;
	}

	public void setIssueEmdAction(String issueEmdAction) {
		this.issueEmdAction = issueEmdAction;
	}
	
	

	public String getQueuePlacePnrAction() {
		return queuePlacePnrAction;
	}

	public void setQueuePlacePnrAction(String queuePlacePnrAction) {
		this.queuePlacePnrAction = queuePlacePnrAction;
	}	

	@Bean
	public WebServiceTemplate webServiceTemplate(@Qualifier("wsdlMashaller") Jaxb2Marshaller mashaller)
			throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, URISyntaxException {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate(mashaller);
	    
		// NOTE: can replace with HttpComponentsMessageSender to use HttpClient
		// instead
	      /*
		HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender() {
			@Override
			protected void prepareConnection(HttpURLConnection connection) throws IOException {
				super.prepareConnection(connection);
				connection.setConnectTimeout(connectionTimeout);
				connection.setReadTimeout(readTimeout);
			}
		};*/
		webServiceTemplate.setMessageSender(oneAwebServiceMessageSender());
		return webServiceTemplate;
	}

	@Bean
	public Jaxb2Marshaller wsdlMashaller() {
		Jaxb2Marshaller jaxbMarshaller = new Jaxb2Marshaller();
		jaxbMarshaller.setPackagesToScan(WSDL_GENERATED_PACKAGE);
		return jaxbMarshaller;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RetryTemplate retryTemplate() {
		UniformRandomBackOffPolicy backOffPolicy = new UniformRandomBackOffPolicy();
		backOffPolicy.setMinBackOffPeriod(backoffDelay);
		backOffPolicy.setMaxBackOffPeriod(backoffMaxDelay);
		RetryTemplate template = new RetryTemplate();
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}

	public void setCatalogueRetrieveAction(String catalogueRetrieveAction) {
		this.catalogueRetrieveAction = catalogueRetrieveAction;
	}

	public String getPnrAddAction() {
		return pnrAddAction;
	}

	public void setPnrAddAction(String pnrAddAction) {
		this.pnrAddAction = pnrAddAction;
	}

	public String getPricingRetrieveAction() {
		return pricingRetrieveAction;
	}

	public void setPricingRetrieveAction(String pricingRetrieveAction) {
		this.pricingRetrieveAction = pricingRetrieveAction;
	}

	public String getPnrCancelAction() {
		return pnrCancelAction;
	}

	public String getCreateTsmAction() {
		return createTsmAction;
	}

	public void setCreateTsmAction(String createTsmAction) {
		this.createTsmAction = createTsmAction;
	}
	
	public String getCreateFopAction() {
		return createFopAction;
	}

	public void setCreateFopAction(String createFopAction) {
		this.createFopAction = createFopAction;
	}

	public void setPnrCancelAction(String pnrCancelAction) {
		this.pnrCancelAction = pnrCancelAction;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	public int getBackoffDelay() {
		return backoffDelay;
	}

	public void setBackoffDelay(int backoffDelay) {
		this.backoffDelay = backoffDelay;
	}

	public int getBackoffMaxDelay() {
		return backoffMaxDelay;
	}

	public void setBackoffMaxDelay(int backoffMaxDelay) {
		this.backoffMaxDelay = backoffMaxDelay;
	}

	public String getMultiElementOptionCode() {
		return multiElementOptionCode;
	}

	public void setMultiElementOptionCode(String multiElementOptionCode) {
		this.multiElementOptionCode = multiElementOptionCode;
	}

	public String getQueuePnrOffice() {
		return queuePnrOffice;
	}

	public void setQueuePnrOffice(String queuePnrOffice) {
		this.queuePnrOffice = queuePnrOffice;
	}

	public String getTicketProcessEdocAction() {
		return ticketProcessEdocAction;
	}

	public void setTicketProcessEdocAction(String ticketProcessEdocAction) {
		this.ticketProcessEdocAction = ticketProcessEdocAction;
	}
	
	

}
