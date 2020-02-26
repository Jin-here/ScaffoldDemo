# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# DJI SDK
-keepattributes Exceptions,InnerClasses,*Annotation*,Signature,EnclosingMethod

-dontwarn okio.**
-dontwarn org.bouncycastle.**
-dontwarn dji.**
-dontwarn com.dji.**

-keepclassmembers enum * {
    public static <methods>;
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep,allowshrinking class * extends dji.publics.DJIUI.** {
    public <methods>;
}

-keep class net.sqlcipher.** { *; }

-keep class net.sqlcipher.database.* { *; }

-keep class dji.** { *; }

-keep class com.dji.** { *; }

-keep class com.google.** { *; }

-keep class org.bouncycastle.** { *; }

-keep,allowshrinking class org.** { *; }

-keep class com.squareup.wire.** { *; }

-keep class sun.misc.Unsafe { *; }

-keep class com.secneo.** { *; }

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class android.support.v7.widget.SearchView { *; }

-keepclassmembers class * extends android.app.Service
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keep class android.support.** { *; }
-keep class android.media.** { *; }
-keep class okio.** { *; }

# MQtt: paho.mqtt.android
-keep class org.eclipse.paho.** {*;}
-keep interface org.eclipse.paho.** {*;}
-keep enum org.eclipse.paho.** {*;}
-dontwarn org.eclipse.paho.**

# MQtt: mqtt-client
#-keep class org.fusesource.** {*;}
#-keep interface org.fusesource.** {*;}
#-keep enum org.fusesource.** {*;}
#-keep class org.osgi.** {*;}
#-keep interface org.osgi.** {*;}
#-keep enum org.osgi.** {*;}
-keep class javax.** {*;}
-keep interface javax.** {*;}
-keep enum javax.** {*;}
#-keep class org.objectweb.** {*;}
#-keep interface org.objectweb.** {*;}
#-keep enum org.objectweb.** {*;}
#-dontwarn org.fusesource.**
#-dontwarn org.osgi.**
-dontwarn javax.**
#-dontwarn org.objectweb.**

#Butterknife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# 高德
-dontwarn com.amap.api.**

##3D 地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}
-dontwarn com.amap.api.maps.**
-dontwarn com.amap.apis.**
##定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
##搜索
-keep class com.amap.api.services.**{*;}
##2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
##导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# 二维码
-keep class cn.bingoogolapple.qrcode.** {*;}
-keep interface cn.bingoogolapple.qrcode.** {*;}
-keep enum cn.bingoogolapple.qrcode.** {*;}

-dontwarn cn.bingoogolapple.qrcode.zxing.**

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#ProtoBuf
#-keep class com.google.protobuf.** {*;}
#-keep interface com.google.protobuf.** {*;}
#-keep enum com.google.protobuf.** {*;}
#-keep class google.protobuf.** {*;}
#-keep interface google.protobuf.** {*;}
#-keep enum google.protobuf.** {*;}
#-keep class sun.misc.** {*;}
#-keep interface sun.misc.** {*;}
#-keep enum sun.misc.** {*;}
#-dontwarn sun.misc.**

#retrofit
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#okio
-dontwarn okio.**

#picasso
-dontwarn com.squareup.okhttp.**

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
# -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#ijkplayer
#-keep class tv.danmaku.ijk.media.** {*;}
#-keep interface tv.danmaku.ijk.media.** {*;}
#-keep enum tv.danmaku.ijk.media.** {*;}

#fastjson
-dontwarn com.alibaba.fastjson.**
-keepattributes Singature
-keepattributes *Annotation

#pldroid-player
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
-dontwarn com.pili.pldroid.player.**

#timber
-dontwarn org.jetbrains.annotations.**

#tape
-keep class com.squareup.tape2.** {*;}
-keep interface com.squareup.tape2.** {*;}
-keep enum com.squareup.tape2.** {*;}

#bugly and upgrade
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

#bd speech
-keep class com.baidu.speech.**{*;}

#zego
-keep class com.zego.**{*;}

#socket.io
-keep class io.socket.** {*;}
-keep interface io.socket.** {*;}
-keep enum io.socket.** {*;}

# MobileLine Core
-keep class com.tencent.qcloud.core.** { *;}
-keep class bolts.** { *;}
-keep class com.tencent.tac.** { *;}
-keep class com.tencent.stat.*{*;}
-keep class com.tencent.mid.*{*;}
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# MobileLine Messaging
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.qq.taf.jce.** {*;}

# MobileLine Vendor Messaging
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class com.meizu.cloud.pushsdk.**{*;}
-keepclasseswithmembernames class com.xiaomi.**{*;}
-dontwarn com.huawei.android.hms.**
-dontwarn com.xiaomi.push.**
-dontwarn com.meizu.cloud.pushsdk.**

# flexbox
-keep class com.google.android.flexbox.** {*;}
-keep interface com.google.android.flexbox.** {*;}
-keep enum com.google.android.flexbox.** {*;}

# hello charts
-keep class lecho.lib.hellocharts.** {*;}
-keep interface lecho.lib.hellocharts.** {*;}
-keep enum lecho.lib.hellocharts.** {*;}

# data range picker
-keep class com.borax12.materialdaterangepicker.** {*;}
-keep interface com.borax12.materialdaterangepicker.** {*;}
-keep enum com.borax12.materialdaterangepicker.** {*;}

# wx
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}

## qq
-keep class * extends android.app.Dialog

# nim
-dontwarn com.netease.**
-keep class com.netease.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}

# citypicker
-keep class com.lljjcoder.**{
	*;
}

# svga
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}

# mi push
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.** {*;}

# huawei push
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes Signature
# hmscore-support: remote transport
-keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
# hmscore-support: remote transport
-keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
<init>(...); }
# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }

# meizu push
-dontwarn com.meizu.cloud.**
-keep class com.meizu.cloud.** {*;}

# oppo push
-keep class com.netease.nimlib.mixpush.oppo.OppoPush* {*;}
-keep class com.netease.nimlib.mixpush.oppo.OppoPushService {*;}

# emoji reader
-keep class com.yy.mobile.emoji.EmojiReader {*;}

#umeng
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.qiandaojie.xiaoshijie.R$*{
public static final int *;
}
-keep class com.umeng.** {*;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class org.android.spdy.** {*;}
-keep interface org.android.spdy.** {*;}
-keep enum org.android.spdy.** {*;}
## oppo
-keep public class * extends android.app.Service
## vivo
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
-keep class xxx.xxx.xxx.PushMessageReceiverImpl{*;}

## photoview
-keep class com.github.chrisbanes.photoview.** {*;}
-keep enum com.github.chrisbanes.photoview.** {*;}
-keep interface com.github.chrisbanes.photoview.** {*;}

## rx
-keep class retrofit2.adapter.rxjava2.** {*;}
-keep enum retrofit2.adapter.rxjava2.** {*;}
-keep interface retrofit2.adapter.rxjava2.** {*;}
-keep class io.reactivex.** {*;}
-keep enum io.reactivex.** {*;}
-keep interface io.reactivex.** {*;}
-keep class com.trello.rxlifecycle3.** {*;}
-keep interface com.trello.rxlifecycle3.** {*;}
-keep enum com.trello.rxlifecycle3.** {*;}

#scaffold
-keep class com.vgaw.scaffold.view.webview.FileChooserWebChromClient {*;}
-keep class com.vgaw.scaffold.util.statusbar.StatusBarUtil {*;}

#App
-keep class com.vgaw.data.** {*;}
-keep class com.vgaw.http.bean.** {*;}