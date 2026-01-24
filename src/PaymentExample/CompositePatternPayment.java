package PaymentExample;


//PaymentInstrument
// ├── Card
// │    ├── CreditCard
// │    │    ├── VisaCredit
// │    │    ├── RupayCredit
// │    ├── DebitCard
// │         ├── VisaDebit
// │         └── RupayDebit
// │
// ├── Wallet
//      ├── Paytm
//      ├── GPay

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

interface PaymentInstrument {
    boolean check();

    String name();

    default void addPaymentMethod(PaymentInstrument paymentInstrument){
        throw new UnsupportedOperationException("Not supported yet.");
    };

    default List<PaymentInstrument> getPaymentInstruments(){
        return List.of();
    };

}

abstract class CompositePayment implements PaymentInstrument {
    protected List<PaymentInstrument> paymentInstruments = new ArrayList<>();

    @Override
    public void addPaymentMethod(PaymentInstrument paymentInstrument) {
        paymentInstruments.add(paymentInstrument);
    }

    @Override
    public List<PaymentInstrument> getPaymentInstruments() {
        return paymentInstruments;
    }

    @Override
    public boolean check() {
        for (PaymentInstrument paymentInstrument : paymentInstruments) {
            if (paymentInstrument.check()) {
                return true;
            }
        }
        return false;
    }
}

class CardPayment extends CompositePayment {

    @Override
    public String name() {
        return "Card Payment";
    }
}

class WalletPayment extends CompositePayment {

   @Override
    public String name() {
       return "Wallet Payment";
   }
}

class CreditCardPayment extends CompositePayment {
    @Override
    public String name() {
        return "Credit Card Payment";
    }
}

class DebitCardPayment extends CompositePayment {
    @Override
    public String name() {
        return "Debit Card Payment";
    }
}

abstract class AbstractCardPayment implements PaymentInstrument {

    CardPaymentContext cardPaymentContext;
    public AbstractCardPayment(CardPaymentContext cardPaymentContext) {
        this.cardPaymentContext = cardPaymentContext;
    }

    @Override
    public boolean check() {
        int expiryYear = Integer.parseInt(cardPaymentContext.getExpiry());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return cardPaymentContext.getCardNumber().length() == 12
                && cardPaymentContext.isCVVValid()
                && cardPaymentContext.isOtpValid()
                && !cardPaymentContext.getCardholderName().isEmpty()
                && expiryYear >= currentYear
                && networkValidation();
    }

    protected abstract boolean networkValidation();
}


class VisaCardPayment extends AbstractCardPayment {

    VisaCardPayment(CardPaymentContext cardPaymentContext) {
        super(cardPaymentContext);
    }

    @Override
    public String name() {
        return "Visa Card";
    }

    @Override
    protected boolean networkValidation(){
        return cardPaymentContext.getCardNumber().startsWith("4");
    }
}


class RupayCardPayment extends AbstractCardPayment {

    RupayCardPayment(CardPaymentContext cardPaymentContext) {
        super(cardPaymentContext);
    }

    @Override
    public String name() {
        return "Rupay Card";
    }

    @Override
    protected boolean networkValidation(){
        return cardPaymentContext.getCardNumber().startsWith("6");
    }
}


class GooglePayPayment implements PaymentInstrument {


    UPIPaymentContext upiPaymentContext;
    public GooglePayPayment(UPIPaymentContext upiPaymentContext) {
        this.upiPaymentContext = upiPaymentContext;
    }

    @Override
    public String name(){
        return "Google Pay";
    }

    @Override
    public boolean check() {
        return upiPaymentContext.getUpiId().contains("@ok")
                && upiPaymentContext.isPinValid();
    }
}

class PaytmPayment implements PaymentInstrument {

    UPIPaymentContext upiPaymentContext;

    public PaytmPayment(UPIPaymentContext upiPaymentContext) {
        this.upiPaymentContext = upiPaymentContext;
    }

    @Override
    public String name(){
        return "Paytm Pay";
    }

    @Override
    public boolean check() {
        return upiPaymentContext.getUpiId().contains("@paytm")
                && upiPaymentContext.isPinValid();
    }
}
