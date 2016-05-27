
public class BinomialQueue {

	private static boolean debug = false;

	public class Node {

		public Node parent;
		public Node leftChild;
		public Node rightSibling;

		public int value;
		public int priority;
		public int degree;

		public Node(int value, int priority, int degree) {
			this.value = value;
			this.priority = priority;
			this.degree = degree;
		}

		@Override
		public String toString() {
			String tostring = "" + value + "v, " + degree + "d, " + (priority == Integer.MAX_VALUE ? "INFIN" : priority)
					+ "p";
			tostring += "|parent: " + (parent != null ? parent.value : "null");
			tostring += "|leftChild: " + (leftChild != null ? leftChild.value : "null");
			tostring += "|rightSib: " + (rightSibling != null ? rightSibling.value : "null");

			return tostring;
		}
	}

	public Node head;
	public Node[] pointers;

	public BinomialQueue(int size) {
		pointers = new Node[size];
		// LOL no wonder this failed beyond size 10 for Test.java but still
		// found correct paths for
		// Dijkstra. Keeping this on svn version as a reminder.
		// insertElem(0, 0);
		// for (int i = 1; i < size; i++) {
		// insertElem(i, Dijkstra.INFINITY);
		// }
	}

	public void insertElem(int elem, int priority) {

		if (debug)
			System.out.println("Call to insert with " + elem + " and " + priority);
		Node newNode = new Node(elem, priority, 0);
		Node curr = head;
		pointers[elem] = newNode;

		if (head == null) {
			head = newNode;
			return;
		} else if (head.degree == 0) {
			if (debug)
				System.out.println("Inserting to tree with one node");
			if (curr.priority <= newNode.priority) {
				curr.leftChild = newNode;
				curr.degree += 1;
				newNode.parent = curr;
			} else {
				newNode.leftChild = curr;
				newNode.degree += 1;
				curr.parent = newNode;
				curr.leftChild = null;
				newNode.rightSibling = curr.rightSibling;
				curr.rightSibling = null;
				head = newNode;

			}
		} else if (getSize(curr, true) == Math.pow(2, curr.degree)) {
			if (debug)
				System.out.println("Creating new head");
			newNode.rightSibling = head;
			this.head = newNode;
		}

		Merge();
	}

	/**
	 * Removes the node with the smallest priority Checks all top level root
	 * nodes for such a node Connects previous tree to that nodes rightsib and
	 * the farthest rightsib of that rightsib to the root's rightsib
	 * 
	 * @return the value of the node with the smallest priority
	 * @see @Merge @Reorder
	 */
	public int removeSmallest() {

		if (head == null) {
			System.err.println("Call to removeSmallest with empty priority queue");
			return -1337;
		}

		Node low = head;
		Node prev1 = null;

		Node curr = head.rightSibling;
		Node prev2 = head;

		if (head.rightSibling == null && head.leftChild == null) {
			head = null;
			return low.value;
		}

		while (curr != null) {
			if (curr.priority < low.priority) {
				prev1 = prev2;
				low = curr;
			}
			prev2 = curr;
			curr = curr.rightSibling;
		}

		if (low == head && head.leftChild == null) {
			head = head.rightSibling;
			// return low.value;
		} else {
			breakUp(low, prev1);
			// Reorder();
		}
		// System.out.println("Pre Reorder");
		// printQueue();
		Reorder();
		// System.out.println("Post Reorder");
		// printQueue();
		Merge();
		// System.out.println("Post Merge");
		// printQueue();

		Reorder();
		// System.out.println("Post 2nd Reorder");
		// printQueue();

		Merge();
		// System.out.println("Post 2nd Merge");
		// printQueue();

		return low.value;
	}

	private void breakUp(Node toplvl, Node prev) {

		if (debug)
			System.out.println(
					"Call to breakUp with " + toplvl.value + " and prev " + (prev == null ? "null" : prev.value));

		Node tmp = toplvl;

		if (prev == null) {
			if (toplvl.degree == 0) {
				// System.out.println("Prev is null, setting head to next
				// tree");
				head = toplvl.rightSibling;
				return;
			} else {
				// System.out.println("Prev is null, setting head to left
				// child");
				head = toplvl.leftChild;
			}
		} else {
			if (toplvl.degree == 0) {
				// System.out.println("Prev aint null, setting head to next tree
				// cuz this tree is a leaf");
				prev.rightSibling = toplvl.rightSibling;
				return;
			} else {
				// System.out.println("Prev aint null, setting head to left
				// child");
				prev.rightSibling = toplvl.leftChild;
			}
		}

		if (toplvl.rightSibling != null) {
			// System.out.println("Doing extra work to connect toplvl's right");
			// System.out.println(tmp.value);
			tmp = toplvl.leftChild;
			// System.out.println(tmp.value);
			tmp.parent = null;
			while (tmp.rightSibling != null) {
				tmp.parent = null;
				tmp = tmp.rightSibling;
			}
			tmp.parent = null;
			// if (tmp.rightSibling == head) {
			// head = tmp.rightSibling;
			// }
			tmp.rightSibling = toplvl.rightSibling;
		} else {
			// System.out.println("Top lvl had no right");
			tmp = toplvl.leftChild;
			do {
				tmp.parent = null;
				tmp = tmp.rightSibling;
			} while (tmp != null);

		}
		// System.out.println("Post breakUp: ");
		// printQueue();
	}

