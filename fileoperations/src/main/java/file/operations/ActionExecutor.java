package file.operations;

import java.io.File;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import file.operations.actions.Action;
import file.operations.actions.ActionFactory;

public class ActionExecutor {
	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	static String WORKING_DIR = System.getProperty("user.dir");

	private static void generateSampleAction() {
		Action a = ActionFactory.createAction("sample_backup", ActionFactory.createBackupFilesByExtensionsActionItem(
				"/Users/anupkalshetty/Desktop/wksp", "/Users/anupkalshetty/Desktop/javafiles", "java"));
		ActionFactory.saveAction(a, WORKING_DIR);
	}

	public static void main(String[] args) {
		p("wkg dir : " + WORKING_DIR);
		List<File> files = FileUtil.findFilesByExtension("json", WORKING_DIR);
		if (CollectionUtils.isEmpty(files)) {
			p("No action json found .... generating sample action for reference......");
			generateSampleAction();
		} else {
			p("files : " + files);
			ActionFactory.executeAllActions(WORKING_DIR);
		}
	}
}
