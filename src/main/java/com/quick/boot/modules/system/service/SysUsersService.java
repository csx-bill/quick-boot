package com.quick.boot.modules.system.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.boot.modules.common.vo.UserInfo;
import com.quick.boot.modules.system.entity.SysUsers;
import com.quick.boot.modules.system.req.UsersListParams;
import com.quick.boot.modules.system.vo.SysUsersVO;

import java.util.List;

public interface SysUsersService extends IService<SysUsers> {

    List<SysUsersVO> usersList(UsersListParams params);

    List<SysUsers> findNotInProjectUsers(Long projectId);

    UserInfo findUserInfo();
}
