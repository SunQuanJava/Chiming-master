package com.sunquan.chimingfazhou.util;

import android.content.Context;

import com.baizhi.baseapp.util.UUIDGenerator;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.MainPageHeaderInfo;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.models.SiDetailInfo;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.models.UserInfos;
import com.sunquan.chimingfazhou.models.WenDetailInfo;
import com.sunquan.chimingfazhou.models.WenDetailIntroductionInfo;
import com.sunquan.chimingfazhou.models.WenDetailListItem;

import java.util.ArrayList;

/**
 * 测试数据
 * <p/>
 * Created by Administrator on 2015/5/7.
 */
public final class TestDataUtils {

    public static MainPageInfo getMainPageInfoTestDataForMain() {

        final MainPageInfo mMainPageInfo = new MainPageInfo();
        //假数据
        ArrayList<MainPageHeaderInfo> headerInfos = new ArrayList<>();
        MainPageHeaderInfo mainPageHeaderInfo1 = new MainPageHeaderInfo();
        mainPageHeaderInfo1.setThumbnail("http://touxiang.qqzhi.com/uploads/2012-11/1111120613758.jpg");
        mainPageHeaderInfo1.setDesc("藏密吉祥经院1");
        headerInfos.add(mainPageHeaderInfo1);
        MainPageHeaderInfo mainPageHeaderInfo2 = new MainPageHeaderInfo();
        mainPageHeaderInfo2.setThumbnail("http://www.qq1234.org/uploads/allimg/141103/3_1103200044O91.jpg");
        mainPageHeaderInfo2.setDesc("藏密吉祥经院2");
        headerInfos.add(mainPageHeaderInfo2);
        MainPageHeaderInfo mainPageHeaderInfo3 = new MainPageHeaderInfo();
        mainPageHeaderInfo3.setThumbnail("http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=a22ee99d49ed2e73fcbc8e28b2318dbd/4610b912c8fcc3ce57a9cf2a9145d688d43f20b9.jpg");
        mainPageHeaderInfo3.setDesc("藏密吉祥经院3");
        headerInfos.add(mainPageHeaderInfo3);
        MainPageHeaderInfo mainPageHeaderInfo4 = new MainPageHeaderInfo();
        mainPageHeaderInfo4.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4d08725d4510b912bf94fefaf6cdd035/7aec54e736d12f2e39219b864dc2d5628535682b.jpg");
        mainPageHeaderInfo4.setDesc("藏密吉祥经院4");
        headerInfos.add(mainPageHeaderInfo4);


        mMainPageInfo.setHeader(headerInfos);

        ArrayList<MainPageBodyInfo> mainPageBodyInfos = new ArrayList<>();
        MainPageBodyInfo mainPageBodyInfo1 = new MainPageBodyInfo();
        mainPageBodyInfo1.setTitle("莲师传");
        mainPageBodyInfo1.setSet_count("12");
        mainPageBodyInfo1.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2851b5d308f79052ef4a4f383cc3fbf2/78310a55b319ebc4bfb0b0598326cffc1f171621.jpg");
        mainPageBodyInfo1.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo2 = new MainPageBodyInfo();
        mainPageBodyInfo2.setTitle("密勒日巴尊全传");
        mainPageBodyInfo2.setSet_count("58");
        mainPageBodyInfo2.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2351621a2fdda3cc0bb1b02631d91539/3c6d55fbb2fb4316054581ff21a4462308f7d3d9.jpg");
        mainPageBodyInfo2.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo3 = new MainPageBodyInfo();
        mainPageBodyInfo3.setTitle("龙钦巴全传");
        mainPageBodyInfo3.setSet_count("完结");
        mainPageBodyInfo3.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo3.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo4 = new MainPageBodyInfo();
        mainPageBodyInfo4.setTitle("莲师传");
        mainPageBodyInfo4.setSet_count("12");
        mainPageBodyInfo4.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2851b5d308f79052ef4a4f383cc3fbf2/78310a55b319ebc4bfb0b0598326cffc1f171621.jpg");
        mainPageBodyInfo4.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo5 = new MainPageBodyInfo();
        mainPageBodyInfo5.setTitle("密勒日巴尊全传");
        mainPageBodyInfo5.setSet_count("58");
        mainPageBodyInfo5.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2351621a2fdda3cc0bb1b02631d91539/3c6d55fbb2fb4316054581ff21a4462308f7d3d9.jpg");
        mainPageBodyInfo5.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo6 = new MainPageBodyInfo();
        mainPageBodyInfo6.setTitle("龙钦巴全传");
        mainPageBodyInfo6.setSet_count("完结");
        mainPageBodyInfo6.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo6.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo7 = new MainPageBodyInfo();
        mainPageBodyInfo7.setTitle("麦彭仁波切与侍者的故事1");
        mainPageBodyInfo7.setAuthor("上师仁波切");
        mainPageBodyInfo7.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo8 = new MainPageBodyInfo();
        mainPageBodyInfo8.setTitle("麦彭仁波切与侍者的故事2");
        mainPageBodyInfo8.setAuthor("上师仁波切");
        mainPageBodyInfo8.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo9 = new MainPageBodyInfo();
        mainPageBodyInfo9.setTitle("麦彭仁波切与侍者的故事3");
        mainPageBodyInfo9.setAuthor("上师仁波切");
        mainPageBodyInfo9.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo10 = new MainPageBodyInfo();
        mainPageBodyInfo10.setTitle("麦彭仁波切与侍者的故事4");
        mainPageBodyInfo10.setAuthor("上师仁波切");
        mainPageBodyInfo10.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo11 = new MainPageBodyInfo();
        mainPageBodyInfo11.setTitle("麦彭仁波切与侍者的故事5");
        mainPageBodyInfo11.setAuthor("上师仁波切");
        mainPageBodyInfo11.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo12 = new MainPageBodyInfo();
        mainPageBodyInfo12.setTitle("麦彭仁波切与侍者的故事6");
        mainPageBodyInfo12.setAuthor("上师仁波切");
        mainPageBodyInfo12.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo13 = new MainPageBodyInfo();
        mainPageBodyInfo13.setTitle("麦彭仁波切与侍者的故事7");
        mainPageBodyInfo13.setAuthor("上师仁波切");
        mainPageBodyInfo13.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo14 = new MainPageBodyInfo();
        mainPageBodyInfo14.setTitle("麦彭仁波切与侍者的故事8");
        mainPageBodyInfo14.setAuthor("上师仁波切");
        mainPageBodyInfo14.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo15 = new MainPageBodyInfo();
        mainPageBodyInfo15.setTitle("麦彭仁波切与侍者的故事9");
        mainPageBodyInfo15.setAuthor("上师仁波切");
        mainPageBodyInfo15.setType(MainPageBodyInfo.SI);

        mainPageBodyInfos.add(mainPageBodyInfo1);
        mainPageBodyInfos.add(mainPageBodyInfo2);
        mainPageBodyInfos.add(mainPageBodyInfo3);
        mainPageBodyInfos.add(mainPageBodyInfo4);
        mainPageBodyInfos.add(mainPageBodyInfo5);
        mainPageBodyInfos.add(mainPageBodyInfo6);
        mainPageBodyInfos.add(mainPageBodyInfo7);
        mainPageBodyInfos.add(mainPageBodyInfo8);
        mainPageBodyInfos.add(mainPageBodyInfo9);
        mainPageBodyInfos.add(mainPageBodyInfo10);
        mainPageBodyInfos.add(mainPageBodyInfo11);
        mainPageBodyInfos.add(mainPageBodyInfo12);
        mainPageBodyInfos.add(mainPageBodyInfo13);
        mainPageBodyInfos.add(mainPageBodyInfo14);
        mainPageBodyInfos.add(mainPageBodyInfo15);

        for(MainPageBodyInfo mainPageBodyInfo:mainPageBodyInfos) {
            mainPageBodyInfo.setId(UUIDGenerator.getUUID());
        }

        mMainPageInfo.setBody(mainPageBodyInfos);

        return mMainPageInfo;
    }

