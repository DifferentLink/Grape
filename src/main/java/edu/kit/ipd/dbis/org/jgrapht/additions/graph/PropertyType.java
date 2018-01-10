package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.KkGraphAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.List;
import java.util.Set;

public enum PropertyType implements PropertyCalculator {
	BFSCODE {
		@Override
		public int[] calculate(PropertyGraph graph) {
			return null;
		}
	},
	NUMVERTICES {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return null;
		}
	},
	NUMEDGES {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return null;
		}
	},
	MINDEGREE {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return null;
		}
	},
	MAXDEGREE {
		@Override
		public Integer calculate(PropertyGraph graph) {
			return null;
		}
	},
	AVERAGEDEGREE {
		@Override
		public Double calculate(PropertyGraph graph) {
			return null;
		}
	},
	TOTALCOLORINGLIST {
		@Override
		public List<TotalColoringAlgorithm.TotalColoring> calculate(PropertyGraph graph) {
			return null;
		}
	},
	VERTEXCOLORINGLIST {
		@Override
		public List<VertexColoringAlgorithm.Coloring> calculate(PropertyGraph graph) {
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
	PROFILE {
		@Override
		public int[][] calculate(PropertyGraph graph) {
			return null;
		}
	},
	MAXCLIQUES {
		// as enums cannot have type parameters such as <V> (the vertex type),
		// Object is used here.
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
	}
}
