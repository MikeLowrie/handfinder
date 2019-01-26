package handfinder;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private String handname;

    private static final int SINGLE_CARD = 10;
    private static final int PAIR = 25;
    private static final int TWO_PAIR = 50;
    private static final int THREE_OF_A_KIND = 60;
    private static final int STRAIGHT = 70;
    private static final int FLUSH = 80;
    private static final int FULL_HOUSE = 90;
    private static final int FOUR_OF_A_KIND = 100;
    private static final int STRAIGHT_FLUSH = 150;
    private static final int ROYAL_FLUSH = 300;
    private static final int ROYAL_FLUSH_IN_SPADES = 400;
    private static final int EVERY_CARD_FLUSH = 1000;
    private static final int FULL_DECK = 5000;

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

    public boolean isPair() {
        return this.handname.equals("Pair");
    }

    public void print() {
        System.out.println(this.handname);
        for(Card c : hand)
            c.print();
    }

    public void clear() {
        this.hand.clear();
    }
}
