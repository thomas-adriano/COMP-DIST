package com.taxifleet.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ByteArrayUtils {

	public static final byte[] ERROR_BYTE_ARRAY = new byte[] { -1 };
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[] {};
	private static final Logger LOGGER = LogManager
			.getLogger(ByteArrayUtils.class);

	public static void printByteArrayContent(byte[] bytes) {
		for (byte b : bytes) {
			LOGGER.debug(b);
		}
	}

	public static boolean isEmpty(byte[] bytes) {
		if (bytes == null)
			return true;

		if (bytes.length == 0)
			return true;

		for (byte b : bytes) {
			if (b != 0b0)
				return false;
		}
		return true;
	}

	public static Iterator<Byte> readBytesFromIputStream(InputStream inStream) {
		DataInputStream dataIn = new DataInputStream(inStream);
		List<Byte> byteList = new ArrayList<Byte>();
		try {
			while (true) {
				byteList.add(dataIn.readByte());
			}
		} catch (EOFException ex) {
			// faz nada. Simplesmente sinaliza o final do stream
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteList.iterator();
	}

	public static byte[] byteIteratorToPrimiteveByteArray(Iterator<Byte> toParse) {
		List<Byte> byteList = new ArrayList<Byte>();
		while (toParse.hasNext()) {
			byteList.add(toParse.next());
		}

		byte[] buff = new byte[byteList.size()];

		for (int i = 0; i < buff.length; i++) {
			buff[i] = byteList.get(i).byteValue();
		}
		return buff;
	}

	public static byte[] wrappedByteArrayToPrimiteveByteArray(Byte[] toParse) {
		byte[] buff = new byte[toParse.length];
		for (int i = 0; i < toParse.length; i++) {
			buff[i] = toParse[i].byteValue();
		}
		return buff;
	}

	public static byte[] intToByteArray(final int i) {
		final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putInt(i);
		return bb.array();
	}

	public static int byteArrayToInt(final byte[] toConvert) {
		final ByteBuffer bb = ByteBuffer.wrap(toConvert);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}

	public static boolean compareArrays(byte[] one, byte[] two) {
		if (one.length != two.length)
			return false;

		for (int i = 0; i < two.length; i++) {
			if (one[i] != two[i])
				return false;
		}
		return true;
	}

	public static void copyByteArray(byte[] toCopy, byte[] buffer) {

		if (toCopy.length != buffer.length) {
			throw new IllegalArgumentException(
					"The buffer's length is different than the byte array to copy. Array content in String utf-8 encoding: "
							+ new String(buffer));
		}

		for (int i = 0; i < toCopy.length; i++) {
			buffer[i] = toCopy[i];
		}
	}

	public static void copyByteArrayIgnoringLengthDifference(byte[] toCopy,
			byte[] buffer) {

		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = toCopy[i];
		}
	}

}
