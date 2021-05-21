package file.operations.actions;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Items {
	copy, move, delete, backup_files_by_names, backup_files_by_extensions, create_folder, create_file,
	find_and_copy_files_by_names, find_and_copy_files_by_extensions, find_and_move_files_by_names,
	find_and_move_files_by_extensions, find_and_delete_files_by_names, find_and_delete_files_by_extensions,
	find_and_backup_files_by_names, find_and_backup_files_by_extensions, restore_from_backup, find_files_or_folders,
	replace_file_contents, file_checksum;

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