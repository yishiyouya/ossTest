package com.badfox.osstest.controller;

import com.alibaba.fastjson2.JSONObject;
import com.badfox.osstest.configs.ScheduledConfig;
import com.badfox.osstest.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@RestController
public class ScheduleController {
    @Autowired
    ScheduledConfig scheduledConfig;

    @GetMapping("/addCron")
    public String addCron(@RequestParam(value = "cron", required = false) String cron) throws IOException {
        int id = 1;
        String[] cronArr = new String[]{
                "0 0/2 * * * ?",
                "0 0 2 1 * ?",
                "0 15 10 ? * MON-FRI",
                "0 15 10 ? 6 L",
                "0 15 10 L * ?",
                "0 15 10 ? * L",
                "0 0 10,14,16 * * ?",
                "0 0/30 9-17 * * ?",
                "0 0 12 ? * WED",
                "0 0 12 * * ?",
                "0 15 10 ? * *",
                "0 15 10 * * ?",
                "0 15 10 * * ?",
                "0 15 10 * * ?",
                "0 * 14 * * ?",
                "0 0/5 14 * * ?",
                "0 0/5 14,18 * * ?",
                "0 0-5 14 * * ?",
                "0 10,44 14 ? 3 WED",
                "0 15 10 ? * MON-FRI",
                "0 15 10 15 * ?",
                "0 15 10 ? * 6#3"
        };
        for (String cronS : cronArr) {
            id++;
            cron = Utils.replaceUrlSpecialChar(cronS);
            String url = "http://localhost:8082/addTask?id=" + id+"&cron="+cron;
            HttpGet httpGet = new HttpGet(url);

            // 设置类型 "application/x-www-form-urlencoded" "application/json"
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
            System.out.println("调用URL: " + httpGet.getURI());

            //        httpClient实例化
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 执行请求并获取返回
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println(response);
        }
        return cronArr.toString();
    }

    @GetMapping("/addTaskInfo")
    public String addTaskInfo(@RequestParam("info")String info)
            throws NoSuchFieldException {
        JSONObject infoJson = JSONObject.parseObject(info);
        String id = infoJson.getString("id");
        String cron = infoJson.getString("cron");

        Map<String, ScheduledFuture<?>> taskRegistrar =
                (Map<String, ScheduledFuture<?>>) Utils.getProperty(scheduledConfig, "taskFutures");
        System.out.println(taskRegistrar.size());
        String curCron = Utils.getCronById(id);

        scheduledConfig.addTask(id, Utils.triggerTask(id, curCron));
        //这里可根据需要自行存库jdbcTemplate.insert()。
        String result = String.format("addTask: id=%s, cron=%s", id, cron);
        return result;
    }


    @GetMapping("/addTask")
    public String addTask(@RequestParam("id") String id,
                          @RequestParam(value = "cron", required = false) String cron)
            throws NoSuchFieldException {
        Map<String, ScheduledFuture<?>> taskRegistrar =
                (Map<String, ScheduledFuture<?>>) Utils.getProperty(scheduledConfig, "taskFutures");
        System.out.println(taskRegistrar.size());
        String curCron = Utils.getCronById(id);

        try {
            scheduledConfig.addTask(id, Utils.triggerTask(id, cron));
        } catch (Exception e) {
            System.err.println(cron);
        }
        //这里可根据需要自行存库jdbcTemplate.insert()。
        return "addTask" + id;
    }


}
