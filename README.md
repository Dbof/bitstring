bitstring
=========
A tiny java library for handling your bits and bytes

### Usage
Here are a few examples of the most useful methods.
```java
byte[] data = new byte[] { 1, 2, 3, 4, 127, -128 };
byte[] sub = new byte[] { 3, 4, 127 };

BitString long = new BitString(data); // init from byte array

long.find("100"); //find first sequence containing '100'
long.find("100110101"); // find specific sequence
int index = long.find(sub); // returns index where the sequence 3, 4, 127 is found
BitString substr = long.substring(index); // creates substring, this now contains (3, 4, 127, -128)
System.out.println(substr); // prints out bit string
```
You can also get and set single bits
```java
BitString long = new BitString("00000100"); // this is 4 in decimal notation
int length = long.length(); // returns 8 (bits)
long.bitSet(0); // false, bit not set
long.bitSet(5); // true, bit set
long.setBit(length-1); // set last bit to 1
System.out.println(long); // returns "00000101", 5 in decimal notation
```

A few more examples for using the bitstring class are provided in the test package, so make sure to read them, too.
