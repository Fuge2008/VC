package com.saas.saasuser.global;


/**
 * 接口封装
 */
public class Constants {

    //public static final String HttpRoot = "http://al.vv-che.com/";//接口：http://120.77.86.137:9527/
    public static final String HttpRoot = "https://api.vv-che.com/";
    public static final String ImageHttpRoot = "http://vv-che.com/";


    /**
     * 是否第一次运行
     **/
    public static final String IS_FIRST_RUN = "isFirstRun";


    /**
     * 1、司机—列表已结束的订单
     */
    public static final String driverEndOrderList = "OrderV2/GetBaseOrderIn4List";
    /**
     * 2、提交用车申请
     */
    public static final String submitOrder = "Order/SaveOrderInfoResult";
    /**
     * 3、乘客—列表已结束的订单
     */
    public static final String userEndOrderList = "OrderV2/GetBaseOrderIn4SList";
    /**
     * 4、司机—完善订单信息
     */
    public static final String drivercompleteOrder = "OrderV2/CompleteOrderInfo";
    /**
     * 5、乘客—完善订单信息
     */
    public static final String userCompleteOrder = "OrderV2/CompleteCostApply";
    /**
     * 6、待确认订单
     */
    public static final String awaitConfirmOrder = "Order/GetBaseOrderPlan";
    /**
     * 7、订单详情
     */
    public static final String getOrderDetailById = "OrderV2/GetOrderDetailInfo";
    /**
     * 8、司机接单|乘客出行
     */
    public static final String driverOrUserConfirmOrder = "Order/FareConfirm";

    /**
     * 9、司机拒单/乘客拒乘
     */
    public static final String driverRefuseOrder = "Order/DriverRefuse";

    /**
     * 10、审批信息 批复 安排 任务列表信息
     */
    public static final String mainMenuListThrid = "OrderV2/GetBOrderInfoList";
    /**
     * 11、已知任务 已知安排列表
     */
    public static final String mainMenuListforth = "OrderV2/GetComBOrederIList";
    /**
     * 12、登录
     */
    public static final String login = "/LoginReg/LoginJson";

    /**
     * 13、司机：任务开始 抵达 开始订单
     */
    public static final String driverExecuteTask = "OrderV2/STaskForDriver";

    /**
     * 14、司机：订单结束
     */
    public static final String driverFinishTask = "OrderV2/STaskFinish";
    /**
     * 15、乘客：行程开始/结束
     */
    public static final String userFinishTrip = "OrderV2/STaskForUser";

    /**
     * 16、司机：订单结束提交数据
     */
    public static final String driverFinishSubmitData = "OrderV2/SaveJourney";

    /**
     * 17、管理者：批复联络（详情）
     */
    public static final String masterReplyOrderDetail = "OrderV2/GetOrderApply";

    /**
     * 18、申请人：申请联络（详情)
     */
    public static final String applicantReplyOrderDetail = "OrderV2/GetOrderRreply";

    /**
     * 19、管理者：申请联络审批(同意/拒绝)
     */
    public static final String masterReplyOrderOpinion = "OrderV2/LeadReply";

    /**
     * 20、乘客/司机—待完善的订单列表（汇总）
     */
    public static final String driverOrUserCompeleteOrderList = "OrderV2/GetBOFinllyInfo";

    /**
     * 21、已知任务，已知安排详情
     */
    public static final String driverOrUserGetConfirmOrder = "OrderV2/GetKnownTask";


    /**
     * 22、批复联络（重新申请数据获取）
     */
    public static final String replyContactResubmit = "OrderV2/GetOrderResubmit";
    /**
     * 23、批复联络（重新提交数据保存）
     */
    public static final String replyContactConfirm = "OrderV2/UpdateOrderApply";

    /**
     * 24、获取结束订单信息（司机完善订单）
     */

    public static final String driverGetCompeleteOrderDetail = "OrderV2/GetOrderComplete";

    /**
     * 25、获取结束订单信息（乘客完善订单）
     */
    public static final String userGetCompeleteOrderDetail = "OrderV2/GetCostApply";
    /**
     * 26、检查app版本
     */

    public static final String checkAPKVersion = "OrderV2/DetectionVersion";

    /**
     * 27、更新app版本
     */

    public static final String downloadAPK = "OrderV2/DownLoadNewVersion";

    /**
     * 28、当前正执行订单
     */
    public static final String underOrder = "OrderV2/GetOSInfoList";
    /**
     * 29、企业名片
     */

    public static final String companyCardInfo = "EnterpriseSetting/EnterpriseIntroduce";

    /**
     * 30、批复联络同意
     */

    public static final String replyOrderApproval = "OrderV2/AgreeApply";

    /**
     * 31、标志信息是否已读
     */

    public static final String markOrderIsReaded = "OrderV2/IsRead";

    /**
     * 32、租行记录
     */

    public static final String orderRecord = "OrderV2/GetLeaseRecord";
    /**
     * 33、获取个人信息
     */
    public static final String getPersonInfo = "PersonInfo/GetPersonInfo";
    /**
     * 34、修改个人头像
     */
    public static final String editPersonImage = "PersonInfo/NotifyHeadImage";
    /**
     * 35、汽车新闻
     */
    //public static final String getCarNews= "http://v.juhe.cn/toutiao/index";
    public static final String getCarNews = "http://z19129d896.iok.la/fuge2000/news.json";

    /**
     * 36、企业发票
     */
    public static final String getCompanyVnvoice = "EnterpriseSetting/EnterpriseInvoice";

    /**
     * 37、图片下单--提交数据
     */
    public static final String submitOrderByPicture = "OrderV2/AddImageOrder";

    /**
     * 38、图片下单--获取数据
     */
    public static final String getDataBeforeMakeOrder = "OrderV2/GetDropList ";


    public static final String User = "saas.db";
    // public static final String DIR_AVATAR = "/sdcard/saasuer/";
    public static final String JSON_KEY_SEX = "sex";
    public static final String JSON_KEY_AVATAR = "avatar";
    public static final String JSON_KEY_CITY = "city";


    //新增订单公司简称
    public static final String GONGSIJIANCHENG = "Order/GetModelPartners";
    //客户信息
    public static final String KEHUXINXI = "Order/GetClientList";
    //计价规则
    public static final String JJROLES = "Order/GetJJRoles";
    //电话号码
    public static final String PHONE = "Order/GetPhone";
    //新增订单
    public static final String NEW_ORDER = "Order/CreatOrder";


    /*个人注册*/
    public static final String SMSSUBMIT = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
    public static String PERSON = "https://api.vv-che.com/LoginReg/EmployerReg";
    public static String PERSONGIVEUP = "https://api.vv-che.com/LoginReg/YGiveUp";
    public static String PERSONDETAIL = "https://api.vv-che.com/LoginReg/YDetailReg";
    public static String PERSONREAPPLY = "https://api.vv-che.com/LoginReg/QRegClear";
    //修改密码
    public static String SETTINGPWD = "https://api.vv-che.com/LoginReg/SettingPwd";

    /*企业注册*/
    //判断手机号是否已注册
    public static String REGISTER = "https://api.vv-che.com/LoginReg/EnterReg";
    public static String ENTERPRISEDETAIL = "https://api.vv-che.com/LoginReg/EDetailReg";
    public static String ENTERPRISINVITECODE = "https://api.vv-che.com/LoginReg/GetShortNameByCode";


}
