package com.quick.online.util;


import com.quick.online.dto.OptionsVO;
import com.quick.system.api.dto.SysDictDataApiDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典转AMIS 数据格式工具类
 */
public class DictDataToAMISJSONUtils {

    /**
     * 数据字典 转 amis 下拉框 格式
     * @param list
     * @return
     */
    public static List<OptionsVO> selectOptions(List<SysDictDataApiDTO> list){
       return  list.stream()
                .map(sysDictData -> {
                    String dictValue = sysDictData.getDictValue();
                    String dictText = sysDictData.getDictText();
                    OptionsVO optionsVO = new OptionsVO();
                    optionsVO.setLabel(dictText);
                    optionsVO.setValue(dictValue);
                    return optionsVO;
                })
                .collect(Collectors.toList());
    }


}
