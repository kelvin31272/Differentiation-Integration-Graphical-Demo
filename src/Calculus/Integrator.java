package Calculus;

import java.util.ArrayList;

public class Integrator {
   
    public Integrator() {
    }

    public static Polynomial getIndefiniteIntegral(Polynomial f) {
        if(f == null){
            return null;
        }
        //integrate all terms in the function
        ArrayList<Term> oldTerms = f.getTerms();
        ArrayList<Term> newTerms = new ArrayList<Term>();
        for (Term term : oldTerms) {
            newTerms.add(term.getTermIntegrated());
        }
        Polynomial integral = new Polynomial(newTerms);
        return integral;
    }

    public static Polynomial getIndefiniteIntegral(Polynomial f1, Polynomial f2) {
        if(f1 == null || f2 == null){
            return null;
        }
        
        //first minus f2 from f1
        ArrayList<Term> f1terms = f1.getTerms();
        ArrayList<Term> f2terms = f2.getTerms();
        ArrayList<Term> f1minusf2terms = new ArrayList<Term>();
        for (Term f1term : f1terms) {
            Term newTerm = f1term;
            for(Term f2term : f2terms){
                if(f1term.getExponent() == f2term.getExponent()){
                    newTerm = newTerm.getTermSubtracted(f2term);
                }
            }
            f1minusf2terms.add(newTerm);
        }
        for(Term f2term : f2terms){
            boolean isExcess = true;
            for(Term f1term : f1terms){
                if(f1term.getExponent() == f2term.getExponent()){
                    isExcess = false;
                }
            }
            if(isExcess){
                f1minusf2terms.add(new Term(-f2term.getCoefficient(), f2term.getExponent()));
            }
        }

        //now integrate all terms in the new function
        ArrayList<Term> newTerms = new ArrayList<Term>();
        for (Term term : f1minusf2terms) {
            newTerms.add(term.getTermIntegrated());
        }
        Polynomial integral = new Polynomial(newTerms);
        return integral;

    }

    public static double getDefiniteIntegral(Polynomial F, double a, double b) {
        return F.getPolynomialOutput(b) - F.getPolynomialOutput(a);
    }

}
