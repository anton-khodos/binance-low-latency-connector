package org.khod.utils;

public class URLBuilder {
    public static String buildURL(String base, String symbol, String type) {
        return String.format("%s/ws/%s@%s", base, symbol, type);
    }

    public static String buildBinanceProdURL(String symbol, String type) {
        return buildURL(BaseURLs.BINANCE_USDM_WS_PROD, symbol, type);
    }

    public static String buildBinanceTestURL(String symbol, String type) {
        return buildURL(BaseURLs.BINANCE_USDM_WS_TEST, symbol, type);
    }
}
