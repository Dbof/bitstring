package com.davidebove.bitstring;

/*
 * This file is part of com.davidebove.bitstring.
 * 
 * The MIT License (MIT). 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * Copyright (c) 2015.
 * 
 * @author Dbof <dbof@ymail.com>
 */
import java.io.IOException;

/**
 * A BitStream allows the sequential access to a bit string and offers methods
 * to be parsed as a bit stream.
 *
 * @author Davide
 */
public class BitStream extends BitString {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1532199227489394377L;

	/** The position. */
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
	 * Instantiates a new bit stream from a byte array.
	 *
	 * @param data
	 *            the data
	 */
	public BitStream(final byte[] data) {
		super(data);
	}

	/**
	 * Instantiates a new bit stream from a string.
	 *
	 * @param data
	 *            the data
	 */
	public BitStream(final String data) {
		super(data);
	}

	/**
	 * Instantiates a new bit stream from an integer. This can also be used to
	 * initiate hex strings:
	 * <p>
	 * BitString s = new BitString(0xAABBCC, true);
	 *
	 * @param num
	 *            the integer (or hex string)
	 * @param pad
	 *            true, if byte array should be padded (to 4 bytes)
	 */
	public BitStream(final int num, final boolean pad) {
		super(num, pad);
	}

	/**
	 * Read a byte.
	 *
	 * @return the byte
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public byte read() throws IOException {
		if (pos + 8 > bits.length())
			throw new IOException();
		pos += 8;
		return (byte) (Integer.parseInt(bits.substring(pos - 8, pos), 2) & 0xff);
	}

	/**
	 * Read a boolean value.
	 *
	 * @return the boolean value
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public boolean readBoolean() throws IOException {
		if (pos + 1 > bits.length())
			throw new IOException();
		pos += 1;
		return bits.charAt(pos - 1) == '1';
	}

	/**
	 * Read a specific byte count.
	 *
	 * @param count
	 *            the count
	 * @return the byte array with the size 'count'.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
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
	 * Read a short value.
	 *
	 * @return the short
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public short readShort() throws IOException {
		if (pos + 16 > bits.length())
			throw new IOException();
		pos += 16;
		return Short.parseShort(bits.substring(pos - 16, pos), 2);
	}

	/**
	 * Read an integer.
	 *
	 * @return the integer
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int readInteger() throws IOException {
		return readInteger(32);
	}

	/**
	 * Read an integer from a specific bit count. <br>
	 * By default, the size of an integer is 32 bits. By specifying the count
	 * parameter, you can read a specific bit count into an integer value.
	 *
	 * @param count
	 *            the count
	 * @return the int
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int readInteger(int count) throws IOException {
		if (count < 0 || pos + count > bits.length())
			throw new IOException();
		pos += count;
		return Integer.parseInt(bits.substring(pos - count, pos), 2);

	}

	/**
	 * Read a long value.
	 *
	 * @return the long
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public long readLong() throws IOException {
		if (pos + 64 > bits.length())
			throw new IOException();
		pos += 64;
		return Long.parseLong(bits.substring(pos - 64, pos), 2);
	}

	/**
	 * Skips over n bits of data from the byte stream.
	 *
	 * @param n
	 *            - the number of bytes to be skipped.
	 * @return the actual number of bytes skipped.
	 * @throws IOException
	 *             if n is negative or if an I/O error occurs.
	 */
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