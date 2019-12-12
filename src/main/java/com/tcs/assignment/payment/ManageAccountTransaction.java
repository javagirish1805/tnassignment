package com.tcs.assignment.payment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Class to manage transactions of payment and consuming the payment entries and notify
 * Also holds logic of updating the balance in each payment record w.r.t earlier records
 *
 * @author Girish KHV
 * @version 1.0
 */
public class ManageAccountTransaction {

	// This is the placeholder to store last processed data from file. And used to compare against further entries
	// This approach is a temporary solution to this stand alone application. This wouldn't be right approach for a REST service
	public static Optional<Transaction> lastProcessedData = Optional.empty();

	/***** Enabling this class as Singleton ***/
	// static instance variable
	private static ManageAccountTransaction manageAccountTransaction;

	/**
	 * Private default constructor
	 */
	private ManageAccountTransaction() {
	}

	/**
	 * Create double checked singleton getInstance
	 * 
	 * @return ManageAccountTransaction
	 */
	public static ManageAccountTransaction getInstance() {
		if (manageAccountTransaction == null) {
			synchronized (ManageAccountTransaction.class) {
				if (manageAccountTransaction == null) {
					manageAccountTransaction = new ManageAccountTransaction();
				}
			}
		}
		return manageAccountTransaction;
	}

	/**
	 * Method to create new entries in the given file
	 * 
	 * @param newTransaction
	 */
	public void createTransaction(Transaction newTransaction) {

		try {
			newTransaction = updateBalance(fetchLastTransaction(), newTransaction);
			Files.write(Paths.get(getFilePath()), System.lineSeparator().concat(newTransaction.toString()).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to fetch last entry as Transaction object from the given file
	 * 
	 * @return Optional<Transaction>
	 */
	public Optional<Transaction> fetchLastTransaction() {
		
		try (Stream<String> stream = Files.lines(Paths.get(getFilePath()))) {
			String lastLineData = stream.reduce((line1, line2) -> line2).orElse(null);
			return getTransactionFromString(lastLineData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Method to validate and create Transaction object from String
	 * 
	 * @param lineData
	 * @return Optional<Transaction>
	 */
	public Optional<Transaction> getTransactionFromString(String lineData) {

		Transaction transaction = null;

		if (StringUtils.isNotEmpty(lineData)) {
			String[] lastTransactionData = lineData.toString().split(AppConstants.DATA_DELIMITER);
			if (lastTransactionData.length == AppConstants.TRANSACTION_DATA_LENGTH) {
				transaction = new Transaction(Instant.parse(lastTransactionData[0]),
						Double.valueOf(lastTransactionData[1]), Double.valueOf(lastTransactionData[2]));
				// Set the Balance Amount from String data
				transaction.setBalanceAmount(Double.valueOf(lastTransactionData[4]));
			}
		}
		return Optional.ofNullable(transaction);
	}

	/**
	 * Print payment message
	 * 
	 * @param  newTransaction
	 */
	protected void printPaymentMessage(Optional<Transaction> newTransaction) {

		if (newTransaction.isPresent() && !newTransaction.equals(lastProcessedData)) {
			System.out.println("Payment received with difference of "
					+ (newTransaction.get().getCreditAmount()
							- lastProcessedData.map(Transaction::getCreditAmount).orElse(AppConstants.ZERO_DOUBLE))
					+ " on credit field, and "
					+ (newTransaction.get().getDebitAmount()
							- lastProcessedData.map(Transaction::getDebitAmount).orElse(AppConstants.ZERO_DOUBLE))
					+ " on debit field, with a balance of " + newTransaction.get().getBalanceAmount());
		}
		lastProcessedData = newTransaction;
	}

	/**
	 * Business rule to update balance Amount in newTransaction
	 * 
	 * @param previousTransaction
	 * @param newTransaction
	 * @return newTransaction
	 */
	private Transaction updateBalance(Optional<Transaction> previousTransaction, Transaction newTransaction) {
		newTransaction.setBalanceAmount(
				previousTransaction.map(Transaction::getBalanceAmount).orElse(AppConstants.ZERO_DOUBLE)
						+ newTransaction.getNetAmount());
		return newTransaction;
	}

	/**
	 * Method to fetch absolute file path
	 * 
	 * @return file absolute path
	 */
	private static String getFilePath() {
		return new File("").getAbsolutePath().concat(File.separator).concat(AppConstants.FILE_PATH);
	}

}
