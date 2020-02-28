package handfinder;

public class Card implements Comparable<Card>{
    /*
     * Number represents the "raw" value of the card, from 2 to 14 (aces high at 14).
     * Face value represents the shown value of the card. In this class, a face value of 'A' is a number 14.
     */
    private int number;
    private char suit;
    private String facevalue;
    private static final String AVAILABLESUITS = "CcDdHhSs";

    public static final int ACE_VALUE = 14;
    public static final int KING_VALUE = 13;
    public static final int QUEEN_VALUE = 12;
    public static final int JACK_VALUE = 11;

    /**
     * Default constructor.
     */
    public Card() {
        this.number = -1;
        this.suit = 'x';
        this.facevalue = "x";
    }

    public Card(int number, char suit, String facevalue) {
        this.number = number;
        this.suit = suit;
        this.facevalue = facevalue;
    }

    /**
     * Copy constructor.
     * @param c Card to be copied.
     */
    public Card(Card c) {
        this.number = c.number;
        this.suit = c.suit;
        this.facevalue = c.facevalue;
    }

    /**
     * Creates a new Card object based on input.
     * @param input String holding face value and suit
     * @return new Card object based on provided information
     */
    public static Card makeNewCard(String input) {
        Card c = new Card();
        if(input.length() == 3) {
            // Card is a 10
            String facevalue = input.substring(0,2);
            if(!facevalue.equals("10")) {
                System.err.println(input + " is not valid input for a card. Card is not a 10, but uses three characters.");
                System.exit(-11);
            }
            char suit = input.charAt(2);
            if(!AVAILABLESUITS.contains(suit + "")) {
                System.err.println(input + " is not valid input for a card. Suit invalid.");
                System.exit(-12);
            }
            int number = 10;
            c = new Card(number, suit, facevalue + "");
        } else if(input.length() == 2) {
            char facevalue = input.charAt(0);
            char suit = input.charAt(1);
            if(!AVAILABLESUITS.contains(suit + "")) {
                System.err.println(input + " is not valid input for a card. Suit invalid.");
                System.exit(-12);
            }
            int number = Card.findNumber(facevalue);
            c = new Card(number, suit, facevalue + "");
        } else {
            System.err.println(input + " is not valid input for a card. Card length invalid.");
            System.exit(-10);
        }
        return c;
    }

    /**
     * Finds the numberic value of a card based on the face value provided. A = 14, K = 13, Q = 12, J = 11
     * @param facevalue Character representing face value of card
     * @return Numeric value of face value
     */
    public static int findNumber(char facevalue) {
        int value = 0;
        if(Character.isDigit(facevalue)) {
            value = Integer.parseInt(facevalue + "");
        } else if(facevalue == 'a' || facevalue == 'A') {
            value = 14;
        } else if(facevalue == 'k' || facevalue == 'K' ) {
            value = 13;
        } else if(facevalue == 'q' || facevalue == 'Q' ) {
            value = 12;
        } else if(facevalue == 'j' || facevalue == 'J' ) {
            value = 11;
        } else {
            System.err.println(facevalue + " is not valid input for a face value.");
            System.exit(-22);
        }

        return value;
    }

    @Override
    public int compareTo(Card other) {
        return (other.number - this.number ) * 1000 + (other.suit - this.suit);
    }

    public boolean equals(Card other) {
        return this.number == other.number && this.suit == other.suit;
    }

    public int getNumber() {
        return this.number;
    }

    public String getFacevalue() {
        return this.facevalue;
    }

    public char getSuit() {
        return this.suit;
    }

    public boolean isRoyal() {
        return this.number > 9;
    }

    public void print() {
        System.out.println("  " + this.facevalue + this.suit);
    }
}
