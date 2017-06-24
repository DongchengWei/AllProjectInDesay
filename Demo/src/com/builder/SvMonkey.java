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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SvMonkey {

	protected Shell shlMonkeytest;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text traceText;
	private Tree tree ;
	
	StringBuffer sBuffer = new StringBuffer("");
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
		shlMonkeytest.open();
		shlMonkeytest.layout();
		while (!shlMonkeytest.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {
		shlMonkeytest = new Shell();
		shlMonkeytest.setSize(882, 492);
		shlMonkeytest.setText("MonkeyTest");
		
		
		tree = new Tree(shlMonkeytest, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
		
		traceText = formToolkit.createText(shlMonkeytest, "New Text", SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		//TextAera
		traceText.setText("文本区域\r\n");
		traceText.setBounds(284, 78, 572, 354);
		
		
		Menu menuBar = new Menu(shlMonkeytest, SWT.BAR);
		shlMonkeytest.setMenuBar(menuBar);
		
		MenuItem toolsItem = new MenuItem(menuBar, SWT.CASCADE);
		toolsItem.setText("工具");
		
		MenuItem settingItem = new MenuItem(menuBar, SWT.CASCADE);
		settingItem.setText("设置");
		
		Menu settingMenu=new Menu(shlMonkeytest,SWT.DROP_DOWN); 
		settingItem.setMenu(settingMenu);
		//"Monkey参数设置"项
		MenuItem monkeySettingItem=new MenuItem(settingMenu,SWT.CASCADE);
		monkeySettingItem.setText("Monkey参数设置");
		//"新建"菜单,如果有这一项会在Monkey参数设置后面有展开箭头
//		Menu newFileMenu=new Menu(shlMonkeytest,SWT.DROP_DOWN);
//		newFileItem.setMenu(newFileMenu); 
		//"邮箱设置"项
		MenuItem emailSettingItem=new MenuItem(settingMenu,SWT.CASCADE);
		emailSettingItem.setText("邮箱设置");
		
		
		MenuItem aboutItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutItem.setText("关于");
		
		Combo combo = new Combo(shlMonkeytest, SWT.NONE);
		combo.setBounds(106, 10, 156, 25);
		Button getDevicesBtn = new Button(shlMonkeytest, SWT.NONE);
		
		
		//Button
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
	                        					String[] tt=line.split("\\s+");
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
		getDevicesBtn.setBounds(10, 8, 80, 27);
		getDevicesBtn.setText("获取设备");
		
		
		Button getPackagesBtn = formToolkit.createButton(shlMonkeytest, "获取所有包", SWT.NONE);
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
		getPackagesBtn.setBounds(10, 41, 80, 27);
		
		
		//Tree，如果内容多，可以左右滚动，可以多选
//		Tree tree = new Tree(shlMonkeytest, SWT.BORDER | SWT.MULTI | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.setBounds(10, 78, 252, 354);
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
