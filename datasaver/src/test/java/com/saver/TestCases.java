package com.saver;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestCases {

	Saver s;

	@BeforeEach
	void init() {
		s = new Saver();
	}

	static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	static void p(Collection c) {
		if (isNotEmpty(c)) {
			p("data : " + c);
		}
	}

	static Stream datasource() {
		return Stream.of("anup", "ashok", "kalshetty", 334, 6678, 999);
	}

	@ParameterizedTest
	@MethodSource("datasource")
	void save(Object data) {
		s.save(data);
		assertTrue(s.exists(data));
		s.save(List.of("macbook", "dell", "acer"));
		p(s);
	}

	@Test
	public void delete() {
		s.save(List.of("anup", "ashok", "kalshetty", 334, 6678, 999));
		int count = s.getAllData().size();

		// delete kalshetty
		if (s.exists("kalshetty")) {
			count = s.getAllData().size();
			s.delete("kalshetty");
			assertFalse(s.exists("kalshetty"));
			count--;
			assertTrue(s.getAllData().size() == count);
		}

		// delete 999
		Integer i = 999;
		if (s.exists(i)) {
			count = s.getAllData().size();
			s.delete(i);
			assertFalse(s.exists(i));
			count--;
			assertTrue(s.getAllData().size() == count);
		}

		// delete unknown element
		count = s.getAllData().size();
		s.delete("unknown");
		assertTrue(s.getAllData().size() == count);

		// delete remaining list
		Set ds = s.getAllData();
		p("remaining elements : " + ds + "\n" + s.getAllData());
		s.delete(ds);
		assertTrue(s.getAllData().size() == 0);
		ds = s.getAllData();
		p("after deleting elements : " + ds);
	}

}
