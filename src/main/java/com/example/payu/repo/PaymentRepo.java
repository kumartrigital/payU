package com.example.payu.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.payu.entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

	Payment findByTxnId(String txnId);

	@Query(value = "SELECT * FROM payment_payu p WHERE p.payment_status = ?1 or p.payment_status is NULL ", nativeQuery = true)
	List<Payment> getPendingPayments(String status);

}
