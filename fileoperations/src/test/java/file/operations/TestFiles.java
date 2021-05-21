package file.operations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import file.operations.actions.ActionFactory;

public class TestFiles {

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	String mainAreaFolder = null, testAreaFolder = null;
	List<File> totalFiles = null;

	// @BeforeEach
	void init() {
		List<File> testAreaFolders = FileUtil
				.findFilesAndFolders(Filters.builder().onlyFolders(true).nameString("test-area").build(), ".");
		List<File> mainAreaFolders = FileUtil
				.findFilesAndFolders(Filters.builder().onlyFolders(true).nameString("main-area").build(), ".");
		mainAreaFolder = mainAreaFolders.get(0).getAbsolutePath();
		testAreaFolder = testAreaFolders.get(0).getAbsolutePath();
		FileUtil.findFilesAndFolders(Filters.builder().onlyFolders(true).nameString("test").build(), mainAreaFolder)
				.forEach(f -> {
					FileUtil.copy(f, testAreaFolder);
				});
		totalFiles = FileUtil.findAllFiles(testAreaFolder);
	}

	@Disabled
	@ParameterizedTest
	@ValueSource(strings = { "properties", "xml", "txt,log", "" })
	public void delete(String input) {
		int totalCount = totalFiles.size();
		List<File> foundFiles = FileUtil
				.findFilesAndFolders(Filters.builder().onlyFiles(true).nameString(input).build(), testAreaFolder);
		int foundCount = foundFiles.size();
		FileUtil.findAndDeleteFilesByName(testAreaFolder, input);
		totalFiles = FileUtil.findAllFiles(testAreaFolder);
		assertTrue(totalCount - foundCount == totalFiles.size());
	}

	static Stream<String> datasource() {
		return Stream.of("/Users/anupkalshetty/Desktop/cei_joi");
	}

	@Disabled
	@ParameterizedTest
	@MethodSource("datasource")
	void save(String data) {
		// assertTrue(true);
		ActionFactory.executeAllActions(data);
	}

	@Test
	void testjson() {
		ActionFactory.executeAllActions("/Users/anupkalshetty/Desktop/actions");
	}

}
