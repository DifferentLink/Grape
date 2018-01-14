package edu.kit.ipd.dbis.org.jgrapht.additions.generate;


import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GraphGenerator;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A random graph generator. It generates a random graph which is strongly connected, but does not create self-loops or
 multiple edges between the same two vertices.

 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class RandomConnectedGraphGenerator<V, E> implements GraphGenerator<V, E, V> {



	private final int minVertices;
	private final int maxVertices;
	private final int minEdges;
	private final int maxEdges;


	/**
	 * Construct a new radom connected graph generator.
	 *
	 * @param minVertices the minimal vertices of the graph
	 * @param maxVertices the maximal vertices of the graph
	 * @param minEdges the minimal edges of the graph
	 * @param maxEdges the maximal edges of the graph
	 */
	public RandomConnectedGraphGenerator(int minVertices, int maxVertices, int minEdges, int maxEdges) {
		if (minVertices < 0 || minEdges < 0 || maxVertices < 0 || maxEdges < 0) {
			throw new IllegalArgumentException("No negative values allowed");
		}
		if (minVertices > maxVertices || minEdges > maxEdges) {
			throw new IllegalArgumentException("no valid interval");
		}
		if ((minVertices - 1) > maxEdges) {
			throw new IllegalArgumentException(("The graph cannot be connected"));
		}
		if ((minEdges > (maxVertices * (maxVertices - 1) / 2))) {
			throw new IllegalArgumentException(("To many edges"));
		}

		this.minVertices = minVertices;
		this.maxVertices = maxVertices;
		this.minEdges = minEdges;
		this.maxEdges = maxEdges;
	}
	@Override
	public void generateGraph(Graph<V, E> target, VertexFactory<V> vertexFactory, Map<String, V> resultMap) {
		//TODO: respect number edges

		//generate the random number of edges in the interval the is possible according to the vertex interval
		int rangeE = (maxVertices * (maxVertices - 1) / 2) - (minVertices - 1);
		if (rangeE < 0) {
			throw new IllegalArgumentException("negative range");
		}
		int randomEdges = (this.minVertices - 1) + (int) (Math.random() * (Math.min((rangeE + 1),
				(maxEdges - minEdges + 1))));

		//generate the random number of vertices
		int minN = (int) (0.5 + Math.sqrt(0.25 + 2 * randomEdges)) + 1;
		int rangeV = (randomEdges + 1) - minN;
		if (rangeV < 0) {
			throw new IllegalArgumentException("negative range");
		}
		int randomVertices = minN + (int) (Math.random() * (Math.min((rangeV + 1), (maxVertices - minN + 1))));


		int[] graphArray = new int[randomVertices * (randomVertices - 1) / 2];
		//allocates the array the number 1 or 0 randomly (random graph)
		for (int i = 0; i < graphArray.length; i++) {
			int value = (int) (Math.random() * 20);
			if (value > 9) {
				graphArray[i] = 1;
			} else {
				graphArray[i] = 0;
			}
		}


		System.out.print("First Array: ");
		for (int i = 0; i < graphArray.length; i++) {
			System.out.print(graphArray[i] + ", ");
		}

		//TODO: nicht alles durchgehen denn muss ja immer gewisse anzahl an Kanten haben
		//checks if the graph is connected or has the right number of edges
		while (!((getNumberOfOnes(graphArray) == randomEdges) && isConnected(graphArray))) {
			//add 1 if not connected
			//boolean full = true;
			int i = graphArray.length - 1;
			while (i >= 0 && graphArray[i] == 1) {
				i--;
			}
			if (i < 0) {
				for (int j = 0; j < graphArray.length; j++) {
					graphArray[j] = 0;
				}
			} else {
				graphArray[i] = 1;
				for (int j = i + 1; j < graphArray.length; j++) {
					graphArray[j] = 0;
				}
			}
			System.out.println("");
			System.out.print("Änderung Array: ");
			for (int k = 0; k < graphArray.length; k++) {
				System.out.print(graphArray[k] + ", ");
			}
		}
		System.out.println("");
		System.out.print("End Array: ");
		for (int i = 0; i < graphArray.length; i++) {
			System.out.print(graphArray[i] + ", ");
		}

		// create vertices
		Map<Integer, V> vertices = new LinkedHashMap<>(randomVertices);
		for (int i = 1; i <= randomVertices; i++) {
			V v = vertexFactory.createVertex();
			target.addVertex(v);
			vertices.put(i, v);
		}
		//add edges
		for (int i = 1; i < randomVertices; i++) {
			for (int j = i + 1; j <= randomVertices; j++) {
				if (graphArray[getNumberInArray(i, j, randomVertices)] == 1) {
					target.addEdge(vertices.get(i), vertices.get(j));
				}
			}
		}
		System.out.println("rangeV: " + rangeV + "  rangeE: " + rangeE);
		System.out.println("Knoten: " + randomVertices + "  Kanten: " + randomEdges);
		System.out.print(minN);
	}

	private int getNumberOfOnes(int[] array) {
		int count = 0;
		for (int i : array) {
			if (array[i] == 1) {
				count++;
			}
		}
		return count;
	}

	/**
	 *Checks, if the graph represented as array is connected.
	 * The method build the reachable array and checks in the end if the reachable array contains a zero.
	 * If the reachable
	 * array contains no zeros, the given graph is connected.
	 *
	 * @param graph the input graph array
	 * @return if the graph is connected
	 */
	private boolean isConnected(int[] graph) {
		//TODO: implement me

		//computes reachable array
		int n = (int) (0.5 + Math.sqrt(0.25 + 2 * graph.length) + 0.1);
		int[] reachableArray = graph.clone();
		int[] newExp = reachableArray.clone();

		int k = 1;
		boolean connected = false;
		while (k <= graph.length + 1 && !connected) {
			int[] result = new int[graph.length];
			for (int i = 1; i <= n; i++) {
				for (int j = i + 1; j <= n; j++) {
					result[getNumberInArray(i, j, n)] = getMultValue(i, j, n, newExp, graph);
				}
			}
			newExp = result;
			//reachableArray = reachableArray + expArray
			for (int j = 0; j < graph.length; j++) {
				reachableArray[j] += newExp[j];
			}
			//checks if the calculation already results that the graph is connected
			int nrOfZero = 0;
			for (int i : reachableArray) {
				if (i == 0) {
					nrOfZero++;
				}
			}
			if (nrOfZero == 0) {
				connected = true;
			}

			k++;
		}


		System.out.println("");
		System.out.print("Reachable Array: ");
		for (int h = 0; h < reachableArray.length; h++) {
			System.out.print(reachableArray[h] + ", ");
		}
		System.out.println("");

		for (int i : reachableArray) {
			if (i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Computes the place in the array of the place in the matrix
	 *
	 * @param i the column count
	 * @param j the row count
	 * @param n the number of vertices
	 * @return the array index
	 */
	private int getNumberInArray(int i, int j, int n) {
		int result = 0;
		for (int k = 1; k <= i - 1; k++) {
			result += (n - k);
		}
		return result + j - i - 1;
	}

	/**
	 * computes the matrix multiplication value in (i, j) from the multiplication of a and b.
	 *
	 * @param i the column number
	 * @param j the row number
	 * @param n the matix size
	 * @param a the first matrix (array)
	 * @param b the second matrix (array)
	 * @return the multiplication value
	 */
	public int getMultValue(int i, int j, int n, int[] a, int[] b) {
		int result = 0;
		for (int k = 1; k <= n; k++) {
			if ((i < k) && (j < k)) {
				result += (a[getNumberInArray(i, k, n)] * b[getNumberInArray(j, k, n)]);

			} else if ((i < k) && (k < j)) {
				result += (a[getNumberInArray(i, k, n)] * b[getNumberInArray(k, j, n)]);
			} else if ((k < i) && (k < j)) {
				result += (a[getNumberInArray(k, i, n)] * b[getNumberInArray(k, j, n)]);
			} else if ((k < i) && (j < k)) {
				result += (a[getNumberInArray(k, i, n)] * b[getNumberInArray(j, k, n)]);
			}
		}
		return result;
	}
}