    public static MainPageInfo getMainPageInfoTestDataForWen() {

        final MainPageInfo mMainPageInfo = new MainPageInfo();
        //假数据
        ArrayList<MainPageBodyInfo> mainPageBodyInfos = new ArrayList<>();
        MainPageBodyInfo mainPageBodyInfo1 = new MainPageBodyInfo();
        mainPageBodyInfo1.setTitle("莲师传");
        mainPageBodyInfo1.setSet_count("12");
        mainPageBodyInfo1.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2851b5d308f79052ef4a4f383cc3fbf2/78310a55b319ebc4bfb0b0598326cffc1f171621.jpg");
        mainPageBodyInfo1.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo2 = new MainPageBodyInfo();
        mainPageBodyInfo2.setTitle("密勒日巴尊全传");
        mainPageBodyInfo2.setSet_count("58");
        mainPageBodyInfo2.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2351621a2fdda3cc0bb1b02631d91539/3c6d55fbb2fb4316054581ff21a4462308f7d3d9.jpg");
        mainPageBodyInfo2.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo3 = new MainPageBodyInfo();
        mainPageBodyInfo3.setTitle("龙钦巴全传");
        mainPageBodyInfo3.setSet_count("完结");
        mainPageBodyInfo3.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo3.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo4 = new MainPageBodyInfo();
        mainPageBodyInfo4.setTitle("甘露明珠");
        mainPageBodyInfo4.setSet_count("12");
        mainPageBodyInfo4.setThumbnail("http://wenwen.soso.com/p/20111105/20111105224324-483943250.jpg");
        mainPageBodyInfo4.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo5 = new MainPageBodyInfo();
        mainPageBodyInfo5.setTitle("塔洛仁波切念诵");
        mainPageBodyInfo5.setSet_count("58");
        mainPageBodyInfo5.setThumbnail("http://wenwen.soso.com/p/20110223/20110223145646-1430824858.jpg");
        mainPageBodyInfo5.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo6 = new MainPageBodyInfo();
        mainPageBodyInfo6.setTitle("龙钦巴全传");
        mainPageBodyInfo6.setSet_count("完结");
        mainPageBodyInfo6.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo6.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo7 = new MainPageBodyInfo();
        mainPageBodyInfo7.setTitle("莲师传");
        mainPageBodyInfo7.setSet_count("12");
        mainPageBodyInfo7.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2851b5d308f79052ef4a4f383cc3fbf2/78310a55b319ebc4bfb0b0598326cffc1f171621.jpg");
        mainPageBodyInfo7.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo8 = new MainPageBodyInfo();
        mainPageBodyInfo8.setTitle("密勒日巴尊全传");
        mainPageBodyInfo8.setSet_count("58");
        mainPageBodyInfo8.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2351621a2fdda3cc0bb1b02631d91539/3c6d55fbb2fb4316054581ff21a4462308f7d3d9.jpg");
        mainPageBodyInfo8.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo9 = new MainPageBodyInfo();
        mainPageBodyInfo9.setTitle("龙钦巴全传");
        mainPageBodyInfo9.setSet_count("完结");
        mainPageBodyInfo9.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo9.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo10 = new MainPageBodyInfo();
        mainPageBodyInfo10.setTitle("甘露明珠");
        mainPageBodyInfo10.setSet_count("12");
        mainPageBodyInfo10.setThumbnail("http://wenwen.soso.com/p/20111105/20111105224324-483943250.jpg");
        mainPageBodyInfo10.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo11 = new MainPageBodyInfo();
        mainPageBodyInfo11.setTitle("塔洛仁波切念诵");
        mainPageBodyInfo11.setSet_count("58");
        mainPageBodyInfo11.setThumbnail("http://wenwen.soso.com/p/20110223/20110223145646-1430824858.jpg");
        mainPageBodyInfo11.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo12 = new MainPageBodyInfo();
        mainPageBodyInfo12.setTitle("龙钦巴全传");
        mainPageBodyInfo12.setSet_count("完结");
        mainPageBodyInfo12.setThumbnail("http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2cff4225b11c8701d6e3bae2124fb219/c2cec3fdfc039245d4041dce8594a4c27c1e25b9.jpg");
        mainPageBodyInfo12.setType(MainPageBodyInfo.WEN);

        MainPageBodyInfo mainPageBodyInfo13 = new MainPageBodyInfo();
        mainPageBodyInfo13.setTitle("莲师传");
        mainPageBodyInfo13.setSet_count("12");
        mainPageBodyInfo13.setThumbnail("http://a.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=2851b5d308f79052ef4a4f383cc3fbf2/78310a55b319ebc4bfb0b0598326cffc1f171621.jpg");
        mainPageBodyInfo13.setType(MainPageBodyInfo.WEN);

        mainPageBodyInfos.add(mainPageBodyInfo1);
        mainPageBodyInfos.add(mainPageBodyInfo2);
        mainPageBodyInfos.add(mainPageBodyInfo3);
        mainPageBodyInfos.add(mainPageBodyInfo4);
        mainPageBodyInfos.add(mainPageBodyInfo5);
        mainPageBodyInfos.add(mainPageBodyInfo6);
        mainPageBodyInfos.add(mainPageBodyInfo7);
        mainPageBodyInfos.add(mainPageBodyInfo8);
        mainPageBodyInfos.add(mainPageBodyInfo9);
        mainPageBodyInfos.add(mainPageBodyInfo10);
        mainPageBodyInfos.add(mainPageBodyInfo11);
        mainPageBodyInfos.add(mainPageBodyInfo12);
        mainPageBodyInfos.add(mainPageBodyInfo13);

        for(MainPageBodyInfo mainPageBodyInfo:mainPageBodyInfos) {
            mainPageBodyInfo.setId(UUIDGenerator.getUUID());
        }

        mMainPageInfo.setBody(mainPageBodyInfos);

        return mMainPageInfo;
    }

