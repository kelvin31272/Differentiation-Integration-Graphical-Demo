package Calculus;

import java.util.ArrayList;

import Parsing.StringParser;

public class Polynomial {
    private ArrayList<Term> terms;
    private boolean isVisible = true; ; //only graphical

    public Polynomial() {
        terms = new ArrayList<Term>();
    }

    public Polynomial(ArrayList<Term> terms) {
        this.terms = terms;
    }

    public static Polynomial createPolynomial(String userInput) {

        if (userInput.length() == 0) {
            return new Polynomial();
        }

        try { // try to parse the user input
              // clean whitespace
            userInput = userInput.replaceAll("\\s", "");

            // add in the sign of the first term if it doesn't exist
            if (userInput.charAt(0) != '+' && userInput.charAt(0) != '-') {
                userInput = '+' + userInput;
            }

            // seperate the input into tokens which we can then parse individually
            ArrayList<String> tokens = new ArrayList<String>();
            {
                char[] setA = { '+' };
                char[] setB = { '+' };
                tokens = StringParser.splitByDelimiterPairs(userInput, setA, setB);
            }

            // parse each token to get terms (each with a coefficient and an exponent)
            ArrayList<Term> terms = new ArrayList<Term>();
            {
                for (String token : tokens) {

                    double coefficient;
                    {// find coefficient
                     // check for a bracketed expression
                        coefficient = 1;
                        char[] setLeft = { '(' };
                        char[] setRight = { ')' };
                        ArrayList<String> exps = StringParser.splitByDelimiterPairs(token, setLeft, setRight);
                        if (exps.size() > 0) {
                            // there are bracketed expressions
                            for (String exp : exps) { // calculate value of expressions multiplied together
                                coefficient *= calculateExpression(exp.substring(1));
                            }

                        } else {
                            // there should be no bracketed expressions
                            if (token.contains("/") || token.contains("*")) { // check for an unbracketed exp.
                                throw new Exception("expressions should be bracketed");
                            }
                            String coefficientStr = StringParser.cutFromLeft(token, 'x');
                            if (coefficientStr.length() == 1) {
                                // no coefficient has been explicitly given, so it must be 1
                                coefficient = 1;
                            } else {
                                try {
                                    coefficient = Double.parseDouble(coefficientStr);
                                } catch (NumberFormatException e) {
                                    throw new Exception(
                                            "INVALID COEFFICIENT " + e);
                                }

                            }

                        }
                    }

                    int exponent;
                    {// find exponent
                        char[] setLeft = { '^' };
                        char[] setRight = {};
                        ArrayList<String> exps = StringParser.splitByDelimiterPairs(token, setLeft, setRight);
                        // EXCEPTIONS
                        if (exps.size() > 1) { // a polynomial should not have more than one exponent
                            throw new Exception("Invalid number of exponents for an individual term of a polynomial");
                        }
                        if (exps.size() == 1) { // exponent detected,
                            if (exps.contains("/") || exps.contains("*")) { // but we have an operator
                                throw new Exception(
                                        "Expressions are not allowed in the exponents of the terms of a polynomial");
                            }
                            if (exps.get(0).length() == 1) { // but no value given
                                throw new Exception(
                                        "Math Error");
                            }
                            if (!token.contains("x")) { // but no variable x,
                                exps.remove(0); // this is not an exponent, but an expression e.g "424^34"
                            }
                        }

                        if (exps.size() == 0) { // no power has been explicitly given, it could be 1 or 0
                            if (token.contains("x")) {
                                exps.add("^1");
                            } else {
                                exps.add("^0");
                            }
                        }
                        String exponentStr = exps.get(0).substring(1);
                        try {
                            exponent = Integer.parseInt(exponentStr);
                        } catch (NumberFormatException e) {
                            throw new Exception(
                                    "INVALID EXPONENT " + e);
                        }
                    }

                    terms.add(new Term(coefficient, exponent));

                }
            }

            // create a new function with these terms
            return new Polynomial(terms);

        } catch (Exception e) {
            System.err.println(e);
            // e.printStackTrace();
        }

        return new Polynomial();
    }

    private static Double calculateExpression(String expStr) {
        for (int i = 0; i < expStr.length() - 1; i++) {
            char ch = expStr.charAt(i);

            if (ch == '/' || ch == '*' || ch == '^') {
                try {
                    double l = Double.parseDouble(expStr.substring(0, i));
                    double r = Double.parseDouble(expStr.substring(i + 1, expStr.length()));
                    switch (ch) {
                        case ('/'):
                            return l / r;
                        case ('*'):
                            return l * r;
                        case ('^'):
                            return Math.pow(l, r);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("INVALID COEFFICIENT EXPRESSION:" + e);
                }
            }
        }
        // no operators, might be an actual value rather than an expression? e.g
        // "(4.232)"
        try {
            return Double.parseDouble(expStr);
        } catch (NumberFormatException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        // not parseable, must be an invalid expression
        return null;
    }

    public void printTerms() {
        ArrayList<Term> terms = this.getTerms();
        for (Term term : terms) {
            System.out.println(term.getCoefficient());
            System.out.println(term.getExponent());
        }
        System.out.println("");
    }

    // SETTERS
    public void toggleVisibility() {    
        this.isVisible = !this.isVisible;
    }

    // GETTERS
    public double getPolynomialOutput(double x) {
        double output = 0;
        for (Term term : terms) {
            output += term.getTermValue(x);
        }
        return output;
    }

    public ArrayList<Term> getTerms() {
        return this.terms;
    }

    public boolean getVisibility(){
        return this.isVisible;
    }
}
