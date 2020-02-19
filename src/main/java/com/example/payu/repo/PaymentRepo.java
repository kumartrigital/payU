package com.example.payu.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payu.entity.Payment;

public interface PaymentRepo  extends JpaRepository<Payment, Long>{

    Payment findByTxnId(String txnId);

}
