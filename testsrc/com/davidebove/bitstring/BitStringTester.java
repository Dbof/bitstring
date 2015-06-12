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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitString}
 * 
 * @author Davide
 *
 */
public class BitStringTester {
	private BitString bs;
	private static final String START_PATTERN = "00000100";

	@Before
	public final void setUp() {
		bs = new BitString(START_PATTERN);
	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#BitString(de.hshn.gi.nsa.utils.BitString)}
	 * .
	 */
	@Test
	public final void testBitStringBitString() {
		BitString dup = new BitString(bs);
		assertEquals(bs, dup);
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#BitString(byte[])}.
	 */
	@Test
	public final void testBitStringByteArray() {
		byte[] arr = new byte[] { 1, 2, 3, 4 };
		BitString b = new BitString(arr);
		BitString b2 = new BitString("00000001" + "00000010" + "00000011"
				+ "00000100");
		assertEquals(b2, b);
	}

	@Test
	public void testBitStringInt() {
		BitString number = new BitString(258, false);
		BitString binary = new BitString("0000000100000010"); // 4 bytes
		assertEquals(binary, number);

		// test padding
		number = new BitString(258, true);
		binary = new BitString("00000000000000000000000100000010");
		assertEquals(binary, number);

		// hex numbers
		number = new BitString(0x102, false); // == 258
		assertEquals(binary, number);

		number = new BitString(-2, false);
		assertEquals(new BitString("11111111111111111111111111111110"), number);

	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#BitString(java.lang.String)}.
	 */
	@Test
	public final void testBitStringString() {
		// Construct big string
		char[] charArray = new char[4096]; // in bytes
		Arrays.fill(charArray, '0');
		new BitString(new String(charArray)); // should not throw
												// StackOverflowException

		BitString b = new BitString(START_PATTERN.substring(1));
		assertEquals(bs, b);
		assertEquals(new BitString("00000000"), new BitString("0"));

		try {
			new BitString("");
			fail("Should throw exception");
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#append(byte[])}.
	 */
	@Test
	public final void testAppendByteArray() {
		byte[] sequence = new byte[] { 99, 33, -127 };

		int lastIndex = bs.length();
		bs.append(sequence);
		assertEquals(lastIndex, bs.find(sequence));
	}

	@Test
	public final void testAppendBitString() {
		byte[] sequence = new byte[] { 99, 33, -127 };
		BitString b = new BitString(sequence);

		int lastIndex = bs.length();
		bs.append(b);
		assertEquals(lastIndex, bs.find(sequence));

		// also append bitstring to itself
		String before = b.toString();
		b.append(b);
		assertEquals(2, b.findAll(before).size());

	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#append(java.lang.String)} .
	 */
	@Test
	public final void testAppendString() {
		String sequence = "00000001" + "00000010";

		int lastIndex = bs.length();
		bs.append(sequence);
		assertEquals(lastIndex, bs.find(sequence));

		try {
			bs.append("Other 010101 data");
			fail("Should throw exception!");
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#find(byte[])}.
	 */
	@Test
	public final void testFindByteArray() {
		byte[] sequence = new byte[] { 99, 33, -127 };
		assertEquals(-1, bs.find(sequence));
		assertEquals(0, bs.find(new byte[] { 4 }));
	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#find(java.lang.String)}.
	 */
	@Test
	public final void testFindString() {
		assertEquals(5, bs.find("100"));
		assertEquals(5, bs.find("1"));
		assertEquals(0, bs.find("00000100"));

		String pattern = "001111000011110000";
		bs.append(pattern);
		assertNotEquals(-1, bs.find(pattern));
		assertNotEquals(-1, bs.find("00" + pattern));
	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#findAll(java.lang.String)}.
	 */
	@Test
	public final void testFindAll() {
		bs.append(START_PATTERN);
		List<Integer> found = bs.findAll(START_PATTERN);
		assertEquals(2, found.size());
		for (Integer i : found) {
			assertEquals(START_PATTERN,
					bs.substring(i, i + START_PATTERN.length()).toString());
		}
	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#substring(int, int)}.
	 */
	@Test
	public final void testSubstring() {
		String pattern = "0100010100101101";
		BitString b = new BitString(pattern);
		BitString b1 = new BitString(pattern.substring(0, 8));
		BitString b2 = new BitString(pattern.substring(8, 16));

		assertEquals(b1, b.substring(0, 8));
		assertEquals(b2, b.substring(8, 16));
		assertEquals(b, b.substring(0));

		// test exception
		try {
			b.substring(-2, 1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}

		try {
			b.substring(0, 99);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}

		try {
			b.substring(5, 1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#bitSet(int)}.
	 */
	@Test
	public final void testBitSet() {
		BitString b = new BitString("01010101");
		assertEquals(false, b.bitSet(0));
		assertEquals(true, b.bitSet(1));
		assertEquals(false, b.bitSet(2));
		assertEquals(true, b.bitSet(3));
		assertEquals(false, b.bitSet(4));
		assertEquals(true, b.bitSet(5));
		assertEquals(false, b.bitSet(6));
		assertEquals(true, b.bitSet(7));
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#setBit(int)}.
	 */
	@Test
	public final void testSetBitInt() {
		BitString b = new BitString("0"); // empty 8-bit string
		b.setBit(0);
		b.setBit(2);
		b.setBit(4);
		b.setBit(6);
		assertEquals("10101010", b.toString());

		try {
			b.setBit(-1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
		try {
			b.setBit(8);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
		try {
			b.setBit(100);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}

	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#setBit(int, boolean)}.
	 */
	@Test
	public final void testSetBitIntBoolean() {
		BitString b = new BitString("10101010"); // empty 8-bit string
		b.setBit(0, false);
		b.setBit(2, false);
		b.setBit(4, false);
		b.setBit(6, false);
		assertEquals("00000000", b.toString());

		try {
			b.setBit(-1, false);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
		try {
			b.setBit(8, false);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
		try {
			b.setBit(100, false);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#toString()}.
	 */
	@Test
	public final void testToString() {
		assertEquals("00000000", new BitString("00").toString());
		assertEquals("00000001", new BitString("01").toString());
		assertEquals("00000010", new BitString("10").toString());
		assertEquals("00001000", new BitString("1000").toString());
		assertEquals("11110000", new BitString("11110000").toString());
		assertEquals("0000000011110000", new BitString("011110000").toString());
		assertEquals("01111000", new BitString("1111000").toString());
	}

	/**
	 * Test method for {@link de.hshn.gi.nsa.utils.BitString#toByteArray()}.
	 */
	@Test
	public final void testToByteArray() {
		byte[] sequence = new byte[] { 99, 33, -127, -128, 127, (byte) 250, 0,
				1, -1 };
		BitString bs = new BitString(sequence);
		assertArrayEquals(sequence, bs.toByteArray());
	}

	/**
	 * Test method for
	 * {@link de.hshn.gi.nsa.utils.BitString#equals(java.lang.Object)} .
	 */
	@Test
	public final void testEqualsObject() {
		BitString orig = new BitString("001010101");
		BitString copy1 = new BitString(orig);
		BitString copy2 = new BitString("000001010101");
		BitString badcopy3 = new BitString("10101010");

		assertEquals(orig, copy1);
		assertEquals(orig, copy2);
		assertNotEquals(orig, badcopy3);
	}

	@Test
	public final void testClear() {
		BitString cpy = new BitString(bs);
		bs.clear();
		assertEquals("", bs.toString());
		bs.append(cpy);
		assertEquals(cpy, bs);
	}
}
