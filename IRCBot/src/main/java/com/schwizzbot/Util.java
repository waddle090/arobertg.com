package com.schwizzbot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Util {
	public static String generateHexStringFromFileByteArray(String fileName) {
		byte[] imageInByte = null;
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(new File(fileName));

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(originalImage, "jpg", baos);

			baos.flush();

			imageInByte = baos.toByteArray();

			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Util.bytesToHex(imageInByte);
	}
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
