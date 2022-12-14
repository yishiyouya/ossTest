package com.badfox.basicpro.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RunTask implements Runnable {

    private String id;
    private String cron;
    private String Bean_name;

    @Override
    public void run() {
        System.out.println("MyRunTask run: " + this.toString());
    }
}
