package com.quick.flow.engine.processor;

import com.alibaba.fastjson2.JSON;
import com.quick.flow.engine.common.ErrorEnum;
import com.quick.flow.engine.common.FlowDefinitionStatus;
import com.quick.flow.engine.common.FlowDeploymentStatus;
import com.quick.flow.engine.common.FlowModuleEnum;
import com.quick.flow.engine.exception.DefinitionException;
import com.quick.flow.engine.exception.ParamException;
import com.quick.flow.engine.exception.TurboException;
import com.quick.flow.engine.param.CreateFlowParam;
import com.quick.flow.engine.param.DeployFlowParam;
import com.quick.flow.engine.param.GetFlowModuleParam;
import com.quick.flow.engine.param.UpdateFlowParam;
import com.quick.flow.engine.result.*;
import com.quick.flow.engine.util.IdGenerator;
import com.quick.flow.engine.util.StrongUuidGenerator;
import com.quick.flow.engine.validator.ModelValidator;
import com.quick.flow.engine.validator.ParamValidator;
import com.quick.flow.entity.FlowDefinition;
import com.quick.flow.entity.FlowDeployment;
import com.quick.flow.service.IFlowDefinitionService;
import com.quick.flow.service.IFlowDeploymentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DefinitionProcessor {

    private static final IdGenerator idGenerator = new StrongUuidGenerator();

    @Resource
    private ModelValidator modelValidator;

    @Resource
    private IFlowDefinitionService flowDefinitionService;

    @Resource
    private IFlowDeploymentService flowDeploymentService;

    public CreateFlowResult create(CreateFlowParam createFlowParam) {
        CreateFlowResult createFlowResult = new CreateFlowResult();
        try {
            ParamValidator.validate(createFlowParam);

            FlowDefinition flowDefinition = new FlowDefinition();
            BeanUtils.copyProperties(createFlowParam, flowDefinition);
            String flowModuleId = idGenerator.getNextId();
            flowDefinition.setFlowModuleId(flowModuleId);
            flowDefinition.setStatus(FlowDefinitionStatus.INIT);
            flowDefinitionService.save(flowDefinition);
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

            flowDefinitionService.updateByFlowModuleId(flowDefinition);

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
            FlowDefinition flowDefinition = flowDefinitionService.selectByFlowModuleId(deployFlowParam.getFlowModuleId());

            if (null == flowDefinition) {
                log.warn("deploy flow failed: flow is not exist.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EXIST);
            }

            Integer status = flowDefinition.getStatus();
            if (status != FlowDefinitionStatus.EDITING) {
                log.warn("deploy flow failed: flow is not editing status.||deployFlowParam={}", deployFlowParam);
                throw new DefinitionException(ErrorEnum.FLOW_NOT_EDITING);
            }

            String flowModel = flowDefinition.getFlowModel();
            modelValidator.validate(flowModel, deployFlowParam);

            FlowDeployment flowDeployment = new FlowDeployment();
            BeanUtils.copyProperties(flowDefinition, flowDeployment);
            String flowDeployId = idGenerator.getNextId();
            flowDeployment.setFlowDeployId(flowDeployId);
            flowDeployment.setStatus(FlowDeploymentStatus.DEPLOYED);

            flowDeploymentService.save(flowDeployment);

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
        FlowDefinition flowDefinition = flowDefinitionService.selectByFlowModuleId(flowModuleId);

        if (flowDefinition == null) {
            log.warn("getFlowModuleByFlowModuleId failed: can not find flowDefinitionPO.||flowModuleId={}", flowModuleId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        BeanUtils.copyProperties(flowDefinition, flowModuleResult);
        Integer status = FlowModuleEnum.getStatusByDefinitionStatus(flowDefinition.getStatus());
        flowModuleResult.setStatus(status);
        log.info("getFlowModuleByFlowModuleId||flowModuleId={}||FlowModuleResult={}", flowModuleId, JSON.toJSONString(flowModuleResult));
        return flowModuleResult;
    }

    private FlowModuleResult getFlowModuleByFlowDeployId(String flowDeployId) throws ParamException {
        FlowDeployment flowDeployment = flowDeploymentService.selectByFlowDeployId(flowDeployId);
        if (flowDeployment == null) {
            log.warn("getFlowModuleByFlowDeployId failed: can not find flowDefinitionPO.||flowDeployId={}", flowDeployId);
            throw new ParamException(ErrorEnum.PARAM_INVALID.getErrNo(), "flowDefinitionPO is not exist");
        }
        FlowModuleResult flowModuleResult = new FlowModuleResult();
        BeanUtils.copyProperties(flowDeployment, flowModuleResult);
        Integer status = FlowModuleEnum.getStatusByDeploymentStatus(flowDeployment.getStatus());
        flowModuleResult.setStatus(status);
        log.info("getFlowModuleByFlowDeployId||flowDeployId={}||response={}", flowDeployId, JSON.toJSONString(flowModuleResult));
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
