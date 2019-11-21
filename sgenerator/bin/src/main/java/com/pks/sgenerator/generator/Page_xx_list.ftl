<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="${r"$"}{contextPath}/admin/stylesheets/common.css?v=${r"$"}{sversion}" />
    <link rel="stylesheet" href="${r"$"}{contextPath}/admin/stylesheets/table.css?v=${r"$"}{sversion}" />
	<script type="text/javascript" src="${r"$"}{contextPath}/ext/jquery/jquery-1.8.1.min.js"></script>
    <script type="text/javascript" src="${r"$"}{contextPath}/ext/layer/layer.min.js"></script>
    <script type="text/javascript" src="${r"$"}{contextPath}/ext/laydate/laydate.js"></script>
    <script type="text/javascript" src="${r"$"}{contextPath}/admin/ext/jquery/selectbox.js"></script>
    <script type="text/javascript" src="${r"$"}{contextPath}/admin/ext/zw/grid.js?v=${r"$"}{sversion}"></script>
    <script type="text/javascript" src="${r"$"}{contextPath}/admin/javascripts/tool.js?v=${r"$"}{sversion}"></script>
	<script type="text/javascript" src="${r"$"}{contextPath}/admin/javascripts/zcommon.js?v=${r"$"}{sversion}" basepath="${r"$"}{contextPath}" baseinit="ajaxCheckLogin"></script>
</head>
<body>
	<div class="place">
        <span class="label-span">位置：</span>
        <span>${(obj.pname)!}<#if (obj.pname)?? && (obj.pname != "")> - </#if>${(obj.name)!}</span>
    </div>
    <div class="body-warp">
        <div class="panel filter-block">
            <form class="form-inline">
            	<#list obj.columnList as c>
	            	<#if c.search>
	            		<#if c.type == "select">
	            <div class="form-group">
                    <select id="search${c.fieldName?cap_first}" class="form-select">
                        <option value="">--请选择${(c.name)!}--</option>
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
		    			<option value="${newKey}">${newValue}</option>
                        </#list>
                    </select>
                </div>
                		<#elseif c.type == "text" || c.type == "textarea" || c.type == "ueditor">
                <div class="form-group">
                    ${c.name}：<input id="search${c.fieldName?cap_first}" class="form-control" type="text" />
                </div>
                		<#elseif c.type == "date">
                <div class="form-group">
                    ${c.name}：<input id="search${c.fieldName?cap_first}Begin" class="form-control" type="text" /> ~ <input id="search${c.fieldName?cap_first}End" class="form-control" type="text" />
                </div>
	            		</#if>
	            	</#if>
            	</#list>
                <div class="form-group">
                    <a href="javascript:void(0);" class="btn" id="search-btn"><i class="iconfont">&#xe61b;</i>搜索</a>
                </div>
            </form>
        </div>
        <div class="panel table-tool-bar">
            <a class="btn" href="###" onclick="add()"><i class="add-btn iconfont">&#xe619;</i>新增</a>
            <a class="btn" href="###" onclick="removeItems()"><i class="remove-btn iconfont">&#xe608;</i>删除</a>
            <#if obj.export>
            <a class="btn" href="###" onclick="zexport()"><i class="iconfont">&#xe612;</i>导出</a>
            </#if>
        </div>
        <table class="table" id="table"></table>
    </div>
