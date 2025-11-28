package com.quick.boot.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.system.entity.SysPages;
import com.quick.boot.modules.system.mapper.SysPagesMapper;
import com.quick.boot.modules.system.service.SysPagesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SysPagesServiceImpl extends ServiceImpl<SysPagesMapper, SysPages> implements SysPagesService {

}
