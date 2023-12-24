package com.quick.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.online.dto.AccessVO;
import com.quick.online.dto.OptionsVO;
import com.quick.online.entity.Access;
import com.quick.online.entity.Document;
import com.quick.online.entity.Request;
import com.quick.online.entity.SysTableColumn;
import com.quick.online.mapper.AccessMapper;
import com.quick.online.service.IAccessService;
import com.quick.online.service.IDocumentService;
import com.quick.online.service.IRequestService;
import com.quick.online.service.ISysTableColumnService;
import com.quick.online.util.APIJSONDocumentUtils;
import com.quick.online.util.APIJSONRequestUtils;
import com.quick.online.util.DictDataToAMISJSONUtils;
import com.quick.online.util.FormatToAPIJSONUtils;
import com.quick.system.api.ISysDictApi;
import com.quick.system.api.dto.SysDictDataApiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {

    private final ISysTableColumnService sysTableColumnService;

    private final ISysDictApi sysDictApi;

    private final IRequestService requestService;

    private final IDocumentService documentService;

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

            // 获取表字段
            List<SysTableColumn> fieldList = sysTableColumnService.selectByTableName(tableName);
            
            // 填充软删除字段
            List<SysTableColumn> delField = fieldList.stream().filter(field -> field.getDbFieldName().equals(CommonConstant.DEL_FLAG)).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(delField)){
                access.setDeletedKey(CommonConstant.DEL_FLAG);
                access.setDeletedValue(CommonConstant.DELETED_VALUE);
                access.setNotDeletedValue(CommonConstant.NOT_DELETED_VALUE);
            }
            save(access);


            List<SysTableColumn> list = new ArrayList<>();
            for (int i = 0; i < fieldList.size(); i++) {
                SysTableColumn sysTableColumn = fieldList.get(i);
                sysTableColumn.setAccessId(access.getId());
                sysTableColumn.setSort(i);
                list.add(sysTableColumn);
            }
            sysTableColumnService.saveBatch(list);

            // 构建APISJON crud 接口
            requestService.saveBatch(APIJSONRequestUtils.builderCRUDRequest(pascalCase, fieldList));
            documentService.saveBatch(APIJSONDocumentUtils.builderCRUDDocument(pascalCase, fieldList));
        }
        return true;
    }

    @Override
    public AccessVO getAccessColumnsById(String id) {
        Access access = getById(id);
        AccessVO accessVO = new AccessVO();
        BeanUtils.copyProperties(access,accessVO);
        // 获取字段信息
        List<SysTableColumn> columns = sysTableColumnService.list(new LambdaQueryWrapper<SysTableColumn>().eq(SysTableColumn::getAccessId, id).orderByAsc(SysTableColumn::getSort));

        // 字典信息
        Result<List<SysDictDataApiDTO>> queryTypeResult = sysDictApi.queryDictDataByDictCode(CommonConstant.COLUMN_QUERY_TYPE);
        Result<List<SysDictDataApiDTO>> showTypeResult = sysDictApi.queryDictDataByDictCode(CommonConstant.COLUMN_SHOW_TYPE);

        List<OptionsVO> queryTypeOptions = DictDataToAMISJSONUtils.selectOptions(queryTypeResult.getData());
        List<OptionsVO> showTypeOptions = DictDataToAMISJSONUtils.selectOptions(showTypeResult.getData());

        accessVO.setColumns(columns);
        accessVO.setQueryTypeOptions(queryTypeOptions);
        accessVO.setShowTypeOptions(showTypeOptions);
        return accessVO;
    }

    @Transactional
    @Override
    public boolean updateAccessColumnsById(AccessVO entity) {
        Access access = new Access();
        access.setId(entity.getId());
        access.setDebug(entity.getDebug());
        access.setDetail(entity.getDetail());
        access.setDeletedKey(entity.getDeletedKey());
        access.setDeletedValue(entity.getDeletedValue());
        access.setNotDeletedValue(entity.getNotDeletedValue());
        updateById(access);
        //更新字段信息
        List<SysTableColumn> columns = entity.getColumns();
        sysTableColumnService.saveOrUpdateBatch(columns);
        // 更新 APIJSON 接口
        access = getById(entity.getId());
        // 转换为小驼峰命名法
        String camelCase = StrUtil.toCamelCase(access.getName());
        // 将第一个字母大写，得到大驼峰命名法
        String pascalCase = StrUtil.upperFirst(camelCase);

        // 删除旧接口 crud 一个版本 好维护
        ArrayList<String> crudTag = new ArrayList<>();

        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.PAGE));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.SAVE));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BATCH_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BATCH_BY_IDS));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.GET_BY_ID));

        requestService.remove(new LambdaQueryWrapper<Request>().in(Request::getTag,crudTag));

        ArrayList<String> crudUrl = new ArrayList<>();

        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.PAGE));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.POST,CommonConstant.SAVE));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BATCH_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BATCH_BY_IDS));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.GET_BY_ID));

        documentService.remove(new LambdaQueryWrapper<Document>().in(Document::getUrl,crudUrl));

        // 批量新增
        requestService.saveBatch(APIJSONRequestUtils.builderCRUDRequest(pascalCase, columns));
        documentService.saveBatch(APIJSONDocumentUtils.builderCRUDDocument(pascalCase, columns));

        return true;
    }

    @Transactional
    @Override
    public boolean refactoringCRUDById(String id) {
        Access access = getById(id);
        List<SysTableColumn> columns = sysTableColumnService.list(new LambdaQueryWrapper<SysTableColumn>()
                .eq(SysTableColumn::getAccessId, access.getId()));
        // 转换为小驼峰命名法
        String camelCase = StrUtil.toCamelCase(access.getName());
        // 将第一个字母大写，得到大驼峰命名法
        String pascalCase = StrUtil.upperFirst(camelCase);

        // 删除旧接口 crud 一个版本 好维护
        ArrayList<String> crudTag = new ArrayList<>();

        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.PAGE));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.SAVE));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BATCH_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BY_ID));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BATCH_BY_IDS));
        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.GET_BY_ID));

        requestService.remove(new LambdaQueryWrapper<Request>().in(Request::getTag,crudTag));

        ArrayList<String> crudUrl = new ArrayList<>();

        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.PAGE));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.POST,CommonConstant.SAVE));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BATCH_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BY_ID));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BATCH_BY_IDS));
        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.GET_BY_ID));

        documentService.remove(new LambdaQueryWrapper<Document>().in(Document::getUrl,crudUrl));

        // 批量新增
        requestService.saveBatch(APIJSONRequestUtils.builderCRUDRequest(pascalCase, columns));
        documentService.saveBatch(APIJSONDocumentUtils.builderCRUDDocument(pascalCase, columns));
        return true;
    }
}
