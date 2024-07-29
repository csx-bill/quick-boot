package com.quick.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.online.dto.OnlCgformHeadDetailsVO;
import com.quick.online.dto.OnlCgformHeadVO;
import com.quick.online.dto.SyncedTableInfoDTO;
import com.quick.online.entity.Access;
import com.quick.online.entity.OnlCgformField;
import com.quick.online.entity.OnlCgformHead;
import com.quick.online.entity.Request;
import com.quick.online.mapper.OnlCgformHeadMapper;
import com.quick.online.service.IAccessService;
import com.quick.online.service.IOnlCgformFieldService;
import com.quick.online.service.IOnlCgformHeadService;
import com.quick.online.service.IRequestService;
import com.quick.online.util.APIJSONRequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlCgformHeadServiceImpl extends ServiceImpl<OnlCgformHeadMapper, OnlCgformHead> implements IOnlCgformHeadService {

    private final IOnlCgformFieldService onlCgformFieldService;

    private final IAccessService accessService;
    private final IRequestService requestService;
    @Override
    public OnlCgformHeadDetailsVO getOnlCgformHeadDetails(Long id) {
        OnlCgformHeadDetailsVO vo = new OnlCgformHeadDetailsVO();
        // 主表
        OnlCgformHead onlCgformHead = getById(id);
        if(Objects.nonNull(onlCgformHead)){
            List<OnlCgformField> list = onlCgformFieldService.list(new LambdaQueryWrapper<OnlCgformField>()
                    .eq(OnlCgformField::getCgformHeadId, id));

            BeanUtils.copyProperties(onlCgformHead,vo);
            vo.setOnlCgformFieldList(list);

            // 附表
            String subTableStr = onlCgformHead.getSubTableStr();
            if(StringUtils.hasText(subTableStr)){

                List<OnlCgformHead> subOnlCgformHeadList = list(new LambdaQueryWrapper<OnlCgformHead>()
                        .in(OnlCgformHead::getTableName, subTableStr.split(","))
                        .orderByAsc(OnlCgformHead::getTabOrderNum));

                List<OnlCgformHeadDetailsVO> subOnlCgformHeadDetailsVOList = new ArrayList<>();
                for (OnlCgformHead subHead : subOnlCgformHeadList) {
                    OnlCgformHeadDetailsVO subVo = new OnlCgformHeadDetailsVO();
                    BeanUtils.copyProperties(subHead,subVo);
                    List<OnlCgformField> subFieldList = onlCgformFieldService.list(new LambdaQueryWrapper<OnlCgformField>()
                            .eq(OnlCgformField::getCgformHeadId, subHead.getId()));
                    subVo.setOnlCgformFieldList(subFieldList);
                    subOnlCgformHeadDetailsVOList.add(subVo);
                }
                vo.setSubOnlCgformHeadDetailsVOList(subOnlCgformHeadDetailsVOList);
            }
            // 允许多数据源，存在表名重复问题，所以必须带库名查询
            Access access = accessService.getOne(new LambdaQueryWrapper<Access>()
                    .eq(Access::getName, onlCgformHead.getTableName())
                    .eq(Access::getSchema,onlCgformHead.getSchema())
            );
            vo.setAlias(access.getAlias());
        }

        return vo;
    }

    @Override
    public OnlCgformHeadVO details(Long id) {
        OnlCgformHeadVO vo = new OnlCgformHeadVO();
        OnlCgformHead onlCgformHead = getById(id);
        List<OnlCgformField> list = onlCgformFieldService.list(new LambdaQueryWrapper<OnlCgformField>()
                .eq(OnlCgformField::getCgformHeadId, id));
        BeanUtils.copyProperties(onlCgformHead,vo);
        vo.setOnlCgformFieldList(list);

        // 允许多数据源，存在表名重复问题，所以必须带库名查询
        Access access = accessService.getOne(new LambdaQueryWrapper<Access>()
                .eq(Access::getName, onlCgformHead.getTableName())
                .eq(Access::getSchema,onlCgformHead.getSchema())
        );
        if(Objects.nonNull(access)){
            vo.setDeletedKey(access.getDeletedKey());
            vo.setDeletedValue(access.getDeletedValue());
            vo.setNotDeletedValue(access.getNotDeletedValue());
        }
        return vo;
    }

    @Override
    public OnlCgformHead selectByTableName(String tableName, String dsName) {
        return baseMapper.selectByTableName(tableName,dsName);
    }


    /**
     * @Transactional (不能添加事务)
     * https://www.kancloud.cn/tracy5546/dynamic-datasource/2304733#1spring_2
     * @param syncedTableInfoDTO
     * @return
     */
    @Override
    public Boolean syncedTableInfo(SyncedTableInfoDTO syncedTableInfoDTO) {
        String dsName = syncedTableInfoDTO.getDsName();
        String tableNamesStr = syncedTableInfoDTO.getTableNames();
        String[] tableNames = tableNamesStr.split(",");
        for (String tableName : tableNames) {
            OnlCgformHead onlCgformHead = selectByTableName(tableName, dsName);
            onlCgformHead.setIsCheckbox(CommonConstant.Y);
            onlCgformHead.setIsPage(CommonConstant.Y);
            onlCgformHead.setIsTree(CommonConstant.N);
            onlCgformHead.setIsDbSynch(CommonConstant.Y);
            onlCgformHead.setTableVersion(1);
            onlCgformHead.setTableType(0);
            save(onlCgformHead);

            List<OnlCgformField> onlCgformFieldList = onlCgformFieldService.selectByTableName(tableName, dsName);
            for (OnlCgformField onlCgformField : onlCgformFieldList) {
                onlCgformField.setCgformHeadId(onlCgformHead.getId());
                onlCgformField.setIsShowForm(1);
                onlCgformField.setIsShowList(1);
            }
            onlCgformFieldService.saveBatch(onlCgformFieldList);

            // 添加 APIJSON 配置表
            // 转换为小驼峰命名法
            String camelCase = StrUtil.toCamelCase(tableName);
            // 将第一个字母大写，得到大驼峰命名法
            String alias = StrUtil.upperFirst(camelCase);
            Access access = Access.builder()
                    .schema(dsName)
                    .alias(alias)
                    .name(onlCgformHead.getTableName())
                    .detail(onlCgformHead.getTableTxt())
                    .debug(0)
                    .date(LocalDateTime.now()).build();
            accessService.save(access);

            // 构建APISJON crud 接口参数校验
            ArrayList<Request> crud = new ArrayList<>();

            String structure = "{}";
            Request save = APIJSONRequestUtils.builderRequest(CommonConstant.SAVE_MSG, RequestMethod.POST.name(), alias, alias, structure);
            Request update = APIJSONRequestUtils.builderRequest(CommonConstant.UPDATE_BY_ID_MSG, RequestMethod.PUT.name(), alias, alias, structure);
            Request delete = APIJSONRequestUtils.builderRequest(CommonConstant.REMOVE_BY_ID_MSG, RequestMethod.DELETE.name(), alias, alias, structure);
            crud.add(save);
            crud.add(update);
            crud.add(delete);
            requestService.saveBatch(crud);
        }
        // 刷新APISJON 配置(不执行这个方法,在线接口无法调用该表的操作)
        accessService.reload(null);

        return true;
    }

    @Override
    public OnlCgformHeadVO bizSave(OnlCgformHeadVO saveDTO) {
        // 切换目标数据源
        DynamicDataSourceContextHolder.push(saveDTO.getSchema());
        List<OnlCgformField> onlCgformFieldList = saveDTO.getOnlCgformFieldList();

        // 自动建表
//        if (onlCgformFieldList == null || onlCgformFieldList.isEmpty()) {
//            throw new RuntimeException("自动创建表[" + tableInfo.getName() + "]中至少需要一个字段");
//        }
//        if (!TableModelEnum.CREATE.name().toLowerCase().equals(tableInfo.getModel())) {
//            throw new RuntimeException("自动创建表[" + tableInfo.getModel() + "]模式的处理未找到,请检查");
//        }
//        try {
//            AnylineService service = ServiceProxy.service();
//            // 如果已存在，删除重键
//            Table table = service.metadata().table(tableInfo.getName(), false);
//            if (null != table)
//                service.ddl().drop(table);
//            // 执行建表SQL
//            service.ddl().create(tableInfo);
//            log.info("自动创建表处理完成!");
//        }
//        catch (Exception e) {
//            throw new RuntimeException("自动创建表异常", e);
//        }


        OnlCgformHead onlCgformHead = new OnlCgformHead();
        BeanUtils.copyProperties(saveDTO,onlCgformHead);
        save(onlCgformHead);
        for (OnlCgformField onlCgformField : onlCgformFieldList) {
            onlCgformField.setCgformHeadId(onlCgformHead.getId());
        }
        onlCgformFieldService.saveBatch(onlCgformFieldList);

        saveDTO.setId(onlCgformHead.getId());
        return saveDTO;
    }

}
