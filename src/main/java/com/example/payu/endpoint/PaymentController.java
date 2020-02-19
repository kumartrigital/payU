package com.example.payu.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.payu.entity.PaymentMode;
import com.example.payu.model.PaymentCallBack;
import com.example.payu.model.PaymentDetail;
import com.example.payu.service.PaymentServiceImpl;

@RequestMapping("/payment")
@RestController
public class PaymentController {

	@Autowired
	private PaymentServiceImpl paymentService;

	@PostMapping(path = "/payment-details")
	public @ResponseBody PaymentDetail proceedPayment(@RequestBody PaymentDetail paymentDetail) {
		return paymentService.proceedPayment(paymentDetail);
	}

	@PostMapping(path = "/payment-details/app")
	public @ResponseBody PaymentDetail proceedPaymentApp(@RequestBody PaymentDetail paymentDetail) {
		return paymentService.proceedPaymentApp(paymentDetail);
	}

	@PostMapping(path = "/payment-response")
	public @ResponseBody String payuCallback(@RequestParam String mihpayid, @RequestParam String status,
			@RequestParam PaymentMode mode, @RequestParam String txnid, @RequestParam String hash) {
		PaymentCallBack paymentCallback = new PaymentCallBack();
		paymentCallback.setMihpayid(mihpayid);
		paymentCallback.setTxnid(txnid);
		paymentCallback.setMode(mode);
		paymentCallback.setHash(hash);
		paymentCallback.setStatus(status);
		return paymentService.payuCallback(paymentCallback);
	}

}