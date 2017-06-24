package com.example.operation;

import com.example.base.OperateAppium;
import com.example.pages.InspectionPage;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by LITP on 2016/9/30.
 */

public class InspectionOperate extends OperateAppium {


    private InspectionPage page;


    public InspectionOperate(AndroidDriver androidDriver) {
        super(androidDriver);
        page = new InspectionPage(androidDriver);
    }



    public boolean intoInspection(){

        if (clickView(page.getInspectionElement())) {
            if(clickView(page.getSearchElement())){
                return true;
            }
        }
        return false;
    }



}
