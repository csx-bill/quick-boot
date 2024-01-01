package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysDept;

import java.util.List;

public interface ISysDeptService extends IService<SysDept> {
    List<SysDept> getDeptTree();
    List<SysDept> getSysDeptTree(List<SysDept> sysDeptList);
    List<SysDept> getChildren(SysDept root,List<SysDept> sysDeptList);
}
