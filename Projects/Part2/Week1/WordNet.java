import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class WordNet {
private In synIn;   // input scanner for synset
private In hypIn;   // input scanner for hypernyms
private HashMap<Integer, List<String> > synSet;
private HashMap<String, List<Integer> > synRev;
private Digraph wordnet;

// constructor takes the name of the two input files
public WordNet(String synsets, String hypernyms) {

        // synset with integer key and list of its synonyms
        synSet = new HashMap<Integer, List<String> >();

        // reverse of synset where each word corresponds to its integer keys
        synRev = new HashMap<String, List<Integer> >();

        // count number of synsets (== Number of vertexes in digraph)
        int count = 0;
        // read in synset
        synIn = new In(synsets);
        while(!synIn.isEmpty()) {
                /*
                   Each line has following format:
                   numeric string, strings (space delimeter), string definition
                   string definition is ignored
                 */
                String s = synIn.readLine();
                String[] fields = s.split(",");

                // set up synSet hashmap
                Integer key = Integer.parseInt(fields[0]);
                String[] words = fields[1].split(" ");
                synSet.put(key, Arrays.asList(words));
                count++;

                // set up synRev hashmap
                for (String word : words) {
                        if (synRev.containsKey(word)) {
                                synRev.get(word).add(key);
                        } else {
                                // need to initialize key and value
                                List<Integer> nodes = new ArrayList<Integer>();
                                nodes.add(key);
                                synRev.put(word, nodes);
                        }
                }
        }

        // read in hypernyms
        hypIn = new In(hypernyms);
        while(!hypIn.isEmpty()) {
                /*
                    Each line has following format:
                    numeric string, numeric string, numeric string
                 */
                String s = hypIn.readLine();
                String[] fields = s.split(",");

                // set up Digraph
                // user number of synsets (count) as number of vertexes
                wordnet = new Digraph(count);

                // first numeric string in fields is v in (v, w)
                // fields[1:] are the neighboring vertexes to v
                int v = Integer.parseInt(fields[0]);

                // add neighboring edges
                for (int i = 1; i < fields.length; i++) {
                        int w = Integer.parseInt(fields[i]);
                        wordnet.addEdge(v, w);
                }
        }
}

// returns all WordNet nouns
public Iterable<String> nouns() {
        return synRev.keySet();
}

// is the word a WordNet noun?
public boolean isNoun(String word) {
        return synRev.containsKey(word);
}

// distance between nounA and nounB
public int distance(String nounA, String nounB) {
        return 1;
}

// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
// in a shortest ancestral path
public String sap(String nounA, String nounB) {
        String hello = "hello";
        return hello;
}

// do unit testing of this class
public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        // get nouns
        Iterable<String> nouns = wordnet.nouns();
        for(String noun : nouns) {
                StdOut.println(noun);
        }
}
}
