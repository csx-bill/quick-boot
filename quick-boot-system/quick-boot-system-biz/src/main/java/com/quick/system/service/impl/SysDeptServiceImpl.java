package com.quick.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.system.entity.SysDept;
import com.quick.system.mapper.SysDeptMapper;
import com.quick.system.service.ISysDeptService;
import com.quick.system.vo.SysDeptTreeVO;
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
    public List<SysDeptTreeVO> getDeptTree() {
        // 组装部门树
        return this.getSysDeptTree(super.list());
    }

    @Override
    public List<SysDeptTreeVO> getSysDeptTree(List<SysDept> sysDeptList) {
        //集合拷贝
        List<SysDeptTreeVO> list = JSON.parseArray(JSON.toJSONString(sysDeptList), SysDeptTreeVO.class);

        //获取父节点
        List<SysDeptTreeVO> treeVOList = list.stream().filter(m -> "0".equals(m.getParentId())).map(
                (m) -> {
                    m.setChildren(getChildren(m, list));
                    return m;
                }
        ).collect(Collectors.toList());

        return treeVOList;
    }

    @Override
    public List<SysDeptTreeVO> getChildren(SysDeptTreeVO root, List<SysDeptTreeVO> sysDeptList) {
        List<SysDeptTreeVO> children = sysDeptList.stream().filter(m -> {
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
