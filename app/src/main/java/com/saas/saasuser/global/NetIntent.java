package com.saas.saasuser.global;


import java.io.File;

public class NetIntent {


    public void log(String url) {
        System.out.println("http->log:" + url);
    }

    //1、司机—列表已结束的订单
    public void client_driverEndOrderList(String driverId, String driverCompanyId, NetIntentCallBackListener callBack) {
        OkHttpClientManager.Param[] params = {new OkHttpClientManager.Param("vvId", driverId),
                new OkHttpClientManager.Param("vvComId", driverCompanyId)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverEndOrderList, callBack, params);
    }

    //2、提交用车申请
    public void client_submitOrder(String userId, String userCompanyId, String tripType, String planCost, String date,
                                   String week, String goBack, String startTime, String startAddress, String endAddress, String purpose, String passagerNum,
                                   String midAddress, String endTime, String carGrade, String passagerBestow, String passagerCrossBorder,
                                   String innerCar, String passagerPeer, String carRequst, String driverRequst, String otherRequst,
                                   NetIntentCallBackListener callBack) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BONeederID", userId),
                new OkHttpClientManager.Param("BOBelongCompany", userCompanyId),
                new OkHttpClientManager.Param("BOTripWay", tripType),
                new OkHttpClientManager.Param("BOPlanCost", planCost),
                new OkHttpClientManager.Param("BOUseDate", date),
                new OkHttpClientManager.Param("BOUseWeek", week),
                new OkHttpClientManager.Param("BOGoBack", goBack),
                new OkHttpClientManager.Param("BOUStartTime", startTime),
                new OkHttpClientManager.Param("BOUpCarPlace", startAddress),
                new OkHttpClientManager.Param("BODestination", endAddress),
                new OkHttpClientManager.Param("BOPurposeDsc", purpose),
                new OkHttpClientManager.Param("BOPassengers", passagerNum),

