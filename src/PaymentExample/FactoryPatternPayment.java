package PaymentExample;

abstract class PaymentMethodFactory {
    abstract PaymentMethod createPaymentMethod();
}

class CreditCardFactory extends PaymentMethodFactory {
    String cardNumber;
    String expiry;
    String cvv;
    String otp;

    CreditCardFactory(String cardNumber, String expiry,
                      String cvv, String otp) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.otp = otp;
    }

    @Override
    PaymentMethod createPaymentMethod() {
        return new CreditCardPayment(cardNumber, expiry, cvv, otp);
    }
}

class UPIFactory extends PaymentMethodFactory {
    private String upiId;
    private String pin;

    public UPIFactory(String upiId, String pin) {
        this.upiId = upiId;
        this.pin = pin;
    }

    @Override
    PaymentMethod createPaymentMethod() {
        return new UPIPayment(upiId, pin);
    }
}
