package com.tcs.assignment.payment;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals(manageAccountTransaction.hashCode(), manageAccountTransaction1.hashCode());
	}
	
	@Test
    public void testCreateTransaction() {
		Transaction newTrans = new Transaction(Instant.now(), 7777.0, 1111.0);
		ManageAccountTransaction.getInstance().createTransaction(newTrans);
		Optional<Transaction> transaction = ManageAccountTransaction.getInstance().fetchLastTransaction();
		assertTrue(transaction.isPresent());
		assertEquals(7777.0, transaction.get().getCreditAmount());
		assertEquals(1111.0, transaction.get().getDebitAmount());
		assertEquals(6666.0, transaction.get().getNetAmount());
	}
	
	@Test
    public void failTransactionWithJunkData() {
		try(PrintWriter writer = new PrintWriter(new File(AppConstants.FILE_PATH))) {
			writer.print("dddd,dddd,dddd,dddd,ddd");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Assertions.assertThrows(Exception.class, () -> ManageAccountTransaction.getInstance().fetchLastTransaction());
	}
	
	@Test
	public void testPrintPaymentMessage() {

		Transaction newTransNull = null;
		Transaction newTrans = new Transaction(Instant.now(), 1000.0, 0.0);
		Transaction lastProcessedData = new Transaction(Instant.now(), 500.0, 0.0);
		Transaction newTransSameAslastProcessedData = new Transaction(Instant.now(), 500.0, 0.0);
		ManageAccountTransaction.lastProcessedData = Optional.ofNullable(lastProcessedData);

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTransNull)));

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTrans)));

		Assertions.assertDoesNotThrow(() -> ManageAccountTransaction.getInstance().printPaymentMessage(Optional.ofNullable(newTransSameAslastProcessedData)));
	}
	
	@Test
	public void testGetTransactionFromString() {
		String testDataNull = null;
		Optional<Transaction> transactionNull = ManageAccountTransaction.getInstance().getTransactionFromString(testDataNull);
		assertTrue(!transactionNull.isPresent());
		
		String testDataEmpty = "";
		Optional<Transaction> transactionEmpty = ManageAccountTransaction.getInstance().getTransactionFromString(testDataEmpty);
		assertTrue(!transactionEmpty.isPresent());
		
		String testDataLessSize = "2019-12-03T19:34:34.837Z,10000.0,0.0";
		Optional<Transaction> transactionDataLessSize = ManageAccountTransaction.getInstance().getTransactionFromString(testDataLessSize);
		assertTrue(!transactionDataLessSize.isPresent());
		
		String testData = "2019-12-03T19:34:34.837Z,10000.0,0.0,10000.0,10000.0";
		Optional<Transaction> transaction = ManageAccountTransaction.getInstance().getTransactionFromString(testData);
		assertTrue(transaction.isPresent());
	}
	
	@Test
	public void testUpdateBalance() {
		tearDown();
		
		Transaction prevTrans = new Transaction(Instant.now(), 7777.0, 0.0);
		ManageAccountTransaction.getInstance().createTransaction(prevTrans);
		Optional<Transaction> storedPrevTrans = ManageAccountTransaction.getInstance().fetchLastTransaction();
		
		Transaction newTrans = new Transaction(Instant.now(), 1.0, 0.0);
 
		Transaction expectedTrans = null;
		
		try { 
			Method method = ManageAccountTransaction.getInstance().getClass().getDeclaredMethod("updateBalance", Optional.class, Transaction.class);
			method.setAccessible(true); 
			expectedTrans = (Transaction) method.invoke(ManageAccountTransaction.getInstance(), storedPrevTrans, newTrans);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
 		assertEquals(7778.0, expectedTrans.getBalanceAmount());
	}
	
	@BeforeEach
	public void prepareTestData() {
		testTransaction = new ArrayList<Transaction>();
		for(int i=10; i<15; i++) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		}
	}
}
