package com.myswt;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HelloSWT {
	
	public static void main(String[] args){
		StringBuffer sBuffer = new StringBuffer("Test");
		Display display = new Display();		//创建一个display对象
		Shell shell = new Shell(display);		//shell是程序的主窗体
		//shell.setLayout(null);				//设置shell的布局方式
		shell.setText("Android自动化测试");		//设置窗体标题
		//FormLayout fromlayout = new FormLayout();//表格布局对象
		//shell.setLayout(fromlayout);
		org.eclipse.swt.widgets.Text tGetInfo = new org.eclipse.swt.widgets.Text(shell, SWT.BORDER | SWT.MULTI);
		tGetInfo.setText("Text test\n");
		tGetInfo.setBounds(100, 10, 300, 400);
		
		org.eclipse.swt.widgets.Button btMkdir = new org.eclipse.swt.widgets.Button(shell, SWT.NULL);
		btMkdir.setText("MakeDir");
		btMkdir.setBounds(10, 10, 75, 30);
		btMkdir.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//execCmd("adb shell",tGetInfo);
				System.out.println("------execute command:  adb -s 2e6408f0344c0122 shell");
				try {
						Process p = Runtime.getRuntime().exec("adb -s 2e6408f0344c0122 shell");
					   final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			           final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			           
			           new Thread(new Runnable() {
			               String line;
			                public void run() {
			                   System.out.println("listener started");
			                    try {
			                        while((line=inputStream.readLine()) != null) {
			                        	System.out.println(line);
			                        	sBuffer.append(line+"\r\n");
			                       }
			                    } catch (IOException e) {
			                       e.printStackTrace();  
			                    }
			                }
			           }).start();
			           new Thread(new Runnable() {
			                //final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			                public void run() {
			                   System.out.println("writer started");
			                    String line;
			                    try {
			                        //while ((line =br.readLine()) != null) {
			                           outputStream.write("su\r\n");
			                           outputStream.flush();
			                           outputStream.write("mkdir /data/logs\r\n");
			                           outputStream.flush();
			                           outputStream.write("exit\r\n");
			                           outputStream.flush();
			                           outputStream.write("exit\r\n");
			                           outputStream.flush();
			                        //}
			                    } catch (IOException e) {
			                       //e.printStackTrace();  
			                    }
			                }
			           }).start();
						int i;
						i = p.waitFor();
						System.out.println("i=" + i);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		org.eclipse.swt.widgets.Button btCopyToSD = new org.eclipse.swt.widgets.Button(shell, SWT.NULL);
		btCopyToSD.setText("CopyToSD");
		btCopyToSD.setBounds(10, 50, 75, 30);
		btCopyToSD.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("------execute command:  adb -s 2e6408f0344c0122 shell");
						try {
								Process p = Runtime.getRuntime().exec("adb -s 2e6408f0344c0122 shell");
							   final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
					           final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
					           new Thread(new Runnable() {
					               String line;
					                public void run() {
					                   System.out.println("listener started");
					                    try {
					                        while((line=inputStream.readLine()) != null) {
					                        	System.out.println(line);
					                        	sBuffer.append(line+"\r\n");
					                       }
					                    } catch (IOException e) {
					                       e.printStackTrace();  
					                    }
					                }
					           }).start();
					           new Thread(new Runnable() {
					                //final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					                public void run() {
					                   System.out.println("writer started");
					                    String line;
					                    try {
					                        //while ((line =br.readLine()) != null) {
					                           outputStream.write("su\r\n");
					                           outputStream.flush();
					                           outputStream.write("cp -rf /data/logs/ /storage/sdcard2/\r\n");
					                           outputStream.flush();
					                           outputStream.write("exit\r\n");
					                           outputStream.flush();
					                           outputStream.write("exit\r\n");
					                           outputStream.flush();
					                        //}
					                    } catch (IOException e) {
					                       //e.printStackTrace();  
					                    }
					                }
					           }).start();
								int i;
								i = p.waitFor();
								System.out.println("i=" + i);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
		
		
		org.eclipse.swt.widgets.Button btPullPC = new org.eclipse.swt.widgets.Button(shell, SWT.NULL);
		btPullPC.setText("btPullPC");
		btPullPC.setBounds(10, 90, 75, 30);
		btPullPC.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime.getRuntime().exec("adb -s 2e6408f0344c0122 pull /storage/sdcard2/logs/ d:\\z\\ \r\n");
					Process pp = Runtime.getRuntime().exec("adb -s 2e6408f0344c0122 shell \r\n");
				   final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(pp.getOutputStream()));
		           final BufferedReader inputStream = new BufferedReader(new InputStreamReader(pp.getInputStream()));
		           new Thread(new Runnable() {
		               String line;
		                public void run() {
		                   System.out.println("listener started");
		                    try {
		                        while((line=inputStream.readLine()) != null) {
		                        	System.out.println(line);
		                        	sBuffer.append(line+"\r\n");
		                       }
		                    } catch (IOException e) {
		                       e.printStackTrace();  
		                    }
		                }
		           }).start();
		           new Thread(new Runnable() {
		                //final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		                public void run() {
		                   System.out.println("writer started");
		                    String line;
		                    try {
		                        //while ((line =br.readLine()) != null) {
		                           outputStream.write("su\r\n");
		                           outputStream.flush();
		                           outputStream.write("rm -rf /data/logs/\r\n");
		                           outputStream.flush();
		                           outputStream.write("rm -rf /storage/sdcard2/logs\r\n");
		                           outputStream.flush();
		                           outputStream.write("exit\r\n");
		                           outputStream.flush();
		                           outputStream.write("exit\r\n");
		                           outputStream.flush();
		                        //}
		                    } catch (IOException e) {
		                       //e.printStackTrace();  
		                    }
		                }
		           }).start();
					int i;
					i = pp.waitFor();
					System.out.println("i=" + i);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		org.eclipse.swt.widgets.Button btGetBuf = new org.eclipse.swt.widgets.Button(shell, SWT.NULL);
		btGetBuf.setText("GetBufer");
		btGetBuf.setBounds(10, 130, 75, 30);
		btGetBuf.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				tGetInfo.append(sBuffer + "");
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
	                       //e.printStackTrace();  
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
