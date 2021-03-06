package com.base;

import com.google.common.base.Function;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by LITP on 2016/9/8.
 */
@SuppressWarnings("rawtypes")
public interface ExpectedCondition<T> extends Function<AndroidDriver, T> {}