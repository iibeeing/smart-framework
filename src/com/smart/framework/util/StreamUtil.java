package com.smart.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.log4j.Logger;

public class StreamUtil {
	private static final Logger logger = Logger.getLogger(StreamUtil.class);

	public static void copyStream(InputStream input, OutputStream output) {
		try {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer, 0, buffer.length)) != -1) {
				output.write(buffer, 0, length);
			}
			output.flush();
		} catch (Exception e) {
			logger.error("����������", e);
			throw new RuntimeException(e);
		}
	}

	public static String toString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("Stream ת String ����", e);
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
}