package handfinder;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;

public class HandFinder {
    private ArrayList<Card> masterlist;
    private ArrayList<Hand> totalhands;
    private boolean isdebugmode;
    private int debugpoints;
    private String debugname;

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

    /**
     * Main method.
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Deck Checker! Please input your deck.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine();
            String[] cards = input.split(" ");
            new HandFinder(cards, false, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor that drives main program.
     * @param args Commandline arguments
     * @param isdebugmode Boolean to assist with debugging unit tests
     * @param debugpoints Integer to help apply unit tests
     */
    public HandFinder(String[] args, boolean isdebugmode, int debugpoints) {
        totalhands = new ArrayList<>();
        this.isdebugmode = isdebugmode;
        this.debugpoints = debugpoints;
        boolean isdeckbuilt = buildList(args);
        if(!isdeckbuilt) {
            System.err.println("Failure occurred during deck build.");
            System.exit(-20);
        } else {
            Collections.sort(masterlist);
            this.findBestHands();
        }
        System.out.println("Thanks for checking!");
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
        int start = 0;
        if(isdebugmode) {
            this.debugname = args[0];
            start++;
        }
        for(int i = start; i < args.length && addworked; i++) {
            // Input will be 'facevalue''suit', like QC (queen of clubs) or 4S (4 of spades)
            nextcard = args[i];
            addworked = masterlist.add(Card.makeNewCard(nextcard));
        }
        return addworked;
    }

