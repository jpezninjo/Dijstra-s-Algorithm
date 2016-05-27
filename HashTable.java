
public class HashTable {

	public class Node {
		public Node next;
		public String city;
		public int vertex;
		public String data;

		public Node(String data, int vertex) {
			this.data = data;
			this.vertex = vertex;
		}
	}

	private Node[] table;
	private int TABLE_SIZE;

	public HashTable() {
		table = new Node[10];
		TABLE_SIZE = 10;
	}

	public HashTable(String[] verteces) {
		table = new Node[verteces.length];
		TABLE_SIZE = verteces.length;
		for (int i = 0; i < verteces.length; i++) {

			put(verteces[i], i);
		}
	}

	public void insert(Integer integer, String key) {
		put(key, integer);
	}

	public void put(String value, int i) {
		int hash = sfold(value, TABLE_SIZE);
		Node newNode = new Node(value, i);
		newNode.next = table[hash];
		table[hash] = newNode;
	}

	// http://research.cs.vt.edu/AVresearch/hashing/strings.php
	// Use folding on a string, summed 4 bytes at a time
	private int sfold(String s, int M) {
		int intLength = s.length() / 4;
		long sum = 0;
		for (int j = 0; j < intLength; j++) {
			char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
			long mult = 1;
			for (int k = 0; k < c.length; k++) {
				sum += c[k] * mult;
				mult *= 256;
			}
		}

		char c[] = s.substring(intLength * 4).toCharArray();
		long mult = 1;
		for (int k = 0; k < c.length; k++) {
			sum += c[k] * mult;
			mult *= 256;
		}

		return (int) (Math.abs(sum) % M);
	}

	public Integer find(String key) {
		int pos = (int) sfold(key, TABLE_SIZE);
		Node tmp = table[pos];
		while (tmp != null) {
			if (tmp.data.equals(key)) {
				return tmp.vertex;
			}
			tmp = tmp.next;
		}
		return null;
	}

	public void delete(String key) {

		if (find(key) == null) {
			System.out.println("Call to delete, element not in HashTable");
			return;
		}

		int pos = (int) sfold(key, TABLE_SIZE);
		Node prev = null;
		Node tmp = table[pos];
		while (tmp != null) {
			if (tmp.data.equals(key)) {
				if (prev == null) {
					table[pos] = tmp.next;
				} else {
					prev.next = tmp.next;
				}
			}
			prev = tmp;
			tmp = tmp.next;
		}
	}

	public void printHashTable() {
		for (int i = 0; i < TABLE_SIZE; i++) {
			String string = i + ": ";
			for (Node tmp = table[i]; tmp != null; tmp = tmp.next) {
				string += "[" + tmp.vertex + "|" + tmp.data + "] -> ";
			}
			System.out.println(string + "null");
		}
	}
}
