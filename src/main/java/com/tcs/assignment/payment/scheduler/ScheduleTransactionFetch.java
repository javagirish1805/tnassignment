package com.tcs.assignment.payment.scheduler;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.tcs.assignment.payment.model.Transaction;
import com.tcs.assignment.payment.processor.ManageAccountTransaction;
import com.tcs.assignment.payment.util.AppConstants;

/**
 *
 * Class to manage scheduler of payment record consuming system
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ScheduleTransactionFetch {
	
	static Logger logger = Logger.getLogger(ScheduleTransactionFetch.class);
	
	/**
	 * Method to schedule task for fetching data from the given file
	 */
	public void scheduleMonitoringTransactions() {
	 
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(processTransactionTask(), 0L, AppConstants.DATA_FETCH_FREQUENCY, TimeUnit.SECONDS);
		logger.debug("Monitoring transactions scheduled with a frequency of " + AppConstants.DATA_FETCH_FREQUENCY);
	}
	
	/**
	 * Method to create task for the Executor to repeat on given frequency
	 * @return Runnable
	 */
	private Runnable processTransactionTask(){
	
		Runnable runnableTask = () -> {
			Optional<Transaction> transaction = ManageAccountTransaction.getInstance().fetchLastTransaction();
			ManageAccountTransaction.getInstance().printPaymentMessage(transaction);
		};
		
		return runnableTask;
	}
		
}
