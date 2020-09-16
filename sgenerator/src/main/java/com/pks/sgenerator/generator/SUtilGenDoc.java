package com.pks.sgenerator.generator;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
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
import com.pks.sgenerator.database.SApiReturn;
import com.pks.sgenerator.database.TableColumnType;
import com.pks.sgenerator.java.JavaCode;
import com.pks.sgenerator.java.JavaCodeBuilder;
import com.pks.sgenerator.page.PageBean;
import com.pks.sgenerator.page.PageBuilder;


public class SUtilGenDoc {
	
	private static String resourceFilePath ; 
	private static List<String> field_base_type_list = new ArrayList<String>();
	static{
		field_base_type_list.add(String.class.getName());
		field_base_type_list.add(Integer.class.getName());
		field_base_type_list.add(Long.class.getName());
		field_base_type_list.add(Date.class.getName());
		field_base_type_list.add("String");
		field_base_type_list.add("Integer");
		field_base_type_list.add("Long");
		field_base_type_list.add("Date");
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
	}
	
	@SuppressWarnings("resource")
	public static void init(){
		SUtilsSource.genSourceFile("gen_application.xml", resourceFilePath);
		ApplicationContext context = new FileSystemXmlApplicationContext(resourceFilePath + "gen_application.xml");
		if (context != null) {
			freemarkerConfig = (MyFreeMarkerConfigurer) context.getBean("freemarkerConfig");
		}
	}
	
