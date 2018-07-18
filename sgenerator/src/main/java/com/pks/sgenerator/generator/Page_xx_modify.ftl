<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="${r"$"}{contextPath}/admin/stylesheets/common.css?v=${r"$"}{sversion}" media="screen" />
	<link rel="stylesheet" type="text/css" href="${r"$"}{contextPath}/admin/stylesheets/table.css?v=${r"$"}{sversion}" media="screen" />
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/jquery/jquery-1.8.1.min.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/jquery_form/jquery.form.min.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/layer/layer.min.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/laydate/laydate.js"></script>
	
	<#if obj.upload>
	<#-- 图片功能 -->
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/json/json2.js"></script>
	<link rel="stylesheet" type="text/css" href="${r"$"}{contextPath}/admin/ext/uploadify/uploadify.css" media="screen" />
	<script type="text/javascript" src="${r"$"}{contextPath}/admin/ext/uploadify/jquery.uploadify.min.js?t=<${r"@z.z_now /"}>"></script>
	<link rel="stylesheet" href="${r"$"}{contextPath}/ext/jquery_zcj/jquery.zimgslider.css?v=${r"$"}{sversion}" />
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/jquery_zcj/jquery.zimgslider.js?v=${r"$"}{sversion}"></script>
	</#if>
	
	<script type="text/javascript" src="${r"$"}{contextPath}/admin/ext/jquery/selectbox.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/admin/ext/zw/check.js?v=${r"$"}{sversion}"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/admin/javascripts/zcommon.js?v=${r"$"}{sversion}" basepath="${r"$"}{contextPath}" baseinit="ajaxCheckLogin"></script>

	<#if obj.ueditor>
	<#-- 编辑器功能 -->
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/ueditor/lang/zh-cn/zh-cn.js"></script>
	</#if>
</head>
<body>
<#if obj.dialog>
		<div class="form-wrap">
<#else>
<div class="place">
    <span class="label-span">位置：</span>
    <ul id="place-list" class="place-ul">
        <li>${(obj.pname)!}<#if (obj.pname)?? && (obj.pname != "")> - </#if>${(obj.name)!}</li>
    </ul>
</div>
<div class="body-warp">
    <div class="panel">
        <div class="panel-title">
            <i class="form-icon"></i>
            <span class="title-text">${(obj.name)!}</span>
        </div>
        <div class="panel-body">
