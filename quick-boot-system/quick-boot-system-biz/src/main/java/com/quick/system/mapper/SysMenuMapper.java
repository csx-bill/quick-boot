package com.quick.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> queryByUser(@Param("userId") Long userId,@Param("menuTypes") List<String> menuTypes);

    /**
     * 根据角色id 查询按钮权限
     * @param roleId
     * @return
     */
    List<SysMenu> getUserRolePermission(@Param("roleId") Long roleId);
}
