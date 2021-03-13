package file.operations.actions;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import file.operations.FileUtil;
import file.operations.Filters;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionItem {
	private String srcDir, targetDir, fileOrFolderNames, fileOrFolderPaths, fileContents, extensions, checksumType,
			checksumFilePath, operation;

	private List<File> foundFilesOrFolders;

	@Builder.Default
	boolean findOnlyFiles = true, findOnlyFolders = false;

	public static enum Items {
		copy, move, delete, backup_files_by_names, backup_files_by_extensions, restore, create_folder, create_file,
		find_and_copy_files_by_names, find_and_copy_files_by_extensions, find_and_move_files_by_names,
		find_and_move_files_by_extensions, find_and_delete_files_by_names, find_and_delete_files_by_extensions,
		find_files_or_folders, file_checksum;

		public static Map<String, Items> valueEnumMap() {
			int length = Items.values().length;
			String values[] = new String[length];
			Map<String, Items> mp = new LinkedHashMap<>();
			for (int i = 0; i < length; i++) {
				values[i] = Items.values()[i].name();
				values[i] = values[i].replaceAll("_", " ");
				mp.put(values[i], Items.values()[i]);
			}
			return mp;
		}

		public String getLabel() {
			return this.name().replaceAll("_", " ");
		}
	}

	public void execute() {
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
					.onlyFolders(findOnlyFolders).nameString(fileOrFolderNames).build(), srcDir);
			break;
		default:
			break;
		}
	}
	
	public String toString() {
		return FileUtil.convertToJSONString(this);
	}
	
}
