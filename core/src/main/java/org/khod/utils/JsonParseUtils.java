package org.khod.utils;

import io.netty.buffer.ByteBuf;
import org.khod.data.Decimal64;

public class JsonParseUtils {

    public static byte peek(ByteBuf buff) throws JsonParseException {
        if (!buff.isReadable()) {
            throw new JsonParseException("Incomplete json received.", buff.readerIndex());
        }
        return buff.getByte(buff.readerIndex());
    }

    public static void skipWhiteSpaces(ByteBuf buff) throws JsonParseException {
        while (buff.isReadable()) {
            if(!Character.isWhitespace((char) peek(buff))) {
                return;
            }
            buff.readByte();
        }
    }

    public static void parseString(ByteBuf buff, StringBuilder builder) {
        boolean initialized = false;
        while (buff.isReadable()) {
            byte b = buff.readByte();
            if(b == '\"') {
                if(initialized) {
                    return;
                }
                initialized = true;
            } else {
                builder.append((char)b);
            }
        }
    }

    public static char parseChar(ByteBuf buff) {
        boolean initialized = false;
        char result = (char) -1;
        while (buff.isReadable()) {
            byte b = buff.readByte();
            if(b == '\"') {
                if(initialized) {
                    return result;
                }
                initialized = true;
            } else {
                result = (char)b;
            }
        }
        return result;
    }

    public static boolean parseBoolean(ByteBuf buff)
            throws JsonParseException {
        final byte peek = peek(buff);
        if(peek == 't') {
            verifyTrue(buff);
            return true;
        } else if(peek == 'f') {
            verifyFalse(buff);
            return false;
        } else {
            throw new JsonParseException(String.format("Unexpected char '%c' inside boolean value", peek), buff.readerIndex());
        }
    }

    private static void verifyTrue(ByteBuf buff)
            throws JsonParseException {
        verifyChar(buff, 't');
        verifyChar(buff, 'r');
        verifyChar(buff, 'u');
        verifyChar(buff, 'e');
    }

    private static void verifyFalse(ByteBuf buff)
            throws JsonParseException {
        verifyChar(buff, 'f');
        verifyChar(buff, 'a');
        verifyChar(buff, 'l');
        verifyChar(buff, 's');
        verifyChar(buff, 'e');
    }


    private static void verifyChar(final ByteBuf buff, final char t)
            throws JsonParseException {
        byte b = buff.readByte();
        if (b != t) {
            throw new JsonParseException(String.format("Unexpected char '%c' inside boolean value", b), buff.readerIndex());
        }
    }

    public static long parseLong(ByteBuf buff) throws JsonParseException {
        long x = 0;
        byte peeked = peek(buff);
        while (peeked >= '0' && peeked <= '9') {
            x = x * 10 + (buff.readByte() - '0');
            peeked = peek(buff);
        }
        return x;
    }

    public static void parseDecimal64(ByteBuf buff, Decimal64 decimal64)
            throws JsonParseException {
        boolean neg = false;
        boolean exponentStarted = false;
        long mantisa = 0;
        int exponent = 0;

        boolean initialized = false;
        while (buff.isReadable()) {
            byte b = buff.readByte();
            if(b == '\"') {
                if(initialized) {
                    break;
                }
                initialized = true;
            } else if(b >= '0' && b <= '9') {
                mantisa = mantisa * 10 + (b - '0');
                if(exponentStarted) exponent++;
            } else if(b == '-') {
                neg = true;
            } else if(b == '.') {
                exponentStarted = true;
            } else {
                throw new JsonParseException(String.format("Unexpected char '%c' inside decimal value", b),
                                             buff.readerIndex());
            }
        }

        mantisa = neg ? -mantisa : mantisa;
        decimal64.setExponent(-exponent);
        decimal64.setMantissa(mantisa);
        decimal64.normalize();
    }

}
