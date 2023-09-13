package com.quick.modules.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.entity.SysTableColumn;
import com.quick.modules.online.mapper.AccessMapper;
import com.quick.modules.online.service.IAccessService;
import com.quick.modules.online.service.ISysTableColumnService;
import com.quick.modules.online.vo.AccessVO;
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
    @Override
    public List<Map<String, String>> listByTableName(List<String> tableNames,String tableName) {
        return baseMapper.listByTableName(tableNames,tableName);
    }

    @Override
    public List<Map<String, Object>> getFieldDetails(String tableName) {
        return baseMapper.getFieldDetails(tableName);
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
            List<Map<String, Object>> fieldDetails = getFieldDetails(tableName);

            List<SysTableColumn> list = new ArrayList<>();
            for (int i = 0; i < fieldDetails.size(); i++) {
                Map<String, Object> fieldDetail = fieldDetails.get(i);
                // 忽略删除标记字段
                if ("del_flag".equals(fieldDetail.get("Field").toString())) {
                    continue;
                }

                SysTableColumn sysTableColumn = new SysTableColumn();
                sysTableColumn.setAccessId(access.getId());
                sysTableColumn.setDbFieldName(fieldDetail.get("Field").toString());
                sysTableColumn.setDbFieldTxt(fieldDetail.get("Comment").toString());
                // id 主键
                if(fieldDetail.get("Key").equals("PRI")){
                    sysTableColumn.setDbIsKey("Y");
                }else {
                    sysTableColumn.setDbIsKey("N");
                }
                sysTableColumn.setSort(i);
                sysTableColumn.setIsQuery("N");
                sysTableColumn.setIsShowForm("Y");
                sysTableColumn.setIsShowList("Y");
                sysTableColumn.setIsReadOnly("N");
                sysTableColumn.setIsRequired("N");
                sysTableColumn.setQueryType("LIKE");

                // 时间控件
                if ("create_time".equals(fieldDetail.get("Field").toString())
                        || "update_time".equals(fieldDetail.get("Field").toString())
                ) {
                    sysTableColumn.setShowType("DATE");
                }else {
                    // 默认文本框
                    sysTableColumn.setShowType("TEXT");
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
        accessVO.setColumns(columns);
        return accessVO;
    }

    @Transactional
    @Override
    public boolean updateAccessColumnsById(AccessVO entity) {
        Access access = new Access();
        BeanUtils.copyProperties(entity,access);
        updateById(access);
        //更新字段信息
        List<SysTableColumn> columns = entity.getColumns();
        sysTableColumnService.updateBatchById(columns);
        return true;
    }
}
