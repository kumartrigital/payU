package com.example.payu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDetail {

	private String mid;
	private String email;
	private String name;
	private String phone;
	private String productInfo;
	private String amount;
	private String txnId;
	private String hash;
	
	private String user_credentials;
	private String mobile_sdk_hash;	
	
	private String PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH;
	
	private String DELETE_USER_CARD_HASH;
	private String GET_USER_CARDS_HASH;
	private String EDIT_USER_CARD_HASH;
	private String SAVE_USER_CARD_HASH;
	private String sUrl;
	private String fUrl;
	private String key;
	private String salt;
	private String Udf1;
	private String Udf2;
	private String Udf3;
	private String Udf4;
	private String Udf5;

	


}
