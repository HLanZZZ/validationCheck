package com.accenture.aesrefund.controller;


import com.accenture.aesrefund.callback.TicketProcessEDocReplyCallBack;
import com.accenture.aesrefund.common.CommonWebServiceException;
import com.accenture.aesrefund.common.ErrorResponseException;
import com.accenture.aesrefund.common.InputParameterException;
import com.accenture.aesrefund.constant.HttpSourceEnum;
import com.accenture.aesrefund.constant.HttpStatusEnum;
import com.accenture.aesrefund.request.ValidationCheckRequest;
import com.accenture.aesrefund.response.ValidationCheckResponse;
import com.accenture.aesrefund.service.ValidationCheckService;
import com.accenture.aesrefund.service.amadeus.TicketProcessEDocService;
import com.accenture.aesrefund.vo.*;

import com.accenture.aesrefund.vo.Error;
import com.amadeus.xml.tcdres_09_1_1a.TicketProcessEDocCCConcealedReply;
import org.apache.http.conn.ConnectTimeoutException;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



@Transactional(rollbackFor = Exception.class)
@RestController
public class ValidationCheckController {

//    private static CxLogger logger = CxLogManager.getLogger(TicketProcessEDocReplyCallBack.class);

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    ValidationCheckService validationCheckService;

    @Autowired
    private TicketProcessEDocService ticketProcessEDocService;


    private ValidationCheckResponse validationCheckResponse;


    public TicketProcessEDocService getTicketProcessEDocService() {
        return ticketProcessEDocService;
    }

    public void setTicketProcessEDocService(TicketProcessEDocService ticketProcessEDocService) {
        this.ticketProcessEDocService = ticketProcessEDocService;
    }

    public ValidationCheckResponse getValidationCheckResponse() {
        return validationCheckResponse;
    }

