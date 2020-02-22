package handfinder;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private String handname;
    private int points;



    public Hand() {
        hand = new ArrayList<Card>();
        handname = "x";
    }

    public Hand(ArrayList<Card> cards, String handname, int points) {
        this.hand = cards;
        this.handname = handname;
        this.points = points;
    }

    public boolean addCard(Card c) {
        return this.hand.add(c);
    }

    public int size() {
        return this.hand.size();
    }

    public Card getHand(int index) {
        return hand.get(index);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    public boolean isPair() {
        return this.handname.equals("Pair");
    }

    public void print() {
        System.out.println(this.handname + " " + this.points);
        for(Card c : hand)
            c.print();
    }

    public void clear() {
        this.hand.clear();
    }
}
