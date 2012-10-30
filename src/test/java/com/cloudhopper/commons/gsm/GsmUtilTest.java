package com.cloudhopper.commons.gsm;

/*
 * #%L
 * ch-commons-gsm
 * %%
 * Copyright (C) 2012 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.cloudhopper.commons.util.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joelauer
 */
public class GsmUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(GsmUtilTest.class);

    @Test
    public void toBcdWithString() {
        byte[] bytes0 = GsmUtil.toBcd("13135554272");
        Assert.assertArrayEquals(HexUtil.toByteArray("3131554572f2"), bytes0);
        byte[] bytes1 = GsmUtil.toBcd("10950");
        Assert.assertArrayEquals(HexUtil.toByteArray("0159f0"), bytes1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void toBcdWithStringInvalidDigits() {
        byte[] bytes0 = GsmUtil.toBcd("131 5554272");
    }

    @Test
    public void getShortMessageUserData() {
        Assert.assertNull(GsmUtil.getShortMessageUserData(null));
        Assert.assertArrayEquals(HexUtil.toByteArray(""), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("")));
        Assert.assertArrayEquals(HexUtil.toByteArray("02"), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("0002")));
        Assert.assertArrayEquals(HexUtil.toByteArray(""), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("0102")));
        Assert.assertArrayEquals(HexUtil.toByteArray("0304050607"), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("01020304050607")));
        // make sure "signed" byte of user data header works
        Assert.assertArrayEquals(HexUtil.toByteArray("08"), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("80000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708")));
        try {
            // UDH length exceeds payload
            Assert.assertArrayEquals(HexUtil.toByteArray("01"), GsmUtil.getShortMessageUserData(HexUtil.toByteArray("01")));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // correct behavior
        }
    }

    @Test
    public void getShortMessageUserDataHeader() {
        Assert.assertNull(GsmUtil.getShortMessageUserDataHeader(null));
        Assert.assertArrayEquals(HexUtil.toByteArray("00"), GsmUtil.getShortMessageUserDataHeader(HexUtil.toByteArray("0002")));
        Assert.assertArrayEquals(HexUtil.toByteArray("0102"), GsmUtil.getShortMessageUserDataHeader(HexUtil.toByteArray("0102")));
        Assert.assertArrayEquals(HexUtil.toByteArray("0102"), GsmUtil.getShortMessageUserDataHeader(HexUtil.toByteArray("01020304050607")));
        // make sure "signed" byte of user data header works
        Assert.assertArrayEquals(HexUtil.toByteArray("800001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607"), GsmUtil.getShortMessageUserDataHeader(HexUtil.toByteArray("80000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708090001020304050607080900010203040506070809000102030405060708")));
        try {
            // UDH length exceeds payload
            Assert.assertArrayEquals(HexUtil.toByteArray("01"), GsmUtil.getShortMessageUserDataHeader(HexUtil.toByteArray("01")));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // correct behavior
        }
    }

    @Test
    public void createConcatenatedBinaryShortMessages() {
        Assert.assertNull(GsmUtil.createConcatenatedBinaryShortMessages(null, (byte)0));
        // these don't need concatenated
        Assert.assertNull(GsmUtil.createConcatenatedBinaryShortMessages(new byte[0], (byte)0));
        Assert.assertNull(GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("00"), (byte)0));
        Assert.assertNull(GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("0001"), (byte)0));
        // full 140 bytes (boundary)
        Assert.assertNull(GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B"), (byte)0));

        // start of concatenated sms
        byte[][] smsParts = null;

        // 141 bytes (2 sms)
        smsParts = GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C"), (byte)2);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020201000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485"), smsParts[0]);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020202868788898A8B8C"), smsParts[1]);

        // 268 bytes (exactly 2 sms)
        smsParts = GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A0B"), (byte)2);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020201000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485"), smsParts[0]);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020202868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A0B"), smsParts[1]);

        // 269 bytes (3 sms)
        smsParts = GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A0B0C"), (byte)2);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020301000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485"), smsParts[0]);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003020302868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A0B"), smsParts[1]);
        Assert.assertArrayEquals(HexUtil.toByteArray("0500030203030C"), smsParts[2]);

        // 267 bytes (2 sms)
        smsParts = GsmUtil.createConcatenatedBinaryShortMessages(HexUtil.toByteArray("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A"), (byte)0xFF);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003FF0201000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485"), smsParts[0]);
        Assert.assertArrayEquals(HexUtil.toByteArray("050003FF0202868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF000102030405060708090A"), smsParts[1]);


        /**
        int size = (134*2) + 1;
        byte[] d = new byte[size];
        for (int i = 0; i < size; i++) {
            d[i] = (byte)i;
        }
        logger.debug(size + ": " + HexUtil.toHexString(d));
         */
        
    }

}
