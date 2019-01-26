package handfinder;

import java.lang.reflect.Array;
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

        // Order is set based on value of hand, except as noted below.
        this.checkFullDeck(cards);
        this.checkAllCardsOneSuit(cards);
        this.checkRoyalFlush(cards);
        this.checkStraightFlush(cards);
        this.checkFourOfAKind(cards);
        this.checkFullHouse(cards);
        this.checkFlush(cards);
        this.checkStraight(cards);
        this.checkThreeOfAKind(cards);
        // Pair is checked before Two Pair as it's easier afterward to just group pairs that are found.
        this.checkPair(cards);
        this.checkTwoPair();
        this.checkHighCard(cards);

        this.print();
    }

    public void checkFullDeck(ArrayList<Card> cards) {

    }

    public void checkAllCardsOneSuit(ArrayList<Card> cards) {

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
        while(i < cards.size() || h.size() < 5) {
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
			
			// Look for more if there are enough cards to check
            if(cards.size() >= 5)
                checkRoyalFlush(cards);
        }
    }

    public void checkStraightFlush(ArrayList<Card> cards) {

    }

    /**
     * Finds all four-of-a-kind hands in the available list of cards.
     * @param cards Available cards to search
     */
    public void checkFourOfAKind(ArrayList<Card> cards) {
        int i = 0;
        Hand h = new Hand(new ArrayList<Card>(), "Four of a Kind");
        Card last;

        while(i < cards.size() || h.size() == 5) {
            Card c = cards.get(i);
            if(h.size() == 0) {
                h.addCard(c);
            } else {
                last = h.get(h.size() - 1);
                if(last.getSuit() != c.getSuit() && last.getNumber() == c.getNumber()) {
                    // This card matches suit, add this card
                    h.addCard(c);
                } else if(last.getNumber() > c.getNumber()) {
                    // There is a gap in the cards. Reset the hand.
                    h.clear();
                    h.addCard(c);
                }
            }

            if(h.size() == 5) {
                for(int k = 0; k < 5; k++) {
                    cards.remove(h.get(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check
                if(cards.size() >= 5)
                    checkFourOfAKind(cards);
            }
        }
    }

    public void checkFullHouse(ArrayList<Card> cards) {

    }

    /**
     * Finds all flushes in the available list of cards.
     * @param cards Available cards to search
     */
    public void checkFlush(ArrayList<Card> cards) {
        int i = 0;
        Hand h = new Hand(new ArrayList<Card>(), "Flush");
        Card last;

        while(i < cards.size() || h.size() == 5) {
            Card c = cards.get(i);
            if(h.size() == 0) {
                h.addCard(c);
            } else {
                last = h.get(h.size() - 1);
                if(last.getSuit() == c.getSuit() && last.getNumber() != c.getNumber()) {
                    // This card matches suit, add this card
                    h.addCard(c);
                }
                // Don't remove all cards from this hand until every card is counted
            }

            if(h.size() == 5) {
                for(int k = 0; k < 5; k++) {
                    cards.remove(h.get(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check
                if(cards.size() >= 5)
                    checkFlush(cards);
            }
        }
    }

	/**
	 * Finds all straights in the available list of cards.
	 * @param cards Available cards to search
	 */
    public void checkStraight(ArrayList<Card> cards) {
		// Assign locals
		int i = 0;
		Hand h = new Hand(new ArrayList<Card>(), "Straight");
		Card last;
		
		// Check for a straight. If a card has a "following" card, add it to the hand.
		while(i < cards.size() || h.size() == 5) {
			Card c = cards.get(i);
			if(h.size() == 0) {
				h.addCard(c);
			} else {
				last = h.get(h.size() - 1);
				if(last.getNumber() - 1 == c.getNumber()) {
					// This card is next in sequence, add this card
					h.addCard(c);
				} else if(last.getNumber() - 1 > c.getNumber()) {
					// There is a gap in the cards (ex. going from Queen to Nine can't cover a straight). Reset the hand.
					h.clear();
					h.addCard(c);
				}
			} 
		}
		
		// Add hand to total hands list
        if(h.size() == 5) {
            for(int k = 0; k < 5; k++) {
                cards.remove(h.get(k));
            }
            totalhands.add(h);
			
			// Look for more if there are enough cards to check
            if(cards.size() >= 5)
                checkStraight(cards);
        }
    }

    /**
     * Finds all three-of-a-kind hands in the current list of cards.
     * @param cards Available cards to search
     */
    public void checkThreeOfAKind(ArrayList<Card> cards) {
		Hand h = new Hand(new ArrayList<Card>(), "Three of a Kind");
		Card current = new Card();
		Card first = new Card();
		Card second = new Card();
		for(int i = 0; i < cards.size() && cards.size() > 3; i++) {
			current = cards.get(i);
			if(h.size() == 0) {
				h.addCard(current);
			} else if(h.size() == 1) {
				first = h.get(0);
				// If there is a mismatch in the number, start over with the new card.
				if(current.getNumber() == first.getNumber()) {
					if(current.getSuit() != first.getSuit()) {
						// The two cards are the same number, but different suits. Add this card to the hand.
						h.addCard(current);
					}
				} else {
					// There is a new number, reset the hand and start over.
					h = new Hand(new ArrayList<Card>(), "Three of a Kind");
					h.addCard(current);
				}
			} else if(h.size() == 2) {
				first = h.get(0);
				second = h.get(1);
				// If there is a mismatch in the number, start over with the new card.
				if(current.getNumber() == second.getNumber()) {
					if(current.getSuit() != first.getSuit() && current.getSuit() != second.getSuit()) {
						// There is a uniqueness to all three cards' suits, the three of a kind is complete!
						h.addCard(current);
						totalhands.add(h);
						cards.remove(first);
						cards.remove(second);
						cards.remove(current);
						
						// Start over from the beginning
						i = -1;
					}
				} else {
					// There is a mismatch in the number on the sorted cards. No more three of a kinds are possible.
					h = new Hand(new ArrayList<Card>(), "Three of a Kind");
					h.addCard(current);
				}
			}
		}
    }

    /**
     * Finds all pairs that were earlier found, then groups them into hands that are Two Pair.
     */
    public void checkTwoPair() {
        Hand first = new Hand();
        Hand second;

        for(int i = 0; i < totalhands.size(); i++) {
            second = totalhands.get(i);
            if(second.isPair()) {
                if (first == null) {
                    first = totalhands.get(i);
                } else {
                    Hand twopair = new Hand(new ArrayList<Card>(), "Two Pair");
                    twopair.addCard(first.get(0));
                    twopair.addCard(first.get(1));
                    twopair.addCard(second.get(0));
                    twopair.addCard(second.get(1));

                    totalhands.add(twopair);
                    totalhands.remove(first);
                    totalhands.remove(second);
                    i -= 2;

                    first = new Hand();
                }
            }
        }
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
