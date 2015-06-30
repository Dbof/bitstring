/**
 * @author Davide
 */
package com.davidebove.bitstring;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author Davide
 *
 */
public class BitStreamTester {

	/**
	 * Test method for {@link com.davidebove.bitstring.BitStream#read()}.
	 */
	@Test
	public final void testRead() {
		BitStream stream = new BitStream(new byte[] { 4, 1, 2 });
		try {
			assertEquals(4, stream.read());
			assertEquals(1, stream.read());
			assertEquals(2, stream.read());
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadBoolean() {
		BitStream stream = new BitStream("10100101");
		try {
			assertTrue(stream.readBoolean()); // 1
			assertFalse(stream.readBoolean()); // 0
			assertTrue(stream.readBoolean()); // 1
			assertFalse(stream.readBoolean()); // 0
			assertFalse(stream.readBoolean()); // 0
			assertTrue(stream.readBoolean()); // 1
			assertFalse(stream.readBoolean()); // 0
			assertTrue(stream.readBoolean()); // 1
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadBytes() {
		byte[] array = new byte[] { 4, 1, -127 };
		BitStream stream = new BitStream(array);
		try {
			byte[] read = stream.readBytes(3);
			if (!Arrays.equals(array, read))
				fail("Arrays should be equal!");
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadShort() {
		BitStream stream = new BitStream("0000000000000100"
				+ "0000000000000001" + "0000000000000010");
		try {
			assertEquals(4, stream.readShort());
			assertEquals(1, stream.readShort());
			assertEquals(2, stream.readShort());
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadInteger() {
		BitStream stream = new BitStream("00000000000000000000000000000100"
				+ "00000000000000000000000000000001"
				+ "00000000000000000000000000000010");
		try {
			assertEquals(4, stream.readInteger());
			assertEquals(1, stream.readInteger());
			assertEquals(2, stream.readInteger());
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadIntegerInt() {
		BitStream stream = new BitStream("00000100"
				+ "00000001"
				+ "00000000000000000000000000000010");
		try {
			stream.skip(4);
			assertEquals(4, stream.readInteger(4));
			
			stream.skip(3);
			assertEquals(1, stream.readInteger(5));
			
			assertEquals(2, stream.readInteger(32));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testReadLong() {
		BitStream stream = new BitStream(
				"0000000000000000000000000000000000000000000000000000000000000100"
						+ "0000000000000000000000000000000000000000000000000000000000000001"
						+ "0000000000000000000000000000000000000000000000000000000000000010");
		try {
			assertEquals(4, stream.readLong());
			assertEquals(1, stream.readLong());
			assertEquals(2, stream.readLong());
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public final void testSkip() {
		BitStream stream = new BitStream("00001000" + "00000001");
		try {
			assertEquals(4, stream.skip(4));
			assertTrue(stream.readBoolean());
			assertEquals(10, stream.skip(10));
			assertTrue(stream.readBoolean());
		} catch (IOException e) {
			fail();
		}
	}
}
