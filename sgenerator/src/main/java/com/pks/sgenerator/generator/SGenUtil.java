package com.pks.sgenerator.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	private static String resourceFilePath ; 
	
	public static List<String> resourceFileList = new ArrayList<String>();
	static{
		resourceFileList.add("DatabaseMySQL.ftl");
		resourceFileList.add("DatabaseOracle.ftl");
		resourceFileList.add("DatabaseSqlServer.ftl");
		resourceFileList.add("XxAction2.ftl");
		resourceFileList.add("XxMapperJava.ftl");
		resourceFileList.add("XxMapperXmlMySQL.ftl");
		resourceFileList.add("XxMapperXmlOracle.ftl");
		resourceFileList.add("XxMapperXmlSqlServer.ftl");
		resourceFileList.add("XxService.ftl");
		resourceFileList.add("XxServiceImpl.ftl");
	}
 	public static void initSourceFile(String basePath){
 		if (!basePath.endsWith("/")) {
 			basePath = basePath + "/";
 		}
 		resourceFilePath = basePath + "resourceFile/" ;
 		File file = new File(resourceFilePath);
 		if (!file.exists()) {
 			file.mkdirs();
 		}
		SUtilsSource.genSourceFile("gen_application.xml", resourceFilePath);
		for (String resourceName: resourceFileList) {
			SUtilsSource.genSourceFile(resourceName, resourceFilePath);
		}
	}
	
	@SuppressWarnings("resource")
	public static void init(){
		ApplicationContext context = new FileSystemXmlApplicationContext(resourceFilePath + "gen_application.xml");
		if (context != null) {
			freemarkerConfig = (MyFreeMarkerConfigurer) context.getBean("freemarkerConfig");
		}
//		ApplicationContext context = new FileSystemXmlApplicationContext(SGenUtil.class.getResource("").getPath()+"gen_application.xml");
//		if (context != null) {
//			freemarkerConfig = (MyFreeMarkerConfigurer) context.getBean("freemarkerConfig");
//		}
	}
	
	public static void GenFile(String basePath,String savepath,String databasetype,String entitypackage){
		initSourceFile(basePath);
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray,null);
		allTable(savepath,databasetype,carray,null);
		System.out.println("文件生成结束！");
	}
	
	public static void GenMySqlFile(String basePath,String savepath,String entitypackage){
		String databasetype = Database.TYPE_MYSQL;
		initSourceFile(basePath);
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray,null);
		allTable(savepath,databasetype,carray,null);
		System.out.println("文件生成结束！");
	}
	
	public static void GenMySqlFile(String basePath,String savepath,String entitypackage,String prefix){
		String databasetype = Database.TYPE_MYSQL;
		initSourceFile(basePath);
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray,prefix);
		allTable(savepath,databasetype,carray,prefix);
		System.out.println("文件生成结束！");
	}
	
	public static FreeMarkerConfigurer freemarkerConfig = null;

	private static void allTable(String savepath,String databasetype,Class<?>[] carray,String prefix) {
		Database database = DatabaseBuilder.initDatabase(carray, databasetype,prefix);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("tables", database.getTables());
		root.put("databasename", database.getName());
		if (Database.TYPE_MYSQL.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root,resourceFilePath+"DatabaseMySQL.ftl", savepath + "init.sql");
		} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"DatabaseSqlServer.ftl", savepath + "init.sql");
		} else if (Database.TYPE_ORACLE.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"DatabaseOracle.ftl", savepath + "init.sql");
		}
	}

	private static void allJavaAndFtl(String savepath,String databasetype, Class<?>[] carray,String prefix) {
		for (Class<?> c : carray) {
			allJava(savepath,databasetype,c,prefix);
//			allFtl(savepath,c);
		}
	}

	private static void allFtl(String savepath,Class<?> className) {
		PageBean obj = PageBuilder.initPage(className);
		if (obj != null) {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("obj", obj);
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile( root, resourceFilePath+"Page_xx_list.ftl",
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase() + "_list.ftl");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"Page_xx_modify2.ftl",	
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase() + "_modify.ftl");
		}
	}

	private static void allJava(String savepath, String databasetype, Class<?> className,String prefix) {

		JavaCode code = JavaCodeBuilder.initJavaCode(className,prefix);

		Map<String, Object> root = new HashMap<String, Object>();
		if (code != null) {
			root.put("packages", code.getPackageName());
			root.put("modules", code.getModuleName());
			root.put("classes", code.getClassName());
			root.put("tables", code.getTableName());
			root.put("fields", code.getFieldList());
			root.put("qbuilderList", code.getQbuilderList());
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxMapperJava.ftl",
					savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.java");
			if (Database.TYPE_MYSQL.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxMapperXmlMySQL.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxMapperXmlSqlServer.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_ORACLE.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxMapperXmlOracle.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			}
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxService.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "Service.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxServiceImpl.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "ServiceImpl.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, resourceFilePath+"XxAction2.ftl",
					savepath + code.getPackageName() + "/action/" + code.getModuleName() + "/" + code.getClassName() + "Action.java");
		} else {
			System.out.println("生成 " + className.getName() + " 失败");
		}

	}

}
