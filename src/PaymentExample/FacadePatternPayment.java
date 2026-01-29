package PaymentExample;


class PaymentGatewayFacade {


    void AmazonPaymentFacade(String Product, float baseamount, PaymentMethodFactory paymentFactory) {

        System.out.println("======================================");
        System.out.println("   AMAZON PAYMENT SYSTEM STARTED");
        System.out.println("======================================\n");

        // can possibly add any other usecase before and after this call.
        PaymentTemplate paymentTemplate =  new AmazonPaymentTemplate(Product, baseamount, paymentFactory);
        paymentTemplate.ProcessPayment();

    }

    void MonthlyBillPayment(String Product, float baseamount, PaymentMethodFactory paymentFactory) {
        System.out.println("======================================");
        System.out.println("   MONTHLY BILL PAYMENT SYSTEM STARTED");
        System.out.println("====================================\n");

        PaymentTemplate paymentTemplate =  new  MonthlyBillPaymentTemplate(Product, baseamount, paymentFactory);
        paymentTemplate.ProcessPayment();

    }
}

