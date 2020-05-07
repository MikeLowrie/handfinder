package handfinder;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;
    private String handname;
    private int points;

    /**
     * Generic constructor.
     */
    public Hand() {
        hand = new ArrayList<Card>();
        handname = "x";
    }

    /**
     * Main constructor that creates this Hand with Cards (possibly null), a name, and its point value.
     * @param cards Any Cards that already are part of this Hand
     * @param handname The name to show on output for this Hand
     * @param points The value of this Hand
     */
    public Hand(ArrayList<Card> cards, String handname, int points) {
        this.hand = cards;
        this.handname = handname;
        this.points = points;
    }

    /**
     * Adds the provided Card to the list of Cards in this Hand.
     * @param c Card to add
     * @return True if the Card was added successfully, false otherwise
     */
    public boolean addCard(Card c) {
        return this.hand.add(c);
    }

    /**
     * Checks the number of Cards in this Hand.
     * @return Number of Cards in Hand
     */
    public int size() {
        return this.hand.size();
    }

    /**
     * Checks for a specific card within a Hand, but does not remove it from the Hand.
     * @param index Index of this Card within the Hand
     * @return Card at "index"
     */
    public Card getCard(int index) {
        return hand.get(index);
    }

    /**
     * Returns the collection of Cards within this Hand.
     * @return All Cards within the Hand
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Returns the point value of this Hand.
     * @return Points for this Hand
     */
    public int getPoints() {
        return points;
    }

    /**
     * Resets the name and point value of this Hand. Mostly useful for reassigning a Royal Flush to a Royal Flush in Spades.
     * @param newname New name of this Hand.
     * @param newpoints New point value of this Hand.
     */
    public void reassignHand(String newname, int newpoints) {
        this.handname = newname;
        this.points = newpoints;
    }

    /**
     * Checks if this Hand is specifically a Pair.
     * @return True if this Hand is a pair, false otherwise
     */
    public boolean isPair() {
        return this.handname.equals("Pair");
    }

    /**
     * Outputs to console this Hand's name, point value, and its Cards.
     */
    public void print() {
        System.out.println(this.handname + " " + this.points);
        for(Card c : hand)
            c.print();
    }

    /**
     * Removes all Cards from the Hand.
     */
    public void clear() {
        this.hand.clear();
    }
}
