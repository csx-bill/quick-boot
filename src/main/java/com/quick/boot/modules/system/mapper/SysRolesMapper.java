package com.quick.boot.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.boot.modules.system.entity.SysRoles;
import com.quick.boot.modules.system.vo.SysRolesVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysRolesMapper extends BaseMapper<SysRoles> {
    List<SysRolesVO> batchUserProjectRoleList(@Param("projectId") Long projectId, @Param("userIds") List<Long> userIds);

}
