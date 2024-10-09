package activity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FraudDetectionSystemTest {

    @Test
    public void testHighTransactionAmount() {
        System.out.println("Running testHighTransactionAmount");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(15000, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());

        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(50, result.riskScore);
    }

    @Test
    public void testExcessiveTransactionsInLastHour() {
        System.out.println("Running testExcessiveTransactionsInLastHour");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Arrays.asList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(10), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(20), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(30), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(40), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(50), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(55), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(56), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(57), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(58), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(59), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(60), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());

        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(30, result.riskScore);
    }

    @Test
    public void testLocationChangeWithinShortTimeFrame() {
        System.out.println("Running testLocationChangeWithinShortTimeFrame");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Collections.singletonList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(20), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "LA");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());

        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(20, result.riskScore);
    }

    @Test
    public void testBlacklistedLocation() {
        System.out.println("Running testBlacklistedLocation");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "BlacklistedLocation");
        List<String> blacklistedLocations = Collections.singletonList("BlacklistedLocation");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), blacklistedLocations);

        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(100, result.riskScore);
    }

    @Test
    public void testNormalTransaction() {
        System.out.println("Running testNormalTransaction");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());

        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    }
    @Test
    public void testHighTransactionAmountBoundary() {
        System.out.println("Running testHighTransactionAmountBoundary");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(10000, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());
    
        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    
        transaction = new FraudDetectionSystem.Transaction(10001, LocalDateTime.now(), "NYC");
        result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());
    
        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(50, result.riskScore);
    }
    
    @Test
    public void testExcessiveTransactionsInLastHourBoundary() {
        System.out.println("Running testExcessiveTransactionsInLastHourBoundary");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Arrays.asList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(10), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(20), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(30), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(40), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(50), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(55), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(56), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(57), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(58), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(59), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());
    
        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    
        previousTransactions = Collections.synchronizedList(new ArrayList<>(previousTransactions));
        previousTransactions.add(new FraudDetectionSystem.Transaction(100, now.minusMinutes(60), "NYC"));
        result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());
    
        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(30, result.riskScore);
    }
    
    @Test
    public void testLocationChangeWithinShortTimeFrameBoundary() {
        System.out.println("Running testLocationChangeWithinShortTimeFrameBoundary");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Collections.singletonList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(30), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "LA");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());
    
        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    
        previousTransactions = Collections.singletonList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(29), "NYC")
        );
        result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());
    
        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(20, result.riskScore);
    }
    
    @Test
    public void testBlacklistedLocationBoundary() {
        System.out.println("Running testBlacklistedLocationBoundary");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "BlacklistedLocation");
        List<String> blacklistedLocations = Collections.singletonList("BlacklistedLocation");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), blacklistedLocations);
    
        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(100, result.riskScore);
    
        transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "SafeLocation");
        result = system.checkForFraud(transaction, Collections.emptyList(), blacklistedLocations);
    
        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    }
    
    @Test
    public void testNormalTransactionBoundary() {
        System.out.println("Running testNormalTransactionBoundary");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());
    
        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    }
}