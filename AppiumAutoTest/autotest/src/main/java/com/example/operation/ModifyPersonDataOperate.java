package com.example.operation;

import com.example.base.Assertion;
import com.example.base.OperateAppium;
import com.example.pages.ModifyPersonDataPage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Created by LITP on 2016/9/28.
 */

public class ModifyPersonDataOperate extends OperateAppium {

    private ModifyPersonDataPage page;

    public ModifyPersonDataOperate(AndroidDriver androidDriver) {
        super(androidDriver);
        page = new ModifyPersonDataPage(androidDriver);
    }


    /**
     * 验证进入个人资料页面
     *
     * @return
     */
    public boolean verfiyIntoPage() {
        swipeToRight(1000, 10);
        clickView(page.getPersonDataElement());
        return page.isAlivePersonData();

    }

    /**
     * 验证修改名字
     *
     * @return
     */
    public boolean verfiyChangeName(String name) {
        AndroidElement element = page.getNameElement();
        if(clickView(element)){
            input(page.getNameEditElement(), name);
            if(clickView(page.getNameSureElement())){
                //对比名字是否相等
                return element.getText().equals(name);
            }
        }
        return false;


    }

    /**
     * 验证头像之拍照上传
     *
     * @return
     */
    public boolean verfiyChangeAvatarTake() {
        if (clickView(page.getAvatarElement())) {
            if (clickView(page.getTakePhotoElement())) {

                //我的小米note
                clickView(page.getPermissionElement());  //允许权限
                if (clickView(page.getMIUITakePhotoElement())) {  //点击拍照
                    if (clickView(page.getSurePhotoElement())) {   //点击确定照片
                        if (clickView(page.getSureCropPhotoElement(),"拍照确定裁剪图片")) {
                            boolean flag = page.isAlivePersonData();
                            Assertion.verifyEquals(flag,true,"拍照是否正常");
                            return flag;
                        }
                    }
                }


            }
        }
        return false;
    }

    /**
     * 验证头像之图库选择
     *
     * @return
     */
    public boolean verfiyChangeAvatarSelect() {
        if (clickView(page.getAvatarElement())) {
            if (clickView(page.getSelectPhotoElement())) {

                //我的小米note
                clickView(page.getAlbumElement());
                if (clickView(page.getAlbumPicElement(),"第一张图片")) {

                    if (clickView(page.getSureCropPhotoElement(),"确定裁剪图片")) {
                        return page.isAlivePersonData();
                    }

                }


            }
        }
        return false;
    }


    /**
     * 设置性别为男
     *
     * @return
     */
    public boolean verfiyChangeSexMale() {
        if(clickView(page.getSexElement())){
            AndroidElement element = page.getTakePhotoElement();
            String str = element.getText();
            if(clickView(element)){
                return str.equals(page.getSexElement().getText());
            }
        }
        return false;
    }

    /**
     * 设置性别为女
     *
     * @return
     */
    public boolean verfiyChangeSexFemale() {

        if(clickView(page.getSexElement())){
            AndroidElement element = page.getSelectPhotoElement();
            String str = element.getText();
            if(clickView(element)){
                return str.equals(page.getSexElement().getText());
            }
        }
        return false;
    }

    /**
     * 验证出生日期
     * @return
     */
    public boolean verfiyChangeBirth(){
        if(clickView(page.getBirthElement())){
            //滑动年
            swipOnElement(page.getYearElement(),SWIP_DOWN);
            //滑动月
            swipOnElement(page.getMonthElement(),SWIP_DOWN);
            //滑动日
            swipOnElement(page.getDayElement(),SWIP_DOWN);

            print(page.getYearElement().getText()+"看看年的文本");
            if(clickView(page.getSureBirthElement())){
                return page.isAlivePersonData();
            }
        }
        return false;
    }


    /**
     * 验证邮箱
     * @return
     */
    public boolean verfiyChangeEmail(String email){
        if(clickView(page.getEmailElement())){
            //输入测试邮箱帐号
            input(page.getNameEditElement(),email);
            if(clickView(page.getNameSureElement())){
                return page.isAlivePersonData();
            }
        }
        return false;
    }


    /**
     * 验证提交，这个验证不了
     * @return
     */
    public boolean verfiySubmit(){
        return clickView(page.getSubmitElement(),"点击提交");
    }


}