</#if>
		    <form id="saveform" method="post">
		    	<input type="hidden" name="id" value="${r"$"}{(obj.id)!}"/>
		        <table class="form-table">
		        	<#list obj.columnList as c>
			        	<#if c.modify>
			        		<#if c.type = "text">
			        <tr>
		                <td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
		                <td>
		                	<input class="form-control" name="${c.fieldName}" type="text" <#if (c.check)?? || c.must>data-check="<#if c.must>must<#if (c.check)??>|</#if></#if>${(c.check)!}"</#if> <#if (c.maxlength)??>maxlength="${c.maxlength}"</#if> value="${r"$"}{(obj.${c.fieldName})!<#if (c.defaultValue)??>'${c.defaultValue}'</#if>}"/>
		                </td>
		            </tr>
			        		<#elseif c.type = "textarea">
			        <tr class="valign-top">
						<td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
						<td>
							<textarea class="form-textarea" name="${c.fieldName}" <#if (c.check)?? || c.must>data-check="<#if c.must>must<#if (c.check)??>|</#if></#if>${(c.check)!}"</#if> >${r"$"}{(obj.${c.fieldName})!<#if (c.defaultValue)??>'${c.defaultValue}'</#if>}</textarea>
						</td>
					</tr>
			        		<#elseif c.type = "select">
			        <tr>
		                <td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
		                <td>
		                    <select class="form-select" name="${c.fieldName}" <#if (c.check)?? || c.must>data-check="<#if c.must>must<#if (c.check)??>|</#if></#if>${(c.check)!}"</#if> >
		                    	<option value="">请选择${c.name}</option>
		                    	<#list (c.keyValue)?keys as key>
		                    		<#assign newKey = key />	            
			                        <#if (key?length > 0) && key?substring(0,1)=="'" && key?ends_with("'")>
			                        	<#assign newKey = (key?substring(1,key?length-1)) />      
			                        </#if>
			                        <#assign oldValue = ((c.keyValue)[key]) />
			                        <#assign newValue = oldValue />
			                        <#if (oldValue?length > 0) && oldValue?substring(0,1)=="'" && oldValue?ends_with("'")>
			                        	<#assign newValue = (oldValue?substring(1,oldValue?length-1)) />      
			                        </#if>
		                        <option value="${newKey}" <${r"#if"} (obj.${c.fieldName})?? && obj.${c.fieldName}=="${newKey}">selected<${r"/#if"}>>${newValue}</option>
		                        </#list>
		                    </select>
		                </td>
		            </tr>
		            		<#elseif c.type = "date">
			        <tr>
		                <td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
		                <td>
		                    <input class="form-control date" id="${c.fieldName}" name="${c.fieldName}" type="text" <#if (c.check)?? || c.must>data-check="<#if c.must>must<#if (c.check)??>|</#if></#if>${(c.check)!}"</#if> value="${r"$"}{((obj.${c.fieldName})?string("yyyy-MM-dd HH:mm:ss"))!<#if (c.defaultValue)??>'${c.defaultValue}'</#if>}"/>
		                </td>
		            </tr>
			        		<#elseif c.type = "img">
					<tr>
		                <td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
		                <td>
		                	<input type="hidden" name="${c.fieldName}">
							<div id="addOrModify_imgs_${c.fieldName}"></div>
							<input id="upload_${c.fieldName}" type="file"/>
		                </td>
		            </tr>
		            		<#elseif c.type = "file">
		            <tr>
		                <td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
		                <td>
		                	<input class="form-control" id="${c.fieldName}" name="${c.fieldName}" type="text" <#if (c.check)?? || c.must>data-check="<#if c.must>must<#if (c.check)??>|</#if></#if>${(c.check)!}"</#if> value="${r"$"}{(obj.${c.fieldName})!}" readonly="readonly">
							<input id="upload_${c.fieldName}" type="file"/>
		                </td>
		            </tr>
			        		<#elseif c.type = "ueditor">
					<tr class="valign-top">
						<td><label class="form-label">${c.name}<#if c.must><b class="red">*</b></#if></label></td>
						<td>
							<script id="editor_${c.fieldName}" name="${c.fieldName}" type="text/plain" style="width:800px;height:300px;"><${r"#noescape"}>${r"$"}{(obj.${c.fieldName})!<#if (c.defaultValue)??>'${c.defaultValue}'</#if>}<${r"/#noescape"}></script>
						</td>
					</tr>		
			        		</#if>
			        	</#if>
		        	</#list>
		            <tr>
		                <td><label class="form-label">&nbsp;</label></td>
		                <td>
		                    <input class="btn btn-success btn-large" type="button" onclick="_save()" value="提交">
		                    <input class="btn btn-danger btn-large" type="reset" value="重置">
		                    <#if !(obj.dialog)>
		                    <input class="btn btn-danger btn-large return-btn" type="button" onclick="_back()" value="返回">
		                    </#if>
		                </td>
		            </tr>
		        </table>
		    </form>
        </div>
<#if !(obj.dialog)>
    </div>
</div>
</#if>
<script type="text/javascript">
var prefix = "${r"$"}{contextPath}";
<#list obj.columnList as c>
	<#if c.modify>
		<#if c.type = "img">
function _initImgPath_${c.fieldName}() {
	var logo_${c.fieldName} = $("#addOrModify_imgs_${c.fieldName}").zImgslider_getImgUrls(prefix);
	if (logo_${c.fieldName} && logo_${c.fieldName} != '') {
		$("input[name='${c.fieldName}']").val(logo_${c.fieldName});
	}
}
		<#elseif c.type = "ueditor">
var ue_${c.fieldName} = UE.getEditor('editor_${c.fieldName}', {
	toolbars : [ [ 'fullscreen', 'source', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'strikethrough',
			'removeformat', '|', 'forecolor', 'insertorderedlist', 'insertunorderedlist', '|', 'fontfamily',
			'fontsize', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'link', 'unlink', '|',
			'simpleupload', 'insertimage', 'insertvideo', 'attachment', '|', 'wordimage',
			'inserttable', 'preview' ] ],
	"imageUrlPrefix" : prefix,
	"scrawlUrlPrefix" : prefix,
	"snapscreenUrlPrefix" : prefix,
	"catcherUrlPrefix" : prefix,
	"videoUrlPrefix" : prefix,
	"fileUrlPrefix" : prefix,
	elementPathEnabled : false,
	saveInterval : 3600000,
	wordCount : false
});
		</#if>
	</#if>
</#list>

function _save() {

	<#list obj.columnList as c>
		<#if c.modify && c.type = "img">
	_initImgPath_${c.fieldName}();
		</#if>
	</#list>
	
	$("#saveform").ajaxSubmit({
		url : '${r"$"}{contextPath}/${obj.className?uncap_first}/modify.ajax',
		dataType : 'json',
		beforeSubmit : function(formData, jqForm, options) {
			if(!$("#saveform").check())return false;

			<#list obj.columnList as c>
				<#if c.modify && c.must>
					<#if c.type = "img">
			if($("input[name='${c.fieldName}']").val()=="") {
				alert("请上传${c.name}");
				return false;
			}
					<#elseif c.type = "">
			if(ue_${c.fieldName}.getContent()=="") {
				alert("请填写${c.name}");
				return false;
			}
					</#if>
				</#if>
			</#list>

			if(!window.confirm("确定提交?"))return false;
		    return true;
		},
		success : function(data, statusText, xhr) {
			if(data.s){
				<#if obj.dialog>
				window.parent.grid.refresh();
				// 解决IE6下关闭弹窗时焦点丢失的问题
				$("#searchKey", window.parent.document).focus();
				window.parent.layer.closeAll();
				<#else>
				z_alert_success(data.d||"操作成功！");
				_back();
				</#if>
			}else{
				z_alert_error(data.d);
			}
		    return true;
		}
	});
}
<#if !(obj.dialog)>
function _back() {
	window.location.href = '${r"$"}{contextPath}/${obj.className?uncap_first}/tolist.do';
}
</#if>
$(document).ready(function(){
	<#list obj.columnList as c>
		<#if c.modify>
			<#if c.type = "img">
			
	// 初始化上传控件，8张及以内可以正常显示
	z_initImgUpload("upload_${c.fieldName}", "addOrModify_imgs_${c.fieldName}", prefix, "upload-${r"$"}{.now?string("yyyyMM")}", 8);
	
	// 初始化图片显示
	$("#addOrModify_imgs_${c.fieldName}").zImgslider_init(prefix,'${r"$"}{(obj.${c.fieldName})!<#if (c.defaultValue)??>'${c.defaultValue}'</#if>}',true);
			<#elseif c.type = "file">
			
	// 初始化文件上传
	z_initFlieUpload("upload_${c.fieldName}", prefix, "upload-${r"$"}{.now?string("yyyyMM")}", "${c.fieldName}");		
			<#elseif c.type = "date">
	
	laydate({
        elem: '#${c.fieldName}',
        event: 'focus',
		format: 'YYYY-MM-DD hh:mm:ss',
		istime: true
    });
			</#if>
		</#if>
	</#list>
	
	<#if obj.dialog>
	// 解决IE6下第二次打开弹窗时焦点丢失的问题
	$('#form :input:not(:hidden):not(:button):first').focus();
	</#if>
});
</script>
</body>
</html>