package com.vgaw.scaffolddemo.data;

import com.vgaw.scaffolddemo.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MockUtil {
    public static List<UserInfo> buildUserList() {
        List<UserInfo> list = new ArrayList<>();

        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2196657174,1994863858&fm=26&gp=0.jpg");
        userInfo.setName("洛华");
        userInfo.setId("luohua");
        list.add(userInfo);

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setAvatar("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2295559292,2932983657&fm=26&gp=0.jpg");
        userInfo1.setName("紫荆");
        userInfo1.setId("zijing");
        list.add(userInfo1);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2083595345,2930054318&fm=26&gp=0.jpg");
        userInfo2.setName("顷阳");
        userInfo2.setId("qingyang");
        list.add(userInfo2);

        UserInfo userInfo3 = new UserInfo();
        userInfo3.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569992914246&di=e41a41672ffa579358494d1053084805&imgtype=0&src=http%3A%2F%2Fimg.wxcha.com%2Ffile%2F201904%2F08%2Fa0113a1b6c.jpg%3Fdown");
        userInfo3.setName("青岩");
        userInfo3.setId("qingyan");
        list.add(userInfo3);

        UserInfo userInfo4 = new UserInfo();
        userInfo4.setAvatar("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3149318384,2735794996&fm=15&gp=0.jpg");
        userInfo4.setName("梅纹");
        userInfo4.setId("meiwen");
        list.add(userInfo4);

        return list;
    }

    public static UserInfo buildLoginSucData() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2196657174,1994863858&fm=26&gp=0.jpg");
        userInfo.setName("洛华");
        userInfo.setId("luohua");
        return userInfo;
    }

    public static List<String> buildADList() {
        List<String> list = new ArrayList<>();
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=672721443,3070988109&fm=26&gp=0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571744860835&di=a6df90fdb4aa96fa11aa9b47393a3048&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F7afa485b8ae44b419df9425106631c168a12bdf0172b1-b7EtqR_fw658");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571744905068&di=8ce5d0f994ed74bea4360574c6c7f35d&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3fef80e17c9e1e7c5fa8965af6a6800610eb50d22ee0-u9jiq0_fw658");
        return list;
    }
}
