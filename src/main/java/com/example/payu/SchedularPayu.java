package com.example.payu;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.payu.entity.Payment;
import com.example.payu.service.PaymentServiceImpl;

@Component
public class SchedularPayu {

	@Autowired
	PaymentServiceImpl payUservice;

	@Autowired
	RestTemplate restTemplate;
						   
	@Scheduled(fixedRate = 3600000)
	public void payUStatus() throws IOException {

		String testUrl = "https://info.payu.in/merchant/postservice.php?form=2";
		String method = "verify_payment";
		// Salt of the merchant
		String salt = "";
		// Key of the merchant
		String key = "";

		String var1 = ""; // transaction id

		List<Payment> Pendingpayments = payUservice.getPendingPayments();
		for (Payment payment : Pendingpayments) {

			var1 = payment.getTxnId();
			salt = payment.getMIHsalt();
			key = payment.getMIHsalt();

			String toHash = key + "|" + method + "|" + var1 + "|" + salt;
			String Hashed = hashCal("SHA-512", toHash);

			String Poststring = "key=" + key + "&command=" + method + "&hash=" + Hashed + "&var1=" + var1;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("PRIVATE-TOKEN", "xyz");

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			map.add("key", key);
			map.add("command", method);
			map.add("hash", Hashed);
			map.add("var1", var1);

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<String> response = restTemplate.exchange(testUrl, HttpMethod.POST, entity, String.class);

			JSONObject jObject = new JSONObject(response.getBody());

			int status = jObject.getInt("status");
			System.out.println("SchedularPayu.payUStatus()" + status);

			TreeMap<String, String> treeMap = new TreeMap<String, String>();

			treeMap.put("status", Integer.toString(status));
			treeMap.put("txid", var1);

			payUservice.updateData(treeMap);
		}

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
				if (hex.length() == 1)
					hexString.append("0");
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException nsae) {
		}
		return hexString.toString();
	}

}