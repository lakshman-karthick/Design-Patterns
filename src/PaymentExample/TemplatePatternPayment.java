package PaymentExample;

// ===============================
// TEMPLATE PATTERN
// ===============================
abstract class PaymentTemplate {

    // ===============================
    // OBSERVER CREATION
    // ===============================
    PaymentObserver email = new EmailNotifier();       // Notify customer
    PaymentObserver courier = new CourierNotifier();   // Notify courier
    PaymentObserver shipping = new ShippingNotifier();

    String Product;
    float BaseAmount;
    PaymentService paymentService;
    PaymentMethodFactory paymentFactory;

    PaymentTemplate(String Product,float BaseAmount,PaymentMethodFactory paymentFactory,PaymentService paymentService) {
        this.Product = Product;
        this.BaseAmount = BaseAmount;
        this.paymentFactory = paymentFactory;
        this.paymentService = paymentService;
    }

    void ProcessPayment() {
        System.out.println("[INIT] Processing Payment Template");
        Payment paymentObject = ContextCreation();
        AddObservers(paymentObject);
        Amount PayableAmount = CalculateAmount();
        selectPaymentMethod(paymentObject);
        PaymentProcess(paymentObject,PayableAmount);
        ShowAvailableBalance();
    }

    // ===============================
    // CONTEXT CREATION (STRATEGY)
    // ===============================
    abstract protected Payment ContextCreation();

    // ===============================
    // REGISTER OBSERVERS
    // ===============================
    abstract protected void AddObservers(Payment paymentObject);

    // ===============================
    // DECORATOR STACK (RECURSIVE)
    // ===============================
    abstract protected Amount CalculateAmount();

    // ===============================
    // PAYMENT METHOD SELECTION
    // ===============================
    void selectPaymentMethod(Payment paymentObject) {
        Command setPayment = new selectPaymentMethodCommand(paymentFactory.createPaymentMethod(),paymentObject);
        setPayment.execute();
    }

    // ===============================
    // PAYMENT EXECUTION
    // ===============================
    void PaymentProcess(Payment paymentObject,Amount payableAmount) {
        System.out.println("[PAYMENT] Initiating payment for '"+Product+"'\n");
        PaymentCommand AmazonPaymentCommand = new PaymentCommand(paymentObject,payableAmount,Product);
        Command LoggingCommand = new LoggingCommand(AmazonPaymentCommand);
        LoggingCommand.execute();
    }

    // ===============================
    // SHOW BALANCE
    // ===============================
    void ShowAvailableBalance(){
        System.out.println("[INIT] Show Available Balance");
        System.out.println("[BALANCE] Remaining balance after payment: "
                + Payment.getBalance() + "\n");
    }


}

class AmazonPaymentTemplate extends PaymentTemplate {


    AmazonPaymentTemplate(String Product, float BaseAmount, PaymentMethodFactory paymentFactory, PaymentService paymentService) {
        super(Product, BaseAmount, paymentFactory, paymentService);
    }

    @Override
    protected Payment ContextCreation() {
        System.out.println("[INIT] Creating Payment Template");
        return new AmazonPayment(paymentService);
    }

    @Override
    protected void AddObservers(Payment paymentObject) {
        System.out.println("[INIT] Adding Observers");
        paymentObject.addObserver(email);
        paymentObject.addObserver(courier);
        paymentObject.addObserver(shipping);
    }

    @Override
    protected Amount CalculateAmount() {
        System.out.println("[INIT] Calculating Payment Template");
        System.out.println("[ORDER] Building price calculation for item: "+Product);
        System.out.println("        BaseAmount -> GST(5%) -> PackageCharge(20) -> Discount(10%)\n");
        Amount baseAmount = new BaseAmount(BaseAmount);

        Amount payableAmount =
                new DiscountDecorator(
                        new PackageChargeDecorator(
                                new GSTDecorator(baseAmount, 5),
                                20
                        ),
                        10
                );

        return payableAmount;
    }

}


class MonthlyBillPaymentTemplate extends PaymentTemplate {

    MonthlyBillPaymentTemplate(String Product, float BaseAmount, PaymentMethodFactory paymentFactory, PaymentService paymentService) {
        super(Product, BaseAmount, paymentFactory,paymentService);
    }

    @Override
    protected Payment ContextCreation() {
        System.out.println("[INIT] Creating Payment Template");
        return new ElectricityBill(paymentService);
    }

    @Override
    protected void AddObservers(Payment paymentObject) {
        System.out.println("[INIT] Adding Observers");
        paymentObject.addObserver(email);
    }

    @Override
    protected Amount CalculateAmount() {
        System.out.println("[INIT] Calculating Payment Template");
        System.out.println("[ORDER] Building price calculation for item: "+Product);
        System.out.println("        BaseAmount -> GST(10%) -> Discount(10%)\n");
        Amount baseAmount = new BaseAmount(BaseAmount);

        Amount payableAmount =
                new DiscountDecorator(
                        new GSTDecorator(baseAmount, 10),
                        10
                );

        return payableAmount;
    }

}
