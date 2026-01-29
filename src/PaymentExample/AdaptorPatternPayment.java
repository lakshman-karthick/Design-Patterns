package PaymentExample;

/**
 * ADDING INCOMPATIBLE PHONEPE CLASS TO COMPOSITE GROUP USING AN ADAPTOR
 */

class PhonePeAdaptor implements PaymentInstrument {

    PhonePePayment phonePePayment;

    public PhonePeAdaptor(PhonePePayment phonePePayment) {
        this.phonePePayment = phonePePayment;
    }

    @Override
    public String name(){
        return phonePePayment.name();
    }

    @Override
    public boolean check() {
        return phonePePayment.validate();
    }
}


class PhonePePayment {
    UPIPaymentContext upiPaymentContext;

    public PhonePePayment(UPIPaymentContext upiPaymentContext) {
        this.upiPaymentContext = upiPaymentContext;
    }

    public String name(){
        return "Phone Pe";
    }

    public boolean validate() {
        return upiPaymentContext.getUpiId().contains("@ybl")
                && upiPaymentContext.isPinValid();
    }
}
