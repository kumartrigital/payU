package com.example.payu.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.payu.entity.Payment;
import com.example.payu.model.PaymentCallBack;
import com.example.payu.model.PaymentDetail;
import com.example.payu.service.PaymentServiceImpl;

@RequestMapping("/payment")
@RestController
public class PaymentController {

	
	@Autowired
	private PaymentServiceImpl paymentService;

	@GetMapping
	public ModelAndView viewPaymentPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("paymentview");
		return model;
	}

	@PostMapping(path = "/payment-details")
	public @ResponseBody PaymentDetail proceedPayment(@RequestBody PaymentDetail paymentDetail) {

		return paymentService.proceedPayment(paymentDetail);
	}

	@PostMapping(path = "/payment-details/app")
	public @ResponseBody PaymentDetail proceedPaymentApp(@RequestBody PaymentDetail paymentDetail) {
		System.out.println("PaymentController.proceedPaymentApp()" + paymentDetail.toString());
		
		return paymentService.proceedPaymentApp(paymentDetail);
	}

	@PostMapping(path = "/payment-response")
	public @ResponseBody String payuCallback(@RequestParam String mihpayid, @RequestParam String status,
			@RequestParam String mode, @RequestParam String txnid, @RequestParam String hash) {

		System.out.println("mid" + mihpayid);
		System.out.println("status" + status);
		System.out.println("txnid" + txnid);
		System.out.println("hash" + hash);

		PaymentCallBack paymentCallback = new PaymentCallBack();
		paymentCallback.setMihpayid(mihpayid);
		paymentCallback.setTxnid(txnid);
		paymentCallback.setMode(mode);
		paymentCallback.setHash(hash);
		paymentCallback.setStatus(status);
		return paymentService.payuCallback(paymentCallback);
	}

	@GetMapping("/reports")
	public List<Payment> getAll() {
		System.out.println("PaymentController.getAll()");
		List<Payment> p = paymentService.getAllReports();
		return p;

	}

	@GetMapping("/pending")
	public List<Payment> getAllPendings(){
		return paymentService.getPendingPayments();
	}

}