    public static MainPageInfo getMainPageInfoTestDataForSiSub1() {
        final MainPageInfo mMainPageInfo = new MainPageInfo();
        //假数据
        ArrayList<MainPageBodyInfo> mainPageBodyInfos = new ArrayList<>();

        MainPageBodyInfo mainPageBodyInfo1 = new MainPageBodyInfo();
        mainPageBodyInfo1.setTitle("麦彭仁波切与侍者的故事1");
        mainPageBodyInfo1.setAuthor("上师仁波切");
        mainPageBodyInfo1.setCreate_date("2015.03.1");
        mainPageBodyInfo1.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo2 = new MainPageBodyInfo();
        mainPageBodyInfo2.setTitle("麦彭仁波切与侍者的故事2");
        mainPageBodyInfo2.setAuthor("上师仁波切");
        mainPageBodyInfo2.setCreate_date("2015.03.1");
        mainPageBodyInfo2.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo3 = new MainPageBodyInfo();
        mainPageBodyInfo3.setTitle("麦彭仁波切与侍者的故事3");
        mainPageBodyInfo3.setAuthor("上师仁波切");
        mainPageBodyInfo3.setCreate_date("2015.03.1");
        mainPageBodyInfo3.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo4 = new MainPageBodyInfo();
        mainPageBodyInfo4.setTitle("麦彭仁波切与侍者的故事4");
        mainPageBodyInfo4.setAuthor("上师仁波切");
        mainPageBodyInfo4.setCreate_date("2015.03.1");
        mainPageBodyInfo4.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo5 = new MainPageBodyInfo();
        mainPageBodyInfo5.setTitle("麦彭仁波切与侍者的故事5");
        mainPageBodyInfo5.setAuthor("上师仁波切");
        mainPageBodyInfo5.setCreate_date("2015.03.1");
        mainPageBodyInfo5.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo6 = new MainPageBodyInfo();
        mainPageBodyInfo6.setTitle("麦彭仁波切与侍者的故事6");
        mainPageBodyInfo6.setAuthor("上师仁波切");
        mainPageBodyInfo6.setCreate_date("2015.03.1");
        mainPageBodyInfo6.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo7 = new MainPageBodyInfo();
        mainPageBodyInfo7.setTitle("麦彭仁波切与侍者的故事7");
        mainPageBodyInfo7.setAuthor("上师仁波切");
        mainPageBodyInfo7.setCreate_date("2015.03.1");
        mainPageBodyInfo7.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo8 = new MainPageBodyInfo();
        mainPageBodyInfo8.setTitle("麦彭仁波切与侍者的故事8");
        mainPageBodyInfo8.setAuthor("上师仁波切");
        mainPageBodyInfo8.setCreate_date("2015.03.1");
        mainPageBodyInfo8.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo9 = new MainPageBodyInfo();
        mainPageBodyInfo9.setTitle("麦彭仁波切与侍者的故事9");
        mainPageBodyInfo9.setAuthor("上师仁波切");
        mainPageBodyInfo9.setCreate_date("2015.03.1");
        mainPageBodyInfo9.setType(MainPageBodyInfo.SI);

        mainPageBodyInfos.add(mainPageBodyInfo1);
        mainPageBodyInfos.add(mainPageBodyInfo2);
        mainPageBodyInfos.add(mainPageBodyInfo3);
        mainPageBodyInfos.add(mainPageBodyInfo4);
        mainPageBodyInfos.add(mainPageBodyInfo5);
        mainPageBodyInfos.add(mainPageBodyInfo6);
        mainPageBodyInfos.add(mainPageBodyInfo7);
        mainPageBodyInfos.add(mainPageBodyInfo8);
        mainPageBodyInfos.add(mainPageBodyInfo9);

        for(MainPageBodyInfo mainPageBodyInfo:mainPageBodyInfos) {
            mainPageBodyInfo.setId(UUIDGenerator.getUUID());
        }

        mMainPageInfo.setBody(mainPageBodyInfos);

        return mMainPageInfo;
    }

