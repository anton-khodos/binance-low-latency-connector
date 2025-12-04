package org.khod.utils;

import io.netty.buffer.ByteBuf;

//TODO: improve parsing
public class JsonParseUtils {

    public static byte peek(ByteBuf buff) {
        return buff.getByte(buff.readerIndex());
    }

    public static byte peek(ByteBuf buff, int index) {
        return buff.getByte(buff.readerIndex() + index);
    }

    public static void skipByte(ByteBuf buff) {
        buff.readByte();
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

    public static boolean parseBoolean(ByteBuf buff) {
        //TODO
        if(peek(buff) == 't') {
            return true;
        } else if(peek(buff) == 'f') {
            return false;
        } else {
            return false;
        }
    }

    public static long parseLong(ByteBuf buff) {
        long x = 0;
        byte peeked = peek(buff);
        while (peeked >= '0' && peeked <= '9') {
            x = x * 10 + (buff.readByte() - '0');
            peeked = peek(buff);
        }
        return x;
    }

    public static void parseDecimal64(ByteBuf buff, Decimal64 decimal64) {
        boolean neg = false;
        boolean exponentStarted = false;
        long mantisa = 0;
        int exponent = 0;

        byte peeked = peek(buff);
        while (peeked >= '0' && peeked <= '9' || peeked == '-' || peeked == '.') {
            byte b = buff.readByte();
            if(b == '-') {
                neg = true;
            } else if(b == '.') {
                exponentStarted = true;
            } else {
                mantisa = mantisa * 10 + (buff.readByte() - '0');
                if(exponentStarted) exponent++;
            }
            peeked = peek(buff);
        }
        exponent = neg ? -exponent : exponent;
        decimal64.setExponent(exponent);
        decimal64.setMantisa(mantisa);
    }

}
