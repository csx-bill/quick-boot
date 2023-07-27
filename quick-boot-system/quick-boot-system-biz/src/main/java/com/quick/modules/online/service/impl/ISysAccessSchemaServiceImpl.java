package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.entity.Request;
import com.quick.modules.online.entity.SysAccessSchema;
import com.quick.modules.online.mapper.AccessMapper;
import com.quick.modules.online.mapper.RequestMapper;
import com.quick.modules.online.mapper.SysAccessSchemaMapper;
import com.quick.modules.online.service.ISysAccessSchemaService;
import com.quick.modules.util.AMISGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISysAccessSchemaServiceImpl extends ServiceImpl<SysAccessSchemaMapper, SysAccessSchema> implements ISysAccessSchemaService {

    private  final AccessMapper accessMapper;

    private final RequestMapper requestMapper;

    @Override
    public SysAccessSchema getSchema(Long accessId) {
        SysAccessSchema sysAccessSchema = this.getOne(new LambdaQueryWrapper<SysAccessSchema>().eq(SysAccessSchema::getAccessId, accessId));
        if(sysAccessSchema==null){
            // 初始化进去
            Access access = accessMapper.selectById(accessId);
            // 表字段信息
            List<Map<String, Object>> fieldDetails = accessMapper.getFieldDetails(access.getName());
            // 1.生成 amis crud json 配置
            String schema = AMISGeneratorUtils.generateAMISCrudSchema(access.getAlias(),fieldDetails);
            // 2.保存 schema
            sysAccessSchema = SysAccessSchema.builder()
                    .accessId(accessId)
                    .schema(schema)
                    .build();
            this.save(sysAccessSchema);

            // 初始化 crud 接口
            // 删除
            Request delete = Request.builder()
                    .tag(access.getAlias())
                    .structure("{\"NECESSARY\": \"id\"}")
                    .method("DELETE")
                    .detail("根据ID删除")
                    .date(LocalDateTime.now()).build();
            requestMapper.insert(delete);

            Request batchDelete = Request.builder()
                    .tag(access.getAlias())
                    .structure("{\"NECESSARY\": \"id{}\"}")
                    .method("DELETE")
                    .detail("批量ID删除")
                    .date(LocalDateTime.now()).build();
            requestMapper.insert(batchDelete);

            // 更新
            Request update = Request.builder()
                    .tag(access.getAlias())
                    .structure("{\"NECESSARY\": \"id\"}")
                    .method("PUT")
                    .detail("根据ID更新")
                    .date(LocalDateTime.now()).build();
            requestMapper.insert(update);

            // 新增
            Request post = Request.builder()
                    .tag(access.getAlias())
                    .structure("{}")
                    .method("POST")
                    .detail("新增")
                    .date(LocalDateTime.now()).build();
            requestMapper.insert(post);

        }
        return sysAccessSchema;
    }
}