    public static MainPageInfo getMainPageInfoTestDataForSiSub2() {
        final MainPageInfo mMainPageInfo = new MainPageInfo();
        //假数据
        ArrayList<MainPageBodyInfo> mainPageBodyInfos = new ArrayList<>();

        MainPageBodyInfo mainPageBodyInfo1 = new MainPageBodyInfo();
        mainPageBodyInfo1.setTitle("身上不舒服，肯定是冤亲债主");
        mainPageBodyInfo1.setAuthor("净空法师");
        mainPageBodyInfo1.setCreate_date("2015.03.1");
        mainPageBodyInfo1.setThumbnail("http://www.dongfei315.com/img/aHR0cDovL2ltZzEuamd4ZncuY29tL3FxdG91eGlhbmcvMjAxMy8wOS8xNi8xNC8xNDI1MDAtMjAxMzA5MTY5ODUuanBn.jpg");
        mainPageBodyInfo1.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo2 = new MainPageBodyInfo();
        mainPageBodyInfo2.setTitle("什么是“息，增，怀，诛”的最高成就");
        mainPageBodyInfo2.setAuthor("宗萨蒋扬钦哲仁波切");
        mainPageBodyInfo2.setThumbnail("http://img0w.pconline.com.cn/pconline/1309/05/3456400_32.jpg");
        mainPageBodyInfo2.setCreate_date("2015.03.1");
        mainPageBodyInfo2.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo3 = new MainPageBodyInfo();
        mainPageBodyInfo3.setTitle("真正拥有出离心的标准");
        mainPageBodyInfo3.setThumbnail("http://www.dongfei315.com/img/aHR0cDovL2ltZzEuamd4ZncuY29tL3FxdG91eGlhbmcvMjAxMy8wOS8xNi8xNC8xNDI1MDAtMjAxMzA5MTY5ODUuanBn.jpg");
        mainPageBodyInfo3.setAuthor("宗萨蒋扬钦哲仁波切");
        mainPageBodyInfo3.setCreate_date("2015.03.1");
        mainPageBodyInfo3.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo4 = new MainPageBodyInfo();
        mainPageBodyInfo4.setTitle("无需花费的布施");
        mainPageBodyInfo4.setAuthor("达真堪布");
        mainPageBodyInfo4.setThumbnail("http://img0w.pconline.com.cn/pconline/1310/16/3639381_01.jpg");
        mainPageBodyInfo4.setCreate_date("2015.03.1");
        mainPageBodyInfo4.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo5 = new MainPageBodyInfo();
        mainPageBodyInfo5.setTitle("身上不舒服，肯定是冤亲债主");
        mainPageBodyInfo5.setAuthor("净空法师");
        mainPageBodyInfo5.setThumbnail("http://www.dongfei315.com/img/aHR0cDovL2ltZzEuamd4ZncuY29tL3FxdG91eGlhbmcvMjAxMy8wOS8xNi8xNC8xNDI1MDAtMjAxMzA5MTY5ODUuanBn.jpg");
        mainPageBodyInfo5.setCreate_date("2015.03.1");
        mainPageBodyInfo5.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo6 = new MainPageBodyInfo();
        mainPageBodyInfo6.setTitle("什么是“息，增，怀，诛”的最高成就最高成就");
        mainPageBodyInfo6.setAuthor("宗萨蒋扬钦哲仁波切");
        mainPageBodyInfo6.setThumbnail("http://www.dongfei315.com/img/aHR0cDovL2ltZzEuamd4ZncuY29tL3FxdG91eGlhbmcvMjAxMy8wOS8xNi8xNC8xNDI1MDAtMjAxMzA5MTY5ODUuanBn.jpg");
        mainPageBodyInfo6.setCreate_date("2015.03.1");
        mainPageBodyInfo6.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo7 = new MainPageBodyInfo();
        mainPageBodyInfo7.setTitle("真正拥有出离心的标准");
        mainPageBodyInfo7.setAuthor("宗萨蒋扬钦哲仁波切");
        mainPageBodyInfo7.setThumbnail("http://img0w.pconline.com.cn/pconline/1309/05/3456400_32.jpg");
        mainPageBodyInfo7.setCreate_date("2015.03.1");
        mainPageBodyInfo7.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo8 = new MainPageBodyInfo();
        mainPageBodyInfo8.setTitle("无需花费的布施");
        mainPageBodyInfo8.setAuthor("达真堪布");
        mainPageBodyInfo8.setThumbnail("http://img0w.pconline.com.cn/pconline/1310/16/3639381_01.jpg");
        mainPageBodyInfo8.setCreate_date("2015.03.1");
        mainPageBodyInfo8.setType(MainPageBodyInfo.SI);

        MainPageBodyInfo mainPageBodyInfo9 = new MainPageBodyInfo();
        mainPageBodyInfo9.setTitle("身上不舒服，肯定是冤亲债主");
        mainPageBodyInfo9.setAuthor("净空法师");
        mainPageBodyInfo9.setThumbnail("http://www.dongfei315.com/img/aHR0cDovL2ltZzEuamd4ZncuY29tL3FxdG91eGlhbmcvMjAxMy8wOS8xNi8xNC8xNDI1MDAtMjAxMzA5MTY5ODUuanBn.jpg");
        mainPageBodyInfo9.setCreate_date("2015.03.1");
        mainPageBodyInfo9.setType(MainPageBodyInfo.SI);

        mainPageBodyInfos.add(mainPageBodyInfo1);
        mainPageBodyInfos.add(mainPageBodyInfo2);
        mainPageBodyInfos.add(mainPageBodyInfo3);
        mainPageBodyInfos.add(mainPageBodyInfo4);
        mainPageBodyInfos.add(mainPageBodyInfo5);
        mainPageBodyInfos.add(mainPageBodyInfo6);
        mainPageBodyInfos.add(mainPageBodyInfo7);
        mainPageBodyInfos.add(mainPageBodyInfo8);
        mainPageBodyInfos.add(mainPageBodyInfo9);

        for(MainPageBodyInfo mainPageBodyInfo:mainPageBodyInfos) {
            mainPageBodyInfo.setId(UUIDGenerator.getUUID());
        }

        mMainPageInfo.setBody(mainPageBodyInfos);

        return mMainPageInfo;
    }

