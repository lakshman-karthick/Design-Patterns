package PaymentExample;

import java.util.List;

interface PaymentIterator {
    boolean hasNext();
    PaymentInstrument next();
}

class PaymentInstrumentIterator implements PaymentIterator {

    List<PaymentInstrument> paymentInstruments;
    int index = 0;

    PaymentInstrumentIterator(List<PaymentInstrument> paymentInstruments) {
        this.paymentInstruments = paymentInstruments;
    }

    @Override
    public boolean hasNext() {
        return index < paymentInstruments.size();
    }

    @Override
    public PaymentInstrument next() {
        return paymentInstruments.get(index++);
    }
}


class PaymentInstrumentReverseIterator implements PaymentIterator {
     List<PaymentInstrument> paymentInstruments;
     int index;

     PaymentInstrumentReverseIterator(List<PaymentInstrument> paymentInstruments) {
         this.paymentInstruments = paymentInstruments;
         index = paymentInstruments.size() - 1;
     }

     @Override
     public boolean hasNext() {
         return index >= 0;
     }

     @Override
     public PaymentInstrument next() {
         return paymentInstruments.get(index--);
     }
}
