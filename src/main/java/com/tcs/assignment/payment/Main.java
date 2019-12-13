package com.tcs.assignment.payment;

import java.time.Instant;

import org.apache.log4j.Logger;

import com.tcs.assignment.payment.model.Transaction;
import com.tcs.assignment.payment.processor.ManageAccountTransaction;
import com.tcs.assignment.payment.scheduler.ScheduleTransactionFetch;

/**
 *
 * Class to trigger scheduler of payment function
 * Populate test data for testing purpose
 *
 * @author Girish KHV
 * @version 1.0
 */
public class Main {
	
	static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) { 
		
		logger.debug("Starting Main for Payment System");
		
		//Schedule data fetch on a defined frequency
		ScheduleTransactionFetch scheduleTransactionFetch = new ScheduleTransactionFetch();
		scheduleTransactionFetch.scheduleMonitoringTransactions();
		
		//Populate data with credit difference to file
		for(int i=10; i<15; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException ex) {
				logger.error(ex);
			}
			Transaction newTrans = new Transaction(Instant.now(), i*1000.0, 0.0);
			ManageAccountTransaction.getInstance().createTransaction(newTrans);
		}
		//Populate data with debit difference to file
		for(int i=5; i<7; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException ex) {
				logger.error(ex);
			}
			Transaction newTrans = new Transaction(Instant.now(), 0L, i*1000.0);
			ManageAccountTransaction.getInstance().createTransaction(newTrans);
		}
		
	}

}
