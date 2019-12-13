package com.tcs.assignment.payment.scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tcs.assignment.payment.model.Transaction;
import com.tcs.assignment.payment.processor.ManageAccountTransaction;
import com.tcs.assignment.payment.util.AppConstants;

/**
 *
 * Class to test ScheduleTransactionFetch
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ScheduleTransactionFetchTest {
	
	static Logger logger = Logger.getLogger(ScheduleTransactionFetchTest.class);
	
	@Test
    public void testScheduler() {
		try {
			ScheduleTransactionFetch scheduleTransactionFetch = new ScheduleTransactionFetch();
			scheduleTransactionFetch.scheduleMonitoringTransactions();
			
			for(int i=10; i<14; i++) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ex) {
					logger.error(ex);
					assertTrue(false, "No exceptions expected while running scheduler");
				}
				Transaction newTrans = new Transaction(Instant.now(), i*1000.0, 0.0);
				ManageAccountTransaction.getInstance().createTransaction(newTrans);
			}
			
		} catch (Exception ex) {
			logger.error(ex);
			assertTrue(false, "No exceptions expected while running scheduler");
		}
		//Test case passes when no exceptions occurred
		assertTrue(true, "No exceptions expected while running scheduler");
		
	}

	@BeforeEach
	public void prepareTestData() {
		Transaction newTrans = new Transaction(Instant.now(), 200.0, 100.0);
		ManageAccountTransaction.getInstance().createTransaction(newTrans);
	}
	
	@AfterEach
	public void tearDown() {
		try(PrintWriter writer = new PrintWriter(new File(AppConstants.FILE_PATH))) {
			writer.print("");
			writer.close();
		} catch (FileNotFoundException ex) {
			logger.error(ex);
			assertTrue(false, "No exceptions expected while tear down");
		}
	}
	
}
