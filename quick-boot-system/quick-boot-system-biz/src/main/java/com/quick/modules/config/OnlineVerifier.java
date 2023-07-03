package com.quick.modules.config;

import apijson.orm.AbstractVerifier;
import apijson.orm.Parser;

public class OnlineVerifier extends AbstractVerifier<Long> {
    public static final String TAG = "OnlineVerifier";

    @Override
    public Parser<Long> createParser() {
        return new OnlineParser();
    }
}
