package handfinder;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private String handname;

    public Hand() {
        hand = new ArrayList<Card>();
        handname = "x";
    }

    public Hand(ArrayList<Card> cards, String handname) {
        this.hand = cards;
        this.handname = handname;
    }

    public boolean addCard(Card c) {
        return this.hand.add(c);
    }

    public int size() {
        return this.hand.size();
    }

    public Card get(int index) {
        return hand.get(index);
    }

    public void print() {
        System.out.println(this.handname);
        for(Card c : hand)
            c.print();
    }
}
