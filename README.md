# Scaffold是什么？

1.  是一个脚手架项目。如果你新开始一个项目，可以从本项目开始；
2.  项目中的某些功能，如果你觉得不错，可以直接拿去使用；

# 内置组件和页面概览

1.  [内置页面](https://juejin.cn/post/7221131892426145853)；
2.  [内置组件SlidingTabLayout](https://juejin.cn/post/7221128727639883832)；
3.  [内置组件BottomNavigationLayout](https://juejin.cn/post/7221138459813986360)；
4.  [内置组件TitleLayout](https://juejin.cn/post/7221131892426293309)；
5.  [内置组件ImgTxtLayout](https://juejin.cn/post/7221131892426407997)；
6.  [内置组件SettingItemView](https://juejin.cn/post/7221128727639703608)；
7.  [内置组件SettingToggleView](https://juejin.cn/post/7221131892426539069)；
8.  recyclerview：
   1.  内置LinearItemDecoration，等分GridItemDecoration；
   2.  支持上拉下拉，用法如下，继承BaseRcv；
    ```java
        public class TestRcv extends BaseRcv<String> {
        public TestRcv(Context context) {
            super(context);
        }

        public TestRcv(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected EasyRcvHolder<String> getItemHolder(@NonNull ViewGroup parent, int type)         {
            return new EasyRcvHolder(LayoutInflater.from(getContext()).inflate(R.layout.normal_recyclerview_item, parent, false)) {
                        private CircleImageView mNormalRecyclerviewItemAvatar;
                        private TextView mNormalRecyclerviewItemName;

                        @Override
                        protected View onCreateView() {
                            mNormalRecyclerviewItemAvatar = mView.findViewById(R.id.normal_recyclerview_item_avatar);
                            mNormalRecyclerviewItemName = mView.findViewById(R.id.normal_recyclerview_item_name);
                            return mView;
                        }

                        @Override
                        protected void refreshView(int position, Object item) {
                            UserInfo userInfo = (UserInfo) item;
                            ImageLoader.load(mContext, userInfo.getAvatar(), mNormalRecyclerviewItemAvatar);
                            mNormalRecyclerviewItemName.setText(Util.nullToEmpty(userInfo.getName()));
                        }
                    }
        }
    }
    ```
9. http请求三级缓存

![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/028f863d0f5c44d0a5e5c1e12d6db35b~tplv-k3u1fbpfcp-watermark.image?)

# 内置文字样式

1.  根据material design设计规范，内置常见样式，例如headline2样式定义如下：

```java
<style name="H2.Black.High" parent="TextAppearance.MaterialComponents.Headline2">
        <item name="android:textColor">@color/black6</item>
    </style>

    <style name="H2.Black.High.Left" parent="H2.Black.High">
        <item name="android:gravity">start</item>
    </style>

    <style name="H2.Black.High.Center" parent="H2.Black.High">
        <item name="android:gravity">center</item>
    </style>

    <style name="H2.Black.High.Right" parent="H2.Black.High">
        <item name="android:gravity">end</item>
    </style>
```

# 内置常用类

1.  ContextManager：上下文管理，包括以下功能：
   1.  获取当前Activity/当前Activity的FragmentManager；
   2.  在不能直接获取Context页面，获取string/dimen/color/drawable等；
   3.  获取当前应用是否在后台以及应用进入前后台监听；
2.  KVFileCache：系统Preference替代品，用于存储一些偏好数据，支持基本数据类型和类；
3.  DialogUtil：封装DialogFragment显示/移除；
4.  DensityUtil：dp，sp，px相互转换，屏幕尺寸获取等；
5.  StatusBarUtil：设置状态栏颜色；
6.  ImgLoader：图片加载；
7.  JsonUtil：json处理；
8.  其他；

# 架构模式

mvvm + livedata + lifecycle，具体使用请查阅源码；

### module介绍

1.  scaffold module：脚手架；
2.  app module：
   1.  依赖于scaffold；
   2.  新增"异常上报"(使用bugly)，"用户反馈"（使用吐个槽）两个功能；
   3.  本项目采用MVVM + DataBinding + LivaData + Lifecycle架构，因此增加架构所需基本代码；
   4.  增加示例代码，方便开发者查阅使用示例；

### 依赖库

app module

|名称|版本|
|---|---|
|[bugly](https://bugly.qq.com/docs)|最新|
|[兔小巢](https://txc.qq.com/helper/AndroidGuide)|最新|

scaffold module

|名称|版本|描述|
|---|---|---|
|androidx|-|详见build文件|
|[lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)|2.5.1|-|
|[Timber](https://github.com/JakeWharton/timber)|4.7.1|日志|
|[fastjson](https://github.com/alibaba/fastjson)|1.1.70.android|-|
|[CircleImageView](https://github.com/hdodenhof/CircleImageView)|2.2.0|-|
|[BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)|3.3.3|已直接集成进包内，优化识别速度，增加光线识别动态显示闪光灯图标|
|[flexbox](https://github.com/google/flexbox-layout)|1.1.0|-|
|[retrofit](https://github.com/square/retrofit)|2.5.0|-|
|[glide](https://github.com/bumptech/glide)|4.10.0|-|
|[MMKV](https://github.com/Tencent/MMKV)|1.0.24|用于替换SharePreference|
|[UltraViewPager](https://github.com/alibaba/UltraViewPager)|1.0.7.7|原版本不支持androidx，因此已直接集成进包内|
|[PhotoView](https://github.com/chrisbanes/PhotoView)|2.3.0|-|
|[SmartRefreshLayout](https://github.com/chrisbanes/PhotoView)|2.0.0-alpha-1|上拉下拉|
|[Compressor](https://github.com/chrisbanes/PhotoView)|3.0.0|图片压缩|

### 注意

1.  更改依赖版本时，需同步更新混淆文件；

# 如何使用？

### 替换包名

1.  用android studio全局替换快捷键
    （如果之前没用过该快捷键，可以在`Settings->Keymap`中搜索`Replace in Path`以查询该快捷键）
    将包名替换为自己的包名
2.  此时文件中的包名已成功替换，不过项目的包名仍然未变，打开项目所在文件，将项目包名替换
3.  此时项目应该能正常运行

### 修改debug keystore

1.  根据需要修改debug keystore及其在build.gradle中的配置

### 替换内置依赖库中的key

全局搜索key_strings.xml文件，里面包括以下key：

1.  bugly：用于异常上报，key申请[点此](https://bugly.qq.com)
2.  兔小巢：用于用户反馈，key申请[点此](https://tucao.qq.com)

### 集成后示例代码如何处理？

项目中示例代码存在于以下几处，开发者可以随时删除，也可以留着以便查阅功能使用（不影响正常开发）：

1.  模块app中：page/demo目录，此为示例代码；
2.  模块app中：res/layout/demo目录（需将目录显示模式改为Project Files，否则看不到此目录），此为示例代码布局文件；
