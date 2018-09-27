package handfinder;

import java.util.ArrayList;
import java.util.Collections;

public class HandFinder {
    private ArrayList<Card> masterlist;
    private ArrayList<Hand> totalhands;

    /**
     * Main method.
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        new HandFinder(args);
    }

    /**
     * Constructor that drives main program.
     * @param args Commandline arguments
     */
    public HandFinder(String[] args) {
        totalhands = new ArrayList<Hand>();
        boolean isdeckbuilt = buildList(args);
        if(!isdeckbuilt) {
            System.err.println("Failure occurred during deck build.");
            System.exit(-20);
        } else {
            Collections.sort(masterlist);
            this.findBestHands();
        }
    }

    /**
     *
     * @param args Commandline arguments
     * @return True if deck built successfully, false otherwise.
     */
    public boolean buildList(String[] args) {
        masterlist = new ArrayList<Card>();
        boolean addworked = true;
        String nextcard = "";
        for(int i = 0; i < args.length && addworked; i++) {
            // Input will be 'facevalue''suit', like QC (queen of clubs) or 4S (4 of spades)
            nextcard = args[i];
            addworked = masterlist.add(Card.makeNewCard(nextcard));
        }
        return addworked;
    }

    /**
     * Method that steps through all types of poker hands and checks the list of available cards for hands.
     */
    public void findBestHands() {
        ArrayList<Card> cards = this.cloneMasterList();

        this.checkRoyalFlush(cards);
        this.checkStraightFlush(cards);
        this.checkFourOfAKind(cards);
        this.checkFullHouse(cards);
        this.checkFlush(cards);
        this.checkStraight(cards);
        this.checkThreeOfAKind(cards);
        this.checkTwoPair(cards);
        this.checkPair(cards);
        this.checkHighCard(cards);

        this.print();
    }

    /**
     * Checks the total list of cards for a royal flush. If one is found, method is called again to find additional hands.
     * @param cards Total list of all cards currently not assigned to a hand.
     */
    public void checkRoyalFlush(ArrayList<Card> cards) {
        // @TODO: Maybe use a switch statement or something? This algorithm doesn't look like it'd work.

        // Assign locals
        int i = 0;
        boolean moveindex;
        boolean goodforroyalflush = false;
        char suittomatch = 'x';
        Hand h = new Hand(new ArrayList<Card>(), "Royal Flush");

        // Check for a royal flush. If a card has a "following" number and a matching suit, add it.
        while(i < cards.size() || h.size() == 5) {
            moveindex = true;
            Card c = cards.get(i);
            if(c.isRoyal()) {
                if(h.size() == 0) {
                    h.addCard(c);
                    suittomatch = c.getSuit();
                } else {
                    for(int j = 0; j < h.size() && !goodforroyalflush; j++) {
                        if((h.get(j)).getNumber() - 1 == c.getNumber())
                            goodforroyalflush = true;
                    }

                    if(!goodforroyalflush) {
                        if(c.getSuit() == suittomatch) {
                            h.addCard(c);
                        }
                    }
                }
            } else {
                // No more "royal" cards available. Stop calculating immediately.
                return;
            }
            i++;
        }

        // Add hand to total hands list
        if(h.size() == 5) {
            for(int k = 0; k < 5; k++) {
                cards.remove(h.get(k));
            }
            totalhands.add(h);
            if(cards.size() >= 5)
                checkRoyalFlush(cards);
        }
    }

    public void checkStraightFlush(ArrayList<Card> cards) {

    }

    public void checkFourOfAKind(ArrayList<Card> cards) {

    }

    public void checkFullHouse(ArrayList<Card> cards) {

    }

    public void checkFlush(ArrayList<Card> cards) {

    }

    public void checkStraight(ArrayList<Card> cards) {

    }

    public void checkThreeOfAKind(ArrayList<Card> cards) {

    }

    public void checkTwoPair(ArrayList<Card> cards) {

    }

    /**
     * Finds all pairs in the current list of cards.
     * @param cards Available cards to search
     */
    public void checkPair(ArrayList<Card> cards) {
        Hand h = new Hand(new ArrayList<Card>(), "Pair");
        Card current = new Card();
        Card previous = new Card();
        boolean reassignprevious = true;
        for(int i = 0; i < cards.size(); i++) {
            if(reassignprevious)
                previous = current;
            reassignprevious = true;
            current = cards.get(i);

            if(current.getNumber() == previous.getNumber() && current.getSuit() != previous.getSuit()) {
                h.addCard(current);
                h.addCard(previous);
                reassignprevious = false;
                totalhands.add(h);

                cards.remove(current);
                cards.remove(previous);
                i -= 2;

                previous = new Card();
                h = new Hand(new ArrayList<Card>(), "Pair");
            }
        }
    }

    public void checkHighCard(ArrayList<Card> cards) {
        if(cards.size() == 0)
            return;

        Hand h = new Hand(new ArrayList<Card>(), "High Card");
        h.addCard(cards.remove(0));
        totalhands.add(h);
        checkHighCard(cards);
    }

    public void print() {
        for(Card c : masterlist)
            System.out.println(c.getFacevalue() + c.getSuit());

        for(Hand h : totalhands)
            h.print();

    }

    public ArrayList<Card> cloneMasterList() {
        ArrayList<Card> clonedlist = new ArrayList<Card>();
        for(Card c : masterlist) {
            clonedlist.add(new Card(c));
        }
        return clonedlist;
    }
}
