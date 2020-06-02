package com.example.payu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hashes {

	public String key;
	public String txnid;
	public String amount;
	public String productinfo;
	public String firstname;
	public String email;
	public String udf1;
	public String udf2;
	public String udf3;
	public String udf4;
	public String udf5;
	public String salt;
	public String var1;
	public String command;
	public String subventionAmount;

}
