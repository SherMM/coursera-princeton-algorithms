import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.Arrays;

public class BaseballElimination {

private static final int inf = Integer.MAX_VALUE;
private int numTeams;
private HashMap<String, Integer> teamNames;
private int[][] gameResults;
private int[][] matchups;

// create a baseball division from given filename in format specified below
public BaseballElimination(String filename) {
        In teamIn = new In(filename);
        this.numTeams = teamIn.readInt();
        this.teamNames = new HashMap<String, Integer>();
        this.gameResults = new int[this.numTeams][3];
        this.matchups = new int[this.numTeams][this.numTeams];

        int index = 0;
        while (!teamIn.isEmpty()) {
                this.teamNames.put(teamIn.readString(), index);

                // read in games won, lost, remaining
                for (int i = 0; i < 3; i++) {
                        this.gameResults[index][i] = teamIn.readInt();
                }

                // read in team-vs-team remaining matchups
                for (int i = 0; i < this.numTeams; i++) {
                        this.matchups[index][i] = teamIn.readInt();
                }

                // move on to next team
                index++;
        }
}

// number of teams
public int numberOfTeams() {
        return this.numTeams;
}


// all teams
public Iterable<String> teams() {
        return this.teamNames.keySet();
}


// number of wins for given team
public int wins(String team) {
        int index = this.teamNames.get(team);
        return this.gameResults[index][0];
}

// number of losses for given team
public int losses(String team) {
        int index = this.teamNames.get(team);
        return this.gameResults[index][1];
}

// number of remaining games for given team
public int remaining(String team) {
        int index = this.teamNames.get(team);
        return this.gameResults[index][2];
}

// number of remaining games between team1 and team2
public int against(String team1, String team2) {
        int idx1 = this.teamNames.get(team1);
        int idx2 = this.teamNames.get(team2);

        return this.matchups[idx1][idx2];
}

// is given team eliminated?
public boolean isEliminated(String team) {
        // get team index
        int index = this.teamNames.get(team);

        // get number of game vertexes
        int games = this.numGameVertexes(index);

        // num vertexes = s + t + games + numTeams
        int numVerts = 2 + games + this.numTeams;

        // build an empty flow network with numVerts vertexes
        FlowNetwork flows = new FlowNetwork(numVerts);

        // set up values for source (s) and sink (t) vertexes
        int s = numVerts - 2;
        int t = numVerts - 1;

        // get best scenario for team parameter (wins all remaining games)
        int possible = this.gameResults[index][0] + this.gameResults[index][2];

        // add team vertex to sink (t) vertex edges
        for (int i = 0; i < this.numTeams; i++) {
                // exclude team parameter
                if (i != index) {
                        int capacity = possible - this.gameResults[i][0];
                        FlowEdge edge = new FlowEdge(i, t, capacity);
                        flows.addEdge(edge);
                }
        }

        // set up mapping of game vertex to teams involved
        HashMap<Integer, Integer[]> gamesToTeams = new HashMap<Integer, Integer[]>();

        // starting vertex value for game vertex
        int gameIdx = this.numTeams;
        // add source (s) vertex to game vertex edges
        for (int i = 0; i < this.numTeams; i++) {
                // exclude team parameter
                if (i != index) {
                        for (int j = i+1; j < this.numTeams; j++) {
                                if (i != j && j != index) {
                                        // remaining games between teams i & j
                                        int capacity = this.matchups[i][j];
                                        // teams involved in a game
                                        Integer[] gameTeams = {i, j};
                                        gamesToTeams.put(gameIdx, gameTeams);
                                        FlowEdge edge = new FlowEdge(s, gameIdx, capacity);
                                        flows.addEdge(edge);
                                        gameIdx++;
                                }
                        }
                }
        }

        // add game vertex to teams vertex edges
        for (int i = this.numTeams; i < s; i++) {
                // get teams involved in each game to map edges correctly
                Integer[] gameTeams = gamesToTeams.get(i);
                for (int j = 0; j < gameTeams.length; j++) {
                        FlowEdge edge = new FlowEdge(i, gameTeams[j], inf);
                        flows.addEdge(edge);
                }
        }
        return true;
}

// write private buildFlowNetwork method to simplify isEliminated

private int numGameVertexes(int teamIdx) {
        int total = 0;
        for (int i = 0; i < this.matchups.length; i++) {
                if (i != teamIdx) {
                        for (int j = i+1; j < this.matchups.length; j++) {
                                if (j != teamIdx && i != j) {
                                        total += 1;
                                }
                        }
                }
        }
        return total;
}

/*
   // subset R of teams that eliminates given team; null if not eliminated
   public Iterable<String> certificateOfElimination(String team) {

   }
 */

public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        boolean test = division.isEliminated("Detroit");
        /*
           for (String team : division.teams()) {
                if (division.isEliminated(team)) {
                        StdOut.print(team + " is eliminated by the subset R = { ");
                        for (String t : division.certificateOfElimination(team)) {
                                StdOut.print(t + " ");
                        }
                        StdOut.println("}");
                }
                else {
                        StdOut.println(team + " is not eliminated");
                }
           }
         */
}
}
