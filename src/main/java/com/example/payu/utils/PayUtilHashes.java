package com.example.payu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.payu.model.PaymentDetail;
import com.payu.india.Payu.PayuConstants;

public class PayUtilHashes {

	private String mid;

	private String key;

	private String salt;

	private static final String sUrl = "http://ec2-13-232-217-251.ap-south-1.compute.amazonaws.com:8888/payment/payment-response";

	private static final String fUrl = "http://ec2-13-232-217-251.ap-south-1.compute.amazonaws.com:8888/payment/payment-response";

	public PaymentDetail getHashes(PaymentDetail paymentDetail) {
		String info = paymentDetail.getProductInfo();

		List<String> productInfo = new ArrayList<String>();
		productInfo.add("912");
		productInfo.add("913");
		HashMap<String, String> Account1 = new HashMap<String, String>();
		Account1.put("mid", "162085");
		Account1.put("key", "dHRGjc");
		Account1.put("salt", "Z6uZC4XQ");

		HashMap<String, String> Account2 = new HashMap<String, String>();
		Account2.put("mid", "162089");
		Account2.put("key", "kjLJx7");
		Account2.put("salt", "Scf8C2t5");

		if (info.equals(productInfo.get(0))) {
			mid = Account1.get("mid");
			key = Account1.get("key");
			salt = Account1.get("salt");
			paymentDetail.setKey(Account1.get("key"));
			paymentDetail.setSalt(Account1.get("salt"));
		} else if (info.equals(productInfo.get(1))) {
			mid = Account2.get("mid");
			key = Account2.get("key");
			salt = Account2.get("salt");
			paymentDetail.setKey(Account2.get("key"));
			paymentDetail.setSalt(Account2.get("salt"));
		} else {

			throw new NotFoundException("not found");

		}

		System.out.println("PayUtilHashes.getHashes()" + mid + " " + key + " " + salt);
		String user_credentials = paymentDetail.getUser_credentials();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String idx = String.valueOf(timestamp.getTime());

		paymentDetail.setTxnId(idx);

		String ph = checkNull(key) + "|" + checkNull(paymentDetail.getTxnId()) + "|"
				+ checkNull(paymentDetail.getAmount()) + "|" + checkNull(paymentDetail.getProductInfo()) + "|"
				+ checkNull(paymentDetail.getName()) + "|" + checkNull(paymentDetail.getEmail()) + "|"
				+ checkNull(paymentDetail.getUdf1()) + "|" + checkNull(paymentDetail.getUdf2()) + "|"
				+ checkNull(paymentDetail.getUdf3()) + "|" + checkNull(paymentDetail.getUdf4()) + "|"
				+ checkNull(paymentDetail.getUdf5()) + "||||||" + salt;

		String paymentHash = getSHA(ph);

		paymentDetail.setHash(paymentHash);

		paymentDetail.setMobile_sdk_hash(generateHashString(PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT));

		if (!checkNull(user_credentials).isEmpty()) {

			paymentDetail.setPAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH(
					generateHashString(PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, user_credentials));

			paymentDetail.setGET_USER_CARDS_HASH(generateHashString(PayuConstants.GET_USER_CARDS, user_credentials));

			paymentDetail.setEDIT_USER_CARD_HASH(generateHashString(PayuConstants.GET_USER_CARDS, user_credentials));

			paymentDetail.setSAVE_USER_CARD_HASH(generateHashString(PayuConstants.GET_USER_CARDS, user_credentials));

			paymentDetail.setDELETE_USER_CARD_HASH(generateHashString(PayuConstants.GET_USER_CARDS, user_credentials));

		} else {
			paymentDetail.setPAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH(
					generateHashString(PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, PayuConstants.DEFAULT));

		}

		paymentDetail.setKey(key);

		paymentDetail.setSUrl(sUrl);

		paymentDetail.setFUrl(fUrl);

		paymentDetail.setMid(mid);
		
		return paymentDetail;

	}

	// This method generates hash string

	private String generateHashString(String command, String var1) {
		return getSHA(key + "|" + command + "|" + var1 + "|" + salt);
	}

	private String checkNull(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

	private String getSHA(String str) {

		MessageDigest md;
		String out = "";
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(str.getBytes());
			byte[] mb = md.digest();

			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return out;

	}

}
