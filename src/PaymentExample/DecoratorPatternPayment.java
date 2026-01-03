package PaymentExample;
/**
 * COMPONENT
 * ----------
 * This is the common interface for all amount calculations.
 * Both the base object and all decorators implement this.
 */
interface Amount {
    float Calculate();
}

/**
 * CONCRETE COMPONENT
 * ------------------
 * Represents the original/base amount before any modification.
 * This is the object that gets wrapped by decorators.
 */
class BaseAmount implements Amount {

    private final float amount;

    public BaseAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public float Calculate() {
        // Base case of recursion: returns the raw amount
        return amount;
    }
}

/**
 * ABSTRACT DECORATOR
 * ------------------
 * Implements the same interface (Amount)
 * and HOLDS a reference to another Amount.
 *
 * This is the core of the Decorator pattern.
 */
abstract class AmountDecorator implements Amount {

    // Wrapped object
    protected Amount amount;

    public AmountDecorator(Amount amount) {
        this.amount = amount;
    }
}

/**
 * ABSTRACT TAX DECORATOR
 * ---------------------
 * Specialized decorator type for taxes.
 * Allows grouping all tax-related decorators under one hierarchy.
 *
 * (Not strictly required, but improves clarity and extensibility.)
 */
abstract class TaxDecorator extends AmountDecorator {

    public TaxDecorator(Amount amount) {
        super(amount);
    }
}

/**
 * CONCRETE DECORATOR - GST
 * -----------------------
 * Adds GST tax to the wrapped amount.
 */
class GSTDecorator extends TaxDecorator {

    private float gstPercentage;

    public GSTDecorator(Amount amount, float gstPercentage) {
        super(amount);
        this.gstPercentage = gstPercentage;
    }

    @Override
    public float Calculate() {
        // 🔁 Recursive call to wrapped object
        float base = amount.Calculate();

        float tax = base * (gstPercentage / 100);

        return base + tax;
    }
}

/**
 * CONCRETE DECORATOR - VAT
 * -----------------------
 * Adds VAT tax to the wrapped amount.
 * Demonstrates that multiple tax types can coexist independently.
 */
class VATDecorator extends TaxDecorator {

    private float vatPercentage;

    public VATDecorator(Amount amount, float vatPercentage) {
        super(amount);
        this.vatPercentage = vatPercentage;
    }

    @Override
    public float Calculate() {
        // 🔁 Recursive delegation
        float base = amount.Calculate();

        float tax = base * (vatPercentage / 100);

        return base + tax;
    }
}

/**
 * CONCRETE DECORATOR - PACKAGE CHARGE
 * ----------------------------------
 * Adds a fixed packaging charge.
 * This is a flat fee, not percentage-based.
 */
class PackageChargeDecorator extends AmountDecorator {

    private float packageCharge;

    public PackageChargeDecorator(Amount amount, float packageCharge) {
        super(amount);
        this.packageCharge = packageCharge;
    }

    @Override
    public float Calculate() {
        // 🔁 Recursive delegation + flat addition
        return amount.Calculate() + packageCharge;
    }
}

/**
 * CONCRETE DECORATOR - DISCOUNT
 * ----------------------------
 * Applies a percentage-based discount.
 * Typically applied after tax/charges.
 */
class DiscountDecorator extends AmountDecorator {

    private float discountPercent;

    public DiscountDecorator(Amount amount, float discountPercent) {
        super(amount);
        this.discountPercent = discountPercent;
    }

    @Override
    public float Calculate() {
        // 🔁 Recursive call to resolve full amount first
        float base = amount.Calculate();

        float discount = base * (discountPercent / 100);

        return base - discount;
    }
}
