package com.file.checksum;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class Checksum {

	public static final List<String> CHECKSUM_TYPES = Arrays.asList(MessageDigestAlgorithms.values());

	public static void main(String[] args) {
		String f = "/Users/anupkalshetty/Desktop/ss.pdf";
		p(calculate("md2", f));
		p(calculate("md5", f));
		p(calculate("sha-1", f));
		p(calculate("sha-256", f));
		p(calculate("sha-384", f));
		p(calculate("sha-512", f));
		p(CHECKSUM_TYPES);
	}

	public static void p(Object o) {
		System.out.print("\n" + o + "\n");
	}

	public static String calculate(String checksumType, String filePath) {
		String hdigest = "", type = "";
		if (isNotBlank(filePath)) {
			for (String t : CHECKSUM_TYPES) {
				if (t.equalsIgnoreCase(checksumType)) {
					type = t;
					break;
				}
			}
			if (isNotBlank(type)) {
				try {
					hdigest = new DigestUtils(type).digestAsHex(new FileInputStream(filePath));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				hdigest = "please enter valid checksum algorithm like md2,md5,sha-256,etc...";
		} else
			hdigest = "please enter valid file path";

		return hdigest;
	}

}
