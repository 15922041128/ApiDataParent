package org.pbccrc.api.base.util;

public class Constants {
	
	// pdf导出目录
	public static final String PDF_FILE_PATH = PropertiesUtil.getStringByKey("pdfFilePath");
	// portal访问路径
	public static final String WEB_URL = PropertiesUtil.getStringByKey("webUrl");
	// 批量报送模板文件
	public static final String BATCH_ADD_TEMPLATE_FILE = "/files/template/addTemplate.xlsm";
	// 批量查询模板文件
	public static final String BATCH_QUERY_TEMPLATE_FILE = "/files/template/queryTemplate.xlsx";
	
	public static final String STR_ZERO = "0";
	public static final String STR_ONE = "1";
	
	public static final int COUNT_MAX = -1;
	
	public static final String BLANK = "";
	public static final String POINT = ".";
	public static final String UNDERLINE = "_";
	public static final String CONNECTOR_LINE = "-";
	public static final String COMMA = ",";
	public static final String AND = " and ";
	public static final String EQUAL = "=";
	public static final String SINGLE_QUOTES = "'";
	public static final String SPACE = " ";
	public static final String ENTER = "\n";
	public static final String COLON = ":";
	
	public static final String STR_NULL = "null";
	
	public static final String URL_CONNECTOR = "?";
	public static final String URL_PARAM_CONNECTOR = "&";
	public static final String URL_EQUAL = "=";
	
	public static final String RET_STAT_SUCCESS = "Y";
	public static final String RET_STAT_ERROR = "N";
	
	public static final String CURRENT_USER = "currentUser";
	
	public static final String PACKAGE_BIZ_IMP_QUERY = "org.pbccrc.api.core.service.impl";
	public static final String QUERY_API_SINGLE = "QueryApiSingle";
	public static final String QUERY_API_MULTIPLE = "QueryApiMultiple";
	
	// 文件类型
	public static final String FILE_TYPE_PDF = ".pdf";
	
	// pdf导出目录
	public static final String FILE_DOWNLOAD_SXR_PDF = "/files/download/sxr/pdf";
	// 上传错误文件目录
	public static final String UPLOAD_ERROR_FILE = "/files/download/uploadError";
	// 批量查询文件目录
	public static final String BATCH_QUERY_FILE = "/files/download/batchQuery";
	// 批量上传文件目录
	public static final String BATCH_ADD_FILE = "/files/download/batchAdd";
	
	/** 返回code */
	public static final String RET_CODE_SUCCESS = "success";
	public static final String RET_CODE_ERROR = "error";
	// return code type
	public static final String RET_CODE_TYPE_CONTINUE = "continue";
	public static final String RET_CODE_TYPE_BREAK = "break";
	
	/** 加密方式 */
	// 不加密
	public static final String ENCRYPT_TYPE_NORMAL = "normal";
	// 全联加密
	public static final String ENCRYPT_TYPE_QL = "ql";
	
	/** 服务器文件相关 */
	public static final String FILE_PATH_BASE = "files";
	public static final String FILE_PATH_PHOTO = "photos";
	public static final String FILE_PATH_LICENSE = "license";
	
	/** url参数相关 */
	// apiKey
	public static final String HEAD_APIKEY = "apiKey";
	public static final String HEAD_USER_ID = "userID";
	// 参数类型
	public static final String PARAM_TYPE_HEAD = "head";
	public static final String PARAM_TYPE_URL = "url";
	public static final String PARAM_TYPE_JSON = "json";
	public static final String PARAM_TYPE_JSON_ARRAY = "jsonArray";
	public static final String PARAM_TYPE_CONSTANT = "constant";
	public static final String PARAM_TYPE_SERVICE = "service";
	public static final String PARAM_TYPE_APIKEY = "apiKey";
	public static final String PARAM_TYPE_FORMAT = "format";
	
	// 是否必须
	public static final String PARAM_REQUIRED_Y = "yes";
	public static final String PARAM_REQUIRED_N = "no";
	
	// 返回信息
	public static final String RETMSG_SUCCESS = "success";
	
