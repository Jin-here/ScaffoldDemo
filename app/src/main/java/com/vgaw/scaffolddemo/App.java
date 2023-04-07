package com.vgaw.scaffolddemo;

import static android.os.Process.myPid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.vgaw.scaffold.BuildConfig;
import com.vgaw.scaffold.Scaffold;
import com.vgaw.scaffold.util.Util;

/**
 * @author caojin
 * @date 2018/6/9
 */
public class App extends Application {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 手机右键退出程序再打开不会再走此处，除非任务管理器关闭
        if (uiProcess()){
            Scaffold.init(this, BuildConfig.DEBUG);

            initBugly();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /*注意：在 MultiDex.install() 完成之前，不要通过反射或 JNI 执行 MultiDex.install() 或其他任何代码。多 dex 文件跟踪功能不会追踪这些调用，从而导致出现 ClassNotFoundException，或因 DEX 文件之间的类分区错误而导致验证错误。*/
        // open multiDex
        /*MultiDex.install(this);*/
    }

    private boolean uiProcess(){
        int pid = myPid();
        String processName = "";
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName;
                break;
            }
        }
        String packageName = getPackageName();
        return processName.equals(packageName);
    }

    private void initBugly() {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
        //userStrategy.setAppChannel();
        //userStrategy.setAppReportDelay(10 * 1000);
        try {
            PackageInfo packageInfo = Util.getPackageInfo(this);
            userStrategy.setAppVersion(packageInfo.versionName);
            userStrategy.setAppPackageName(packageInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // for bugly bug report only
        CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_appid), BuildConfig.DEBUG, userStrategy);
    }
}
