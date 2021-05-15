package file.operations.actions;

import java.io.File;
import java.util.List;

import file.operations.FileUtil;
import file.operations.Filters;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionItem {
	private String srcDir, targetDir, fileOrFolderNames, fileOrFolderPaths, fileContents, extensions, checksumType,
			checksumFilePath, operation, replacers;

	private List<File> foundFilesOrFolders;

	boolean findOnlyFiles, findOnlyFolders;

	public void execute() {
		if (findOnlyFiles == false && findOnlyFolders == false)
			findOnlyFiles = true;
		switch (Items.valueEnumMap().get(operation)) {
		case copy:
			FileUtil.copy(new File(srcDir), targetDir);
			break;
		case backup_files_by_names:
			FileUtil.backupFilesByName(srcDir, targetDir, fileOrFolderNames);
			break;
		case backup_files_by_extensions:
			FileUtil.backupFilesByExtension(srcDir, targetDir, extensions);
			break;
		case restore:
			FileUtil.restoreFiles(srcDir);
			break;
		case create_file:
			FileUtil.write(fileOrFolderNames, targetDir, fileContents);
			break;
		case create_folder:
			File f = new File(srcDir);
			FileUtil.createFoldersIfAbsent(f);
			break;
		case move:
			FileUtil.move(new File(srcDir), targetDir);
			break;
		case delete:
			FileUtil.delete(srcDir);
			break;
		case find_and_copy_files_by_names:
			FileUtil.findAndCopyFilesByName(srcDir, targetDir, fileOrFolderNames);
			break;
		case find_and_copy_files_by_extensions:
			FileUtil.findAndCopyFilesByExtension(srcDir, targetDir, extensions);
			break;
		case find_and_move_files_by_names:
			FileUtil.findAndMoveFilesByName(srcDir, targetDir, fileOrFolderNames);
			break;
		case find_and_move_files_by_extensions:
			FileUtil.findAndMoveFilesByExtension(srcDir, targetDir, extensions);
			break;
		case find_and_delete_files_by_names:
			FileUtil.findAndDeleteFilesByName(srcDir, fileOrFolderNames);
			break;
		case find_and_delete_files_by_extensions:
			FileUtil.findAndDeleteFilesByExtensions(srcDir, extensions);
			break;
		case find_files_or_folders:
			foundFilesOrFolders = FileUtil.findFilesAndFolders(Filters.builder().onlyFiles(findOnlyFiles)
					.onlyFolders(findOnlyFolders).nameString(fileOrFolderNames).extensionString(extensions).build(),
					srcDir);
			break;
		case replace_file_contents:
			foundFilesOrFolders = FileUtil.findFilesAndFolders(Filters.builder().onlyFiles(findOnlyFiles)
					.onlyFolders(findOnlyFolders).nameString(fileOrFolderNames).extensionString(extensions).build(),
					srcDir);
			String[] replacerArray = replacers.split(",");
			foundFilesOrFolders.parallelStream().forEach(fff -> {
				String contents = FileUtil.readFile(fff);
				for (String r : replacerArray) {
					String s1 = r.split("->")[0];
					String s2 = r.split("->")[1];
					if (contents.contains(s1))
						contents = contents.replaceAll(s1, s2);
				}
				FileUtil.write(fff.getName(), fff.getParent(), contents);
			});
			break;
		default:
			break;
		}
	}

	public String toString() {
		return FileUtil.convertToJSONString(this);
	}

}
