package com.quick.flow.engine.util;

import com.quick.flow.engine.exception.ProcessException;

import java.util.Map;

public interface ExpressionCalculator {

    /**
     * Execute the conditional expression and get the result of the evaluation
     *
     * @param expression conditional expression
     * @param dataMap    data for calculate
     * @return true or false
     * @throws ProcessException
     */
    Boolean calculate(String expression, Map<String, Object> dataMap) throws ProcessException;
}
