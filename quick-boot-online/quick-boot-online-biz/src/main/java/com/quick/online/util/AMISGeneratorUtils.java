package com.quick.online.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.quick.common.constant.CommonConstant;
import com.quick.online.entity.SysTableColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * AMIS 页面配置 生成工具类  自动构建 CRUD 基础功能
 */
@Slf4j
@Component
public class AMISGeneratorUtils {

    @Value("classpath:template/crud/crud.json")
    private Resource crudJsonFile;

    @Value("classpath:template/crud/filter.json")
    private Resource filterJsonFile;

    @Value("classpath:template/crud/footerToolbar.json")
    private Resource footerToolbarJsonFile;

    @Value("classpath:template/crud/headerToolbar.json")
    private Resource headerToolbarJsonFile;

    @Value("classpath:template/crud/add.json")
    private Resource addJsonFile;

    @Value("classpath:template/crud/bulkDelete.json")
    private Resource bulkDeleteJsonFile;

    @Value("classpath:template/crud/bulkEdit.json")
    private Resource bulkEditJsonFile;

    @Value("classpath:template/crud/delete.json")
    private Resource deleteJsonFile;

    @Value("classpath:template/crud/edit.json")
    private Resource editJsonFile;

    @Value("classpath:template/crud/view.json")
    private Resource viewJsonFile;



    /**
     * 构建 crud
     * @param aliasTableName
     * @param fieldList
     * @return
     * @throws IOException
     */
    public JSONObject crud(String aliasTableName, List<SysTableColumn> fieldList) throws IOException {

        // 读取文件内容
        JSONObject crud = readJsonFile(crudJsonFile.getURI());
        crud.put("id",getUuid());

        // 获取body中的JSONArray
        JSONArray bodyArray = crud.getJSONArray("body");

        // 修改JSONArray中的JSONObject  此处用表别名
        bodyArray.getJSONObject(0).put("id",aliasTableName);

        // 添加 分页查询接口
        JSONObject api = createApi(aliasTableName, CommonConstant.GET,
                CommonConstant.PAGE, pageRequestAdaptor(aliasTableName),
                pageAdaptor(aliasTableName));
        bodyArray.getJSONObject(0).put("api",api);

        // 添加 filter 即 查询参数
        bodyArray.getJSONObject(0).put("filter",filter(fieldList));

        // 添加 columns 即 列表展示字段
        bodyArray.getJSONObject(0).put("columns",columns(aliasTableName,fieldList));

        // 添加 headerToolbar
        bodyArray.getJSONObject(0).put("headerToolbar",headerToolbar(aliasTableName,fieldList));

        // 添加 footerToolbar
        bodyArray.getJSONObject(0).put("footerToolbar",footerToolbar());

        // 将修改后的JSONArray重新设置到JSONObject中
        crud.put("body", bodyArray);

        return crud;
    }



    /**
     * 转成 APIJSON 格式
     * 分页请求适配器
     * api->requestAdaptor
     */
    private String pageRequestAdaptor(String aliasTableName) {
        return """
                var page = (api.data.page - 1);
                var count = api.data.perPage
                // 删除 perPage
                delete api.data['page'];
                delete api.data['perPage'];
                
                // 必须加个 条件 否则 APIJSON 软删除不生效
                api.data['@order'] = "id";
                
                api.data={
                  "%s[]": {
                    "%s": api.data,
                    "page": page,
                    "count": count
                  },
                  "total@": "/%s[]/total",
                  "format": true
                };
                return api;
                """.formatted(aliasTableName,aliasTableName,aliasTableName);
    }


    /**
     * 转成 AMIS 接收格式
     * 分页接收适配器
     *
     * @return
     */
    private String pageAdaptor(String aliasTableName) {
        return """                                
                return {
                    "status": payload.status,
                    "msg": payload.msg,
                    "data": {
                        "items": payload.data.%s ? payload.data.%s : [],
                        "total": payload.data.%s ? payload.data.total : 0
                    }
                };
                """.formatted(StrUtil.lowerFirst(aliasTableName),StrUtil.lowerFirst(aliasTableName),StrUtil.lowerFirst(aliasTableName));
    }


