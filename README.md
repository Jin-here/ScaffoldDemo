# 这是什么？
1. 脚手架。如果你新开始一个项目，可以从本项目开始；
2. 该项目仍处于测试阶段；
3. 项目中的某些功能，如果你觉得不错，可以直接拿去使用；

# 如何使用？
### 替换包名
1. 用android studio全局替换快捷键
（如果之前没用过该快捷键，可以在`Settings->Keymap`中搜索`Replace in Path`以查询该快捷键）
将包名替换为自己的包名
2. 此时文件中的包名已成功替换，不过项目的包名仍然未变，打开项目所在文件，将项目包名替换
3. 此时项目应该能正常运行

### 修改debug keystore
1. 根据需要修改debug keystore及其在build.gradle中的配置

### 替换内置依赖库中的key
全局搜索key_strings.xml文件，里面包括以下key：
1. bugly：用于异常上报和软件升级，key申请[点此](https://bugly.qq.com)
2. 吐个槽：用于用户反馈，key申请[点此](https://tucao.qq.com)

### 集成后示例代码如何处理？
项目中示例代码存在于以下几处，开发者可以随时删除，也可以留着以便查阅功能使用（不影响正常开发）：
1. 模块app中：page/demo目录，此为示例代码；
2. 模块app中：res/layout/demo目录（需将目录显示模式改为Project Files，否则看不到此目录），此为示例代码布局文件；

# 有哪些功能？
### module介绍
1. scaffold module：脚手架；
2. app module：
   1. 依赖于scaffold；
   2. 新增"软件升级和异常上报"(使用bugly)，"用户反馈"（使用吐个槽）两个功能；
   3. 本项目采用MVVM + DataBinding + LivaData + Lifecycle架构，因此增加架构所需基本代码；
   4. 增加示例代码，方便开发者查阅使用示例；
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
|[lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)|2.1.0-alpha02|-|
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


### 内置组件
组件具体使用可下载源码查看；
1. checkable layout：支持单选多选
2. bottom nav：基于checkable layout实现，支持气泡；
3. tab，基于checkable layout实现：
   1. 除基本功能，增加设置:
      1. tab间分割线;
      2. 底部分割线;
      3. 底部indicator；
      4. tab选中/非选中样式；
      5. 气泡；
   2. 底部indicator内置如下两种，开发者可通过实现BaseIndicator抽象类来自定义绘制；

   ![](https://github.com/VolodymyrCj/ScaffoldDemo/blob/develop/gallery/compoment_tab_indicator_style.png)
4. recyclerview：
   1. 内置LinearItemDecoration，等分GridItemDecoration；
   2. 封装adapter，方便使用，示例如下：
   ```java
   mAdapter = new EasyRcvAdapter<UserInfo>(getContext(), mDataList) {
               @Override
               protected EasyRcvHolder getHolder(int type) {
                   return new EasyRcvHolder(LayoutInflater.from(getContext()).inflate(R.layout.normal_recyclerview_item, null)) {
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
                   };
               }
           };
   ```
5. viewpager：
   1. 封装adapter，使用示例如下，仅需实现该方法即可：
   ```java
   @Override
       protected View instantiateView(int position) {
           View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_ad_item, null);
           ImageView iv = view.findViewById(R.id.viewpager_ad_item_content);
           String item = getItem(position);
           ImageLoader.load(mContext, item, iv);
           return view;
       }
   ```
6. other：
   1. toolbar：支持以下四种样式：

   ![](https://github.com/VolodymyrCj/ScaffoldDemo/blob/develop/gallery/compoment_other_toolbar.png)
   
   2. checkable imagebutton：支持src选中和background选中：

   ![](https://github.com/VolodymyrCj/ScaffoldDemo/blob/develop/gallery/compoment_other_checkable_imagebutton.png)
   
   3. setting item：支持以下5中样式：

   ![](https://github.com/VolodymyrCj/ScaffoldDemo/blob/develop/gallery/compoment_other_setting.png)
   
   4. 其他；

### 内置页面
具体使用可下载源码查看；
1. 二维码识别；
2. 选择相册/拍照；
3. 权限申请；
4. 版本更新检查；
5. 用户反馈；
6. 图片预览；


### 内置文字样式
1. 根据material design设计规范，内置常见样式，例如headline2样式定义如下：
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

### 架构模式
mvvm + livedata + lifecycle，具体使用请查阅源码；

### 内置常用类
1. ContextManager：上下文管理，包括以下功能：
    1. 获取当前Activity/当前Activity的FragmentManager；
    2. 在不能直接获取Context页面，获取string/dimen/color/drawable等；
    3. 获取当前应用是否在后台；
3. KVCache；
3. DialogUtil：封装DialogFragment显示/移除；
4. DensityUtil；
5. StatusBarUtil：状态栏相关；
6. ImgLoader：图片加载；
7. JsonUtil：json处理；
8. 其他；

# 注意
1. 更改依赖版本时，需同步更新混淆文件；
