/**
 * 分割文件
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-04-28
 */
package com.demo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年4月28日 上午11:11:20 
* 类说明 :分割txt文本的工具
*/
/**
 * @author uidq0460
 *
 */
public class TxtSplitTool {

	protected Shell shlTxtfilesplit;
	private Text textFilePath;
	private Text textSavePath;
	private String filePathStartStr = "";
	private String saveDirectoryStartStr = "d:\\";
	private Text textFileSize;
	private Text textFileCounter;
	int fileCounter;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
			System.out.println("Date: " + nowTimeStr);
			TxtSplitTool window = new TxtSplitTool();
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
		shlTxtfilesplit.open();
		shlTxtfilesplit.layout();
		while (!shlTxtfilesplit.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {
		shlTxtfilesplit = new Shell();
		
		//让shell窗口居中
		Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
		Rectangle rect = shlTxtfilesplit.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shlTxtfilesplit.setLocation(x, y);
		
		shlTxtfilesplit.setSize(543, 223);
		shlTxtfilesplit.setText("TxtFileSplit");
		shlTxtfilesplit.setLayout(new FormLayout());
		Button btnFileSize = new Button(shlTxtfilesplit, SWT.RADIO);
		Label lblFileSize = new Label(shlTxtfilesplit, SWT.NONE);
		
		
		Button buttonFilePath = new Button(shlTxtfilesplit, SWT.NONE);
		FormData fd_buttonFilePath = new FormData();
		fd_buttonFilePath.right = new FormAttachment(0, 109);
		fd_buttonFilePath.top = new FormAttachment(0, 10);
		fd_buttonFilePath.left = new FormAttachment(0, 10);
		buttonFilePath.setLayoutData(fd_buttonFilePath);
		buttonFilePath.setText("选择分割文件");
		buttonFilePath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				String pathStr = fileSeleteDialog(shlTxtfilesplit);
				textFilePath.setText(pathStr);
				long fileSizeByteSeleteL = getFileSize(pathStr);		//获取文件大小(单位byte)
				String sizeStr = getFileSizeFormate(fileSizeByteSeleteL);
				System.out.println("sizeStr=" + sizeStr);
				lblFileSize.setText(sizeStr);
				textSavePath.setText(getAbsolutePath(pathStr));
			}
		});
		
		Button buttonSavePath = new Button(shlTxtfilesplit, SWT.NONE);
		FormData fd_buttonSavePath = new FormData();
		fd_buttonSavePath.right = new FormAttachment(buttonFilePath, 0, SWT.RIGHT);
		fd_buttonSavePath.top = new FormAttachment(buttonFilePath, 6);
		fd_buttonSavePath.left = new FormAttachment(0, 10);
		buttonSavePath.setLayoutData(fd_buttonSavePath);
		buttonSavePath.setText("设置保存路径");
		buttonSavePath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				String pathStr = showDirectoryDialog(shlTxtfilesplit);
				textSavePath.setText(pathStr);
			}
		});
		
		textFilePath = new Text(shlTxtfilesplit, SWT.BORDER);
		FormData fd_textFilePath = new FormData();
		fd_textFilePath.right = new FormAttachment(buttonFilePath, 313, SWT.RIGHT);
		fd_textFilePath.top = new FormAttachment(0, 14);
		fd_textFilePath.left = new FormAttachment(buttonFilePath, 24);
		textFilePath.setLayoutData(fd_textFilePath);
		textFilePath.setText(filePathStartStr);
		
		textSavePath = new Text(shlTxtfilesplit, SWT.BORDER);
		FormData fd_textSavePath = new FormData();
		fd_textSavePath.top = new FormAttachment(textFilePath, 10);
		fd_textSavePath.right = new FormAttachment(100, -105);
		fd_textSavePath.left = new FormAttachment(buttonSavePath, 24);
		textSavePath.setLayoutData(fd_textSavePath);
		textSavePath.setText(saveDirectoryStartStr);
		
		Button buttonStart = new Button(shlTxtfilesplit, SWT.NONE);
		FormData fd_buttonStart = new FormData();
		fd_buttonStart.bottom = new FormAttachment(100, -10);
		fd_buttonStart.right = new FormAttachment(100, -219);
		buttonStart.setLayoutData(fd_buttonStart);
		buttonStart.setText("开始分割");
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				String filePathStr = textFilePath.getText();//文件路径
				String savePathStr = textSavePath.getText();//保存路径
				
				boolean doSplit = false;
				if (btnFileSize.getSelection()) { //根据文件大小分割
					long fileSizeMegaL = Long.parseLong(textFileSize.getText());//获取设置的文件大小
					long fileSizeByteL = fileSizeMegaL * 1024 * 1024;			//转换成byte
					long fileSizeByteSeleteL = getFileSize(filePathStr);		//获取文件大小(单位byte)
					if (fileSizeByteL >= fileSizeByteSeleteL) {//如果设置的大小大于或等于选择的文件大小
						showDialogMessage(shlTxtfilesplit, "错误", "设置的单个文件大小必须要小于选择的文件的大小");
						doSplit = false;
					} else {
						doSplit = true;
					}
					fileCounter = (int) (fileSizeByteSeleteL/fileSizeByteL);	//根据大小计算需要分割成多少文件
				} else { 						//根据文件个数分割
					fileCounter = Integer.parseInt(textFileCounter.getText());
					if (fileCounter < 2) {
						showDialogMessage(shlTxtfilesplit, "错误", "分割的文件数量必须要大于等于2");
						doSplit = false;
					} else {
						doSplit = true;
					}
				}
				if (doSplit == true) {
					buttonFilePath.setEnabled(false);
					buttonSavePath.setEnabled(false);
					buttonStart.setEnabled(false);
					new Thread(new Runnable() {
						@Override
						public void run() {
							long lineCounterL = getLineCounter(filePathStr);//文件总行数
							if (lineCounterL < fileCounter) {//待分割文件行数小于分割成的文件个数
								display.asyncExec (new Runnable () {
	                        		public void run () {
	                        			showDialogMessage(shlTxtfilesplit, "错误", "待分割文件行数小于分割成的文件个数！");
	                        		}
	                        	});
							} else {//正常分割
								System.out.println("lineCounterL:" + lineCounterL);
								System.out.println("start split...");
								boolean finishedSplit = splitFile(filePathStr, lineCounterL, fileCounter, savePathStr);
								if (finishedSplit) {
									System.out.println("split finished!");
								} else {
									System.out.println("split error!");
								}
								display.asyncExec (new Runnable () {
									public void run () {
										buttonFilePath.setEnabled(true);
										buttonSavePath.setEnabled(true);
										buttonStart.setEnabled(true);
										MessageBox box = new MessageBox(shlTxtfilesplit ,SWT.YES | SWT.ICON_INFORMATION);
										//设置对话框的标题
										box.setText("完成");
										//设置对话框显示的消息
										box.setMessage("分割完成!");
										//打开对话框
										box.open();
									}
								});
							}
						}
					}).start();
				}
			}
		});
		
		Label labelSplitMode = new Label(shlTxtfilesplit, SWT.NONE);
		FormData fd_labelSplitMode = new FormData();
		fd_labelSplitMode.right = new FormAttachment(buttonFilePath, 0, SWT.RIGHT);
		fd_labelSplitMode.bottom = new FormAttachment(buttonSavePath, 57, SWT.BOTTOM);
		fd_labelSplitMode.left = new FormAttachment(0, 25);
		labelSplitMode.setLayoutData(fd_labelSplitMode);
		labelSplitMode.setText("选择分割模式：");
		
		fd_labelSplitMode.top = new FormAttachment(btnFileSize, 0, SWT.TOP);
		FormData fd_btnFileSize = new FormData();
		fd_btnFileSize.top = new FormAttachment(textSavePath, 30);
		fd_btnFileSize.left = new FormAttachment(labelSplitMode, 24);
		btnFileSize.setLayoutData(fd_btnFileSize);
		btnFileSize.setText("按大小");
		btnFileSize.setSelection(true);
		
		textFileSize = new Text(shlTxtfilesplit, SWT.BORDER);
		textFileSize.setText("200");
		FormData fd_textFileSize = new FormData();
		fd_textFileSize.top = new FormAttachment(textSavePath, 27);
		fd_textFileSize.right = new FormAttachment(btnFileSize, 57, SWT.RIGHT);
		fd_textFileSize.left = new FormAttachment(btnFileSize, 6);
		textFileSize.setLayoutData(fd_textFileSize);
		
		Button btnFileCounter = new Button(shlTxtfilesplit, SWT.RADIO);
		FormData fd_btnFileCounter = new FormData();
		fd_btnFileCounter.top = new FormAttachment(textSavePath, 30);
		fd_btnFileCounter.left = new FormAttachment(textFileSize, 53);
		btnFileCounter.setLayoutData(fd_btnFileCounter);
		btnFileCounter.setText("按分割成的文件个数");
		
		Label lblM = new Label(shlTxtfilesplit, SWT.NONE);
		FormData fd_lblM = new FormData();
		fd_lblM.top = new FormAttachment(textSavePath, 30);
		fd_lblM.left = new FormAttachment(textFileSize, 5);
		lblM.setLayoutData(fd_lblM);
		lblM.setText("M");
		
		textFileCounter = new Text(shlTxtfilesplit, SWT.BORDER);
		textFileCounter.setText("5");
		textFileCounter.setToolTipText("");
		FormData fd_textFileCounter = new FormData();
		fd_textFileCounter.top = new FormAttachment(0, 97);
		fd_textFileCounter.right = new FormAttachment(btnFileCounter, 42, SWT.RIGHT);
		fd_textFileCounter.left = new FormAttachment(btnFileCounter, 6);
		textFileCounter.setLayoutData(fd_textFileCounter);
		
		Label label = new Label(shlTxtfilesplit, SWT.NONE);
		label.setText("个");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 100);
		fd_label.left = new FormAttachment(textFileCounter, 6);
		label.setLayoutData(fd_label);
		
		
		FormData fd_lblFileSize = new FormData();
		fd_lblFileSize.right = new FormAttachment(label, -4, SWT.RIGHT);
		fd_lblFileSize.bottom = new FormAttachment(buttonFilePath, 0, SWT.BOTTOM);
		fd_lblFileSize.left = new FormAttachment(textFilePath, 6);
		lblFileSize.setLayoutData(fd_lblFileSize);
		
		Menu menuBar = new Menu(shlTxtfilesplit, SWT.BAR);
		shlTxtfilesplit.setMenuBar(menuBar);
		
		//关于菜单
		MenuItem aboutItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutItem.setText("关于");
		aboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    //事件处理
				MessageBox box = new MessageBox(shlTxtfilesplit ,SWT.YES);
			     //设置对话框的标题
			    box.setText("关于");
			     //设置对话框显示的消息
			    box.setMessage("有任何问题请Email:Dongcheng.Wei@desay-svautomotive.com \r\n版本：V1.0.0 \r\n日期：2017-05-03");
			     //打开对话框
			    box.open();
			}

		});

	}
	
	
	/**
	 * 显示对话框
	 * @param shell
	 * @param titleStr 对话框标题
	 * @param contentStr 对话框内容
	 * @return 
	 * @Date 2017-05-03
	 */
	public int showDialogMessage(Shell shell, String titleStr, String contentStr) {
		  //创建消息框对象，使用警告图标并显示是和否按钮
	     MessageBox box = new MessageBox(shell ,SWT.ICON_ERROR|SWT.YES);
	     //设置对话框的标题
	     box.setText(titleStr);
	     //设置对话框显示的消息
	     box.setMessage(contentStr);
	     //打开对话框，将返回值赋给choice
	     int choice = box.open();
//	     //打印出所选择的值
//	     if (choice==SWT.YES)
//	      System.out.print("Yes");
//	     else if ( choice==SWT.NO)
//	      System.out.print("No");
	     return choice;
	}
	
	/**
	 * 分割文件
	 * @param filePath 文件路径
	 * @param fileLines 文件总行数
	 * @param countFiles 分割成的文件数
	 * @param savePath 保存路径
	 * @return 完成后返回true
	 * @Date 2017-05-03
	 */
	@SuppressWarnings("resource")
	public boolean splitFile(String filePath, long fileLines, int countFiles, String savePath) {
		boolean isFinished = false;
        try {  
            FileReader read = new FileReader(filePath);  
            BufferedReader br = new BufferedReader(read);  
            String row;  
            List<FileWriter> flist = new ArrayList<FileWriter>(); 
            String fileNameStr = getFileName(filePath);
            for (int i = 0; i < countFiles; i++) {  
                flist.add(new FileWriter(savePath + "\\" + fileNameStr + "_part" + i + ".txt"));  
            }  
            int rownum = 0;// 计数器  
            long oneFileLines = fileLines/countFiles;
            int fileCounter = 0;
            while ((row = br.readLine()) != null) { 
            	if (rownum < oneFileLines) {//
            		flist.get(fileCounter).append(row + "\r\n");  //第一个文件
				} else {
					rownum = 0;
					fileCounter ++;
					if (fileCounter >= countFiles) {//超过设置的文件数量
						fileCounter = countFiles-1;
					}
				}
                rownum++;  
            }  
            for (int i = 0; i < flist.size(); i++) {  
                flist.get(i).close();  
            } 
            isFinished = true;
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return isFinished;
	}
	
	/**
	 * 获取文件内容的行数
	 * @param pathStr 文件路径
	 * @return 行数
	 * @Date 2017-05-03
	 */
	@SuppressWarnings("resource")
	public long getLineCounter(String pathStr) {
		long rownum = 0;// 计数器  
        try {  
            FileReader read = new FileReader(pathStr);  
            BufferedReader br = new BufferedReader(read);  
            
            while (br.readLine() != null) {  
                rownum++;  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		return rownum;
	}
	
	/**
	 * 获取文件大小
	 * @param pathStr 文件路径
	 * @return Byte 字节
	 * @Date 2017-05-03
	 */
	public long getFileSize(String pathStr) {
		File f= new File(pathStr);
		long fileSizeL = 0;
	    if (f.exists() && f.isFile()){  
	    	fileSizeL = f.length();
	    }else{  
	    	System.out.println("file doesn't exist or is not a file");  
	    }
	    return fileSizeL;
	}
	
	/**
	 * 获取文件名（不包含后缀）
	 * @param pathStr 文件路径
	 * @return 文件名
	 * @Date 2017-05-03
	 */
	public String getFileName(String pathStr) {
		File f= new File(pathStr);
		String fileNameStr = "";
	    if (f.exists() && f.isFile()){  
	    	fileNameStr = f.getName();
	    	fileNameStr = fileNameStr.substring(0, fileNameStr.lastIndexOf("."));
	    }else{  
	    	System.out.println("file doesn't exist or is not a file");  
	    }
	    return fileNameStr;
	}
	
	/**
	 * 获取文件当前目录
	 * @param pathStr
	 * @return
	 * @Date 2017-05-03
	 */
	public String getAbsolutePath(String pathStr) {
		File f= new File(pathStr);
		String fileAbsolutePath = "";
	    if (f.exists() && f.isFile()){  
	    	fileAbsolutePath = f.getParent();
	    }else{  
	    	System.out.println("file doesn't exist or is not a file");  
	    }
	    return fileAbsolutePath;
	}
	
	/**
	 * 选择目录对话框
	 * @param shell
	 * @return 路径
	 * @Date 2017-04-28
	 */
	public String showDirectoryDialog(Shell shell) {
		//选择目录
		String dirctory = "c:\\";
		DirectoryDialog dirctorydialog = new DirectoryDialog(shell);
		dirctorydialog.setText("目录选择对话框");
		dirctorydialog.setFilterPath(dirctory);
		dirctorydialog.setMessage("选择要使用的工作空间目录:");
		dirctory = dirctorydialog.open();
		if (dirctory == null) {
			dirctory = saveDirectoryStartStr;
		}
		System.out.println("Selete save path:" + dirctory);
		return dirctory;
	}
	
	
	/**
	 * 文件选择对话框
	 * @param shell
	 * @return 文件全路径 (包含文件名)
	 * @Date 2017-04-28
	 */
	public String fileSeleteDialog(Shell shell){
		//新建文件对话框，并设置为打开的方式
		FileDialog filedlg=new FileDialog(shell,SWT.OPEN);
		//设置文件对话框的标题
		filedlg.setText("文件选择");
		//设置初始路径
		filedlg.setFilterPath("SystemRoot");
		//打开文件对话框，返回选中文件的绝对路径
		String selected=filedlg.open();
		System.out.println("Selete file："+selected);
		if (selected == null) {
			selected = filePathStartStr;
		}
		
		return selected;
	}
	
    /**
     * 把文件大小格式化
     * @param size byte为单位的文件大小
     * @return 转成的字符串
     * @Date 2017-05-03
     */
    private static String getFileSizeFormate(double size){
        String[] units = new String[]{"B","KB","MB","GB","TB","PB"};
        double mod = 1024.0;
        int i=0;
        for (i = 0; size >= mod; i++) {
        	size /= mod;
        }
        return Math.round(size) + units[i];
    }
}
