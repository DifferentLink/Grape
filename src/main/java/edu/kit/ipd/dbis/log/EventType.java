package edu.kit.ipd.dbis.log;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.TablesNotAsExpectedException;

import java.util.Set;

public enum EventType {
	ADD {
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
			for (int id : changedGraphs) {
				database.deleteGraph(id);
			}
		}

		public void redo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
			for (int id : changedGraphs) {
				database.restoreGraph(id);
			}
		}
	},
	REMOVE {
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
			for (int id : changedGraphs) {
				database.restoreGraph(id);
			}
		}

		public void redo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException {
			for (int id : changedGraphs) {
				database.deleteGraph(id);
			}
		}
	},
	MESSAGE {
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) {

		}

		public void redo(GraphDatabase database, Set<Integer> changedGraphs) {

		}
	};

	abstract void undo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException;

	abstract void redo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException;
}
