package PaymentExample;

/**
 * State Pattern
 * 1. INITIATE PAYMENT
 * 2. METHOD CHOSEN / VALIDATION
 * 3. SUCCESS
 * 4. FAILED
 * 5. REFUND
 */
interface PaymentState {

    void selectPaymentMethod(Payment payment);

    void pay(Payment payment, Amount amount, String product);

    void refund(Payment payment, Amount amount, String product);

    String name();
}

class InitiatePaymentState implements PaymentState{

    @Override
    public void selectPaymentMethod(Payment payment) {
        payment.setState(new PaymentProcessingState());
    }

    @Override
    public void pay(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Payment method not chosen yet.");
    }

    @Override
    public void refund(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Payment method not chosen yet.");
    }

    @Override
    public String name() {
        return "Initiate Payment State";
    }
}

class PaymentProcessingState implements PaymentState{

    @Override
    public void selectPaymentMethod(Payment payment) {
        throw new UnsupportedOperationException("Payment processing is already started.");
    }

    @Override
    public void pay(Payment payment, Amount amount, String Product) {
        float FinalAmount = amount.Calculate();
        if (payment.paymentInstrument.check()) {
            payment.Balance -= FinalAmount;
            payment.notifySuccess(Product);
            payment.setState(new PaymentSuccessState());
            return;
        }
        payment.notifyFailure(Product);
        payment.setState(new PaymentFailureState());
    }

    @Override
    public void refund(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Payment processing is going on.");
    }

    @Override
    public String name() {
        return "Payment Processing State";
    }

}

class PaymentSuccessState implements PaymentState{

    @Override
    public void selectPaymentMethod(Payment payment) {
        throw new UnsupportedOperationException("Payment is already successful");
    }

    @Override
    public void pay(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Payment is already successful");
    }

    @Override
    public void refund(Payment payment, Amount amount, String Product) {
        float FinalAmount = amount.Calculate();
        if (payment.paymentInstrument.check()) {
            payment.Balance += FinalAmount;
            payment.notifyFailure(Product);
            payment.setState(new RefundState());
        }
    }

    @Override
    public String name() {
        return "Payment Success";
    }
}

class PaymentFailureState implements PaymentState{

    @Override
    public void selectPaymentMethod(Payment payment) {
        payment.setState(new InitiatePaymentState());
    }

    @Override
    public void pay(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Wait for payment method to be chosen.");
    }

    @Override
    public void refund(Payment payment, Amount amount, String product) {
        throw new UnsupportedOperationException("Payment is not yet completed");
    }

    @Override
    public String name() {
        return "Payment Failure";
    }
}

class RefundState implements PaymentState{

    @Override
    public void selectPaymentMethod(Payment payment) {
        throw new IllegalStateException("Payment refunded");
    }

    @Override
    public void pay(Payment payment, Amount amount, String product) {
        throw new IllegalStateException("Payment refunded");
    }

    @Override
    public void refund(Payment payment, Amount amount, String product) {
        throw new IllegalStateException("Already refunded");
    }

    @Override
    public String name() {
        return "REFUNDED";
    }
}


