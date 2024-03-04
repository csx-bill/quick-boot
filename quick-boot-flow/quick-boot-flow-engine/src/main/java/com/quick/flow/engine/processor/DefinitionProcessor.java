package com.quick.flow.engine.processor;

import com.alibaba.fastjson2.JSON;
import com.quick.flow.engine.common.ErrorEnum;
import com.quick.flow.engine.common.FlowDefinitionStatus;
import com.quick.flow.engine.common.FlowDeploymentStatus;
import com.quick.flow.engine.common.FlowModuleEnum;
import com.quick.flow.engine.dao.FlowDeploymentDAO;
import com.quick.flow.engine.entity.FlowDefinition;
import com.quick.flow.engine.entity.FlowDeployment;
import com.quick.flow.engine.exception.DefinitionException;
import com.quick.flow.engine.exception.ParamException;
import com.quick.flow.engine.exception.TurboException;
import com.quick.flow.engine.param.CreateFlowParam;
import com.quick.flow.engine.param.DeployFlowParam;
import com.quick.flow.engine.param.GetFlowModuleParam;
import com.quick.flow.engine.param.UpdateFlowParam;
import com.quick.flow.engine.result.*;
import com.quick.flow.engine.service.IFlowDefinitionService;
import com.quick.flow.engine.util.IdGenerator;
import com.quick.flow.engine.util.StrongUuidGenerator;
import com.quick.flow.engine.validator.ModelValidator;
import com.quick.flow.engine.validator.ParamValidator;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DefinitionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionProcessor.class);

    private static final IdGenerator idGenerator = new StrongUuidGenerator();

    @Resource
    private ModelValidator modelValidator;

    @Resource
    private IFlowDefinitionService flowDefinitionService;

    @Resource
    private FlowDeploymentDAO flowDeploymentDAO;

    public CreateFlowResult create(CreateFlowParam createFlowParam) {
        CreateFlowResult createFlowResult = new CreateFlowResult();
        try {
            ParamValidator.validate(createFlowParam);

            FlowDefinition flowDefinition = new FlowDefinition();
            BeanUtils.copyProperties(createFlowParam, flowDefinition);
            String flowModuleId = idGenerator.getNextId();
            flowDefinition.setFlowModuleId(flowModuleId);
            flowDefinition.setStatus(FlowDefinitionStatus.INIT);
            LocalDateTime date = LocalDateTime.now();
            flowDefinition.setCreateTime(date);
            flowDefinition.setUpdateTime(date);

            boolean rows = flowDefinitionService.save(flowDefinition);
            if (!rows) {
                LOGGER.warn("create flow failed: insert to db failed.||createFlowParam={}", createFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_INSERT_INVALID);
            }

            BeanUtils.copyProperties(flowDefinition, createFlowResult);
            fillCommonResult(createFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(createFlowResult, te);
        }
        return createFlowResult;
    }

    public UpdateFlowResult update(UpdateFlowParam updateFlowParam) {
        UpdateFlowResult updateFlowResult = new UpdateFlowResult();
        try {
            ParamValidator.validate(updateFlowParam);

            FlowDefinition flowDefinition = new FlowDefinition();
            BeanUtils.copyProperties(updateFlowParam, flowDefinition);
            flowDefinition.setStatus(FlowDefinitionStatus.EDITING);
            flowDefinition.setUpdateTime(LocalDateTime.now());

            boolean rows = flowDefinitionService.updateByModuleId(flowDefinition);
            if (!rows) {
                LOGGER.warn("update flow failed: update to db failed.||updateFlowParam={}", updateFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_UPDATE_INVALID);
            }
            fillCommonResult(updateFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(updateFlowResult, te);
        }
        return updateFlowResult;
    }

    public DeployFlowResult deploy(DeployFlowParam deployFlowParam) {
        DeployFlowResult deployFlowResult = new DeployFlowResult();
        try {
            ParamValidator.validate(deployFlowParam);

            FlowDefinition flowDefinition = flowDefinitionService.selectByModuleId(deployFlowParam.getFlowModuleId());
            if (null == flowDefinition) {
                LOGGER.warn("deploy flow failed: flow is not exist.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EXIST);
            }

            Integer status = flowDefinition.getStatus();
            if (status != FlowDefinitionStatus.EDITING) {
                LOGGER.warn("deploy flow failed: flow is not editing status.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EDITING);
            }

            String flowModel = flowDefinition.getFlowModel();
            modelValidator.validate(flowModel, deployFlowParam);

            FlowDeployment flowDeployment = new FlowDeployment();
            BeanUtils.copyProperties(flowDefinition, flowDeployment);
            String flowDeployId = idGenerator.getNextId();
            flowDeployment.setFlowDeployId(flowDeployId);
            flowDeployment.setStatus(FlowDeploymentStatus.DEPLOYED);

            boolean rows = flowDeploymentDAO.save(flowDeployment);
            if (!rows) {
                LOGGER.warn("deploy flow failed: insert to db failed.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.DEFINITION_INSERT_INVALID);
            }

            BeanUtils.copyProperties(flowDeployment, deployFlowResult);
            fillCommonResult(deployFlowResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(deployFlowResult, te);
        }
        return deployFlowResult;
    }

    public FlowModuleResult getFlowModule(GetFlowModuleParam getFlowModuleParam) {
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        try {
            ParamValidator.validate(getFlowModuleParam);
            String flowModuleId = getFlowModuleParam.getFlowModuleId();
            String flowDeployId = getFlowModuleParam.getFlowDeployId();
            if (StringUtils.isNotBlank(flowDeployId)) {
                flowModuleResult = getFlowModuleByFlowDeployId(flowDeployId);
            } else {
                flowModuleResult = getFlowModuleByFlowModuleId(flowModuleId);
            }
            fillCommonResult(flowModuleResult, ErrorEnum.SUCCESS);
        } catch (TurboException te) {
            fillCommonResult(flowModuleResult, te);
        }
        return flowModuleResult;
    }

    private FlowModuleResult getFlowModuleByFlowModuleId(String flowModuleId) throws ParamException {
        FlowDefinition flowDefinition = flowDefinitionService.selectByModuleId(flowModuleId);
        if (flowDefinition == null) {
            LOGGER.warn("getFlowModuleByFlowModuleId failed: can not find flowDefinitionPO.||flowModuleId={}", flowModuleId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        BeanUtils.copyProperties(flowDefinition, flowModuleResult);
        Integer status = FlowModuleEnum.getStatusByDefinitionStatus(flowDefinition.getStatus());
        flowModuleResult.setStatus(status);
        LOGGER.info("getFlowModuleByFlowModuleId||flowModuleId={}||FlowModuleResult={}", flowModuleId, JSON.toJSONString(flowModuleResult));
        return flowModuleResult;
    }

    private FlowModuleResult getFlowModuleByFlowDeployId(String flowDeployId) throws ParamException {
        FlowDeployment flowDeployment = flowDeploymentDAO.selectByDeployId(flowDeployId);
        if (flowDeployment == null) {
            LOGGER.warn("getFlowModuleByFlowDeployId failed: can not find flowDefinitionPO.||flowDeployId={}", flowDeployId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        BeanUtils.copyProperties(flowDeployment, flowModuleResult);
        Integer status = FlowModuleEnum.getStatusByDeploymentStatus(flowDeployment.getStatus());
        flowModuleResult.setStatus(status);
        LOGGER.info("getFlowModuleByFlowDeployId||flowDeployId={}||response={}", flowDeployId, JSON.toJSONString(flowModuleResult));
        return flowModuleResult;
    }

    private void fillCommonResult(CommonResult commonResult, ErrorEnum errorEnum) {
        fillCommonResult(commonResult, errorEnum.getErrNo(), errorEnum.getErrMsg());
    }

    private void fillCommonResult(CommonResult commonResult, TurboException turboException) {
        fillCommonResult(commonResult, turboException.getErrNo(), turboException.getErrMsg());
    }

    private void fillCommonResult(CommonResult commonResult, int errNo, String errMsg) {
        commonResult.setErrCode(errNo);
        commonResult.setErrMsg(errMsg);
    }
}
