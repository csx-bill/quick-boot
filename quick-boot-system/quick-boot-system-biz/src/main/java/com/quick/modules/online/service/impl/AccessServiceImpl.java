package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.mapper.AccessMapper;
import com.quick.modules.online.service.IAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {
}
