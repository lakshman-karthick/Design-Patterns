package PaymentExample;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


class IdempotencyStore {

    private static final Set<String> processedTransactions =
            ConcurrentHashMap.newKeySet();

    static boolean isProcessed(String txnId) {
        return processedTransactions.contains(txnId);
    }

    static void markProcessed(String txnId) {
        processedTransactions.add(txnId);
    }
}

class PaymentMonitor {

    static void attemptRepeated(String txnId) {
        System.out.println("[MONITOR] Idempotency issue: " + txnId);
    }

    static void recordAttempt(String txnId) {
        System.out.println("[MONITOR] Attempt started for TXN: " + txnId);
    }

    static void recordSuccess(String txnId) {
        System.out.println("[MONITOR] SUCCESS TXN: " + txnId);
    }

    static void recordFailure(String txnId) {
        System.out.println("[MONITOR] FAILURE TXN: " + txnId);
    }
}

interface PaymentService{
    boolean paymentExecution(Payment payment, Amount amount, String Product, String TransactionId);
    boolean refundExecution(Payment payment, Amount amount, String Product, String TransactionId);
}

class PaymentProcess implements PaymentService{
    @Override
    public boolean paymentExecution(Payment payment,Amount amount, String Product, String TransactionId) {
        payment.Balance -= amount.Calculate();
        payment.notifySuccess(Product);
        return true;
    }

    @Override
    public boolean refundExecution(Payment payment, Amount amount, String Product, String TransactionId) {
        payment.Balance += amount.Calculate();
        payment.notifyFailure(Product);
        return true;
    }
}

class PaymentProxy implements PaymentService{

    PaymentService paymentService;
    int MAX_RETRIES = 3;
    PaymentMonitor paymentMonitor = new PaymentMonitor();

    PaymentProxy(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @Override
    public boolean paymentExecution(Payment payment,Amount amount, String Product, String TransactionId) {
        float FinalAmount = amount.Calculate();

        if( IdempotencyStore.isProcessed(TransactionId) ){
            paymentMonitor.attemptRepeated(TransactionId);
            return true;
        }
        for(int attempt = 1;attempt <= MAX_RETRIES;attempt++){
            if(FinalAmount<0 || FinalAmount > Payment.getBalance()){
                paymentMonitor.recordAttempt(TransactionId);
                continue;
            }

            if(!payment.paymentInstrument.check()){
                paymentMonitor.recordAttempt(TransactionId);
                continue;
            }
            try{
                paymentService.paymentExecution(payment, amount, Product, TransactionId);
                IdempotencyStore.markProcessed(TransactionId);
                paymentMonitor.recordSuccess(TransactionId);
                return true;
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        paymentMonitor.recordFailure(TransactionId);
        return false;

    }

    @Override
    public boolean refundExecution(Payment payment, Amount amount, String product, String transactionId) {

        String refundTxnId = transactionId + "_REFUND";

        if (IdempotencyStore.isProcessed(refundTxnId)) {
            paymentMonitor.attemptRepeated(refundTxnId);
            return true;
        }

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {


            PaymentMonitor.recordAttempt(refundTxnId);

            try {
                paymentService.refundExecution(payment, amount, product, transactionId);

                IdempotencyStore.markProcessed(refundTxnId);

                PaymentMonitor.recordSuccess(
                        refundTxnId
                );
                return true;

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        PaymentMonitor.recordFailure(
                refundTxnId
        );
        return false;
    }
}
