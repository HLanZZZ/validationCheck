package com.accenture.aesrefund.constant;

public enum HttpStatusEnum {

	//Retrieve Cart
	RET_INVALID_CART_PARM(400,"AES_RC_E001","Invalid query parameter",""),
	RET_CART_NOT_EXIST(403,"AES_RC_E002","Cart does not exists","Unable to find this cart"),
	RET_CART_LOCKED(403,"AES_RC_E003","Cart is locked","Cart is locked because its payment process has started"),
	RET_CART_CANCELLED(403,"AES_RC_E004","Cart is cancelled",""),
	RET_CART_CLOSED(403,"AES_RC_E005","Cart is closed",""),
	//Delete Product From Cart
	DEL_INVALID_DEL_PRODUCT_PARM(400,"AES_DP_E001","Invalid query parameter",""),
	DEL_CART_NOT_EXIST(403,"AES_DP_E002","Cart does not exists","The cart ID provided is not a valid cart ID"),
	DEL_CART_LOCKED(403,"AES_DP_E003","Cart is locked","Cart is locked because its payment process has started"),
	DEL_CART_CANCELLED(403,"AES_DP_E004","Cart is cancelled","This cart has been cancelled"),
	DEL_CART_CLOSED(403,"AES_DP_E005","Cart is closed","This cart is closed because its order has completed"),
	DEL_PD_ITEM_NOT_EXIST(403,"AES_DP_E006","Product item does not exists",""),
	DEL_PD_ITEM_CANCELLED(403,"AES_DP_E007","Product item is already cancelled",""),
	DEL_CONCURRENT(412,"AES_DP_E008","Product item was modified","The carts product item has been modified"),
	//Add Product to Cart
	ADD_INVALID_CART_PARM(400,"AES_AP_E001","Invalid parameter",""),
	ADD_INCOMPLETE_BODY(400,"AES_AP_E002","Incomplete request body",""),	
	ADD_WRONG_BODY_FORMAT(400,"AES_AP_E003","Request body in wrong format",""),
	ADD_PRODUCT_EXIST(403,"AES_AP_E004","Product already exists","Product is already exists in this cart"),
	ADD_INVALID_PRODUCT_ID(403,"AES_AP_E005","Invalid product ID","The product ID provided is not a valid product ID"),
	ADD_CART_NOT_EXIST(403,"AES_AP_E006","Cart does not exists","The cart ID provided is not a valid cart ID"),
	ADD_CART_LOCKED(403,"AES_AP_E007","Cart is locked","Cart is locked because its payment process has started"),
	ADD_CART_CANCELLED(403,"AES_AP_E008","Cart is cancelled","This cart has been cancelled"),
	ADD_CART_CLOSED(403,"AES_AP_E009","Cart is Closed","The cart is closed because its order has completed"),
	ADD_NOT_SAME_CHAR(403,"AES_AP_E010","Invalid Characteristic","Air Product and product Characteristic not the same"),
	ADD_PRODUCT_EXCEED(403,"AES_AP_E011","Exceed max number of product item ","Number of product item exceed in cart"),
	ADD_CONCURRENT(412,"AES_AP_E012","Product item was modified","The carts product item has been modified"),
	ADD_PRODUCT_EXCEED_IN_CART(403,"AES_AP_E013","Exceed max of adding product item ","Number of product waiting to add in request is exceed the rest number of product allowed to add in cart"),
	ADD_PRODUCT_EXCEED_AT_REQUEST(403,"AES_AP_E014","Exceed max of adding product item in one request",""),
	ADD_TEST(400,"AES_AP_XXXX","XXXX","XXXX"),
	//Create Cart
	CREATE_INVALID_CART_PARM(400,"AES_CC_E001","Invalid query parameter",""),
	CREATE_INCOMPLETE_BODY(400,"AES_CC_E002","Incomplete request body",""),
	CREATE_WRONG_BODY_FORMAT(400,"AES_CC_E003","Request body in wrong format",""),
	CREATE_INVALID_PRODUCT_ID(403,"AES_CC_E004","Invalid product ID","The product ID provided is not a valid product ID"),
	//Update Cart
	UPDATE_INVALID_CART_PARM(400,"AES_UP_E001","Invalid query parameter",""),
	UPDATE_CART_NOT_EXIST(400,"AES_UP_E002","Cart does not exists","The cart ID provided is not a valid cart ID"),
	UPDATE_CART_LOCKED(403,"AES_UP_E003","Cart is locked","Cart is locked because its payment process has started"),
	UPDATE_CART_CANCELLED(403,"AES_UP_E004","Cart is cancelled","This cart has been cancelled"),
	UPDATE_CART_CLOSED(403,"AES_UP_E005","Cart is Closed","The cart is closed because its order has completed"),
	UPDATE_PD_ITEM_NOT_EXIST(403,"AES_UP_E006","Product item does not exists in this cart",""),
	UPDATE_PD_ITEM_CANCELLED(403,"AES_UP_E007","Product item is already cancelled",""),
	UPDATE_NOT_SAME_CHAR(403,"AES_UP_E008","Invalid Characteristic","Air Product and product Characteristic not the same"),
	UPDATE_CONCURRENT(412,"AES_UP_E009","Product item was modified","The carts product item has been modified"),
	UPDATE_TEST(400,"AES_UP_XXXX","XXXX","XXXX"),
	//Prices
	PRICES_MISSING_PARM(400,"AES_PP_E001","Query parameter missing","Query parameter missing"),
	PRICES_INVALID_PARM(400,"AES_PP_E002","Invalid query parameter","Invalid query parameter"),
	PRICES_CART_NOT_EXIST(403,"AES_PP_E003","Cart does not exists","The cart ID provided is not a valid cart ID"),
	PRICES_CART_LOCKED(403,"AES_PP_E004","Cart is locked","Cart is locked because its payment process has started"),
	PRICES_CART_CANCELLED(403,"AES_PP_E005","Cart is cancelled","This cart has been cancelled"),
	PRICES_CART_CLOSED(403,"AES_PP_E006","Cart is closed","This cart is closed because its order has completed"),
	PRICES_SEAT_NOT_EXIST(403,"AES_PP_E007","No seat exists for the requested segments","No seat exists for the requested segments"),
	PRICES_POS_NOT_EXIST(403,"AES_PP_E008","Invalid POS","The POS provided is not supported"),
//	PRICES_NOT_AVAILABLE(403,"AES_PP_E009","Unavailable seats exists in the cart","Some of the product items in the cart is not available anymore, please remove them and retry"),
	PRICES_PNR_RET_1AWS_ERR(403,"AES_PP_A001","1A webservices error",""),
	PRICES_PNR_CANCEL_1AWS_ERR(403,"AES_PP_A002","1A webservices error",""),
	PRICES_MULT_ELEMENT_NOT4676_1AWS_ERR(403,"AES_PP_A003","1A webservices error",""),
	PRICES_ADD_PRICING_1AWS_ERR(403,"AES_PP_A004","1A webservices error",""),
	PRICES_MULT_ELEMENT_4676_1AWS_ERR(403,"AES_PP_E010","Seat items already exists for requested segments",""),
	PRICES_EMD_EXIST(403,"AES_PP_E013","EMD attached with a seat","The seat is attached with a EMD, system unable to exchange it with a extra long leg room seat"),
	//Products
	PRODUCT_MISSING_PARM(400,"AES_FP_E001","Query parameter missing","Query parameter missing"),
	PRODUCT_WRONG_FORMAT_PARM(400,"AES_FP_E002","Invalid query parameter","Invalid query parameter"),
	PRODUCT_POS_NOT_EXIST(403,"AES_FP_E003","Invalid POS","The POS provided is not supported"),
	PRODUCT_POS_NOT_MATCH(403,"AES_FP_E004","Wrong POS provided","POS provided does not match with the booking creation POS"),
	PRODUCT_PNR_RET_1AWS_ERR(403,"AES_FP_A001","1A webservices error",""),
	PRODUCT_CATALOG_RET_1AWS_ERR(403,"AES_FP_A002","1A webservices error",""),
	//Book order(payment)
			PAYMENT_MISSING_PARM(400,"AES_BO_E001","Incomplete request body","Incomplete request body"),
			PAYMENT_SIGN_NOT_MATCH(403,"AES_BO_E002","Signature not match","The signature provided does not match with the API request content"),
			PAYMENT_INVALID_POS(403,"AES_BO_E003","Invalid POS","The POS provided is not supported"),
			PAYMENT_INVALID_CHANNEL(403,"AES_BO_E004","Invalid channel","The channel provided is not a valid channel"),
			PAYMENT_INVALID_CART_STATUS(403,"AES_BO_E005","Invalid cart status","This cart is locked, its not allowed to proceed payment"),
			PAYMENT_INVALID_ORDER(403,"AES_BO_E006","Invalid order","This order number provided is not valid"),
			PAYMENT_INVALID_ORDER_STATUS(403,"AES_BO_E007","Invalid order status","This order is locked, its not allowed to proceed payment"),
			PAYMENT_INVALID_PRD_ITEM(403,"AES_BO_E008","Invalid product item exists in the cart","Some of the product items is not valid anymore in the cart, please remove it and retry"),
			PAYMENT_NO_SEAT(403,"AES_BO_E009","No seat exists for the requested segments","No seat exists for the requested segments, exchange flag for that should not be true"),
		    PAYMENT_AMT_CR_NOT_MATCH(403,"AES_BO_E010","Total amount/currency not match","The pricing total amount/currency of the cart may have changed, its not match with the value provided in the request"),
		    PAYMENT_MULT_ELEMENT_4676_1AWS_ERR(403,"AES_BO_E011","Seat items already exists for requested segments",""),
		    PAYMENT_INFO_INCORRECT(403,"AES_BO_E012","Incorrect payment info","The payment info provided is not correct"),
		    PAYMENT_EMD_EXIST(403,"AES_BO_E013","EMD attached with a seat","The seat is attached with a EMD, system unable to exchange it with a extra long leg room seat"),
		    PAYMENT_MISSING_ALI_PARM(400,"AES_BO_E014","Incomplete request body",""),
		    PAYMENT_ORDER_NOT_AUTH(403,"AES_BO_E015","The order is not authorized","The order is not authorized"),
		    PAYMENT_AMT_CUR_NOT_MATCH(403,"AES_BO_E016","The amount or currency is not match","CPST authorized amount/currency doest not match with the Cart amount/currency"),
		    // add by ywei
		    PAYMENT_PLATFORM_NOT_SUPPORTED(403,"AES_BO_E017","Payment platform not supported","The payment platform of this order is not supported for this book order API version"),
		    PAYMENT_METHOD_NOT_SUPPORTED(403,"AES_BO_E018","Payment method not supported","The payment method of this order is not supported for this book order API version"),
		    PAYMENT_CHECK_SIGNATURE(403,"AES_BO_C002","Signature verification failed","The signature in the request does not match with the request content"),
		    