    public static WenDetailInfo getWenTestData() {
        final WenDetailInfo wenDetailInfo = new WenDetailInfo();
        final WenDetailIntroductionInfo introductionInfo = new WenDetailIntroductionInfo();
        introductionInfo.setThumbnail("http://img4q.duitang.com/uploads/item/201502/27/20150227130642_Eme4A.thumb.224_0.jpeg");
        introductionInfo.setTitle("莲师传");
        introductionInfo.setScore("4");
        introductionInfo.setAuthor("伊喜.措嘉");
        introductionInfo.setBroadcast("王猛");
        introductionInfo.setSet_count("42");
        introductionInfo.setBrief("莲花生大士");
        introductionInfo.setCreate_date("2015.01.13");
        introductionInfo.setBrief("莲花生大士，印度佛教史上最伟大的大成就者之一。公元八世纪，应藏王赤松德赞迎请入藏弘法，成功创立了西藏第一座佛、法、僧三宝齐全的佛教寺院——桑耶寺。他教导藏族弟子学习译经，从印度迎请无垢友等大德入藏，将重要显密经论译成藏文，创建显密经院及密宗道场，开创了在家出家的两种圣者应供轨范，如是等等，奠定了西藏佛教的基础。");

        ArrayList<WenDetailListItem> listItems = new ArrayList<>();
        for(int i=0;i<20;i++) {
            final WenDetailListItem listItem = new WenDetailListItem();
            listItem.setTitle("第"+(i+1)+"集");
            listItem.setDownload_url("http://wting.info:81/asdb/renwensheke/tndmm/jvmwybd6.mp3");
            listItem.setSize("4608659");
            listItems.add(listItem);
        }
        wenDetailInfo.setIntroduction(introductionInfo);
        wenDetailInfo.setList(listItems);
        return wenDetailInfo;
    }

