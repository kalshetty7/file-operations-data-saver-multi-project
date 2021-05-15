package file.operations;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Filters {
	private boolean isExactMatch, isCaseSensitive, onlyFiles, onlyFolders, onlyEmptyFolders;
	private List<String> extensions;
	private List<String> names;
	private File inputFile;
	private String nameString, extensionString;

	public static List<String> splitToList(String items) {
		List<String> itemList = new ArrayList<String>();
		if (items != null) {
			String[] tokens = items.split(",");
			if (tokens != null && tokens.length > 0)
				for (String token : tokens)
					itemList.add(token);
		}
		return itemList;
	}

	private String getInputFileExtension() {
		String name = inputFile.getName();
		String extension = "";
		if (name.contains(".")) {
			String tokens[] = name.split("\\.");
			extension = tokens[tokens.length - 1];
		}
		return extension;
	}

	private boolean isPresent(List<String> names, String nameToCheck) {
		if (names != null && names.size() > 0) {
			for (String n : names) {
				if (isCaseSensitive) {
					if (isExactMatch) {
						if (n.equals(nameToCheck))
							return true;
					} else {
						if (nameToCheck.contains(n))
							return true;
					}
				} else {
					if (isExactMatch) {
						if (n.equalsIgnoreCase(nameToCheck))
							return true;
					} else {
						if (nameToCheck.toLowerCase().contains(n.toLowerCase()))
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isEmptyFolder() {
		if (inputFile.isDirectory()) {
			File files[] = inputFile.listFiles();
			if (files == null || files.length == 0)
				return true;
		}
		return false;
	}

	public boolean predicate() {
		if (isNotBlank(nameString) && isEmpty(names))
			names = splitToList(nameString);
		if (isNotBlank(extensionString) && isEmpty(extensions))
			extensions = splitToList(extensionString);
		if (onlyEmptyFolders)
			return isEmptyFolder();
		if (onlyFiles) {
			if (inputFile.isFile()) {
				if (isNotEmpty(names) || isNotEmpty(extensions)) {
					return (isPresent(extensions, getInputFileExtension()) || isPresent(names, inputFile.getName()));
				} else
					return true;
			}
		}
		if (onlyFolders) {
			if (inputFile.isDirectory()) {
				if (isNotEmpty(names)) {
					return (isPresent(names, inputFile.getName()));
				} else
					return true;
			}
		}
		return false;
	}
}