		    PAYMENT_AES_BO_A001(403,"AES_BO_A001","1A webservices error",""),
			PAYMENT_AES_BO_A002(403,"AES_BO_A002","1A webservices error",""),
			PAYMENT_AES_BO_A003(403,"AES_BO_A003","1A webservices error",""),
			PAYMENT_AES_BO_A004(403,"AES_BO_A004","1A webservices error",""),
			PAYMENT_AES_BO_A005(403,"AES_BO_A005","1A webservices error",""),
			PAYMENT_AES_BO_A006(403,"AES_BO_A006","1A webservices error",""),
			PAYMENT_CPST_WS_ERROR(403,"AES_BO_C001","Payment platform webservice error",""),
	//Create_Orders
	CRE_ORDER_INCOMPLETE_BODY(400,"AES_CO_E001","Incomplete request body",""),
	CRE_ORDER_SIGNATURE_NOT_MATCH(403,"AES_CO_E002","Signature not match","The signature provided does not match with the API request content"),
	CRE_ORDER_INVALID_CHANNEL(403,"AES_CO_E003","Invalid channel","The channel provided is not a valid channel"),
	CRE_ORDER_CART_NOT_EXISTS(403,"AES_CO_E005","Cart does not exists","The cart provided is not a valid cart"),
	CRE_ORDER_CART_CANCELLED(403,"AES_CO_E006","Cart is cancelled","This cart has been cancelled"),
	CRE_ORDER_CART_CLOSED(403,"AES_CO_E007","Cart is closed","This cart is closed because its order has completed"),
	CRE_ORDER_INVALID_PRODUCT_ITEM(403,"AES_CO_E008","Invalid product item exists in the cart","Some of the product items is not valid anymore in the cart, please remove it and retry"),
	CRE_ORDER_NO_PRODUCT_EXIST(403,"AES_CO_009","No products in the cart","No products in the cart"),
	CRE_ORDER_CART_LOCKED(403,"AES_CO_E010","Cart is locked","Cart is locked because its payment process has started"),
	CRE_ORDER_INCORRECT_PAYMENT(403,"AES_CO_E011","Incorrect payment info","The payment info provided is not correct"),
	CRE_ORDER_CPST_WS_ERROR(403,"AES_CO_C001","CPST webservice error",""),	
	CRE_ORDER_MERCHANT_PRD_ID_MISSING(403,"AES_CO_E012","Incomplete request body", "MerchantProductId field is missing, merchantProductId has no value"),
	CRE_ORDER_1AWS_ERR(403,"AES_CO_A001","1A webservices error",""),
	CRE_ORDER_CPST_ERR(403,"AES_CO_C001","CPST webservice error",""),
	//Retrieve_order
	RET_ORDER_MISSING_FIELD(400,"AES_RO_E001","Invalid query parameter",""),
	RET_ORDER_NOT_EXIST(403,"AES_RO_E002","Order not exists","Unable to find this Order"),
	RET_ORDER_CANCELLED(403,"AES_RO_E003","Order is cancelled","This order has been cancelled"),
	RET_ORDER_CART_NOT_MATCH(403,"AES_RO_E004","Incorrect CartId","This CartId provided does not match with the order"),
	RET_ORDER_SSR_NOT_FOUND(403,"AES_RO_E005","Order in unstable status","This order is in unstable status, please start the booking flow again"),
	RET_ORDER_1AWS_ERR(403,"AES_RO_A001","1A webservices error",""),
	//validation-check  error response
	MANDATORY_FIELD_MISSING(400,"VS_AESRSH_E001","Malformed request","The request could not be understood by the server due to malformed syntax "),
	SOURCE_BE_EITHER_INT_OR_EXT(403,"VS_AESRSH_E002","Invalid source value","Source should be either 'INT' or 'EXT'"),
	CHECK_IF_SAME_ID(403,"VS_AESRSH_E008","Mulitple products with same id","Mulitple products with same id"),