    public static SiDetailInfo getSiTestData() {
        final SiDetailInfo siDetailInfo = new SiDetailInfo();
        siDetailInfo.setId("001");
        siDetailInfo.setLink("http://798.rabbitor.com/api/news/60010?l=cn");
        return siDetailInfo;
    }

    public static UserInfo login(Context context, String phone, String password, String code) {
        final UserInfo userInfo;
        userInfo = new UserInfo();
        userInfo.setNickname("孙泉");
        userInfo.setFarmington("办事求码");
        userInfo.setPhoto("");
        userInfo.setUid("001");
        userInfo.setPhone(phone);
        userInfo.setPassword(password);
        GlobalDataHolder.getInstance(context).setUserInfo(userInfo);
        GlobalDataHolder.getInstance(context).initXiuDatasFromDB();
        return userInfo;
    }

    public static UserInfo modify(Context context,UserInfo tempUserInfo) {
        final UserInfo userInfo;
        userInfo = new UserInfo();
        userInfo.setNickname(tempUserInfo.getNickname());
        userInfo.setFarmington(tempUserInfo.getFarmington());
        userInfo.setPhoto(tempUserInfo.getPhoto());
        userInfo.setProvince(tempUserInfo.getProvince());
        userInfo.setCity(tempUserInfo.getCity());
        userInfo.setLocation(tempUserInfo.getLocation());
        userInfo.setGender(tempUserInfo.getGender());
        userInfo.setUid(tempUserInfo.getUid());
        userInfo.setPassword(tempUserInfo.getPassword());
        userInfo.setDescription(tempUserInfo.getDescription());
        userInfo.setPhone(tempUserInfo.getPhone());
        GlobalDataHolder.getInstance(context).setUserInfo(userInfo);
        return userInfo;
    }

