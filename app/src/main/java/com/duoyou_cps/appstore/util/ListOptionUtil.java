package com.duoyou_cps.appstore.util;


import androidx.annotation.StringRes;

import com.blankj.utilcode.util.Utils;
import com.duoyou.develop.utils.AppInfoUtils;
import com.duoyou_cps.appstore.R;
import com.duoyou_cps.appstore.entity.OptionEntity;

import java.util.ArrayList;
import java.util.List;

public class ListOptionUtil {

    public static List<OptionEntity> initSettingOption() {

        List<OptionEntity> list = new ArrayList<>();

        list.add(new OptionEntity(0, R.drawable.icon_mine_options_change_psd, "修改密码", "", true, true));
        list.add(new OptionEntity(1, R.drawable.icon_mine_options_unbind_mobile, "解绑手机", "", true, true));
        list.add(new OptionEntity(2, R.drawable.icon_mine_options_download, "下载管理", "", true, true));
        list.add(new OptionEntity(3, R.drawable.icon_mine_options_gift, "我的礼包", "", true, true));
        list.add(new OptionEntity(4, R.drawable.icon_mine_options_service, "联系客服", "", true, true));
        list.add(new OptionEntity(5, R.drawable.icon_mine_options_about_me, "关于我们", "", true, true));
        return list;
    }

    public static List<OptionEntity> initAboutMeOption(boolean debug) {

        List<OptionEntity> list = new ArrayList<>();
        list.add(new OptionEntity(0, 0, "检查更新", String.format("V%s", AppInfoUtils.getVerName()), "",true, true));
        list.add(new OptionEntity(1, 0, "用户协议", "", "",true, true));
        list.add(new OptionEntity(2, 0, "隐私政策", "", "",true, true));
//        list.add(new OptionEntity(4, 0, "服务协议", "", true, true));
//        if (debug || AppInfoUtils.isDebug()) {
//            String title;
//            boolean isDebug = SPManager.getValue(SPManager.SERVER_RELEASE, AppInfoUtils.isDebug());
//            if (isDebug) {
//                title = "切换至正式服";
//            } else {
//                title = "切换至测试服";
//            }
//            list.add(new OptionEntity(6, 0, title, "", true, true));
//        }

        return list;
    }
//
//
//    public static List<PaymentModeEntity> initPaymentModeList(int orderType) {
//        List<PaymentModeEntity> list = new ArrayList<>();
//
////        list.add(new PaymentModeEntity(1, "微信", R.drawable.icon_payment_mode_wechat, ORDER_PAYMENT_WX_PAY,true));
//        list.add(new PaymentModeEntity(2, "支付宝", R.drawable.icon_payment_mode_alipay, ORDER_PAYMENT_ALIPAY, true));
//
//        if (orderType == ORDER_TYPE_BLIND_BOX) {
//            list.add(new PaymentModeEntity(4, "彩币", R.drawable.icon_payment_mode_caibi, ORDER_PAYMENT_COIN, false));
//        }
//
////        if (orderType != 0) {
////            list.add(new PaymentModeEntity(3, "彩豆", R.drawable.icon_payment_mode_caidou, ORDER_PAYMENT_BEAN, false));
////        }
//        return list;
//    }
//
//    public static int getCheckedPayment(List<PaymentModeEntity> list) {
//        if (list == null && list.size() == 0) {
//            return -1;
//        }
//        for (PaymentModeEntity entity : list) {
//            if (entity.getCheck()) {
//                return entity.getTypeMode();
//            }
//        }
//        return -1;
//    }


    private static String getString(@StringRes int strId) {
        return Utils.getApp().getString(strId);
    }

}
