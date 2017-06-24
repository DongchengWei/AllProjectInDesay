package com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerCtrl {

	   //设备udid list
    public static List<String> udidList;

    /**
     * 获取当前链接的手机的udid列表
     *
     * @return
     */
    public static List<String> getUdidList() {

        if (udidList == null || udidList.isEmpty()) {
            udidList = new ArrayList<>();
            List<String> list = CmdCtrl.getInstance().execCmd("adb devices");
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {

                    if (i != 0) {

                        String[] devicesInfo = list.get(i).split("\t");
                        //状态为device才是正确链接了手机，如果是offline、组织
                        try {
                            if (devicesInfo[1].equals("device")) {
                                System.out.println("成功获取设备:" + devicesInfo[0].trim());
                                udidList.add(devicesInfo[0].trim());
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //跳过两行
                            // * daemon not running. starting it now on port 5037 *
                            // * daemon started successfully *
                            i = i + 2;
                        }

                    }
                }
            } else {
                System.out.println("当前没有手机链接...");
                return null;
            }

            if (udidList.isEmpty()) {
                System.out.println("有" + list.size() + "台手机链接,但是手机没有正确链接,请尝试重新链接手机");
            }
        }


        return udidList;
    }


    /**
     * 创建 启动服务的命令
     *
     * @return
     */
    public static List<String> createServerCommand() throws Exception {

        //appium服务的端口号
        List<Integer> appiumPortList = PortCtrl.getPortList(6666);

        //bootstrap的端口号
        List<Integer> bsPortList = PortCtrl.getPortList(9999);

        //获取手机的udid列表
        List<String> devicesList = getUdidList();

        List<String> commandList = new ArrayList<>();

        //对应log的名字，保存起来可以提供删除
        List<String> logNameList = new ArrayList<>();

        //生成开启服务的命令,把对应的日志保存到D盘的AppiumLogs目录下

        for (int i = 0; i < devicesList.size(); i++) {

            String logName =  devicesList.get(i) + "_" + XmlUtils.getCurrentTime() + ".log";

            String command = "appium.cmd --address 127.0.0.1 -p " + appiumPortList.get(i) + " -bp " + bsPortList.get(i) +
                    " --session-override -U " + devicesList.get(i) + ">" + FileCtrl.getLogsPath()+logName;

            commandList.add(command);

            logNameList.add(logName);
        }

        //把设备信息保存起来，启动服务之后可以自动生成testng
        XmlUtils.createDeviceXml(devicesList, appiumPortList,logNameList);

        return commandList;


    }


    /**
     * 根据进程pid杀死进程，用在结束测试之后，杀死那些端口
     *
     * @param pid 要杀死的pid进程
     * @return
     */
    public static Boolean killServerByPid(String pid) {
        if (CmdCtrl.getInstance().execCmdTrue("taskkill -f -pid " + pid)) {
            System.out.println("根据pid:" + pid + "杀死进程成功");
            return true;
        } else {
            System.out.println("根据pid:" + pid + "杀死进程失败");
            return false;
        }
    }


    /**
     * 获取上一次开启服务端口
     *
     * @return
     */
    public static List<String> getStartPortList() throws Exception {
        List<Map<String, String>> mapList = new ArrayList<>();

        mapList = XmlUtils.readDevicesXml(FileCtrl.getModulePath() + "devicesInfo.xml");

        List<String> portList = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            String port = map.get(XmlUtils.APPIUMPORT);
            portList.add(port);
        }

        return portList;

    }


    /**
     * 占用服务的程序的pid
     *
     * @return
     */
    public static List<String> getStartPidList(List<String> portList) throws Exception {

        List<String> pidList = new ArrayList<>();

        if (!portList.isEmpty()) {

            for (String port : portList) {
                //根据端口查询对应占用程序的pid
                List<String> resultList = CmdCtrl.getInstance().execCmd("netstat -aon | findstr " + port);
                if (!resultList.isEmpty()) {

                    for (String line : resultList) {
                        //利用正则表达式来获取pid
                        Pattern p = Pattern.compile(" (\\d{2,5})$");
                        Matcher m = p.matcher(line);
                        if (m.find()) {
                            String pid = m.group(m.groupCount());
                            //不存在就add进pid列表
                            if (!pidList.contains(pid)) {
                                pidList.add(pid);
                            }
                        }

                    }

                }

            }

        }

        return pidList;

    }
}
