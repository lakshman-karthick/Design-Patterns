
package PaymentExample;

interface PaymentContext {
}

final class CardPaymentContext implements PaymentContext {

    private final String cardNumber;
    private final String expiry;
    private final String cardholderName;
    private final char[] cvv;
    private final char[] otp;

    // 🔒 private constructor
    private CardPaymentContext(String cardNumber,
                               String expiry,
                               String cardholderName,
                               char[] cvv,
                               char[] otp) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cardholderName = cardholderName;
        this.cvv = cvv.clone();
        this.otp = otp.clone();
    }

    /* -------- getters (non-sensitive only) -------- */

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public boolean isCVVValid() {
        if(cvv == null) {
            return false;
        }
        for (char ch : cvv) {
            if(!Character.isDigit(ch)) {
                return false;
            }
        }
        if(cvv.length == 3) {
            return true;
        }
        return false;
    }

    public boolean isOtpValid() {
        if(otp == null) {
            return false;
        }
        for (char ch : otp) {
            if(!Character.isDigit(ch)) {
                return false;
            }
        }
        if(otp.length == 4) {
            return true;
        }
        return false;
    }

    /* -------- lifecycle -------- */

    public void clear() {
        java.util.Arrays.fill(cvv, '\0');
        java.util.Arrays.fill(otp, '\0');
    }

    /* -------- factory access -------- */

    static CardPaymentContext create(String cardNumber,
                                     String expiry,
                                     String cardholderName,
                                     char[] cvv,
                                     char[] otp) {
        return new CardPaymentContext(
                cardNumber, expiry, cardholderName, cvv, otp
        );
    }
}

final class UPIPaymentContext implements PaymentContext {

    private final String upiId;
    private final char[] pin;

    // 🔒 private constructor
    private UPIPaymentContext(String upiId, char[] pin) {
        this.upiId = upiId;
        this.pin = pin.clone();
    }

    /* -------- getters -------- */

    public String getUpiId() {
        return upiId;
    }

    public boolean isPinValid() {
        if (pin == null) {
            return false;
        }
        for (char ch : pin) {
            if(!Character.isDigit(ch)) {
                return false;
            }
        }

        if(pin.length == 4) {
            return true;
        }
        return false;
    }

    /* -------- lifecycle -------- */

    public void clear() {
        java.util.Arrays.fill(pin, '\0');
    }

    /* -------- factory access -------- */

    static UPIPaymentContext create(String upiId, char[] pin) {
        return new UPIPaymentContext(upiId, pin);
    }
}

