package log;

public class Logger {
	public static void Info(Object text) {
		System.out.println(text);
	}
	public static void Warn(Object text) {
		System.out.println(text);
	}
	public static void Error(Object text) {
		System.err.println(text);
	}
}
