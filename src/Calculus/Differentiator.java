package Calculus;

import java.util.ArrayList;

public class Differentiator {

    public Differentiator() {
    }

    public static Polynomial getDerivative(Polynomial f) {
        ArrayList<Term> oldTerms = f.getTerms();
        ArrayList<Term> newTerms = new ArrayList<Term>();
        for (Term term : oldTerms) {
            newTerms.add(term.getTermDifferentiated());
        }

        Polynomial derivative = new Polynomial(newTerms);
        return derivative;
    }
    
}
