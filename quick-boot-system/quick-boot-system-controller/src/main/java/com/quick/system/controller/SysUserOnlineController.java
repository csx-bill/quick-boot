package com.quick.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.system.entity.SysUser;
import com.quick.system.req.SysUserPageParam;
import com.quick.system.service.ISysUserService;
import com.quick.system.vo.SysUserOnlineVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/userOnline")
@RequiredArgsConstructor
@Tag(name = "在线用户")
public class SysUserOnlineController {
    private final ISysUserService sysUserService;

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询在线用户", description = "分页查询在线用户")
    public Result<IPage<SysUserOnlineVO>> page(@RequestBody SysUserPageParam pageParam) {

        Page page = pageParam.buildPage();

        List<String> tokenList = StpUtil.searchTokenValue("",0 ,-1, true);

        // 当前页数据
        List<String> currentList = getPage(tokenList,(int)page.getCurrent(),(int)page.getSize());

        List<SysUserOnlineVO> list = new ArrayList<>();
        for (String token : currentList) {
            Object userId = StpUtil.getLoginIdByToken(token.split(":")[3]);
            if(null!=userId){
                SysUser sysUser = sysUserService.getById(userId.toString());
                if(sysUser!=null){
                    SysUserOnlineVO vo = new SysUserOnlineVO();
                    vo.setUsername(sysUser.getUsername());
                    vo.setRealName(sysUser.getRealName());
                    vo.setToken(token.split(":")[3]);
                    list.add(vo);
                }
            }
        }

        IPage<SysUserOnlineVO> pageVo = new Page<>();
        BeanUtils.copyProperties(page,pageVo);
        pageVo.setRecords(list);
        pageVo.setTotal(tokenList.size());
        return Result.success(pageVo);
    }

    public List<String> getPage(List<String> tokenList, int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, tokenList.size());
        if (startIndex >= tokenList.size()) {
            return Collections.emptyList();
        } else if (endIndex > tokenList.size()) {
            return tokenList.subList(startIndex, tokenList.size());
        }
        return tokenList.subList(startIndex, endIndex);
    }
}
