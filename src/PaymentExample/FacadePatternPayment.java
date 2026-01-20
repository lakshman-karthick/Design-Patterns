package PaymentExample;


class PaymentGatewayFacade {

    // ===============================
    // OBSERVER CREATION
    // ===============================
    PaymentObserver email = new EmailNotifier();       // Notify customer
    PaymentObserver courier = new CourierNotifier();   // Notify courier
    PaymentObserver shipping = new ShippingNotifier(); // Trigger shipping

    void AmazonPaymentFacade(String Product, float baseamount, PaymentMethodFactory paymentFactory) {

        System.out.println("======================================");
        System.out.println("   Amazon PAYMENT SYSTEM STARTED");
        System.out.println("======================================\n");

        // ===============================
        // CONTEXT CREATION (STRATEGY)
        // ===============================
        System.out.println("[INIT] Creating Amazon payment context "
                + "(default strategy: Credit Card)\n");
        Payment amazonPayment = new AmazonPayment();

        // ===============================
        // REGISTER OBSERVERS
        // ===============================
        System.out.println("[INIT] Initializing observers (post-payment actions)");
        System.out.println("[INIT] Registering observers to Amazon payment\n");
        amazonPayment.addObserver(email);
        amazonPayment.addObserver(courier);
        amazonPayment.addObserver(shipping);

        // ===============================
        // DECORATOR STACK (RECURSIVE)
        // ===============================
        System.out.println("[ORDER] Building price calculation for item: "+Product);
        System.out.println("        BaseAmount -> GST(5%) -> PackageCharge(20) -> Discount(10%)\n");
        Amount baseAmount = new BaseAmount(baseamount);

        Amount payableAmount =
                new DiscountDecorator(
                        new PackageChargeDecorator(
                                new GSTDecorator(baseAmount, 5),
                                20
                        ),
                        10
                );



        Command setPayment = new selectPaymentMethodCommand(paymentFactory.createPaymentMethod(),amazonPayment);
        setPayment.execute();

        // ===============================
        // PAYMENT EXECUTION
        // ===============================
        System.out.println("[PAYMENT] Initiating payment for '"+Product+"'\n");
        Command amazonPaymentCommand = new PaymentCommand(amazonPayment,payableAmount,Product);
        Command LoggingCommand = new LoggingCommand(amazonPaymentCommand);
        LoggingCommand.execute();

        System.out.println("[BALANCE] Remaining balance after payment: "
                + Payment.getBalance() + "\n");
    }

    void MonthlyBillPayment(String Product, float baseamount, PaymentMethodFactory paymentFactory) {
        System.out.println("======================================");
        System.out.println("   ELECTRICITY PAYMENT SYSTEM STARTED");
        System.out.println("====================================\n");

        // ===============================
        // CONTEXT CREATION (STRATEGY)
        // ===============================
        System.out.println("[INIT] Creating "+Product+" payment context "
                + "(default strategy: Credit Card)\n");

        Payment electricityPayment = new ElectricityBill();

        // ===============================
        // REGISTER OBSERVERS
        // ===============================
        System.out.println("[INIT] Initializing observers (post-payment actions)");
        System.out.println("[INIT] Registering observers to "+Product+" payment\n");
        electricityPayment.addObserver(email);

        // ===============================
        // DECORATOR STACK (RECURSIVE)
        // ===============================
        System.out.println("[ORDER] Building price calculation for item: "+Product);
        System.out.println("        BaseAmount -> GST(10%) -> Discount(10%)\n");
        Amount baseAmount = new BaseAmount(baseamount);

        Amount payableAmount =
                new DiscountDecorator(
                        new GSTDecorator(baseAmount, 10),
                        10
                );

        Command setPayment = new selectPaymentMethodCommand(paymentFactory.createPaymentMethod(),electricityPayment);
        setPayment.execute();

        // ===============================
        // PAYMENT EXECUTION
        // ===============================
        System.out.println("[PAYMENT] Initiating payment for '"+Product+"'\n");
        PaymentCommand ElectricityPaymentCommand = new PaymentCommand(electricityPayment,payableAmount,Product);
        Command LoggingCommand = new LoggingCommand(ElectricityPaymentCommand);
        LoggingCommand.execute();

        System.out.println("[BALANCE] Remaining balance after payment: "
                + Payment.getBalance() + "\n");


    }
}

