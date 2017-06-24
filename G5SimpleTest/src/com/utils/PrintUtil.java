package com.utils;

import android.util.Log;

public class PrintUtil {
	//打印调试信息
	public static void logAndPrint(String str){
		System.out.println("=========" + str);
		Log.d("DEBUGINFO","=========" + str);
	}
	
	//打印结果信息
	public static void resultInfoprint(String str) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>" + str);
		Log.d("RESULTINFO", ">>>>>>>>>>>>>>>>>>>>>>>>>>" + str);
	}
	
	//输出版本信息
	public static void PrintInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android Bluetooth AutoTest v0.0.1======");
		System.out.println("=========       2016-12-30       =================");
		System.out.println("=========        DesaySV         =================");
		System.out.println("==================================================");
		Log.d("DEBUGINFO","=========G5Android Bluetooth AutoTest v0.0.1======");
		Log.d("DEBUGINFO","=========       2016-12-30       =================");
		Log.d("DEBUGINFO","=========        DesaySV         =================");
		Log.d("DEBUGINFO","==================================================");
	}
}
