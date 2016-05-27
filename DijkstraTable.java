
public class DijkstraTable {

	private final int INFINITY = Integer.MAX_VALUE;
	int cost[];
	int path[];
	
	public DijkstraTable(int size){
		cost = new int[size];
		path = new int[size];
		cost[0] = 0;
		for(int i = 1; i < size; i++){
			cost[i] = INFINITY;
		}
		for(int i = 0; i < size; i++){
			path[i] = -1;
		}
	}
	
	//TODO: fuck these methods. just keep two arrays in the Dijkstra class
	public void updateCost(int index, int newValue){
		cost[index] = newValue;
	}
	
	public void updatePath(int index, int newValue){
		path[index] = newValue;
	}
	
	public void update(int index, int newPath, int newCost){
//		System.out.println("Comparing newCost " + newCost + " to " + cost[index]);
		if(newCost < cost[index]){
//			System.err.println("Changed " + index + " from " + 
//					"(" + path[index] + "," + cost[index] + ") to " +
//					"(" + newPath + "," + newCost + ")");
			
			cost[index] = newCost;
			path[index] = newPath;
			
		}else{
			System.out.println("Awkard...didn't do anything");
		}
	}
	
	public void printDijkstraTable(){
		System.out.println(" Cost\t" + (cost[1] == INFINITY ? "\t" : "") + " Path");
		for(int i = 0; i < cost.length; i++){
			String builder = "";
			if(Integer.valueOf(i).toString().length() == 1){
				builder += " ";
			}
			builder += i + ": " + cost[i];
			if(Integer.valueOf(cost[i]).toString().length() <= 3){
//				builder += (cost[i] == INFINITY ? "\t\t" : "\t");
				builder += "\t";
			}
			System.out.println( builder + (cost[0] == 0 && cost[1] == INFINITY ? "\t" : "") + " " + path[i]);
		
		}
	}
	
	public int getShortestPath(int vertex){
		return path[vertex];
	}
	
	public int getPathCost(int vertex){
		return cost[vertex];
	}
}
