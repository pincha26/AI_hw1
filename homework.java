import java.io.*;
import java.util.*;

class Edge{
	Node nodeName;
	int cost;
	
	Edge(Node node, int pathCost){
		nodeName = node;
		cost = pathCost;
	}
}

class Node{
	private static int objectCounter = 0;
	static ArrayList<Node> exists =new ArrayList<Node>();
	public String state;
	public int totalCost;
	public LinkedList<Edge> neighbouringNodes = new LinkedList<Edge>();
	public Node parent;
	public int  heuristicCost;
	public int totalHeuristicCost;
	
	//constructor
	Node(){
			neighbouringNodes = new LinkedList<Edge>();
	}
	
	private Node(String value)
    {
        this.state=value;
    }
	
	
    public Node getInstance(String value)
    {
        Node node=null;
        for(Node a: exists)
        {
            if(a.getVal().equals(value))
                return a;
        }
        node=new Node(value);
        objectCounter++; 
        exists.add(node);
        return node;
    }
    public static int getCounter() {
        return objectCounter;
    }
    public String getVal() {
        return state;
    }
    
    public Node(Node node) {
    	state = node.state;
    	totalCost = node.totalCost;
    	parent = node.parent;
    	}
    
	//function to add an edge between two nodes
	void addEdge(Node nodeB, int pathCost){
				Edge edge = new Edge(nodeB,pathCost);
				neighbouringNodes.add(edge);
				
	}
	
