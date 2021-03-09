package com.file.manager.ui.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.file.manager.ui.ctrls.SearchType;
import com.file.manager.ui.entities.SearchCriteria;

import file.operations.FileUtil;
import file.operations.Filters;

public class Utils {

	public static String defaultWorkingDir() {
		return System.getProperty("user.dir");
	}

	public static Filters getFilters(SearchCriteria sc) {
		Filters.FiltersBuilder builder = Filters.builder();
		if (isNotBlank(sc.getFileExtensions()))
			builder.extensionString(sc.getFileExtensions());
		if (isNotBlank(sc.getFileOrFolderNames()))
			builder.nameString(sc.getFileOrFolderNames());
		if (CollectionUtils.isNotEmpty(sc.getSearchTypes())) {
			Map<String, SearchType> valueEnumMp = SearchType.valueEnumMap();
			Set<String> valueSet = valueEnumMp.keySet();
			Iterator<String> it = valueSet.iterator();
			while (it.hasNext()) {
				String selectedValue = it.next();
				if (sc.getSearchTypes().contains(selectedValue)) {
					switch (valueEnumMp.get(selectedValue)) {
					case files:
						builder.onlyFiles(true);
						break;
					case empty_folders:
						builder.onlyEmptyFolders(true);
						break;
					case file_extensions:
						builder.extensionString(sc.getFileExtensions());
						break;
					case folders:
						builder.onlyFolders(true);
						break;
					default:
						break;
					}
				}
			}
		}
		return builder.build();
	}

	public static List<File> getFilesAndFolders(SearchCriteria sc) {
		return FileUtil.findFilesAndFolders(getFilters(sc), sc.getWorkingDir());
	}

	public static SearchCriteria defaultData() {
		List<String> types = new ArrayList<>();
		types.add("Files");
		return SearchCriteria.builder().workingDir(defaultWorkingDir()).searchTypes(types)
				.backupPath(defaultWorkingDir()).build();
	}

}
