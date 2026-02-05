// Payment Program

package PaymentExample;

import java.util.ArrayList;
import java.util.List;

/*****************************************************************************************
 *
 * 🧠 STRATEGY PATTERN – PAYMENT EXAMPLE
 *
 * GOAL:
 *  - Define a FAMILY OF ALGORITHMS (Payment methods)
 *  - ENCAPSULATE each one
 *  - Make them INTERCHANGEABLE at runtime
 *
 * KEY IDEA:
 *  - The client depends ONLY on an INTERFACE
 *  - NOT on concrete implementations
 *
 *****************************************************************************************/

/**
 *  For this same usecase , we used composite pattern also.
 */

// ========================================================================================
// 1️⃣ STRATEGY INTERFACE
// ========================================================================================
//
// ➤ This is the HEART of the Strategy Pattern
// ➤ Declares a COMMON CONTRACT for all payment behaviors
// ➤ High-level modules depend ONLY on this interface
//
// Examples of strategies:
//   - Credit Card
//   - UPI
//   - NetBanking
//   - Wallet
//
interface PaymentMethod {

    /**
     * Executes validation / authorization logic
     * specific to a payment type.
     *
     * @return true if payment is valid, false otherwise
     */
    boolean check();
}



// ========================================================================================
// 2️⃣ CONCRETE STRATEGY – CREDIT CARD PAYMENT
// ========================================================================================
//
// ➤ Encapsulates CREDIT CARD–specific behavior
// ➤ Owns ONLY the data it needs
// ➤ Implements the SAME interface as all other strategies
//
class CreditCardPaymentStrategy implements PaymentMethod {

    // Card-specific data (belongs ONLY to this strategy)
    private final String cardNumber;
    private final String expiry;
    private final String cvv;
    private final String otp;

    /**
     * Each strategy manages its OWN required data.
     * No shared fields across strategies.
     */
    public CreditCardPaymentStrategy(String cardNumber,
                             String expiry,
                             String cvv,
                             String otp) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.otp = otp;
    }

    /**
     * Strategy-specific algorithm implementation
     */
    @Override
    public boolean check() {
        System.out.println("💳 Payment using Credit Card");

        // Credit card–specific validation rules
        return cardNumber.length() == 12
                && cvv.length() == 3
                && otp.length() == 4
                && expiry.startsWith("20");
    }
}



// ========================================================================================
// 3️⃣ CONCRETE STRATEGY – UPI PAYMENT
// ========================================================================================
//
// ➤ Another strategy in the SAME family
// ➤ Completely different algorithm
// ➤ SAME interface → interchangeable at runtime
//
class UPIPayment implements PaymentMethod {

    // UPI-specific data
    private final String upiId;
    private final String pin;

    public UPIPayment(String upiId, String pin) {
        this.upiId = upiId;
        this.pin = pin;
    }

    /**
     * UPI-specific validation logic
     */
    @Override
    public boolean check() {
        System.out.println("📱 Payment using UPI");

        return upiId.contains("@ok")
                && pin.length() == 4;
    }
}

interface PaymentSubject {

    // Register an observer
    void addObserver(PaymentObserver observer);

    // Remove an observer
    void removeObserver(PaymentObserver observer);
}


// =====================
// CONTEXT CLASS
// =====================
// This is the CONTEXT in Strategy Pattern.
// It USES a PaymentMethod strategy but does NOT know
// which concrete implementation it is using.
class Payment implements PaymentSubject {

    // Strategy reference (composition)

    PaymentInstrument paymentInstrument;

    PaymentService paymentService;

    // Shared balance (static for demonstration)
    static float Balance = 10000;

    Payment(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    List<PaymentObserver> observers =  new ArrayList<>();

    protected PaymentState state = new InitiatePaymentState();

    void setState(PaymentState state) {
        this.state = state;
    }

    @Override
    public void addObserver(PaymentObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PaymentObserver observer) {
        observers.remove(observer);
    }

    void notifySuccess(String Product) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentSuccess(Product);
        }
    }

    void notifyFailure(String Product) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentFailure(Product);
        }
    }

    public void PaymentToConsumption(Amount amount, String Product, String TransactionId) {
        System.out.println("Current State: " + state.name());
        state.pay(this, amount, Product, TransactionId);
        System.out.println("Payment State: " + state.name());
    }

    public void RefundPayment(Amount amount, String Product, String TransactionId) {
        System.out.println("Current State: " + state.name());
        state.refund(this, amount, Product,TransactionId);
    }

    // Static utility method to access shared balance
    static public float getBalance() {
        return Balance;
    }

    // Allows changing strategy at runtime
    public void setPaymentMethod(PaymentInstrument paymentInstrument) {
        System.out.println("Current State: " + state.name());
        this.paymentInstrument = paymentInstrument;
        state.selectPaymentMethod(this);
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }
}

// =====================
// CONCRETE CONTEXT 1
// =====================
// AmazonPayment is a specific USE CASE.
// It decides the DEFAULT strategy during construction.
class AmazonPayment extends Payment {
    AmazonPayment(PaymentService paymentService) {
        super(paymentService);
    }
}


// =====================
// CONCRETE CONTEXT 2
// =====================
// Another use case using the SAME strategy infrastructure.
class ElectricityBill extends Payment {
    ElectricityBill(PaymentService paymentService) {
        super(paymentService);
    }
}

