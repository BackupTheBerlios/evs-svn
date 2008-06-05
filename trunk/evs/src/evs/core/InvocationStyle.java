package evs.core;

public enum InvocationStyle {
	
	SYNC(0),
	FIRE_FORGET(1),
	POLL_OBJECT(2),
	RESULT_CALLBACK(3);
	
	private final int value;
	
	InvocationStyle(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static InvocationStyle fromInt(int value) {
		if (value == SYNC.value) return SYNC;
		if (value == FIRE_FORGET.value) return FIRE_FORGET;
		if (value == POLL_OBJECT.value) return POLL_OBJECT;
		if (value == RESULT_CALLBACK.value) return RESULT_CALLBACK;
		return null;
	}
	
}