	// 前缀
	// 唯一外部api
	public static final String PREFIX_SINGLE = "s";
	// 多个外部api
	public static final String PREFIX_MULTIPLE = "m";
	
	
	/** return content */
	// 正常返回
	public static final String ERR_NOERR = "0";
	public static final String RET_MSG_SUCCESS = "success";
	
	// 缺少apiKey
	public static final String ERR_MISSING_APIKEY = "100101";
	public static final String RET_MSG_MISSING_APIKEY = "缺少apiKey";
	
	// apiKey与用户不匹配
	public static final String ERR_APIKEY_USER_INVALID = "100102";
	public static final String RET_MSG_APIKEY_USER_INVALID = "apiKey与用户不匹配";
	
	// apiKey无效
	public static final String ERR_APIKEY_INVALID = "100103";
	public static final String RET_MSG_APIKEY_INVALID = "无效的apiKey";
	
	// url参数不匹配
	public static final String ERR_URL_INVALID = "100104";
	public static final String RET_MSG_URL_INVALID = "缺少参数 : ";
	
	// 查询次数达到上限
	public static final String ERR_CNT = "100105";
	public static final String RET_MSG_CNT = "查询次数达到上限";
	
	// 每日查询次数达到上限
	public static final String ERR_DAY_CNT = "100112";
	public static final String RET_MSG_DAY_CNT = "每日查询次数达到上限";
	
	// 服务器内部错误
	public static final String ERR_SERVER = "100106";
	public static final String RET_MSG_SERVER = "服务器内部错误";
	
	// 缺少userID
	public static final String ERR_MISSING_USER_ID = "100107";
	public static final String RET_MSG_MISSING_USER_ID = "缺少userID";
	
	// 查询接口不存在
	public static final String ERR_NO_SERVICE = "100108";
	public static final String RET_MSG_NO_SERVICE = "查询接口不存在";
	
	// 查询无结果
	public static final String ERR_NO_RESULT = "100109";
	public static final String RET_MSG_NO_RESULT = "查询无结果";
	
	// 余额或信用额不足
	public static final String ERR_BLANCE_NOT_ENOUGH = "100110";
	public static final String RET_MSG_BLANCE_NOT_ENOUGH = "余额或信用额不足";
	
	// IP受限
	public static final String ERR_RESTRICTED_IP = "100111";
	public static final String RET_MSG_RESTRICTED_IP = "IP受限,请联系管理员";
	
	// subParam格式必须为jsonArray
	public static final String ERR_PARAM_TYPE_JSON_SUBPARAM_FORMAT = "100113";
	public static final String RET_MSG_PARAM_TYPE_JSON_SUBPARAM_FORMAT = "subParam格式必须为json或jsonArray";
	
	// subParam格式必须为jsonArray
	public static final String ERR_PARAM_LENGTH_ZERO = "100114";
	public static final String RET_MSG_PARAM_LENGTH_ZERO = "参数长度不能为0 ： ";
	
	// 缺少service或service格式不正确
	public static final String ERR_SERVICE = "101001";
	public static final String RET_MSG_SERVICE = "缺少service或service格式不正确";
	
	// 参数格式不正确
	public static final String CODE_ERR_PARAM_FORMAT = "102007";
	public static final String CODE_ERR_PARAM_FORMAT_MSG = "参数格式不正确";
	
	// 请求参数错误
	public static final String CODE_ERR_REQ_PARAM = "102002";
	public static final String CODE_ERR_REQ_PARAM_MSG = "请求参数错误";

	// 查询成功
	public static final String CODE_ERR_SUCCESS = "102000";
	public static final String CODE_ERR_SUCCESS_MSG = "查询成功";
	// 查询失败
	public static final String CODE_ERR_FAIL = "102001";
	public static final String CODE_ERR_FAIL_MSG = "查询失败";
	
	// 参数必须为数字
	public static final String CODE_ERR_PARAM_NUMBER = "102008";
	public static final String CODE_ERR_PARAM_NUMBER_MSG = "参数必须为数字 : ";
	
	public static final String CODE_ERR_PARAM_NUMBER_ZERO = "102009";
	public static final String CODE_ERR_PARAM_NUMBER_ZERO_MSG = "参数不能为0 : ";
	
