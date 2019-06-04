package com.accenture.aesrefund.controller;

import com.accenture.aesrefund.model.AncillaryBookingVo;
import com.accenture.aesrefund.service.AncillaryBookingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
public class AncillaryProductsController {



    @Autowired
    AncillaryBookingDataService ancillaryBookingDataService;

    @RequestMapping(value = "/ancillary_product/{bookingRef}", method = RequestMethod.POST)
    public AncillaryBookingVo getAncillaryBookingByBookingRef(@PathVariable(value = "bookingRef",required = true)String rloc) {

        AncillaryBookingVo ancillaryBookingVo =new AncillaryBookingVo();
        ancillaryBookingVo.setRloc(ancillaryBookingDataService.getDataByBookingRef(rloc).getRloc());
        ancillaryBookingVo.setAncillaryProducts(ancillaryBookingDataService.getDataByBookingRef(rloc).getAncillaryProducts());



        return  ancillaryBookingVo;

    }







}
