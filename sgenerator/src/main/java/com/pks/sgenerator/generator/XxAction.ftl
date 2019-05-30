package ${packages}.action.${modules};

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ${packages}.common.ZwPageResult;
import ${packages}.entity.${modules}.${classes};
import ${packages}.service.${modules}.${classes}Service;
import com.zcj.util.UtilConvert;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.springmvc.action.BasicAction;

@Controller
@RequestMapping("/${classes?uncap_first}")
@Scope("prototype")
@Component("${classes?uncap_first}Action")
public class ${classes}Action extends BasicAction {

	@Resource
	private ${classes}Service ${classes?uncap_first}Service;

	@RequestMapping("/tolist")
	public String tolist(Model model) {

		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_list.ftl";
	}

	@RequestMapping("/list")
	public void list(<#list qbuilderList as q><#if q.listQuery><#if q.oper == "=" || q.oper == "like">${q.fieldType} search${q.fieldName?cap_first}, <#elseif q.oper == "time" || q.oper == "between">${q.fieldType} search${q.fieldName?cap_first}Begin, ${q.fieldType} search${q.fieldName?cap_first}End, </#if></#if></#list>PrintWriter out) {
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		<#list qbuilderList as q>
			<#if q.listQuery>
				<#if q.oper == "=">
					<#if q.fieldType == "String">
						if (UtilString.isNotBlank(search${q.fieldName?cap_first})) {
							qbuilder.put("${q.fieldName}", search${q.fieldName?cap_first});
						}
					<#else>
						if (search${q.fieldName?cap_first} != null) {
							qbuilder.put("${q.fieldName}", search${q.fieldName?cap_first});
						}
					</#if>
				<#elseif q.oper == "like">
					if (UtilString.isNotBlank(search${q.fieldName?cap_first})) {
						qbuilder.put("${q.fieldName}", "%" + search${q.fieldName?cap_first} + "%");
					}
				<#elseif q.oper == "time" || q.oper == "between">
					if (search${q.fieldName?cap_first}Begin != null) {
						qbuilder.put("${q.fieldName}Begin", search${q.fieldName?cap_first}Begin);
					}
					if (search${q.fieldName?cap_first}End != null) {
						qbuilder.put("${q.fieldName}End", search${q.fieldName?cap_first}End);
					}
				</#if>
			</#if>
		</#list>
		List<${classes}> list = new ArrayList<${classes}>();
		int total = 0;
		total = ${classes?uncap_first}Service.getTotalRows(qbuilder);
		if(total > 0 ){
			list = ${classes?uncap_first}Service.findByPage(null, qbuilder);
		}
		page.setRows(list);
		page.setTotal(total);
		out.write(ZwPageResult.converByServiceResult(ServiceResult.initSuccess(page)));
	}

	@RequestMapping("/toadd")
	public String toadd(Model model) {
		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_modify.ftl";
	}

	@RequestMapping("/tomodify")
	public String tomodify(Long id, Model model) {
		${classes} obj = null;
		if(id!=null){
			obj = ${classes?uncap_first}Service.findById(id);
		}
		model.addAttribute("obj", obj);
		return "/WEB-INF/ftl/admin/${modules?lower_case}/${classes?lower_case}_modify.ftl";
	}

	@RequestMapping("/delete")
	public void delete(HttpServletRequest request, String ids, PrintWriter out) {
		if (UtilString.isBlank(ids)) {
			out.write(ServiceResult.initErrorJson("请选择需要删除的记录！"));
			return;
		}
		if(!ids.matches("^[0-9]+(,[0-9]+)*$")){
			out.write(ServiceResult.initErrorParamJson());
			return ;
		}
		Long[] s = UtilConvert.string2Long(ids.split(","));
		if(s.length == 1){
			${classes?uncap_first}Service.delete(s[0]);
		}else{
			${classes?uncap_first}Service.deleteByIds(Arrays.asList(s));
		}
		out.write(ServiceResult.initSuccessJson(null));
	}
	
	@RequestMapping("/modify")
	public void modify(HttpServletRequest request, Long id, ${classes} obj, PrintWriter out) {
		if (obj == null) {
			out.write(ServiceResult.initErrorParamJson());
			return ;
		}
		if(id==null){
			obj.setId(UtilString.getLongUUID());
			${classes?uncap_first}Service.insert(obj);
		}else{
			${classes} update = ${classes?uncap_first}Service.findById(id);
			if(update==null){
				out.write(ServiceResult.initErrorJson("数据不存在！"));
				return ;
			}
			${classes?uncap_first}Service.update(obj);
		}
		out.write(ServiceResult.initSuccessJson("操作成功！"));
	}

	@RequestMapping("/findInfo")
	public void findInfo(HttpServletRequest request, Long id, PrintWriter out) {
		if(id==null){
			out.write(ServiceResult.initErrorParamJson());
			return ;
		}
		${classes} obj = ${classes?uncap_first}Service.findById(id);
		if(obj==null){
			out.write(ServiceResult.initErrorJson("信息不存在！"));
			return ;
		}
		out.write(ServiceResult.initSuccessJson(obj));
	}

}
