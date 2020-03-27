package handfinder;

/**
 * Class that helps log previous tests to see what works and what doesn't.
 */
public class UnitTestDriver {

    public UnitTestDriver() {
        String[] fourofakindtest = new String[] {"Four of a Kind Test 1", "Ac", "Kh", "Qd", "Qd", "Js", "10s", "10d", "10c", "10h", "10h", "10h", "7d", "7c", "4d", "4c", "3d", "3h"};
        // Ac Kh Qd Qd Js 10s 10d 10c 10h 10h 10h 7d 7c 4d 4c 3d 3h
        // four of a kind = 100 "10s", "10d", "10c", "10h"
        // straight = 70 "Ac", "Kh", "Qd", "Js", "10h",
        // two pair = 50 "7d", "7c", "4d", "4c",
        // pair = 25 "3d", "3h"
        // two high cards = 20 [10 each] "Qd",  "10h",
        // total should be 265
        new HandFinder(fourofakindtest, true, 265);

        //Ac Kc Kh Qc Qd Qd Jc Js 10c 10s 10d 10c 10h 10h 10h 7d 7c 4d 4c 3d 3h
        // straight flush, 385 points

        //Ac Kc Kh Qc Qd Qd Jc Js 10c 10s 10d 10c 10h 10h 10h 7d 7c 4d 4c 3d 3h
        // royal flush, 535


    }
}