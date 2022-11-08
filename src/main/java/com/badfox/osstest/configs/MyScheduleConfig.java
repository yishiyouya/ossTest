package com.badfox.osstest.configs;

import com.badfox.osstest.pojo.MyTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

//@Configuration
public class MyScheduleConfig implements SchedulingConfigurer {

    protected static Logger logger = LoggerFactory.getLogger(MyScheduleConfig.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    private static Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    private static List<MyTask> taskList = new ArrayList<>();

    static {
        threadPoolTaskScheduler.initialize();
    }

    private List<MyTask> getAllTasks() throws Exception {
        List<MyTask> list = new ArrayList<>();
        int tCnt = 10;
        for (int i = 0; i < tCnt; i++) {
            MyTask myTask = new MyTask(i + "", "0/" + i + " 0/5 00 * * ?", "beanName" + i);
            list.add(myTask);
        }
        return list;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        /*List<MyTask> tasks = null;
        try {
            tasks = getAllTasks();
            logger.info("定时任务启动,预计启动任务数量={}; time={}", tasks.size(), sdf.format(new Date()));

            //校验数据（这个步骤主要是为了打印日志，可以省略）
            checkDataList(tasks);

            //通过校验的数据执行定时任务
            int count = 0;
            if (!tasks.isEmpty()) {
                for (MyTask task : tasks) {
                    try {
                        //满足条件的task才执行
                        if (Integer.parseInt(task.getId()) % 2 == 0) {
                            start(task);
                            count++;
                            logger.info(task.toString());
                        } else {
                            logger.error("Do not run the task:{}", task.toString());
                        }
                    } catch (Exception e) {
                        logger.error("定时任务启动错误:{};{}", task.getBean_name(), e.getMessage());
                    }
                }
            }
            logger.info("定时任务实际启动数量={}; time={}", count, sdf.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private static Runnable getRunnable(MyTask task) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Runnable run..." + task.toString());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        };
    }


    private static Trigger getTrigger(MyTask task) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //将Cron 0/1 * * * * ? 输入取得下一次执行的时间
                String cron = "0/1 * * * * ?";
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };

    }

    private List<MyTask> checkDataList(List<MyTask> list) {
        String errMsg = "";

        return list;
    }

    public static String checkOneData(MyTask task) {
        String result = "success";
        Class cal = null;

        return result;
    }

    /**
     * 启动定时任务
     *
     * @param task
     * @param
     */
    public static void start(MyTask task) {
        ScheduledFuture<?> scheduledFuture =
                threadPoolTaskScheduler.schedule(getRunnable(task), getTrigger(task));
        scheduledFutureMap.put(task.getId(), scheduledFuture);
        logger.info("启动定时任务" + task.getId());
    }

    /**
     * 取消定时任务
     *
     * @param task
     */
    public static void cancel(MyTask task) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(task.getId());

        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(Boolean.FALSE);
        }

        scheduledFutureMap.remove(task.getId());
        logger.info("取消定时任务" + task.getId());

    }

    /**
     * 编辑
     *
     * @param task
     * @param
     */
    public static void reset(MyTask task) {
        logger.info("修改定时任务开始" + task.getId());
        cancel(task);
        start(task);
        logger.info("修改定时任务结束" + task.getId());
    }

}