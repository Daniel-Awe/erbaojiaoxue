package cn.edu.zju.temp.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneratorTool {
    /**
     * 输出文件夹，是绝对路径，一直填到java文件夹
     * <p>
     * 不同平台修改这个就可以
     */
    private static String linuxOutputDir = "/home/yoke/workspace/algorithmic-asset/entity/src/main/java";
    private static String winOutputDir = "C:\\Users\\15737\\IdeaProjects\\temp-maven\\src\\main\\java";
    /**
     * 生成的xml文件保存路径，绝对路径
     * <p>
     * 不同平台只需要更改这个，但是要填到resources的mapper文件夹
     */
    private static String linuxXmlOutputDir = "/home/yoke/workspace/algorithmic-asset/entity/src/main/resources/mapper";
    private static String winXmlOutputDir = "C:\\Users\\15737\\IdeaProjects\\temp-maven\\src\\main\\resources\\mapper";

    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(
            "jdbc:mysql://localhost:3306/ebjiaoxue?serverTimezone=Asia/Shanghai", "root", "122333gsq")
            .schema("nucleus")
            .build();

    /**
     * 策略配置
     * <p>
     * 要生成的表名在这里配
     */
    protected static StrategyConfig strategyConfig() {
        // 字段填充,显示标注：创建时间、修改时间、逻辑删除三个字段的填充策略
        List<IFill> tableFills = Arrays.asList(new Column("create_time", FieldFill.INSERT), new Column("edit_time", FieldFill.INSERT_UPDATE), new Column("deleted", FieldFill.INSERT));

        // 这是表名
        return new StrategyConfig.Builder()
                .addInclude(Arrays.asList("model_study"))
                .entityBuilder()
                .enableFileOverride()
                .addTableFills(tableFills)
                .logicDeleteColumnName("deleted")
                .enableLombok()
                .controllerBuilder()
                .enableFileOverride()
                .enableRestStyle()
                .mapperBuilder()
                .enableFileOverride()
                .serviceBuilder()
                .enableFileOverride()
                .build();
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig() {
        String os = System.getProperty("os.name");
        String outputDir = os.startsWith("Windows") ? winOutputDir : linuxOutputDir;
        return new GlobalConfig.Builder()
                .author("zcx")
                .disableOpenDir()
                .enableSpringdoc()
                .outputDir(outputDir);
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig() {
        String os = System.getProperty("os.name");
        String xmlOutputDir = os.startsWith("Windows") ? winXmlOutputDir : linuxXmlOutputDir;
        return new PackageConfig.Builder()
                .parent("cn.edu.zju")
                .moduleName("temp")
                .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir));
    }

    /**
     * 模板配置
     */
    protected static TemplateConfig.Builder templateConfig() {
        return new TemplateConfig.Builder()
                .controller("templates/controller.java")
                .entity("templates/entity.java")
                .mapper("templates/mapper.java")
                .xml("templates/mapper.xml")
                .service("templates/service.java")
                .serviceImpl("templates/serviceImpl.java");
    }

    /**
     * 注入配置
     */
    // protected static InjectionConfig.Builder injectionConfig() {
    // // 测试自定义输出文件之前注入操作，该操作再执行生成代码前 debug 查看
    // return new InjectionConfig.Builder().beforeOutputFile((tableInfo, objectMap)
    // -> {
    // System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: "
    // + objectMap.size());
    // });
    // }
    /**
     * 注入配置
     */
    protected static InjectionConfig.Builder injectionConfig() {
        return new InjectionConfig.Builder()
                .beforeOutputFile(((tableInfo, objectMap) -> {
                    String entityName = tableInfo.getEntityName();
                    String lowWord = entityName.substring(0, 1).toLowerCase();
                    String serviceObjectName = lowWord + entityName.substring(1) + "Service" ;
                    objectMap.put("serviceRefName" , serviceObjectName);
                }))
                .customFile(Collections.emptyList());
    }

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig());
        generator.global(globalConfig().build());
        generator.packageInfo(packageConfig().build());
        generator.template(templateConfig().build());
        generator.injection(injectionConfig().build());

        FreemarkerTemplateEngine engine = new FreemarkerTemplateEngine();
        generator.execute(engine);
    }

}
