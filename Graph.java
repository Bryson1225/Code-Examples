
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Graph {
	
	public class Vertex {
		private int inDegree;
		private int degree;
		private int outDegree;
		private List<Integer> edges;
		private int name;
		
		public Vertex(int value) {
			this.name = value;
			this.edges = new ArrayList<Integer>();
			for (int i = 0; i < numVertices; i++) {
				this.edges.add(-1);
			}
			this.inDegree = 0;
			this.degree = 0;
			this.outDegree = 0;
		}
	}
	
	private HashMap<Integer, Vertex> vertexMap;
	private HashMap<Integer, ArrayList<Integer>> map;
	private List<List<Integer>> adjencyList;
	private boolean isDirected;
	private int numVertices;
	private int numEdges;
	private boolean selfLoop = false;
	
	public Graph(int n, boolean directed) {
		this.numVertices = n;
		this.numEdges = 0;
		this.isDirected = directed;
		this.map = new HashMap<Integer, ArrayList<Integer>>();
		this.adjencyList = new ArrayList<List<Integer>>(this.numVertices);
		buildAdjList();
		this.vertexMap = new HashMap<Integer, Vertex>();
	}
	
	public void buildAdjList() {
		List<Integer> filler = new ArrayList<Integer>(this.numVertices);
		for (int i  = 0; i < this.numVertices; i++) {
			filler.add(0);
		}
		for (int i = 0; i < this.numVertices; i++) {
			this.adjencyList.add(new ArrayList<Integer>(filler));
		}
	}
	
	public Graph(String fn) throws FileNotFoundException {
		this.map = new HashMap<Integer, ArrayList<Integer>>();
		this.vertexMap = new HashMap<Integer, Vertex>();
		
		File myObj = new File(fn);
		Scanner myReader = new Scanner(myObj);
		boolean firstLine = true;
		String[] lineHold;
		while (myReader.hasNextLine()) {
			String line = myReader.nextLine();
			if (firstLine) {
				firstLine = false;
				lineHold = line.split(" ");
				this.numVertices = Integer.parseInt(lineHold[1]);
				this.adjencyList = new ArrayList<List<Integer>>(this.numVertices);
				buildAdjList();
				if (lineHold[0].equals("u")) {
					this.isDirected = false;
				} else {
					this.isDirected = true;
				}
			} else {
				lineHold = line.split(" ");
				int one = Integer.parseInt(lineHold[0]);
				int two = Integer.parseInt(lineHold[1]);
				if (one == two) {
					this.selfLoop = true;
				}
				if (this.isDirected) {
					graphDirectedHelper(one, two);
					changeMap(one, two);
					buildAdjancyMatrix(one, two);
				} else {
					graphUndirectedHelper(one, two);
					changeMap(one, two);
					buildAdjancyMatrix(one, two);
				}
			}
		}
		myReader.close();
	}
	
	
	private void buildAdjancyMatrix(int vertexOne, int vertexTwo) {
		(this.adjencyList.get(vertexOne)).set(vertexTwo, 1);;
	}
	
	
	private void changeMap(int vertexOne, int vertexTwo) {
		if (this.isDirected) {
			if (this.map.containsKey(vertexOne)) {
				this.map.get(vertexOne).add(vertexTwo);
			} else {
				ArrayList<Integer> forMap = new ArrayList<Integer>();
				forMap.add(vertexTwo);
				this.map.put(vertexOne, forMap);
			}
		} else {
			if (this.map.containsKey(vertexOne)) {
				this.map.get(vertexOne).add(vertexTwo);
			} else {
				ArrayList<Integer> forMap = new ArrayList<Integer>();
				forMap.add(vertexTwo);
				this.map.put(vertexOne, forMap);
			} if (this.map.containsKey(vertexTwo)) {
				this.map.get(vertexTwo).add(vertexOne);
			} else {
				ArrayList<Integer> forMap = new ArrayList<Integer>();
				forMap.add(vertexOne);
				this.map.put(vertexTwo, forMap);
			}
		}
	}
	
	private void graphDirectedHelper(int vertexOne, int vertexTwo) {
		if (this.vertexMap.containsKey(vertexOne)) {
			Vertex one = this.vertexMap.get(vertexOne);
			if (one.edges.get(vertexTwo) == null) {
				one.edges.add(vertexTwo, vertexTwo);
				one.outDegree++;
			}
		} else {
			Vertex one = new Vertex(vertexOne);
			this.vertexMap.put(vertexOne, one);
			one.edges.add(vertexTwo, vertexTwo);
			one.outDegree++;
		} if (this.vertexMap.containsKey(vertexTwo)) {
			Vertex two = this.vertexMap.get(vertexTwo);
			two.inDegree++;
		} else {
			Vertex two = new Vertex(vertexTwo);
			this.vertexMap.put(vertexTwo, two);
			two.inDegree++;
		}
	}
	private void graphUndirectedHelper(int vertexOne, int vertexTwo) {
		if (this.vertexMap.containsKey(vertexOne)) {
			Vertex one = this.vertexMap.get(vertexOne);
			if (this.vertexMap.containsKey(vertexTwo)) {
				Vertex two = this.vertexMap.get(vertexTwo);
				if (!one.edges.contains(vertexTwo)) {
					one.edges.add(vertexTwo);
					two.edges.add(vertexOne);
					one.degree++;
					two.degree++;
					this.numEdges++;
				}
			} else {
				Vertex two = new Vertex(vertexTwo);
				this.vertexMap.put(vertexTwo, two);
				one.edges.add(vertexTwo);
				two.edges.add(vertexOne);
				one.degree++;
				two.degree++;
				this.numEdges++;
			}
		} else {
			Vertex one = new Vertex(vertexOne);
			this.vertexMap.put(vertexOne, one);
			if (this.vertexMap.containsKey(vertexTwo)) {
				Vertex two = this.vertexMap.get(vertexTwo);
				if (!two.edges.contains(vertexOne)) {
					one.edges.add(vertexTwo);
					two.edges.add(vertexOne);
					one.degree++;
					two.degree++;
					this.numEdges++;
				}
			} else {
				Vertex two = new Vertex(vertexTwo);
				this.vertexMap.put(vertexTwo, two);
				two.edges.add(vertexOne);
				one.edges.add(vertexOne);
				two.degree++;
				one.degree++;
				this.numEdges++;
			}
		}
	}
	
	public int degree(int v) {
		if (this.isDirected) {
			return 0;
		} else {
			if (this.vertexMap.containsKey(v)) {
				Vertex V = this.vertexMap.get(v);
				return V.degree;
			}
		}
		return 0;
	}
	
	public int indegree(int v) {
		if (this.isDirected) {
			return (this.vertexMap.get(v).inDegree);
		} else {
			return 0;
		}
	}
	
	public int outdegree(int v) {
		if (this.isDirected) {
			return (this.vertexMap.get(v).outDegree);
		} else {
			return 0;
		}
	}
	
	public int V() {
		return this.numVertices;
	}
	
	public int E() {
		return this.numEdges;
	}
	
	public void addEdge(int v, int w) {
		if (v == w) {
			this.selfLoop = true;
		}
		if (this.isDirected) {
			graphDirectedHelper(v, w);
			changeMap(v, w);
			buildAdjancyMatrix(v, w);
		} else {
			graphUndirectedHelper(v, w);
			changeMap(v, w);
			buildAdjancyMatrix(v, w);
		}
	}
	
	public boolean adjTo(int v, int w) {
		if (this.vertexMap.containsKey(v)) {
			Vertex V = this.vertexMap.get(v);
			if (V.edges.contains(w)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean isSimple() {
		return this.selfLoop;
	}
	
	public HashMap<Integer, ArrayList<Integer>> components() {
		HashMap<Integer, ArrayList<Integer>> toReturn = new HashMap<Integer, ArrayList<Integer>>();
		boolean[] visited = new boolean[this.numVertices];
		
		for (int i = 0; i < this.numVertices; i++) {
			for (int x : this.map.get(i)) {
				if(!visited[x]) {
					compHelper(i, x, visited, toReturn);
				}
			}
		}
		return toReturn;
	}
	
	public int numComponents() {
		HashMap<Integer, ArrayList<Integer>> map = this.components();
		return map.size();
	}
	
	private void compHelper(int v, int x, boolean[] visited, HashMap<Integer, ArrayList<Integer>> toReturn) {
		visited[v] = true;
		if (!toReturn.containsKey(v)) {
			toReturn.put(v, new ArrayList<Integer>(this.numVertices));
			toReturn.get(v).add(v);
		} if (!visited[x]) {
			visited[x] = true;
			toReturn.get(v).add(x);
		}
		for (int y : this.map.get(x)) {
			if (!visited[y]) {
				visited[y] = true;
				toReturn.get(v).add(y);
				compHelper(v, y, visited, toReturn);
			}
		}
	}
	
	public boolean isBiconnected() {
		if (this.isDirected) {
			return false;
		} else {
			boolean visited[] = new boolean[this.numVertices];
			int disc[] = new int[this.numVertices];
			int low[] = new int[this.numVertices];
			int parent[] = new int[this.numVertices];
			
			for (int i = 0; i < this.numVertices; i++) {
				parent[i] = -1;
				visited[i] = false;
			}
			
			if (biHelper(0, visited, disc, low, parent, 0) == true) {
				return false;
			}
			for(int i = 0; i < this.numVertices; i++) {
				if (visited[i] == false) {
					return false;
				}
			}
			return true;
		}
	}
	
	private boolean biHelper(int x, boolean visited[], int disc[], int low[], int parent[], int tracker) {
		int children = 0;
		visited[x] = true;
		disc[x] = low[x] = ++tracker;
		for (int y : this.map.get(x)) {
			if(!visited[y]) {
				children++;
				parent[y] = x;
				if (biHelper(y, visited, disc, low, parent, tracker)) {
					return true;
				}
				low[x] = Math.min(low[x], low[y]);
				if(parent[x] == -1 && children  > 1) {
					return true;
				}
				if (parent[x] != -1 && low[y] >= disc[x]) {
					return true;
				}
			} else if (y != parent[x]) {
				low[x] = Math.min(low[x], disc[y]);
			}
		}
		return false;
	}
	
	private void articulationHelper(int v, boolean visited[], int disc[], int low[], int parent, boolean AP[], int tracker) {
		int children = 0;
		visited[v] = true;
		disc[v] = low[v] = ++tracker;
		for(Integer x : this.map.get(v)) {
			if (!visited[x]) {
				children++;
				articulationHelper(x, visited, disc, low, v, AP, tracker);
				
				low[v] = Math.min(low[v], low[x]);
				if(parent!= -1 && low[x] >= disc[v]) {
					AP[v] = true;
				}
			} else if(x != parent) {
				low[v] = Math.min(low[v], disc[x]);
			}
		}
		if (parent == -1 && children > 1) {
			AP[v] = true;
		}
	}
	
	public ArrayList<Integer> articulationVertices() {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		if (this.isDirected) {
			return null;
		} else {
			boolean visited[] = new boolean[this.numVertices];
			int disc[] = new int[this.numVertices];
			int low[] = new int[this.numVertices];
			boolean[] articulationP = new boolean[this.numVertices];
			int tracker = 0;
			int par = -1;
			
			for (int i = 0; i < this.numVertices; i++) {
				if(visited[i] == false) {
					articulationHelper(i, visited, disc, low, par, articulationP, tracker);
				}
			}
			for (int i = 0; i < this.numVertices; i++) {
				if (articulationP[i] == true) {
					toReturn.add(i);
				}
			}
		}
		return toReturn;
	}
	
	private void topSortHelper(int v, boolean visited[], Stack<Integer> stack) {
		visited[v] = true;
		for (int x : this.map.get(v)) {
			if(!visited[x]) {
				topSortHelper(x, visited, stack);
			}
		}
		stack.push(v);
	}
	
	public ArrayList<Integer> topSort() {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[this.numVertices];
		for (int i = 0; i < this.numVertices; i++) {
			visited[i] = false;
		}
		for(int i = 0; i < this.numVertices; i++) {
			if(visited[i] == false) {
				topSortHelper(i, visited, stack);
			}
		} while (stack.empty() == false) {
			toReturn.add(stack.pop());
		}
		return toReturn;
	}
	
	public Graph spanningTree() {
		return null;
	}
	
	public boolean isAcyclic() {
		return true;
	}
	
	public ArrayList<Integer> path(int v, int w) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		return toReturn;
	}
	
	public ArrayList<Integer> cycle(int v) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		return toReturn;
	}
	
	public boolean isConnected() {
		return true;
	}
	
	public boolean isStronglyConnected() {
		return true;
	}
	
	public boolean isArticulation(int v) {
		return true;
	}
	
	public boolean isBridge(int v, int w) {
		return true;
	}
	
	public ArrayList<Integer> dfsOrder(int v) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		return toReturn;
	}
	
	public ArrayList<Integer> bfsOrder(int v) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(this.numVertices);
		return toReturn;
	}
	
	public boolean equals(Graph G) {
		return true;
	}
	
	public Graph transitiveClosure() {
		return null;
	}
	
	public void printAdjList() {
		for (int i = 0; i < this.numVertices; i++) {
			for (int j = 0; j < this.numVertices; j++) {
				System.out.print(" " + this.adjencyList.get(i).get(j));
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String file = "src/graph1.txt";
		Graph test = new Graph(file);
		test.addEdge(0, 0);
		test.printAdjList();
		HashMap<Integer, ArrayList<Integer>> testing = test.components();
		System.out.println((test.topSort()).toString());
	}
	
}