	/** 企业评分code */
	public static final String CODE_ERR_ENT_SOCE_FAIL = "103000";
	public static final String CODE_ERR_ENT_SOCE_FAIL_MSG = "type类型不正确";
	
	/** 发送短信用code */
	// 发送成功
	public static final String CODE_ERR_SEND_MESSAGE_SUCCESS = "202000";
	public static final String CODE_ERR_SEND_MESSAGE_SUCCESS_MSG = "发送成功";
	
	// 发送失败
	public static final String CODE_ERR_SEND_MESSAGE_FAIL = "202001";
	public static final String CODE_ERR_SEND_MESSAGE_FAIL_MSG = "发送失败";
	
	// 类型不正确
	public static final String CODE_ERR_SEND_MESSAGE_ERR = "202002";
	public static final String CODE_ERR_SEND_MESSAGE_ERR_MSG = "类型不正确";
	
	/**  table filed   */
	public static final String API_KEY = "apiKey";
	/** ldb_dishonest_info */
	// 被执行人身份证号码
//	public static final String LDB_DISHONEST_INFO_CARDNUM = "CARDNUM";
//	// 执行法院
//	public static final String LDB_DISHONEST_INFO_COURT_NAME = "COURT_NAME";
//	// 案号
//	public static final String LDB_DISHONEST_INFO_CASE_CODE = "CASE_CODE";
//	// 被执行人姓名
//	public static final String LDB_DISHONEST_INFO_INAME = "INAME";
//	// 执行情况
//	public static final String LDB_DISHONEST_INFO_PERFORMANCE = "PERFORMANCE";
//	// 发布时间
//	public static final String LDB_DISHONEST_INFO_PUBLISH_DATE = "PUBLISH_DATE";
//	// 省份名称
//	public static final String LDB_DISHONEST_INFO_AREA_NAME = "AREA_NAME";
//	// 职责
//	public static final String LDB_DISHONEST_INFO_DUTY = "DUTY";
//	// 失信被执行人行为具体情形
//	public static final String LDB_DISHONEST_INFO_DISREPUT_TYPE_NAME = "DISREPUT_TYPE_NAME";
	
	// person
	public static final String PERSON_ID = "id";
	
	/** conditionType */
	public static final String CONDITION_TYPE_NOTNULL = "notNull";
	public static final String CONDITION_TYPE_TEXT = "text";
	public static final String CONDITION_TYPE_REGEX = "regex";
	
	/** cost type */
	public static final String COST_TYPE_COUNT = "0";
	public static final String COST_TYPE_PRICE = "1";
	
	/** localApi return type */
	public static final String RETURN_TYPE_OBJECT = "1";
	public static final String RETURN_TYPE_ARRAY = "2";
	
	/** 本地访问用service */
	// 个人信用分
	public static final String SERVICE_L_SCORE2 = "l-score2";
	// 公积金 
	public static final String SERVICE_S_QGJJD = "s-qgjjd";
	// 涉诉信息(从执行公告中查询某人)
	public static final String SERVICE_S_UCACCIND_ZXGG = "s-ucaccindZXGG";
	// 涉诉信息(从失信公告中查询某人)
	public static final String SERVICE_S_UCACCIND_SXGG = "s-ucaccindSXGG";
	// 涉诉信息(从裁判文书中查询某人)
	public static final String SERVICE_S_UCACCIND_CPWS = "s-ucaccindCPWS";
	// 综合查询(人员基本信息)
	public static final String SERVICE_ZH_PERSON2 = "zh-person2";
	// 综合两标查询(居住信息)
	public static final String SERVICE_ZH_ADDRESS2 = "zh-address2";
	// 综合两标查询(职业信息)
	public static final String SERVICE_ZH_EMPLOYMENT2 = "zh-employment2";
	// 综合两标查询(信用卡信息)
	public static final String SERVICE_ZH_CREDITCARD2 = "zh-creditcard2";
	// 综合两标查询(信用卡信息)
	public static final String SERVICE_ZH_CREDITCARD_ALL2 = "zh-creditcardAll2";
	// 综合两标查询(贷款信息)
	public static final String SERVICE_ZH_LOAN2 = "zh-loan2";
	// 综合两标查询(担保信息)
	public static final String SERVICE_ZH_GUARANTEE2 = "zh-guarantee2";
	
