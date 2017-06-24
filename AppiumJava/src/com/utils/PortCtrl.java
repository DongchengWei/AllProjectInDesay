package com.utils;

import java.util.ArrayList;
import java.util.List;

public class PortCtrl {

	   /**
     * 判断端口是否被占用
     *
     * @param portNum 端口号
     * @return
     */
    private static Boolean isPortUsed(int portNum) {

        List<String> portRes = new ArrayList<>();

        boolean flag = true;   //是否被占用

        try {
            //
            portRes = CmdCtrl.getInstance().execCmd("netstat -an|findstr " + portNum);
            if (portRes.size() > 0) {
                System.out.println("端口" + portNum + "已被占用");
            } else {
                System.out.println("端口" + portNum + "没有被占用");
                flag = false;
            }
            return flag;
        } catch (Exception e) {
            System.out.println("获取端口占用情况失败!=");
        }


        return flag;

    }


    /**
     * 创建可用的端口列表,是个设备就是20个端口，因为一个设备有2个端口需要开通
     *
     * @param startPort    开始的端口
     * @param devicesTotal 设备总数
     * @return
     */
    public static List<Integer> createPortList(int startPort, int devicesTotal) {
        List<Integer> portList = new ArrayList<>();
        while (portList.size() != devicesTotal) {
            if (startPort > 0 && startPort < 65535) {
                if(!isPortUsed(startPort)){
                    portList.add(startPort);
                }
                startPort = startPort + 1;
            }

        }

        return portList;

    }


    /**
     * 根据设备数量来生成可用端口列表
     * @param startPort 起点端口
     * @return
     */
    public static List<Integer> getPortList(int startPort){
        List<String> deviceList = ServerCtrl.getUdidList();
        List<Integer> portList = new ArrayList<>();
        if(deviceList != null){
            portList =  createPortList(startPort,deviceList.size());
        }

        return portList;

    }

}
