package cn.magicbeans.android.ipmanager.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * date：2019/3/12 0012 on 16:01
 * desc:${}
 * author:BarryL
 */
public class MBPingUtils {

    public static boolean ping(String address){
        Process process = null;
        String returnMsg="";
        try {
            process = Runtime.getRuntime().exec("ping "+address);
            InputStreamReader r = new InputStreamReader(process.getInputStream());
            LineNumberReader returnData = new LineNumberReader(r);
            String line = "";
            while ((line = returnData.readLine()) != null) {
                Log.e("line", "ping: " + line );
                returnMsg += line;
            }
            Log.e("returnMsg", "ping: " + returnMsg );
            if(returnMsg.indexOf("100% loss")!=-1){
                Log.e("", "与 " +address +" 连接不畅通.");
                return false;
            }  else{
                Log.e("", "与 " +address +" 连接畅通.");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static final boolean ping1(String urld) {

                 String result = null;
                 try {
                         String ip = urld;// 除非百度挂了，否则用这个应该没问题(也可以换成自己要连接的服务器地址)
                         Process p = Runtime.getRuntime().exec("ping -c 1 -w 10 " + ip);// ping3次
                         // 读取ping的内容，可不加。
                         InputStream input = p.getInputStream();
                         BufferedReader in = new BufferedReader(new InputStreamReader(input));
                         StringBuffer stringBuffer = new StringBuffer();
                         String content = "";
                         while ((content = in.readLine()) != null) {
                                 stringBuffer.append(content);
                             }
                         Log.i("TTT", "result content : " + stringBuffer.toString());
                         // PING的状态
                         int status = p.waitFor();
                         if (status == 0) {
                                 result = "successful~";
                                 return true;
                            } else {
                                 result = "failed~ cannot reach the IP address";
                                 return false;
                             }

                     } catch (IOException e) {
                         result = "failed~ IOException";
                     } catch (InterruptedException e) {
                         result = "failed~ InterruptedException";
                     } finally {
                         Log.i("TTT", "result = " + result);
                     }
                 return false;
             }


}
