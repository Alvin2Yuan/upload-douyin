package com.slong.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Hello world!
 *
 */
public class App 
{
    static {
        WebDriverManager.chromedriver().clearDriverCache().driverVersion("105.0.5195.52").setup();
    }
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );
        String baseUrl="127.0.0.1:6873";
        //获取环境列表
       String envUrl=baseUrl+"/api/v1/env/list";
       String envStr= HttpUtil.post(envUrl,"");
       System.out.println(envStr);
        JSONObject envObj= JSONUtil.parseObj(envStr);
        JSONArray list= envObj.getJSONObject("data").getJSONArray("list");
        //打开环境
        String browserUrl=baseUrl+"/api/v1/browser/start";
        for(int i=0;i<list.size();i++){
           JSONObject env= list.getJSONObject(i);
          String code= env.getStr("containerCode");
          JSONObject browserParams= new JSONObject();
            browserParams.set("containerCode",code);
            String browserStr= HttpUtil.post(browserUrl,browserParams.toString());
            System.out.println(browserStr);
            String debuggingPort=JSONUtil.parseObj(browserStr).getJSONObject("data").getStr("debuggingPort");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("debuggerAddress", "127.0.0.1:" + debuggingPort); // 填写打开环境的debuggingPort参数
            ChromeDriver driver = new ChromeDriver(chromeOptions);
            driver.get("http://www.baidu.com");
            driver.findElement(By.id("kw")).sendKeys("抖音");
            driver.findElement(By.id("su")).click();
        }
    }
}
