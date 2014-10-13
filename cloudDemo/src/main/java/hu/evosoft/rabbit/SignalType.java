package hu.evosoft.rabbit;

/**
 * Signal types used to control the aggregation from Redis to Mongo.
 * @author Csaba.Szegedi
 *
 */
public enum SignalType {

	BEGIN,
	END,
	CHUNK
	
}