    public static UserInfo register(Context context, String phone, String password) {
        final UserInfo userInfo;
        userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(password);
        userInfo.setUid("001");
        GlobalDataHolder.getInstance(context).setUserInfo(userInfo);
        GlobalDataHolder.getInstance(context).initXiuDatasFromDB();
        return userInfo;
    }

    public static UserInfo modifyPassword(Context context, String password) {
        final UserInfo userInfo = GlobalDataHolder.getInstance(context).getUserInfo();
        userInfo.setPassword(password);
        GlobalDataHolder.getInstance(context).setUserInfo(userInfo);
        return userInfo;
    }

    public static UserInfos getMemberData() {
        final UserInfo userInfo1 = new UserInfo();
        userInfo1.setNickname("李大刚");
        userInfo1.setFarmington("八戒");
        userInfo1.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126982091223501-lp.jpg");

        final UserInfo userInfo2 = new UserInfo();
        userInfo2.setNickname("李二刚");
        userInfo2.setFarmington("悟空");
        userInfo2.setPhoto("http://img.w944.com/img/f54eda539580aced.jpg");

        final UserInfo userInfo3 = new UserInfo();
        userInfo3.setNickname("李天刚");
        userInfo3.setFarmington("悟净");
        userInfo3.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126989091223501-lp.jpg");

        final UserInfo userInfo4 = new UserInfo();
        userInfo4.setNickname("沈秋阳");
        userInfo4.setFarmington("玄奘");
        userInfo4.setPhoto("http://img.w944.com/img/6ca3cd8388ef1cde.jpg");

        final UserInfo userInfo5 = new UserInfo();
        userInfo5.setNickname("李大刚");
        userInfo5.setFarmington("八戒");
        userInfo5.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126982091223501-lp.jpg");

        final UserInfo userInfo6 = new UserInfo();
        userInfo6.setNickname("李二刚");
        userInfo6.setFarmington("悟空");
        userInfo6.setPhoto("http://img.w944.com/img/f54eda539580aced.jpg");

        final UserInfo userInfo7 = new UserInfo();
        userInfo7.setNickname("李天刚");
        userInfo7.setFarmington("悟净");
        userInfo7.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126989091223501-lp.jpg");

        final UserInfo userInfo8 = new UserInfo();
        userInfo8.setNickname("沈秋阳");
        userInfo8.setFarmington("玄奘");
        userInfo8.setPhoto("http://img.w944.com/img/6ca3cd8388ef1cde.jpg");

        final UserInfo userInfo9 = new UserInfo();
        userInfo9.setNickname("李大刚");
        userInfo9.setFarmington("八戒");
        userInfo9.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126982091223501-lp.jpg");

        final UserInfo userInfo10 = new UserInfo();
        userInfo10.setNickname("李二刚");
        userInfo10.setFarmington("悟空");
        userInfo10.setPhoto("http://img.w944.com/img/f54eda539580aced.jpg");

        final UserInfo userInfo11 = new UserInfo();
        userInfo11.setNickname("李天刚");
        userInfo11.setFarmington("悟净");
        userInfo11.setPhoto("http://img.web07.cn/UpPic/QQ/201408/09/126989091223501-lp.jpg");

        final UserInfo userInfo12 = new UserInfo();
        userInfo12.setNickname("沈秋阳");
        userInfo12.setFarmington("玄奘");
        userInfo12.setPhoto("http://img.w944.com/img/6ca3cd8388ef1cde.jpg");

        final UserInfos userInfos = new UserInfos();
        userInfos.add(userInfo1);
        userInfos.add(userInfo2);
        userInfos.add(userInfo3);
        userInfos.add(userInfo4);
        userInfos.add(userInfo5);
        userInfos.add(userInfo6);
        userInfos.add(userInfo7);
        userInfos.add(userInfo8);
        userInfos.add(userInfo9);
        userInfos.add(userInfo10);
        userInfos.add(userInfo11);
        userInfos.add(userInfo12);

        return userInfos;
    }
}
