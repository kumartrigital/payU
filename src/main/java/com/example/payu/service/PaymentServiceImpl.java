package com.example.payu.service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.payu.entity.Payment;
import com.example.payu.entity.PaymentMode;
import com.example.payu.entity.PaymentStatus;
import com.example.payu.model.PaymentCallBack;
import com.example.payu.model.PaymentDetail;
import com.example.payu.repo.PaymentRepo;
import com.example.payu.utils.PayUtilHashes;
import com.example.payu.utils.PaymentUtil;

@Service
public class PaymentServiceImpl {

	@Autowired
	private PaymentRepo paymentRepository;

	public PaymentDetail proceedPayment(PaymentDetail paymentDetail) {
		PaymentUtil paymentUtil = new PaymentUtil();
		paymentDetail = paymentUtil.populatePaymentDetail(paymentDetail);
		savePaymentDetail(paymentDetail);
		return paymentDetail;
	}

	public PaymentDetail proceedPaymentApp(PaymentDetail paymentDetail) {
		PayUtilHashes payUtilHashes = new PayUtilHashes();

		paymentDetail = payUtilHashes.getHashes(paymentDetail);
		savePaymentDetail(paymentDetail);
		System.out.println("PaymentServiceImpl.proceedPaymentApp()" + paymentDetail);
		return paymentDetail;

	}

	public String payuCallback(PaymentCallBack paymentResponse) {
		String msg = "Transaction failed.";
		Payment payment = paymentRepository.findByTxnId(paymentResponse.getTxnid());
		if (payment != null) {
			// TODO validate the hash
			PaymentStatus paymentStatus = null;
			if (paymentResponse.getStatus().equals("failure")) {
				paymentStatus = PaymentStatus.Failed;
			} else if (paymentResponse.getStatus().equals("success")) {
				paymentStatus = PaymentStatus.Success;
				msg = "Transaction success";
			}
			payment.setPaymentStatus(paymentStatus);
			payment.setMihpayId(paymentResponse.getMihpayid());
			payment.setMode(PaymentMode.DC);
			paymentRepository.save(payment);
		}
		return msg;
	}

	private void savePaymentDetail(PaymentDetail paymentDetail) {
		Payment payment = new Payment();
		payment.setAmount(Double.parseDouble(paymentDetail.getAmount()));
		payment.setEmail(paymentDetail.getEmail());
		payment.setName(paymentDetail.getName());
		payment.setPaymentDate(new Date());
		payment.setPaymentStatus(PaymentStatus.Pending);
		payment.setPhone(paymentDetail.getPhone());
		payment.setProductInfo(paymentDetail.getProductInfo());
		payment.setTxnId(paymentDetail.getTxnId());
		payment.setHash(paymentDetail.getHash());
		payment.setMihkey(paymentDetail.getKey());
		payment.setMIHsalt(paymentDetail.getSalt());
		paymentRepository.save(payment);
	}

	public List<Payment> getAllReports() {
		List<Payment> p = paymentRepository.findAll();
		return p;
	}

	public List<Payment> getPendingPayments() {
		System.out.println("PaymentServiceImpl.getPendingPayments()");
		return paymentRepository.getPendingPayments("Pending");
	}

	public void updateData(TreeMap<String, String> treeMap) {

		Payment payment = paymentRepository.findByTxnId(treeMap.get("txid"));
		if (payment == null) {
			throw new EntityExistsException("some thing went wrong");
		}

		if (treeMap.get("status").equals("0")) {
			payment.setPaymentStatus(PaymentStatus.Failed);

		} else if (treeMap.get("status").equals("1")) {
			payment.setPaymentStatus(PaymentStatus.Success);
		}

		paymentRepository.save(payment);

	}

}
