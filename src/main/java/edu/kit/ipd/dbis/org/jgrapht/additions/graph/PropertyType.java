package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.AverageDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.MaxDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.degree.MinDegreeFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.MinimalBfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.MinimalProfileAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.ProfileDensityAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.List;
import java.util.Set;

/**
 * This enum represents the different types of properties
 * and defines how they are calculated.
 */
public enum PropertyType {
	/**
	 * Calculates a graph's BFS-Code.
	 */
	BFSCODE {
		// TODO: differs from design as it returns a BfsCode-object
		@Override
		public BfsCodeAlgorithm.BfsCode calculate(PropertyGraph graph) {
			MinimalBfsCodeAlgorithm alg = new MinimalBfsCodeAlgorithm(graph);
			return alg.getBfsCode();
		}
	},

	/**
	 * Calculates the number of vertices in a graph.
	 */
	NUMVERTICES {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return graph.vertexSet().size();
		}
	},

	/**
	 * Calculates the number of edges in a graph.
	 */
	NUMEDGES {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return graph.edgeSet().size();
		}
	},

	/**
	 * Calculates the smallest degree of all nodes
	 * in a graph.
	 */
	MINDEGREE {
		@Override
		public Integer calculate(PropertyGraph graph) {
			MinDegreeFinder finder = new MinDegreeFinder(graph);
			return finder.getDegree();
		}
	},

	/**
	 * Calculates the greatest degree of all nodes
	 * in a graph.
	 */
	MAXDEGREE {
		@Override
		public Integer calculate(PropertyGraph graph) {
			MaxDegreeFinder finder = new MaxDegreeFinder(graph);
			return finder.getDegree();
		}
	},

	/**
	 * Calculates the average degree of all nodes
	 * in a graph.
	 */
	AVERAGEDEGREE {
		@Override
		public Double calculate(PropertyGraph graph) {
			AverageDegreeFinder finder = new AverageDegreeFinder(graph);
			return finder.getDegree();
		}
	},

	/**
	 * Calculates a List of TotalColoring objects.
	 */
	TOTALCOLORINGLIST {
		@Override
		public List<TotalColoringAlgorithm.TotalColoring> calculate(PropertyGraph graph) {
			//TODO: need method calculateAllColorings()
			return null;
		}
	},

	/**
	 * Calculates a List of Coloring objects, which are
	 * vertex colorings.
	 */
	VERTEXCOLORINGLIST {
		@Override
		public List<VertexColoringAlgorithm.Coloring> calculate(PropertyGraph graph) {
			//TODO: need method calculateAllColorings()
			return null;
		}
	},

	PROPORTIONDENSITY {
		@Override
		public Double calculate(PropertyGraph graph) {
			return null;
		}
	},

	STRUCTUREDENSITY {
		@Override
		public Double calculate(PropertyGraph graph) {
			return null;
		}
	},

	NODESPECIFICDENSITY {
		@Override
		public Double calculate(PropertyGraph graph) {
			return null;
		}
	},

	/**
	 * Calculates a graph's Profile.
	 */
	PROFILE {
		// TODO: differs from design as it returns Profile object
		@Override
		public ProfileDensityAlgorithm.Profile calculate(PropertyGraph graph) {
			MinimalProfileAlgorithm alg = new MinimalProfileAlgorithm(graph);
			return alg.getProfile();
		}
	},
	MAXCLIQUES {
		@Override
		public List<Set<Object>> calculate(PropertyGraph graph) {
			return null;
		}
	},
	KKGRAPH {
		@Override
		public KkGraphAlgorithm.KkGraph calculate(PropertyGraph graph) {
			return null;
		}
	};

	/**
	 *
	 * @param graph
	 * @return
	 */
	abstract Object calculate(PropertyGraph graph);
}