	void addHeuristicCost(int hCost){
		this.heuristicCost = hCost;
	}
}

 public class shortestPath{
	public static void main(String[] args) throws Exception{
		String algorithmToBeUsed = null;
		int numberOfTrafficLines;
		int heuristicCost; 
		try{
				Node node = new Node();
				Node startNode = new Node();
				Node endNode = new Node();
				Scanner inputFileData = new Scanner(new File("/Users/Aakanksha/Desktop/USC/AI/shortestPathInputs/UCS2.txt"));
			    algorithmToBeUsed = inputFileData.next();
			    startNode = node.getInstance(inputFileData.next());
				endNode = node.getInstance(inputFileData.next());
				numberOfTrafficLines = inputFileData.nextInt();
				inputFileData.nextLine();
				String[] trafficInformation = new String[numberOfTrafficLines];
				while (numberOfTrafficLines>0) {
					node = new Node();
					trafficInformation = inputFileData.nextLine().split(" ");
					node.getInstance(trafficInformation[0]).addEdge(
							node.getInstance(trafficInformation[1]), Integer.valueOf(trafficInformation[2]));
					numberOfTrafficLines--;
				}
				if(algorithmToBeUsed.equals("A*")){
					heuristicCost = inputFileData.nextInt();
					inputFileData.nextLine();
					while (heuristicCost>0) {
						node = new Node();
						trafficInformation = inputFileData.nextLine().split(" ");
						char[] check = trafficInformation[1].toCharArray();
						// for 0 value
						if(Character.isLetter(check[0])){
							node.getInstance(trafficInformation[0]).addHeuristicCost(0);
						}else{
							node.getInstance(trafficInformation[0]).addHeuristicCost(
									Integer.valueOf(trafficInformation[1]));
						}
						heuristicCost--;
					}
				}

//				if(algorithmToBeUsed.equals("BFS"))
//					BFStraversal(startNode, endNode);
//				else if(algorithmToBeUsed.equals("UCS"))
//					UCStraversal(startNode, endNode);
//				else if(algorithmToBeUsed.equals("A*"))
//					AStartraversal(startNode, endNode);
//				else if(algorithmToBeUsed.equals("DFS"))
//					DFStraversal(startNode, endNode);
				
				System.out.println("\nBFS");
				BFStraversal(startNode, endNode);
				System.out.println("\nUCS");
				UCStraversal(startNode, endNode);
				System.out.println("\nA*");
				AStartraversal(startNode, endNode);
				System.out.println("\nDFS");
				DFStraversal(startNode, endNode);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	 // function to find shortest path using Breadth First Search
	public static void BFStraversal(Node source, Node destination) throws IOException{
		    Node poppedNode;
		    Node nodeToBeExplored = new Node();
			if(source.state == destination.state){
				printWithoutCostPath(destination);
			}
			 Set<String> explored = new HashSet<String>();
			 LinkedList<Node> queue = new LinkedList<Node>();
			 queue.add(source);
	        
			 while(queue.size()!=0){
	        	poppedNode = queue.poll();
	        	explored.add(poppedNode.state);
	        	ListIterator<Edge> hasNeightbour = poppedNode.neighbouringNodes.listIterator();
	        	while(hasNeightbour.hasNext()){
	        		Edge nextNode = hasNeightbour.next();
	        		if (nextNode.nodeName.state.equals(destination.state))
	                {
	        			destination.parent = poppedNode;
	        			printWithoutCostPath(destination);
	                	return;
	                }
	                if (!explored.contains(nextNode.nodeName.state))
	                {
	                	explored.add(nextNode.nodeName.state);
	                    nodeToBeExplored = nodeToBeExplored.getInstance(nextNode.nodeName.state);
	                    nodeToBeExplored.parent = poppedNode;
	                    queue.add(nodeToBeExplored);
	                }
	        	}
			 }
			 return;
		}

	// function to find shortest path using Uniform Cost Search
	public static void UCStraversal(Node source, Node destination) throws IOException{
		source.totalCost = 0;
//		PriorityQueue<Node> queue = new PriorityQueue<Node>(Node.getCounter(),new Comparator<Node>() {
//		            public int compare(Node i, Node j) {
//		                if (i.totalCost >= j.totalCost) {return 1;}
//		                else if (i.totalCost < j.totalCost) {return -1;}
//		                else {return 0;}
//		            }});
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(source);
		Set<Node> explored = new HashSet<Node>();
		do {
		    Node current = queue.poll();
		    explored.add(current);
		    if (current.state.equals(destination.state)) {
		    	destination.parent = current.parent;
		    	destination.totalCost = current.totalCost;
		        break;
		    }
		    for (Edge edges : current.neighbouringNodes) {
		        Node child = edges.nodeName;
		        int cost = edges.cost;
		        if (!explored.contains(child) && !queue.contains(child)) {
		        	child.totalCost = current.totalCost + cost;
		            child.parent = current;
		            queue.add(child);
		        }else if (queue.contains(child) || explored.contains(child)) {//instead of deleting - updated the parent
		        	if(child.totalCost > cost + current.totalCost ){
		        		child.parent = current;
		        		child.totalCost = current.totalCost + cost;
		        	}
		        }
		    }
		    queue = sortQueue(queue,"UCS");
		} while (!queue.isEmpty());
		printPath(destination);
}
	
	
	public static Queue<Node> sortQueue (Queue queue, String algo){
		
		Queue<Node> queue1 = new LinkedList<Node>();
		Queue<Node> queue2 = new LinkedList<Node>();

		while (!queue.isEmpty())
			queue1.add((Node) queue.remove());
		
		while (!queue1.isEmpty()) {
			Node q = queue1.remove();
			if(algo.equals("A*")){
				while (!queue2.isEmpty() && q.totalHeuristicCost < queue2.peek().totalHeuristicCost)
					if (q.totalHeuristicCost < queue2.peek().totalHeuristicCost){
						queue1.add(queue2.remove());
					}
				queue2.add(q);
			}else if(algo.equals("UCS")){
				while (!queue2.isEmpty() && q.totalCost < queue2.peek().totalCost)
					if (q.totalCost < queue2.peek().totalCost){
						queue1.add(queue2.remove());
					}
				queue2.add(q);
			}
			}
		return queue2;
	}
	
	
	// function to find shortest path using A* search
	public static void AStartraversal(Node source, Node destination) throws IOException{
			source.totalCost = 0;
//			PriorityQueue<Node> queue = new PriorityQueue<Node>(Node.getCounter(),new Comparator<Node>() {
//			            public int compare(Node i, Node j) {
//			                if (i.totalHeuristicCost > j.totalHeuristicCost) {
//			                	return 1;
//			                	}
//			                else if (i.totalHeuristicCost < j.totalHeuristicCost) {
//			                	return -1;
//			                	}
//			                else {
//			                	return 0;
//			                	}
//			            }});
			
			Queue<Node> queue = new LinkedList<Node>();
			
			queue.add(source);
			Set<Node> explored = new HashSet<Node>();
			do {
			    Node current = queue.poll();
			    explored.add(current);
			    if (current.state.equals(destination.state)) {
			    	if(current.parent == null){
			    		destination.parent = current.parent;
				    	destination.totalCost = current.totalCost;
			    	}
			    	else{
			    		for (Edge edges : current.parent.neighbouringNodes) {
			    			if(edges.nodeName.state == current.state){
			    				destination.totalCost = edges.cost + current.parent.totalCost;
			    			}
			    		}
			    		destination.parent = current.parent;
			    	}
			        break;
			    }
			    for (Edge edges : current.neighbouringNodes) {
			        Node child = edges.nodeName;
			        int cost = edges.cost;
			        if (queue.contains(child) || explored.contains(child)) {
			        	if(child.totalHeuristicCost > cost + current.totalCost + child.heuristicCost){	
			        		child.parent = current;
			        		child.totalCost = current.totalCost + cost;
			        		child.totalHeuristicCost = current.totalCost + cost + child.heuristicCost;
			        		//System.out.println(child.state + " up " + child.parent.state + " " + (cost + current.totalCost + child.heuristicCost));
			        	}
			        } else{
			            child.totalCost = current.totalCost + cost;
			            child.totalHeuristicCost = current.totalCost + cost + child.heuristicCost;
			            child.parent = current;
			           // System.out.println(child.state + " " + child.parent.state + " " + (cost + current.totalCost + child.heuristicCost));
			            queue.add(child);
			        }
			    }
			   queue = sortQueue(queue,"A*");
			} while (!queue.isEmpty());
			printPath(destination);
			}
	
	 // function to find shortest path using Depth First Search
	public static void DFStraversal(Node source, Node destination) throws IOException{
	    Node poppedNode;
	    Node nodeToBeExplored = new Node();
	    source.parent = null;
		if(source.state == destination.state){
			printWithoutCostPath(destination);
			return;
		}
		 Set<String> explored = new HashSet<String>();
		 LinkedList<Node> queue = new LinkedList<Node>();
		 queue.add(source);
        
		 while(queue.size()!=0){
        	poppedNode = queue.poll();
        	if (poppedNode.state.equals(destination.state))
        	{
        		printWithoutCostPath(destination);
        		return;
        	}
        	explored.add(poppedNode.state);
        	Iterator<Edge> hasNeightbour = poppedNode.neighbouringNodes.descendingIterator();
        	while(hasNeightbour.hasNext()){
        		Edge nextNode = hasNeightbour.next();
                if (explored.contains(nextNode.nodeName.state))//ask
                {
//                    nodeToBeExplored = nodeToBeExplored.getInstance(nextNode.nodeName.state);
//                    nodeToBeExplored.parent = poppedNode;
//                    queue.addFirst(nodeToBeExplored);
                }else if(queue.contains(nextNode.nodeName)){
                	
                }else{
                	nodeToBeExplored = nodeToBeExplored.getInstance(nextNode.nodeName.state);
                    nodeToBeExplored.parent = poppedNode;
                    queue.addFirst(nodeToBeExplored);
                }
        	}
		 }
		 return;
	}	
	
	public static void printPath(Node target) throws IOException {
			List<Node> path = new ArrayList<Node>();
			for (Node node = target; node != null; node = node.parent) {
		    path.add(node);
			}
			Collections.reverse(path);
			File outputFile = new File("/Users/Aakanksha/Desktop/USC/AI/shortestPathInputs/a.txt");
			FileWriter fout = new FileWriter(outputFile);
			for(Node node:path){
				fout.write(node.state + " " + node.totalCost + "\n");
				System.out.println(node.state + " " + node.totalCost); //remove
			}
			fout.close();
		}
	
	public static void printWithoutCostPath(Node target) throws IOException {
			List<Node> path = new ArrayList<Node>();
			for (Node node = target; node != null; node = node.parent) {
				path.add(node);
			}
			Collections.reverse(path);
			int i = 0,j=0; //remove
			File outputFile = new File("/Users/Aakanksha/Desktop/USC/AI/shortestPathInputs/b.txt");
			FileWriter fout = new FileWriter(outputFile);
			for(Node node:path){
				fout.write(node.state + " " + i++ + "\n");
	            System.out.println(node.state + " " + j++);	//remove
			}
			fout.close();
		}
 }
