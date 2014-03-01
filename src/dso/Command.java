package dso;

public enum Command {
    OK,
    LOCK,
    UNLOCK,
    SYNC,
    JOIN,
    LEAVE,
    ERROR,
    READY,
    UNKNOWN,
    WAIT,
    NULL;

    public static Command detect(String string) {
		if (null == string) {
			return NULL;
		}
		try {
			return valueOf(string);
		} catch (Exception e) {
		}
		return UNKNOWN;
    }
}