package com.badfox.basicpro.util;

import com.badfox.basicpro.thread.RunTask;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Field;

public class Utils {

    public static void main(String[] args) {
        testReplaceUrlSpecialChar();
    }

    public static void testReplaceUrlSpecialChar() {
        String url = "00 0/4 * * + ?";
        String newUrl = replaceUrlSpecialChar(url);
        System.out.println(newUrl);
        String backUrl = backUrlSpecialChar(newUrl);
        System.out.println(backUrl);
    }
    public static String replaceUrlSpecialChar(String url) {
        String newUrl = url;

        newUrl = newUrl.replaceAll("%", "%25");
        newUrl = newUrl.replaceAll("\\+", "%2B");
        newUrl = newUrl.replaceAll("\\?", "%3F");
        newUrl = newUrl.replaceAll("\\*", "%2A");
        newUrl = newUrl.replaceAll("/", "%2F");
        newUrl = newUrl.replaceAll(" ","%20");
        newUrl = newUrl.replaceAll("#", "%23");
        newUrl = newUrl.replaceAll("&", "%26");
        newUrl = newUrl.replaceAll("=", "%3D");
        return newUrl;
    }

    public static String backUrlSpecialChar(String url) {
        String newUrl = url;

        newUrl = newUrl.replace("%20"," ");
        //newUrl = newUrl.replaceAll("%2F", "/");
        //newUrl = newUrl.replaceAll("%2B", "+");
        //newUrl = newUrl.replaceAll("%3F", "?");
        //newUrl = newUrl.replaceAll("%25", "%");
        //newUrl = newUrl.replaceAll("%23", "#");
        //newUrl = newUrl.replaceAll("%26", "&");
        //newUrl = newUrl.replaceAll("%3D", "=");
        return newUrl;
    }

    public static String getCronById(String taskId) {
        return "0/" + taskId + MyConstants.curCronHalf;
    }

    public static TriggerTask triggerTask(String taskId, String cron) {
        RunTask task = new RunTask();
        task.setId(taskId);
        task.setCron(cron);
        return new TriggerTask(task, triggerContext -> {
            System.out.println("执行:" + cron + ",id=" + taskId);
            //这里与config类的代码类似。根据需要自行处理
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });

    }
    public static Object getProperty(Object obj, String name) throws NoSuchFieldException {
        Object value = null;
        Field field = findField(obj.getClass(), name);
        if (field == null) {
            throw new NoSuchFieldException("no such field [" + name + "]");
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            value = field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(accessible);
        return value;
    }

    public static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            return findDeclaredField(clazz, name);
        }
    }
    public static Field findDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredField(clazz.getSuperclass(), name);
            }
            return null;
        }
    }


}
