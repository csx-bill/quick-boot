package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.SysTableColumn;
import com.quick.modules.online.mapper.SysTableColumnMapper;
import com.quick.modules.online.service.ISysTableColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysTableColumnServiceImpl extends ServiceImpl<SysTableColumnMapper, SysTableColumn> implements ISysTableColumnService {

}
