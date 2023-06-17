package com.sym.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehiclesystem?useSSL=false";
        String username = "root";
        String password="123456";
        String moduleName = "";
        String mapperLocation = "D:\\mavenWork\\CarManageSystem\\src\\main\\resources\\mapper"+moduleName;
        String tables = "car,component,order,order_component,user";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("sym") // 设置作者
                            /*.enableSwagger() */// 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\mavenWork\\CarManageSystem\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.sym") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix(""); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

