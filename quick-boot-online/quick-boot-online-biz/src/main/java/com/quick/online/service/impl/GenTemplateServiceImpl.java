package com.quick.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.online.dto.GenTemplateDetailsVO;
import com.quick.online.dto.GenTemplateSaveDTO;
import com.quick.online.dto.GenTemplateUpdateDTO;
import com.quick.online.entity.GenTemplate;
import com.quick.online.entity.GenTemplateVariableJs;
import com.quick.online.mapper.GenTemplateMapper;
import com.quick.online.service.IGenTemplateService;
import com.quick.online.service.IGenTemplateVariableJsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenTemplateServiceImpl extends ServiceImpl<GenTemplateMapper, GenTemplate> implements IGenTemplateService {

    private final IGenTemplateVariableJsService genTemplateVariableJsService;

    @Override
    public Boolean saveGenTemplate(GenTemplateSaveDTO saveDTO) {
        GenTemplate genTemplate = new GenTemplate();
        BeanUtils.copyProperties(saveDTO,genTemplate);
        save(genTemplate);
        List<GenTemplateVariableJs> templateVariable = saveDTO.getVariableJsList();
        for (GenTemplateVariableJs templateVariableJs : templateVariable) {
            templateVariableJs.setTemplateId(genTemplate.getId());
        }
        genTemplateVariableJsService.saveBatch(templateVariable);
        return true;
    }

    @Override
    public GenTemplateDetailsVO getGenTemplateById(Long id) {
        GenTemplateDetailsVO vo = new GenTemplateDetailsVO();
        GenTemplate genTemplate = getById(id);
        if(Objects.nonNull(genTemplate)){
            List<GenTemplateVariableJs> list = genTemplateVariableJsService.list(new LambdaQueryWrapper<GenTemplateVariableJs>()
                    .eq(GenTemplateVariableJs::getTemplateId, id));
            BeanUtils.copyProperties(genTemplate,vo);
            vo.setVariableJsList(list);
        }
        return vo;
    }

    @Override
    public Boolean updateGenTemplateById(GenTemplateUpdateDTO updateDTO) {
        GenTemplate genTemplate = new GenTemplate();
        BeanUtils.copyProperties(updateDTO,genTemplate);
        updateById(genTemplate);
        List<GenTemplateVariableJs> templateVariable = updateDTO.getVariableJsList();
        for (GenTemplateVariableJs templateVariableJs : templateVariable) {
            templateVariableJs.setTemplateId(updateDTO.getId());
        }
        genTemplateVariableJsService.saveOrUpdateBatch(templateVariable);
        return true;
    }
}
