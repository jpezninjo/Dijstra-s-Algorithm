
class Graph {
	public int numVertex;
	public String[] vertices;
	public Edge edges[];

	public HashTable hashtable;

	public Graph(int numVerteces, String[] vertices) {

		this.numVertex = numVerteces;
		this.vertices = vertices;
		this.edges = new Edge[numVerteces];
		
		int i;
		for (i = 0; i < numVertex; i++) {
			edges[i] = null;
		}
		
		hashtable = new HashTable(vertices);
	}

	public void insert(String origin, String destination, int cost) {

		// System.out.println("Call to insert with " + origin + " " +
		// destination + " " + cost);

		Edge start = edges[lookUp(origin)];
		Edge newEdge = new Edge(lookUp(destination), null, cost);

		newEdge.next = start;
		edges[lookUp(origin)] = newEdge;

	}

	public void print() {
		int i;
		for (i = 0; i < numVertex; i++) {
			System.out.print("Vertex " + i + " Adj. List:");
			if (edges[i] == null) {
				System.out.println("<empty>");
			} else {
				for (Edge tmp = edges[i]; tmp != null; tmp = tmp.next) {
					System.out.print(" " + tmp.neighbor);
				}
				System.out.println();
			}
		}
	}

	public int lookUp(String orig) {
		return hashtable.find(orig);
	}

	public void printLookupList() {
		for (int i = 0; i < vertices.length; i++) {
			System.out.println((i < 10 ? " " : "") + i + ": " + vertices[i]);
		}
	}

	public void printAdjacencyList() {
		for (int i = 0; i < edges.length; i++) {
			Edge tmp = edges[i];
			String concat = i + ": ";
			if (i < 10) {
				concat = " " + concat;
			}
			Edge tmp2 = tmp;
			while (tmp2 != null) {
				concat += "[" + tmp2.neighbor + "|" + tmp2.cost + "] ->";
				tmp2 = tmp2.next;
			}
			System.out.println(concat + (tmp == null ? "" : " ") + "[null]");
		}
	}

	public Edge get(int vertice) {
		return edges[vertice];
	}
}