package com.badfox.basicpro.configs;

import com.badfox.basicpro.util.Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
@EnableScheduling
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {
    //笔者这里配合数据库来实现动态添加。方便查询数据库这里使用JdbcTemplate

    private ScheduledTaskRegistrar taskRegistrar;

    private Set<ScheduledFuture<?>> scheduledFutures = null;
    private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    /**
     * 只启动时，执行
     * 这个方法在Spring初始化的时候会帮我们执行，这里也会拉取数据库内需要执行的任务，进行添加到定时器里。
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        /**
         * List<TriggerTask> list= new ArrayList<>();
        //查询出来当前数据库中存储的所有有效的任务
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from corn  where status=1");
        //循环添加任务
        maps.forEach(t->{
            TriggerTask triggerTask = new TriggerTask(()->{
                System.out.println("执行定时任务："+ LocalDateTime.now().toLocalTime());
            },triggerContext -> {
                System.out.println("执行Cron:"+t.get("corn").toString()+",id="+t.get("id").toString());
                //如果需要动态的指定当前定时任务的执行corn。这里可以增加一步，查询数据库操作。如果任务corn不需要精确修改，corn可进行缓存。到期在去查询数据库。这里根据读者的需求自行取舍。
                return new CronTrigger(t.get("corn").toString()).nextExecutionTime(triggerContext);
            });
            list.add(triggerTask);
        });
        //将任务列表注册到定时器
        scheduledTaskRegistrar.setTriggerTasksList(list);*/

        /**long curSecond = System.currentTimeMillis() / 1000;
        String taskId = ""+curSecond % 10;
        if (curSecond % 5 == 0){
            addTask(taskId, Utils.triggerTask(taskId, Utils.getCronById(taskId)));
            System.out.println("second-"+curSecond+", MyRunTask can run: " + this.toString());
        } else {
            System.err.println("second-"+curSecond+", No run: " + this.toString());
        }*/
        this.taskRegistrar = scheduledTaskRegistrar;
    }


    /**
     * 添加任务
     * @param taskId
     * @param triggerTask
     */
    public void addTask(String taskId, TriggerTask triggerTask) {
        //如果定时任务id已存在，则取消原定时器，从新创建新定时器，这里也是个更新定时任务的过程。
        if (taskFutures.containsKey(taskId)) {
            System.out.println("the taskId[" + taskId + "]  取消，重新添加");
            cancelTriggerTask(taskId);
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        getScheduledFutures().add(future);
        taskFutures.put(taskId, future);
    }

    /**
     * 获取任务列表
     */
    private Set<ScheduledFuture<?>> getScheduledFutures() {
        if (scheduledFutures == null) {
            try {
                scheduledFutures = (Set<ScheduledFuture<?>>) Utils.getProperty(taskRegistrar, "scheduledTasks");
            } catch (NoSuchFieldException e) {
                throw new SchedulingException("not found scheduledFutures field.");
            }
        }
        return scheduledFutures;
    }

    /**
     * 取消任务
     */
    public void cancelTriggerTask(String taskId) {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
        getScheduledFutures().remove(future);
    }

}
