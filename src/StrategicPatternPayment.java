// Payment Program

import java.util.ArrayList;
import java.util.List;

// =====================
// STRATEGY INTERFACE
// =====================
// This interface represents the STRATEGY.
// It defines a COMMON CONTRACT for all payment behaviors.
// The Payment class will depend ONLY on this interface,
// not on any concrete payment implementation.
interface PaymentMethod {
    boolean pay();
}


// =====================
// CONCRETE STRATEGY 1
// =====================
// One concrete implementation of the strategy.
// Encapsulates CREDIT CARD payment logic.
class CreditCardPayment implements PaymentMethod {

    private String cardNumber;
    private String expiry;
    private String cvv;
    private String otp;

    // Each strategy maintains its OWN required data
    public CreditCardPayment(String cardNumber, String expiry,
                             String cvv, String otp) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.otp = otp;
    }

    // Implements the strategy-specific algorithm
    @Override
    public boolean pay() {
        System.out.println("Payment using Credit Card");

        // Credit card–specific validation logic
        return cardNumber.length() == 11
                && cvv.length() == 3
                && otp.length() == 4
                && expiry.startsWith("20");
    }
}


// =====================
// CONCRETE STRATEGY 2
// =====================
// Another concrete implementation of the SAME strategy.
// Encapsulates UPI payment logic.
class UPIPayment implements PaymentMethod {

    private String upiId;
    private String pin;

    public UPIPayment(String upiId, String pin) {
        this.upiId = upiId;
        this.pin = pin;
    }

    // UPI-specific algorithm
    @Override
    public boolean pay() {
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
    PaymentMethod paymentMethod;

    // Shared balance (static for demonstration)
    static float Balance = 10000;

    List<PaymentObserver> observers =  new ArrayList<>();

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

    // Delegates payment behavior to the strategy
    public void PaymentToConsumption(Amount amount, String Product) {

        // Strategy Pattern in action:
        // Payment does NOT know HOW payment is done.
        // It simply calls pay() on the strategy.
        float FinalAmount = amount.Calculate();
        if (paymentMethod.pay()) {
            Balance -= FinalAmount;
            notifySuccess(Product);
            return;
        }
        notifyFailure(Product);
    }

    // Static utility method to access shared balance
    static public float getBalance() {
        return Balance;
    }

    // Allows changing strategy at runtime
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}


// =====================
// CONCRETE CONTEXT 1
// =====================
// AmazonPayment is a specific USE CASE.
// It decides the DEFAULT strategy during construction.
class AmazonPayment extends Payment {

    public AmazonPayment() {
        // Default strategy assigned
        paymentMethod =
                new CreditCardPayment("12345678901", "2031", "123", "1234");
    }
}


// =====================
// CONCRETE CONTEXT 2
// =====================
// Another use case using the SAME strategy infrastructure.
class ElectricityBill extends Payment {

    public ElectricityBill() {
        paymentMethod =
                new CreditCardPayment("12345678901", "2031", "123", "1234");
    }
}

