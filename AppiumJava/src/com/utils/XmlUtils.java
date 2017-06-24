package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtils {

	
    public final static String DEVICE = "device";
    public final static String DEVICEID = "deviceId";
    public final static String DEVICENAME = "deviceName";
    public final static String APPIUMPORT = "appiumPort";
    public final static String LOGNAME = "logName";


    /**
     * 创建设备和对应服务的xml信息，
     *
     * @param devicesList    手机列表
     * @param appiumPortList 端口列表
     * @param logNameList    保存的log的名字
     */
    public static void createDeviceXml(List<String> devicesList, List<Integer> appiumPortList, List<String> logNameList) throws Exception {
        Document document = DocumentHelper.createDocument();

        //创建根元素:<Device></Device>
        Element root = DocumentHelper.createElement(DEVICE);
        document.setRootElement(root);

        //根元素Device添加一个属性appiumStartList:<Device appiumStartList=""></Device>
        root.addAttribute("name", "devicesList");
        if (!devicesList.isEmpty()) {

            for (int i = 0; i < devicesList.size(); i++) {
                //在根元素下创建对应元素deviceId:
                Element deviceId = root.addElement(DEVICEID);
                //为
                deviceId.addAttribute("id", i + "");

                //在deviceId元F素下创建对应元素deviceName:
                Element deviceName = deviceId.addElement(DEVICENAME);
                //在deviceId元素下创建对应元素appiumPort:
                Element appiumPort = deviceId.addElement(APPIUMPORT);
                //在deviceId元素下创建对应元素appiumPort:
                Element logName = deviceId.addElement(LOGNAME);

                //设置deviceName的文本 <deviceName>要设置的文本</deviceName>
                deviceName.setText(devicesList.get(i));
                //设置appiumPort的文本
                appiumPort.setText(appiumPortList.get(i) + "");
                //设置logName的文本
                logName.setText(logNameList.get(i));
            }


            //生成testng.xml
            OutputFormat format = new OutputFormat("    ", true);
            XMLWriter xmlWriter = null;
            try {
                xmlWriter = new XMLWriter(new FileOutputStream(FileCtrl.getModulePath() + "devicesInfo.xml"), format);
                xmlWriter.write(document);
                System.out.println("生成设备信息文件");
            } catch (Exception e) {
                System.out.println("生成设备信息文件失败");
            }

        }


    }


    /**
     * 创建Testng xml文件 到module根目录
     *
     * @param threadCount 线程数，0 是根据手机数量来生成
     * @param className   测试类的类名
     */
    public static void createTestNgXml(int threadCount, List<String> className) throws Exception {


        Document document = DocumentHelper.createDocument();

        Element root = DocumentHelper.createElement("suite");
        document.setRootElement(root);
        root.addAttribute("name", "Suite");

        //设备信息的list
        List<Map<String, String>> devicesInfo = readDevicesXml(FileCtrl.getModulePath() + "devicesInfo.xml");

        //线程数为0 或者线程数大于设备数就添加全部手机
        if (threadCount == 0 || threadCount > devicesInfo.size()) {
            root.addAttribute("parallel", "tests");
            root.addAttribute("thread-count", devicesInfo.size() + "");
        } else {
            root.addAttribute("thread-count", "1");
        }

        //创建listeners 监听器元素
        Element listeners = root.addElement("listeners");
        //创建listenerHtml元素
        Element listenerHtml = listeners.addElement("listener");
        Element listenerXML = listeners.addElement("listener");
        //添加报告监听器
        listenerHtml.addAttribute("class-name", "org.uncommons.reportng.HTMLReporter");
        listenerXML.addAttribute("class-name", "org.uncommons.reportng.JUnitXMLReporter");

        //循环创建对应的test
        for (int i = 0; i < ((threadCount == 0 || threadCount > devicesInfo.size()) ? devicesInfo.size() : threadCount); i++) {

            //创建test元素
            Element test = root.addElement("test");

            //每个test的名字要不一样，这里以设备udid_类名进行区分
            test.addAttribute("name",
                    devicesInfo.get(i).get(DEVICENAME) + "_" + className.get(0).substring(className.get(0).lastIndexOf(".") + 1));

            //在test下创建port端口parameter元素
            Element port = test.addElement("parameter");
            port.addAttribute("name", "port");
            port.addAttribute("value", devicesInfo.get(i).get(APPIUMPORT));

            //在test下创建udid端口parameter元素
            Element udid = test.addElement("parameter");
            udid.addAttribute("name", "udid");
            udid.addAttribute("value", devicesInfo.get(i).get(DEVICENAME));

            //创建classes 执行用例元素
            Element classes = test.addElement("classes");

            //添加要执行的用例
            for(String cls:className){
                //创建class元素
                Element classElement = classes.addElement("class");
                classElement.addAttribute("name", cls);
            }



        }

        //生成testng.xml
        OutputFormat format = new OutputFormat("    ", true);
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(new FileOutputStream(FileCtrl.getModulePath() + "testng_" + getCurrentTime() + ".xml"), format);
            xmlWriter.write(document);
            System.out.println("生成testng文件");
        } catch (Exception e) {
            System.out.println("生成testng文件失败");
        }

    }

    /**
     * 获取当前的时间 年月日时分秒
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date now = new Date();
        return dateFormat.format(now);
    }


    public static Document getDevicesDocument(String fileName) throws DocumentException {
        //将src下面的xml转换为输入流
        //InputStream inputStream = new FileInputStream(new File(fileName));
        //也可以根据类的编译文件相对路径去找xml
        //InputStream inputStream = this.getClass().getResourceAsStream("/module01.xml");

        //创建SAXReader读取器，专门用于读取xml
        SAXReader saxReader = new SAXReader();
        //根据saxReader的read重写方法可知，既可以通过inputStream输入流来读取，也可以通过file对象来读取
        //Document document = saxReader.read(inputStream);

        //fileName必须指定文件的绝对路径
        return saxReader.read(new File(fileName));
    }


    /**
     * 解析devicesInfo.xml 为
     *
     * @param fileName 设备信息xml路径，绝对路径
     * @return
     */
    public static List<Map<String, String>> readDevicesXml(String fileName) throws Exception {

        Document document = getDevicesDocument(fileName);

        //根节点
        Element element = document.getRootElement();

        //每个设备的list
        List<Element> deviceIDList = element.elements(DEVICEID);

        List<Map<String, String>> devices = new ArrayList<>();

        if (deviceIDList != null && !deviceIDList.isEmpty()) {


            //每个设备的信息
            for (Element deviceID : deviceIDList) {

                Map<String, String> map = new HashMap<>();
                map.put(DEVICENAME, deviceID.element(DEVICENAME).getText());
                map.put(APPIUMPORT, deviceID.element(APPIUMPORT).getText());
                devices.add(map);

            }


        }

        return devices;

    }

}
