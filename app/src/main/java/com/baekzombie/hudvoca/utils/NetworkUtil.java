/*
 * **********************************
 *
 * Copyright (c) 2016 kt Inc. All rights reserved.
 *
 * This is a proprietary software of kt Inc. and you may not use this file except in compliance with license agreement
 * with kt Inc. Any redistribution or use of this software, with or without modification shall be strictly prohibited without
 * prior written approval of kt Inc. and the copyright notice above does not evidence any actual or intended publication
 * of such software.
 *
 * **********************************
 */

package com.baekzombie.hudvoca.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 네트워크에 필요한 부분들이 구현되어 있는 클래스</br>
 * 네트워크 연결체크등, 연결된 타입등을 알 수 있는 기능들이 있다.
 * 
 * @author hyunsikkim
 *
 */
public class NetworkUtil {
	public static final int OFF = -1;
	public static final int WIFI = 0;
	public static final int MOBILE3G = 1;
	public static final int MOBILE4G = 2;
	public static final int ETC = 3;
	
	public static final int SUB_TYPE_LTE = 13;
	private NetworkUtil() {
	}

	/**
	 * 현재 접속된 네트웍 정보를 반환한다.
	 * @param context
	 * @return connection network type
	 */
	public static int getCurrentConnection(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			return OFF;
		}
		
		switch(networkInfo.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				return WIFI;
			case ConnectivityManager.TYPE_MOBILE:
				if(networkInfo.getSubtype() == SUB_TYPE_LTE)
					return MOBILE4G;
				else
					return MOBILE3G;
			case ConnectivityManager.TYPE_WIMAX:
				return ETC;
			default:
				break;
		}
		
		return OFF;
	}
	
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * connected network is wifi
	 * @param context
	 * @return isWifiCondected
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo wifi = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi != null && wifi.isAvailable() && wifi.isConnected()) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 3G/4G 여부를 판단하며 사내 보안정책으로인해 로그인 프로세스상에서 사용하게 된다.</br>
	 * 3G/4G일 경우 인승방식을 확인하는 절차를 거치게 되고,</br>
	 * 아닐 경우 통신사 망 사용이 가능한 버전인지 체크하는 프로세스를 거치게 된다.</br>
	 * @return connected mobile type
	 */
	public static boolean isMobileConnected(Context context) {
		boolean isXg = false;
		
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		
		switch(networkInfo.getType()) {
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_WIMAX:
			isXg = true;
			break;
		default:
			break;
		}
		
		return isXg;
	}
}