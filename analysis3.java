import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Edge {
    String origin, target;
    Edge(String origin, String target) {
        this.origin = origin;
        this.target = target;
    }
}
// creates a class that represents an edge between 2 different nodes (initialises the origin and target of the edge)

class Node {
    String name;
    Node(String name) {
        this.name = name;
    }
}
// creates a class that represents the node ( initialises name )
class Analysis {
    static List<List<Node>> nodeList;
    static Map<String, Integer> nameCount;
// creates list of nodes then uses map so we can count followers for each node later
    public static void graph(List<Edge> edgeList, int numNodes) {
        nodeList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodeList.add(new ArrayList<>());
        }
        for (Edge e : edgeList) {
            nodeList.get(nodeList.size() - 1).add(new Node(e.target));
        }
    }
//using the list of edges, creates a graph
    public static double density(int numNodes, int numEdges) {
        return (double) numEdges / (numNodes * (numNodes - 1));
    }
// function to calculate the density using the number of edges and nodes
    public static String mostFollowers(Map<String, Integer> words) {
        int maxFollowers = 0;
        String mostFollowers = "";

        for (Map.Entry<String, Integer> entry : words.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            if (count > maxFollowers) {
                maxFollowers = count;
                mostFollowers = name;
            } else if (count == maxFollowers && name.compareTo(mostFollowers) < 0) {
                mostFollowers = name;
            }
        }
        return mostFollowers;
    }
// iterates over the map and uses a count to find who has the most followers
    public static String mostFollowing(List<String[]> nameList) {
        int maxFollowing = 0;
        String mostFollowing = "";
        for (String[] names : nameList) {
            if (names.length > maxFollowing || (names.length == maxFollowing && names[0].compareTo(mostFollowing) < 0)) {
                maxFollowing = names.length;
                mostFollowing = names[0];
            }
        }
        return mostFollowing;
    }
// iterates over the namelist and uses a count to find who follows the most people by comparing length of each list
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        List<String[]> nameList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();
        nameCount = new HashMap<>();
// uses args to get the file path, then uses a list to store names and edges, and a Map to count followers
        int totalNames = 0;
        int numEdges = 0;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] names = line.split(" ");
                nameList.add(names);
                totalNames += names.length;

                String origin = names[0];
                nameCount.put(origin, nameCount.getOrDefault(origin, 0));
                for (int i = 1; i < names.length; i++) {
                    String target = names[i];
                    edgeList.add(new Edge(origin, target));
                    nameCount.put(target, nameCount.getOrDefault(target, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
            System.exit(1);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
// uses bufferedreader to read file line by line, splitting each line into names and adding them to the nameList
// has the exception if incorrect file is used
        int numNodes = nameList.size();
        numEdges = totalNames - numNodes;
        graph(edgeList, numNodes);
        System.out.println("Density: " + density(numNodes, numEdges));
        System.out.println("The person with the most followers is " + mostFollowers(nameCount));
        System.out.println("The person who follows the most people is " + mostFollowing(nameList));
    }
}
