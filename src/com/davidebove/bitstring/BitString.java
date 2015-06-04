/**
 * @author Dbof
 */
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

import java.util.ArrayList;
import java.util.List;

/**
 * A mutable bit string class with easy-to-use methods for creating,
 * manipulating and analysis of binary data.
 * 
 * @author Dbof
 *
 */
public class BitString {
	private static String REGEX_PATTERN = "^(0|1)+$";
	private String bits;

	/**
	 * Copy constructor
	 * 
	 * @param bitstring
	 */
	public BitString(final BitString bitstring) {
		bits = bitstring.bits;
	}

	/**
	 * Constructs a new bit string from a byte array
	 */
	public BitString(final byte[] data) {
		bits = byteArrayToString(data);
	}

	/**
	 * Constructs a new bit string from a string
	 */
	public BitString(final String data) {
		if (!data.matches(REGEX_PATTERN))
			throw new IllegalArgumentException("String is not a bit string!");
		bits = pad(data);
	}

	/**
	 * Appends a new bit string from a byte array to the current bitstring
	 * 
	 * @param data
	 */
	public void append(final byte[] data) {
		bits = bits.concat(byteArrayToString(data));
	}

	/**
	 * Appends a new bit string from a byte array to the current bitstring
	 * 
	 * @param data
	 */
	public void append(final String data) {
		if (!data.matches(REGEX_PATTERN))
			throw new IllegalArgumentException("String is not a bit string!");
		bits = bits.concat(pad(data));
	}

	/**
	 * Returns the index within this bit string of the first occurrence of the
	 * specified data.
	 * 
	 * @param sequence
	 * @return index
	 */
	public int find(final byte[] sequence) {
		return find(byteArrayToString(sequence));
	}

	/**
	 * Returns the index within this bit string of the first occurrence of the
	 * specified data.
	 * 
	 * @param bitstring
	 * @return index
	 */
	public int find(final String bitstring) {
		return bits.indexOf(bitstring);
	}

	public List<Integer> findAll(final String bitstring) {
		ArrayList<Integer> result = new ArrayList<>();
		int index = bits.indexOf(bitstring);
		while (index != -1) {
			result.add(index);
			index = bits.indexOf(bitstring, index + 1);
		}
		return result;
	}

	/**
	 * Returns a string that is a substring of this string. The substring begins
	 * at the specified beginIndex and extends to the character at index
	 * endIndex - 1. Thus the length of the substring is endIndex-beginIndex.
	 * 
	 * @param beginIndex
	 *            - the beginning index, inclusive.
	 * @param endIndex
	 *            - the ending index, exclusive.
	 * @return the specified substring.
	 */
	public BitString substring(final int beginIndex, final int endIndex) {
		return new BitString(bits.substring(beginIndex, endIndex));
	}

	/**
	 * Returns a bit string that is a substring of this bit string. The substring begins
	 * with the character at the specified index and extends to the end of this
	 * string.
	 * 
	 * @param beginIndex
	 *            - the beginning index, inclusive.
	 * @return the specified substring.
	 */
	public BitString substring(final int beginIndex) {
		return new BitString(bits.substring(beginIndex));
	}

	public boolean bitSet(final int index) {
		return bits.charAt(index) == '1';
	}

	/**
	 * Sets a bit to true (1)
	 * 
	 * @param index
	 */
	public void setBit(final int index) {
		this.setBit(index, true);
	}

	/**
	 * Sets a bit to the specified value
	 * 
	 * @param index
	 * @param value
	 *            is true (1) or false (0)
	 */
	public void setBit(final int index, final boolean value) {
		if (index < 0 || index >= bits.length())
			throw new IndexOutOfBoundsException();
		char[] tmp = bits.toCharArray();
		tmp[index] = (value) ? '1' : '0';
		bits = String.valueOf(tmp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return bits;
	}

	public byte[] toByteArray() {
		byte[] result = new byte[bits.length() / 8];
		int b_index = 0;

		// parse every 8 bits
		for (int i = 0; i < bits.length(); i += 8) {
			short b = (short) (Short.parseShort(bits.substring(i, i + 8), 2));
			result[b_index++] = (byte) b;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BitString) {
			BitString b = (BitString) obj;
			return (bits.equals(b.bits));
		}
		return false;
	}

	private String byteArrayToString(final byte[] data) {
		StringBuilder builder = new StringBuilder();
		StringBuilder inner = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			inner.append(Integer.toBinaryString(data[i] & 0xFF));

			// add padding
			while (inner.length() < 8) {
				inner.insert(0, '0');
			}
			builder.append(inner.toString());
			inner.setLength(0);
		}
		return builder.toString();
	}

	private String pad(final String binary) {
		StringBuilder b = new StringBuilder(binary);
		while (b.length() % 8 != 0)
			b.insert(0, '0');
		return b.toString();
	}

	/**
	 * @return
	 */
	public int length() {
		return bits.length();
	}
}
