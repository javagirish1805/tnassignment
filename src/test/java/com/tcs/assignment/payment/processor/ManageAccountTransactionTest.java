package com.tcs.assignment.payment.processor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tcs.assignment.payment.model.Transaction;
import com.tcs.assignment.payment.processor.ManageAccountTransaction;
import com.tcs.assignment.payment.util.AppConstants;

/**
 * Test class to test ManageAccountTransaction
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ManageAccountTransactionTest {
    
	List<Transaction> testTransaction; 
	
	@Test
    public void testManageAccountTransactionSingleTon() {
		ManageAccountTransaction manageAccountTransaction = ManageAccountTransaction.getInstance();
		ManageAccountTransaction manageAccountTransaction1 = ManageAccountTransaction.getInstance();
		assertEquals(manageAccountTransaction.hashCode(), manageAccountTransaction1.hashCode(), "Being Singleton class, both hashCode should be same");
	}
	
	@Test
    public void testCreateTransaction() {
		Transaction newTrans = new Transaction(Instant.now(), 7777.0, 1111.0);
		ManageAccountTransaction.getInstance().createTransaction(newTrans);
		Optional<Transaction> transaction = ManageAccountTransaction.getInstance().fetchLastTransaction();
		assertTrue(transaction.isPresent(), "New transaction record created should be present when fetched");
		assertEquals(7777.0, transaction.get().getCreditAmount(), "Credit amount of new fetched record should match with input");
		assertEquals(1111.0, transaction.get().getDebitAmount(), "Debit amount of new fetched record should match with input");
		assertEquals(6666.0, transaction.get().getNetAmount(), "Net amount of new fetched record should match with input");
	}
	
	@Test
    public void failTransactionWithJunkData() {
		try(PrintWriter writer = new PrintWriter(new File(AppConstants.FILE_PATH))) {
			writer.print("dddd,dddd,dddd,dddd,ddd");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assertions.assertFalse(true, "No errors expected while writing data to file");
		}
		Assertions.assertThrows(Exception.class, () -> ManageAccountTransaction.getInstance().fetchLastTransaction(), "Exception expected while fetching junk data from file");
	}
	
	@Test
	public void testPrintPaymentMessage() {

		Transaction newTransNull = null;
		Transaction newTrans = new Transaction(Instant.now(), 1000.0, 0.0);
		Transaction lastProcessedData = new Transaction(Instant.now(), 500.0, 0.0);
		Transaction newTransSameAslastProcessedData = new Transaction(Instant.now(), 500.0, 0.0);
		ManageAccountTransaction.lastProcessedData = Optional.ofNullable(lastProcessedData);

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTransNull)), "No exceptions expected while printing message with null transaction");

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTrans)), "No exceptions expected while printing message with valid transaction");

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTransSameAslastProcessedData)), "No exceptions expected while printing message with valid and same as earlier transaction");
	}
	 
	@Test
	public void testGetTransactionFromString() {
		String testDataNull = null;
		Optional<Transaction> transactionNull = ManageAccountTransaction.getInstance().getTransactionFromString(testDataNull);
		assertFalse(transactionNull.isPresent(), "Transaction object should not be returned for a null test data");
		
		String testDataEmpty = "";
		Optional<Transaction> transactionEmpty = ManageAccountTransaction.getInstance().getTransactionFromString(testDataEmpty);
		assertFalse(transactionEmpty.isPresent(), "Transaction object should not be returned for an empty test data");
		
		String testDataLessSize = "2019-12-03T19:34:34.837Z,10000.0,0.0";
		Optional<Transaction> transactionDataLessSize = ManageAccountTransaction.getInstance().getTransactionFromString(testDataLessSize);
		assertFalse(transactionDataLessSize.isPresent(), "Transaction object should not be returned for incomplete test data");
		
		String testData = "2019-12-03T19:34:34.837Z,10000.0,0.0,10000.0,10000.0";
		Optional<Transaction> transaction = ManageAccountTransaction.getInstance().getTransactionFromString(testData);
		assertTrue(transaction.isPresent(), "Transaction object should be returned for a valid test data");
	}
	
	@BeforeEach
	public void prepareTestData() {
		testTransaction = new ArrayList<Transaction>();
		for(int i=10; i<15; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Assertions.assertFalse(true);
			}
			Transaction newTrans = new Transaction(Instant.now(), i*1000.0, 0.0);
			ManageAccountTransaction.getInstance().createTransaction(newTrans);
			testTransaction.add(newTrans);
		}
	}
	
	@AfterEach
	public void tearDown() {
		try(PrintWriter writer = new PrintWriter(new File(AppConstants.FILE_PATH))) {
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assertions.assertFalse(true);
		}
	}
}
