package com.quick.online.util;

import apijson.JSONObject;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONFunctionParser;
import apijson.framework.APIJSONVerifier;
import apijson.orm.AbstractVerifier;
import apijson.router.APIJSONRouterApplication;
import apijson.router.APIJSONRouterVerifier;

public class ApijsonInitUtil {
	public static boolean IS_INIT_SHUTDOWNWHENSERVERERROR = false;

	/***
	 * 全量初始化
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		// 默认只会初始化 100条数据
		APIJSONApplication.init(IS_INIT_SHUTDOWNWHENSERVERERROR);

		// 需要将超过100条的记录进行增量初始化
		// 通过总条数判断是否已经加载完成
		initAccess();
		initFunction();
		initRequest();

		APIJSONRouterApplication.init();
		initDocument();
	}


	private static void initAccess() throws Exception {
		int accessMapCount = AbstractVerifier.ACCESS_MAP.size();
		int eveNum = 1;
		while (true) {
			JSONObject table = new JSONObject();
			table.put("id{}", ">=" + (100 * eveNum));
			table.setOrder("id+");
			APIJSONVerifier.initAccess(IS_INIT_SHUTDOWNWHENSERVERERROR, APIJSONApplication.DEFAULT_APIJSON_CREATOR,
					table);
			int tmp_accessMapCount = AbstractVerifier.ACCESS_MAP.size();
			if (accessMapCount == tmp_accessMapCount) {
				break;
			} else {
				accessMapCount = tmp_accessMapCount;
			}
			eveNum++;
		}
	}

	private static void initFunction() throws Exception {
		int accessMapCount = APIJSONFunctionParser.FUNCTION_MAP.size();
		int eveNum = 1;
		while (true) {
			JSONObject table = new JSONObject();
			table.put("id{}", ">=" + (30 * eveNum));
			table.setOrder("id+");
			APIJSONFunctionParser.init(IS_INIT_SHUTDOWNWHENSERVERERROR, APIJSONApplication.DEFAULT_APIJSON_CREATOR,
					table);
			int tmp_accessMapCount = APIJSONFunctionParser.FUNCTION_MAP.size();
			if (accessMapCount == tmp_accessMapCount) {
				break;
			} else {
				accessMapCount = tmp_accessMapCount;
			}
			eveNum++;
		}
	}

	public static void initDocument() throws Exception {
		int documentMapCount = APIJSONRouterVerifier.DOCUMENT_MAP.size();
		int eveNum = 1;
		while (true) {
			JSONObject table = new JSONObject();
			table.put("id{}", ">=" + (100 * eveNum));
			table.setOrder("id+");
			APIJSONRouterVerifier.initDocument(IS_INIT_SHUTDOWNWHENSERVERERROR,
					APIJSONApplication.DEFAULT_APIJSON_CREATOR, table);
			int tmp_accessMapCount = APIJSONRouterVerifier.DOCUMENT_MAP.size();
			if (documentMapCount == tmp_accessMapCount) {
				break;
			} else {
				documentMapCount = tmp_accessMapCount;
			}
			eveNum++;
		}
	}

	private static void initRequest() throws Exception {
		int accessMapCount = APIJSONVerifier.REQUEST_MAP.size();
		int eveNum = 1;
		while (true) {
			JSONObject table = new JSONObject();
			table.put("id{}", ">=" + (100 * eveNum));
			table.setOrder("version-,id+");
			APIJSONVerifier.initRequest(IS_INIT_SHUTDOWNWHENSERVERERROR, APIJSONApplication.DEFAULT_APIJSON_CREATOR,
					table);
			int tmp_accessMapCount = APIJSONVerifier.REQUEST_MAP.size();
			if (accessMapCount == tmp_accessMapCount) {
				break;
			} else {
				accessMapCount = tmp_accessMapCount;
			}
			eveNum++;
		}
	}
}