    /**
     * Method that steps through all types of poker hands and checks the list of available cards for hands.
     */
    private void findBestHands() {
        ArrayList<Card> cards = this.cloneMasterList();

        // Order is set based on value of hand, except as noted below.
        this.checkEveryCardFlush(cards);
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

    /**
     * Checks the total list of cards for a complete set of one suit.
     * @param cards Available cards to search
     */
    public void checkEveryCardFlush(ArrayList<Card> cards) {
        int i = 0;
        int nextstart = 1;
        Hand h = new Hand(new ArrayList<Card>(), "Every Card Flush", EVERY_CARD_FLUSH);
        while(i < cards.size() && h.size() < 13) {
            Card c = cards.get(i);
            if(h.size() == 0 && c.getNumber() == Card.ACE_VALUE) {
                // If starting from the beginning, make sure this is an Ace. If so, add the card
                h.addCard(c);
            } else if(h.size() == 0 && c.getNumber() < Card.ACE_VALUE) {
                // If starting from the beginning, and this is not an Ace, stop looking.
                return;
            } else {
                // If a hand is started, look further
                Card next = h.getCard(h.size() - 1);
                if(c.getSuit() == next.getSuit() && c.getNumber() + 1 == next.getNumber()) {
                    // If the cards match suit, and is next in order, add to the hand
                    h.addCard(c);
                } else if(c.getNumber() - 1 > next.getNumber()) {
                    // If the cards are too far apart, restart the hand from the next card from originally started.
                    h.clear();
                    i = nextstart;
                    nextstart++;
                }
            }
            i++;
        }

        // Add hand to total hands list
        if(h.size() == 13) {
            for(int k = 0; k < 13; k++) {
                cards.remove(h.getCard(k));
            }
            totalhands.add(h);

            // Look for more if there are enough cards to check
            if(cards.size() >= 13)
                checkRoyalFlush(cards);
        }
    }

    /**
     * Checks the total list of cards for a royal flush. If one is found, method is called again to find additional hands.
     * @param cards Total list of all cards currently not assigned to a hand.
     */
    private void checkRoyalFlush(ArrayList<Card> cards) {
        // Assign locals
        int i = 0;
        char suittomatch = 'x';
        Hand h = new Hand(new ArrayList<Card>(), "Royal Flush", ROYAL_FLUSH);

        // Check for a royal flush. If a card has a "following" number and a matching suit, add it.
        while(i < cards.size() && h.size() < 5) {
            boolean goodforroyalflush = false;
            boolean moveindex = true;
            Card c = cards.get(i);
            if(c.isRoyal()) {
                if(h.size() == 0) {
                    h.addCard(c);
                    suittomatch = c.getSuit();
                } else {
                    for(int j = 0; j < h.size() && !goodforroyalflush; j++) {
                        if((h.getCard(j)).getNumber() - 1 == c.getNumber())
                            goodforroyalflush = true;
                    }

                    if(goodforroyalflush) {
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
                cards.remove(h.getCard(k));
            }
            totalhands.add(h);

            if(h.getCard(0).getSuit() == 's') {
                h.reassignHand("Royal Flush in Spades", ROYAL_FLUSH_IN_SPADES);
            }
			
			// Look for more if there are enough cards to check
            if(cards.size() >= 5)
                checkRoyalFlush(cards);
        }
    }

    /**
     * Finds all straight flushes in the available list of cards.
     * @param cards Available cards to search
     */
    private void checkStraightFlush(ArrayList<Card> cards) {
        int i = 0;
        Hand h = new Hand(new ArrayList<Card>(), "Straight Flush", STRAIGHT_FLUSH);
        Card last;

        while(i < cards.size()) {
            Card c = cards.get(i);
            if(h.size() == 0) {
                h.addCard(c);
            } else {
                last = h.getCard(h.size() - 1);
                if(last.getNumber() - 1 == c.getNumber() && last.getSuit() == c.getSuit()) {
                    // This card is next in sequence, add this card
                    h.addCard(c);
                } else if(last.getNumber() - 1 > c.getNumber()) {
                    // There is a gap in the cards (ex. going from Queen to Nine can't cover a straight). Reset the hand.
                    h.clear();
                    h.addCard(c);
                } // No need to clear if the numbers are the same, just keep looking on to the next
            }

            if(h.size() == 5) {
                for(int k = 0; k < 5; k++) {
                    cards.remove(h.getCard(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check
                if(cards.size() >= 5)
                    checkStraightFlush(cards);
                return;
            }
            i++;
        }
    }

    /**
     * Finds all four-of-a-kind hands in the available list of cards.
     * @param cards Available cards to search
     */
    private void checkFourOfAKind(ArrayList<Card> cards) {
        int i = 0;
        Hand h = new Hand(new ArrayList<Card>(), "Four of a Kind", FOUR_OF_A_KIND);
        Card last;

        while(i < cards.size() || h.size() == 5) {
            Card c = cards.get(i);
            if(h.size() == 0) {
                h.addCard(c);
            } else {
                last = h.getCard(h.size() - 1);
                if(last.getSuit() != c.getSuit() && last.getNumber() == c.getNumber()) {
                    // This card matches suit, add this card
                    h.addCard(c);
                } else if(last.getNumber() != c.getNumber()) {
                    // There is a gap in the cards. Reset the hand.
                    h.clear();
                    h.addCard(c);
                }
            }

            if(h.size() == 4) {
                for(int k = 0; k < 4; k++) {
                    cards.remove(h.getCard(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check, and break out of this loop to prevent incorrect state
                if(cards.size() >= 4)
                    checkFourOfAKind(cards);
                return;
            }
            i++;
        }
    }

    /**
     * Finds all full houses in the available list of cards.
     * @param cards Available cards to search
     */
    private void checkFullHouse(ArrayList<Card> cards) {
        Hand h = new Hand(new ArrayList<Card>(), "Full House", FULL_HOUSE);
        for(int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            h.addCard(c);

            // Look for a possible pair or three of a kind, or if not at least a pair
            for(int j = i + 1; j < cards.size() && h.size() < 3; j++) {
                Card check = cards.get(j);

                boolean pass = true;
                for(Card card : h.getHand())
                    if(card != check)
                        if(card.getSuit() == check.getSuit() || card.getNumber() != check.getNumber())
                            pass = false;

                if(pass)
                    h.addCard(check);

            }

            if(h.size() == 3) {
                // If a three-of-a-kind was found, look for a pair
                for(int j = i + 1; j < cards.size() - 1 && h.size() < 5; j++) {
                    Card check = cards.get(j);

                    // Make sure the card isn't already in the hand first
                    boolean uniquecard = true;
                    for(Card card : h.getHand())
                        if (card.equals(check))
                            uniquecard = false;

                    if(uniquecard) {
                        Card next = cards.get(j + 1);
                        if(next.getNumber() == check.getNumber() && next.getSuit() != check.getSuit()) {
                            // Found a pair! Add these cards and the code will automatically pop out of the for loop
                            h.addCard(check);
                            h.addCard(next);
                        }
                    }
                }
            } else if(h.size() == 2) {
                // If a pair was found, look for a three-of-a-kind
                for(int j = i + 1; j < cards.size() - 2 && h.size() < 5; j++) {
                    Card check = cards.get(j);

                    // Make sure the card isn't already in the hand first
                    boolean uniquecard = true;
                    for(Card card : h.getHand())
                        if(card.equals(check))
                            uniquecard = false;

                    if(uniquecard) {
                        int index = j + 1;
                        Card first = cards.get(index);
                        boolean continueloop = true;
                        while(check.getNumber() == first.getNumber() && index < cards.size() - 2 && continueloop) {
                            index++;
                            if(check.getSuit() != first.getSuit()) {
                                // The two cards COULD combine for a three-of-a-kind!
                                //Card second = cards.getHand(++index);
                                Card second = cards.get(index);
                                while(first.getNumber() == second.getNumber() && index < cards.size() - 1) {
                                    if(first.getSuit() != second.getSuit()) {
                                        // Found a three-of-a-kind!
                                        h.addCard(check);
                                        h.addCard(first);
                                        h.addCard(second);
                                        continueloop = false;
                                        break;
                                    } else {
                                        // Same suit, just look at the next card
                                        //second = cards.getHand(++index);
                                        index++;
                                        second = cards.get(index);
                                    }
                                }
                            } else {
                                // Same suit, just look at the next card
                                first = cards.get(++index);
                            }
                        }
                    }
                }
            }

            if(h.size() == 5) {
                for(int k = 0; k < 5; k++) {
                    cards.remove(h.getCard(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check
                if(cards.size() >= 5)
                    checkFullHouse(cards);

                return;
            }
            h.clear();
        }
    }

    /**
     * Finds all flushes in the available list of cards.
     * @param cards Available cards to search
     */
    private void checkFlush(ArrayList<Card> cards) {
        int i = 0;
        Hand h = new Hand(new ArrayList<Card>(), "Flush", FLUSH);
        Card last;

        while(i < cards.size() || h.size() == 5) {
            Card c = cards.get(i);
            if(h.size() == 0) {
                h.addCard(c);
            } else {
                last = h.getCard(h.size() - 1);
                if(last.getSuit() == c.getSuit() && last.getNumber() != c.getNumber()) {
                    // This card matches suit, add this card
                    h.addCard(c);
                }
                // Don't remove all cards from this hand until every card is counted
            }
            if(h.size() == 5) {
                for(int k = 0; k < 5; k++) {
                    cards.remove(h.getCard(k));
                }
                totalhands.add(h);

                // Look for more if there are enough cards to check
                if(cards.size() >= 5)
                    checkFlush(cards);
                return;
            }
            i++;
        }
    }

	/**
	 * Finds all straights in the available list of cards.
	 * @param cards Available cards to search
	 */
    private void checkStraight(ArrayList<Card> cards) {
		// Assign locals
		int i = 0;
		Hand h = new Hand(new ArrayList<Card>(), "Straight", STRAIGHT);
		Card last;
		
		// Check for a straight. If a card has a "following" card, add it to the hand.
		while(i < cards.size() && h.size() < 5) {
			Card c = cards.get(i);
			if(h.size() == 0) {
				h.addCard(c);
			} else {
				last = h.getCard(h.size() - 1);
				if(last.getNumber() - 1 == c.getNumber()) {
					// This card is next in sequence, add this card
					h.addCard(c);
				} else if(last.getNumber() - 1 > c.getNumber()) {
					// There is a gap in the cards (ex. going from Queen to Nine can't cover a straight). Reset the hand.
					h.clear();
					h.addCard(c);
				}
			}
			i++;
		}
		
		// Add hand to total hands list
        if(h.size() == 5) {
            for(int k = 0; k < 5; k++) {
                cards.remove(h.getCard(k));
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
    private void checkThreeOfAKind(ArrayList<Card> cards) {
		Hand h = new Hand(new ArrayList<Card>(), "Three of a Kind", THREE_OF_A_KIND);
		Card current = new Card();
		Card first = new Card();
		Card second = new Card();
		for(int i = 0; i < cards.size() && cards.size() > 3; i++) {
			current = cards.get(i);
			if(h.size() == 0) {
				h.addCard(current);
			} else if(h.size() == 1) {
				first = h.getCard(0);
				// If there is a mismatch in the number, start over with the new card.
				if(current.getNumber() == first.getNumber()) {
					if(current.getSuit() != first.getSuit()) {
						// The two cards are the same number, but different suits. Add this card to the hand.
						h.addCard(current);
					}
				} else {
					// There is a new number, reset the hand and start over.
					h = new Hand(new ArrayList<Card>(), "Three of a Kind", THREE_OF_A_KIND);
					h.addCard(current);
				}
			} else if(h.size() == 2) {
				first = h.getCard(0);
				second = h.getCard(1);
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
					h = new Hand(new ArrayList<Card>(), "Three of a Kind", THREE_OF_A_KIND);
					h.addCard(current);
				}
			}
		}
    }

    /**
     * Finds all pairs that were earlier found, then groups them into hands that are Two Pair.
     */
    private void checkTwoPair() {
        Hand first = new Hand();
        Hand second;

        for(int i = 0; i < totalhands.size(); i++) {
            second = totalhands.get(i);
            if(second.isPair()) {
                if (first.size() == 0) {
                    first = totalhands.get(i);
                } else {
                    Hand twopair = new Hand(new ArrayList<Card>(), "Two Pair", TWO_PAIR);
                    twopair.addCard(first.getCard(0));
                    twopair.addCard(first.getCard(1));
                    twopair.addCard(second.getCard(0));
                    twopair.addCard(second.getCard(1));

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
    private void checkPair(ArrayList<Card> cards) {
        Hand h = new Hand(new ArrayList<Card>(), "Pair", PAIR);
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
                h = new Hand(new ArrayList<Card>(), "Pair", PAIR);
            }
        }
    }

    /**
     * Checks for a "high card" hand, when no other hands can be found.
     * @param cards Remaining cards to be checked
     */
    private void checkHighCard(ArrayList<Card> cards) {
        if(cards.size() == 0)
            return;

        Hand h = new Hand(new ArrayList<Card>(), "High Card", SINGLE_CARD);
        h.addCard(cards.remove(0));
        totalhands.add(h);
        checkHighCard(cards);
    }

    private int getTotalPoints() {
        int totalpoints = 0;
        for (Hand h : totalhands) {
            if(!isdebugmode)
                h.print();
            totalpoints += h.getPoints();
        }
        return totalpoints;
    }

    public void print() {
        int totalpoints = this.getTotalPoints();

        if(isdebugmode) {
            boolean testpass = totalpoints == debugpoints;
            System.out.println(testpass + " | " + this.debugname + " expects " + this.debugpoints + ", counted " + totalpoints);
        } else {
            for (Card c : masterlist)
                System.out.println(c.getFacevalue() + c.getSuit());

            System.out.println("Total points: " + totalpoints);
        }
    }

    public ArrayList<Card> cloneMasterList() {
        ArrayList<Card> clonedlist = new ArrayList<Card>();
        for(Card c : masterlist) {
            clonedlist.add(new Card(c));
        }
        return clonedlist;
    }
}