	private void Reorder() {

		Node prev = null;
		Node curr = head;
		Node next = head.rightSibling;

		while (next != null) {
			if (curr.degree > next.degree) {
				// System.out.println(curr.value + " and " + next.value + " are
				// out of place");
				if (prev == null) {
					head = next;
				} else {
					prev.rightSibling = next;
				}
				curr.rightSibling = next.rightSibling;
				next.rightSibling = curr;
				prev = null;
				curr = head;
				next = head.rightSibling;
			} else {
				prev = curr;
				curr = next;
				next = next.rightSibling;
			}
		}
	}

	private void Merge() {
		Node prev2 = null;
		Node prev = head;
		Node curr = head.rightSibling;
		while (curr != null) {
			if (prev.degree == curr.degree && getSize(prev, true) == getSize(curr, true)) {
				combine(prev2, prev, curr);
				// printQueue();
				prev2 = null;
				prev = head;
				curr = head.rightSibling;
			} else {
				prev2 = prev;
				prev = curr;
				curr = curr.rightSibling;
			}
		}
	}

	public void combine(Node prev2, Node prev, Node curr) {
		if (curr.priority < prev.priority) {
			if (prev2 != null) {
				prev2.rightSibling = curr;
			}
			curr.degree += 1;
			prev.rightSibling = curr.leftChild;
			curr.leftChild = prev;
			prev.parent = curr;

			if (prev == head) {
				head = curr;
			}

		} else {
			prev.degree += 1;
			prev.rightSibling = curr.rightSibling;
			curr.rightSibling = prev.leftChild;
			prev.leftChild = curr;
			curr.parent = prev;
		}
	}

	public void decreaseKey(int elem, int new_priority) {
		if (debug)
			System.out.println("Call to decreaseKey on " + elem + " with " + new_priority);
		Node node = pointers[elem];
		node.priority = new_priority;
		// if (node.value != elem) {
		// System.err.println(elem + " != " + node);
		// }

		while (node.parent != null && (node.parent.priority > node.priority)) {
			swap(node, node.parent);
			node = node.parent;
		}
	}

	private void swap(Node a, Node b) {
		if (debug)
			System.out.println("Swapping " + a.value + " and " + b.value);
		pointers[a.value] = b;
		pointers[b.value] = a;
		int tmp1 = a.priority;
		int tmp2 = a.value;
		a.priority = b.priority;
		a.value = b.value;
		b.priority = tmp1;
		b.value = tmp2;
	}

	public int getSize(Node topLevel, boolean t) {
		return 1 + getSize(topLevel.leftChild);
	}

	private int getSize(Node lowerLevel) {
		if (lowerLevel == null) {
			return 0;
		}
		return 1 + getSize(lowerLevel.leftChild) + getSize(lowerLevel.rightSibling);
	}

	public boolean isEmpty() {
		return head == null;
	}

	public void printQueue() {
		boolean GallesWay = true;
		if(GallesWay){
			print(head, 0);
		}else{
			printQueue(head);
		}
	}

	public void printQueue(Node node) {
		if (node != null) {
			System.out.println(node);
			printQueue(node.leftChild);
			printQueue(node.rightSibling);
		}
	}
	
    public static void print(Node tree, int offset) {
        if (tree != null)
        {
            for (int i = 0; i < offset; i++)
                System.out.print("\t");
                System.out.println("Value- " + tree.value + " " + "Degree- " + tree.degree + ", Priority " + 
                	(tree.priority == Integer.MAX_VALUE ? "INFIN" : tree.priority));
            for (Node tmp = tree.leftChild; tmp != null; tmp = tmp.rightSibling)
            {
                print(tmp, offset+1);
            }
        }
    }

	// TODO yo test this out
	// removeSmallest works but
	// print this out. you'll find a bug with parent pointers
	public void printPointers() {
		for (int i = 0; i < pointers.length; i++) {
			System.out.println(i + ": " + pointers[i]);
		}
	}
}