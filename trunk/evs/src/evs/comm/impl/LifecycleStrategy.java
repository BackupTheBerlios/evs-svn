package evs.comm.impl;

/**
 * Lifecycle Strategy for Remote Objects
 * 
 * @author evs069
 *
 */
public enum LifecycleStrategy {
	
	/** Static Instance */
	STATIC,
	/** Static Instance with Passivation */
	STATIC_PASSIVATION, 
	
	/** Per-Request Instance */
	PER_REQUEST,
	
	/** Client-Dependent Instance (using Leasing) */
	CLIENT,	
	/** Client-Dependent Instance with Passivation */
	CLIENT_PASSIVATION,
	
	/** Lazy Acquisition */
	LAZY,
	
	/** Pooling */
	POOLING
	
}
