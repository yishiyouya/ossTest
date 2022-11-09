package com.badfox.osstest.util;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerateMyBatisPlus {

    public static String OUTFILE = "";
    public static String OUTFILE_RESOURCE = "";
    public static final String URL = "jdbc:mysql://localhost:3306/mall?useUnicode=true&characterEncoding=utf-8&useSSL=true";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root123@W";

    static {
        File file = new File("D://workspace//idea//ossTest//generator//");
        String path = file.getPath();
        OUTFILE = path + File.separator + "java";
        OUTFILE_RESOURCE = path + File.separator + "resources";
    }

    public static void main(String[] args) {
        testGenerator();
        //generatorManual();
    }

    public static void testGenerator() {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    // 设置作者
                    builder.author("badfox")
                            // 开启 swagger 模式
                            .enableSwagger()
                            // 覆盖已生成文件
                            .fileOverride()
                            // 指定输出目录
                            .outputDir(OUTFILE);
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent("com.badfox.osstest.util.generator")
                            // 设置父包模块名
                            .moduleName("system")
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    OUTFILE_RESOURCE));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("productinfo")
                            // 设置过滤表前缀
                            .addTablePrefix("t_", "c_");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    /**
     * 手动生成
     */
    public static void generatorManual() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        DataSourceConfig.Builder DATA_SOURCE_CONFIG =
                new DataSourceConfig.Builder(URL, USERNAME, PASSWORD);

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？"))
                        .fileOverride())
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(
                                                            getTables(
                                                                    scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                                                            .controllerBuilder().enableRestStyle().enableHyphenStyle()
                                                            .entityBuilder().enableLombok().addTableFills(
                                                            new Column("crt_time", FieldFill.INSERT)
                        ).build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();
    }

    /**
     * 处理 all 情况
      */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
