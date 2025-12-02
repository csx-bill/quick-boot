package com.quick.boot.modules.common.config.magic;

import cn.dev33.satoken.session.SaTerminalInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.quick.boot.modules.common.constant.CommonConstants;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.modules.db.inteceptor.NamedTableInterceptor;
import org.ssssssss.magicapi.modules.db.model.SqlMode;
import org.ssssssss.magicapi.modules.db.table.NamedTable;

import java.time.LocalDateTime;

@Component
public class CustomTableInterceptor implements NamedTableInterceptor {

	/*
	 * 执行单表操作之前
	 */
	@Override
	public void preHandle(SqlMode sqlMode, NamedTable namedTable) {
        SaTerminalInfo info = StpUtil.getTerminalInfo();
        String username = "noLogin";
        if (info != null) {
            username = (String) info.getExtra("username");
        }
        LocalDateTime now = LocalDateTime.now();

		if (sqlMode == SqlMode.INSERT) {
            namedTable.column("id", IdWorker.getId());
            namedTable.column("create_time", now);
            namedTable.column("update_time", now);
            namedTable.column("create_by", username);
            namedTable.column("update_by", username);
            namedTable.column("del_flag", CommonConstants.STATUS_NORMAL);
		} else if (sqlMode == SqlMode.UPDATE) {
            namedTable.column("update_time", now);
            namedTable.column("update_by", username);
        }
	}
}