    @RequestMapping(value = "/validation-check", method = RequestMethod.POST)
    public ValidationCheckResponse validationCheckResponse(@RequestBody ValidationCheckRequest validationCheckRequest) throws CommonWebServiceException, ConnectTimeoutException, ParseException, ErrorResponseException, InputParameterException, ApplicationException {

        List<AncillaryProducts> list = validationCheckRequest.getAncillaryProducts();

        //  Check if any mandatory field missing
        if(validationCheckRequest.getRloc()== null|| validationCheckRequest.getSource() ==null || validationCheckRequest.getAncillaryProducts() == null){

            throw new CommonWebServiceException(HttpStatusEnum.MANDATORY_FIELD_MISSING,
                    HttpSourceEnum.SOURCE.toString());
        }

        //Maybe exist some questions
        for (AncillaryProducts ancillaryProducts :list){
            // check if mandatory field missing
            if (ancillaryProducts.getId()== null|| ancillaryProducts.getProductType()== null || ancillaryProducts.getPassengers()== null||ancillaryProducts.getSegmentInfo()==null
                    ||ancillaryProducts.getPassengers().get(0).getGivenName() == null|| ancillaryProducts.getPassengers().get(0).getFamilyName()== null || ancillaryProducts.getSegmentInfo().get(0).getDepartureDate() == null
                    ||ancillaryProducts.getSegmentInfo().get(0).getOrigin() == null|| ancillaryProducts.getSegmentInfo().get(0).getDestination() == null
            ){
                    throw new CommonWebServiceException(HttpStatusEnum.MANDATORY_FIELD_MISSING,HttpSourceEnum.SOURCE.toString());
            }

        }

        //Check if  the value of source be either 'INT' or 'EXT'
        if (!validationCheckRequest.getSource().equals("INT") && !validationCheckRequest.getSource().equals("EXT")){
            throw  new CommonWebServiceException(HttpStatusEnum.SOURCE_BE_EITHER_INT_OR_EXT, HttpSourceEnum.SOURCE.toString());
        }

        // match the One A with emdNumber which in validatonCheckResponse
        validationCheckResponse = validationCheckService.getValidationCheckResponse(validationCheckRequest);

        TicketProcessEDocReplyCallBack ticketProcessEDocReplyCallBack = ticketProcessEDocService.getTicketCallBack(getDetailNumber());

        ValidationCheckResponse validationCheckResponse1 = new ValidationCheckResponse();

        List<AncillaryProducts>  ancillaryProductsList =new ArrayList<>();
        RefundInfo refundInfo = new RefundInfo();
        Validation validation =new Validation();
        Error error = new Error();
        //foreach DocGroup
        for (TicketProcessEDocCCConcealedReply.DocGroup docGroup : ticketProcessEDocReplyCallBack.getResponse().getDocGroup()){

            //foreach DocDetailsGroup
             for (TicketProcessEDocCCConcealedReply.DocGroup.DocDetailsGroup detailsGroup : docGroup.getDocDetailsGroup()){
                 //foreach DetailNumber
                 for (String detailNumber :getDetailNumber()){
                     //>>Match the docDetailsGroup/docInfo/documentDetails/number = (emdInfo/emdAirlineCode+emdInfo/emdNumber)
                     if (detailsGroup.getDocInfo().getDocumentDetails().getNumber().equals(detailNumber)){
                         //in this docDetailsGroup, loop through each couponGroup
                          for (TicketProcessEDocCCConcealedReply.DocGroup.DocDetailsGroup.CouponGroup cGroup :detailsGroup.getCouponGroup()){
                              //foreach which AncillaryProducts match successful
                              int a = 0;
                              for (AncillaryProducts ancillaryProducts : getAncillaryProducts()) {
                                  // If source = 'EXT', for ancillaryProduct with emdInfo/emdNumber not start with 455, mark its validation/status = 'fail' with error code VF_AESRSH_C007
                                  if (validationCheckResponse.getRefundInfo().getSource().equals("EXT")) {
                                      for (String numbers : ancillaryProducts.getEmdNumber()) {
                                          String numHead = numbers.substring(0, 3); // String truncation
                                          if (!(numHead.equals("455"))) {
                                              validation.setCode("VF_AESRSH_C007");
                                              validation.setMessage("Not sold by CX/KA");
                                              validation.setStatus("fail");
                                              ancillaryProducts.setValidation(validation);
                                          }
                                      }
                                  }

                                  //Match the couponGroup/leg[0]/boardPointDetails/trueLocationId against ancillaryProducts/segmentInfo/origin
                                  if (cGroup.getLeg().get(0).getBoardPointDetails().getTrueLocationId().equals(ancillaryProducts.getSegmentInfo().get(0).getOrigin())) {
                                      //Match the couponGroup/leg[0]/offpointDetails/trueLocationId against ancillaryProducts/segmentInfo/destination
                                      if (cGroup.getLeg().get(0).getOffpointDetails().getTrueLocationId().equals(ancillaryProducts.getSegmentInfo().get(0).getDestination())) {
                                          //Match the couponGroup/validityDates/dateAndTimeDetails/date against ancillaryProducts/segmentInfo/departureDate on couponGroup/validityDates/dateAndTimeDetails/qualifier = B
                                          if (!(cGroup.getValidityDates().getDateAndTimeDetails().get(0).getDate() == null) && cGroup.getValidityDates().getDateAndTimeDetails().get(0).getQualifier().equals("B")) {

                                              DateFormat dateFormat = new SimpleDateFormat("DD_MM_YY");
                                              dateFormat.format(cGroup.getValidityDates().getDateAndTimeDetails().get(0).getDate());

                                              if (cGroup.getValidityDates().getDateAndTimeDetails().get(0).getDate().equals(ancillaryProducts.getSegmentInfo().get(0).getDepartureDate())) {

                                                  a++;

                                                  List<SegmentInfo> segmentInfoList = new ArrayList<>();
                                                  Fop fop = new Fop();

                                                  for (SegmentInfo segmentInfo : ancillaryProducts.getSegmentInfo()) {
                                                      Integer cpnNumber = Integer.valueOf(cGroup.getCouponInfo().getCouponDetails().get(0).getCpnNumber());
                                                      segmentInfo.setCpnNumber(cpnNumber);
                                                      segmentInfoList.add(segmentInfo);
                                                  }
                                                  for (SegmentInfo segmentInfo : ancillaryProducts.getSegmentInfo()) {
                                                      segmentInfo.setCpnStatus(cGroup.getCouponInfo().getCouponDetails().get(0).getCpnStatus());
                                                      segmentInfoList.add(segmentInfo);
                                                  }

                                                  fop.setFopFreeText(docGroup.getFop().getFormOfPayment().get(0).getType());
                                                  ancillaryProducts.setFop(fop);
                                                  ancillaryProducts.setSegmentInfo(segmentInfoList);
                                              }
                                          } else {

                                              a++;
                                              List<SegmentInfo> segmentInfoList = new ArrayList<>();
                                              Fop fop = new Fop();
                                              for (SegmentInfo segmentInfo : ancillaryProducts.getSegmentInfo()) {
                                                  Integer cpnNumber = Integer.valueOf(cGroup.getCouponInfo().getCouponDetails().get(0).getCpnNumber());
                                                  segmentInfo.setCpnNumber(cpnNumber);
                                                  segmentInfoList.add(segmentInfo);
                                              }
                                              for (SegmentInfo segmentInfo : ancillaryProducts.getSegmentInfo()) {
                                                  segmentInfo.setCpnStatus(cGroup.getCouponInfo().getCouponDetails().get(0).getCpnStatus());
                                                  segmentInfoList.add(segmentInfo);
                                              }
                                              fop.setFopFreeText(docGroup.getFop().getFormOfPayment().get(0).getType());
                                              ancillaryProducts.setFop(fop);
                                              ancillaryProducts.setSegmentInfo(segmentInfoList);
                                          }

                                      }
                                  }
                                  // return ancillaryProducts
                                  System.out.println(ancillaryProducts);

                                  ancillaryProductsList.add(ancillaryProducts);
                                  if (a > 1) {
                                      error.setCode("VU_AESRSH_U003");
                                      error.setStatus("unidentified");
                                      error.setMessage("Multiple EMD/Coupon matched");
                                      error.setDetail("Multiple EMD/Coupon matched");
                                      ancillaryProductsList.add(ancillaryProducts);
                                  }


                              }

                          }


                     }

                 }

             }

        }

        List<AncillaryProducts> productsList = new ArrayList<>();

        for (AncillaryProducts ancillaryProduct :ancillaryProductsList){
            // For ancillaryProduct which does not have cpnNumber or cpnStatus, mark its validation/status = 'fail' with error code VF_AESRSH_C004
            for (SegmentInfo segmentInfo :ancillaryProduct.getSegmentInfo()){
                if (segmentInfo.getCpnNumber() == null || segmentInfo.getCpnStatus() == null){
                    validation.setCode("VF_AESRSH_C004");
                    validation.setMessage("Coupon/EMD not found");
                    validation.setStatus("fail");
                    ancillaryProduct.setValidation(validation);
                }
                //For ancillaryProduct with cpnStatus = 'RF', mark its validation/status = 'fail' with error code VF_AESRSH_C005
                if (segmentInfo.getCpnStatus().equals("RF")){
                    validation.setCode("VF_AESRSH_C005");
                    validation.setMessage("EMD has already been refunded");
                    validation.setStatus("fail");
                    ancillaryProduct.setValidation(validation);
                }
                //For ancillaryProduct with cpnStatus in ('CLO','V','S',NAV','Q','E', '708', '701'), mark its validation/status = 'fail' with error code VF_AESRSH_C006
                if (segmentInfo.getCpnStatus().equals("CLO") ||segmentInfo.getCpnStatus().equals("V") ||segmentInfo.getCpnStatus().equals("S")||segmentInfo.getCpnStatus().equals("NAV")||segmentInfo.getCpnStatus().equals("Q")
                        ||segmentInfo.getCpnStatus().equals("E") || segmentInfo.getCpnStatus().equals("708") || segmentInfo.getCpnStatus().equals("701") ){

                    validation.setCode("VF_AESRSH_C006");
                    validation.setMessage("EMD has already been forfeited");
                    validation.setStatus("fail");
                    ancillaryProduct.setValidation(validation);
                }
                //For ancillaryProduct with fop/fopFreeText not starts with 'PSC' or 'CC', mark its validation/status = 'fail' with error code VF_AESRSH_C008
            }
            String fopTreeText = ancillaryProduct.getFop().getFopFreeText();
            String subTreeText = fopTreeText.substring(0,3);
            String subTreeText1 = fopTreeText.substring(0,2);

            if (subTreeText.equals("PSC") || subTreeText1.equals("CC")){

                validation.setCode("VF_AESRSH_C008");
                validation.setMessage("Not paid by credit card");
                validation.setStatus("fail");
                ancillaryProduct.setValidation(validation);
            }

            validation.setStatus("pass");
            ancillaryProduct.setValidation(validation);

            productsList.add(ancillaryProduct);
        }



        refundInfo.setRloc(validationCheckResponse.getRefundInfo().getRloc());
        refundInfo.setSource(validationCheckResponse.getRefundInfo().getSource());
        refundInfo.setAncillaryProducts(productsList);

        validationCheckResponse1.setError(error);
        validationCheckResponse1.setRefundInfo(refundInfo);


        logger.info(validationCheckResponse1.toString());
        System.out.println(validationCheckResponse1);

        return  validationCheckResponse1;
    }

    public List<String>  getDetailNumber(){
        ValidationCheckResponse validationCheckResponse = this.getValidationCheckResponse();
        List<AncillaryProducts> list = validationCheckResponse.getRefundInfo().getAncillaryProducts();
        List<String> emdNumber = new ArrayList<>();
        for (AncillaryProducts ancillaryProducts :list){
            if (!(ancillaryProducts.getEmdNumber() == null)){

                for (String stringList:ancillaryProducts.getEmdNumber()){
                    String number = ancillaryProducts.getEmdAirlineCode()+stringList;
                    emdNumber.add(number);
                }
            }
        }
        return emdNumber;
    }

    //which ancillaryProducts match successful
    public List<AncillaryProducts>  getAncillaryProducts(){

        ValidationCheckResponse validationCheckResponse = this.getValidationCheckResponse();
        List<AncillaryProducts> list = validationCheckResponse.getRefundInfo().getAncillaryProducts();
        List<AncillaryProducts> list1 = new ArrayList<>();

        for (AncillaryProducts ancillaryProducts :list){
            if (!(ancillaryProducts.getEmdNumber() == null)){
                list1.add(ancillaryProducts);
                }
            }
        return list1;
        }

    }


