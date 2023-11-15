package com.quick.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.online.dto.AccessVO;
import com.quick.online.dto.SelectOptionsVO;
import com.quick.online.dto.SysTableColumnVO;
import com.quick.online.entity.Access;
import com.quick.online.entity.SysTableColumn;
import com.quick.online.mapper.AccessMapper;
import com.quick.online.service.IAccessService;
import com.quick.online.service.ISysTableColumnService;
import com.quick.online.util.DictDataToAMISJSONUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {

    private final ISysTableColumnService sysTableColumnService;

    //private final ISysDictDataService sysDictDataService;
    @Override
    public List<Map<String, String>> listByTableName(List<String> tableNames,String tableName) {
        return baseMapper.listByTableName(tableNames,tableName);
    }

    @Transactional
    @Override
    public boolean sync(List<String> tableNames) {
        List<Map<String, String>> tableMaps = listByTableName(tableNames,null);
        for (Map<String, String> tableMap : tableMaps) {
             String tableName = tableMap.get("tableName");
             String tableComment = tableMap.get("tableComment");
            Access table = getOne(new LambdaQueryWrapper<Access>().eq(Access::getName, tableName));
            if(table!=null){
                continue;
            }
            // 转换为小驼峰命名法
            String camelCase = StrUtil.toCamelCase(tableName);
            // 将第一个字母大写，得到大驼峰命名法
            String pascalCase = StrUtil.upperFirst(camelCase);
            Access access = Access.builder()
                   .alias(pascalCase)
                    .name(tableName)
                    .detail(tableComment)
                    .debug(0).date(LocalDateTime.now()).build();
            save(access);
            // 获取表字段
            List<SysTableColumn> fieldList = sysTableColumnService.selectByTableName(tableName);

            List<SysTableColumn> list = new ArrayList<>();
            for (int i = 0; i < fieldList.size(); i++) {
                SysTableColumn sysTableColumn = fieldList.get(i);
                sysTableColumn.setAccessId(access.getId());
                sysTableColumn.setSort(i);
                // 时间控件
                if ("create_time".equals(sysTableColumn.getDbFieldName())
                        || "update_time".equals(sysTableColumn.getDbFieldName())
                ) {
                    sysTableColumn.setShowType("DATE");
                    sysTableColumn.setQueryType("BETWEEN");
                }
                list.add(sysTableColumn);
            }
            sysTableColumnService.saveBatch(list);
        }
        return true;
    }

    @Override
    public AccessVO getAccessColumnsById(String id) {
        Access access = getById(id);
        AccessVO accessVO = new AccessVO();
        BeanUtils.copyProperties(access,accessVO);
        // 获取字段信息
        List<SysTableColumn> columns = sysTableColumnService.list(new LambdaQueryWrapper<SysTableColumn>().eq(SysTableColumn::getAccessId, id));

        // 字典信息
//        List<SysDictData> queryTypeSysDictData = sysDictDataService.queryDictDataByDictCode(CommonConstant.COLUMN_QUERY_TYPE);
//        List<SysDictData> showTypeSysDictData = sysDictDataService.queryDictDataByDictCode(CommonConstant.COLUMN_SHOW_TYPE);
//        List<SysDictData> dictTableJoinSysDictData = sysDictDataService.queryDictDataByDictCode(CommonConstant.COLUMN_TABLE_JOIN);

//        SelectOptionsVO queryTypeOptionsVO = DictDataToAMISJSONUtils.selectOptions(queryTypeSysDictData);
//        SelectOptionsVO showTypeOptionsVO = DictDataToAMISJSONUtils.selectOptions(showTypeSysDictData);
//        SelectOptionsVO dictTableJoiOptionsVO = DictDataToAMISJSONUtils.selectOptions(dictTableJoinSysDictData);

        List<SysTableColumnVO> columnsVO = new ArrayList<>();
//        for (SysTableColumn sysTableColumn : columns) {
//            SysTableColumnVO sysTableColumnVO = new SysTableColumnVO();
//            BeanUtils.copyProperties(sysTableColumn, sysTableColumnVO);
//            SelectOptionsVO queryType = new SelectOptionsVO();
//            queryType.setValue(sysTableColumn.getQueryType());
//            queryType.setOptions(queryTypeOptionsVO.getOptions());
//            sysTableColumnVO.setQueryType(queryType);
//
//            SelectOptionsVO showType = new SelectOptionsVO();
//            showType.setValue(sysTableColumn.getShowType());
//            showType.setOptions(showTypeOptionsVO.getOptions());
//            sysTableColumnVO.setShowType(showType);
//
//            SelectOptionsVO dictTableJoin = new SelectOptionsVO();
//            dictTableJoin.setValue(sysTableColumn.getDictTableJoin());
//            dictTableJoin.setOptions(dictTableJoiOptionsVO.getOptions());
//            sysTableColumnVO.setDictTableJoin(dictTableJoin);
//
//            columnsVO.add(sysTableColumnVO);
//        }
        accessVO.setColumns(columnsVO);
        return accessVO;
    }

    @Transactional
    @Override
    public boolean updateAccessColumnsById(AccessVO entity) {
        Access access = new Access();
        BeanUtils.copyProperties(entity,access);
        updateById(access);
        //更新字段信息
        //List<SysTableColumn> columns = entity.getColumns();
        //sysTableColumnService.updateBatchById(columns);
        // 更新 Schema

        return true;
    }
}
