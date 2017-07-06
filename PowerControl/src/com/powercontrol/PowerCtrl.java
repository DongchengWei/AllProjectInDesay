/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-05-27
 */
package com.powercontrol;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.utils.DataEncode;
import com.utils.PropUtil;
import com.utils.SerialPortUtil;

import gnu.io.SerialPort;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年5月27日 下午2:08:30 
* 类说明 :
*/
/**
 * @author uidq0460
 *
 */
public class PowerCtrl {
	Display display = Display.getDefault();
	protected Shell shlPowercontrol = new Shell(SWT.MIN | SWT.CLOSE);
	Group groupCtrl = new Group(shlPowercontrol, SWT.NONE);
	Group groupOutput = new Group(shlPowercontrol, SWT.NONE);
	Button btnOutputOn = new Button(groupOutput, SWT.RADIO);
	Button btnOutputOff = new Button(groupOutput, SWT.RADIO);
	
	public boolean keepTest = true;
	public long testTimesCounter = 0;
	public String propFilePath = "";
	
	public String comSaveStr = "";
	public String stepVoltStr1 = "";
	public String stepVoltStr2 = "";
	public String stepTimeStr1 = "";
	public String stepTimeStr2 = "";
	public String repeatTimes = "";
	public String outputVoltageStr = "";
	public String outputCurrentStr = "";
	public String repeatMode = "";
	
	
	SerialPortUtil portUtil = new SerialPortUtil();
	Thread geThread;
	
