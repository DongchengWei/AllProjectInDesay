package com.builder;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;


import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.graphics.Point;

public class SvMonkey {

	protected Shell shlSVMonkey;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text traceText;
	private Tree tree ;
	
	StringBuffer sBuffer = new StringBuffer("");
	private Text textThrottle;
	private Text txtLogLevel;
	private Text textTestTime;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SvMonkey window = new SvMonkey();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents(display);
		shlSVMonkey.open();
		shlSVMonkey.layout();
		while (!shlSVMonkey.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void ShowDirectoryDialog(Shell shell) {
		//选择目录
		DirectoryDialog dirctorydialog = new DirectoryDialog(shlSVMonkey);
		dirctorydialog.setText("目录选择对话框");
		dirctorydialog.setFilterPath("C:\\");
		dirctorydialog.setMessage("选择要使用的工作空间目录:");
		String dirctory = dirctorydialog.open();
		if (dirctory != null) {
			System.out.println(dirctory);
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {
		shlSVMonkey = new Shell();
		shlSVMonkey.setMinimumSize(new Point(140, 38));
		shlSVMonkey.setSize(882, 495);
		shlSVMonkey.setText("SvMonkey");
		shlSVMonkey.setLayout(new FormLayout());
		
		
		tree = new Tree(shlSVMonkey, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(0, 432);
		fd_tree.right = new FormAttachment(0, 262);
		fd_tree.top = new FormAttachment(0, 130);
		fd_tree.left = new FormAttachment(0, 10);
		tree.setLayoutData(fd_tree);
		
		traceText = formToolkit.createText(shlSVMonkey, "New Text", SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		FormData fd_traceText = new FormData();
		fd_traceText.top = new FormAttachment(0, 78);
		fd_traceText.right = new FormAttachment(0, 856);
		fd_traceText.bottom = new FormAttachment(0, 432);
		traceText.setLayoutData(fd_traceText);
		//TextAera
		traceText.setText("文本区域\r\n");
		
		//菜单栏
		Menu menuBar = new Menu(shlSVMonkey, SWT.BAR);
		shlSVMonkey.setMenuBar(menuBar);
		
		//工具菜单
		MenuItem toolsItem = new MenuItem(menuBar, SWT.CASCADE);
		toolsItem.setText("工具");
		
		//设置菜单
		MenuItem settingItem = new MenuItem(menuBar, SWT.CASCADE);
		settingItem.setText("设置");
		Menu settingMenu=new Menu(shlSVMonkey,SWT.DROP_DOWN); 
		settingItem.setMenu(settingMenu);
		//"Monkey参数设置"项
		MenuItem monkeySettingItem=new MenuItem(settingMenu,SWT.CASCADE);
		monkeySettingItem.setText("Monkey参数配置");
		//"新建"菜单,如果有这一项会在Monkey参数设置后面有展开箭头
//		Menu newFileMenu=new Menu(shlMonkeytest,SWT.DROP_DOWN);
//		newFileItem.setMenu(newFileMenu); 
		//"邮箱设置"项
		MenuItem emailSettingItem=new MenuItem(settingMenu,SWT.CASCADE);
		emailSettingItem.setText("邮箱配置");
		//"Log保存路径"项
		MenuItem logPathItem=new MenuItem(settingMenu,SWT.CASCADE);
		logPathItem.setText("Log保存路径");
		logPathItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				ShowDirectoryDialog(shlSVMonkey);
			}

		});
		
		
		//关于菜单
		MenuItem aboutItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutItem.setText("关于");
		
		//下拉栏
		Combo combo = new Combo(shlSVMonkey, SWT.NONE);
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(tree, 0, SWT.RIGHT);
		fd_combo.left = new FormAttachment(0, 106);
		combo.setLayoutData(fd_combo);
		
		
		//Button获取设备
		Button getDevicesBtn = new Button(shlSVMonkey, SWT.NONE);
		fd_combo.top = new FormAttachment(getDevicesBtn, 2, SWT.TOP);
		FormData fd_getDevicesBtn = new FormData();
		fd_getDevicesBtn.left = new FormAttachment(tree, 0, SWT.LEFT);
		fd_getDevicesBtn.right = new FormAttachment(0, 90);
		getDevicesBtn.setLayoutData(fd_getDevicesBtn);
		getDevicesBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("------execute command:  adb devices");
				try {
					Process p = Runtime.getRuntime().exec("adb devices");
					final BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			           
		           new Thread(new Runnable() {
		               String line;
		               List<String> devicesList = Collections.synchronizedList(new ArrayList<String>());
		                public void run() {
		                    try {
		                        while((line=inputStream.readLine()) != null) {
		                        	sBuffer.append(line + "\r\n");
		                        	if (line.equals("List of devices attached ")) {
	                        			while((line=inputStream.readLine()) != null) {
	                        				sBuffer.append(line + "\r\n");
	    		                        	display.asyncExec (new Runnable () {
	    		                        		public void run () {
	    	                        				traceText.append(sBuffer.toString());
	    	                        				sBuffer = new StringBuffer();
	    		                        		}
	    		                        	});
	                        				if (! line.equals("")) {
	                        					String[] tt=line.split("\\s+");//多个空格分隔
	                        					devicesList.add(tt[0]);
											}
	                        			}
									}
		                       }
		                        if ((line=inputStream.readLine()) == null) {
		                        	System.out.println("list size:" + devicesList.size());
		                        	display.asyncExec (new Runnable () {
		                        		public void run () {
		                        			combo.removeAll();//清空combo
		                        			for(int i=0; i<devicesList.size(); i++){
		                        				combo.add(devicesList.get(i));
		                        			}
		                        			combo.select(0);//设置默认选择第一项
		                        		}
		                        	});
								}
		                        inputStream.close();
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
		getDevicesBtn.setText("获取设备");
		
		//Button获取所有包
		Button getPackagesBtn = formToolkit.createButton(shlSVMonkey, "获取所有包", SWT.NONE);
		FormData fd_getPackagesBtn = new FormData();
		fd_getPackagesBtn.right = new FormAttachment(getDevicesBtn, 0, SWT.RIGHT);
		fd_getPackagesBtn.left = new FormAttachment(tree, 0, SWT.LEFT);
		getPackagesBtn.setLayoutData(fd_getPackagesBtn);
		getPackagesBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("------execute command:  adb -s devices shell pm list packages"); 
				String comboStr = combo.getText();
				System.out.println("devices:" + comboStr); 
				String getPackagesCmd = "adb -s " + comboStr + " shell pm list packages";
				try {
					Process processGetPackages = Runtime.getRuntime().exec(getPackagesCmd);
					final BufferedReader inputStreamPack = new BufferedReader(new InputStreamReader(processGetPackages.getInputStream()));
					new Thread(new Runnable() {
						String linePack;
						List<String> packagesList = Collections.synchronizedList(new ArrayList<String>());
						@Override
						public void run() {
							try {
								while((linePack=inputStreamPack.readLine()) != null) {
									sBuffer.append(linePack + "\r\n");
		                        	display.asyncExec (new Runnable () {
		                        		public void run () {
	                        				traceText.append(sBuffer.toString());
	                        				sBuffer = new StringBuffer();
		                        		}
		                        	});
									if (! linePack.equals("")) {
										String[] tt=linePack.split(":");
										packagesList.add(tt[1]);
									}
								}
								System.out.println("Package Count:" + packagesList.size());
								 if ((linePack=inputStreamPack.readLine()) == null) {
			                        	display.asyncExec (new Runnable () {
			                        		public void run () {
			                        			tree.removeAll();
			                        			for (int loopIndex0 = 0; loopIndex0 < 1; loopIndex0++) {
			                        		        TreeItem treeItemDevice = new TreeItem(tree, 0);
			                        		        treeItemDevice.setText(comboStr);
			                        		        for (int loopIndex1 = 0; loopIndex1 < packagesList.size(); loopIndex1++) {
			                        		          TreeItem treeItemPackage = new TreeItem(treeItemDevice, 0);
			                        		          treeItemPackage.setText(packagesList.get(loopIndex1));
			                        		        }
			                        		      }
			                        		}
			                        	});
									}
							} catch (IOException e2) {
								e2.printStackTrace(); 
							}
						}
					}).start();
					int ii;
					ii = processGetPackages.waitFor();
					System.out.println("ii=" + ii);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Button btnnativeCrash = new Button(shlSVMonkey, SWT.CHECK);
		fd_getDevicesBtn.top = new FormAttachment(btnnativeCrash, -5, SWT.TOP);
		fd_traceText.left = new FormAttachment(btnnativeCrash, 0, SWT.LEFT);
		btnnativeCrash.setSelection(true);
//		btnnativeCrash.getSelection();
		FormData fd_btnnativeCrash = new FormData();
		fd_btnnativeCrash.top = new FormAttachment(0, 10);
		fd_btnnativeCrash.left = new FormAttachment(0, 284);
		btnnativeCrash.setLayoutData(fd_btnnativeCrash);
		btnnativeCrash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		formToolkit.adapt(btnnativeCrash, true, true);
		btnnativeCrash.setText("出现error时停止测试");
		
		Button btnanr = new Button(shlSVMonkey, SWT.CHECK);
		btnanr.setSelection(true);
		FormData fd_btnanr = new FormData();
		fd_btnanr.top = new FormAttachment(0, 10);
		fd_btnanr.left = new FormAttachment(0, 425);
		btnanr.setLayoutData(fd_btnanr);
		formToolkit.adapt(btnanr, true, true);
		btnanr.setText("出现ANR时停止测试");
		
		Button btncrash = new Button(shlSVMonkey, SWT.CHECK);
		btncrash.setSelection(true);
		FormData fd_btncrash = new FormData();
		fd_btncrash.top = new FormAttachment(0, 10);
		fd_btncrash.left = new FormAttachment(0, 562);
		btncrash.setLayoutData(fd_btncrash);
		formToolkit.adapt(btncrash, true, true);
		btncrash.setText("出现CRASH时停止测试");
		
		Label lblThrolltl = new Label(shlSVMonkey, SWT.NONE);
		FormData fd_lblThrolltl = new FormData();
		fd_lblThrolltl.top = new FormAttachment(btnnativeCrash, 6);
		fd_lblThrolltl.left = new FormAttachment(0, 284);
		lblThrolltl.setLayoutData(fd_lblThrolltl);
		formToolkit.adapt(lblThrolltl, true, true);
		lblThrolltl.setText("Throttle Millisec");
		
		textThrottle = new Text(shlSVMonkey, SWT.BORDER);
		fd_lblThrolltl.right = new FormAttachment(textThrottle, -6);
		textThrottle.setText("300");
		FormData fd_textThrottle = new FormData();
		fd_textThrottle.left = new FormAttachment(0, 390);
		fd_textThrottle.bottom = new FormAttachment(btnnativeCrash, 23, SWT.BOTTOM);
		fd_textThrottle.top = new FormAttachment(btnnativeCrash, 6);
		textThrottle.setLayoutData(fd_textThrottle);
		formToolkit.adapt(textThrottle, true, true);
		
		Label lblLogLevel = new Label(shlSVMonkey, SWT.NONE);
		fd_textThrottle.right = new FormAttachment(lblLogLevel, -36);
		FormData fd_lblLogLevel = new FormData();
		fd_lblLogLevel.top = new FormAttachment(btnanr, 6);
		fd_lblLogLevel.left = new FormAttachment(0, 472);
		lblLogLevel.setLayoutData(fd_lblLogLevel);
		formToolkit.adapt(lblLogLevel, true, true);
		lblLogLevel.setText("Log Level");
		
		txtLogLevel = new Text(shlSVMonkey, SWT.BORDER);
		txtLogLevel.setText("-v -v -v");
		FormData fd_txtLogLevel = new FormData();
		fd_txtLogLevel.right = new FormAttachment(lblLogLevel, 64, SWT.RIGHT);
		fd_txtLogLevel.bottom = new FormAttachment(btnanr, 23, SWT.BOTTOM);
		fd_txtLogLevel.top = new FormAttachment(btnanr, 6);
		fd_txtLogLevel.left = new FormAttachment(lblLogLevel, 2);
		txtLogLevel.setLayoutData(fd_txtLogLevel);
		formToolkit.adapt(txtLogLevel, true, true);
		
		Label lblTestTimeh = new Label(shlSVMonkey, SWT.NONE);
		FormData fd_lblTestTimeh = new FormData();
		fd_lblTestTimeh.top = new FormAttachment(btncrash, 6);
		fd_lblTestTimeh.left = new FormAttachment(txtLogLevel, 40);
		lblTestTimeh.setLayoutData(fd_lblTestTimeh);
		formToolkit.adapt(lblTestTimeh, true, true);
		lblTestTimeh.setText("Test time(h)");
		
		textTestTime = new Text(shlSVMonkey, SWT.BORDER);
		textTestTime.setText("3");
		FormData fd_textTestTime = new FormData();
		fd_textTestTime.right = new FormAttachment(lblTestTimeh, 86, SWT.RIGHT);
		fd_textTestTime.bottom = new FormAttachment(btncrash, 23, SWT.BOTTOM);
		fd_textTestTime.top = new FormAttachment(btncrash, 6);
		fd_textTestTime.left = new FormAttachment(lblTestTimeh);
		textTestTime.setLayoutData(fd_textTestTime);
		formToolkit.adapt(textTestTime, true, true);
		
		Button btnStartMonkeyTest = new Button(shlSVMonkey, SWT.NONE);
		FormData fd_btnbug = new FormData();
		fd_btnbug.top = new FormAttachment(getPackagesBtn, 0, SWT.TOP);
		fd_btnbug.left = new FormAttachment(combo, 0, SWT.LEFT);
		btnStartMonkeyTest.setLayoutData(fd_btnbug);
		formToolkit.adapt(btnStartMonkeyTest, true, true);
		btnStartMonkeyTest.setText("开始测试");
		btnStartMonkeyTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comboStr = combo.getText();
				System.out.println("------execute command:  adb -s " + comboStr + " shell monkey --throttle 300 -v -v -v 200"); 
				String monkeyCmd = "adb -s " + comboStr + " shell monkey --throttle 300 -v -v -v 200";
				try {
					Process processMonkey = Runtime.getRuntime().exec(monkeyCmd);
					
					final BufferedReader inputStreamMonkey = new BufferedReader(new InputStreamReader(processMonkey.getInputStream()));
			        
					new Thread(new Runnable() {
						String linePack;
						@Override
						public void run() {
							try {
								while((linePack=inputStreamMonkey.readLine()) != null) {
									sBuffer.append(linePack + "\r\n");
									System.out.println(linePack);
		                        	display.asyncExec (new Runnable () {//asyncExec
		                        		public void run () {
	                        				traceText.append(sBuffer.toString());
	                        				sBuffer = new StringBuffer();
		                        		}
		                        	});
								}
							} catch (IOException e2) {
								e2.printStackTrace(); 
							}
						}
					}).start();
					int ii;
					ii = processMonkey.waitFor();
					System.out.println("ii=" + ii);
				} catch (Exception foundBug) {
					foundBug.printStackTrace();
				}
			}
		});
		
		Button btnFoundBug = new Button(shlSVMonkey, SWT.NONE);
		fd_getPackagesBtn.top = new FormAttachment(btnFoundBug, 6);
		FormData fd_btnFoundBug = new FormData();
		fd_btnFoundBug.left = new FormAttachment(tree, 0, SWT.LEFT);
		fd_btnFoundBug.top = new FormAttachment(getDevicesBtn, 6);
		fd_btnFoundBug.right = new FormAttachment(getDevicesBtn, -21, SWT.RIGHT);
		btnFoundBug.setLayoutData(fd_btnFoundBug);
		formToolkit.adapt(btnFoundBug, true, true);
		btnFoundBug.setText("发现bug");
		btnFoundBug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comboStr = combo.getText();
				System.out.println("------execute command:  adb -s " + comboStr + " shell su 0 svbugreport 1"); 
				String foundBugCmd = "adb -s " + comboStr + " shell su 0 svbugreport 1";
				try {
					Process processFoundBug = Runtime.getRuntime().exec(foundBugCmd);
					final BufferedReader inputStreamBug = new BufferedReader(new InputStreamReader(processFoundBug.getInputStream()));
					new Thread(new Runnable() {
						String linePack;
						@Override
						public void run() {
							try {
								while((linePack=inputStreamBug.readLine()) != null) {
									sBuffer.append(linePack + "\r\n");
		                        	display.asyncExec (new Runnable () {
		                        		public void run () {
	                        				traceText.append(sBuffer.toString());
	                        				sBuffer = new StringBuffer();
		                        		}
		                        	});
								}
							} catch (IOException e2) {
								e2.printStackTrace(); 
							}
						}
					}).start();
					int ii;
					ii = processFoundBug.waitFor();
					System.out.println("ii=" + ii);
				} catch (Exception foundBug) {
					foundBug.printStackTrace();
				}
			}
		});
		
		Button btnCopyLogs = new Button(shlSVMonkey, SWT.NONE);
		FormData fd_btnCopyLogs = new FormData();
		fd_btnCopyLogs.top = new FormAttachment(combo, 6);
		fd_btnCopyLogs.left = new FormAttachment(combo, 0, SWT.LEFT);
		btnCopyLogs.setLayoutData(fd_btnCopyLogs);
		formToolkit.adapt(btnCopyLogs, true, true);
		btnCopyLogs.setText("拷贝Logs");
		btnCopyLogs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comboStr = combo.getText();
				System.out.println("------execute command:  adb -s " + comboStr + " pull /data/logs/ d:\\z\\pull\\"); 
				String copyLogsCmd = "adb -s " + comboStr + " pull /data/logs/ d:/z/pull/";
				try {
					Process processCopyLogs = Runtime.getRuntime().exec(copyLogsCmd);
					final BufferedReader inputStreamLogs = new BufferedReader(new InputStreamReader(processCopyLogs.getInputStream()));
					new Thread(new Runnable() {
						String linePack;
						@Override
						public void run() {
							try {
								while((linePack=inputStreamLogs.readLine()) != null) {
									sBuffer.append(linePack + "\r\n");
		                        	display.asyncExec (new Runnable () {
		                        		public void run () {
	                        				traceText.append(sBuffer.toString());
	                        				sBuffer = new StringBuffer();
		                        		}
		                        	});
								}
							} catch (IOException e2) {
								e2.printStackTrace(); 
							}
						}
					}).start();
					int ii;
					ii = processCopyLogs.waitFor();
					System.out.println("ii=" + ii);
				} catch (Exception foundBug) {
					foundBug.printStackTrace();
				}
			}
		});
		
		
	    tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          if (event.detail == SWT.CHECK) {
	        	  traceText.setText(event.item + " was checked.");
	          } else {
	        	  traceText.setText(event.item + " was selected");
	          }
	        }
	      });
	}
}
