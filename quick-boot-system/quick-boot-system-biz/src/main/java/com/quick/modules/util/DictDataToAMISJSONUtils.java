package com.quick.modules.util;

import com.quick.modules.online.vo.OptionsVO;
import com.quick.modules.online.vo.SelectOptionsVO;
import com.quick.modules.system.entity.SysDictData;

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
    public static SelectOptionsVO selectOptions(List<SysDictData> list){
        SelectOptionsVO selectOptionsVO = new SelectOptionsVO();

        List<OptionsVO> options = list.stream()
                .map(sysDictData -> {
                    String dictValue = sysDictData.getDictValue();
                    String dictText = sysDictData.getDictText();
                    OptionsVO optionsVO = new OptionsVO();
                    optionsVO.setLabel(dictText);
                    optionsVO.setValue(dictValue);
                    return optionsVO;
                })
                .collect(Collectors.toList());
        selectOptionsVO.setOptions(options);
        return selectOptionsVO;
    }


}
