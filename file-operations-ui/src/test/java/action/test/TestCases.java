package action.test;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
	}

}
