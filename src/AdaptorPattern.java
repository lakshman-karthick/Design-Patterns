interface PaymentProcessor {
    void pay(double amount);
}

class PaytmAdapter implements PaymentProcessor {

    private PaytmSDK paytmSDK;

    public PaytmAdapter(PaytmSDK paytmSDK) {
        this.paytmSDK = paytmSDK;
    }

    @Override
    public void pay(double amount) {
        paytmSDK.makePayment(amount);
    }
}

class RazorpayAdapter implements PaymentProcessor {

    private RazorpaySDK razorpaySDK;

    public RazorpayAdapter(RazorpaySDK razorpaySDK) {
        this.razorpaySDK = razorpaySDK;
    }

    @Override
    public void pay(double amount) {
        razorpaySDK.process(amount);
    }
}

class PaytmSDK {
    void makePayment(double rupees) {
        System.out.println("Paid ₹" + rupees + " using Paytm");
    }
}

class RazorpaySDK {
    void process(double amountInRupees) {
        System.out.println("Paid ₹" + amountInRupees + " using Razorpay");
    }
}

public class AdaptorPattern {
    public static void main(String[] args) {


        PaymentProcessor paytm =
                new PaytmAdapter(new PaytmSDK());


        PaymentProcessor razorpay =
                new RazorpayAdapter(new RazorpaySDK());


        paytm.pay(500);
        razorpay.pay(1000);
    }
}
