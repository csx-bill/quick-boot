package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysDept;
import com.quick.modules.system.vo.SysDeptTreeVO;

import java.util.List;

public interface ISysDeptService extends IService<SysDept> {
    List<SysDeptTreeVO> getDeptTree();
    List<SysDeptTreeVO> getSysDeptTree(List<SysDept> sysDeptList);
    List<SysDeptTreeVO> getChildren(SysDeptTreeVO root,List<SysDeptTreeVO> sysDeptList);
}
