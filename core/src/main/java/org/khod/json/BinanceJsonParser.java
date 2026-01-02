package org.khod.json;

import io.netty.buffer.ByteBuf;
import org.khod.pojo.field.*;
import org.khod.data.Decimal64;
import org.khod.pojo.item.DefaultPojoItem;
import org.khod.utils.JsonParseException;
import org.khod.utils.StringUtils;

import static org.khod.utils.JsonParseUtils.*;

public class BinanceJsonParser {

    private static final ThreadLocal<StringBuilder> currentKeyTL = ThreadLocal.withInitial((() -> new StringBuilder(128)));

    public static<T extends DefaultPojoItem> void parsePojo(ByteBuf buffer, T pojo)
            throws JsonParseException {
        StringBuilder currentKey = currentKeyTL.get();

        readVerifyNextChar(buffer, '{');

        while (peek(buffer) != '}') {
            Field field = parseKey(buffer, pojo, currentKey);
            readVerifyNextChar(buffer, ':');
            parseValue(buffer, field);
            if(peek(buffer) != '}') {
                readVerifyNextChar(buffer, ',');
            }
        }
    }

    private static void parseArray(final ByteBuf buffer, final ArrayField arrayField)
            throws JsonParseException {
        arrayField.initialize();
        readVerifyNextChar(buffer, '[');
        while (peek(buffer) != ']') {
            Field elementField = arrayField.getNextField();
            parseValue(buffer, elementField);
            if(peek(buffer) != ']') {
                readVerifyNextChar(buffer, ',');
            }
        }
        buffer.readByte(); // consume closing ']'
        arrayField.finalizeArray();
    }

    private static void parseValue(final ByteBuf buffer, final Field field)
            throws JsonParseException {
        skipWhiteSpaces(buffer);
        switch (field.getType()) {
            case BOOLEAN -> {
                boolean value = parseBoolean(buffer);
                ((BooleanField) field).setValue(value);
            }
            case LONG -> {
                long value = parseLong(buffer);
                ((LongField) field).setValue(value);
            }
            case DECIMAL -> {
                Decimal64 value = ((DecimalField) field).getValue();
                parseDecimal64(buffer, value);
            }
            case STRING -> {
                StringBuilder value = ((StringField) field).getValue();
                value.setLength(0);
                parseString(buffer, value);
            }
            case OBJECT -> parsePojo(buffer, ((ObjectField) field).getValue());
            case ARRAY -> parseArray(buffer, ((ArrayField) field));
        }
        skipWhiteSpaces(buffer);
    }



    private static void readVerifyNextChar(final ByteBuf buffer, final char x)
            throws JsonParseException {
        skipWhiteSpaces(buffer);
        char separator = (char) buffer.readByte();
        if (separator != x) {
            throw new JsonParseException(String.format("Expected open bracket '%c' but received '%c'", x, separator),
                                         buffer.readerIndex());
        }
        skipWhiteSpaces(buffer);
    }

    private static <T extends DefaultPojoItem> Field parseKey(final ByteBuf buffer, final T pojo, final StringBuilder currentKey)
            throws JsonParseException {
        currentKey.setLength(0);
        parseString(buffer, currentKey);
        return findField(pojo, buffer.readerIndex(), currentKey);
    }

    private static <T extends DefaultPojoItem> Field findField(T pojo, int index, CharSequence key)
            throws JsonParseException {
        for (int i = 0; i < pojo.getFields().length; i++) {
            Field field = pojo.getFields()[i];
            if (StringUtils.charSequenceEquals(field.getName(), key)) {
                return field;
            }
        }
        throw new JsonParseException(String.format("Unexpected key read '%s'", key),
                                     index);
    }

}
