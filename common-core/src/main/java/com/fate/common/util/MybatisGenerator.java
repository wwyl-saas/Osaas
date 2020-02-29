package com.fate.common.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;



/**
 * @program: parent
 * @description: 生成mybatis类
 * @author: chenyixin
 * @create: 2019-06-01 11:33
 **/
@Slf4j
public class MybatisGenerator {
    /**
     * 代码生成
     * @param args
     */
    public static void main(String[] args) {
        //需要生成的表名
        String[] tables=new String[]{"t_coupon","t_card","t_coupon_effect_rule","t_coupon_issue_rule","t_customer_card","t_customer_coupon"};

        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(createGlobalConfig()); // 全局配置
        generator.setDataSource(createDataSourceConfig());  // 数据源配置
        generator.setStrategy(createStrategyConfig(tables));// 策略配置
        generator.setPackageInfo(createPackageConfig());   // 包配置
        generator.setCfg(createInjectionConfig()); // 注入自定义配置
        generator.setTemplate(createTemplateConfig());

        generator.execute();
    }

    /**
     * 数据源配置，通过该配置，指定需要生成代码的具体数据库
     * @return
     */
    private static DataSourceConfig createDataSourceConfig(){
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                if (fieldType.toLowerCase().equals("tinyint")) {
                    return DbColumnType.INTEGER;
                }
                if (fieldType.toLowerCase().equals("date")) {
                    return DbColumnType.LOCAL_DATE;
                }
                if (fieldType.toLowerCase().equals("time")) {
                    return DbColumnType.LOCAL_TIME;
                }
                if (fieldType.toLowerCase().equals("datetime")) {
                    return DbColumnType.LOCAL_DATE_TIME;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });
        dataSourceConfig.setSchemaName("fate");
        dataSourceConfig.setUrl("jdbc:mysql://cdb-hhkbssqo.bj.tencentcdb.com:10138/fate?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("fate");
        dataSourceConfig.setPassword("P@ssw0rd");
        return dataSourceConfig;
    }

    /**
     * 全局策略配置
     * @return
     */
    private static GlobalConfig createGlobalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(getJavaPath());//输出目录
        globalConfig.setFileOverride(false);//是否覆盖已有文件
        globalConfig.setAuthor("mybatis-plus");//作者
        globalConfig.setOpen(false);//是否打开输出目录
        globalConfig.setEnableCache(false);//是否在xml中添加二级缓存配置
        globalConfig.setBaseResultMap(false);//开启 BaseResultMap
        globalConfig.setBaseColumnList(false);//baseColumnList
        globalConfig.setSwagger2(false);//开启 swagger2 模式
        globalConfig.setActiveRecord(true);//开启 ActiveRecord 模式
        globalConfig.setDateType(DateType.TIME_PACK);//时间类型对应策略
        globalConfig.setEntityName("%s");//实体命名方式
        globalConfig.setMapperName("%sMapper");//mapper 命名方式
        globalConfig.setXmlName("%sMapper");//Mapper xml 命名方式
        globalConfig.setServiceName("%sDao");//mp 命名方式
        globalConfig.setServiceImplName("%sDaoImpl");//mp impl 命名方式
        globalConfig.setControllerName("%sController");//controller 命名方式
        globalConfig.setIdType(IdType.ID_WORKER);//指定生成的主键的ID类型
        return globalConfig;
    }

    /**
     * 注入配置，通过该配置，可注入自定义参数等操作以实现个性化操作
     * @return
     */
    private static InjectionConfig createInjectionConfig(){
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        return injectionConfig;
    }

    /**
     *  数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
     * @return
     */
    private static StrategyConfig createStrategyConfig(String[] tables) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(false);// 全局大写命名
        strategy.setTablePrefix("t_");// 去除前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//        strategy.setSuperEntityClass("org.crown.framework.model.BaseModel"); //自定义实体父类
//        strategy.setSuperEntityColumns("id");// 自定义实体，公共字段
//        strategy.setSuperMapperClass("org.crown.framework.mapper.BaseMapper");  // 自定义 mapper 父类
//        strategy.setSuperControllerClass("org.crown.framework.controller.SuperController");  // 自定义 controller 父类
//        strategy.setSuperServiceImplClass("org.crown.framework.mp.impl.BaseServiceImpl"); // 自定义 mp 实现类父类
//        strategy.setSuperServiceClass("org.crown.framework.mp.BaseService");// 自定义 mp 接口父类
        strategy.setEntityTableFieldAnnotationEnable(true);
//        strategy.setVersionFieldName("status");
//        strategy.setLogicDeleteFieldName("enabled");
        strategy.setEntityColumnConstant(true); // 【实体】是否生成字段常量（默认 false）
        strategy.setEntityBuilderModel(false);  // 【实体】是否为构建者模型（默认 false）
        strategy.setEntityLombokModel(true); // 【实体】是否为lombok模型（默认 false）
        strategy.setEntityBooleanColumnRemoveIsPrefix(false); // Boolean类型字段是否移除is前缀处理
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tables); // 需要生成的表
        return strategy;
    }

    /**
     * 包名配置，通过该配置，指定生成代码的包路径
     * @return
     */
    private static PackageConfig createPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.fate.common");
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setService("dao");
        packageConfig.setServiceImpl("dao.impl");
        return packageConfig;
    }

    /**
     * 模板配置，可自定义代码生成的模板，实现个性化操作
     * @return
     */
    private static TemplateConfig createTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        return templateConfig;
    }

    /**
     * 获取根目录
     *
     * @return
     */
    private static String getRootPath() {
        String projectPath = System.getProperty("user.dir");
        return projectPath;
    }

    /**
     * 获取JAVA目录
     *
     * @return
     */
    protected static String getJavaPath() {
        String javaPath = getRootPath() + "/src/main/java";
        log.error(" Generator Java Path:【 " + javaPath + " 】");
        return javaPath;
    }

    /**
     * 获取Resource目录
     *
     * @return
     */
    protected static  String getResourcePath() {
        String resourcePath = getRootPath() + "/src/main/resources";
        log.error(" Generator Resource Path:【 " + resourcePath + " 】");
        return resourcePath;
    }

}
