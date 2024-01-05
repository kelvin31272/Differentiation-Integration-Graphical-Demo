package Calculus;

public class Term {
    private double coefficient;
    private int exponent;

    public Term(double coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    // GETTERS
    public double getTermValue(double x) {
        return Math.pow(x, exponent) * coefficient;
    }

    public Term getTermSubtracted(Term operand) {
        Term termSubtracted = new Term(this.coefficient - operand.getCoefficient(), this.exponent);
        return termSubtracted;
    }

    public Term getTermDifferentiated() {
        Term termDifferentiated = new Term(coefficient * exponent, exponent - 1);
        return termDifferentiated;
    }

    public Term getTermIntegrated() {
        Term termIntegrated = new Term(coefficient / (exponent + 1), (exponent + 1));
        return termIntegrated;
    }

    public double getCoefficient() {
        return this.coefficient;
    }

    public int getExponent() {
        return this.exponent;
    }
}