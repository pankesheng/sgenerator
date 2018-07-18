
<#list tables as t>
CREATE TABLE [${databasename}].[dbo].[${t.name}](
  [id] [bigint] NOT NULL PRIMARY KEY,
  <#list t.columns as c>
  [${c.name}] [${c.type}]<#if (c.length)?? && (c.length gt 0)>(${c.length})</#if> <#if (c.nullable)?? && !(c.nullable)>NOT </#if>NULL,
  </#list>
  [ctime] [datetime] NULL,
  [utime] [datetime] NULL
)


</#list>