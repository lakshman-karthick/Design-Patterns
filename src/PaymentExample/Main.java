package PaymentExample;

//https://chatgpt.com/c/695b06a8-48d8-8324-942a-4edde5fb848a

// Entry point of the application
// Demonstrates Strategy + Decorator + Observer patterns working together
public class Main {

    public static void main(String[] args) {


          PaymentGatewayFacade paymentGatewayFacade = new PaymentGatewayFacade();
          paymentGatewayFacade.AmazonPaymentFacade("SG Bat",1000,new UPIFactory("lakshman@okicici","2376"));
          paymentGatewayFacade.MonthlyBillPayment("Electricity",500,new CreditCardFactory("675687698901","2031","234","6547"));


//        LoggingCommand.undo();
//
//        System.out.println("[BALANCE] Remaining balance after refund electricity bill: "
//                + Payment.getBalance() + "\n");
//
//        LoggingCommand.undo();
//
//        System.out.println("[BALANCE] Remaining balance after refund for kukaburra ball: "
//                + Payment.getBalance() + "\n");

        // ===============================
        // SHARED STATE CHECK
        // ===============================
        System.out.println("======================================");
        System.out.println("[FINAL] Shared Balance across all payments: "
                + Payment.getBalance());
        System.out.println("======================================");
    }
}


// ============================
// E-COMMERCE
// ============================
// 1. Select Product
// 2. Select Payment method
// 3. Payment
//
