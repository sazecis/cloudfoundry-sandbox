package hu.evosoft.logger;

/**
 * Enum for Performance counters. These are the phases where measurements are done.
 * 
 * @author Csaba.Szegedi
 *
 */
public enum CounterCategory {

	/**
	 * Used to measure how long it took to upload a file.
	 */
	RABBIT_SEND,
	/**
	 * Used to measure how long it took receive and process further to redis the content of the file from the queue. 
	 */
	RABBIT_RECEIVE,
	/**
	 * Used to measure how long it took to read all data from redis plus how long it was to add it mongo.
	 */
	MONGO_ADD,
	/**
	 * Used to measure a MapReduce on the Mongo collections.
	 */
	MONGO_MR,
	
}
