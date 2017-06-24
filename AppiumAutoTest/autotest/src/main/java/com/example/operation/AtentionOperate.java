package com.example.operation;

import com.example.base.OperateAppium;
import com.example.pages.AtentionPage;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by LITP on 2016/9/28.
 */

public class AtentionOperate extends OperateAppium{

    private AtentionPage page;

    public AtentionOperate(AndroidDriver androidDriver) {
        super(androidDriver);
        page = new AtentionPage(androidDriver);
    }


    /**
     * 验证取消关注专家
     * @return
     */
    public boolean verifyCanle(){
        if(clickView(page.getHeadElement())){
            //点击取消关注
            if(clickView(page.getAtentionElement())){
                String name = page.getCardNameElement().getText();
                if(clickView(page.getReturnListElement())){
                    return page.isTextExist(name);
                }
            }
        }

        return false;
    }




    /**
     * 是否存在专家
     * @return
     */
    private boolean isExistExpert(){
        if(page.getRefreshElement()==null){
            if(page.getHeadElement()== null ){
                //没有关注专家
                return false;
            }
        }else if(page.getHeadElement() != null){
            return true;
        }
        return false;
    }


}