    /**
     * 转成 APIJSON 格式
     * GetById 请求适配器
     * api->requestAdaptor
     */
    private String getByIdRequestAdaptor(String aliasTableName) {
        return """
           api.data={
             "%s": {
             "id": context.id
             },
           };
           return api;
           """.formatted(aliasTableName);
    }


    /**
     * 转成 AMIS 接收格式
     * GetById 接收适配器
     *
     * @return
     */
    private String getByIdAdaptor(String aliasTableName) {
        return """
           return {
               "status": payload.status,
               "msg": payload.msg,
               "data": payload.data.%s
           };
           """.formatted(aliasTableName);
    }


    /**
     * 转成 APIJSON 格式
     * UpdateById 请求适配器
     * api->requestAdaptor
     */
    private String updateByIdRequestAdaptor(String aliasTableName) {
        return """
           api.data={
             "%s": api.data
           };
           return api;
           """.formatted(aliasTableName);
    }

    /**
     * 转成 APIJSON 格式
     * UpdateBatchById 请求适配器
     * api->requestAdaptor
     */
    private String updateBatchByIdRequestAdaptor(String aliasTableName) {
        return """
           // 添加新属性
           api.data['id{}'] = api.data.ids.split(",");
           api.data={
             "%s": api.data
           };
           return api;
           """.formatted(aliasTableName);
    }

    /**
     * 转成 APIJSON 格式
     * RemoveById 请求适配器
     * api->requestAdaptor
     */
    private String removeByIdRequestAdaptor(String aliasTableName) {
        return """
           api.data={
             "%s": {
                "id": context.id
             }
           };
           return api;
           """.formatted(aliasTableName);
    }


    /**
     * 转成 APIJSON 格式
     * RemoveBatchByIds 请求适配器
     * api->requestAdaptor
     */
    private String removeBatchByIdsRequestAdaptor(String aliasTableName) {
        return """
           api.data={
             "%s": {
                "id{}": context.ids.split(",")
             }
           };
           return api;
           """.formatted(aliasTableName);
    }





    /**
     * 转成 APIJSON 格式
     * Save 请求适配器
     * api->requestAdaptor
     */
    public static String saveRequestAdaptor(String aliasTableName) {
        return """
           api.data={
             "%s": api.data
           };
           return api;
           """.formatted(aliasTableName);
    }