	/** 本地访问用url(local DB) */
	// 失信人被执行信息
	public static final String URL_LDB_GETSXR = "/ApiData/r/ldb/getSxr";
	
	/** 本地访问用service(local DB) */
	// 授信信息
	public static final String SERVICE_LDB_CREDIT = "credit";
	
	/** remote url */
	// 行为评分
	public static final String REMOTE_URL_SCORE = "http://www.qilingyz.com/api.php?m=open.score";
	
	/** 查询参数项 */
	// 人员基本信息
	public static final String ITEM_PERSON = "person";
	// 居住信息
	public static final String ITEM_ADDRESS = "address";
	// 职业信息
	public static final String ITEM_EMPLOYMENT = "employment";
	// 信用卡信息
	public static final String ITEM_CREDITCARD = "creditCard";
	// 贷款信息
	public static final String ITEM_LOAN = "loan";
	// 担保信息
	public static final String ITEM_GUARANTEE = "guarantee";
	// 公积金信息
	public static final String ITEM_GGJ = "ggj";
	// 涉法涉诉信息
	public static final String ITEM_SFSS = "sfss";
	// 失信人被执行信息
	public static final String ITEM_SXR = "sxr";
	
	/** 参数相关 */
	// 是否成功-是
	public static final String IS_SUCCESS_TRUE = "true";
	// 是否成功-否
	public static final String IS_SUCCESS_FALSE = "false";
	// 是否计费-是
	public static final String IS_COST_TRUE = "true";
	// 是否计费-否
	public static final String IS_COST_FALSE = "false";
	// 数据来源-本地
	public static final String DATA_FROM_LOCAL = "local";
	// 数据来源-新颜
	public static final String DATA_FROM_XINYAN = "xinyan";
	// 数据来源-qilingyz
	public static final String DATA_FROM_QILINGYZ = "qilingyz";
	// 数据来源-全联
	public static final String DATA_FROM_QL = "ql";
	// 数据来源-唯品会
	public static final String DATA_FROM_VIP = "vip";
	// 数据来源-凭安
	public static final String DATA_FROM_PA = "pa";
	// 数据来源-葫芦数据
	public static final String DATA_FROM_HULU = "hulu";
	// 数据来源-信智上
	public static final String DATA_FROM_XINZHISHANG = "xinzhishang";
	// 数据来源-创蓝
	public static final String DATA_FROM_CL = "cl";
	// 数据库类型-oracle
//	public static final String DATABASE_TYPE_ORACLE = "oracle";
	
	/** apiID */
	// 身份证认证
//	public static final String API_ID_SFZRZ = "9999";
	public static final String API_SERVICE_SFZRZ = "sf-sfz";
	// 页面pdf
	public static final String API_ID_PAGE_PDF = "99999999999";
	// 银行卡三/四要素认证
	public static final String API_ID_BANK_CARD_AUTH = "99999999998";
	// 信贷模型
	public static final String API_ID_CREDIT_MODEL = "99999999997";
	// 鹰泽个人信用评分
//	public static final String API_ID_YINGZE_SCORE = "99999999996";
	public static final String API_SERVICE_YINGZE_SCORE = "yzScore";
	// 鹰泽个人信用评分TRI
//	public static final String API_ID_YINGZE_SCORE_TRI = "99999999995";
	public static final String API_SERVICE_YINGZE_SCORE_TRI = "yzScoreTri";
	// 借贷需求用户查询
	public static final String API_ID_YINGZE_DATA_QUERY = "99999999994";
	// 根据两标查询电话号码
//	public static final String API_ID_GET_TEL = "99999999993";
	public static final String API_SERVICE_GET_TEL = "l-getTel";
	// 鹰泽个人信用评分(param)
//	public static final String API_ID_YINGZE_SCORE_PARAM = "99999999992";
	public static final String API_SERVICE_YINGZE_SCORE_PARAM = "yzScoreParam";
	// 查询白名单(955钱包)
//	public static final String API_ID_GET_WHITE_LIST = "99999999991";
	public static final String API_SERVICE_GET_WHITE_LIST = "whiteList";
	
