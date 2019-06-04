package com.accenture.aesrefund.common;


import com.accenture.aesrefund.constant.HttpSourceEnum;
import com.accenture.aesrefund.constant.HttpStatusEnum;
import com.accenture.aesrefund.model.ErrorBodyVo;
import com.accenture.aesrefund.vo.WebServiceExceptionVo;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
//	private static CxLogger logger = CxLogManager.getLogger(GlobalExceptionHandler.class).defaultTag("log_type","Exception");

	@Autowired
    private HttpServletRequest request;
	
	
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
            WebServiceExceptionVo webServiceExceptionVo = new WebServiceExceptionVo();
	        ErrorBodyVo errorBodyVo = new ErrorBodyVo();
	        errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());
	    e.printStackTrace();
        HttpStatus httpStatus = null;
    	String url = request.getRequestURL().toString();
    	String reqMethod = request.getMethod();
    	String reqBody = "";
    	if("GET".equalsIgnoreCase(reqMethod)) {
    		reqBody = request.getQueryString();
    	}
        else if(e instanceof NumberFormatException)
        {    
        	errorBodyVo.setMessage(HttpStatusEnum.ADD_INVALID_CART_PARM.getMessage());
        	errorBodyVo.setDetails("Incorrect number format"); 	
        	errorBodyVo.setCode(HttpStatusEnum.ADD_INVALID_CART_PARM.getCode());
        	httpStatus = HttpStatus.BAD_REQUEST;
        }
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
                httpStatus = HttpStatus.NOT_FOUND;
		        errorBodyVo.setMessage(HttpStatusEnum.COMMON_PAGE_NOTFOUND.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.COMMON_PAGE_NOTFOUND.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.COMMON_PAGE_NOTFOUND.getCode());
        }
        else if (e instanceof MissingServletRequestParameterException)
        {        
        	httpStatus = HttpStatus.BAD_REQUEST;
        	 if(req.getRequestURL().toString().contains(HttpSourceEnum.PRICES.toString()) && req.getMethod().equals(RequestMethod.POST.toString()))
        	 {
 		        errorBodyVo.setMessage(HttpStatusEnum.PRICES_MISSING_PARM.getMessage());
 		        errorBodyVo.setDetails(HttpStatusEnum.PRICES_MISSING_PARM.getDetail());  
 		        errorBodyVo.setCode(HttpStatusEnum.PRICES_MISSING_PARM.getCode());
        	 }
        	 else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTS.toString()) && req.getMethod().equals(RequestMethod.GET.toString()))
        	 {
        		errorBodyVo.setMessage(HttpStatusEnum.PRODUCT_MISSING_PARM.getMessage());
  		        errorBodyVo.setDetails(HttpStatusEnum.PRODUCT_MISSING_PARM.getDetail());  
  		        errorBodyVo.setCode(HttpStatusEnum.PRODUCT_MISSING_PARM.getCode());
        	 }
        	 else if(req.getRequestURL().toString().contains(HttpSourceEnum.ORDERS.toString()) && req.getMethod().equals(RequestMethod.GET.toString()))
        	 {
        		HttpStatusEnum.RET_ORDER_MISSING_FIELD.setDetail("Cart id is missing");
        		errorBodyVo.setMessage(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getCode());
        	 }
        }
        else if(e instanceof org.springframework.web.HttpRequestMethodNotSupportedException)
        {
        	httpStatus = HttpStatus.BAD_REQUEST;
        	System.out.println("request url:"+req.getRequestURL());
        	System.out.println("request method"+req.getMethod());
        	if(req.getRequestURL().toString().contains(HttpSourceEnum.ORDERS.toString()) && req.getMethod().equals(RequestMethod.GET.toString()))
       	 	{   
        		HttpStatusEnum.RET_ORDER_MISSING_FIELD.setDetail("Order id is missing");
		        errorBodyVo.setMessage(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.RET_ORDER_MISSING_FIELD.getCode());
       	 	}
        }
        else if(e instanceof IllegalArgumentException)
        {
        	 httpStatus = HttpStatus.BAD_REQUEST;
        	 if(req.getRequestURL().toString().contains(HttpSourceEnum.PRICES.toString()) && req.getMethod().equals(RequestMethod.POST.toString()))
        	 {   
 		        errorBodyVo.setMessage(HttpStatusEnum.PRICES_INVALID_PARM.getMessage());
 		        errorBodyVo.setDetails(HttpStatusEnum.PRICES_INVALID_PARM.getDetail());  
 		        errorBodyVo.setCode(HttpStatusEnum.PRICES_INVALID_PARM.getCode());
        	 }
        	 else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTS.toString()) && req.getMethod().equals(RequestMethod.GET.toString()))
        	 {
        		errorBodyVo.setMessage(HttpStatusEnum.PRODUCT_WRONG_FORMAT_PARM.getMessage());
  		        errorBodyVo.setDetails(HttpStatusEnum.PRODUCT_WRONG_FORMAT_PARM.getDetail());  
  		        errorBodyVo.setCode(HttpStatusEnum.PRODUCT_WRONG_FORMAT_PARM.getCode());
        	 }
        }  
        else if(e instanceof ErrorResponseException)
        {     
        	httpStatus = HttpStatus.FORBIDDEN;
        	errorBodyVo.setMessage(((ErrorResponseException) e).getMessage());
        	errorBodyVo.setDetails(((ErrorResponseException) e).getDetail()); 	
        	errorBodyVo.setCode(((ErrorResponseException) e).getErrorCode());        
        }
        else if(e instanceof CommonWebServiceException) {           	        
	        errorBodyVo.setMessage(((CommonWebServiceException) e).getHttpStatusEnum().getMessage());
	        errorBodyVo.setDetails(((CommonWebServiceException) e).getHttpStatusEnum().getDetail());  
	        errorBodyVo.setCode(((CommonWebServiceException) e).getHttpStatusEnum().getCode());
	        errorBodyVo.setStatus("System Error");
	        httpStatus = HttpStatus.BAD_REQUEST;
	        if(((CommonWebServiceException) e).getHttpStatusEnum().getStatusCode() == HttpStatus.FORBIDDEN.value()) {
	        	httpStatus = HttpStatus.FORBIDDEN;
	        }else if (((CommonWebServiceException) e).getHttpStatusEnum().getStatusCode() == HttpStatus.PRECONDITION_FAILED.value()) {
	        	httpStatus = HttpStatus.PRECONDITION_FAILED;
	        }
        }
        else if(e instanceof InputParameterException)
        {
        	httpStatus = HttpStatus.FORBIDDEN;
        	errorBodyVo.setMessage(((InputParameterException) e).getMessage());
        	errorBodyVo.setDetails(((InputParameterException) e).getDetail()); 	
        	errorBodyVo.setCode(((InputParameterException) e).getErrorCode());    
        }  
        else if(e instanceof org.springframework.http.converter.HttpMessageNotReadableException)
        {
            httpStatus = HttpStatus.BAD_REQUEST;
	        //Add Product to Cart
	        if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())) {
		        errorBodyVo.setMessage(HttpStatusEnum.ADD_WRONG_BODY_FORMAT.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.ADD_WRONG_BODY_FORMAT.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.ADD_WRONG_BODY_FORMAT.getCode());	        
	        //Create Carts

	        }else if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())) {
		        errorBodyVo.setMessage(HttpStatusEnum.CREATE_WRONG_BODY_FORMAT.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.CREATE_WRONG_BODY_FORMAT.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.CREATE_WRONG_BODY_FORMAT.getCode());   		        
	        }else{
	        //Undefined default	    
		        errorBodyVo.setMessage(HttpStatusEnum.COMMON_WRONG_BODY_FORMAT.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.COMMON_WRONG_BODY_FORMAT.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.COMMON_WRONG_BODY_FORMAT.getCode());	  
	        }       	
        }
        else if(e instanceof org.springframework.dao.DataIntegrityViolationException)
        {
	        //Create Carts
	        if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())) {
		        errorBodyVo.setMessage(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getCode());	   		        
	        }else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.PUT.toString())){
		    //Add Product Item to Cart   
		        errorBodyVo.setMessage(HttpStatusEnum.ADD_INCOMPLETE_BODY.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.ADD_INCOMPLETE_BODY.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.ADD_INCOMPLETE_BODY.getCode());
	        }else {        
		        errorBodyVo.setMessage(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getMessage());
		        errorBodyVo.setDetails(e.toString());
		        errorBodyVo.setCode(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getCode());
	        }
            httpStatus = HttpStatus.BAD_REQUEST;
        }      
        else if(e instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException)
        {	                	
	        //Delete Carts
	        if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.DELETE.toString())) {
		        errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
		        errorBodyVo.setMessage(HttpStatusEnum.DEL_INVALID_DEL_PRODUCT_PARM.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.DEL_INVALID_DEL_PRODUCT_PARM.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.DEL_INVALID_DEL_PRODUCT_PARM.getCode());	   		        
	        }else if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.GET.toString())){
		    //Retrieve Cart    
	        	errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
		        errorBodyVo.setMessage(HttpStatusEnum.RET_INVALID_CART_PARM.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.RET_INVALID_CART_PARM.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.RET_INVALID_CART_PARM.getCode());
	        }else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.PUT.toString())){
		    //Update Cart    
	        	errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
		        errorBodyVo.setMessage(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getCode());

	        }else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())){
	        //Add product item 
	        	errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        		       
		        errorBodyVo.setMessage(HttpStatusEnum.ADD_INVALID_CART_PARM.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.ADD_INVALID_CART_PARM.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.ADD_INVALID_CART_PARM.getCode());	        
	        }else{
		        errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
		        errorBodyVo.setMessage(HttpStatusEnum.COMMON_MISSING_PARM.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.COMMON_MISSING_PARM.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.COMMON_MISSING_PARM.getCode());
	        }

	        webServiceExceptionVo.setError(errorBodyVo);  
	        httpStatus = HttpStatus.BAD_REQUEST;
   
	        return new ResponseEntity<Object>(webServiceExceptionVo,httpStatus);
        }
        else if(e instanceof NullPointerException)
        {
        	if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())){
		    //Add Product Item to Cart       
		        errorBodyVo.setMessage(HttpStatusEnum.ADD_INCOMPLETE_BODY.getMessage());
		        errorBodyVo.setDetails(HttpStatusEnum.ADD_INCOMPLETE_BODY.getDetail());  
		        errorBodyVo.setCode(HttpStatusEnum.ADD_INCOMPLETE_BODY.getCode());
        	}else if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())){
        	//Create Cart    
	    	    errorBodyVo.setMessage(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getMessage());
	    	    errorBodyVo.setDetails(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getDetail());  
	    	    errorBodyVo.setCode(HttpStatusEnum.CREATE_INCOMPLETE_BODY.getCode());	        
	        }else{        
		        errorBodyVo.setMessage(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getMessage());
				errorBodyVo.setDetails(e.toString());
				errorBodyVo.setCode(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getCode());
	        }    
	        httpStatus = HttpStatus.BAD_REQUEST;
        }
        else if(e instanceof DataAccessResourceFailureException
        		|| e instanceof CannotCreateTransactionException)
        {        	
        	errorBodyVo.setMessage(HttpStatusEnum.COMMON_DB_NO_CONNECTION.getMessage());
        	errorBodyVo.setDetails(HttpStatusEnum.COMMON_DB_NO_CONNECTION.getDetail()); 	
        	errorBodyVo.setCode(HttpStatusEnum.COMMON_DB_NO_CONNECTION.getCode());
        	httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }  
        else if(e instanceof ConnectTimeoutException)  // time-out
        {        	
        	errorBodyVo.setMessage(HttpStatusEnum.COMMON_CONNECTION_TIMEOUT.getMessage());
        	errorBodyVo.setDetails(HttpStatusEnum.COMMON_CONNECTION_TIMEOUT.getDetail());
        	errorBodyVo.setStatus("System error");
        	errorBodyVo.setCode(HttpStatusEnum.COMMON_CONNECTION_TIMEOUT.getCode());
        	httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }         
        else if(e instanceof ApplicationException)
        {    
        	errorBodyVo.setMessage(((ApplicationException) e).getMessage());
        	errorBodyVo.setDetails(((ApplicationException) e).getMessage()); 	
        	errorBodyVo.setCode(((ApplicationException) e).getErrorCode());
        	httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else if(e instanceof ServletRequestBindingException)
        {   
        	//lack of if-Cart-Unmodified-Since header
			//Delete Carts
			if(req.getRequestURL().toString().contains(HttpSourceEnum.CARTS.toString()) && req.getMethod().equals(RequestMethod.DELETE.toString())) {
			    errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
			    errorBodyVo.setMessage(HttpStatusEnum.DEL_INVALID_DEL_PRODUCT_PARM.getMessage());
			    errorBodyVo.setDetails(e.getMessage());  
			    errorBodyVo.setCode(HttpStatusEnum.DEL_INVALID_DEL_PRODUCT_PARM.getCode());
			    
			}else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.PUT.toString())){
			//Update Cart    
				errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
			    errorBodyVo.setMessage(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getMessage());
			    errorBodyVo.setDetails(e.getMessage());  
			    errorBodyVo.setCode(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getCode());
			
			}else if(req.getRequestURL().toString().contains(HttpSourceEnum.PRODUCTITEMS.toString()) && req.getMethod().equals(RequestMethod.POST.toString())){
			//Add product item 
				errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        		       
			    errorBodyVo.setMessage(HttpStatusEnum.ADD_INVALID_CART_PARM.getMessage());
			    errorBodyVo.setDetails(e.getMessage());  
			    errorBodyVo.setCode(HttpStatusEnum.ADD_INVALID_CART_PARM.getCode());	        
			}else if(req.getRequestURL().toString().contains(HttpSourceEnum.UPDATE_PRICES.toString()) && req.getMethod().equals(RequestMethod.PUT.toString())){
			//update product item prices
				errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        		       
			    errorBodyVo.setMessage(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getMessage());
			    errorBodyVo.setDetails(e.getMessage());  
			    errorBodyVo.setCode(HttpStatusEnum.UPDATE_INVALID_CART_PARM.getCode());
			}
			else{
			    errorBodyVo.setSource(HttpSourceEnum.SOURCE.toString());	        
			    errorBodyVo.setMessage(HttpStatusEnum.COMMON_MISSING_PARM.getMessage());
			    errorBodyVo.setDetails(HttpStatusEnum.COMMON_MISSING_PARM.getDetail());  
			    errorBodyVo.setCode(HttpStatusEnum.COMMON_MISSING_PARM.getCode());
			}
			webServiceExceptionVo.setError(errorBodyVo);  
			httpStatus = HttpStatus.BAD_REQUEST;
			
			return new ResponseEntity<Object>(webServiceExceptionVo,httpStatus);

        }
        else
        {
			
          	errorBodyVo.setMessage(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getMessage());
          	errorBodyVo.setDetails(e.getMessage()); 	
          	errorBodyVo.setCode(HttpStatusEnum.COMMON_UNEXPECTED_ERROR.getCode());
          	httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        webServiceExceptionVo.setError(errorBodyVo); 
        String reqInfo = " - method:" + request.getMethod()  +" - reqBody:"+ reqBody;
//        logger.info(errorBodyVo.getCode()+": "+errorBodyVo.getMessage()+" - "+errorBodyVo.getDetails() + " - url:"+ url + ("GET".equalsIgnoreCase(request.getMethod())? reqInfo : ""));
		ThreadContext.clearAll();
        return new ResponseEntity<Object>(webServiceExceptionVo,httpStatus);
    }
}
