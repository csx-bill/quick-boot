package com.quick.flow.engine.executor;


import com.quick.flow.engine.common.RuntimeContext;
import com.quick.flow.engine.dao.*;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.util.IdGenerator;
import com.quick.flow.engine.util.StrongUuidGenerator;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RuntimeExecutor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExecutor.class);

    @Resource
    protected ExecutorFactory executorFactory;

    @Resource
    protected InstanceDataDAO instanceDataDAO;

    @Resource
    protected NodeInstanceDAO nodeInstanceDAO;

    @Resource
    protected ProcessInstanceDAO processInstanceDAO;

    @Resource
    protected NodeInstanceLogDAO nodeInstanceLogDAO;

    private static final IdGenerator ID_GENERATOR = new StrongUuidGenerator();
    @Resource
    protected FlowInstanceMappingDAO flowInstanceMappingDAO;

    private static final IdGenerator idGenerator = new StrongUuidGenerator();


    protected String genId() {
        return ID_GENERATOR.getNextId();
    }

    public abstract void execute(RuntimeContext runtimeContext) throws ProcessException;

    public abstract void commit(RuntimeContext runtimeContext) throws ProcessException;

    public abstract void rollback(RuntimeContext runtimeContext) throws ProcessException;

    protected abstract boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException;

    protected boolean isSubFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        return runtimeContext.getParentRuntimeContext() != null;
    }

    protected abstract RuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException;

    protected abstract RuntimeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException;
}