    /**
     * crud->columns
     * 通过读取表结构来构建
     */
    public JSONArray columns(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {
        JSONArray columns = new JSONArray();

        for (SysTableColumn fieldDetail : fieldList) {

            // 忽略删除标记字段
            if (CommonConstant.DEL_FLAG.equals(fieldDetail.getDbFieldName())) {
                continue;
            }

            // 跳过不需要显示列表字段
            if (!fieldDetail.getIsShowList().equals(CommonConstant.Y)){
                continue;
            }

            JSONObject column = new JSONObject();
            column.put("id", getUuid());
            column.put("type", "tpl");
            column.put("name", fieldDetail.getDbFieldName());
            column.put("title", fieldDetail.getDbFieldTxt());
            columns.add(column);
        }

        // 操作栏
        columns.add(operation(aliasTableName,fieldList));

        return columns;
    }

    /**
     * columns->操作栏
     */
    public JSONObject operation(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {
        // 操作栏
        JSONObject operation = new JSONObject();
        operation.put("id", getUuid());
        operation.put("type", "operation");
        operation.put("title", "操作");

        JSONArray buttons = new JSONArray();
        // 查看
        buttons.add(operationView(aliasTableName,fieldList));
        // 编辑
        buttons.add(operationEdit(aliasTableName,fieldList));
        // 删除
        buttons.add(operationDelete(aliasTableName));

        operation.put("buttons", buttons);

        return operation;
    }

    /**
     * 操作栏->查看
     */
    public JSONObject operationView(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {

        JSONObject view = readJsonFile(viewJsonFile.getURI());
        view.put("id", getUuid());
        // 添加按钮权限控制
        updateButtonHiddenOn(view,aliasTableName,CommonConstant.VIEW);

        // 使用 JSONPath 获取 $.onEvent.click.actions[0] 数组的第一个元素
        JSONObject firstAction = (JSONObject) JSONPath.eval(view, "$.onEvent.click.actions[0]");
        JSONObject drawer = firstAction.getJSONObject("drawer");
        drawer.put("id", getUuid());

        // 使用 JSONPath 获取 drawer  $.body[0] 数组的第一个元素
        JSONObject firstDrawerBody = (JSONObject) JSONPath.eval(drawer, "$.body[0]");
        firstDrawerBody.put("id", getUuid());

        // 添加编辑表单字段
        JSONArray formFields = createFormFields(fieldList);
        firstDrawerBody.put("body",formFields);

        // 初始化接口
        JSONObject initApi = createApi(aliasTableName, CommonConstant.GET,
                CommonConstant.GET_BY_ID, getByIdRequestAdaptor(aliasTableName),
                getByIdAdaptor(aliasTableName));

        firstDrawerBody.put("initApi", initApi);

        // 使用 JSONPath 修改 关闭 的元素的 id 属性
        JSONPath.set(drawer, "$.actions[0].id", getUuid());

        // 将修改后的 drawer  $.body[0] 数组的第一个元素  对象插入回原始的 JSON 结构中
        JSONPath.set(drawer, "$.body[0]", firstDrawerBody);

        firstAction.put("drawer",drawer);

        // 将修改后的 firstAction 对象插入回原始的 JSON 结构中
        JSONPath.set(view, "$.onEvent.click.actions[0]", firstAction);

        return view;
    }


    /**
     * 操作栏->编辑
     */
    public JSONObject operationEdit(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {

        JSONObject edit = readJsonFile(editJsonFile.getURI());
        edit.put("id", getUuid());
        // 添加按钮权限控制
        updateButtonHiddenOn(edit,aliasTableName,CommonConstant.UPDATE);

        // 使用 JSONPath 获取 $.onEvent.click.actions[0] 数组的第一个元素
        JSONObject firstAction = (JSONObject) JSONPath.eval(edit, "$.onEvent.click.actions[0]");
        JSONObject drawer = firstAction.getJSONObject("drawer");
        drawer.put("id", getUuid());

        // 使用 JSONPath 获取 drawer  $.body[0] 数组的第一个元素
        JSONObject firstDrawerBody = (JSONObject) JSONPath.eval(drawer, "$.body[0]");
        firstDrawerBody.put("id", getUuid());

        // 添加编辑表单字段
        JSONArray formFields = createFormFields(fieldList);
        firstDrawerBody.put("body",formFields);

        // 更新接口
        JSONObject api = createApi(aliasTableName, CommonConstant.PUT,
                CommonConstant.UPDATE_BY_ID, updateByIdRequestAdaptor(aliasTableName),"");

        firstDrawerBody.put("api",api);

        // 初始化接口
        JSONObject initApi = createApi(aliasTableName, CommonConstant.GET,
                CommonConstant.GET_BY_ID, getByIdRequestAdaptor(aliasTableName),
                getByIdAdaptor(aliasTableName));

        firstDrawerBody.put("initApi", initApi);

        // 使用 JSONPath 修改 取消 & 确认 的元素的 id 属性
        JSONPath.set(drawer, "$.actions[0].id", getUuid());
        // 使用 JSONPath 更新数组中的元素的 id 属性
        JSONPath.set(drawer, "$.actions[1].id", getUuid());

        // 编辑成功后列表 重新搜索事件 componentId 必须是 当前crud body id
        JSONPath.set(firstDrawerBody, "$.onEvent.submitSucc.actions[0].componentId", aliasTableName);

        // 将修改后的 drawer  $.body[0] 数组的第一个元素  对象插入回原始的 JSON 结构中
        JSONPath.set(drawer, "$.body[0]", firstDrawerBody);

        firstAction.put("drawer",drawer);

        // 将修改后的 firstAction 对象插入回原始的 JSON 结构中
        JSONPath.set(edit, "$.onEvent.click.actions[0]", firstAction);

        return edit;
    }

    /**
     * 操作栏->删除
     */
    public JSONObject operationDelete(String aliasTableName) throws IOException {

        JSONObject delete = readJsonFile(deleteJsonFile.getURI());
        delete.put("id", getUuid());
        // 添加按钮权限控制
        delete.put("hiddenOn", "${!ARRAYINCLUDES(permsCode,'"+aliasTableName+":delete')}");
        // 添加按钮权限控制
        updateButtonHiddenOn(delete,aliasTableName,CommonConstant.DELETE);

        // 添加删除接口
        JSONObject api = createApi(aliasTableName, CommonConstant.DELETE,
                CommonConstant.REMOVE_BY_ID, removeByIdRequestAdaptor(aliasTableName), "");

        // 使用 JSONPath $.onEvent.click.actions[0].api 插入 JSON 结构中
        JSONPath.set(delete, "$.onEvent.click.actions[0].api", api);

        // 表单重新查询
        // 使用 JSONPath $.onEvent.click.actions[1].componentId 插入 JSON 结构中
        JSONPath.set(delete, "$.onEvent.click.actions[1].componentId", aliasTableName);

        return delete;
    }



    /**
     * 批量删除
     * crud->bulkDelete
     */
    public JSONObject bulkDelete(String aliasTableName) throws IOException {
        JSONObject bulkDelete = readJsonFile(bulkDeleteJsonFile.getURI());
        bulkDelete.put("id", getUuid());
        // 添加按钮权限控制
        updateButtonHiddenOn(bulkDelete,aliasTableName,CommonConstant.BATCHDELETE);

        JSONObject api = createApi(aliasTableName, CommonConstant.DELETE,
                CommonConstant.REMOVE_BATCH_BY_IDS, removeBatchByIdsRequestAdaptor(aliasTableName), "");

        // 使用 JSONPath $.onEvent.click.actions[0].api 插入 JSON 结构中
        JSONPath.set(bulkDelete, "$.onEvent.click.actions[0].api", api);

        // 表单重新查询
        // 使用 JSONPath $.onEvent.click.actions[1].componentId 插入 JSON 结构中
        JSONPath.set(bulkDelete, "$.onEvent.click.actions[1].componentId", aliasTableName);

        return bulkDelete;

    }

    /**
     * 操作栏->编辑
     */
    public JSONObject bulkEdit(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {

        JSONObject bulkEdit = readJsonFile(bulkEditJsonFile.getURI());
        bulkEdit.put("id", getUuid());
        // 添加按钮权限控制
        updateButtonHiddenOn(bulkEdit,aliasTableName,CommonConstant.BATCHUPDATE);

        // 使用 JSONPath 获取 $.onEvent.click.actions[0] 数组的第一个元素
        JSONObject firstAction = (JSONObject) JSONPath.eval(bulkEdit, "$.onEvent.click.actions[0]");
        JSONObject drawer = firstAction.getJSONObject("drawer");
        drawer.put("id", getUuid());

        // 使用 JSONPath 获取 drawer  $.body[0] 数组的第一个元素
        JSONObject firstDrawerBody = (JSONObject) JSONPath.eval(drawer, "$.body[0]");
        firstDrawerBody.put("id", getUuid());

        // 添加编辑表单字段
        JSONArray formFields = createFormFields(fieldList);
        firstDrawerBody.put("body",formFields);

        // 批量更新接口
        JSONObject api = createApi(aliasTableName, CommonConstant.PUT,
                CommonConstant.UPDATE_BATCH_BY_ID, updateBatchByIdRequestAdaptor(aliasTableName), "");

        firstDrawerBody.put("api",api);

        // 使用 JSONPath 修改 取消 & 确认 的元素的 id 属性
        JSONPath.set(drawer, "$.actions[0].id", getUuid());
        // 使用 JSONPath 更新数组中的元素的 id 属性
        JSONPath.set(drawer, "$.actions[1].id", getUuid());

        // 编辑成功后列表 重新搜索事件 componentId 必须是 当前crud body id
        JSONPath.set(firstDrawerBody, "$.onEvent.submitSucc.actions[0].componentId", aliasTableName);

        // 将修改后的 drawer  $.body[0] 数组的第一个元素  对象插入回原始的 JSON 结构中
        JSONPath.set(drawer, "$.body[0]", firstDrawerBody);

        firstAction.put("drawer",drawer);

        // 将修改后的 firstAction 对象插入回原始的 JSON 结构中
        JSONPath.set(bulkEdit, "$.onEvent.click.actions[0]", firstAction);

        return bulkEdit;
    }



    /**
     * crud->headerToolbar
     */
    public JSONArray headerToolbar(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {
        JSONArray headerToolbar = readJsonFileArray(headerToolbarJsonFile.getURI());

        JSONPath.set(headerToolbar.getJSONObject(0), "$.id", getUuid());

        // 第一个容器
        JSONPath.set(headerToolbar.getJSONObject(0), "$.items[0].id", getUuid());

        // 第一个容器 $.[0].items[0].body 新增 批量编辑 批量删除
        JSONArray body = new JSONArray();
        // 新增
        body.add(operationAdd(aliasTableName, fieldList));
        // 批量删除
        body.add(bulkDelete(aliasTableName));
        // 批量编辑
        body.add(bulkEdit(aliasTableName,fieldList));

        JSONPath.set(headerToolbar.getJSONObject(0), "$.items[0].body", body);

        // 第二个容器
        JSONPath.set(headerToolbar.getJSONObject(0), "$.items[1].id", getUuid());

        return headerToolbar;
    }

    public JSONArray footerToolbar() throws IOException {

        JSONArray footerToolbar = readJsonFileArray(footerToolbarJsonFile.getURI());

        JSONPath.set(footerToolbar.getJSONObject(0), "$.id", getUuid());

        // 第一个容器
        JSONPath.set(footerToolbar.getJSONObject(0), "$.items[0].id", getUuid());

        // 第二个容器
        JSONPath.set(footerToolbar.getJSONObject(0), "$.items[1].id", getUuid());
        JSONPath.set(footerToolbar.getJSONObject(0), "$.items[1].body[0].id", getUuid());

        return footerToolbar;
    }


    /**
     * 新增
     * @param aliasTableName
     * @return
     */
    public JSONObject operationAdd(String aliasTableName,List<SysTableColumn> fieldList) throws IOException {

        JSONObject add = readJsonFile(addJsonFile.getURI());
        add.put("id", getUuid());
        // 添加按钮权限控制
        updateButtonHiddenOn(add,aliasTableName,CommonConstant.ADD);

        // 使用 JSONPath 获取 $.onEvent.click.actions[0].drawer
        JSONObject drawer = (JSONObject) JSONPath.eval(add, "$.onEvent.click.actions[0].drawer");
        drawer.put("id", getUuid());

        JSONObject firstDrawerBody = (JSONObject) JSONPath.eval(drawer, "$.body[0]");
        firstDrawerBody.put("id", getUuid());
        // 添加表单字段
        JSONArray formFields = createFormFields(fieldList);
        JSONPath.set(firstDrawerBody, "$.body", formFields);


        // 添加 接口
        JSONObject api = createApi(aliasTableName, CommonConstant.POST,
                CommonConstant.SAVE, saveRequestAdaptor(aliasTableName), "");

        firstDrawerBody.put("api",api);

        // 使用 JSONPath 修改 取消 & 确认 的元素的 id 属性
        JSONPath.set(drawer, "$.actions[0].id", getUuid());
        // 使用 JSONPath 更新数组中的元素的 id 属性
        JSONPath.set(drawer, "$.actions[1].id", getUuid());

        // 新增成功后列表 重新搜索事件 componentId 必须是 当前crud body id
        JSONPath.set(firstDrawerBody, "$.onEvent.submitSucc.actions[0].componentId", aliasTableName);

        // 使用 JSONPath 更新
        JSONPath.set(add, "$.onEvent.click.actions[0].drawer", drawer);

        return add;
    }


    /**
     * crud->filter
     * 查询参数
     */
    public JSONObject filter(List<SysTableColumn> fieldList) throws IOException {
        JSONObject filter = readJsonFile(filterJsonFile.getURI());
        filter.put("id",getUuid());

        // 添加 filter 即 查询参数
        JSONArray body = new JSONArray();
        for (SysTableColumn fieldDetail : fieldList) {
            String dbFieldName = fieldDetail.getDbFieldName();
            // 忽略字段 删除标记 主键 字段
            if (CommonConstant.DEL_FLAG.equals(dbFieldName) || CommonConstant.Y.equals(fieldDetail.getDbIsKey())) {
                continue;
            }
            // 跳过非查询字段
            if(!fieldDetail.getIsQuery().equals(CommonConstant.Y)){
                continue;
            }

            JSONObject column = new JSONObject();
            column.put("id", getUuid());
            column.put("type", "input-text");
            column.put("size", "full");
            column.put("name", fieldDetail.getDbFieldName());
            column.put("label", fieldDetail.getDbFieldTxt());
            body.add(column);
        }

        // 查询  重置 按钮
        JSONObject submit = new JSONObject();
        submit.put("id", getUuid());
        submit.put("type", "submit");
        submit.put("label", "查询");
        submit.put("level", "primary");
        body.add(submit);

        JSONObject reset = new JSONObject();
        reset.put("id", getUuid());
        reset.put("type", "reset");
        reset.put("label", "重置");
        body.add(reset);

        // 将修改后的JSONArray重新设置到JSONObject中
        filter.put("body", body);

        return filter;
    }



    /**
     * 获取UUID
     * @return
     */
    public String getUuid(){
        return CommonConstant.U+IdUtil.nanoId(12);
    }

    /**
     * 读取JSON文件内容
     *
     * @param filePath 文件路径
     * @return JSON对象
     * @throws IOException 读取文件异常
     */
    public JSONObject readJsonFile(URI filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(filePath));
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * 读取JSON数组文件内容
     *
     * @param filePath 文件路径
     * @return JSON数组
     * @throws IOException 读取文件异常
     */
    public JSONArray readJsonFileArray(URI filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(filePath));
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        return JSONArray.parseArray(jsonStr);
    }


    /**
     * 表单字段
     * @param fieldList
     * @return
     */
    private JSONArray createFormFields(List<SysTableColumn> fieldList) {
        JSONArray body = new JSONArray();
        for (SysTableColumn fieldDetail : fieldList) {
            if (!shouldIgnoreField(fieldDetail)) {
                JSONObject column = new JSONObject();
                column.put("id", getUuid());
                column.put("type", "input-text");
                column.put("required", CommonConstant.Y.equals(fieldDetail.getIsRequired()));
                column.put("name", fieldDetail.getDbFieldName());
                column.put("label", fieldDetail.getDbFieldTxt());

                if (CommonConstant.Y.equals(fieldDetail.getDbIsKey())) {
                    column.put("hidden", true);
                }

                body.add(column);
            }
        }
        return body;
    }

    /**
     * 忽略字段
     * @param fieldDetail
     * @return
     */
    private boolean shouldIgnoreField(SysTableColumn fieldDetail) {
        String dbFieldName = fieldDetail.getDbFieldName();
        return CommonConstant.DEL_FLAG.equals(dbFieldName)
                || CommonConstant.CREATE_TIME.equals(dbFieldName)
                || CommonConstant.CREATE_BY.equals(dbFieldName)
                || CommonConstant.UPDATE_TIME.equals(dbFieldName)
                || CommonConstant.UPDATE_BY.equals(dbFieldName);
    }

    /**
     * 按钮权限
     * @param jsonObject
     * @param aliasTableName
     * @param actionType  操作类型
     * @return
     */
    public JSONObject updateButtonHiddenOn(JSONObject jsonObject, String aliasTableName, String actionType) {
        String hiddenOnValue = "${!ARRAYINCLUDES(permsCode, '" + aliasTableName + ":" + actionType + "')}";
        jsonObject.put("hiddenOn", hiddenOnValue);
        return jsonObject;
    }


    /**
     * 创建接口对象
     *
     * @param aliasTableName 表别名
     * @return 初始化接口的 JSONObject
     */
    private JSONObject createApi(String aliasTableName,String method,String tag, String requestAdaptor,String adaptor) {
        JSONObject api = new JSONObject();
        String url = String.format("/api/online/crud/%s/%s%s", method, aliasTableName,tag);
        api.put("url", url);
        api.put("method", "post");
        // 请求适配
        api.put("requestAdaptor", requestAdaptor);
        // 接收适配
        api.put("adaptor", adaptor);
        return api;
    }
}
