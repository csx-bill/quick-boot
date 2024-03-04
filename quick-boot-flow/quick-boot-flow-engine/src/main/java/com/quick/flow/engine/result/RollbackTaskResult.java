package com.quick.flow.engine.result;

import com.google.common.base.MoreObjects;

public class RollbackTaskResult extends RuntimeResult {
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("errCode", getErrCode())
                .add("errMsg", getErrMsg())
                .toString();
    }
}
