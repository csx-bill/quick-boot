package com.quick.flow.engine.validator;

import com.quick.flow.engine.common.Constants;
import com.quick.flow.engine.common.ErrorEnum;
import com.quick.flow.engine.common.FlowElementType;
import com.quick.flow.engine.exception.ProcessException;
import com.quick.flow.engine.model.FlowElement;
import com.quick.flow.engine.util.FlowModelUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ElementValidatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementValidatorFactory.class);

    @Resource
    private StartEventValidator startEventValidator;

    @Resource
    private EndEventValidator endEventValidator;

    @Resource
    private SequenceFlowValidator sequenceFlowValidator;

    @Resource
    private UserTaskValidator userTaskValidator;

    @Resource
    private ExclusiveGatewayValidator exclusiveGatewayValidator;

    @Resource
    private CallActivityValidator callActivityValidator;

    public ElementValidator getElementValidator(FlowElement flowElement) throws ProcessException {
        int elementType = flowElement.getType();
        ElementValidator elementValidator = getElementValidator(elementType);

        if (elementValidator == null) {
            LOGGER.warn("getElementValidator failed: unsupported elementType.||elementType={}", elementType);
            throw new ProcessException(ErrorEnum.UNSUPPORTED_ELEMENT_TYPE,
                MessageFormat.format(Constants.NODE_INFO_FORMAT, flowElement.getKey(),
                    FlowModelUtil.getElementName(flowElement), elementType));
        }
        return elementValidator;
    }

    private ElementValidator getElementValidator(int elementType) {
        switch (elementType) {
            case FlowElementType.START_EVENT:
                return startEventValidator;
            case FlowElementType.END_EVENT:
                return endEventValidator;
            case FlowElementType.SEQUENCE_FLOW:
                return sequenceFlowValidator;
            case FlowElementType.USER_TASK:
                return userTaskValidator;
            case FlowElementType.EXCLUSIVE_GATEWAY:
                return exclusiveGatewayValidator;
            case FlowElementType.CALL_ACTIVITY:
                return callActivityValidator;
            default:
                return null;
        }
    }
}
