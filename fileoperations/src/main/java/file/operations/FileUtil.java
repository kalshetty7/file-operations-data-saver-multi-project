package file.operations;

import static org.apache.commons.io.FileUtils.copyToDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtil {

	private static final String BACKUP_FILE_NAME = "Backups.bp";
	private static final String BACKUP_RESTORE_SEPERATOR = "->";

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	static void p(Collection c) {
		Iterator it = c.iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			p(i + ". " + it.next());
		}
	}

	public static void write(String fileName, String targetDir, String contents) {
		String delim = delim(targetDir);
		File f = new File(targetDir + delim + fileName);
		try {
			writeStringToFile(f, contents);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static Path pathNIO(String input) {
		Path p = Paths.get(input);
		return p;
	}

	public static Path pathNIO(File input) {
		return pathNIO(input.getAbsolutePath());
	}

	public static List<String> readFileByLines(Path p) {
		List<String> fileList = null;
		try {
			fileList = Files.readAllLines(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileList;
	}

	public static List<String> readFileByLines(String input) {
		return readFileByLines(pathNIO(input));
	}

	public static List<String> readFileByLines(File input) {
		return readFileByLines(pathNIO(input));
	}

	public static String readFile(String input) {
		return readFile(pathNIO(input));
	}

	public static String readFile(File input) {
		return readFile(pathNIO(input));
	}

	public static String readFile(Path pathNIO) {
		String data = null;
		try {
			data = Files.readString(pathNIO, StandardCharsets.ISO_8859_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private static boolean isEmptyFolder(File f) {
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			if (files == null || files.length == 0)
				return true;
		}
		return false;
	}

	private static List<File> toFileList(String items) {
		List<String> itemList = Filters.splitToList(items);
		List<File> files = new ArrayList<File>();
		itemList.forEach(s -> {
			files.add(new File(s));
		});
		return files;
	}

	private static List<File> toFileList(String... items) {
		List<File> files = new ArrayList<File>();
		for (String item : items)
			files.add(new File(item));
		return files;
	}

	private static List<File> toFileList(File... items) {
		return Arrays.asList(items);
	}

	public static void delete(String items) {
		delete(toFileList(items));
	}

	public static void delete(List<File> files) {
		files.parallelStream().forEach(FileUtil::delete);
	}

	public static void delete(File f) {
		try {
			if (f.isDirectory())
				deleteDirectory(f);
			else
				f.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<File> findAllFiles(String dir) {
		return findFilesAndFolders(Filters.builder().onlyFiles(true).build(), dir);
	}

	public static List<File> findFilesAndFolders(Filters filters, String dir) {
		List<File> fileList = new ArrayList<File>();
		File folder = new File(dir);
		File[] files = folder.listFiles();
		if (files != null && files.length > 0) {
			for (File f : files) {
				filters.setInputFile(f);
				if (filters.predicate())
					fileList.add(f);
				if (f.isDirectory()) {
					fileList.addAll(findFilesAndFolders(filters, f.getAbsolutePath()));
				}
			}
		}
		return fileList;
	}

	public static List<File> findAndDeleteFilesByExtensions(String extensions, String dir) {
		List<File> files = findFilesAndFolders(Filters.builder().onlyFiles(true).extensionString(extensions).build(),
				dir);
		delete(files);
		return files;
	}

	public static List<File> findAndDeleteFolders(String folderNames, String dir) {
		Filters ftrs = Filters.builder().onlyFolders(true).nameString(folderNames).build();
		List<File> foldersToBeDeleted = findFilesAndFolders(ftrs, dir);
		delete(foldersToBeDeleted);
		return foldersToBeDeleted;
	}

	public static List<File> findAndDeleteFilesByName(String dir, String fileNames) {
		Filters ftrs = Filters.builder().onlyFiles(true).nameString(fileNames).build();
		List<File> filesToBeDeleted = findFilesAndFolders(ftrs, dir);
		delete(filesToBeDeleted);
		return filesToBeDeleted;
	}

	public static void move(File f, String targetDir) {
		try {
			String target = targetDir + delim(targetDir) + f.getName();
			new File(target).mkdirs();
			Files.move(pathNIO(f), pathNIO(target), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copy(File f, String targetDir) {
		try {
			File targetFile = new File(targetDir);
			copyToDirectory(f, targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void move(String srcFiles, String targetDir) {
		move(toFileList(srcFiles), targetDir);
	}

	public static void move(List<File> files, String targetDir) {
		files.forEach(f -> move(f, targetDir));
	}

	public static void copy(List<File> files, String targetDir) {
		files.forEach(f -> copy(f, targetDir));
	}

	public static void copy(String srcFiles, String targetDir) {
		copy(toFileList(srcFiles), targetDir);
	}

	private static String delim(String path) {
		if (path.contains("/"))
			return "/";
		else
			return "\\";
	}

	private static String delim(File f) {
		return delim(f.getAbsolutePath());
	}

	public static void findAndCopyFilesByExtension(String srcDir, String targetDir, String extensions) {
		copy(findFilesByExtension(extensions, srcDir), targetDir);
	}

	public static void findAndCopyFilesByName(String srcDir, String targetDir, String names) {
		copy(findFilesByName(names, srcDir), targetDir);
	}

	public static void findAndMoveFilesByExtension(String srcDir, String targetDir, String extensions) {
		move(findFilesByExtension(extensions, srcDir), targetDir);
	}

	public static void findAndMoveFilesByName(String srcDir, String targetDir, String names) {
		move(findFilesByName(names, srcDir), targetDir);
	}

	public static List<File> findFilesByName(String names, String dir) {
		return findFilesAndFolders(Filters.builder().onlyFiles(true).nameString(names).build(), dir);
	}

	public static List<File> findFoldersByName(String names, String dir) {
		return findFilesAndFolders(Filters.builder().onlyFolders(true).nameString(names).build(), dir);
	}

	public static List<File> findFilesByExtension(String extensions, String dir) {
		return findFilesAndFolders(Filters.builder().onlyFiles(true).extensionString(extensions).build(), dir);
	}

	// backup
	public static void backup(String src, String target) {
		backup(toFileList(src), target);
	}

	public static void backup(List<File> files, String target) {
		StringBuilder sb = new StringBuilder();
		files.parallelStream().forEach(f -> {
			String targetDir = target + f.getParent();
			sb.append("\n" + f + BACKUP_RESTORE_SEPERATOR + targetDir + "\n");
			copy(f, targetDir);
		});
		write(BACKUP_FILE_NAME, target, sb.toString());
	}

	public static void findAndBackupFilesByName(String srcDir, String targetDir, String fileNames) {
		backup(findFilesByName(fileNames, srcDir), targetDir);
	}

	public static void findAndBackupFilesByExtension(String srcDir, String targetDir, String extensions) {
		backup(findFilesByExtension(extensions, srcDir), targetDir);
	}

	public static void findAndBackupFoldersByName(String srcDir, String targetDir, String folderNames) {
		backup(findFoldersByName(folderNames, srcDir), targetDir);
	}

	// end backup

	public static void restore(String srcDir) {
		String delim = delim(srcDir);
		File f = new File(srcDir + delim + BACKUP_FILE_NAME);
		List<String> lines = readFileByLines(f);
		for (String l : lines) {
			if (l.contains(BACKUP_RESTORE_SEPERATOR)) {
				String src = l.split(BACKUP_RESTORE_SEPERATOR)[1];
				String target = l.split(BACKUP_RESTORE_SEPERATOR)[0];
				File targetFile = new File(target);
				File srcFile = new File(src + delim + targetFile.getName());
				copy(srcFile, targetFile.getParent());
			}
		}
	}

	public static <T> void saveObjectAsJSON(T t, String fileName, String targetDir) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		String jsonStr = gson.toJson(t);
		write(fileName, targetDir, jsonStr);
	}

	public static <T> T readJSONAsObject(Class<T> c, String file) {
		Gson gson = new Gson();
		T ob = (T) gson.fromJson(readFile(file), c);
		return ob;
	}

	public static <T> T readJSONAsObject(Class<T> c, File f) {
		return readJSONAsObject(c, f.getAbsolutePath());
	}

	public static <T> String convertToJSONString(T t) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		String jsonStr = gson.toJson(t);
		return jsonStr;
	}

}