	// 企业评分
//	public static final String API_ID_ENT_SCORE = "99999999987";
	public static final String API_SERVICE_ENT_SCORE = "entScore";

	// 短信营销接口(查询符合条件营销对象数量)
	public static final String API_ID_GET_MARKETEE_COUNT = "99999999989";
	// 短信营销接口(发送信息)
	public static final String API_ID_SEND_MESG = "99999999988";
	// 发送短信
	public static final String API_ID_SEND_MESSAGE = "99999999990";
	
	// 唯品会个人风险查询接口
//	public static final String API_ID_VIP_QUERYBLACKLIST = "99999999986";
	public static final String API_SERVICE_VIP_QUERYBLACKLIST = "vipQueryBlackList";
	
	// 凭安电话号码标记查询接口
//	public static final String API_ID_PA_PHONE_TAG = "99999999985";
	public static final String API_SERVICE_PA_PHONE_TAG = "paPhoneTag";
	
	// 凭安失信被执行人查询接口
//	public static final String API_ID_PA_SHIXIN = "99999999983";
	public static final String API_SERVICE_PA_SHIXIN = "paShixin";
	
	// 凭安逾期查询接口
//	public static final String API_ID_PA_OVERDUE = "99999999982";
	public static final String API_SERVICE_PA_OVERDUE = "paOverdue";
	
	// 凭安借贷查询接口
//	public static final String API_ID_PA_LOAN = "99999999981";
	public static final String API_SERVICE_PA_LOAN = "paLoan";
	
	// 凭安黑名单查询接口
//	public static final String API_ID_PA_BLACK_LIST = "99999999980";
	public static final String API_SERVICE_PA_BLACK_LIST = "paBlackList";
	
	// 凭安申请人属性查询接口
//	public static final String API_ID_PA_PHKJ_MODELER_SCORE = "99999999979";
	public static final String API_SERVICE_PA_PHKJ_MODELER_SCORE = "paScore";
	
	// 凭安综合查询接口
//	public static final String API_ID_PA_ZH = "99999999978";
	public static final String API_SERVICE_PA_ZH = "paZh";
	
	// 产品-反欺诈服务接口
//	public static final String API_ID_PRODUCT_ANTI_FRAUD = "99999999977";
	public static final String API_SERVICE_PRODUCT_ANTI_FRAUD = "fraud";
	
	// 产品-信用卡申请风控接口
//	public static final String API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL = "99999999976";
	public static final String API_SERVICE_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL = "rcarc";
	
	// 产品-信用卡申请风控接口-获取动态码
//	public static final String API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_GET_CODE = "99999999975";
	public static final String API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_GET_CODE = "20025";
		
	// 产品-信用卡申请风控接口-重置密码
//	public static final String API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_RESET_PASSWORD = "99999999974";
	public static final String API_ID_PRODUCT_REDIT_CARD_APPLY_RISK_CONTROL_RESET_PASSWORD = "20024";
	
	// 产品-新反欺诈服务接口
//	public static final String API_ID_PRODUCT_ANTI_NEW_FRAUD = "99999999973";
	public static final String API_SERVICE_PRODUCT_ANTI_NEW_FRAUD = "fraud2";
	
	// 产品-百行信息服务接口
//	public static final String API_ID_PRODUCT_BHYH = "99999999972";
	public static final String API_SERVICE_PRODUCT_BHYH = "bhyh";
	
	// 产品-一致性验证服务接口
//	public static final String API_ID_PRODUCT_CHECK_CONSISTENCY = "99999999971";
	public static final String API_SERVICE_PRODUCT_CHECK_CONSISTENCY = "checkConsistency";
	
	// 产品-本地查询身份证
	public static final String API_SERVICE_PRODUCT_GET_IDCARD = "getIdCard";
	
	// 产品-空号检测
	public static final String API_SERVICE_PRODUCT_BATCH_CHECK_MOBILE = "batchCheckMobile";
	
