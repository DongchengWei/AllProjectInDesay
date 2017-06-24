package com.example.pages;

import com.example.base.PageAppium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Created by LITP on 2016/9/28.
 */

public class AtentionPage extends PageAppium {


    public AtentionPage(AndroidDriver androidDriver) {
        super(androidDriver);
    }



    //有关注的人的头像id
    private final String HEAD_FLAG_ID = "doctor_item_iv_avatar";

    //取消关注按钮
    private final String ATENTION_FLAG_ID = "attention_component_textview";
    //返回关注列表按钮
    private final String RETURN_LIST_FLAG_ID = "personal_card_close";

    //正在刷新的text
    private final String REFRESH_FLAG_ID = "xlistview_header_hint_textview";

    //资料卡名字Id
    private final String CARD_NAME_FLAG_ID = "personal_card_name";



    public AndroidElement getHeadElement(){
        return findById(HEAD_FLAG_ID);

    }

    public AndroidElement getWaitHeadElement(){
        return waitAutoById(HEAD_FLAG_ID,5);

    }

    /**
     * 获取正在刷新的文本控件
     * @return
     */
    public AndroidElement getRefreshElement(){
        return findById(REFRESH_FLAG_ID);
    }


    /**
     * 获取关注的按钮控件
     * @return
     */
    public AndroidElement getAtentionElement(){
        return findById(ATENTION_FLAG_ID);
    }

    /**
     * 获取关注的按钮控件
     * @return
     */
    public AndroidElement getReturnListElement(){
        return findById(RETURN_LIST_FLAG_ID);
    }


    /**
     * 获取资料卡的文本控件
     * @return
     */
    public AndroidElement getCardNameElement(){
        return findById(CARD_NAME_FLAG_ID);
    }


}