	public static void GenDocFile(String basePath,String savepath,String actionpackage) throws IOException{
//		initSourceFile(basePath);
//		init();
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Set<Class<?>> classesSet = UtilClass.getClasses(actionpackage);
		Class<?>[] test = new Class<?>[classesSet.size()];
		Class<?>[] array = (Class<?>[]) classesSet.toArray(test);
		String url_first = "";
		String url_second = "";
		String postType = "POST/GET";
		File genFile = new File(savepath + "文档.txt");
		if (!genFile.exists()) {
			(new File(genFile.getParent())).mkdirs();
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(savepath + "文档.txt"));
		try {
			System.out.println("[TOC]");
			out.write("[TOC]"); // \r\n即为换行
			out.write("\r\n");
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
					if (method.isAnnotationPresent(RequestMapping.class) && method.isAnnotationPresent(SApiMethod.class)) {
						RequestMapping mapping = method.getAnnotation(RequestMapping.class);
						url_second = mapping.value()[0];
						if (!url_second.startsWith("/")) {
							url_second = "/" + url_second;
						}
						if (mapping.method().length > 0) {
							StringBuffer sb = new StringBuffer();
							for (RequestMethod rm : mapping.method()) {
								if (StringUtils.isNotBlank(sb)) {
									sb.append("/");
								}
								sb.append(rm);
							}
							postType = sb.toString();
						}
						if (method.isAnnotationPresent(SApiMethod.class)) {
							SApiMethod apiMethod = method.getAnnotation(SApiMethod.class);
							System.out.println("- - - ");
							out.write("- - - "); // \r\n即为换行
							out.write("\r\n");
							System.out.println("[[回到目录]](#TOC)");
							out.write("[[回到目录]](#TOC)");
							out.write("\r\n");
							System.out.println("####接口名称：" + apiMethod.methodName());
							out.write("####接口名称：" + apiMethod.methodName());
							out.write("\r\n");
							System.out.println("**接口路径**" );
							out.write("**接口路径**" );
							out.write("\r\n");
							System.out.println("```");
							out.write("```");
							out.write("\r\n");
							System.out.println(url_first + url_second);
							out.write(url_first + url_second);
							out.write("\r\n");
							System.out.println("```");
							out.write("```");
							out.write("\r\n");
							System.out.println("**请求方式**");
							out.write("**请求方式**");
							out.write("\r\n");
							System.out.println("```");
							out.write("```");
							out.write("\r\n");
							System.out.println(postType);
							out.write(postType);
							out.write("\r\n");
							System.out.println("```");
							out.write("```");
							out.write("\r\n");
							
							Map<String, List<Field>> obj_fields_map = new HashMap<String, List<Field>>();
							SApiParam[] params = apiMethod.params();
							if (params.length > 0 ) {
								System.out.println("**请求参数列表**");
								out.write("**请求参数列表**");
								out.write("\r\n");
								System.out.println("");
								out.write("");
								out.write("\r\n");
								System.out.println("|参数|必选|类型|说明|");
								out.write("|参数|必选|类型|说明|");
								out.write("\r\n");
								System.out.println("| - | - | - | - |");
								out.write("| - | - | - | - |");
								out.write("\r\n");
							}
							for (SApiParam param : params) {
								String f_type = param.f_type();
								if (field_base_type_list.contains(f_type)) {
									System.out.println("|" + param.f_name() + "|" + param.required() + "|" + param.f_type() + "|" + param.v_name() + (StringUtils.isNotBlank(param.defaultValue())?("<br />默认值：" + param.defaultValue()):"") + "|");
									out.write("|" + param.f_name() + "|" + param.required() + "|" + param.f_type() + "|" + param.v_name() + (StringUtils.isNotBlank(param.defaultValue())?("<br />默认值：" + param.defaultValue()):"") + "|");
									out.write("\r\n");
								} else {
									Class clazz = null;
									try {
										clazz = loader.loadClass(param.f_type());
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
	//								System.out.println(clazz.getName());
									List<Field> fields = CoderUtil.allField(clazz, true);
									obj_fields_map.put(param.f_name(), fields);
									System.out.println("|" + param.f_name() + "|" + param.required() + "|" + "对象" + "|" + param.v_name() + "|");
									out.write("|" + param.f_name() + "|" + param.required() + "|" + "对象" + "|" + param.v_name() + "|");
									out.write("\r\n");
								}
							}
							if (!obj_fields_map.isEmpty()) {
								System.out.println("**附：对象属性**");
								out.write("**附：对象属性**");
								out.write("\r\n");
							}
							for (Iterator<Map.Entry<String, List<Field>>> it = obj_fields_map.entrySet().iterator(); it.hasNext();) {
					            Map.Entry<String, List<Field>> entry = (Map.Entry<String, List<Field>>) it.next();
					            System.out.println("");
					            out.write("");
								out.write("\r\n");
					            System.out.println("**"+entry.getKey()+"属性列表**");
					            out.write("**"+entry.getKey()+"属性列表**");
								out.write("\r\n");
					            System.out.println("");
					            out.write("");
								out.write("\r\n");
					            System.out.println("|参数|是否允许为空|类型|说明|");
					            out.write("|参数|是否允许为空|类型|说明|");
								out.write("\r\n");
					            System.out.println("| - | - | - | - |");
					            out.write("| - | - | - | - |");
								out.write("\r\n");
					            List<Field> fields = entry.getValue();
								for (Field field : fields) {
									if (field.isAnnotationPresent(TableColumnType.class)) {
										TableColumnType tct = field.getAnnotation(TableColumnType.class);
										String field_type = field.getGenericType().toString();
										if (field_type.startsWith("class ")) {
											field_type = field_type.substring("class ".length());
										}
										System.out.println("|" + field.getName() + "|" + tct.nullable() + "|" + field_type + "|" + tct.comment() + (StringUtils.isNotBlank(tct.defaultValue())?("<br />默认值：" + tct.defaultValue()):"") + "|");
										out.write("|" + field.getName() + "|" + tct.nullable() + "|" + field_type + "|" + tct.comment() + (StringUtils.isNotBlank(tct.defaultValue())?("<br />默认值：" + tct.defaultValue()):"") + "|");
										out.write("\r\n");
									}
								}
							}
						}
	
						
						if (method.isAnnotationPresent(SApiReturn.class)) {
							
							System.out.println("");
							out.write("");
							out.write("\r\n");
							System.out.println("**返回格式**");
							out.write("**返回格式**");
							out.write("\r\n");
							System.out.println("");
							out.write("");
							out.write("\r\n");
							
							SApiReturn apiReturn = method.getAnnotation(SApiReturn.class);
							System.out.println("|参数|类型|说明|");
							out.write("|参数|类型|说明|");
							out.write("\r\n");
							System.out.println("| - | - | - |");
							out.write("| - | - | - |");
							out.write("\r\n");
							SApiParam[] params = apiReturn.params();
							Map<String, List<Field>> obj_fields_map = new HashMap<String, List<Field>>();
							for (SApiParam param : params) {
								String f_type = param.f_type();
								if (field_base_type_list.contains(f_type)) {
									System.out.println("|" + param.f_name() + "|" + param.f_type() + "|" + param.v_name() + (StringUtils.isNotBlank(param.defaultValue())?("<br />默认值：" + param.defaultValue()):"") + "|");
									out.write("|" + param.f_name() + "|" + param.f_type() + "|" + param.v_name() + (StringUtils.isNotBlank(param.defaultValue())?("<br />默认值：" + param.defaultValue()):"") + "|");
									out.write("\r\n");
								} else {
									Class clazz = null;
									try {
										clazz = loader.loadClass(param.f_type());
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
									List<Field> fields = CoderUtil.allField(clazz, true);
									obj_fields_map.put(param.f_name(), fields);
									
								}
							}
							if (!obj_fields_map.isEmpty()) {
								System.out.println("**附：对象属性**");
								out.write("**附：对象属性**");
								out.write("\r\n");
							}
							for (Iterator<Map.Entry<String, List<Field>>> it = obj_fields_map.entrySet().iterator(); it.hasNext();) {
					            Map.Entry<String, List<Field>> entry = (Map.Entry<String, List<Field>>) it.next();
					            System.out.println("");
					            out.write("");
								out.write("\r\n");
					            System.out.println("**"+entry.getKey()+"属性列表**");
					            out.write("**"+entry.getKey()+"属性列表**");
								out.write("\r\n");
								System.out.println("");
								out.write("");
								out.write("\r\n");
								System.out.println("|参数|类型|说明|");
								out.write("|参数|类型|说明|");
								out.write("\r\n");
								System.out.println("| - | - | - |");
								out.write("| - | - | - |");
								out.write("\r\n");
								List<Field> fields = entry.getValue();
								for (Field field : fields) {
									if (field.isAnnotationPresent(TableColumnType.class)) {
										TableColumnType tct = field.getAnnotation(TableColumnType.class);
										String field_type = field.getGenericType().toString();
										if (field_type.startsWith("class ")) {
											field_type = field_type.substring("class ".length());
										}
										System.out.println("|" + field.getName() + "|" + field_type + "|" + tct.comment() + (StringUtils.isNotBlank(tct.defaultValue())?("<br />默认值：" + tct.defaultValue()):"") + "|");
										out.write("|" + field.getName() + "|" + field_type + "|" + tct.comment() + (StringUtils.isNotBlank(tct.defaultValue())?("<br />默认值：" + tct.defaultValue()):"") + "|");
										out.write("\r\n");
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	
	
	public static FreeMarkerConfigurer freemarkerConfig = null;


}
