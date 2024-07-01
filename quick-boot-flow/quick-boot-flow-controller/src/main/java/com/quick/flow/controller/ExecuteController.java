package com.quick.flow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import com.quick.flow.dto.DoneTaskPageQuery;
import com.quick.flow.dto.ToDoTaskPageQuery;
import com.warm.flow.core.FlowFactory;
import com.warm.flow.core.constant.FlowCons;
import com.warm.flow.core.entity.*;
import com.warm.flow.core.enums.NodeType;
import com.warm.flow.core.service.HisTaskService;
import com.warm.flow.core.service.InsService;
import com.warm.flow.core.service.NodeService;
import com.warm.flow.core.service.TaskService;
import com.warm.flow.core.utils.StreamUtils;
import com.warm.flow.orm.entity.FlowHisTask;
import com.warm.flow.orm.entity.FlowTask;
import com.warm.flow.core.utils.page.Page;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程实例Controller
 *
 * @author hh
 * @date 2023-04-18
 */
@Validated
@RestController
@RequestMapping("/flow/execute")
public class ExecuteController {
//    @Resource
//    private ISysUserService userService;

    @Resource
    private HisTaskService hisTaskService;

    @Resource
    private TaskService taskService;

    @Resource
    private NodeService nodeService;

    @Resource
    private InsService insService;


    @PostMapping(value = "/toDoPage")
    @Operation(summary = "分页待办任务列表")
    public Result<IPage<Task>> toDoPage(@RequestBody PageParam<ToDoTaskPageQuery> pageParam) {
        FlowTask flowTask = new FlowTask();
        BeanUtil.copyProperties(pageParam.getModel(), flowTask);
        // flow组件自带分页功能
        Page<Task> page = Page.pageOf(pageParam.getPage()==null?1:pageParam.getPage().intValue(), pageParam.getPerPage()==null?10:pageParam.getPerPage().intValue());
        //flowTask.setPermissionList(new ArrayList<String>());
//        page = taskService.toDoPage(flowTask, page);
//
//        IPage<Task> pageVo = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
//        BeanUtils.copyProperties(page,pageVo);
//        pageVo.setRecords(page.getList());
//        pageVo.setTotal(page.getTotal());
        //return Result.success(pageVo);
        return Result.success(null);
    }

    @PostMapping(value = "/donePage")
    @Operation(summary = "分页已办任务列表")
    public Result<IPage<HisTask>> donePage(@RequestBody PageParam<DoneTaskPageQuery> pageParam) {
        FlowHisTask flowHisTask = BeanUtil.toBean(pageParam.getModel(), FlowHisTask.class);
        // flow组件自带分页功能
        Page<HisTask> page = Page.pageOf(pageParam.getPage()==null?1:pageParam.getPage().intValue(), pageParam.getPerPage()==null?10:pageParam.getPerPage().intValue());
        //page = hisTaskService.donePage(flowHisTask, page);

        IPage<HisTask> pageVo = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        BeanUtils.copyProperties(page,pageVo);
        pageVo.setRecords(page.getList());
        pageVo.setTotal(page.getTotal());
        return Result.success(pageVo);
    }

//    /**
//     * 分页待办任务列表
//     */
//    @GetMapping("/toDoPage")
//    public TableDataInfo toDoPage(FlowTask flowTask) {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Page<Task> page = Page.pageOf(pageDomain.getPageNum(), pageDomain.getPageSize());
//        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
//        List<String> roleKeyList = roles.stream().map(role ->
//                "role:" + role.getRoleId().toString()).collect(Collectors.toList());
//        flowTask.setPermissionList(roleKeyList);
//        page = taskService.toDoPage(flowTask, page);
//
//        page.getList().forEach(instance -> {
//            if (StringUtils.isNotBlank(instance.getApprover())) {
//                SysUser sysUser = userService.selectUserById(Long.valueOf(instance.getApprover()));
//                if (StringUtils.isNotNull(sysUser)) {
//                    instance.setApprover(sysUser.getNickName());
//                }
//            }
//        });
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(page.getList());
//        rspData.setTotal(page.getTotal());
//        return rspData;
//    }

//    /**
//     * 分页已办任务列表
//     */
//    @PreAuthorize("@ss.hasPermi('flow:execute:donePage')")
//    @GetMapping("/donePage")
//    public TableDataInfo donePage(FlowHisTask flowHisTask) {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Page<HisTask> page = Page.pageOf(pageDomain.getPageNum(), pageDomain.getPageSize());
//        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
//        List<String> roleKeyList = roles.stream().map(role ->
//                "role:" + role.getRoleId().toString()).collect(Collectors.toList());
//        flowHisTask.setPermissionList(roleKeyList);
//        page = hisTaskService.donePage(flowHisTask, page);
//        page.getList().forEach(hisTask -> {
//            if (StringUtils.isNotBlank(hisTask.getApprover())) {
//                SysUser sysUser = userService.selectUserById(Long.valueOf(hisTask.getApprover()));
//                if (StringUtils.isNotNull(sysUser)) {
//                    hisTask.setApprover(sysUser.getNickName());
//                }
//            }
//        });
//        TableDataInfo rspData = new TableDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(page.getList());
//        rspData.setTotal(page.getTotal());
//        return rspData;
//    }

    /**
     * 查询已办任务历史记录
     */
    @GetMapping("/doneList/{instanceId}")
    @Operation(summary = "查询已办任务历史记录")
    public Result<List<HisTask>> doneList(@PathVariable("instanceId") Long instanceId) {
        //List<HisTask> flowHisTasks = hisTaskService.getByInsIds(instanceId);
//        flowHisTasks.forEach(hisTask -> {
//            if (StringUtils.isNotBlank(hisTask.getApprover())) {
//                SysUser sysUser = userService.selectUserById(Long.valueOf(hisTask.getApprover()));
//                if (StringUtils.isNotNull(sysUser)) {
//                    hisTask.setApprover(sysUser.getNickName());
//                }
//                hisTask.setApprover(sysUser.getNickName());
//            }
//        });
        return null;
    }

    /**
     * 根据节点code查询代表任务
     * @param nodeCode
     * @return
     */
    @GetMapping("/flowTaskById/{nodeCode}/{instanceId}")
    @Operation(summary = "根据节点code查询代表任务")
    public Result<Task> flowTaskById(@PathVariable("nodeCode") String nodeCode, @PathVariable("instanceId") Long instanceId) {
        return Result.success(taskService.getOne(new FlowTask().setNodeCode(nodeCode).setInstanceId(instanceId)));
    }

    /**
     * 查询跳转任意节点列表
     */
    @GetMapping("/anyNodeList/{instanceId}/{nodeCode}")
    @Operation(summary = "查询跳转任意节点列表")
    public Result<List<Node>> anyNodeList(@PathVariable("instanceId") Long instanceId
            , @PathVariable("nodeCode") String nodeCode) {
        Instance instance = insService.getById(instanceId);
        Node node = nodeService.getOne(FlowFactory.newNode().setDefinitionId(instance.getDefinitionId()).setNodeCode(nodeCode));
        if (FlowCons.SKIP_ANY_N.equals(node.getSkipAnyNode())) {
            return Result.success(new ArrayList<>());
        }
        List<Node> nodeList = nodeService.list(FlowFactory.newNode().setDefinitionId(instance.getDefinitionId()));
        nodeList = StreamUtils.filter(nodeList, n -> (NodeType.isBetween(n.getNodeType())
                || NodeType.isEnd(n.getNodeType()))&& !nodeCode.equals(n.getNodeCode()));
        return Result.success(nodeList);
    }
}
