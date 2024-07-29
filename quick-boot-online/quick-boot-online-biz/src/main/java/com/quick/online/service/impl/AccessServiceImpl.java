package com.quick.online.service.impl;

import apijson.StringUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.online.dto.AccessVO;
import com.quick.online.dto.OptionsVO;
import com.quick.online.entity.*;
import com.quick.online.mapper.AccessMapper;
import com.quick.online.parser.OnlineFunctionParser;
import com.quick.online.parser.OnlineParser;
import com.quick.online.parser.OnlineVerifier;
import com.quick.online.service.*;
import com.quick.online.util.*;
import com.quick.system.api.ISysDictApi;
import com.quick.system.api.dto.SysDictDataApiDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static apijson.framework.APIJSONConstant.*;
import static apijson.framework.APIJSONConstant.REQUEST_;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {


    private final ISysDictApi sysDictApi;

    private final IRequestService requestService;

    private final IDocumentService documentService;

    private final IAccessSchemaService accessSchemaService;

    private final AMISGeneratorUtils amisGeneratorUtils;

    public static final String TYPE = "type";
    public static final String VALUE = "value";


    @Override
    public List<Map<String, String>> listByTableName(List<String> tableNames,String tableName) {
        return baseMapper.listByTableName(tableNames,tableName);
    }

    @Transactional
    @Override
    public boolean sync(List<String> tableNames) throws IOException {
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
           // List<SysTableColumn> fieldList = sysTableColumnService.selectByTableName(tableName);


//            List<OnlCgformField> onlCgformFieldList = new ArrayList<>();
//            for (SysTableColumn sysTableColumn : fieldList) {
//                OnlCgformField onlCgformField = new OnlCgformField();
//                BeanUtils.copyProperties(sysTableColumn,onlCgformField);
//                onlCgformField.setCgformHeadId(onlCgformHead.getId());
//                onlCgformFieldList.add(onlCgformField);
//            }
            //onlCgformFieldService.saveBatch(onlCgformFieldList);

            
            // 填充软删除字段
//            List<SysTableColumn> delField = fieldList.stream().filter(field -> field.getDbFieldName().equals(CommonConstant.DEL_FLAG)).collect(Collectors.toList());
//            if(!CollectionUtils.isEmpty(delField)){
//                access.setDeletedKey(CommonConstant.DEL_FLAG);
//                access.setDeletedValue(CommonConstant.DELETED_VALUE);
//                access.setNotDeletedValue(CommonConstant.NOT_DELETED_VALUE);
//            }
//            save(access);


        }
        return true;
    }

    @Override
    public AccessVO getAccessColumnsById(String id) {
        Access access = getById(id);
        AccessVO accessVO = new AccessVO();
        BeanUtils.copyProperties(access,accessVO);
//        // 获取字段信息
//        List<SysTableColumn> columns = sysTableColumnService.list(new LambdaQueryWrapper<SysTableColumn>().eq(SysTableColumn::getAccessId, id).orderByAsc(SysTableColumn::getSort));
//
//        // 字典信息
//        Result<List<SysDictDataApiDTO>> queryTypeResult = sysDictApi.queryDictDataByDictCode(CommonConstant.COLUMN_QUERY_TYPE);
//        Result<List<SysDictDataApiDTO>> showTypeResult = sysDictApi.queryDictDataByDictCode(CommonConstant.COLUMN_SHOW_TYPE);
//
//        List<OptionsVO> queryTypeOptions = DictDataToAMISJSONUtils.selectOptions(queryTypeResult.getData());
//        List<OptionsVO> showTypeOptions = DictDataToAMISJSONUtils.selectOptions(showTypeResult.getData());
//
//        accessVO.setColumns(columns);
//        accessVO.setQueryTypeOptions(queryTypeOptions);
//        accessVO.setShowTypeOptions(showTypeOptions);
        return accessVO;
    }

    @Transactional
    @Override
    public boolean updateAccessColumnsById(AccessVO entity) throws IOException {

        return true;
    }

    @Transactional
    @Override
    public boolean refactoringCRUDById(String id) {
//        Access access = getById(id);
//        List<SysTableColumn> columns = sysTableColumnService.list(new LambdaQueryWrapper<SysTableColumn>()
//                .eq(SysTableColumn::getAccessId, access.getId()));
//        // 转换为小驼峰命名法
//        String camelCase = StrUtil.toCamelCase(access.getName());
//        // 将第一个字母大写，得到大驼峰命名法
//        String pascalCase = StrUtil.upperFirst(camelCase);
//
//        // 删除旧接口 crud 一个版本 好维护
//        ArrayList<String> crudTag = new ArrayList<>();
//
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.PAGE));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.SAVE));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BY_ID));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.UPDATE_BATCH_BY_ID));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BY_ID));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.REMOVE_BATCH_BY_IDS));
//        crudTag.add(FormatToAPIJSONUtils.getTag(pascalCase, CommonConstant.GET_BY_ID));
//
//        requestService.remove(new LambdaQueryWrapper<Request>().in(Request::getTag,crudTag));
//
//        ArrayList<String> crudUrl = new ArrayList<>();
//
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.PAGE));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.POST,CommonConstant.SAVE));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BY_ID));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.PUT,CommonConstant.UPDATE_BATCH_BY_ID));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BY_ID));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.DELETE,CommonConstant.REMOVE_BATCH_BY_IDS));
//        crudUrl.add(FormatToAPIJSONUtils.getUrl(pascalCase, CommonConstant.GET,CommonConstant.GET_BY_ID));
//
//        documentService.remove(new LambdaQueryWrapper<Document>().in(Document::getUrl,crudUrl));
//
//        // 批量新增
//        requestService.saveBatch(APIJSONRequestUtils.builderCRUDRequest(pascalCase, columns));
//        documentService.saveBatch(APIJSONDocumentUtils.builderCRUDDocument(pascalCase, columns));
        return true;
    }

    @Override
    public JSONObject getAccessSchemaById(String id) {
        AccessSchema accessSchema = accessSchemaService.getOne(new LambdaQueryWrapper<AccessSchema>().eq(AccessSchema::getAccessId, id));
        if(accessSchema!=null){
            return JSON.parseObject(accessSchema.getSchema());
        }
        return null;
    }

    @SneakyThrows
    @Override
    public boolean reload(String request) {
        OnlineVerifier.initAccess(false, null, null);
        OnlineFunctionParser.init(false, null, null);
        OnlineVerifier.initRequest(false, null, null);
        return true;
    }
}
