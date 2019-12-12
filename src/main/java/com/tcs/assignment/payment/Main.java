package com.tcs.assignment.payment;

import java.time.Instant;

/**
 *
 * Class to trigger sheduler of payment function
 * Populate test data for testing purpose
 *
 * @author Girish KHV
 * @version 1.0
 */
public class Main {
	
	
	public static Transaction latestData;

	public static void main(String[] args) {
		
		//Schedule data fetch on a defined frequency
		ScheduleTransactionFetch scheduleTransactionFetch = new ScheduleTransactionFetch();
		scheduleTransactionFetch.scheduleMonitoringTransactions();
		
		//Populate data with credit diff to file
		for(int i=10; i<15; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Transaction newTrans = new Transaction(Instant.now(), i*1000.0, 0.0);
			ManageAccountTransaction.getInstance().createTransaction(newTrans);
		}
		//Populate data with debit diff to file
		for(int i=5; i<7; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Transaction newTrans = new Transaction(Instant.now(), 0L, i*1000.0);
			ManageAccountTransaction.getInstance().createTransaction(newTrans);
		}
		
	}

}
