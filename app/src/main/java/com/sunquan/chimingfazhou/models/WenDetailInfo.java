package com.sunquan.chimingfazhou.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 闻的详情实体类
 *
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailInfo implements Serializable {

    private static final long serialVersionUID = 1190283664556389756L;

    private WenDetailIntroductionInfo introduction;

    private ArrayList<WenDetailListItem> list;

    public WenDetailIntroductionInfo getIntroduction() {
        return introduction;
    }

    public void setIntroduction(WenDetailIntroductionInfo introduction) {
        this.introduction = introduction;
    }

    public ArrayList<WenDetailListItem> getList() {
        return list;
    }

    public void setList(ArrayList<WenDetailListItem> list) {
        this.list = list;
    }
}
