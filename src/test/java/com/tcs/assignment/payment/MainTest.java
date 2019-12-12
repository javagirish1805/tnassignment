package com.tcs.assignment.payment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Test class to test Main
 *
 *  @author Girish KHV
 *  @version 1.0
 */
public class MainTest {
	
	@Test
    public void testScheduler() {
		String[] args = null;
		
		try{
			Main.main(args);
		}catch(Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);		
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
