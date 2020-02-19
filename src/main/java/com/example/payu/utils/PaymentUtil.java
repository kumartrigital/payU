package com.example.payu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import com.example.payu.model.PaymentDetail;

public class PaymentUtil {

	private static final String paymentKey = "yY5MfV";

	private static final String paymentSalt = "AI8ISMsD";

	private static final String sUrl = "http://ec2-13-232-217-251.ap-south-1.compute.amazonaws.com:8888/payment/payment-response";

	private static final String fUrl = "http://ec2-13-232-217-251.ap-south-1.compute.amazonaws.com:8888/payment/payment-response";
	
	public static PaymentDetail populatePaymentDetail(PaymentDetail paymentDetail) {
		String hashString = "";
		Random rand = new Random();
		String randomId = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
		// String txnId = "Dev" + hashCal("SHA-256", randomId).substring(0, 12);

		String txnId = new Date().getTime() + "";
		paymentDetail.setTxnId(txnId);
		String hash = "";
		
		String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||";
		hashString = hashSequence.concat(paymentSalt);
		hashString = hashString.replace("key", paymentKey);
		hashString = hashString.replace("txnid", txnId);
		hashString = hashString.replace("amount", paymentDetail.getAmount());
		hashString = hashString.replace("productinfo", paymentDetail.getProductInfo());
		hashString = hashString.replace("firstname", paymentDetail.getName());
		hashString = hashString.replace("email", paymentDetail.getEmail());
	
		hash = hashCal("SHA-512", hashString);
		paymentDetail.setHash(hash);
		paymentDetail.setFUrl(fUrl);
		paymentDetail.setSUrl(sUrl);
		paymentDetail.setKey(paymentKey);
		System.out.println("PaymentUtil.populatePaymentDetail()"+hash);
		paymentDetail.setHash(hash);
		return paymentDetail;
	}

	public static String hashCal(String type, String str) {
		byte[] hashseq = str.getBytes();
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest algorithm = MessageDigest.getInstance(type);
			algorithm.reset();
			algorithm.update(hashseq);
			byte messageDigest[] = algorithm.digest();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1) {
					hexString.append("0");
				}
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException nsae) {
		}
		return hexString.toString();
	}

}
