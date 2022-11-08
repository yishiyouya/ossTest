package com.badfox.osstest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyTask {
    private String id;
    private String cron;
    private String Bean_name;

}
