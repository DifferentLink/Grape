package edu.kit.ipd.dbis.log;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.AccessDeniedForUserException;
import edu.kit.ipd.dbis.database.exceptions.sql.DatabaseDoesNotExistException;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;

import java.util.Set;

/**
 * The enum Event type.
 */
public enum EventType {
	/**
	 * The Add.
	 */
	ADD {
		@Override
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) throws ConnectionFailedException {
			for (int id : changedGraphs) {
				database.deleteGraph(id);
			}
		}

		@Override
		public void redo(GraphDatabase database, Set<Integer> changedGraphs) throws ConnectionFailedException {
			for (int id : changedGraphs) {
				database.restoreGraph(id);
			}
		}
	},
	/**
	 * The Remove.
	 */
	REMOVE {
		@Override
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) throws ConnectionFailedException {
			for (int id : changedGraphs) {
				database.restoreGraph(id);
			}
		}

		@Override
		public void redo(GraphDatabase database, Set<Integer> changedGraphs) throws ConnectionFailedException {
			for (int id : changedGraphs) {
				database.deleteGraph(id);
			}
		}
	},
	/**
	 * The Message.
	 */
	MESSAGE {
		@Override
		public void undo(GraphDatabase database, Set<Integer> changedGraphs) {

		}

		@Override
		public void redo(GraphDatabase database, Set<Integer> changedGraphs) {

		}
	};

	/**
	 * Undo.
	 *
	 * @param database      the database
	 * @param changedGraphs the changed graphs
	 * @throws DatabaseDoesNotExistException the database does not exist exception
	 * @throws AccessDeniedForUserException  the access denied for user exception
	 * @throws ConnectionFailedException     the connection failed exception
	 */
	abstract void undo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException,
			AccessDeniedForUserException, ConnectionFailedException;

	/**
	 * Redo.
	 *
	 * @param database      the database
	 * @param changedGraphs the changed graphs
	 * @throws DatabaseDoesNotExistException the database does not exist exception
	 * @throws AccessDeniedForUserException  the access denied for user exception
	 * @throws ConnectionFailedException     the connection failed exception
	 */
	abstract void redo(GraphDatabase database, Set<Integer> changedGraphs) throws DatabaseDoesNotExistException,
			AccessDeniedForUserException, ConnectionFailedException;
}
