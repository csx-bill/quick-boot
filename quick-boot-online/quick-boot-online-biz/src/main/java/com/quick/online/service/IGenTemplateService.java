package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.dto.GenTemplateDetailsVO;
import com.quick.online.dto.GenTemplateSaveDTO;
import com.quick.online.dto.GenTemplateUpdateDTO;
import com.quick.online.entity.GenTemplate;

public interface IGenTemplateService extends IService<GenTemplate> {

    Boolean saveGenTemplate(GenTemplateSaveDTO saveDTO);
    GenTemplateDetailsVO getGenTemplateById(Long id);

    Boolean updateGenTemplateById(GenTemplateUpdateDTO updateDTO);
}
