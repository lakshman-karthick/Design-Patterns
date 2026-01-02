// Entry point of the application
// Demonstrates Strategy + Decorator + Observer patterns working together
public class Main {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   PAYMENT SYSTEM STARTED");
        System.out.println("======================================\n");

        // ===============================
        // CONTEXT CREATION (STRATEGY)
        // ===============================
        System.out.println("[INIT] Creating Amazon payment context "
                + "(default strategy: Credit Card)\n");

        Payment amazon = new AmazonPayment();


        // ===============================
        // OBSERVER CREATION
        // ===============================
        System.out.println("[INIT] Initializing observers (post-payment actions)");

        PaymentObserver email = new EmailNotifier();       // Notify customer
        PaymentObserver courier = new CourierNotifier();   // Notify courier
        PaymentObserver shipping = new ShippingNotifier(); // Trigger shipping


        // ===============================
        // REGISTER OBSERVERS
        // ===============================
        System.out.println("[INIT] Registering observers to Amazon payment\n");

        amazon.addObserver(email);
        amazon.addObserver(courier);
        amazon.addObserver(shipping);


        // ===============================
        // DECORATOR STACK (RECURSIVE)
        // ===============================
        System.out.println("[ORDER] Building price calculation for item: SG Bat");
        System.out.println("        BaseAmount -> GST(5%) -> PackageCharge(20) -> Discount(10%)\n");

        Amount baseAmount = new BaseAmount(1000);

        Amount payableAmount =
                new DiscountDecorator(
                        new PackageChargeDecorator(
                                new GSTDecorator(baseAmount, 5),
                                20
                        ),
                        10
                );


        // ===============================
        // PAYMENT EXECUTION
        // ===============================
        System.out.println("[PAYMENT] Initiating payment for 'SG Bat'\n");

        amazon.PaymentToConsumption(payableAmount, "SG Bat");

        System.out.println("[BALANCE] Remaining balance after purchase: "
                + amazon.getBalance() + "\n");


        // ===============================
        // STRATEGY CHANGE AT RUNTIME
        // ===============================
        System.out.println("[STRATEGY] Switching payment method to UPI at runtime\n");

        amazon.setPaymentMethod(
                new UPIPayment("lakshman@okicici", "7653")
        );


        // New purchase with same decorator rules
        System.out.println("[ORDER] Building price calculation for item: Kookabura Ball");
        System.out.println("        BaseAmount -> GST(5%) -> PackageCharge(20) -> Discount(10%)\n");

        Amount baseAmount1 = new BaseAmount(500);

        Amount payableAmount1 =
                new DiscountDecorator(
                        new PackageChargeDecorator(
                                new GSTDecorator(baseAmount1, 5),
                                20
                        ),
                        10
                );

        System.out.println("[PAYMENT] Initiating payment for 'Kookabura Ball'\n");

        amazon.PaymentToConsumption(payableAmount1, "Kookabura Ball");

        System.out.println("[BALANCE] Remaining balance after purchase: "
                + amazon.getBalance() + "\n");


        // ===============================
        // DIFFERENT USE CASE (ELECTRICITY)
        // ===============================
        System.out.println("[INIT] Creating Electricity Bill payment context\n");

        Amount baseAmount2 = new BaseAmount(500);

        Amount payableAmount2 =
                new DiscountDecorator(
                        new GSTDecorator(baseAmount2, 10),
                        10
                );

        Payment electricity = new ElectricityBill();

        System.out.println("[INIT] Registering only Email observer for Electricity bill\n");

        // Electricity bill only cares about email notification
        electricity.addObserver(email);

        System.out.println("[PAYMENT] Initiating Electricity bill payment\n");

        electricity.PaymentToConsumption(payableAmount2, "Electricity");

        System.out.println("[BALANCE] Remaining balance after electricity bill: "
                + electricity.getBalance() + "\n");


        // ===============================
        // SHARED STATE CHECK
        // ===============================
        System.out.println("======================================");
        System.out.println("[FINAL] Shared Balance across all payments: "
                + Payment.getBalance());
        System.out.println("======================================");
    }
}
