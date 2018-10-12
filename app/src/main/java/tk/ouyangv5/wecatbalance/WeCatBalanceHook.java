package tk.ouyangv5.wecatbalance;

import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WeCatBalanceHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //拦截除微信以外的程序
        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;

        String hookClass = "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI";
        String hookMethodName = "onCreate";
        /*
            在onCreate方法运行完之后做一些操作
         */
        XposedHelpers.findAndHookMethod(hookClass, lpparam.classLoader, hookMethodName, Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //获得当前的activity对象
                Object walletBalanceActivity = param.thisObject;

                //遍历activity的所有属性，找到所有的TextView并添加监听
                Field[] allField = walletBalanceActivity.getClass().getDeclaredFields();
                for (Field field : allField) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(walletBalanceActivity);
                    if (fieldObject != null && fieldObject instanceof TextView) {
                        TextView textView = (TextView) fieldObject;
                        textView.addTextChangedListener(new TextViewWatcher(textView));
                        XposedBridge.log(field.getName() + "," + textView.getText().toString());
                    }
                }
            }
        });

    }
}
