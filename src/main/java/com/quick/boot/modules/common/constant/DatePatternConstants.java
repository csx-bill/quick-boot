package com.quick.boot.modules.common.constant;

import java.time.format.DateTimeFormatter;

public interface DatePatternConstants {

    String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    DateTimeFormatter DTF_DIRS = DateTimeFormatter.ofPattern("/yyyy/MM/dd/");
}
