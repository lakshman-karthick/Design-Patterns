package PaymentExample;

abstract class PaymentMethodFactory {
    abstract PaymentInstrument createPaymentMethod();
}

class CreditCardFactory extends PaymentMethodFactory {

    CardPaymentContext cardPaymentContext;
    CreditCardFactory(String cardNumber, String expiry,String cardholderName,
                      char[] cvv, char[] otp) {
        cardPaymentContext = CardPaymentContext.create(cardNumber,expiry,cardholderName,cvv,otp);
    }


    @Override
    CreditCardPayment createPaymentMethod() {
        CreditCardPayment creditCardPayment = new CreditCardPayment();
        VisaCardPayment visaCardPayment = new VisaCardPayment(cardPaymentContext);
        RupayCardPayment rupayCardPayment = new RupayCardPayment(cardPaymentContext);
        creditCardPayment.addPaymentMethod(visaCardPayment);
        creditCardPayment.addPaymentMethod(rupayCardPayment);
        return creditCardPayment;
    }
}

class UPIFactory extends PaymentMethodFactory {
    private String upiId;
    private String pin;
    UPIPaymentContext upiPaymentContext;
    public UPIFactory(String upiId, char[] pin) {
        this.upiPaymentContext = UPIPaymentContext.create(upiId,pin);
    }

    @Override
    WalletPayment createPaymentMethod() {
        WalletPayment walletPayment = new WalletPayment();
        GooglePayPayment googlePayPayment = new GooglePayPayment(upiPaymentContext);
        PaytmPayment paytmPayment = new PaytmPayment(upiPaymentContext);
        walletPayment.addPaymentMethod(googlePayPayment);
        walletPayment.addPaymentMethod(paytmPayment);
        return walletPayment;
    }
}