	//General Error
	COMMON_WRONG_BODY_FORMAT(400,"AES_GE_E004","Request body in wrong format",""),
	//COMMON_MISSING_FIELD(400,"AES_GE_E005","Incomplete request body",""),
	COMMON_UNEXPECTED_ERROR(400,"AES_GE_E005","Unexpected Error",""),
	COMMON_MISSING_PARM(400,"AES_GE_E006","Invalid query parameter",""),
	COMMON_PAGE_NOTFOUND(404,"AES_GE_E001","Page not found",""),
	COMMON_DB_NO_CONNECTION(500,"AES_GE_E002","Unable to connect to the database",""),
	COMMON_CONNECTION_TIMEOUT(403,"VS_AESRSH_E004","Datastore connection timeout","The connection to the AESSS datastore time-out");


	
	private HttpStatusEnum(int statusCode, String code, String message, String detail) {
		this.statusCode = statusCode;
		this.code = code;
		this.message = message;
		this.detail = detail;
	}
	
	private final int statusCode;
	private final String code;
	private final String message;
/*	private final String detail;*/
	private String detail;
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	public void setDetail(String detail) {		
		this.detail = detail;
	}
	
	public String getDetail() {		
		if(detail.isEmpty()) {
			return message;
		}else {
			return detail;
		}
	}
}
