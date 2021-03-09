package com.file.manager.ui.ctrls;

import java.util.LinkedHashMap;
import java.util.Map;

public enum SearchType {
	files, folders, file_extensions, empty_folders;

	public static Map<String, SearchType> valueEnumMap() {
		int length = SearchType.values().length;
		String values[] = new String[length];
		Map<String, SearchType> mp = new LinkedHashMap<>();
		for (int i = 0; i < length; i++) {
			values[i] = SearchType.values()[i].name();
			values[i] = capitalize(values[i].replaceAll("_", " "));
			mp.put(values[i], SearchType.values()[i]);
		}
		return mp;
	}

	private static String capitalize(String c) {
		String s = "";
		for (int i = 0; i < c.length(); i++) {
			String t = c.charAt(i) + "".toLowerCase();
			if (i == 0)
				t = t.toUpperCase();
			if (i > 0 && (s.charAt(i - 1) + "").equals(" "))
				t = t.toUpperCase();
			s += t;
		}
		return s;
	}
}
