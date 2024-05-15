import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Edge {
	int origin, target;
	Edge(int origin, int target){
		this.origin = origin;
		this.target = target;
	}
}

class Node {
	int ID;
	String name;
	Node(int ID, String name) { 
		this.ID = ID;
		this.name = name;
		}
	}
class Analysis {	
	static List<List<Node>> nodeList;
	static int numEdges;
	static Map<Integer, String> IDToName;
	static Map<String, Integer> nameToID;
		
	public static void graph(List<Edge> edgeList, int numNodes){
		nodeList = new ArrayList<>();
		for (int i = 0; i < numNodes; i++){
			nodeList.add(new ArrayList<>());
		}
		for (Edge e : edgeList){
			String targetName = IDToName.get(e.target);
			nodeList.get(e.origin).add(new Node(e.target, targetName));
			numEdges = countEdges(edgeList);
		}
	}
	private static int countEdges(List<Edge> edgelList){
		Set<Edge> differentEdges = new HashSet<>(edgelList);
		differentEdges.addAll(edgelList);
		return differentEdges.size();
	}
	public static double density(){
		int numNodes = nodeList.size();
		for (List<Node> nodes : nodeList){
		numEdges += nodes.size();
	}
	return (double) numEdges / (numNodes * (numNodes - 1));
	}	
	
	public static String mostFollowers(){
		int[] followersCount = new int[nodeList.size()];
		for (List<Node> nodes: nodeList){
			for(Node node : nodes){
				followersCount[node.ID]++;
			}
			
		}
		int maxFollowers = 0;
		String mostFollowers = ""; 
		for (int i=0; i < followersCount.length; i++){
			if (maxFollowers < followersCount[i]){
				maxFollowers = followersCount[i] ;
				mostFollowers = IDToName.get(i);
			}
			else if (maxFollowers == followersCount[i] && IDToName.get(i).compareTo(mostFollowers)< 0) {
				mostFollowers = IDToName.get(i);
			}
		}
	return mostFollowers;
	}

	public static String mostFollowing(){
		int maxFollowing = 0;
		String mostFollowing = "";
		for (int i = 0; i < nodeList.size(); i++){
			int followingCount = nodeList.get(i).size();
			String currentName = IDToName.get(i);
		
		if(maxFollowing < followingCount){ 				
			maxFollowing = followingCount;
			mostFollowing = currentName;
			}
		else if (maxFollowing == followingCount){
			if(currentName.compareTo(mostFollowing)< 0){
				mostFollowing = currentName;
			}
		}
		}
		return mostFollowing;
		}
	public static void main (String[] args) throws IOException{

		String filePath = args[0];
		List<Edge> edgeList = new ArrayList<>();
		nameToID = new HashMap<>();
		IDToName = new HashMap<>();
		int numNodes = 0;

		BufferedReader reader = null;
		try{
			reader = new BufferedReader (new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null){
				String[] names  = line.split(" ");
				String originName = names[0];

				int originID = assignIDIfNeeded(originName, numNodes++);
				if (originID == numNodes) numNodes++;
				for (int i = 1; i < names.length; i++){
					String targetName  = names[i];
					int targetID = assignIDIfNeeded( targetName, numNodes++);
					if(targetID == numNodes) numNodes++;
					
					edgeList.add(new Edge(originID, targetID));
				}

			}
		} catch (FileNotFoundException e){
			System.err.println("file not found" + e.getMessage());
			System.exit(1);
		} finally{
			if (reader != null){
				reader.close();
			}
		}
		// parse content
		graph(edgeList, numNodes);

		System.out.println(numNodes);
		System.out.println(numEdges);
		System.out.println("density = " + density());
		System.out.println("the person with the most followers is" + mostFollowers());
		System.out.println("the person who follows the most people is"+ mostFollowing());
		}

		private static int assignIDIfNeeded(String name, int currentID) {
			if (!nameToID.containsKey(name)){
				nameToID.put(name, currentID);
				IDToName.put(currentID, name);
				return currentID;
			}
			return nameToID.get(name);
}
}
