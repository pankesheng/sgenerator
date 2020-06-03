
<#list tables as t>
GO
CREATE TABLE [${databasename}].[dbo].[${t.name}](
  [id] [bigint] NOT NULL PRIMARY KEY,
  <#list t.columns as c>
  [${c.name}] [${c.type}]<#if (c.length)?? && (c.length gt 0)>(${c.length})</#if> <#if (c.nullable)?? && !(c.nullable)>NOT </#if>NULL,
  </#list>
  [ctime] [datetime] NULL,
  [utime] [datetime] NULL
)

GO
<#list t.columns as c>
execute sp_addextendedproperty 'MS_Description','${c.comment}','user','dbo','table','${t.name}','column','${c.name}';  
</#list>
execute sp_addextendedproperty 'MS_Description','创建时间','user','dbo','table','${t.name}','column','ctime';  
execute sp_addextendedproperty 'MS_Description','修改时间','user','dbo','table','${t.name}','column','utime';  

</#list>