	SerialPort serialPort;
	private Text textVol1;
	private Text textVol2;
	private Text textTime1;
	private Text textTime2;
	private Text textTimes;
	private Label lblCounter;
	private Text textOutputVolt;
	private Text textOutputCurr;
	private Button btnStep1;
	private Button btnStep2;
	private Button btnStart;
	private Text textNowVolt;
	private Text textNowCurr;
	private Table tableSteps;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PowerCtrl window = new PowerCtrl();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	@SuppressWarnings("deprecation")
	public void open() {
		createContents();
		shlPowercontrol.open();
		shlPowercontrol.layout();
		while (!shlPowercontrol.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		geThread.stop();
		It6831.ctrlMode(It6831.PANAL_MODE);
		portUtil.close();
	}

	//脉冲测试
	public void pulseTest() {
		try {
			keepTest = true;
			testTimesCounter = 0;
			while (keepTest) {
				testTimesCounter ++;
				
				display.asyncExec (new Runnable () {
            		public void run () {
            			lblCounter.setText("" + testTimesCounter);
            		}
            	});
				if (keepTest) {
					display.asyncExec (new Runnable () {
	            		public void run () {
	            			btnStep1.setVisible(true);
	            			btnStep1.setSelection(true);
	            			btnStep2.setVisible(false);
	    					btnStep2.setSelection(false);
	            		}
	            	});
					It6831.setOutputVoltage(stepVoltStr1);
					Thread.sleep(Long.parseLong(stepTimeStr1)*1000);
				}
				if (keepTest) {
					display.asyncExec (new Runnable () {
	            		public void run () {
	            			btnStep2.setVisible(true);
	            			btnStep2.setSelection(true);
	            			btnStep1.setSelection(false);
	            			btnStep1.setVisible(false);
	            		}
	            	});
					
					It6831.setOutputVoltage(stepVoltStr2);
					Thread.sleep(Long.parseLong(stepTimeStr2)*1000);
				}
				
				//根据循环模式决定结束测试
				if (!repeatMode.equals("still")) {
					if (repeatTimes.equals(testTimesCounter + "")) {
						keepTest = false;
					}
				}
			}
			display.asyncExec (new Runnable () {
        		public void run () {
        			btnStart.setEnabled(true);
        		}
        	});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取配置文件，通过配置文件保存的信息初始化界面
	 * @Date 2017-05-31
	 */
	public void initUi() {
		String rootDir = System.getProperty("user.dir");
		System.out.println(rootDir);//user.dir指定了当前的路径
		File propDir = new File(rootDir);// + "\\power.properties"
		if (propDir.exists()) {//目录存在
			File propFile = new File(rootDir + "\\power.properties");
			if (propFile.exists()) {//文件存在
				propFilePath = propFile.getAbsolutePath();
				System.out.println("propFile = exists");
			} else {
				System.out.println("propFile = not exists");
				try {
					if (propFile.createNewFile()) {
						propFilePath = propFile.getAbsolutePath();
						System.out.println("创建成功");
					} else {
						System.out.println("创建失败");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (propFilePath != "") {
			comSaveStr = PropUtil.getValueOfProp("comSaveStr", propFilePath);
			if (comSaveStr.equals("") || comSaveStr == null) {
				comSaveStr = "COM1";
			}
			stepVoltStr1 = PropUtil.getValueOfProp("stepVoltStr1", propFilePath);
			if (stepVoltStr1.equals("") || stepVoltStr1 == null) {
				stepVoltStr1 = "0";
			}
			stepVoltStr2 = PropUtil.getValueOfProp("stepVoltStr2", propFilePath);
			if (stepVoltStr2.equals("") || stepVoltStr2 == null) {
				stepVoltStr2 = "12.000";
			}
			stepTimeStr1 = PropUtil.getValueOfProp("stepTimeStr1", propFilePath);
			if (stepTimeStr1.equals("") || stepTimeStr1 == null) {
				stepTimeStr1 = "10";
			}
			stepTimeStr2 = PropUtil.getValueOfProp("stepTimeStr2", propFilePath);
			if (stepTimeStr2.equals("") || stepTimeStr2 == null) {
				stepTimeStr2 = "10";
			}
			repeatTimes = PropUtil.getValueOfProp("repeatTimes", propFilePath);
			if (repeatTimes.equals("") || repeatTimes == null) {
				repeatTimes = "1000";
			}
			outputVoltageStr = PropUtil.getValueOfProp("outputVoltageStr", propFilePath);
			if (outputVoltageStr.equals("") || outputVoltageStr == null) {
				outputVoltageStr = "12.000";
			}
			outputCurrentStr = PropUtil.getValueOfProp("outputCurrentStr", propFilePath);
			if (outputCurrentStr.equals("") || outputCurrentStr == null) {
				outputCurrentStr = "5.000";
			}
			repeatMode = PropUtil.getValueOfProp("repeatMode", propFilePath);
			if (repeatMode.equals("") || repeatMode == null) {
				repeatMode = "still";
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shlPowercontrol.setLocation(600, 200);
		shlPowercontrol.setSize(849, 593);
		shlPowercontrol.setText("电源控制");
		initUi();
		
		Combo comSelCombo = new Combo(shlPowercontrol, SWT.NONE);
		comSelCombo.setBounds(53, 7, 76, 25);
		comSelCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comNameStr = comSelCombo.getText();
				portUtil.close();
				SerialPortUtil.selectPort(comNameStr);
				PropUtil.setProperties(propFilePath, "comSaveStr", comNameStr, false);
				btnOutputOn.setEnabled(true);
				btnOutputOff.setEnabled(true);
			}
		});
		
		ArrayList<String> portNameList=new ArrayList<String>();
		portNameList = SerialPortUtil.listPort();
		comSelCombo.removeAll();
		int portCount = 0;
		for(int i=0; i<portNameList.size(); i++){
			String portStr = portNameList.get(i);
			comSelCombo.add(portStr);
			if (comSaveStr.equals(portStr)) {
				portCount = i;//记录下com端口
			}
		}
		comSelCombo.select(portCount);//设置默认选择端口
		SerialPortUtil.selectPort(comSaveStr);//选择端口
		portUtil.startRead(0);//监听端口
		It6831.ctrlMode(It6831.PC_MODE);//设置为PC端控制
		
		Menu menu = new Menu(shlPowercontrol, SWT.BAR);
		shlPowercontrol.setMenuBar(menu);
		
		MenuItem menuItemFile = new MenuItem(menu, SWT.NONE);
		menuItemFile.setText("文件");
		
		MenuItem menuItemSettings = new MenuItem(menu, SWT.NONE);
		menuItemSettings.setText("设置");
		
		MenuItem menuItemAbout = new MenuItem(menu, SWT.NONE);
		menuItemAbout.setText("关于");
		
		groupCtrl.setText("控制方式");
		groupCtrl.setBounds(13, 68, 92, 71);
		Button btnPc = new Button(groupCtrl, SWT.RADIO);
		btnPc.setBounds(10, 28, 97, 17);
		btnPc.setText("PC端控制");
		btnPc.setSelection(true);
		btnPc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnPc.getSelection()) {
					It6831.ctrlMode(It6831.PC_MODE);
					btnOutputOn.setEnabled(true);
					btnOutputOff.setEnabled(true);
				}
				
			}
		});
		
		Button btnPanal = new Button(groupCtrl, SWT.RADIO);
		btnPanal.setBounds(10, 51, 97, 17);
		btnPanal.setText("面板控制");
		btnPanal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnPanal.getSelection()) {
					It6831.ctrlMode(It6831.PANAL_MODE);
					btnOutputOn.setSelection(false);
					btnOutputOn.setEnabled(false);
					btnOutputOff.setSelection(false);
					btnOutputOff.setEnabled(false);
				}
			}
		});
		
		groupOutput.setText("输出控制");
		groupOutput.setBounds(176, 68, 92, 71);
		
		btnOutputOn.setBounds(10, 27, 97, 17);
		btnOutputOn.setText("输出打开");
		btnOutputOn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnOutputOn.getSelection()) {
					It6831.output(It6831.POWER_ON);
				}
			}
		});
		
		btnOutputOff.setBounds(10, 50, 97, 17);
		btnOutputOff.setText("输出关闭");
		btnOutputOff.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnOutputOff.getSelection()) {
					It6831.output(It6831.POWER_OFF);
				}
			}
		});
		
		
		Label lblpc = new Label(shlPowercontrol, SWT.NONE);
		lblpc.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblpc.setFont(SWTResourceManager.getFont("幼圆", 12, SWT.NORMAL));
		lblpc.setBounds(13, 45, 255, 17);
		lblpc.setText("所有控制必须在PC控制下进行！");
		
		Label lblComSel = new Label(shlPowercontrol, SWT.NONE);
		lblComSel.setBounds(13, 10, 36, 17);
		lblComSel.setText("端口：");
		
		Group groupStep = new Group(shlPowercontrol, SWT.NONE);
		groupStep.setText("脉冲程序");
		groupStep.setBounds(10, 156, 308, 192);
		
		btnStart = new Button(groupStep, SWT.NONE);
		btnStart.setBounds(40, 155, 80, 27);
		btnStart.setText("开始");
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stepVoltStr1 = textVol1.getText();
				stepVoltStr2 = textVol2.getText();
				stepTimeStr1 = textTime1.getText();
				stepTimeStr2 = textTime2.getText();
				repeatTimes = textTimes.getText();
				PropUtil.setProperties(propFilePath, "stepVoltStr1", stepVoltStr1, false);
				PropUtil.setProperties(propFilePath, "stepVoltStr2", stepVoltStr2, false);
				PropUtil.setProperties(propFilePath, "stepTimeStr1", stepTimeStr1, false);
				PropUtil.setProperties(propFilePath, "stepTimeStr2", stepTimeStr2, false);
				PropUtil.setProperties(propFilePath, "repeatTimes", repeatTimes, false);
				btnStart.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						pulseTest();
					}
				}).start();
			}
		});
		
		Label label = new Label(groupStep, SWT.NONE);
		label.setBounds(10, 21, 30, 17);
		label.setText("步骤");
		
		Label labelVol1 = new Label(groupStep, SWT.NONE);
		labelVol1.setBounds(42, 44, 39, 17);
		labelVol1.setText("1.电压");
		
		Label labelVol2 = new Label(groupStep, SWT.NONE);
		labelVol2.setBounds(42, 74, 39, 17);
		labelVol2.setText("2.电压");
		
		textVol1 = new Text(groupStep, SWT.BORDER);
		textVol1.setText(stepVoltStr1);
		textVol1.setBounds(87, 41, 61, 23);
		
		textVol2 = new Text(groupStep, SWT.BORDER);
		textVol2.setText(stepVoltStr2);
		textVol2.setBounds(87, 71, 61, 23);
		
		Button btnStop = new Button(groupStep, SWT.NONE);
		btnStop.setBounds(182, 155, 80, 27);
		btnStop.setText("停止");
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				keepTest = false;
				btnStart.setEnabled(true);
			}
		});
		
		Label lblSateTime1 = new Label(groupStep, SWT.NONE);
		lblSateTime1.setBounds(168, 44, 63, 17);
		lblSateTime1.setText("停留时间(S)");
		
		textTime1 = new Text(groupStep, SWT.BORDER);
		textTime1.setText(stepTimeStr1);
		textTime1.setBounds(237, 41, 61, 23);
		
		Label lblSateTime2 = new Label(groupStep, SWT.NONE);
		lblSateTime2.setText("停留时间(S)");
		lblSateTime2.setBounds(168, 74, 63, 17);
		
		textTime2 = new Text(groupStep, SWT.BORDER);
		textTime2.setText(stepTimeStr2);
		textTime2.setBounds(237, 71, 61, 23);
		
		Group groupRepeatMode = new Group(groupStep, SWT.NONE);
		groupRepeatMode.setText("循环方式");
		groupRepeatMode.setBounds(11, 97, 287, 51);
		
		Button btnStillRepeat = new Button(groupRepeatMode, SWT.RADIO);
		btnStillRepeat.setBounds(10, 24, 69, 17);
		btnStillRepeat.setText("一直重复");
		btnStillRepeat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnStillRepeat.getSelection()) {
					repeatMode = "still";
					PropUtil.setProperties(propFilePath, "repeatMode", repeatMode, false);
				}
				
			}
		});
		
		Button btnRepeatTimes = new Button(groupRepeatMode, SWT.RADIO);
		btnRepeatTimes.setBounds(88, 24, 69, 17);
		btnRepeatTimes.setText("重复次数");
		btnRepeatTimes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnRepeatTimes.getSelection()) {
					repeatMode = "counter";
					PropUtil.setProperties(propFilePath, "repeatMode", repeatMode, false);
				}
				
			}
		});
		if (repeatMode.equals("still")) {
			btnStillRepeat.setSelection(true);
		}else {
			btnRepeatTimes.setSelection(true);
		}
		
		textTimes = new Text(groupRepeatMode, SWT.BORDER);
		textTimes.setText(repeatTimes);
		textTimes.setBounds(156, 21, 69, 23);
		
		lblCounter = new Label(groupRepeatMode, SWT.NONE);
		lblCounter.setAlignment(SWT.RIGHT);
		lblCounter.setBounds(231, 24, 46, 17);
		lblCounter.setText("0");
		
		Label label_3 = new Label(groupStep, SWT.NONE);
		label_3.setText("V");
		label_3.setBounds(154, 44, 8, 17);
		
		Label label_4 = new Label(groupStep, SWT.NONE);
		label_4.setText("V");
		label_4.setBounds(154, 74, 8, 17);
		
		btnStep1 = new Button(groupStep, SWT.RADIO);
		btnStep1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		btnStep1.setBounds(20, 44, 13, 17);
		btnStep1.setVisible(false);
		
		btnStep2 = new Button(groupStep, SWT.RADIO);
		btnStep2.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_FOREGROUND));
		btnStep2.setBounds(20, 74, 13, 17);
		btnStep2.setVisible(false);
		
		Group group_2 = new Group(shlPowercontrol, SWT.NONE);
		group_2.setText("电压电流输出设置");
		group_2.setBounds(13, 448, 308, 94);
		
		Label label_1 = new Label(group_2, SWT.NONE);
		label_1.setBounds(10, 26, 24, 17);
		label_1.setText("电压");
		
		textOutputVolt = new Text(group_2, SWT.BORDER);
		textOutputVolt.setText(outputVoltageStr);
		textOutputVolt.setBounds(38, 26, 63, 17);
		
		Label lblV = new Label(group_2, SWT.NONE);
		lblV.setBounds(107, 26, 8, 17);
		lblV.setText("V");
		
		Label label_2 = new Label(group_2, SWT.NONE);
		label_2.setBounds(162, 26, 24, 17);
		label_2.setText("电流");
		
		textOutputCurr = new Text(group_2, SWT.BORDER);
		textOutputCurr.setText(outputCurrentStr);
		textOutputCurr.setBounds(192, 26, 63, 17);
		
		Label lblA = new Label(group_2, SWT.NONE);
		lblA.setText("A");
		lblA.setBounds(261, 26, 8, 17);
		
		Button btnConfirmOutput = new Button(group_2, SWT.NONE);
		btnConfirmOutput.setBounds(106, 57, 80, 27);
		btnConfirmOutput.setText("确定");
		
		Group groupNow = new Group(shlPowercontrol, SWT.NONE);
		groupNow.setText("当前电压和电流");
		groupNow.setBounds(13, 354, 305, 88);
		
		textNowVolt = new Text(groupNow, SWT.BORDER | SWT.CENTER);
		textNowVolt.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		textNowVolt.setFont(SWTResourceManager.getFont("微软雅黑", 18, SWT.NORMAL));
		textNowVolt.setText("12.000");
		textNowVolt.setBounds(10, 43, 120, 35);
		
		textNowCurr = new Text(groupNow, SWT.BORDER | SWT.CENTER);
		textNowCurr.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		textNowCurr.setFont(SWTResourceManager.getFont("微软雅黑", 18, SWT.NORMAL));
		textNowCurr.setText("5.000");
		textNowCurr.setBounds(164, 43, 110, 35);
		
		Label label_5 = new Label(groupNow, SWT.NONE);
		label_5.setBounds(56, 20, 24, 17);
		label_5.setText("电压");
		
		Label label_6 = new Label(groupNow, SWT.NONE);
		label_6.setText("电流");
		label_6.setBounds(206, 20, 24, 17);
		
		Label label_7 = new Label(groupNow, SWT.NONE);
		label_7.setText("V");
		label_7.setBounds(136, 61, 8, 17);
		
		Label label_8 = new Label(groupNow, SWT.NONE);
		label_8.setText("A");
		label_8.setBounds(280, 61, 8, 17);
		
		TabFolder tabFolder = new TabFolder(shlPowercontrol, SWT.NONE);
		tabFolder.setBounds(342, 7, 497, 528);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("快速设置");
		
		Composite cpsQuickSet = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(cpsQuickSet);
		
		TabItem tabItemProgram = new TabItem(tabFolder, SWT.NONE);
		tabItemProgram.setText("程序");
		
		Composite cpsProgram = new Composite(tabFolder, SWT.NONE);
		tabItemProgram.setControl(cpsProgram);
		
		Label lblNewLabel = new Label(cpsProgram, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
		lblNewLabel.setBounds(45, 10, 218, 21);
		lblNewLabel.setText("测试程序(最小时间是300毫秒)");
		
		tableSteps = new Table(cpsProgram, SWT.BORDER | SWT.FULL_SELECTION);
		tableSteps.setBounds(10, 37, 264, 313);
		tableSteps.setHeaderVisible(true);
		tableSteps.setLinesVisible(true);
		
		TableColumn tableColumnStep = new TableColumn(tableSteps, SWT.CENTER);
		tableColumnStep.setWidth(40);
		tableColumnStep.setText("步骤");
		
		TableColumn tableColumnVolt = new TableColumn(tableSteps, SWT.CENTER);
		tableColumnVolt.setWidth(75);
		tableColumnVolt.setText("电压(V)");
		
		TableColumn tableColumnCurr = new TableColumn(tableSteps, SWT.CENTER);
		tableColumnCurr.setWidth(78);
		tableColumnCurr.setText("电流(A)");
		
		TableColumn tableColumnTime = new TableColumn(tableSteps, SWT.CENTER);
		tableColumnTime.setWidth(65);
		tableColumnTime.setText("时间(S)");
		
		Group groupSetRun = new Group(cpsProgram, SWT.NONE);
		groupSetRun.setText("运行程序");
		groupSetRun.setBounds(10, 356, 303, 66);
		
		Button button = new Button(groupSetRun, SWT.RADIO);
		button.setBounds(10, 37, 45, 17);
		button.setText("单次");
		
		Button button_1 = new Button(groupSetRun, SWT.RADIO);
		button_1.setBounds(79, 37, 45, 17);
		button_1.setText("重复");
		
		Button button_2 = new Button(groupSetRun, SWT.RADIO);
		button_2.setBounds(148, 37, 41, 17);
		button_2.setText("定制");
		
		text = new Text(groupSetRun, SWT.BORDER);
		text.setBounds(195, 34, 63, 23);
		
		Group groupRun = new Group(cpsProgram, SWT.NONE);
		groupRun.setBounds(10, 428, 303, 60);
		
		Button btnRunProgram = new Button(groupRun, SWT.NONE);
		btnRunProgram.setBounds(20, 22, 80, 27);
		btnRunProgram.setText("运行");
		
		Button btnRunStep = new Button(groupRun, SWT.NONE);
		btnRunStep.setBounds(180, 22, 80, 27);
		btnRunStep.setText("单步");
		btnConfirmOutput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				outputVoltageStr = textOutputVolt.getText();
				outputCurrentStr = textOutputCurr.getText();
				PropUtil.setProperties(propFilePath, "outputVoltageStr", outputVoltageStr, false);
				PropUtil.setProperties(propFilePath, "outputCurrentStr", outputCurrentStr, false);
				It6831.setOutputVoltage(outputVoltageStr);
				It6831.setOutputCurrent(outputCurrentStr);
			}
		});
		
		//开一个线程读取并显示当前电压电流
		geThread = new Thread(new Runnable() {
			@Override
			public void run() {
				SerialPortUtil.haveReturn = false;
				while(true){
					if (SerialPortUtil.haveReturn == false) {
						It6831.sentGetPowerStatusCmd();
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (SerialPortUtil.haveReturn) {
						SerialPortUtil.haveReturn = false;
						
						String returnStr = SerialPortUtil.returnPackage;
						//读取电流
						String currCmd = returnStr.substring(6, 10);
						String realCurrStr = DataEncode.switchCode(currCmd);
						long currLong = Long.parseLong(realCurrStr, 16);
						double curr = ((double)currLong)/1000;
						String currStr = String.format("%.3f", curr).toString();
						//读取电压
						String voltCmd = returnStr.substring(10, 14);
						String realVoltStr = DataEncode.switchCode(voltCmd);
						long voltLong = Long.parseLong(realVoltStr, 16);
						double volt = ((double)voltLong)/1000;
						String voltStr = String.format("%.3f", volt).toString();
						
						if (voltStr != null && !voltStr.equals("")) {
							display.asyncExec (new Runnable () {
			            		public void run () {
			            			textNowVolt.setText(voltStr);
			            		}
			            	});
						}
						if (currStr != null && !currStr.equals("")) {
							display.asyncExec (new Runnable () {
			            		public void run () {
			            			textNowCurr.setText(currStr);
			            		}
			            	});
						}
					}
				}
			}
		});
		geThread.start();
	}
}
