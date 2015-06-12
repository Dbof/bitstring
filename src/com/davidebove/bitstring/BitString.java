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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A mutable bit string class with easy-to-use methods for creating,
 * manipulating and analysis of binary data.
 * 
 * @author Dbof
 *
 */
public class BitString implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8890179148412068511L;

	/** The regex pattern. */
	private static String REGEX_PATTERN = "^[0|1]+$";

	/** The bits. */
	private String bits;

	/**
	 * Copy constructor.
	 *
	 * @param bitstring
	 *            the bitstring
	 */
	public BitString(final BitString bitstring) {
		bits = bitstring.bits;
	}

	/**
	 * Instantiates a new bit string from a byte array.
	 *
	 * @param data
	 *            the data
	 */
	public BitString(final byte[] data) {
		bits = byteArrayToString(data);
	}

	/**
	 * Instantiates a new bit string from an integer. This can also be used to
	 * initiate hex strings:
	 * <p>
	 * BitString s = new BitString(0xAABBCC);
	 *
	 * @param num
	 *            the integer (or hex string)
	 * @param pad
	 *            true, if byte array should be padded (to 4 bytes)
	 */
	public BitString(final int num, final boolean pad) {
		this(intToByteArray(num, pad));
	}

	/**
	 * Instantiates a new bit string from a string.
	 *
	 * @param data
	 *            the data
	 */
	public BitString(final String data) {
		if (!data.matches(REGEX_PATTERN))
			throw new IllegalArgumentException("String is not a bit string!");
		bits = pad(data);
	}

	/**
	 * Appends a new bit string from a byte array to the current bitstring.
	 *
	 * @param data
	 *            the data
	 */
	public void append(final byte[] data) {
		bits = bits.concat(byteArrayToString(data));
	}

	/**
	 * Appends a new bit string from another bitstring to the current bitstring.
	 *
	 * @param data
	 *            the data
	 */
	public void append(final BitString data) {
		bits = bits.concat(data.bits);
	}

	/**
	 * Appends a new bit string from a string to the current bitstring.
	 *
	 * @param data
	 *            the data
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
	 *            the sequence
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
	 *            the bitstring
	 * @return index
	 */
	public int find(final String bitstring) {
		return bits.indexOf(bitstring);
	}

	/**
	 * Returns all occurrences of the specified bit string contained in the
	 * current bit string.
	 *
	 * @param bitstring
	 *            the bitstring to find
	 * @return a list of indices
	 */
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
	 * Returns a bit string that is a substring of this bit string. The
	 * substring begins with the character at the specified index and extends to
	 * the end of this string.
	 * 
	 * @param beginIndex
	 *            - the beginning index, inclusive.
	 * @return the specified substring.
	 */
	public BitString substring(final int beginIndex) {
		return new BitString(bits.substring(beginIndex));
	}

	/**
	 * Returns the state of the bit at the specified index.
	 *
	 * @param index
	 *            the index
	 * @return true for 1 and false for 0
	 */
	public boolean bitSet(final int index) {
		return bits.charAt(index) == '1';
	}

	/**
	 * Sets a bit to true (1).
	 *
	 * @param index
	 *            the index
	 */
	public void setBit(final int index) {
		this.setBit(index, true);
	}

	/**
	 * Sets a bit to the specified value.
	 *
	 * @param index
	 *            the index
	 * @param value
	 *            the value (0 or 1) to set the bit to
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

	/**
	 * Returns a byte array representing this bit string.
	 *
	 * @return the bit string data as byte sequence
	 */
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bits == null) ? 0 : bits.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BitString)) {
			return false;
		}
		BitString other = (BitString) obj;
		if (bits == null) {
			if (other.bits != null) {
				return false;
			}
		} else if (!bits.equals(other.bits)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the length of this string. The length is equal to the number of
	 * bits in the string
	 * 
	 * @return the length of the sequence of bits represented by this object.
	 */
	public int length() {
		return bits.length();
	}

	/**
	 * Clears this bit string. Note: This is currently the only way to
	 * completely empty a bit string.
	 */
	public void clear() {
		bits = "";
	}

	/**
	 * Converts a byte array to a (binary) string.
	 *
	 * @param data
	 *            the byte data to convert
	 * @return the binary string
	 */
	private static String byteArrayToString(final byte[] data) {
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

	/**
	 * Int to byte array.
	 *
	 * @param data
	 *            the data
	 * @param padding
	 *            true if byte array should be padded (to 4 bytes)
	 * @return the byte[]
	 */
	private static byte[] intToByteArray(final int data, boolean padding) {
		byte[] original = new byte[4];

		original[0] = (byte) (data >> 24);
		original[1] = (byte) (data >> 16);
		original[2] = (byte) (data >> 8);
		original[3] = (byte) (data);

		if (!padding) {
			int lastIndex = 0;
			for (int i = 0; i < 4; ++i) {
				if (original[i] == 0)
					lastIndex = i + 1;
				else
					break;
			}
			return Arrays.copyOfRange(original, lastIndex, 4);
		} else
			return original;
	}

	/**
	 * Adds padding to the string so it corresponds to a byte (8-bit) sequence.
	 *
	 * @param binary
	 *            bit sequence to add padding to
	 * @return the string with padding
	 */
	private String pad(final String binary) {
		StringBuilder b = new StringBuilder(binary);
		while (b.length() % 8 != 0)
			b.insert(0, '0');
		return b.toString();
	}
}
