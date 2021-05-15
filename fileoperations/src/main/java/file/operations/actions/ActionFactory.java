package file.operations.actions;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import file.operations.FileUtil;
import file.operations.Filters;

public class ActionFactory {
	public static List<Action> getAllActions(String actionDir) {
		List<File> actionFiles = FileUtil
				.findFilesAndFolders(Filters.builder().onlyFiles(true).extensionString("json").build(), actionDir);
		return actionFiles.stream().map(f -> getActionFromFile(f)).collect(Collectors.toList());
	}

	public static void saveOrUpdateActions(List<Action> actions, String dir) {
		actions.forEach(a -> saveOrUpdateAction(a, dir));
	}

	public static void saveOrUpdateAction(Action a, String dir) {
		FileUtil.saveObjectAsJSON(a, a.getName(), dir);
	}

	public static String executeAllActions(String dir) {
		List<Action> actions = getAllActions(dir);
		actions.forEach(a -> a.execute());
		return actions.toString();
	}

	public static String executeActions(String dir, String... actionNames) {
		List<Action> actions = getAllActions(dir);
		actions.forEach(a -> {
			for (String name : actionNames)
				if (a.getName().toLowerCase().contains(name.toLowerCase()))
					a.execute();
		});
		return actions.toString();
	}

	public static String executeAction(Action a) {
		a.execute();
		return a.toString();
	}

	public static Action createAction(String actionName) {
		return Action.builder().name(actionName).build();
	}

	public static Action createAction(String actionName, ActionItem... items) {
		return Action.builder().name(actionName).actionItems(List.of(items)).build();
	}

	public static ActionItem createCopyActionItem(String src, String tar) {
		return ActionItem.builder().srcDir(src).targetDir(tar).operation(Items.copy.getLabel()).build();
	}

	public static ActionItem createMoveActionItem(String src, String tar) {
		return ActionItem.builder().srcDir(src).targetDir(tar).operation(Items.move.getLabel()).build();
	}

	public static ActionItem createDeleteActionItem(String src) {
		return ActionItem.builder().srcDir(src).operation(Items.delete.getLabel()).build();
	}

	public static ActionItem createFindAndDeleteFilesByNamesActionItem(String src, String names) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(names)
				.operation(Items.find_and_delete_files_by_names.getLabel()).build();
	}

	public static ActionItem createFindAndDeleteFilesByExtensionsActionItem(String src, String extns) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(extns)
				.operation(Items.find_and_delete_files_by_extensions.getLabel()).build();
	}

	public static ActionItem createBackupFilesByExtensionsActionItem(String src, String tar, String extns) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(extns).targetDir(tar)
				.operation(Items.backup_files_by_extensions.getLabel()).build();
	}

	public static ActionItem getBackupFilesByNamesActionItem(String src, String tar, String names) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(names).targetDir(tar)
				.operation(Items.backup_files_by_names.getLabel()).build();
	}

	public static ActionItem createRestoreActionItem(String src) {
		return ActionItem.builder().srcDir(src).operation(Items.restore.getLabel()).build();
	}

	public static ActionItem createFindAndCopyFilesByNamesActionItem(String src, String tar, String names) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(names).targetDir(tar)
				.operation(Items.find_and_copy_files_by_names.getLabel()).build();
	}

	public static ActionItem createFindAndCopyFilesByExtensionsActionItem(String src, String tar, String extns) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(extns).targetDir(tar)
				.operation(Items.find_and_copy_files_by_extensions.getLabel()).build();
	}

	public static ActionItem createFindAndMoveFilesByNamesActionItem(String src, String tar, String names) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(names).targetDir(tar)
				.operation(Items.find_and_move_files_by_names.getLabel()).build();
	}

	public static ActionItem createFindAndMoveFilesByExtensionsActionItem(String src, String tar, String extns) {
		return ActionItem.builder().srcDir(src).fileOrFolderNames(extns).targetDir(tar)
				.operation(Items.find_and_move_files_by_extensions.getLabel()).build();
	}

	public static ActionItem createCreateFileActionItem(String name, String tar, String content) {
		return ActionItem.builder().fileOrFolderNames(name).fileContents(content).targetDir(tar)
				.operation(Items.create_file.getLabel()).build();
	}

	public static ActionItem createFindFilesOrFoldersActionItem(String names, String extensions, String src) {
		return ActionItem.builder().fileOrFolderNames(names).srcDir(src).extensions(extensions)
				.operation(Items.find_files_or_folders.getLabel()).build();
	}

	public static ActionItem createReplaceFileContentsActionItem(String names, String extensions, String replacers,
			String src) {
		return ActionItem.builder().fileOrFolderNames(names).srcDir(src).extensions(extensions).replacers(replacers)
				.operation(Items.replace_file_contents.getLabel()).build();
	}

	public static void saveAction(Action a, String dir) {
		FileUtil.saveObjectAsJSON(a, a.getName() + ".json", dir);
	}

	public static Action getActionFromFile(File f) {
		return FileUtil.readJSONAsObject(Action.class, f);
	}
}
