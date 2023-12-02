/*Copyright ©2022 APIJSON(https://github.com/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.router;

import apijson.NotNull;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;


/**启动入口 Application 基类
 * 右键这个类 > Run As > Java Application
 * @author Lemon
 */
public class APIJSONRouterApplication extends APIJSONApplication {
	public static final String TAG = "APIJSONRouterApplication";

	/**初始化，加载所有配置并校验
	 * @return 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		init(true, DEFAULT_APIJSON_CREATOR);
	}
	/**初始化，加载所有配置并校验
	 * @param shutdownWhenServerError 
	 * @return 
	 * @throws Exception
	 */
	public static void init(boolean shutdownWhenServerError) throws Exception {
		init(shutdownWhenServerError, DEFAULT_APIJSON_CREATOR);
	}
	/**初始化，加载所有配置并校验
	 * @param creator 
	 * @return 
	 * @throws Exception
	 */
	public static <T extends Object> void init(@NotNull APIJSONCreator<T> creator) throws Exception {
		init(true, creator);
	}
	/**初始化，加载所有配置并校验
	 * @param shutdownWhenServerError 
	 * @param creator 
	 * @return 
	 * @throws Exception
	 */
	public static <T extends Object> void init(boolean shutdownWhenServerError, @NotNull APIJSONCreator<T> creator) throws Exception {
		//	避免多个插件重复调用这句	APIJSONApplication.init(shutdownWhenServerError, creator);
		System.out.println("\n\n\n\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON Router 开始启动 >>>>>>>>>>>>>>>>>>>>>>>>\n");


		System.out.println("\n\n\n开始初始化: Document 请求映射配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		try {
			APIJSONRouterVerifier.initDocument(shutdownWhenServerError, creator);
		}
		catch (Throwable e) {
			e.printStackTrace();
			if (shutdownWhenServerError) {
				onServerError("Document 请求映射配置 初始化失败！", shutdownWhenServerError);
			}
		}
		System.out.println("\n完成初始化: Document 请求映射配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		System.out.println("\n插件地址： https://github.com/APIJSON/apijson-router");
		System.out.println("\n\n<<<<<<<<<<<<<<<<<<<<<<<<< APIJSON Router 启动完成，试试调用类 RESTful API 吧 ^_^ >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

}
