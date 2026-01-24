package PaymentExample;

//https://chatgpt.com/c/695b06a8-48d8-8324-942a-4edde5fb848a

// Entry point of the application
// Demonstrates Strategy + Decorator + Observer patterns working together
public class Main {

    public static void main(String[] args) {


        PaymentGatewayFacade paymentGatewayFacade = new PaymentGatewayFacade();
        paymentGatewayFacade.AmazonPaymentFacade("SG Bat",1000,new UPIFactory("lakshman@okicici","2376".toCharArray()));
        paymentGatewayFacade.AmazonPaymentFacade("Kookabura Ball",400,new CreditCardFactory("412345678998","2031","Lakshman Karthick","123".toCharArray(),"2376".toCharArray()));
        paymentGatewayFacade.MonthlyBillPayment("Electricity",500,new CreditCardFactory("675687698901","2031","Lakshman Karthick","234".toCharArray(),"6547".toCharArray()));


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
