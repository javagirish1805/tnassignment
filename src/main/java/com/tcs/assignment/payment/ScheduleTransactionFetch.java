package com.tcs.assignment.payment;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * Class to manage scheduler of payment record consuming system
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ScheduleTransactionFetch { 

	
	/**
	 * Method to schedule task for fetching data from the given file
	 */
	public void scheduleMonitoringTransactions() {
	 
		try {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(processTransactionTask(), 0L, AppConstants.DATA_FETCH_FREQUENCY, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to create task for the Executor to repeat on given frequency
	 * @return Runnable
	 */
	private Runnable processTransactionTask() throws Exception{
	
		Runnable runnableTask = () -> {
			Optional<Transaction> transaction = ManageAccountTransaction.getInstance().fetchLastTransaction();
			ManageAccountTransaction.getInstance().printPaymentMessage(transaction);
		};
		
		return runnableTask;
	}
	
}
