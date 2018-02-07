package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.NextDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import org.jgrapht.alg.ConnectivityInspector;

import java.util.Objects;

/**
 The next denser graph algorithm.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class NextDenserGraphFinder<V, E> implements NextDensityAlgorithm {

	/**
	 * The input graph
	 */
	protected final PropertyGraph<V, E> graph;

	/**
	 * Construct a new next denser graph finder.
	 *
	 * @param graph the input graph
	 */
	public NextDenserGraphFinder(PropertyGraph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	@Override
	public PropertyGraph getNextDenserGraph() throws NoDenserGraphException {
		if (graph.vertexSet().size() == 0) {
			throw new NoDenserGraphException("empty graph");
		}
		if (graph.vertexSet().size() == 1 || graph.vertexSet().size() == 2) {
			throw new NoDenserGraphException("no denser graph exists 1");
		}

		BfsCodeAlgorithm.BfsCodeImpl bfscode =
				(BfsCodeAlgorithm.BfsCodeImpl) graph.getProperty(BfsCode.class).getValue();
		int[] codeArray = bfscode.getCode();

		//checks if there is any denser graph with this number of vertices (number vertices >= 2)
		int cnt = 1;
		int a = 1;
		int b = 2;
		int arrayCnt = 1;
		boolean denserGraphExist = false;
		int numberOfEdgesForDensestGraph = (graph.vertexSet().size() - 2) * (graph.vertexSet().size() - 1) / 2 + 1;
		if (graph.edgeSet().size() > numberOfEdgesForDensestGraph) {
			denserGraphExist = true;
		}
		while (cnt < graph.edgeSet().size() && !denserGraphExist && !((a == 2) && (b == graph.vertexSet().size()))) {
			if (!(codeArray[arrayCnt] == a && codeArray[arrayCnt + 1] == b)) {
				denserGraphExist = true;
			} else {
				if (a < b - 1) {
					a = a + 1;
				} else {
					a = 1;
					b++;
				}
				arrayCnt += 3;
			}
		}
		if (!denserGraphExist) {
			throw new NoDenserGraphException("no denser graph exists 2");
		}

		//calculates next denser bfs code and checks if this code is a minimal bfscode and ,if the graph with this next
		// code is connected and has the same number of vertices
		int[] nextDenserBfsCode = {};
		boolean graphIsConnected = false;
		boolean isMinimalCode = false;

		while(!(graphIsConnected && isMinimalCode) || (codeArray[codeArray.length - 1] != graph.vertexSet().size())) {
			graphIsConnected = false;
			isMinimalCode = false;

			nextDenserBfsCode = this.getNextDenserCode(codeArray, graph.vertexSet().size());
			PropertyGraph nextDenserGraph = new PropertyGraph(new BfsCodeAlgorithm.BfsCodeImpl(nextDenserBfsCode));
			ConnectivityInspector in = new ConnectivityInspector(nextDenserGraph);
			if (in.isGraphConnected()) {
				graphIsConnected = true;
				int[] nextDenserGraphMinimalBfsCode = ((BfsCodeAlgorithm.BfsCodeImpl)
						nextDenserGraph.getProperty(BfsCode.class).getValue()).getCode();

				int[] absolutNextDenserMinimalCodeArray = new int[nextDenserGraphMinimalBfsCode.length];
				for (int i = 0; i < absolutNextDenserMinimalCodeArray.length; i++) {
					absolutNextDenserMinimalCodeArray[i] = Math.abs(nextDenserGraphMinimalBfsCode[i]);
				}

				//checks if the nextDenser bfs codee is a minimal code
				BfsCodeAlgorithm.BfsCodeImpl absMinCode =
						new BfsCodeAlgorithm.BfsCodeImpl(absolutNextDenserMinimalCodeArray);

				int[] absNextDenserBfsCode = new int[nextDenserBfsCode.length];
				for (int i = 0; i < absNextDenserBfsCode.length; i++) {
					absNextDenserBfsCode[i] = Math.abs(nextDenserBfsCode[i]);
				}
				BfsCodeAlgorithm.BfsCodeImpl absNextCode = new BfsCodeAlgorithm.BfsCodeImpl(absNextDenserBfsCode);

				if (absMinCode.compareTo(absNextCode) == 0) {
					isMinimalCode = true;
				}
			}
			codeArray = nextDenserBfsCode;
		}
		return new PropertyGraph(new BfsCodeAlgorithm.BfsCodeImpl(nextDenserBfsCode));
	}

	/**
	 * calculates the next denser bfs code without checking if the generating graph is connected
	 * calculated bfs code must not be a minimal bfs code
	 *
	 * @param code the start code
	 * @return the next denser code from code
	 */
	private int[] getNextDenserCode(int[] code, int numberOfNodes) {
		int[] nextCode;
		int[] lastEdge = {code[code.length - 2], code[code.length - 1]};
		int[] prevLastEdge = {code[code.length - 5], code[code.length - 4]};

		if ((lastEdge[1] == prevLastEdge[1] && lastEdge[0] == (prevLastEdge[0] + 1))
				|| (lastEdge[0] == 1 && (prevLastEdge[1] == lastEdge[1] - 1 && prevLastEdge[0] == prevLastEdge[1] - 1))) {
			//...113-123 -> ...113 oder ...123114 -> ...123
			nextCode = new int[code.length - 3];
			for (int i = 0; i < nextCode.length; i++) {
				nextCode[i] = code[i];
			}
		} else {
			nextCode = new int[code.length + 3];
			for (int i = 0; i < code.length - 3; i++) {
				nextCode[i] = code[i];
			}
			//decrease last edge (f.e from 3,5 to 2,5 or from 1,4 to 2,3)
			if (lastEdge[0] != 1) {
				nextCode[nextCode.length - 6] = 1;
				nextCode[nextCode.length - 5] = lastEdge[0] - 1;
				nextCode[nextCode.length - 4] = lastEdge[1];
			} else {
				nextCode[nextCode.length - 6] = 1;
				nextCode[nextCode.length - 5] = lastEdge[1] - 2;
				nextCode[nextCode.length - 4] = lastEdge[1] - 1;
			}
			//add biggest edge (f.e graph with 5 nodes -> edge 4,5)
			nextCode[nextCode.length - 3] = 1;
			nextCode[nextCode.length - 2] = numberOfNodes - 1;
			nextCode[nextCode.length - 1] = numberOfNodes;
		}
		return nextCode;
	}

}
