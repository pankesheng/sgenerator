-- 自动生成的文件，请勿修改（如需修改表结构，请新建SQL文件编写SQL语句修改）

<#list tables as t>
DROP TABLE IF EXISTS `${t.name}`;
CREATE TABLE `${t.name}` (
  `id` bigint(20) NOT NULL,
  <#list t.columns as c>
  `${c.name}` ${c.type}<#if (c.length)?? && (c.length gt 0)>(${c.length})</#if> <#if (c.nullable)?? && !(c.nullable)>NOT NULL<#else>DEFAULT NULL</#if>,
  </#list>
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

</#list>