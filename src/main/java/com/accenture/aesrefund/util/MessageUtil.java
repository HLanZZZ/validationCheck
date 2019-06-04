package com.accenture.aesrefund.util;

import com.amadeus.xml.tcdreq_09_1_1a.TicketProcessEDocCCConcealed;

import com.amadeus.xml.tcdreq_09_1_1a.MessageActionDetailsTypeI;
import com.amadeus.xml.tcdreq_09_1_1a.MessageFunctionBusinessDetailsTypeI;
import com.amadeus.xml.tcdreq_09_1_1a.TicketNumberDetailsTypeI;
import com.amadeus.xml.tcdreq_09_1_1a.TicketNumberTypeI;

import java.util.List;



public class MessageUtil {
	
  private MessageUtil() {}


  public static TicketProcessEDocCCConcealed constructTicketProcessEDoc(List<String> ticketList){

	  TicketProcessEDocCCConcealed request = new TicketProcessEDocCCConcealed();
	  MessageActionDetailsTypeI msgActionDetails = new MessageActionDetailsTypeI();
	  MessageFunctionBusinessDetailsTypeI messageFunction = new MessageFunctionBusinessDetailsTypeI();
	  messageFunction.setMessageFunction("791");
	  msgActionDetails.setMessageFunctionDetails(messageFunction);
	  request.setMsgActionDetails(msgActionDetails);

	  for(String ticket : ticketList){
		  TicketProcessEDocCCConcealed.InfoGroup info = new TicketProcessEDocCCConcealed.InfoGroup();
		  TicketNumberTypeI docInfo = new TicketNumberTypeI();
		  TicketNumberDetailsTypeI docDetails = new TicketNumberDetailsTypeI();
		  String number = ticket;
		  String type = "J";
		  docDetails.setNumber(number);
		  docDetails.setType(type);
		  docInfo.setDocumentDetails(docDetails);
		  info.setDocInfo(docInfo);
		  request.getInfoGroup().add(info);
	  }
	  return request;
  }

  
}
 
 
