package com.tcs.assignment.payment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * Class to test ScheduleTransactionFetch
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ScheduleTransactionFetchTest {
	
	@Test
    public void testScheduler() {
		try {
			ScheduleTransactionFetch scheduleTransactionFetch = new ScheduleTransactionFetch();
			scheduleTransactionFetch.scheduleMonitoringTransactions();
			
			for(int i=10; i<14; i++) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Transaction newTrans = new Transaction(Instant.now(), i*1000.0, 0.0);
				ManageAccountTransaction.getInstance().createTransaction(newTrans);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);//No exception thrown
		
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
