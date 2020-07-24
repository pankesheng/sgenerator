package com.pks.sgenerator.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pks.sgenerator.coder.CoderUtil;
import com.pks.sgenerator.database.Database;
import com.pks.sgenerator.database.DatabaseBuilder;
import com.pks.sgenerator.database.SApiMethod;
import com.pks.sgenerator.database.SApiParam;
import com.pks.sgenerator.database.TableColumnType;
import com.pks.sgenerator.java.JavaCode;
import com.pks.sgenerator.java.JavaCodeBuilder;
import com.pks.sgenerator.page.PageBean;
import com.pks.sgenerator.page.PageBuilder;


public class SUtilGenDoc {

	@SuppressWarnings("resource")
	public static void init(){
		ApplicationContext context = new FileSystemXmlApplicationContext(SUtilGenDoc.class.getResource("").getPath()+"gen_application.xml");
		if (context != null) {
			freemarkerConfig = (MyFreeMarkerConfigurer) context.getBean("freemarkerConfig");
		}
	}
	
	public static void GenDocFile(String savepath,String actionpackage){
		init();
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Set<Class<?>> classesSet = UtilClass.getClasses(actionpackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] array = (Class<?>[]) classesSet.toArray(test);
		String url_first = "";
		String url_second = "";
		String postType = "POST/GET";
		for (Class<?> c : array) {
			url_first = "";
			if (c.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping mapping = c.getAnnotation(RequestMapping.class);
				url_first = mapping.value()[0];
				if(!url_first.startsWith("/")){
					url_first = "/" + url_first;
				}
			}
			Method[] methods = c.getDeclaredMethods();
			for (Method method : methods) {
				url_second = "";
				postType = "POST/GET";
				if(method.isAnnotationPresent(RequestMapping.class)){
					RequestMapping mapping = method.getAnnotation(RequestMapping.class);
					url_second = mapping.value()[0];
					if(!url_second.startsWith("/")){
						url_second = "/" + url_second;
					}
					if(mapping.method().length > 0){
						StringBuffer sb = new StringBuffer();
						for (RequestMethod rm : mapping.method()) {
							if(StringUtils.isNotBlank(sb)){
								sb.append("/");
							}
							sb.append(rm);
						}
						postType = sb.toString();
					}
					if(method.isAnnotationPresent(SApiMethod.class)){
						SApiMethod apiMethod = method.getAnnotation(SApiMethod.class);
						System.out.println("接口名称：" + apiMethod.methodName());
						System.out.println("接口路径：" + url_first + url_second);
						System.out.println("请求方式：" + postType);
						System.out.println("请求参数列表：");
						SApiParam[] params = apiMethod.params();
						for (SApiParam param : params) {
							String f_type = param.f_type();
							Class clazz = null;
							try {
								clazz = loader.loadClass(param.f_type());
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							if(f_type.equals(String.class.getName()) 
									|| f_type.equals(Integer.class.getName()) 
									|| f_type.equals(Long.class.getName())
									|| f_type.equals(Date.class.getName())
							){
								System.out.println(param.f_name() + "|" + param.f_type() + "|" + param.v_name());
							}else{
								System.out.println(clazz.getName());
								List<Field> fields = CoderUtil.allField(clazz, true);
								for (Field field : fields) {
									if(field.isAnnotationPresent(TableColumnType.class)){
										TableColumnType tct = field.getAnnotation(TableColumnType.class);
										String field_type = field.getGenericType().toString();
										if(field_type.startsWith("class ")){
											field_type = field_type.substring("class ".length());
										}
										System.out.println(field.getName() + "|" + field_type + "|" + tct.comment());
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void GenFile(String savepath,String databasetype,String entitypackage){
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray,null);
		allTable(savepath,databasetype,carray,null);
		System.out.println("文件生成结束！");
	}
	
	public static void GenMySqlFile(String savepath,String entitypackage){
		String databasetype = Database.TYPE_MYSQL;
		init();
		Set<Class<?>> classesSet = UtilClass.getClasses(entitypackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] carray = (Class<?>[]) classesSet.toArray(test);
		allJavaAndFtl(savepath,databasetype,carray,null);
		allTable(savepath,databasetype,carray,null);
		System.out.println("文件生成结束！");
	}
	
	public static void GenMySqlFile(String savepath,String entitypackage,String prefix){
		String databasetype = Database.TYPE_MYSQL;
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
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root,SUtilGenDoc.class.getResource("").getPath()+"DatabaseMySQL.ftl", savepath + "init.sql");
		} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"DatabaseSqlServer.ftl", savepath + "init.sql");
		} else if (Database.TYPE_ORACLE.equals(databasetype)) {
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"DatabaseOracle.ftl", savepath + "init.sql");
		}
	}

	private static void allJavaAndFtl(String savepath,String databasetype, Class<?>[] carray,String prefix) {
		for (Class<?> c : carray) {
			allJava(savepath,databasetype,c,prefix);
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
					SUtilGenDoc.class.getResource("").getPath()+"Page_xx_list.ftl",
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase()
							+ "_list.ftl");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(
					root,
					SUtilGenDoc.class.getResource("").getPath()+"Page_xx_modify2.ftl",
					savepath + "/WEB-INF/ftl/admin/" + obj.getModuleName().toLowerCase() + "/" + obj.getClassName().toLowerCase()
							+ "_modify.ftl");
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
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxMapperJava.ftl",
					savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.java");
			if (Database.TYPE_MYSQL.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxMapperXmlMySQL.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_SQLSERVER.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxMapperXmlSqlServer.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			} else if (Database.TYPE_ORACLE.equals(databasetype)) {
				FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxMapperXmlOracle.ftl",
						savepath + code.getPackageName() + "/mapper/" + code.getModuleName() + "/" + code.getClassName() + "Mapper.xml");
			}
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxService.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "Service.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxServiceImpl.ftl",
					savepath + code.getPackageName() + "/service/" + code.getModuleName() + "/" + code.getClassName() + "ServiceImpl.java");
			FreemarkerUtil.getInstance(freemarkerConfig).htmlFile(root, SUtilGenDoc.class.getResource("").getPath()+"XxAction2.ftl",
					savepath + code.getPackageName() + "/action/" + code.getModuleName() + "/" + code.getClassName() + "Action.java");
		} else {
			System.out.println("生成 " + className.getName() + " 失败");
		}

	}

}
