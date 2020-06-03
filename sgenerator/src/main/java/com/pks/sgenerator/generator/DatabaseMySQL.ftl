
<#list tables as t>
DROP TABLE IF EXISTS `${t.name}`;
CREATE TABLE `${t.name}` (
  `id` bigint(20) NOT NULL comment 'ID编号',
  <#list t.columns as c>
  `${c.name}` ${c.type}<#if (c.length)?? && (c.length gt 0)>(${c.length})</#if> <#if (c.nullable)?? && !(c.nullable)>NOT NULL<#else>DEFAULT NULL</#if> comment '${c.comment}',
  </#list>
  `ctime` datetime DEFAULT NULL comment '创建时间',
  `utime` datetime DEFAULT NULL comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

<#list t.columns as c>
    <#if c.index>
alter table `${t.name}` add index index_${c.name} (`${t.name}`);
    </#if>
</#list>

</#list>