                new OkHttpClientManager.Param("BODownCarPlace", midAddress),
                new OkHttpClientManager.Param("BOUEndTime", endTime),
                new OkHttpClientManager.Param("BOAssignCarLevel", carGrade),
                new OkHttpClientManager.Param("BOIsSleepover", passagerBestow),
                new OkHttpClientManager.Param("BOIsTransnational", passagerCrossBorder),
                new OkHttpClientManager.Param("BOIsSelfCar", innerCar),
                new OkHttpClientManager.Param("BOAssociate", passagerPeer),
                new OkHttpClientManager.Param("BOVehicleRequire", carRequst),
                new OkHttpClientManager.Param("BODriverRequire", driverRequst),
                new OkHttpClientManager.Param("OtherRequire", otherRequst)
        };

        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.submitOrder, callBack, params);
    }

    //3、乘客—列表已结束的订单
    public void client_userEndOrderList(String userId, String userCompanyId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId)
                , new OkHttpClientManager.Param("vvComId", userCompanyId)};
        OkHttpClientManager.getInstance().getAsyn(Constants.HttpRoot + Constants.userEndOrderList, callBackListener, params);

    }

    //4、司机—完善订单信息
    public void client_drivercompleteOrder(String orderId, String driverCompanyId, String driverId, String startTime, String endTime, String calculTime,
                                           String startKilometre, String endKilometre, String calculKilometre, String startAddress, String midwayAddress, String endAddress,
                                           String roadBridgeBill, String roadBridgeBillType, String roadBridgeBillNum, String parkingBill, String parkingBillType, String parkingBillNum,
                                           String fuelBill, String fuelBillType, String fuelBillNum, String OilGrade, String OilLitre, String realCollectionIncoming,
                                           String realCurrency, String vehicleStartMiles, String vehicleEndMiles, String orderOverTimeNum, String realDriverKilometre, String notPassagerKilometre,
                                           String collectionDescribe, NetIntentCallBackListener callBackListener) {

        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvComId", driverCompanyId),
                new OkHttpClientManager.Param("vvId", driverId),
                new OkHttpClientManager.Param("OFStartTime", startTime),
                new OkHttpClientManager.Param("OFEndTime", endTime),

                new OkHttpClientManager.Param("OFCalcTiems", calculTime),
                new OkHttpClientManager.Param("OFStartMileage", startKilometre),
                new OkHttpClientManager.Param("OFEndMileage", endKilometre),
                new OkHttpClientManager.Param("OFCalcMileage", calculKilometre),
                new OkHttpClientManager.Param("OFRealStartSite", startAddress),

                new OkHttpClientManager.Param("OFRealGobySite", midwayAddress),
                new OkHttpClientManager.Param("OFRealEndSite", endAddress),
                new OkHttpClientManager.Param("OFETCCal", roadBridgeBill),
                new OkHttpClientManager.Param("OFCalcETCCalWay", roadBridgeBillType),
                new OkHttpClientManager.Param("OFCalcetcCalRelated", roadBridgeBillNum),

                new OkHttpClientManager.Param("OFParkingCal", parkingBill),
                new OkHttpClientManager.Param("OFCalcParkingCalWay", parkingBillType),
                new OkHttpClientManager.Param("OFCalcParkingCalRelated", parkingBillNum),
                new OkHttpClientManager.Param("OFFuelCal", fuelBill),
                new OkHttpClientManager.Param("OFCalcFuelCalWay", fuelBillType),

                new OkHttpClientManager.Param("OFCalcfuelCalRelated", fuelBillNum),
                new OkHttpClientManager.Param("OFSubmitApplyOil", OilGrade),
                new OkHttpClientManager.Param("OFSubmitApplyOilLPM", OilLitre),
                new OkHttpClientManager.Param("OFORealCollection", realCollectionIncoming),
                new OkHttpClientManager.Param("OFORealCurrency", realCurrency),

                new OkHttpClientManager.Param("OFOVehicleStartMiles", vehicleStartMiles),
                new OkHttpClientManager.Param("OFOVehicleEndMiles", vehicleEndMiles),
                new OkHttpClientManager.Param("OFOTaskOverTime", orderOverTimeNum),
                new OkHttpClientManager.Param("OFORealDriving", realDriverKilometre),
                new OkHttpClientManager.Param("OFOEmptyRunning", notPassagerKilometre),

                new OkHttpClientManager.Param("OFOCollectionRemark", collectionDescribe)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.drivercompleteOrder, callBackListener, params);
    }

    // 5、乘客—完善订单信息
    public void client_userCompleteOrder(String orderId, String settleType, String billMoeny, String billCurrency, String billNum, String tripDescribe, String userId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("CAOFID", orderId),
                new OkHttpClientManager.Param("CASettleType", settleType),
                new OkHttpClientManager.Param("CARouteCost", billMoeny),
                new OkHttpClientManager.Param("CAOfferType", billCurrency),
                new OkHttpClientManager.Param("CATicketNums", billNum),

                new OkHttpClientManager.Param("CARouteCostMark", tripDescribe),
                new OkHttpClientManager.Param("CAAddUser", userId)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.userCompleteOrder, callBackListener, params);
    }


    //6、待确认订单
    public void client_awaitConfirmOrder(String userId, String companyId, String type, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("LoginID", userId)
                , new OkHttpClientManager.Param("CompanyID", companyId)
                , new OkHttpClientManager.Param("Type", type)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.awaitConfirmOrder, callBackListener, params);
    }


    //7、订单详情
    public void client_getOrderDetailById(String orderId, String userId, String type, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId)
                , new OkHttpClientManager.Param("vvId", userId)
                , new OkHttpClientManager.Param("vvType", type)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.getOrderDetailById, callBackListener, params);
    }

    //8、司机接单|乘客出行
    public void client_driverOrUserConfirmOrder(String orderId, String userId, String type, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("LoginID", userId),
                new OkHttpClientManager.Param("Type", type)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverOrUserConfirmOrder, callBackListener, params);
    }

    //9、司机拒单|乘客拒乘
    public void client_driverOrUserRefuseOrder(String orderId, String userId, String type, String content, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId)
                , new OkHttpClientManager.Param("LoginID", userId)
                , new OkHttpClientManager.Param("Type", type)
                , new OkHttpClientManager.Param("Content", content)};
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverRefuseOrder, callBackListener, params);
    }

    //10、审批信息 批复 安排 任务列表信息
    public void client_mainMenuListThrid(String userId, String page, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("page", page)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.mainMenuListThrid, callBackListener, params);
    }

    //11、已知任务 已知安排列表
    public void client_mainMenuListforth(String userId, String page, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("page", page)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.mainMenuListforth, callBackListener, params);
    }

    //12、登录
    public void client_login(String userId, String password, String deviceID, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("loginName", userId),
                new OkHttpClientManager.Param("pwd", password),
                new OkHttpClientManager.Param("DeviceID", deviceID)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.login, callBackListener, params);
    }

    //13、司机：任务开始 抵达 开始订单
    public void client_driverExecuteTask(String userId, String orderId, String status, String position, String longitude, String latitude, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvState", status),
                new OkHttpClientManager.Param("Place", position),
                new OkHttpClientManager.Param("Longitude", longitude),
                new OkHttpClientManager.Param("Latitude", latitude)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverExecuteTask, callBackListener, params);
    }


    //14、司机：订单结束
    public void client_driverFinishTask(String userId, String orderId, String companyId, String position, String longitude, String latitude, String planMile, String realMile, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvComId", companyId),
                new OkHttpClientManager.Param("Place", position),
                new OkHttpClientManager.Param("Longitude", longitude),
                new OkHttpClientManager.Param("Latitude", latitude),
                new OkHttpClientManager.Param("PMileage", planMile),
                new OkHttpClientManager.Param("RMileage", realMile)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverFinishTask, callBackListener, params);
    }

    //15、乘客：行程开始/结束
    public void client_userFinishTrip(String userId, String companyId, String orderId, String status, String position, String longitude, String latitude, String planMile, String realMile, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("vvComId", companyId),
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvState", status),
                new OkHttpClientManager.Param("Place", position),
                new OkHttpClientManager.Param("Longitude", longitude),
                new OkHttpClientManager.Param("Latitude", latitude),
                new OkHttpClientManager.Param("PMileage", planMile),
                new OkHttpClientManager.Param("RMileage", realMile)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.userFinishTrip, callBackListener, params);
    }

    //16、司机：订单结束提交数据
    public void client_driverFinishSubmitData(String userId, String orderId, String type, String jsonData, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvType", type),
                new OkHttpClientManager.Param("Journey", jsonData)

        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverFinishSubmitData, callBackListener, params);
    }

    //17、管理者：批复联络（详情）
    public void client_masterReplyOrderDetail(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.masterReplyOrderDetail, callBackListener, params);
    }

    //18、申请人：申请联络（详情)
    public void client_applicantReplyOrderDetail(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.applicantReplyOrderDetail, callBackListener, params);
    }

    //19、管理者：批复联络审批(同意/拒绝)
    public void client_masterReplyOrderOpinion(String userId, String orderId, String replyMessage, String replyType, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("AuditMsg", replyMessage),
                new OkHttpClientManager.Param("AuditResult", replyType)

        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.masterReplyOrderOpinion, callBackListener, params);
    }

    //20、乘客/司机—待完善的订单列表（汇总）
    public void client_driverOrUserCompeleteOrderList(String userId, String companyId, String type, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("vvComId", companyId),
                new OkHttpClientManager.Param("vvType", type)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverOrUserCompeleteOrderList, callBackListener, params);
    }

    //21、已知任务，已知安排详情
    public void client_driverOrUserGetConfirmOrder(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverOrUserGetConfirmOrder, callBackListener, params);
    }

    //22、批复联络（重新提交数据获取）
    public void client_replyContactResubmit(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.replyContactResubmit, callBackListener, params);
    }

    //23、批复联络（重新提交数据保存）
    public void client_replyContactConfirm(String orderId, String userId, String userCompanyId, String tripType, String planCost, String date,
                                           String week, String goBack, String startTime, String startAddress, String endAddress, String purpose, String passagerNum,
                                           String midAddress, String endTime, String carGrade, String passagerBestow, String passagerCrossBorder,
                                           String innerCar, String passagerPeer, String carRequst, String driverRequst, String otherRequst,
                                           NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("BONeederID", userId),
                new OkHttpClientManager.Param("BOBelongCompany", userCompanyId),
                new OkHttpClientManager.Param("BOTripWay", tripType),
                new OkHttpClientManager.Param("BOPlanCost", planCost),
                new OkHttpClientManager.Param("BOUseDate", date),
                new OkHttpClientManager.Param("BOUseWeek", week),
                new OkHttpClientManager.Param("BOGoBack", goBack),
                new OkHttpClientManager.Param("BOUStartTime", startTime),
                new OkHttpClientManager.Param("BOUpCarPlace", startAddress),
                new OkHttpClientManager.Param("BODestination", endAddress),
                new OkHttpClientManager.Param("BOPurposeDsc", purpose),
                new OkHttpClientManager.Param("BOPassengers", passagerNum),

                new OkHttpClientManager.Param("BODownCarPlace", midAddress),
                new OkHttpClientManager.Param("BOUEndTime", endTime),
                new OkHttpClientManager.Param("BOAssignCarLevel", carGrade),
                new OkHttpClientManager.Param("BOIsSleepover", passagerBestow),
                new OkHttpClientManager.Param("BOIsTransnational", passagerCrossBorder),
                new OkHttpClientManager.Param("BOIsSelfCar", innerCar),
                new OkHttpClientManager.Param("BOAssociate", passagerPeer),
                new OkHttpClientManager.Param("BOVehicleRequire", carRequst),
                new OkHttpClientManager.Param("BODriverRequire", driverRequst),
                new OkHttpClientManager.Param("OtherRequire", otherRequst)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.replyContactConfirm, callBackListener, params);
    }

    //24、获取结束订单信息（司机完善订单）
    public void client_driverGetCompeleteOrderDetail(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.driverGetCompeleteOrderDetail, callBackListener, params);
    }

    //25、获取结束订单信息（乘客完善订单）
    public void client_userGetCompeleteOrderDetail(String userId, String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.userGetCompeleteOrderDetail, callBackListener, params);
    }

    //26、检查app版本
    public void client_checkAPKVersion(String version, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("CurrentVersion", version)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.checkAPKVersion, callBackListener, params);
    }

    //27、更新app版本
    public void client_downloadAPK(String userId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.downloadAPK, callBackListener, params);
    }

    //28、当前正在执行订单
    public void client_underOrder(String userId, String type, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("vvType", type)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.underOrder, callBackListener, params);
    }

    //29、企业名片
    public void client_companyCardInfo(String companyId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("companyID", companyId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.companyCardInfo, callBackListener, params);
    }

    //30、批复联络同意
    public void client_replyOrderApproval(String orderId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.replyOrderApproval, callBackListener, params);
    }

    //31、标志信息是否已读
    public void client_markOrderIsReaded(String orderId, String orderStatue, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("BOID", orderId),
                new OkHttpClientManager.Param("vvType", orderStatue)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.markOrderIsReaded, callBackListener, params);
    }

    //32、租行记录
    public void client_orderRecord(String userId, String page, String searchType, String keyword, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("vvId", userId),
                new OkHttpClientManager.Param("page", page),
                new OkHttpClientManager.Param("seekContent", keyword),
                new OkHttpClientManager.Param("type", searchType)

        };
        OkHttpClientManager.getInstance().getAsyn(Constants.HttpRoot + Constants.orderRecord, callBackListener, params);
    }


    //33、获取个人信息
    public void client_getPersonInfo(String userId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("LoginId", userId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.getPersonInfo, callBackListener, params);
    }


    //34、修改个人头像
    public void client_editPersonImage(String userId, File file, NetIntentCallBackListener callBackListener) {

        try {
            OkHttpClientManager.Param param = new OkHttpClientManager.Param("LoginId", userId);
            String filekey = "file5";
            OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.editPersonImage, callBackListener, file, filekey, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    //35、汽车新闻
//    public void client_getCarNews(NetIntentCallBackListener callBackListener) {
//        OkHttpClientManager.Param[] params = {
//                new OkHttpClientManager.Param("type", "qiche"),
//                new OkHttpClientManager.Param("key", "71b8bd3c57c382ac6e1bf2b45b9ef97a")
//        };
//        OkHttpClientManager.getInstance().postAsyn(Constants.getCarNews, callBackListener, params);
//    }
    //35、汽车新闻
    public void client_getCarNews(NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("type", "qiche"),
                new OkHttpClientManager.Param("key", "71b8bd3c57c382ac6e1bf2b45b9ef97a")
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.getCarNews, callBackListener, params);
    }

    //36、企业发票
    public void client_getCompanyVnvoice(String companyId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("companyID", companyId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.getCompanyVnvoice, callBackListener, params);
    }

    //37、图片下单--提交数据
    public void client_submitOrderByPicture(String userId, String userName, String userPhone, String companyId, String orderId, String driverResource, String vehicleSource
            , String startDate, String vehicleUserId, String vehicleUserName, String startTime, String vehicleType, File file, NetIntentCallBackListener callBackListener) {
        try {
            OkHttpClientManager.Param[] params = {
                    new OkHttpClientManager.Param("LgId", userId),
                    new OkHttpClientManager.Param("TBOPlanName", userName),
                    new OkHttpClientManager.Param("TBOPlanPhone", userPhone),
                    new OkHttpClientManager.Param("CompanyId", companyId),
                    new OkHttpClientManager.Param("TempOrderId", orderId),
                    new OkHttpClientManager.Param("DriverSource", driverResource),
                    new OkHttpClientManager.Param("VehicleSource", vehicleSource),
                    new OkHttpClientManager.Param("UseCarDate", startDate),
                    new OkHttpClientManager.Param("VehicleUserId", vehicleUserId),
                    new OkHttpClientManager.Param("VehicleUserName", vehicleUserName),
                    new OkHttpClientManager.Param("UseTime", startTime),
                    new OkHttpClientManager.Param("VehicleClass", vehicleType)
            };
            String filekey = "Files";
            OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.submitOrderByPicture, callBackListener, file, filekey, params);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //38、38、图片下单--获取数据
    public void client_getDataBeforeMakeOrder(String companyId, NetIntentCallBackListener callBackListener) {
        OkHttpClientManager.Param[] params = {
                new OkHttpClientManager.Param("CompanyId", companyId)
        };
        OkHttpClientManager.getInstance().postAsyn(Constants.HttpRoot + Constants.getDataBeforeMakeOrder, callBackListener, params);
    }


}
