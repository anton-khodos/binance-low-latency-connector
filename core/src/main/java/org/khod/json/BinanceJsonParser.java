package org.khod.json;

import io.netty.buffer.ByteBuf;
import org.khod.pojo.BinanceAggTrade;
import org.khod.utils.Decimal64;

import java.text.ParseException;

import static org.khod.utils.JsonParseUtils.*;

public class BinanceJsonParser {
    private final ThreadLocal<Decimal64> decimal64 = ThreadLocal.withInitial(Decimal64::new);

    public void parseAggTrade(ByteBuf buffer, BinanceAggTrade out)
            throws ParseException {
        byte currentKey = -1;

        while(buffer.isReadable()) {
            byte peeked = peek(buffer);
            if(Character.isWhitespace((char) peeked) || peeked == '{' || peeked == '}') {
                skipByte(buffer);
                continue;
            }
            if (peeked == ':') {
                if(currentKey == -1) {
                    throw new ParseException("Failed to parse json. Expected (:) but key was not read for the pair.",
                                             buffer.readerIndex()
                    );
                }
                skipByte(buffer);
                continue;
            }

            if(peeked == '\"') {
                skipByte(buffer);
                if (currentKey == -1) {
                    currentKey = buffer.readByte();
                    peeked = peek(buffer);
                    if (peeked != '\"') {
                        throw new ParseException("Failed to parse json. Expected (\") but was (%c)".formatted((char) peeked),
                                                 buffer.readerIndex()
                        );
                    } else {
                        skipByte(buffer);
                    }
                } else {
                    Decimal64 value = decimal64.get();
                    parseDecimal64(buffer, value);
                    switch (currentKey) {
                        case 'e': // Event type
                            break;
                        case 's': // Symbol
                            break;
                        case 'p': // Price
                            out.setPrice(value);
                            break;
                        case 'q': // Quantity
                            out.setQuantity(value);
                            break;
                        case 'm': // Is the buyer the market maker?
                            break;
                    }
                }
            } else {
                long value = parseLong(buffer);
                switch (currentKey) {
                    case 'E' -> // Event time
                            out.setEventTime(value);
                    case 'a' -> // Aggregate trade ID
                            out.setTradeId(value);
                    case 'f' -> // First trade ID
                            out.setFirstTradeId(value);
                    case 'l' -> // Last trade ID
                            out.setLastTradeId(value);
                    case 'T' -> // Trade time
                            out.setTradeTime(value);
                }
            }
        }
    }



}
