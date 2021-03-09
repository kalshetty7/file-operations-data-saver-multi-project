package com.file.manager.ui.entities;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchCriteria {
	private String workingDir, backupPath, fileOrFolderNames, fileExtensions, fileContent;
	private List<File> foundRecords;
	private List<String> searchTypes;
}
