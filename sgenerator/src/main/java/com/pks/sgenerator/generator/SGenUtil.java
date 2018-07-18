package com.pks.sgenerator.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pks.sgenerator.database.Database;
import com.pks.sgenerator.database.DatabaseBuilder;
import com.pks.sgenerator.java.JavaCode;
import com.pks.sgenerator.java.JavaCodeBuilder;
import com.pks.sgenerator.page.PageBean;
import com.pks.sgenerator.page.PageBuilder;


public class SGenUtil {

	@SuppressWarnings("resource")
	public static void init(){
		ApplicationContext context = new FileSystemXmlApplicationContext(SGenUtil.class.getResource("").getPath()+"gen_application.xml");
		if (context != null) {
			freemarkerConfig = (MyFreeMarkerConfigurer) context.getBean("freemarkerConfig");
		}
	}
	
	public static void GenFile(String savepath,String databasetype,String entitypackage){
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray);
		allTable(savepath,databasetype,carray);
	}
	
	public static FreeMarkerConfigurer freemarkerConfig = null;

	private static void allTable(String savepath,String databasetype,Class<?>[] carray) {
		Database database = DatabaseBuilder.initDatabase(carray, databasetype);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("tables", database.getTables());
		root.put("databasename", database.getName());
		if (Database.TYPE_MYSQL.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root,SGenUtil.class.getResource("").getPath()+"DatabaseMySQL.ftl", savepath + "init.sql");
		} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"DatabaseSqlServer.ftl", savepath + "init.sql");
		} else if (Database.TYPE_ORACLE.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"DatabaseOracle.ftl", savepath + "init.sql");
		}
	}

	private static void allJavaAndFtl(String savepath,String databasetype, Class<?>[] carray) {
		for (Class<?> c : carray) {
			allJava(savepath,databasetype,c);
			allFtl(savepath,c);
		}
	}

	private static void allFtl(String savepath,Class<?> className) {
		PageBean obj = PageBuilder.initPage(className);
		if (obj != null) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("obj", obj);
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(
					root,
					SGenUtil.class.getResource("").getPath()+"Page_xx_list.ftl",
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase()
							+ "_list.ftl");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(
					root,
					SGenUtil.class.getResource("").getPath()+"Page_xx_modify.ftl",
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase()
							+ "_modify.ftl");
		}
	}

	private static void allJava(String savepath, String databasetype, Class<?> className) {

		JavaCode code = JavaCodeBuilder.initJavaCode(className);

		Map<String, Object> root = new HashMap<String, Object>();
		if (code != null) {
			root.put("packages", code.getPackageName());
			root.put("modules", code.getModuleName());
			root.put("classes", code.getClassName());
			root.put("tables", code.getTableName());
			root.put("fields", code.getFieldList());
			root.put("qbuilderList", code.getQbuilderList());
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxMapperJava.ftl",
					savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.java");
			if (Database.TYPE_MYSQL.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxMapperXmlMySQL.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxMapperXmlSqlServer.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_ORACLE.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxMapperXmlOracle.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			}
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxService.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "Service.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxServiceImpl.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "ServiceImpl.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SGenUtil.class.getResource("").getPath()+"XxAction.ftl",
					savepath + code.getPackageName() + "/action/" + code.getModuleName() + "/" + code.getClassName() + "Action.java");
		} else {
			System.out.println("生成 " + className.getName() + " 失败");
		}

	}

}
