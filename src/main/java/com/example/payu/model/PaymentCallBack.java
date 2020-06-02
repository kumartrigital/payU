package com.example.payu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCallBack {

	private String txnid;
	private String mihpayid;
	private String mode;
	private String status;
	private String hash;
	


}
