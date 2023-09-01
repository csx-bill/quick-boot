package com.quick.modules.parser;

import apijson.framework.APIJSONParser;

public class OnlineJSONParse extends APIJSONParser {
    @Override
    public int getMaxQueryDepth() {
        return 6;
    }
}
