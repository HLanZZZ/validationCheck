package com.accenture.aesrefund.service.impl;



import com.accenture.aesrefund.constant.ValidationEnum;
import com.accenture.aesrefund.model.AncillaryBookingProducts;
import com.accenture.aesrefund.model.EmdInfoVo;
import com.accenture.aesrefund.model.ErrorBodyVo;
import com.accenture.aesrefund.request.ValidationCheckRequest;
import com.accenture.aesrefund.response.AncillaryBookingResponse;
import com.accenture.aesrefund.response.ValidationCheckResponse;
import com.accenture.aesrefund.service.ValidationCheckService;
import com.accenture.aesrefund.vo.*;
import com.accenture.aesrefund.vo.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



@Transactional(rollbackFor = Exception.class)
@Service
public class ValidationCheckServiceImpl implements ValidationCheckService {

    private static final String  BOOKING_URI = "http://localhost:8081/getAllBookings?";

    private final Gson gson = new GsonBuilder().setDateFormat("yyyy_MM_dd").create();

    @Autowired
    private RestTemplate restTemplate ;

    Error error = new Error();

    @Override
    public ValidationCheckResponse getValidationCheckResponse(ValidationCheckRequest request) throws ConnectTimeoutException, ParseException {


        String uri = BOOKING_URI +"bookingRef="+request.getRloc();
        // First Api data

        AncillaryBookingResponse ancillaryBookingResponse = this.getAncillaryBookingResponse(uri);


        // foreach the AncillaryBookingProducts  and get the passengerInfo'familyName&givenName
        List<AncillaryBookingProducts>  productsList = ancillaryBookingResponse.getAncillaryProducts();

        List<AncillaryProducts> list = request.getAncillaryProducts();

        //get request AncillaryProducts list
        List<AncillaryProducts>  productsList1 = new ArrayList<>();


        RefundInfo refundInfo = new RefundInfo();
        Validation validation =new Validation();
        // if productList == null  get successful response and the validation changed
        if (productsList == null){

            for (AncillaryProducts ancillaryProducts: list){

                validation.setCode("VF_AESRSH_C009");
                validation.setMessage("No ancillary products found for this booking");
                validation.setStatus("fail");
                ancillaryProducts.setValidation(validation);
                productsList1.add(ancillaryProducts);
            }
            refundInfo.setRloc(request.getRloc());
            refundInfo.setSource(request.getSource());
            refundInfo.setAncillaryProducts(productsList1);
        }

        //init ValidationCheckResponse
//        ValidationCheckResponse validationCheckResponse = new ValidationCheckResponse();  use this way will product NullpointerException because new ValidationCheckResponse will assignment null
        ValidationCheckResponse validationCheckResponse = new ValidationCheckResponse();

        List<AncillaryProducts>  resultList = new ArrayList<>();
        continueOut :
        // requestBody   size： 2
       for (AncillaryProducts ancillaryProducts :list){
           int a =0;
            //  data from first Api size :5
              for (AncillaryBookingProducts products : productsList){

                  AncillaryProducts ancillaryProducts1 =new AncillaryProducts();
                  validation =new Validation();

                  //If failed in step 1, mark its validation/status = 'fail' with error code VF_AESRSH_C001
                 if (ancillaryProducts.getPassengers().get(0).getFamilyName().equals(products.getPassengerInfo().getFamilyName())&&ancillaryProducts.getPassengers().get(0).getGivenName().equals(products.getPassengerInfo().getGivenName())){
                     //If failed in step 2, mark its validation/status = 'fail' with error code VF_AESRSH_C002
                     if (ancillaryProducts.getSegmentInfo().get(0).getOrigin().equals(products.getSegmentInfo().getOrigin())&&ancillaryProducts.getSegmentInfo().get(0).getDestination().equals(products.getSegmentInfo().getDestination())&&ancillaryProducts.getSegmentInfo().get(0).getDepartureDate().equals(products.getSegmentInfo().getDepartureDate())){
                         //If current date > ancillaryProducts/segmentInfo[0]/departureDate + 31 days, mark its validation/status = 'fail' with error code VF_AESRSH_C003
                          Date date = new Date();
                          //将String 类型转成date 类型
                          String departureDate = products.getSegmentInfo().getDepartureDate();
                          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                          Date date1 = sdf.parse(departureDate) ;
                          Calendar rightNow = Calendar.getInstance();
                          rightNow.setTime(date1);
                         //日期加31天
                          rightNow.add(Calendar.DAY_OF_YEAR,31);
                          Date dt1=rightNow.getTime();
                          List<SegmentInfo>  segmentInfos  =new ArrayList<>();

                          int res = date.compareTo(dt1);
                         //date 实现了Comparable 接口 调用compareTo(Date date)方法可以比较大小,相等则返回0,date大返回1,否则返回-1;
                                if (res == 1){

                                    validation.setStatus("pass");
                                    validation.setCode(null);
                                    validation.setMessage(null);
                                    ancillaryProducts1.setValidation(validation);
                                    List<String> list1 = new ArrayList<String>();
                                    ancillaryProducts1.setEmdAirlineCode(products.getEmdInfo().get(0).getEmdAirlineCode());

                                    for (SegmentInfo segmentInfo1 :ancillaryProducts.getSegmentInfo()){
                                        segmentInfo1.setFlightNumber(products.getSegmentInfo().getFlightNumber());
                                        segmentInfos.add(segmentInfo1);
                                    }
                                    ancillaryProducts1.setSegmentInfo(segmentInfos);


                                    ancillaryProducts1.setEmdNumber(list1);
                                    for (EmdInfoVo vo :products.getEmdInfo()){
                                        list1.add(vo.getEmdNumber());
                                    }
                                    productsList1.add(ancillaryProducts1);
                                }else {
                                    validation.setCode("VF_AESRSH_C003");
                                    validation.setMessage("Refund request needed to be submit within one month after departure");
                                    validation.setStatus("fail");
                                    ancillaryProducts1.setValidation(validation);
                                    productsList1.add(ancillaryProducts1);
                                }

                     }else {

                         validation.setCode("VF_AESRSH_C002");
                         validation.setMessage("Segment info not match");
                         validation.setStatus("fail");
                         ancillaryProducts1.setValidation(validation);
                         productsList1.add(ancillaryProducts1);
                     }
                 }else {
                     validation.setCode("VF_AESRSH_C001");
                     validation.setMessage("Passenger not found");
                     validation.setStatus("fail");
                     ancillaryProducts1.setValidation(validation);
                     productsList1.add(ancillaryProducts1);
                 }
                  a++;
                 if (a == productsList.size()){
                     int b=0;
                     int c=0;
                     for (AncillaryProducts ancillaryProduct :productsList1){

                        //Add one at foreach a time
                         if (ancillaryProduct.getValidation().getStatus().equals("pass")) {
                             b++;
                             // 直接在这里给值
                             ancillaryProducts.setValidation(ancillaryProduct.getValidation());
                             ancillaryProducts.setEmdAirlineCode(ancillaryProduct.getEmdAirlineCode());
                             ancillaryProducts.setEmdNumber(ancillaryProduct.getEmdNumber());
                             ancillaryProducts.setSegmentInfo(ancillaryProduct.getSegmentInfo());
                         }else {
                             c++;
                             // if all ancillaryProducts with validation/status = 'fail'
                             if (c == productsList1.size()){
//                                 if (ancillaryProduct.getValidation().getCode().equals(""))
                                 ancillaryProducts.setValidation(ancillaryProduct.getValidation());
                             }
                         }

                         if (ancillaryProduct.getValidation().getStatus().equals("pass") && ancillaryProduct.getEmdNumber().size() > 1){

                             error.setCode("VU_AESRSH_U003");
                             error.setStatus("unidentified");
                             error.setMessage("Multiple EMD/Coupon matched");
                             error.setDetail("Multiple EMD/Coupon matched");
                             validation.setStatus("manual");
                             validation.setCode(null);
                             validation.setMessage(null);
                             ancillaryProducts.setValidation(validation);
                         }
                         //if 1 inbound request ancillaryProduct can match with more than 1 record in the getAncillaryBooking response
                         if (b > 1){

                             error.setCode("VU_AESRSH_U003");
                             error.setStatus("unidentified");
                             error.setMessage("Multiple EMD/Coupon matched");
                             error.setDetail("Multiple EMD/Coupon matched");
                             validation.setStatus("manual");
                             validation.setCode(null);
                             validation.setMessage(null);
                             ancillaryProducts.setEmdAirlineCode(ancillaryProduct.getEmdAirlineCode());
                             ancillaryProducts.setSegmentInfo(ancillaryProduct.getSegmentInfo());
                             ancillaryProducts.setEmdNumber(ancillaryProduct.getEmdNumber());
                             ancillaryProducts.setValidation(validation);
                         }

                     }
                     resultList.add(ancillaryProducts);
                     // chear productsListl
                     productsList1.clear();
                     //for jump out of the loop
                     continue continueOut;
                 }
             }
         }
        refundInfo.setRloc(request.getRloc());
        refundInfo.setSource(request.getSource());
        refundInfo.setAncillaryProducts(resultList);


        validationCheckResponse.setError(error);
        validationCheckResponse.setRefundInfo(refundInfo);
        //Check if any ancillaryProducts with same id
        return validationCheckResponse;
    }
    // get the first Api data by request.getRloc
    private AncillaryBookingResponse getAncillaryBookingResponse (String uri) throws ConnectTimeoutException {
        // Check if this request time-out

        ResponseEntity<String> responseEntity =restTemplate.getForEntity(uri,String.class);

        ErrorBodyVo errorBodyVo = new ErrorBodyVo();

//        ResponseEntity<RefundInfo> responseEntity =restTemplate.getForEntity(uri,RefundInfo.class);
        System.out.println(responseEntity.getBody());

        // 这里已经获得了 第一条Api 拿到的值 和post请求的值进行对比  现在就是为了拿到第二条Api 的返回结果 其结果返回样式 其实就是ValidationCheckResponse的最后结果

        ObjectMapper objectMapper = new ObjectMapper();
        AncillaryBookingResponse ancillaryBookingResponse =null ;

        String strBody = null;

        if (responseEntity.getStatusCodeValue() == 200) {
            strBody = responseEntity.getBody();
        }
        try {
            ancillaryBookingResponse = objectMapper.readValue(strBody, AncillaryBookingResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // response == System error and errorBody accept it  when response'statusCodeValue == 403
        if (responseEntity.getStatusCodeValue() == 403){

            strBody = responseEntity.getBody();

            try {
                errorBodyVo = objectMapper.readValue(strBody, ErrorBodyVo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // judge if errorBody code == AESSS_ERR_EOO1
            if (errorBodyVo.getCode().equals("AESSS_ERR_E001")){
                error.setCode("VU_AESRSH_U001");
                error.setStatus("unidentified");
                error.setMessage("No record for this rloc in the database");
                error.setDetail("No record for this rloc in the database");
            }
            // judge if errorBody code == AESSS_ERR_EOO4
            if (errorBodyVo.getCode().equals("AESSS_ERR_E004")){
                error.setCode("VU_AESRSH_U002");
                error.setStatus("unidentified");
                error.setMessage("More than one matched record in the database");
                error.setDetail("More than one matched record in the database");
            }
        }


        return ancillaryBookingResponse;

    }



}
