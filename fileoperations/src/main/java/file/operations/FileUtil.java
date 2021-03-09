package file.operations;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.io.File;
import java.io.FileWriter;
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

import org.apache.commons.collections4.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import file.operations.actions.Action;

public class FileUtil {

	private static final String BACKUP_FILE_NAME = "Backups.bp";

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
		createFoldersIfAbsent(targetDir);
		try (FileWriter fw = new FileWriter(f)) {
			fw.write(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Path pathNIO(String input) {
		Path p = Paths.get(input);
		return p;
	}

	private static Path pathNIO(File input) {
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

	public static List<File> delete(String items) {
		return delete(toFileList(items));
	}

	public static List<File> delete(List<File> files) {
		List<File> filesToBeDeleted = new ArrayList<>();
		files.forEach(f -> {
			File deletedFile = delete(f);
			if (deletedFile != null)
				filesToBeDeleted.add(deletedFile);
		});
		return filesToBeDeleted;
	}

	public static File delete(File f) {
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
				return f;
			} else if (isEmptyFolder(f)) {
				f.delete();
				return f;
			} else {
				List<File> allFiles = findFilesAndFolders(Filters.builder().onlyFiles(true).build(),
						f.getAbsolutePath());
				if (CollectionUtils.isNotEmpty(allFiles))
					allFiles.forEach(File::delete);
				List<File> emptyFolders = findFilesAndFolders(Filters.builder().onlyEmptyFolders(true).build(),
						f.getAbsolutePath());
				if (CollectionUtils.isNotEmpty(emptyFolders))
					emptyFolders.forEach(File::delete);
				while (!isEmptyFolder(f) && f.exists())
					return delete(f);
				return delete(f);
			}
		} else
			return null;
	}

	public static void backupFilesByName(String srcDir, String targetDir, String fileNames) {
		Filters ftrs = Filters.builder().onlyFiles(true).nameString(fileNames).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		File bkp = new File(targetDir + delim + BACKUP_FILE_NAME);
		createFoldersIfAbsent(targetDir);
		try (FileWriter fw = new FileWriter(bkp)) {
			foundFiles.parallelStream().forEach(f -> {
				String suffix = f.getAbsolutePath().replace(new File(srcDir).getParent(), "");
				String targetFile = targetDir + suffix;
				copy(f, new File(targetFile).getParent());
				try {
					fw.write(f.getAbsolutePath() + "  ->  " + targetFile + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void backupFiles(String srcDir, String targetDir, List<File> files) {
		String delim = delim(targetDir);
		File bkp = new File(targetDir + delim + BACKUP_FILE_NAME);
		createFoldersIfAbsent(targetDir);
		try (FileWriter fw = new FileWriter(bkp)) {
			files.parallelStream().forEach(f -> {
				String suffix = f.getAbsolutePath().replace(new File(srcDir).getParent(), "");
				String targetFile = targetDir + suffix;
				copy(f, new File(targetFile).getParent());
				try {
					fw.write(f.getAbsolutePath() + "  ->  " + targetFile + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void backupFilesByExtension(String srcDir, String targetDir, String extensions) {
		Filters ftrs = Filters.builder().onlyFiles(true).extensionString(extensions).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		File bkp = new File(targetDir + delim + BACKUP_FILE_NAME);
		createFoldersIfAbsent(targetDir);
		try (FileWriter fw = new FileWriter(bkp)) {
			foundFiles.parallelStream().forEach(f -> {
				String suffix = f.getAbsolutePath().replace(new File(srcDir).getParent(), "");
				String targetFile = targetDir + suffix;
				copy(f, new File(targetFile).getParent());
				try {
					fw.write(f.getAbsolutePath() + "  ->  " + targetFile + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
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
		return delete(findFilesAndFolders(Filters.builder().onlyFiles(true).extensionString(extensions).build(), dir));
	}

	public static void createFoldersIfAbsent(String dir) {
		createFoldersIfAbsent(new File(dir));
	}

	public static void createFoldersIfAbsent(File f) {
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	public static List<File> findAndDeleteFolders(String folderNames, String dir) {
		Filters ftrs = Filters.builder().onlyFolders(true).nameString(folderNames).build();
		List<File> foldersToBeDeleted = findFilesAndFolders(ftrs, dir);
		return delete(foldersToBeDeleted);
	}

	public static List<File> findAndDeleteFilesByName(String dir, String fileNames) {
		Filters ftrs = Filters.builder().onlyFiles(true).nameString(fileNames).build();
		List<File> filesToBeDeleted = findFilesAndFolders(ftrs, dir);
		return delete(filesToBeDeleted);
	}

	public static void move(File f, String targetDir) {
		try {
			createFoldersIfAbsent(targetDir);
			String delim = delim(targetDir);
			if (f.isDirectory()) {
				List<File> allFiles = findAllFiles(f.getAbsolutePath());
				allFiles.parallelStream().forEach(af -> {
					String suffix = af.getAbsolutePath().replace(f.getParent(), "");
					String targetFile = targetDir + suffix;
					copy(af, new File(targetFile).getParent());
				});
				findFilesAndFolders(Filters.builder().onlyEmptyFolders(true).build(), f.getAbsolutePath())
						.forEach(ef -> {
							String suffix = ef.getAbsolutePath().replace(f.getParent(), "");
							String targetFile = targetDir + suffix;
							createFoldersIfAbsent(targetFile);
						});
			} else
				Files.move(pathNIO(f), pathNIO(targetDir + delim + f.getName()), StandardCopyOption.REPLACE_EXISTING);
			if (f.exists())
				delete(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copy(File f, String targetDir) {
		try {
			createFoldersIfAbsent(targetDir);
			String delim = delim(targetDir);
			if (f.isDirectory()) {
				List<File> allFiles = findAllFiles(f.getAbsolutePath());
				allFiles.parallelStream().forEach(af -> {
					String suffix = af.getAbsolutePath().replace(f.getParent(), "");
					String targetFile = targetDir + suffix;
					copy(af, new File(targetFile).getParent());
				});
				findFilesAndFolders(Filters.builder().onlyEmptyFolders(true).build(), f.getAbsolutePath())
						.forEach(ef -> {
							String suffix = ef.getAbsolutePath().replace(f.getParent(), "");
							String targetFile = targetDir + suffix;
							createFoldersIfAbsent(targetFile);
						});
			} else
				Files.copy(pathNIO(f), pathNIO(targetDir + delim + f.getName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void restoreFiles(String dir) {
		Filters ftrs = Filters.builder().onlyFiles(true).isExactMatch(true).nameString(BACKUP_FILE_NAME).build();
		File f = findFilesAndFolders(ftrs, dir).get(0);
		List<String> lines = readFileByLines(f);
		if (isNotEmpty(lines)) {
			lines.parallelStream().forEach(l -> {
				if (l.contains("->")) {
					String tokens[] = l.split("->");
					String from = tokens[1].trim();
					String to = tokens[0].trim();
					copy(from, new File(to).getParent());
				}
			});
		}
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

	public static void findAndCopyFilesByExtension(String srcDir, String targetDir, String extensions) {
		Filters ftrs = Filters.builder().onlyFiles(true).extensionString(extensions).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		createFoldersIfAbsent(targetDir);
		copy(foundFiles, targetDir);
	}

	public static void findAndCopyFilesByName(String srcDir, String targetDir, String names) {
		Filters ftrs = Filters.builder().onlyFiles(true).nameString(names).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		createFoldersIfAbsent(targetDir);
		copy(foundFiles, targetDir);
	}

	public static void findAndMoveFilesByExtension(String srcDir, String targetDir, String extensions) {
		Filters ftrs = Filters.builder().onlyFiles(true).extensionString(extensions).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		createFoldersIfAbsent(targetDir);
		move(foundFiles, targetDir);
	}

	public static void findAndMoveFilesByName(String srcDir, String targetDir, String names) {
		Filters ftrs = Filters.builder().onlyFiles(true).nameString(names).build();
		List<File> foundFiles = findFilesAndFolders(ftrs, srcDir);
		String delim = delim(targetDir);
		createFoldersIfAbsent(targetDir);
		move(foundFiles, targetDir);
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
