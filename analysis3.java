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

class Node {
    String name;
    Node(String name) {
        this.name = name;
    }
}

class Analysis {
    static List<List<Node>> nodeList;
    static Map<String, Integer> nameCount;

    public static void graph(List<Edge> edgeList, int numNodes) {
        nodeList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodeList.add(new ArrayList<>());
        }
        for (Edge e : edgeList) {
            nodeList.get(nodeList.size() - 1).add(new Node(e.target));
        }
    }

    public static double density(int numNodes, int numEdges) {
        return (double) numEdges / (numNodes * (numNodes - 1));
    }

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

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        List<String[]> nameList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();
        nameCount = new HashMap<>();
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

        int numNodes = nameList.size();
        numEdges = totalNames - numNodes;
        graph(edgeList, numNodes);
        System.out.println("Density: " + density(numNodes, numEdges));
        System.out.println("The person with the most followers is " + mostFollowers(nameCount));
        System.out.println("The person who follows the most people is " + mostFollowing(nameList));
    }
}
