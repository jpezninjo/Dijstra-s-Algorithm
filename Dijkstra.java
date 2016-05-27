import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Dijkstra {

	public final static int INFINITY = Integer.MAX_VALUE;
	private static boolean debug = false;

	public static void main(String[] args) {

		BufferedReader reader;
		// BufferedWriter writer = null;
		String input = args[0];
		// This project was completed by a college student
		// on a Saturday night at 4 AM
		// after taking way too much Niquil
		// String output = "ItookTooMuchNiquil";

		Graph graph = null;
		// HashTable hashtable = null;
		DijkstraTable dijkstraTable = null;
		BinomialQueue Q = null;

		String nextLine;
		String tmp = "";
		String verteces[] = null;

		if (args[0] == null) {
			Usage();
		}

		try {
			reader = new BufferedReader(new FileReader(input));

			nextLine = reader.readLine();
			while (nextLine.compareTo(".") != 0) {
				tmp += nextLine + " ";
				nextLine = reader.readLine();
			}

			verteces = tmp.split(" ");
			graph = new Graph(verteces.length, verteces);
			// hashtable = new HashTable(verteces);
			dijkstraTable = new DijkstraTable(verteces.length);
			Q = new BinomialQueue(verteces.length);
			Q.insertElem(0, 0);
			for (int i = 1; i < verteces.length; i++) {
				Q.insertElem(i, Dijkstra.INFINITY);
			}

			nextLine = reader.readLine();
			while (nextLine != null) {
				graph.insert(nextLine, reader.readLine(), Integer.valueOf(reader.readLine()));
				nextLine = reader.readLine();

			}
		} catch (IOException e) {
			System.out.println("File Error");
		}

		// Comment out this section
		// to view supporting data structures

		// System.out.println("Name List");
		// graph.printLookupList();
		// System.out.println();
		//
		// System.out.println("Adjacency List");
		// graph.printAdjacencyList();
		// System.out.println();
		//
		// System.out.println("New Dijkstra Table");
		// dijkstraTable.printDijkstraTable();
		// System.out.println();

		int nextV = -12;
		int runningpathcost = 0;

		while (!Q.isEmpty()) {

			// grab an arbitrary vertex that we haven't seen before
			// compare cost of (shortest path from start to vertex) + each
			// neighbor
			// with the current value in dijkstaTable
			// Q.printQueue();
			nextV = Q.removeSmallest();
			runningpathcost = dijkstraTable.getPathCost(nextV);
			if (debug) {
				System.out.println("=======================================");
				System.out.println(
						"Currently looking at vertex " + nextV + " whose cost from start is " + runningpathcost);
			}

			for (Edge tmp1 = graph.get(nextV); tmp1 != null; tmp1 = tmp1.next) {

				if (debug) {
					System.out.println();
					System.out.println(tmp1.neighbor);
					System.out.println("New path = " + runningpathcost + " + " + tmp1.cost);
					System.out.println("Old Cost = " + dijkstraTable.getPathCost(tmp1.neighbor));
				}

				Q.decreaseKey(tmp1.neighbor, runningpathcost + tmp1.cost);

				if ((runningpathcost + tmp1.cost) < dijkstraTable.getPathCost(tmp1.neighbor)) {
					if (debug)
						System.out.println("Updating cost of " + tmp1.neighbor);
					dijkstraTable.update(tmp1.neighbor, nextV, runningpathcost + tmp1.cost);
				}
			}
			if (debug)
				System.out.println();
		}

		System.out.println("Finished Dijkstra Table");
		dijkstraTable.printDijkstraTable();
		System.out.println();

		String sPath = "";
		int start = 0; // This variable controls the starting vertex
		int path = 0;
		int cost = 0;

		System.out.println("Original Graph");

		for (int i = 0; i < verteces.length; i++) {
			String builder = graph.vertices[i] + ":";
			for (Edge tmp1 = graph.edges[i]; tmp1 != null; tmp1 = tmp1.next) {
				builder += " " + graph.vertices[tmp1.neighbor] + " " + tmp1.cost + ",";
			}
			builder = builder.substring(0, builder.length() - 1);
			System.out.println(builder);
		}
		System.out.println("\nShortestPaths");

		for (int i = 0; i < verteces.length; i++) {
			sPath = "";
			cost = 0;
			path = dijkstraTable.getShortestPath(i);
			// System.out.println("First shortest path for " + i + ": " + path);
			cost = dijkstraTable.getPathCost(i);
			while (path != start) {
				if (path == -1) {
					break;
				} else {
					sPath = " " + graph.vertices[path] + sPath;
					path = dijkstraTable.getShortestPath(path);

				}
			}
			if (path == -1) {
				System.out.println(graph.vertices[i] + " 0 : "
						+ (dijkstraTable.getPathCost(i) == INFINITY ? "NO PATH" : "") + graph.vertices[i]);
			} else {
				System.out.println(graph.vertices[i] + " " + cost + ":" + " " + graph.vertices[start] + sPath + " "
						+ graph.vertices[i]);
			}
		}
	}

	/**
	 * I heart Pacheco 2016
	 */
	private static void Usage() {
		System.out.println("Usage java Dijkstra <input file>");
		System.out.println("input file- name of text file for Graph reading/making");
		System.exit(0);
	}

}
