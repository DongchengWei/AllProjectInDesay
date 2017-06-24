package com.example.pages;

import com.example.base.PageAppium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Created by LITP on 2016/9/28.
 */

public class ModifyPersonDataPage extends PageAppium {


    public ModifyPersonDataPage(AndroidDriver androidDriver) {
        super(androidDriver);
    }

    //侧边栏的个人资料按钮的id
    private final String PERSON_DATA_ID = "slidingmenu_personal";

    //个人资料界面的TextView文本标识
    private final String PERSON_DATA_FLAG_TEXT = "基本信息";

    //头像的ImageView的 id
    private final String AVATAR_FLAG_ID = "my_slidingmenu_avatar";
    //拍照上传、性别男 Button的 id
    private final String AVATAR_TAKE_PHOTO_FLAG_ID = "popwindow_otherfeedlist_htv_male";
    //图库上传、性别女 Button的 id
    private final String AVATAR_SELECT_PHOTO_FLAG_ID = "popwindow_otherfeedlist_htv_female";
    //取消选择的 id
    private final String AVATAR_CANCLE_FLAG_ID = "popwindow_otherfeedlist_htv_cancel";

    //拍照上传头像的权限的提允许按钮id
    private final String AVATAR_ALBUM_TEXT = "相册";
    //相册第一张图片id
    private final String AVATAR_ALBUM_PIC_ID = "com.miui.gallery:id/pick_num_indicator";

    //拍照上传头像的权限的提允许按钮id
    private final String AVATAR_PERMISSION_BUTTON_ID = "android:id/button1";
    //拍照按钮
    private final String AVATAR_TAKE_BUTTON_ID = "com.android.camera:id/v6_shutter_button_internal";
    //确定照片按钮
    private final String AVATAR_SURE_PHOTO_BUTTON_ID = "com.android.camera:id/v6_btn_done";
    //裁剪确定照片按钮
    private final String AVATAR_SURE_CROP_PHOTO_BUTTON_ID = "headeright_htv_r";



    //名字的TextView的 id
    private final String NAME_FLAG_ID = "my_slidingmenu_name";
    //修改名字的EditText的 id  （没有，直接使用input）
    //private final String PERSON_DATA_CHANGE_NAME_FLAG_ID = "my_slidingmenu_name";
    //确认修改名字的Button的 id
    private final String CHANGE_NAME_FLAG_ID = "dialog_generic_btn_button2";

    //性别、、日期、邮箱的TextView的 id
    private final String SEX_FLAG_ID = "reg_baseinfo_et";

    //年月日的id
    private final String YEAR_FLAG_ID = "year", MONTH_FLAG_ID = "month",DAY_FLAG_ID = "day";
    //确定修改年月日按钮id
    private final String BIRTH_SURE_FLAG_ID = "birthday_window_btn_sure";


    //提交按钮Button的 id
    private final String SUBMIT_FLAG_ID = "apply_btn_commit";



    /**
     * 获取点击拍照上传 、 性别男 按钮
     * @return
     */
    public AndroidElement getTakePhotoElement(){
        return findById(AVATAR_TAKE_PHOTO_FLAG_ID);
    }
    /**
     * 获取点击图库上传按钮
     * @return
     */
    public AndroidElement getSelectPhotoElement(){
        return findById(AVATAR_SELECT_PHOTO_FLAG_ID);
    }
    /**
     * 获取选择图片的相册
     * @return
     */
    public AndroidElement getAlbumElement(){
        return findByXpath("//android.widget.TextView[@text='"+AVATAR_ALBUM_TEXT+"']");
    }

    /**
     * 获取相册里照片的id
     * @return
     */
    public AndroidElement getAlbumPicElement(){
        return findByFullId(AVATAR_ALBUM_PIC_ID);
    }
    /**
     * 获取允许权限按钮
     * @return
     */
    public AndroidElement getPermissionElement(){
        return findByFullId(AVATAR_PERMISSION_BUTTON_ID);
    }

    /**
     * 获取拍照按钮
     * @return
     */
    public AndroidElement getMIUITakePhotoElement(){
        return findByFullId(AVATAR_TAKE_BUTTON_ID);
    }

    /**
     * 获取确定照片按钮
     * @return
     */
    public AndroidElement getSurePhotoElement(){
        return findByFullId(AVATAR_SURE_PHOTO_BUTTON_ID);
    }
    /**
     * 获取确定裁剪照片按钮
     * @return
     */
    public AndroidElement getSureCropPhotoElement(){
        return waitAutoById(AVATAR_SURE_CROP_PHOTO_BUTTON_ID,5);
    }
    /**
     * 获取头像控件
     * @return
     */
    public AndroidElement getAvatarElement(){
        return findById(AVATAR_FLAG_ID);
    }



    /**
     * 侧边栏是否存在个人资料的按钮id
     * @return
     */
    public AndroidElement getPersonDataElement(){
        return findById(PERSON_DATA_ID);
    }




    /**
     * 是否在个人资料界面
     * @return
     */
    public boolean isAlivePersonData(){
        return isXpathExist("//android.widget.TextView[@text='"+PERSON_DATA_FLAG_TEXT+"']");
    }


    /**
     * 获取个人资料界面的名字的textView
     * @return
     */
    public AndroidElement getNameElement(){
        return findById(NAME_FLAG_ID);
    }


    /**
     * 获取输入名字的控件
     * @return
     */
    public AndroidElement getNameEditElement(){
        return findByClassName("android.widget.EditText");
    }

    /**
     * 获取确定修改名字的控件
     * @return
     */
    public AndroidElement getNameSureElement(){
        return findById(CHANGE_NAME_FLAG_ID);
    }

    /**
     * 获取性别的TextView控件
     * @return
     */
    public AndroidElement getSexElement(){
        return findById(SEX_FLAG_ID);
    }


    /**
     * 获取出生日期的TextView控件
     * @return
     */
    public AndroidElement getBirthElement(){
        return getListOneElementById(SEX_FLAG_ID,1);
    }

    /**
     * 获取年控件
     * @return
     */
    public AndroidElement getYearElement(){
        return findById(YEAR_FLAG_ID);

    }

    /**
     * 获取月控件
     * @return
     */
    public AndroidElement getMonthElement(){
        return findById(MONTH_FLAG_ID);

    }

    /**
     * 获取日控件
     * @return
     */
    public AndroidElement getDayElement(){
        return findById(DAY_FLAG_ID);

    }

    /**
     * 获取确定修改日期控件
     * @return
     */
    public AndroidElement getSureBirthElement(){
        return findById(BIRTH_SURE_FLAG_ID);

    }

    /**
     * 获取邮箱的TextView控件
     * @return
     */
    public AndroidElement getEmailElement(){
        return getListOneElementById(SEX_FLAG_ID,2);
    }


    /**
     * 获取提交Button控件
     * @return
     */
    public AndroidElement getSubmitElement(){
        return findById(SUBMIT_FLAG_ID);
    }


}
