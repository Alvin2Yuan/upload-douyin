package com.slong.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 比特浏览器支持
 *
 */
public class App 
{

    public static void main( String[] args )
    {


        String baseUrl="http://127.0.0.1:54345";
        //获取浏览器窗口列表
       String envUrl=baseUrl+"/browser/list";
       for(int page=0;page<100;page++){
           JSONObject body=new JSONObject();
           body.set("page",page);
           body.set("pageSize",100);
           String envStr= HttpUtil.post(envUrl,body.toString());
           System.out.println(envStr);

           JSONObject envObj= JSONUtil.parseObj(envStr);
           JSONArray list= envObj.getJSONObject("data").getJSONArray("list");
           //窗口已经获取完成判断
           if(list.size()<=0){
               break;
           }
           //打开浏览器窗口
           String browserUrl=baseUrl+"/browser/open";
           for(int i=0;i<list.size();i++){
               JSONObject env= list.getJSONObject(i);
               String id= env.getStr("id");
               JSONObject browserParams= new JSONObject();
               browserParams.set("id",id);
               String browserStr= HttpUtil.post(browserUrl,browserParams.toString());
               System.out.println(browserStr);
               JSONObject browserData=JSONUtil.parseObj(browserStr).getJSONObject("data");
               String http=browserData.getStr("http");
               String driverPath=browserData.getStr("driver");
               System.setProperty("webdriver.chrome.driver",driverPath);
               ChromeOptions chromeOptions = new ChromeOptions();
               chromeOptions.setExperimentalOption("debuggerAddress", http); // 填写打开环境的debuggingPort参数
               ChromeDriver driver = new ChromeDriver(chromeOptions);
               driver.get("http://www.baidu.com");
//               String windowName="百度";
//               for (String windowHandle : driver.getWindowHandles()) {
//                   WebDriver window= driver.switchTo().window(windowHandle);
//                   if(window.getTitle().contains(windowName)) {
//                       break;
//                   }
//               }
               driver.findElement(By.id("kw")).sendKeys("抖音");
               driver.findElement(By.id("su")).click();
               driver.close();
           }
       }


    }
}
