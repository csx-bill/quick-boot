package com.quick.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.system.entity.SysDept;
import com.quick.system.mapper.SysDeptMapper;
import com.quick.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> getDeptTree() {
        // 组装部门树
        return this.getSysDeptTree(super.list());
    }

    @Override
    public List<SysDept> getSysDeptTree(List<SysDept> sysDeptList) {
        //获取父节点
        List<SysDept> treeVOList = sysDeptList.stream().filter(m -> "0".equals(m.getParentId())).map(
                (m) -> {
                    m.setChildren(getChildren(m, sysDeptList));
                    return m;
                }
        ).collect(Collectors.toList());

        return treeVOList;
    }

    @Override
    public List<SysDept> getChildren(SysDept root, List<SysDept> sysDeptList) {
        List<SysDept> children = sysDeptList.stream().filter(m -> {
            return Objects.equals(m.getParentId(), root.getId());
        }).map(
                (m) -> {
                    m.setChildren(getChildren(m, sysDeptList));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }
}
