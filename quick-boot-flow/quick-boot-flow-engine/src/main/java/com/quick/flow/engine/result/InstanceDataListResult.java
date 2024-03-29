package com.quick.flow.engine.result;

import com.quick.flow.engine.common.ErrorEnum;
import com.quick.flow.engine.model.InstanceData;
import com.google.common.base.MoreObjects;

import java.util.List;

public class InstanceDataListResult extends CommonResult {
    private List<InstanceData> variables;

    public InstanceDataListResult(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public List<InstanceData> getVariables() {
        return variables;
    }

    public void setVariables(List<InstanceData> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("errCode", getErrCode())
                .add("errMsg", getErrMsg())
                .add("variables", variables)
                .toString();
    }
}
