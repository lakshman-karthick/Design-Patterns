package PaymentExample;

/**
 * OBSERVER INTERFACE
 * ------------------
 * Defines the contract for all observers interested in
 * payment events.
 *
 * Payment (Subject) will notify these observers
 * without knowing their concrete implementations.
 */
interface PaymentObserver {

    // Called when payment is successful
    void onPaymentSuccess(String product);

    // Called when payment fails
    void onPaymentFailure(String product);
}

/**
 * CONCRETE OBSERVER - EMAIL NOTIFICATION
 * -------------------------------------
 * Responsible for notifying the customer via email.
 */
class EmailNotifier implements PaymentObserver {

    @Override
    public void onPaymentSuccess(String product) {
        System.out.println("[EMAIL] Order Confirmed");
        System.out.println("        Product : " + product);
        System.out.println("        Status  : Payment Successful");
        System.out.println("        Message : Shipping is in progress. "
                + "Your product will be delivered shortly.\n");
    }

    @Override
    public void onPaymentFailure(String product) {
        System.out.println("[EMAIL] Order Failed");
        System.out.println("        Product : " + product);
        System.out.println("        Status  : Payment Failed");
        System.out.println("        Message : Please retry the payment.\n");
    }
}

/**
 * CONCRETE OBSERVER - SHIPPING SERVICE
 * -----------------------------------
 * Triggered to start or stop the shipping workflow
 * based on payment outcome.
 */
class ShippingNotifier implements PaymentObserver {

    @Override
    public void onPaymentSuccess(String product) {
        System.out.println("[SHIPPING] Action Required");
        System.out.println("           Product : " + product);
        System.out.println("           Action  : Start shipping process\n");
    }

    @Override
    public void onPaymentFailure(String product) {
        System.out.println("[SHIPPING] Action Halted");
        System.out.println("           Product : " + product);
        System.out.println("           Action  : Shipping cancelled\n");
    }
}

/**
 * CONCRETE OBSERVER - COURIER SERVICE
 * ----------------------------------
 * Prepares the courier team for delivery once
 * payment is confirmed.
 */
class CourierNotifier implements PaymentObserver {

    @Override
    public void onPaymentSuccess(String product) {
        System.out.println("[COURIER] Delivery Scheduled");
        System.out.println("          Product : " + product);
        System.out.println("          Action  : Prepare for delivery\n");
    }

    @Override
    public void onPaymentFailure(String product) {
        System.out.println("[COURIER] Delivery On Hold");
        System.out.println("          Product : " + product);
        System.out.println("          Action  : Await payment confirmation\n");
    }
}
