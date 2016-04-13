package com.smart.framework.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

public class CodecUtil {
	private static final Logger logger = Logger.getLogger(CodecUtil.class);

	public static String encodeForUTF8(String str) {
		String target;
		try {
			target = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			logger.error("±àÂë³ö´í£¡", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	public static String decodeForUTF8(String str) {
		String target;
		try {
			target = URLDecoder.decode(str, "UTF-8");
		} catch (Exception e) {
			logger.error("½âÂë³ö´í£¡", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	public static String encodeForBase64(String str) {
		return Base64.encodeBase64String(str.getBytes());
	}

	public static String decodeForBase64(String str) {
		return new String(Base64.decodeBase64(str.getBytes()));
	}

	public static String encryptForMD5(String str) {
		return DigestUtils.md5Hex(str);
	}

	public static String encryptForSHA(String str) {
		return DigestUtils.sha1Hex(str);
	}

	public static String createRandomNumber(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}