# Payment System Assignment
Stand alone Java application to read and write payment records and calculate the balance amount along with differences in credit and debit amount

## Prerequisites
1. Java 1.8
2. IDE for Java
3. Maven (If not embedded with IDE)
4. Git (If not embedded with IDE)


## Usage
Application can be understood in two parts logically
1. A stream makes entries of payment transaction into the "transaction" file. Please refer to com.tcs.assignment.payment.Main.java
2. Another stream consumes the entries made in transaction file. This is scheduled to hit every 1 second, please refer to com.tcs.assignment.payment.scheduler.ScheduleTransactionFetch.java

## Installation
1. Checkout from git https://github.com/javagirish1805/tnassignment
2. Import to Java IDE as Maven project
3. Update/Download sources through Maven (IDE should do it automatically, if not can be done manually)

## Running the application
1. Run com.tcs.assignment.payment.Main.java to see the results printed on System console
2. Run "mvn test" to execute all junit tests to see the results of unit tests
3. After successful "mvn test" execution, Jacoco plugin will help to see the code coverage. Please refer to "target/jacoco-ut/index.html"
