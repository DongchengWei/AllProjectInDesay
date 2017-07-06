package com.monkey;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MonkeyTest {
	private static org.eclipse.swt.widgets.Label label;
	private static org.eclipse.swt.widgets.Text tGetInfo;
	private static org.eclipse.swt.widgets.Combo combo;
	private static org.eclipse.swt.widgets.Button btnGetDevice;
	
	public static void main(String[] args){
		
		StringBuffer sBuffer = new StringBuffer("");
		Display display = new Display();		//创建一个display对象
		Shell shell = new Shell(display);		//shell是程序的主窗体
		
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginLeft = 5;
		layout.marginBottom = 10;
		shell.setLayout(new FormLayout());		//设置shell的布局方式
		
		
		shell.setText("MonkeyTest");			//设置窗体标题
		
		//控件定义
		combo = new Combo(shell, SWT.NONE);
		label = new org.eclipse.swt.widgets.Label(shell, SWT.WRAP);
		tGetInfo = new org.eclipse.swt.widgets.Text(shell, SWT.BORDER | SWT.MULTI);
		btnGetDevice = new org.eclipse.swt.widgets.Button(shell, SWT.NULL);
		
		//下拉框
//		combo.setBounds(410, 10, 200, 25);
		FormData dataCombo = new FormData();
//		dataCombo.height = 50;
		dataCombo.right = new FormAttachment(100, -50);
		dataCombo.left = new FormAttachment(0, 10);
		dataCombo.top = new FormAttachment(1, 4, 0);
		combo.setLayoutData(dataCombo);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				label.setText("当前选择：" + combo.getText());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				label.setText("当前选择：" + combo.getText());
			}
		});
		
		//文字标签
//		label.setBounds(410, 40, 200, 25);
		label.setText("已选择设备：");
		FormData dataLabel = new FormData();
//		dataLabel.bottom = new FormAttachment(100, 5);//底部
		dataLabel.right = new FormAttachment(combo, 100, SWT.RIGHT);
		dataLabel.left = new FormAttachment(combo, 0, SWT.LEFT);//最左边与控件最左边的距离
		dataLabel.top = new FormAttachment(combo, 5);//顶端与combo控件距离
		label.setLayoutData(dataLabel);
		
		//文本框SWT.NONE/SWT.BORDER | SWT.MULTI
		tGetInfo.setText("Text test\n");
//		tGetInfo.setBounds(100, 10, 300, 400);
		FormData data = new FormData();
//		data.bottom = new FormAttachment(100, 5);
		data.right = new FormAttachment(label, 100, SWT.RIGHT);
		data.left = new FormAttachment(label, 0, SWT.LEFT);
		data.top = new FormAttachment(label, 5);
		tGetInfo.setLayoutData(data);
		
	
		btnGetDevice.setText("Get devices");
//		btMkdir.setBounds(10, 10, 75, 30);
		FormData dataBtnGetDev = new FormData();
		dataBtnGetDev.top = new FormAttachment(0, 5);
		dataBtnGetDev.left = new FormAttachment(0, 5);
		dataBtnGetDev.right = new FormAttachment(50, -5);
		dataBtnGetDev.bottom = new FormAttachment(50, -5);
		tGetInfo.setLayoutData(dataBtnGetDev);
		btnGetDevice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//execCmd("adb shell",tGetInfo);
				System.out.println("------execute command:  adb devices");
				try {
					Process p = Runtime.getRuntime().exec("adb devices");
					final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
					final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			           
		           new Thread(new Runnable() {
		               String line;
		               int counter = 0;
		               String device1,device2;
		                public void run() {
		                   System.out.println("listener started");
		                    try {
		                        while((line=inputStream.readLine()) != null) {
		                        	System.out.println(line);
		                        	if (line.equals("List of devices attached ")) {
	                        			System.out.println("test devices:");
	                        			
	                        			while((line=inputStream.readLine()) != null) {
	                        				counter ++;
	                        				if (! line.equals("")) {
//	                        					System.out.println("devices" + counter + ":" + line + "\r\n");
	                        					String[] tt=line.split("\\s+");
//	                        			        for(String s:tt)
//	                        			        {
//	                        			            System.out.println(s);
//	                        			        }
	                        					if (counter == 1) {
	                        						device1 = tt[0];
	                        						System.out.println(device1);
												}else {
													device2 = tt[0];
													System.out.println(device2);
												}
											}
	                        			}
									}
//		                        	sBuffer.append(line+"\r\n");
		                       }
		                        if ((line=inputStream.readLine()) == null) {
		                        	display.asyncExec (new Runnable () {
		                        		public void run () {
		                        			combo.removeAll();//情况combo
		                        			for(int i=0; i<counter; i++){
		                        				if (i == 1) {
													combo.add(device1);
												}else if (i == 2) {
													combo.add(device2);
												}
		                        				combo.select(0);//设置默认选择第一项
		                        				//选择后文本label显示
		                        				label.setText("当前选择：" + combo.getText());
		                        			}
		                        			tGetInfo.append(sBuffer.toString());
		                        		}
		                        	});
								}
		                    } catch (IOException e) {
		                       e.printStackTrace();  
		                    }
		                }
		           }).start();
					int i;
					i = p.waitFor();
					System.out.println("i=" + i);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});


		shell.pack();							//自动调整文本框大小
		shell.open();							//打开主窗体
		while (!shell.isDisposed()) {			//如果主窗体没有关闭则一直循环
			if (!display.readAndDispatch()) {	//如果display不忙
				display.sleep();
			}
		}
		display.dispose();						//销毁display
	}
	
	/**
	 * 需求：执行cmd命令，且输出信息到控制台
	 * @param cmd
	 */
	public static void execCmd(String cmd,Text tt) {
		System.out.println("------execute command:  " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
//			InputStream input = p.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					input));
//			int code = 0;
//			while ((code = reader.read()) != -1) {
//				System.out.print((char) code);
//				//tt.append((char) code+"");
//			}
			   final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
	           final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
	           
	           new Thread(new Runnable() {
	               String line;
	                public void run() {
	                   System.out.println("listener started");
	                    try {
	                        while((line=inputStream.readLine()) != null) {
	                        System.out.println(line);
	                       }
	                    } catch (IOException e) {
	                       //e.printStackTrace();  
	                    }
	                }
	           }).start();
	           new Thread(new Runnable() {
	                final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	                public void run() {
	                   System.out.println("writer started");
	                    String line;
	                    try {
	                        while ((line =br.readLine()) != null) {
	                           outputStream.write(line + "\r\n");
	                           outputStream.flush();
	                        }
	                    } catch (IOException e) {
	                       e.printStackTrace();  
	                    }
	                }
	           }).start();
			try {
				int i;
				i = p.waitFor();
				System.out.println("i=" + i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
