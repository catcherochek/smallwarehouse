package model.tools;

public class staticTools {
	public static String strObfusc(String str) {
		String key  = str.replaceAll("\\s", "");
		key = key.toLowerCase();
		return key;
	}
}
