/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-05-29
 */
package com.powercontrol;

import com.utils.DataEncode;
import com.utils.SerialPortUtil;

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年5月29日 下午4:45:30 
* 类说明 :
*/
/**
 * @author uidq0460
 *
 */
public class It6831 {

	public static String START_CODE = "AA";//第一个字节
	public static String POWER_ADD = "00";//第二个字节，电源地址
	public static String POWER_CMD = "00";//第三个字节，命令字
	public static String CODE_FRAME = "AA00000000000000000000000000000000000000000000000000";		//pc
	public static final boolean POWER_ON = true;
	public static final  boolean POWER_OFF = false;
	public static final  boolean PC_MODE = true;
	public static final  boolean PANAL_MODE = false;
	

	public static int OUTPUT_VOLTAGE = 23;//输出电压命令
	public static int OUTPUT_CURRENT = 24;//输出电压命令
	

	/**
	 * 发送查询电源状态的指令
	 * @Date 2017-06-01
	 */
	public static void sentGetPowerStatusCmd() {
//		AA002600000000000000000000000000000000000000000000D00A
		String cmdStr = "AA002600000000000000000000000000000000000000000000D00A";
		SerialPortUtil.writeHexStrToPort(cmdStr);
	}
	
	/**
	 * 解析电源返回码
	 * @param returnStr 返回码，通过这个返回码解析当前电源输出电压电流等信息
	 * @return 返回电源状态
	 * @Date 2017-06-01
	 */
//	public static PowerStatus getPowerStatus(String returnStr) {
//		String startCmd = returnStr.substring(0, 2);
//		String addCmd = returnStr.substring(2, 4);
//		String cmdCmd = returnStr.substring(4, 6);
//		PowerStatus powerStatus = new PowerStatus();
//		if (startCmd.equals("AA") && addCmd.equals(POWER_ADD) && cmdCmd.equals("26")) {
//			String currCmd = returnStr.substring(6, 10);
//			String realCurrStr = DataEncode.switchCode(currCmd);
//			long currLong = Long.parseLong(realCurrStr, 16);
//			double curr = ((double)currLong)/1000;
//			String currStr = String.format("%.3f", curr).toString();
//			powerStatus.setCurrNowStr(currStr);
//			System.out.println("currLong = " + currLong + ", double=" + currStr);
//			
//			String voltCmd = returnStr.substring(10, 14);
//			String realVoltStr = DataEncode.switchCode(voltCmd);
//			long voltLong = Long.parseLong(realVoltStr, 16);
//			double volt = ((double)voltLong)/1000;
//			String voltStr = String.format("%.3f", volt).toString();
//			powerStatus.setVoltNowStr(voltStr);
//			System.out.println("voltLong = " + voltLong + ", doubleVolt=" + voltStr);
//		}
//		return powerStatus;
//	}
	
	/**
	 * 设置电源输出电压,待增加数值限制
	 * 占两个字节，第4和第5个字节
	 * @param voltStr 电压如12.000
	 * @Date 2017-05-29
	 */
	public static void setOutputVoltage(String voltStr) {
		setCmd(OUTPUT_VOLTAGE, voltStr);
	}
	
	/**
	 * 设置电源输出电流大小
	 * @param currStr
	 * @Date 2017-05-31
	 */
	public static void setOutputCurrent(String currStr) {
		setCmd(OUTPUT_CURRENT, currStr);
	}
	
	/**
	 * 设置电压和电流等的命令
	 * @param cmd 命令，电压，电流等
	 * @param dataStr 命令数据，比如电压值、电流值的字符串
	 * @Date 2017-05-31
	 */
	public static void setCmd(int cmd,String dataStr) {
		POWER_CMD = "" + cmd;//电压的命令字
		String dataCmd = "0000";
		double testDou = Double.parseDouble(dataStr);
		int dataInt = (int) (testDou*1000);				//int
		
		String dataHexStr = Integer.toHexString(dataInt);
		int hexIntLength = dataHexStr.length();			//实际电压字符串长度
		int dataIntLength = dataCmd.length();  			//规定电压字符串长度
		int addCounter = dataIntLength - hexIntLength;	//需要补的长度
		String addStr = "";								//待补字符串
		if (hexIntLength < dataIntLength) {
			for (int i = 0; i < addCounter; i++) {
				addStr += "0";
			}
			dataHexStr = addStr + dataHexStr;
		}
		
		String switchCodeStr = DataEncode.switchCode(dataHexStr);
//		System.out.println("SetData="+dataInt + ", DataHexsStr=" + dataHexStr + ",switch=" + switchCodeStr);
		String cmdStr = START_CODE + POWER_ADD + POWER_CMD + switchCodeStr + CODE_FRAME.substring(10, 50);
		cmdStr += DataEncode.getVerifyCode(cmdStr);
//		System.out.println("cmdVolt=" + cmdStr);
		SerialPortUtil.writeHexStrToPort(cmdStr);
	}
	
	/**
	 * 控制电源输出
	 * @param onOrOff true为打开电源输出
	 * @Date 2017-05-29
	 */
	public static void output(boolean onOrOff) {
		POWER_CMD = "21";//电源命令字
		String codeStr = "00";
		if (onOrOff == POWER_ON) {
			codeStr = "01";
		}
		String cmdVolt = START_CODE + POWER_ADD + POWER_CMD + codeStr + CODE_FRAME.substring(8, 50);
		cmdVolt += DataEncode.getVerifyCode(cmdVolt);
//		System.out.println(cmdVolt);
		SerialPortUtil.writeHexStrToPort(cmdVolt);
	}
	
	/**
	 * 电源控制模式
	 * @param mode true为PC端(远程)，false为面板控制
	 * @Date 2017-05-29
	 */
	public static void ctrlMode(boolean mode) {
		POWER_CMD = "20";//电源命令字
		String codeStr = "00";
		if (mode == PC_MODE) {
			codeStr = "01";
		}
		String cmdVolt = START_CODE + POWER_ADD + POWER_CMD + codeStr + CODE_FRAME.substring(8, 50);
		cmdVolt += DataEncode.getVerifyCode(cmdVolt);
//		System.out.println(cmdVolt);
		SerialPortUtil.writeHexStrToPort(cmdVolt);
	}
}
