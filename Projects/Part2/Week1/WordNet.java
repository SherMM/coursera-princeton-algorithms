import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class WordNet {
private HashMap<Integer, String> synSet;
private HashMap<String, List<Integer>> synRev;
private Digraph wordnet;
private SAP sapGraph;

// constructor takes the name of the two input files
public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException("Null Agruments");
        // synset with integer key and list of its synonyms
        synSet = new HashMap<Integer, String>();

        // reverse of synset where each word corresponds to its integer keys
        synRev = new HashMap<String, List<Integer>>();

        // count number of synsets (== Number of vertexes in digraph)
        int count = 0;
        // read in synset
        In synIn = new In(synsets);
        while (!synIn.isEmpty()) {
                /*
                   Each line has following format:
                   numeric string, strings (space delimeter), string definition
                   string definition is ignored
                 */
                String s = synIn.readLine();
                String[] fields = s.split(",");

                // set up synSet hashmap
                int key = Integer.parseInt(fields[0]);
                String wordString = fields[1];
                String[] words = fields[1].split(" ");
                //synSet.put(key, Arrays.asList(words));
                synSet.put(key, wordString);
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
        In hypIn = new In(hypernyms);
        // set up Digraph
        // user number of synsets (count) as number of vertexes
        wordnet = new Digraph(count);
        while (!hypIn.isEmpty()) {
                /*
                    Each line has following format:
                    numeric string, numeric string, numeric string
                 */
                String s = hypIn.readLine();
                String[] fields = s.split(",");

                // first numeric string in fields is v in (v, w)
                // fields[1:] are the neighboring vertexes to v
                int v = Integer.parseInt(fields[0]);
                // add neighboring edges
                for (int i = 1; i < fields.length; i++) {
                        int w = Integer.parseInt(fields[i]);
                        wordnet.addEdge(v, w);
                }
        }

        // check for cycles - must be DAG
        DirectedCycle finder = new DirectedCycle(wordnet);
        if (finder.hasCycle()) {
                throw new IllegalArgumentException("Not a DAG");
        }

        // check that wordnet is a rooted DAG
        int zeroOutDegreeCount = 0; // number outdegree zero nodes
        for (int vertex = 0; vertex < wordnet.V(); vertex++) {
                if (zeroOutDegreeCount > 1) {
                        throw new IllegalArgumentException("Not a rooted DAG");
                }
                if (wordnet.outdegree(vertex) == 0) {
                        zeroOutDegreeCount++;
                }
        }

        // handle if an extra root occurs on last step
        if (zeroOutDegreeCount > 1) {
                throw new IllegalArgumentException("Not a rooted DAG");
        }

        if (zeroOutDegreeCount == 0) {
                throw new IllegalArgumentException("Not a rooted DAG");
        }

        // create SAP object for wordnet object
        sapGraph = new SAP(wordnet);
}

// returns all WordNet nouns
public Iterable<String> nouns() {
        return synRev.keySet();
}

// is the word a WordNet noun?
public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException("Null argument");
        return synRev.containsKey(word);
}

// distance between nounA and nounB
public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException("Null Arguments");
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) throw new IllegalArgumentException("Not wordnet nouns");

        // get vertex keys for nounA and nounB
        List<Integer> averts = synRev.get(nounA);
        List<Integer> bverts = synRev.get(nounB);

        return sapGraph.length(averts, bverts);
}

// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
// in a shortest ancestral path
public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException("Null Arguments");
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) throw new IllegalArgumentException("Not wordnet nouns");

        // get vertex keys for nounA and nounB
        List<Integer> averts = synRev.get(nounA);
        List<Integer> bverts = synRev.get(nounB);

        // get common ancestor
        int a = sapGraph.ancestor(averts, bverts);

        return synSet.get(a);
}

// do unit testing of this class
public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);

        // get nouns
        Iterable<String> nouns = wordnet.nouns();
        int nounCount = 0;
        for (String noun : nouns) {
                nounCount++;
        }
        StdOut.println(nounCount);

        StdOut.println(wordnet.sap("worm", "bird"));
        StdOut.println(wordnet.distance("worm", "bird"));
}
}
