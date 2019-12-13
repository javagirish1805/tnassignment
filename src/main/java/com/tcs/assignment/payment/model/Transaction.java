package com.tcs.assignment.payment.model;

import java.time.Instant;
import java.util.Objects;

import com.tcs.assignment.payment.util.AppConstants;

/**
 *
 * Class to hold transaction data
 *
 * @author Girish KHV
 * @version 1.0
 */
public class Transaction {

	private Instant  instant;
	private double creditAmount;
	private double debitAmount;
	private double netAmount;
	private double balanceAmount;
	
	public Transaction(Instant instant, double creditAmount, double debitAmount) {
		this.instant = instant;
		this.creditAmount = creditAmount;
		this.debitAmount = debitAmount;
	}
	
	
	public Instant getInstant() {
		return instant;
	}

	public double getCreditAmount() {
		return creditAmount;
	}
	
	public double getDebitAmount() {
		return debitAmount;
	}
	
	public double getNetAmount() {
		netAmount = creditAmount - debitAmount;
		return netAmount;
	}
	
	
	public double getBalanceAmount() {
		return balanceAmount;
	}


	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}


	@Override
	public String toString() {
		
		return getInstant().toString()
				+ AppConstants.DATA_DELIMITER
				+ getCreditAmount()
				+ AppConstants.DATA_DELIMITER
				+ getDebitAmount()
				+ AppConstants.DATA_DELIMITER
				+ getNetAmount()
				+ AppConstants.DATA_DELIMITER
				+ getBalanceAmount();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.instant, this.creditAmount, this.debitAmount, this.netAmount);
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Transaction)) {
			return false;			
		}
		return (this.hashCode() == object.hashCode());
		
	}
	
}
