package com.quik.boot.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quik.boot.modules.system.entity.SysUsers;
import com.quik.boot.modules.system.mapper.SysProjectsUsersMapper;
import com.quik.boot.modules.system.mapper.SysUsersMapper;
import com.quik.boot.modules.system.req.UsersListParams;
import com.quik.boot.modules.system.service.SysUsersService;
import com.quik.boot.modules.system.vo.SysUsersVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysUsersServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUsersService {

    private final SysProjectsUsersMapper sysProjectsUsersMapper;

    @Override
    public List<SysUsersVO> usersList(UsersListParams params) {
        LambdaQueryWrapper<SysUsers> wrapper = Wrappers.<SysUsers>lambdaQuery()
                .eq(Objects.nonNull(params.getUserId()), SysUsers::getId, params.getUserId())
                .like(StringUtils.hasText(params.getUsername()), SysUsers::getUsername, params.getUsername());

        return list(wrapper).stream()
                .map(sysUser -> {
                    SysUsersVO vo = new SysUsersVO();
                    BeanUtils.copyProperties(sysUser, vo); // 属性复制
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SysUsers> findNotInProjectUsers(Long projectId) {
        return sysProjectsUsersMapper.findNotInProjectUsers(projectId);
    }

}
