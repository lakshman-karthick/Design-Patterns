package PaymentExample;


import java.util.Stack;

interface Command {
    void execute();
    void undo();
}

class PaymentCommand implements Command {

    Amount amount;
    Payment pay;
    String Product;


    PaymentCommand(Payment pay, Amount amount, String Product) {
        this.amount = amount;
        this.pay = pay;
        this.Product = Product;
    }

    @Override
    public void execute() {
        pay.PaymentToConsumption(amount, Product);
    }

    @Override
    public void undo() {
        pay.RefundPayment(amount, Product);
    }
}


class selectPaymentMethodCommand implements Command {
    PaymentMethod paymentMethod;
    Payment pay;
    Stack<PaymentMethod> previousPaymentMethodStack = new Stack<>();
    selectPaymentMethodCommand(PaymentMethod paymentMethod, Payment pay) {
        this.paymentMethod = paymentMethod;
        this.pay = pay;
    }

    @Override
    public void execute() {
        previousPaymentMethodStack.push(pay.paymentMethod);
        pay.setPaymentMethod(paymentMethod);
    }

    @Override
    public void undo() {
        PaymentMethod undoPaymentMethod = previousPaymentMethodStack.pop();
        pay.setPaymentMethod(undoPaymentMethod);
    }
}

class LoggingCommand implements Command {
    Command cmd;
    LoggingCommand(Command cmd) {
        this.cmd = cmd;
    }

    @Override
    public void execute() {
        System.out.println("[LOG] START " + cmd.getClass().getSimpleName());
        try {
            cmd.execute();
            System.out.println("[LOG] SUCCESS " + cmd.getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("[LOG] FAILURE " + cmd.getClass().getSimpleName()
                    + " | Reason: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void undo() {
        System.out.println("[LOG] UNDO " + cmd.getClass().getSimpleName());
        cmd.undo();
    }

}



