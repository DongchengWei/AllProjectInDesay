package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdCtrl {
	
    private static CmdCtrl cmdCtrl;

    private Runtime runtime = Runtime.getRuntime();

    public static  CmdCtrl getInstance(){
        if(cmdCtrl == null){
            cmdCtrl = new CmdCtrl();
        }

        return cmdCtrl;
    }


    /**
     * 运行cmd，并且返回结果
     *
     * @param command 要运行的命令
     * @return
     */
    public List<String> execCmd(String command) {
        if (!command.isEmpty()) {

            BufferedReader br = null;
            try {
                //执行cmd命令
                Process process = runtime.exec("cmd /c " + command);

                br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
                String line = "";

                List<String> content = new ArrayList<>();

                while ((line = br.readLine()) != null){
                    if (!line.isEmpty()) {
                        content.add(line);
                    }
                }

                //process.destroy();
                return content;
            } catch (Exception e) {
                System.out.println("execCmd执行命令错误!" + e.getMessage());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }


        return null;
    }


    /**
     * 执行cmd命令看看有没有成功执行
     * @param command 对应的命令
     * @return
     */
    public Boolean execCmdTrue(String command){
        try {
            //执行cmd命令
            Process process = runtime.exec("cmd /c " + command);
            //process.waitFor();
            //process.destroy();
            return true;
        } catch (Exception e) {
            System.out.println("execCmdTrue的cmd命令执行错误" + e.getMessage());
            return false;
        }
    }
}
