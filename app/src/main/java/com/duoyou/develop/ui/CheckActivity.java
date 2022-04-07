//package com.duoyou.develop.ui;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.TextView;
//
//import com.duoyou.develop.base.BaseCompatActivity;
//import com.duoyou_cps.appstore.R;
//
//public class CheckActivity extends BaseCompatActivity {
//
//    private TextView kefuTv;
//    private String risk;
//    private TextView tvOaid;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.charge_act_check;
//    }
//
//    @Override
//    public void initView() {
//        kefuTv = findViewById(R.id.kefu_tv);
//        tvOaid = findViewById(R.id.tv_oaid);
//    }
//
//    @Override
//    public void initData() {
////        risk = getIntent().getStringExtra("risk");
////        String text = String.format("(%s)设备异常，请<font color='#FF6F41'>联系客服</font>", risk);
////        kefuTv.setText(StringUtils.fromHtml(text));
////        String s = "oaid : " + SPManager.getValue(SPManager.OAID, "");
////        StringBuilder stringBuilder = new StringBuilder(s);
////        tvOaid.setText(stringBuilder.toString());
////        DyIdApi.getApi().getDyId(new OnDyIdCallback() {
////            @Override
////            public void onDyIdCallback(int i, String s) {
////                stringBuilder.append("  dyId = ").append(s);
////                tvOaid.setText(stringBuilder.toString());
////            }
////        });
//
//    }
//
//    @Override
//    public void initListener() {
//    }
//
//    public static int riskControl;
//
//    public static void riskControl(final Activity context) {
////        if (AppInfoUtils.isDebug()) {
////            return;
////        }
////        DyIdApi.getApi().checkRisk((code, risk) -> {
////            ToastHelper.showShortDebug("当前信息仅在测试包可见，风控code = "+risk);
////            LogUtil.i("json__", "risk_control  code = " + code + "  risk = " + risk);
////            if (code == 200) {
////                riskControl = risk;
////                if (risk == 505||risk == 506) {
////                    CheckActivity.launch(context, risk);
////                }
////            }
////        });
//    }
//
//
//    public static void launch(Context context, int risk) {
//        Intent intent = new Intent(context, CheckActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG设置
//        intent.putExtra("risk", risk + "");
//        context.startActivity(intent);
//    }
//}
