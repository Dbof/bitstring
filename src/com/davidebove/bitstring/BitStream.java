/**
 * @author Davide
 */
package com.davidebove.bitstring;

import java.io.IOException;

/**
 * @author Davide
 *
 */
public class BitStream extends BitString {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1532199227489394377L;

	private int pos = 0;

	/**
	 * Instantiates a new bit stream.
	 *
	 * @param bitstring
	 *            the bitstring
	 */
	public BitStream(final BitString bitstring) {
		super(bitstring);
	}

	/**
	 * Instantiates a new bit stream.
	 *
	 * @param data
	 *            the data
	 */
	public BitStream(final byte[] data) {
		super(data);
	}

	/**
	 * Instantiates a new bit stream.
	 *
	 * @param data
	 *            the data
	 */
	public BitStream(final String data) {
		super(data);
	}

	/**
	 * Instantiates a new bit stream.
	 *
	 * @param num
	 *            the num
	 * @param pad
	 *            the pad
	 */
	public BitStream(final int num, final boolean pad) {
		super(num, pad);
	}

	/**
	 * Read a byte from the bit stream.
	 *
	 * @return the byte
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte read() throws IOException {
		if (pos + 8 > bits.length())
			throw new IOException();
		pos += 8;
		return (byte) (Integer.parseInt(bits.substring(pos - 8, pos), 2) & 0xff);
	}

	public boolean readBoolean() throws IOException {
		if (pos + 1 > bits.length())
			throw new IOException();
		pos += 1;
		return bits.charAt(pos - 1) == '1';
	}

	public byte[] readBytes(final int count) throws IOException {
		if (count < 0 || pos + count * 8 > bits.length())
			throw new IOException();

		byte[] result = new byte[count];
		for (int i = 0; i < count; i++) {
			result[i] = read();
		}
		return result;
	}

	/**
	 * Read short.
	 *
	 * @return the short
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public short readShort() throws IOException {
		if (pos + 16 > bits.length())
			throw new IOException();
		pos += 16;
		return Short.parseShort(bits.substring(pos - 16, pos), 2);
	}

	/**
	 * Read integer.
	 *
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readInteger() throws IOException {
		return readInteger(32);
	}
	
	public int readInteger(int count) throws IOException {
		if (count < 0 || pos + count > bits.length())
			throw new IOException();
		pos += count;
		return Integer.parseInt(bits.substring(pos - count, pos), 2);
		
	}

	/**
	 * Read long.
	 *
	 * @return the long
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public long readLong() throws IOException {
		if (pos + 64 > bits.length())
			throw new IOException();
		pos += 64;
		return Long.parseLong(bits.substring(pos - 64, pos), 2);
	}

	public long skip(long n) throws IOException {
		if (n < 0)
			throw new IOException();
		long diff = bits.length() - 1 - pos;
		if (diff < n) {
			pos = bits.length();
			return diff;
		} else {
			pos += n;
			return n;
		}
	}
}