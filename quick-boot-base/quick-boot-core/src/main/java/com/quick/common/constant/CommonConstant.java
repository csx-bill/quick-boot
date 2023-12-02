package com.quick.common.constant;

public interface CommonConstant {
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SUCCESS_CODE = 0;

    Integer FORBIDDEN = 403;
    String SUCCESS_MSG = "success";

    String Y = "Y";

    String N = "N";

    /**
     * 菜单类型
     */
    // 菜单
    String MENU = "MENU";
    // 目录
    String DIR = "DIR";
    // 按钮
    String BUTTON = "BUTTON";


    /**
     * 树结构顶级ID
     */
    String PARENTID = "0";


    /**
     * 数据表 基础字段 创建人 创建时间 更新人 更新时间
     */
    String CREATE_TIME = "create_time";
    String CREATE_BY = "create_by";
    String UPDATE_TIME = "update_time";
    String UPDATE_BY = "update_by";

    String CREATETIME = "createTime";
    String CREATEBY = "createBy";
    String UPDATETIME = "updateTime";
    String UPDATEBY = "updateBy";

    String DEL_FLAG = "del_flag";

    /**
     * 字典解析后缀
     */
    String DICT_TEXT_SUFFIX = "Text";
    String _DICT_TEXT_SUFFIX = "_text";


    String COLUMN_QUERY_TYPE = "COLUMN_QUERY_TYPE";
    String COLUMN_SHOW_TYPE = "COLUMN_SHOW_TYPE";
    String COLUMN_TABLE_JOIN = "COLUMN_TABLE_JOIN";

    /**
     * 系统服务名
     */
    String SERVICE_SYSTEM = "quick-boot-system";
    String SERVICE_OAUTH2 = "quick-boot-oauth2";
    String SERVICE_GATEWAY = "quick-boot-gateway";
    String SERVICE_ONLINE = "quick-boot-online";

    /**
     * amis
     */
    String U = "u:";

    /**
     * 增删改查权限
     */
    String ADD = "add";
    String VIEW = "view";
    String UPDATE = "update";
    String DELETE = "delete";
    String BATCHDELETE = "batchDelete";
    String BATCHUPDATE = "batchUpdate";

    /**
     * APIJSON 请求方式
     */
    String GET = "get";
    String POST = "post";
    String PUT = "put";

    //String DELETE = "delete";

    /**
     *  接口后缀
     */
    String PAGE = "Page";
    String GET_BY_ID = "GetById";
    String SAVE = "Save";
    String UPDATE_BY_ID = "UpdateById";
    String UPDATE_BATCH_BY_ID = "UpdateBatchById";
    String REMOVE_BY_ID = "RemoveById";
    String REMOVE_BATCH_BY_IDS = "RemoveBatchByIds";

    /**
     *  接口描述
     */
    String PAGE_MSG = "分页查询";
    String GET_BY_ID_MSG = "根据ID查询";
    String SAVE_MSG = "保存";
    String UPDATE_BY_ID_MSG = "根据ID更新";
    String UPDATE_BATCH_BY_ID_MSG = "根据ID批量更新";
    String REMOVE_BY_ID_MSG = "根据ID删除";
    String REMOVE_BATCH_BY_IDS_MSG = "根据ID批量删除";

}
