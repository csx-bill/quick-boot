package com.quick.modules.parser;

import apijson.framework.APIJSONParser;

public class OnliineJSONParse extends APIJSONParser {
    @Override
    public int getMaxQueryDepth() {
        return 6;
    }
}
