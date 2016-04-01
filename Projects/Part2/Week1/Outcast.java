import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
private WordNet outcast;

public Outcast(WordNet wordnet) {
        outcast = wordnet;
}
public String outcast(String[] nouns) {
        int score = -1;
        String best = null;

        // given that nouns array has at least 2 members
        for (int i = 0; i < nouns.length; i++) {
                String curr = nouns[i];
                int sum = 0;
                for (int j = 0; j < nouns.length; j++) {
                        if (i != j) {
                                sum += outcast.distance(curr, nouns[j]);
                        }
                }
                if (sum >= score) {
                        score = sum;
                        best = curr;
                }
        }
        return best;
}
public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
                In in = new In(args[t]);
                String[] nouns = in.readAllStrings();
                StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
}
}
