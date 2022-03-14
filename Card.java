package flashcards;

import java.util.ArrayList;
import java.util.List;

public class Card {
    String term;
    String definition;
    int error;
    static List<Card> cards = new ArrayList<>();

    static public int getMaxNumError() {
        int max = 0;
        if(cards.size()>0) {
            for (Card card : cards) {
                if (card.getError() > max) {
                    max = card.getError();
                }
            }
        }
        return max;
    }

    public Card(String term, String definition, int error) {
        this.term = term;
        this.definition = definition;
        this.error = error;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void upError() {
        this.error = error+1;
    }


}