	/** productID */
	// 页面pdf
	public static final String PRODUCT_ID_PAGE_PDF = "99999999999";
	
	/** remote访问接口 */
	// 全联
	public static final String REMOTE_URL_QL = "http://www.uniocredit.com/nuapi/UService.do"; 
	// qilingyz
	public static final String REMOTE_URL_QILINGYZ = "http://www.qilingyz.com/api.php";
	
	/** 缓存 */
	// 登录用户cookie
	public static final String COOKIE_USERID = "cookieUserID";
	// 登录用户
	public static final String CACHE_USER = "cacheUser";
	
	/** 产品状态 */
	// 待发布
	public static final String TO_BE_PUBLISH  = "0"; 
	// 发布
	public static final String PUBLISHED = "1"; 
	// 下架
	public static final String REMOVED = "2";
	// 删除
	public static final String DELETE = "3";
	
	/** 产品可见状态 */
	// 可见
	public static final int IS_SHOW_TRUE = 1;
	// 不可见
	public static final int IS_SHOW_FALSE = 2;
	
	/** 账户认证状态 */
	// 未认证
	public static final String AUTH_STATUS_NO = "0";
	// 待审核
	public static final String AUTH_STATUS_WAIT = "1";
	// 已认证
	public static final String AUTH_STATUS_YES = "2";
	// 认证失败
	public static final String AUTH_STATUS_ERROR = "3";
	
	/** 账户角色*/
	// 普通用户
	public static final String USER_ROLE_NORMAL = "0";
	// 管理员
	public static final String USER_ROLE_ADMIN = "9999";
		
	/** localApi.returnParam 中英文key */
	public static final String EN_NAME = "EN_NAME";
	public static final String CH_NAME = "CH_NAME";
	
	/** api前缀  */
	// 综合查询
	public static final String API_PREFIX_ZH = "zh";
	// 身份证认证
	public static final String API_PREFIX_SF = "sf";
	
	/** oracle table name */
	// 人员基本信息
	public static final String ORA_TBL_NAME_PERSON = "person";
	// 居住信息
	public static final String ORA_TBL_NAME_ADDRESS = "address";
	// 职业信息
	public static final String ORA_TBL_NAME_EMPLOYMENT = "employment";
	// 信用卡信息
	public static final String ORA_TBL_NAME_CREDITCARD = "creditcard";
	// 贷款信息
	public static final String ORA_TBL_NAME_LOAN = "loan";
	// 担保信息
	public static final String ORA_TBL_NAME_GUARANTEE = "guarantee";
	// 信用分
	public static final String ORA_TBL_NAME_SCORE = "score";
	// 黑名单
	public static final String ORA_TBL_NAME_BLACK = "black";
	// 失信人
	public static final String ORA_TBL_DISHONEST_INFO = "dishonest_info";
	// 信用分(TRI)
	public static final String ORA_TBL_SCORE_TRI = "score_tri";
	
	/** 日期格式化 */
	// systemLog
	public static final String DATE_FORMAT_SYSTEMLOG = "yyyy-MM-dd HH:mm:ss";
	// apiLog
	public static final String DATE_FROMAT_APILOG = "yyyy-MM-dd HH:mm:ss";
	
	/** 日期格式化 */
	// 启用
	public static final int USER_STATE_OPEN = 1;
	// 停用
	public static final int USER_STATE_CLOSE = 0;
	
	/** 信用风险信息服务平台用户操作类型 */
	// 单次查询
	public static final String OPERATOR_TYPE_QUERY = "1";
	// 单次报送step1
	public static final String OPERATOR_TYPE_ADD_STEP1 = "2";
	// 单次报送step2
	public static final String OPERATOR_TYPE_ADD_STEP2 = "3";
	// 批量查询
	public static final String OPERATOR_TYPE_QUERYALL = "4";
	// 批量报送
	public static final String OPERATOR_TYPE_ADDALL = "5";
	
	/** 调后外部参数 */
	/** 凭安 */
	// 凭安pname
	public static String pn_pname = "120180424002";
	// 凭安pkey
	public static String pn_pkey = "4ce9548728a2aa4220af150be5bb162a";
	
}