<script>
    var grid = {};
    ${r"$"}(function() {
        grid = ${r"$"}('#table').grid({
            store: {
				url: '${r"$"}{contextPath}/${obj.className?uncap_first}/list.ajax'
            },
            tool: {
                pagingBar: true
            },
            columns: [
            <${r"#"}-- 
            	align: 'left'
             -->
            <#list obj.columnList as c><#if c.grid>{
                title: '${(c.name)!}',
                dataIndex: '${(c.fieldName)!}'<#if c.type == "select">,
                renderer: function(cellData, rowData){
            		<#list (c.keyValue)?keys as key>                        
                    <#if key_index gt 0>else </#if>if(cellData == ${key}){
						return ${(c.keyValue)[key]};
					}
                    </#list>
                }<#elseif c.type == "img">,
	            renderer: function(cell, row) {
                	var result = "";
                	if (cell && cell != "") {
	                	var imgsArray = cell.split(",");
	                	for (i in imgsArray) {
	                		result = result + "<a href='${r"$"}{contextPath}"+imgsArray[i]+"' target='_blank'><img height='30' width='60' src='${r"$"}{contextPath}"+imgsArray[i]+"'/></a>";
	                		result = result + "&nbsp;&nbsp;&nbsp;&nbsp;";
	                	}
                	}
                	return result;
                }</#if>
            },</#if></#list>{
                title: '操作',
                dataIndex: 'id',
                renderer: function(cellData, rowData){
                	var result = '';
                	result += '<div style="overflow:hidden;">';
                		result += '<a class="btn btn-primary" href="###" onclick="editItem(\''+cellData+'\');">编辑</a>\n';
                	result += '</div>';
                	return result;
                }
            }]
        });
        
        <#list obj.columnList as c>
    		<#if c.search>
    			<#if c.type == "date">
        laydate({
	        elem: '#search${c.fieldName?cap_first}Begin',
	        event: 'focus',
			format: 'YYYY-MM-DD hh:mm:ss',
			istime: true
	    });
	    laydate({
	        elem: '#search${c.fieldName?cap_first}End',
	        event: 'focus',
			format: 'YYYY-MM-DD hh:mm:ss',
			istime: true
	    });
	    		</#if>
	    	</#if>
	    </#list>
    });
        
    //添加
	function add(){
		<#if obj.dialog>
		z_openIframe('新增', 700, 400, '${r"$"}{contextPath}/${obj.className?uncap_first}/toadd.do');
		<#else>		
		window.location.href = '${r"$"}{contextPath}/${obj.className?uncap_first}/toadd.do';
		</#if>
	}
	
	//编辑
	function editItem(id){
		<#if obj.dialog>
		z_openIframe('编辑', 700, 400, '${r"$"}{contextPath}/${obj.className?uncap_first}/tomodify/' + id + '.do');
		<#else>
		window.location.href = '${r"$"}{contextPath}/${obj.className?uncap_first}/tomodify/' + id + '.do';
		</#if>
	}

	//删除
	function removeItems(){
		var data = grid.getSelectedData('id');
		z_delete2(data, '${r"$"}{contextPath}/${obj.className?uncap_first}/delete.ajax');
	}
	
	<#if obj.export>
	// 导出
	function zexport() {
		z_export("${r"$"}{contextPath}/${obj.className?uncap_first}/export.ajax");
	}
	</#if>
	
	${r"$"}('#search-btn').click(function(){
    	var params = {};
    	<#list obj.columnList as c>
        	<#if c.search>
        		<#if c.type == "select">
    	params['search${c.fieldName?cap_first}'] = $('#search${c.fieldName?cap_first}').val();
				<#elseif c.type == "text" || c.type == "textarea" || c.type == "ueditor">
    	params['search${c.fieldName?cap_first}'] = $('#search${c.fieldName?cap_first}').val();
				<#elseif c.type == "date">
    	params['search${c.fieldName?cap_first}Begin'] = $('#search${c.fieldName?cap_first}Begin').val();
    	params['search${c.fieldName?cap_first}End'] = $('#search${c.fieldName?cap_first}End').val();
				</#if>
        	</#if>
        </#list>
    	grid.load(params);
    });
    <${r"#"}-- 
    ${r"$"}('#searchKey').keydown(function(e){
		if(e.keyCode==13){
		   ${r"$"}('#search-btn').click();
		   return false;
		}
	});
    -->
	<#list obj.columnList as c>
    	<#if c.search>
    		<#if c.type == "select">
	${r"$"}('#search${c.fieldName?cap_first}').change(function(){
		${r"$"}('#search-btn').click();
	});
			</#if>
    	</#if>
    </#list>
</script>
</body>
</html>