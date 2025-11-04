package com.quik.boot.modules.system.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quik.boot.modules.system.entity.SysUsers;
import com.quik.boot.modules.system.req.UsersListParams;
import com.quik.boot.modules.system.vo.SysUsersVO;

import java.util.List;

public interface SysUsersService extends IService<SysUsers> {

    List<SysUsersVO> usersList(UsersListParams params);
}
