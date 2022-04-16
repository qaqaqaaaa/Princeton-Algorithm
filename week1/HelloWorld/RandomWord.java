/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        double i = 0.0;
        String champion = "";
        String sup = "";
        while (!StdIn.isEmpty()) {
            i++;
            sup = StdIn.readString();
            if (StdRandom.bernoulli(1 / i)) {
                champion = sup;
            }
        }
        System.out.println(champion);
    }
}
