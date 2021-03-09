package action.test;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import file.operations.actions.Action;
import file.operations.actions.ActionItem;

public class TestCases {

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	static void p(Collection c) {
		if (isNotEmpty(c)) {
			p("data : " + c);
		}
	}

	static Stream<String> datasource() {
		return Stream.of("/Users/anupkalshetty/Desktop/testop");
	}

	@ParameterizedTest
	@MethodSource("datasource")
	void save(String data) {
		// assertTrue(true);
		ActionItem ai = ActionItem.builder().source(data).operation("delete").build();
		ai.execute();
		Action a = Action.builder().name("to delete cock").actionItems(List.of(ai)).build();
		p(a);
	}

}
