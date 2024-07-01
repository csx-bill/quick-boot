package com.quick.flow.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import com.quick.flow.dto.DefinitionPageQuery;
import com.warm.flow.core.entity.Definition;
import com.warm.flow.core.service.DefService;
import com.warm.flow.orm.entity.FlowDefinition;
import com.warm.flow.core.utils.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义Controller
 *
 * @author hh
 * @date 2023-04-11
 */
@Validated
@RestController
@RequestMapping("/flow/definition")
public class DefController  {
    @Resource
    private DefService defService;

    @PostMapping(value = "/page")
    @Operation(summary = CommonConstant.PAGE_MSG)
    public Result<IPage<Definition>> page(@RequestBody PageParam<DefinitionPageQuery> pageParam) {
        FlowDefinition flowDefinition = new FlowDefinition();
        BeanUtil.copyProperties(pageParam.getModel(), flowDefinition);
        // flow组件自带分页功能
        Page<Definition> page = Page.pageOf(pageParam.getPage()==null?1:pageParam.getPage().intValue(), pageParam.getPerPage()==null?10:pageParam.getPerPage().intValue());
        page = defService.orderByCreateTime().desc().page(flowDefinition, page);

        IPage<Definition> pageVo = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        BeanUtils.copyProperties(page,pageVo);
        pageVo.setRecords(page.getList());
        pageVo.setTotal(page.getTotal());
        return Result.success(pageVo);
    }

    @GetMapping(value = "/getById")
    @Operation(summary = CommonConstant.GET_BY_ID_MSG)
    @Parameter(name = "id",required = true)
    public Result<Definition> getById(@RequestParam("id") Long id) {
        return Result.success(defService.getById(id));
    }

    //@PreAuth("{}"+ CommonConstant.ADD)
    @PostMapping(value = "/save")
    @Operation(summary = CommonConstant.SAVE_MSG)
    public Result<FlowDefinition> save(@RequestBody FlowDefinition flowDefinition) {
        defService.checkAndSave(flowDefinition);
        return Result.success(flowDefinition);
    }

    /**
     * 发布流程定义
     */
    @GetMapping("/publish/{id}")
    @Transactional(rollbackFor = Exception.class)
    @Operation(summary = "发布流程定义")
    public Result<Boolean> publish(@PathVariable("id") Long id) {
        return Result.success(defService.publish(id));
    }


    /**
     * 取消发布流程定义
     */
    @GetMapping("/unPublish/{id}")
    @Transactional(rollbackFor = Exception.class)
    @Operation(summary = "取消发布流程定义")
    public Result<Boolean> unPublish(@PathVariable("id") Long id) {
        defService.unPublish(id);
        return Result.success();
    }


    /**
     * 修改流程定义
     */
    @PutMapping(value = "/updateById")
    @Operation(summary = CommonConstant.UPDATE_BY_ID_MSG)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateById(@RequestBody FlowDefinition flowDefinition) {
        return Result.success(defService.updateById(flowDefinition));
    }

    /**
     * 删除流程定义
     */
    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = CommonConstant.REMOVE_BATCH_BY_IDS_MSG)
    @Parameter(name = "ids",required = true)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return Result.success(defService.removeDef(Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList())));
    }


    @PostMapping("/saveXml")
    @Transactional(rollbackFor = Exception.class)
    @Operation(summary = "保存XML")
    public Result<Boolean> saveXml(@RequestBody FlowDefinition def) throws Exception {
        defService.saveXml(def);
        return Result.success();
    }

    /**
     * 复制流程定义
     */
    @GetMapping("/copyDef/{id}")
    @Transactional(rollbackFor = Exception.class)
    @Operation(summary = "保存XML")
    public Result<Boolean> copyDef(@PathVariable("id") Long id) {
        return Result.success(defService.copyDef(id));
    }


    @PostMapping("/importDefinition")
    public Result<Boolean> importDefinition(MultipartFile file) throws Exception {
        defService.importXml(file.getInputStream());
        return Result.success();
    }


    @PostMapping("/exportDefinition/{id}")
    public void exportDefinition(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        Document document = defService.exportXml(id);
        // 设置生成xml的格式
        OutputFormat of = OutputFormat.createPrettyPrint();
        // 设置编码格式
        of.setEncoding("UTF-8");
        of.setIndent(true);
        of.setIndent("    ");
        of.setNewlines(true);

        // 创建一个xml文档编辑器
        XMLWriter writer = new XMLWriter(response.getOutputStream(), of);
        writer.setEscapeText(false);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;");
        writer.write(document);
        writer.close();
    }


    @GetMapping("/xmlString")
    @Parameter(name = "id",required = true)
    public Result<String> xmlString(@RequestParam("id") Long id) throws Exception {
        return Result.success(defService.xmlString(id));
    }

    /**
     * 查询流程图
     *
     * @param instanceId
     * @return
     * @throws IOException
     */
    @GetMapping("/flowChart/{instanceId}")
    public Result<String> flowChart(@PathVariable("instanceId") Long instanceId) throws IOException {
        return Result.success(defService.flowChart(instanceId));
    }
}
