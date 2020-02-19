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

	private String email;
	private String name;
	private String phone;
	private String productInfo;
	private String amount;
	private String txnId;
	private String hash;
	private String sUrl;
	private String fUrl;
	private String key;
	
	
}
