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
import com.accenture.aesrefund.vo.Error;
import com.accenture.aesrefund.vo.*;
import com.amadeus.xml.tcdres_09_1_1a.TicketProcessEDocCCConcealedReply;
import org.apache.http.conn.ConnectTimeoutException;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Transactional(rollbackFor = Exception.class)
@RestController
public class ValidationCheckController {


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
        Error error = new Error();

        int a =0;
        for (AncillaryProducts ancillaryProducts : getAncillaryProducts()) {

            Validation validation1 =new Validation();
            // If source = 'EXT', for ancillaryProduct with emdInfo/emdNumber not start with 455, mark its validation/status = 'fail' with error code VF_AESRSH_C007
            if (validationCheckResponse.getRefundInfo().getSource().equals("EXT")) {
                for (String numbers : ancillaryProducts.getEmdNumber()) {
                    String numHead = numbers.substring(0, 3); // String truncation
                    if (!(numHead.equals("455"))) {
                        validation1.setCode("VF_AESRSH_C007");
                        validation1.setMessage("Not sold by CX/KA");
                        validation1.setStatus("fail");
                        ancillaryProducts.setValidation(validation1);
                    }
                }
            }
            if (ancillaryProducts.getEmdNumber().size() ==1){
                //foreach DocGroup
                for (TicketProcessEDocCCConcealedReply.DocGroup docGroup : ticketProcessEDocReplyCallBack.getResponse().getDocGroup()){
                    //foreach DocDetailsGroup
                    for (TicketProcessEDocCCConcealedReply.DocGroup.DocDetailsGroup detailsGroup : docGroup.getDocDetailsGroup()){

                        String  number = ancillaryProducts.getEmdAirlineCode()+ancillaryProducts.getEmdNumber().get(0);
                        if (number.equals(detailsGroup.getDocInfo().getDocumentDetails().getNumber())){
                            //in this docDetailsGroup, loop through each couponGroup
                            for (TicketProcessEDocCCConcealedReply.DocGroup.DocDetailsGroup.CouponGroup cGroup :detailsGroup.getCouponGroup()){

                                if (cGroup.getLeg() != null && cGroup.getLeg().size()>0 && cGroup.getLeg().get(0).getBoardPointDetails()!= null && cGroup.getLeg().get(0).getBoardPointDetails().getTrueLocationId() !=null){
                                    //Match the couponGroup/leg[0]/boardPointDetails/trueLocationId against ancillaryProducts/segmentInfo/origin
                                    if (cGroup.getLeg().get(0).getBoardPointDetails().getTrueLocationId().equals(ancillaryProducts.getSegmentInfo().get(0).getOrigin())) {
                                        if (cGroup.getLeg() != null && cGroup.getLeg().get(0).getOffpointDetails()!= null && cGroup.getLeg().get(0).getOffpointDetails().getTrueLocationId() !=null)
                                        //Match the couponGroup/leg[0]/offpointDetails/trueLocationId against ancillaryProducts/segmentInfo/destination
                                        if (cGroup.getLeg().get(0).getOffpointDetails().getTrueLocationId().equals(ancillaryProducts.getSegmentInfo().get(0).getDestination())) {
                                            //Match the couponGroup/validityDates/dateAndTimeDetails/date against ancillaryProducts/segmentInfo/departureDate on couponGroup/validityDates/dateAndTimeDetails/qualifier = B
                                            if (!(cGroup.getValidityDates().getDateAndTimeDetails().get(0).getDate() == null) && cGroup.getValidityDates().getDateAndTimeDetails().get(0).getQualifier().equals("B")) {
//
                                                if (cGroup.getValidityDates().getDateAndTimeDetails().get(0).getDate().equals(ancillaryProducts.getSegmentInfo().get(0).getDepartureDate())) {
                                                    a++;
                                                    List<SegmentInfo> segmentInfoList = new ArrayList<>();
                                                    Fop fop = new Fop();

                                                    for (SegmentInfo segmentInfo : ancillaryProducts.getSegmentInfo()) {
                                                        Integer cpnNumber = Integer.valueOf(cGroup.getCouponInfo().getCouponDetails().get(0).getCpnNumber());
                                                        segmentInfo.setCpnNumber(cpnNumber);
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

                              }
                            }
                          }

                     }

                 }

             }
            // return ancillaryProducts
            ancillaryProductsList.add(ancillaryProducts);
            if (a > 1) {
                error.setCode("VU_AESRSH_U003");
                error.setStatus("unidentified");
                error.setMessage("Multiple EMD/Coupon matched");
                error.setDetail("Multiple EMD/Coupon matched");
                ancillaryProductsList.add(ancillaryProducts);
            }

        }


        //这部分不管 主要做一些判断
        List<AncillaryProducts> productsList = new ArrayList<>();

        for (AncillaryProducts ancillaryProduct :ancillaryProductsList){
            Validation validation =new Validation();

            // For ancillaryProduct which does not have cpnNumber or cpnStatus, mark its validation/status = 'fail' with error code VF_AESRSH_C004
            for (SegmentInfo segmentInfo :ancillaryProduct.getSegmentInfo()){
                if (segmentInfo.getCpnNumber() == null || segmentInfo.getCpnStatus() == null){
                    validation.setCode("VF_AESRSH_C004");
                    validation.setMessage("Coupon/EMD not found");
                    validation.setStatus("fail");
                    ancillaryProduct.setValidation(validation);
                }else {
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
                }

                //For ancillaryProduct with fop/fopFreeText not starts with 'PSC' or 'CC', mark its validation/status = 'fail' with error code VF_AESRSH_C008
            }
            if ( ancillaryProduct.getFop()!=null && ancillaryProduct.getFop().getFopFreeText() !=null){

                String fopTreeText = ancillaryProduct.getFop().getFopFreeText();
                if ( fopTreeText.length() >2){
                    String subTreeText = fopTreeText.substring(0,2);
                    String subTreeText1 = fopTreeText.substring(0,2);
                    if (subTreeText.equals("PSC") || subTreeText1.equals("CC")){

                        validation.setCode("VF_AESRSH_C008");
                        validation.setMessage("Not paid by credit card");
                        validation.setStatus("fail");
                        ancillaryProduct.setValidation(validation);
                    }
                }else if (fopTreeText.length()==2){

                    String subTreeText1 = fopTreeText.substring(0,2);
                    if (subTreeText1.equals("CC")){

                        validation.setCode("VF_AESRSH_C008");
                        validation.setMessage("Not paid by credit card");
                        validation.setStatus("fail");
                        ancillaryProduct.setValidation(validation);
                    }
                }else {
                    validation.setCode("VF_AESRSH_C008");
                    validation.setMessage("Not paid by credit card");
                    validation.setStatus("fail");
                    ancillaryProduct.setValidation(validation);
                }

                validation.setStatus("pass");
                ancillaryProduct.setValidation(validation);


             }
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
                String test = "1604552631099";
                emdNumber.add(test);
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

            if (ancillaryProducts.getEmdNumber() ==null){
                List<String> number =new ArrayList<>();
                String emdCode = "4552631099";
                String  emdAirlineCode ="160";
                number.add(emdCode);
                ancillaryProducts.setEmdNumber(number);
                ancillaryProducts.setEmdAirlineCode(emdAirlineCode);
                list1.add(ancillaryProducts);
            }

            }
        return list1;
        }

    }


