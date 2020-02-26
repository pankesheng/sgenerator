<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packages}.mapper<#if modules!="">.${modules}</#if>.${classes}Mapper">
	
	<!-- <cache /> -->
	
	<!-- 大于等于(&gt;=);小于等于(&lt;=); -->
	<sql id="qbuilder">
		<where>
			<if test="qbuilder.id != null">${tables}.`id` = ${r"#"}{qbuilder.id}</if>
			<if test="qbuilder.idneq!=null"> AND ${tables}.`id` != ${r"#"}{qbuilder.idneq}</if>
			<if test="qbuilder.ids!=null"> AND ${tables}.`id` in
				<foreach item="item" index="index" collection="qbuilder.ids" open="(" separator="," close=")" >
					${r"#"}{item}
				</foreach>
			</if>
			<#list qbuilderList as q>
			<#if q.oper == "=" || q.oper == "like">
			<if test="qbuilder.${q.fieldName} != null">AND ${tables}.`${q.fieldName}` ${q.oper} ${r"#"}{qbuilder.${q.fieldName}}</if>
			<#elseif q.oper == "in">
			<if test="qbuilder.${q.fieldName}s != null">AND ${tables}.`${q.fieldName}` in
				<foreach item="${q.fieldName}" index="index" collection="qbuilder.${q.fieldName}s" open="(" separator="," close=")">
					${r"#"}{${q.fieldName}}
				</foreach>
			</if>
			<#elseif q.oper == "time" || q.oper == "between">
			<if test="qbuilder.${q.fieldName}Begin != null">AND ${tables}.`${q.fieldName}` &gt;= ${r"#"}{qbuilder.${q.fieldName}Begin}</if>
			<if test="qbuilder.${q.fieldName}End != null">AND ${tables}.`${q.fieldName}` &lt; ${r"#"}{qbuilder.${q.fieldName}End}</if>
			</#if>
			</#list>
		</where>
	</sql>
	
	<select id="find" resultType="${classes}">
		SELECT *
		FROM ${tables}
		<if test="qbuilder != null">
			<include refid="qbuilder"/>
		</if>
		<choose>
			<when test="orderby != null">
				ORDER BY ${tables}.${r"${orderby}"}
			</when>
			<otherwise>
				ORDER BY ${tables}.id
			</otherwise>
		</choose>
		<choose>
			<when test="start != null and size != null">
				LIMIT ${r"#"}{start},${r"#"}{size}
			</when> 
			<when test="start == null and size != null">
				LIMIT ${r"#"}{size}
			</when> 
		</choose>
	</select>
	
	<select id="getTotalRows" resultType="int">
		SELECT COUNT(${tables}.id) 
		FROM ${tables}
		<if test="qbuilder != null">
			<include refid="qbuilder"/>
		</if>
	</select>
	
	<select id="findById" resultType="${classes}">
		SELECT ${tables}.* FROM ${tables} WHERE id = ${r"#"}{id}
	</select>
	
	<insert id="insert">
		INSERT INTO ${tables}
			(`id`,<#list fields as f>`${f.name}`,</#list>`ctime`,`utime`)
		VALUES
			(${r"#"}{object.id},<#list fields as f>${r"#"}{object.${f.name}},</#list>now(),now())
	</insert>
	
	<insert id="inserts">
		INSERT INTO ${tables}
			(`id`,<#list fields as f>`${f.name}`,</#list>`ctime`,`utime`)
		VALUES
			<foreach collection="objects" item="object" index="index" separator="," >
				(${r"#"}{object.id},<#list fields as f>${r"#"}{object.${f.name}},</#list>now(),now())
			</foreach>
	</insert>
	
	<update id="update">
		UPDATE ${tables}
		SET
			<#list fields as f>
			`${f.name}` = ${r"#"}{object.${f.name}},
			</#list>
			`utime` = now()
		WHERE id = ${r"#"}{object.id}
	</update>
	
	<delete id="delete">
		DELETE FROM ${tables} WHERE id = ${r"#"}{id}
	</delete>
	
	<delete id="deleteByIds">
		DELETE FROM ${tables} WHERE id in 
		<foreach item="id" index="index" collection="ids" open="(" separator="," close=")">
			${r"#"}{id}
		</foreach>
	</delete>
	
	<delete id="cleanTable">
		DELETE FROM ${tables}
	</delete>
	
	<select id="selectExists" resultType="Integer" >
        select 1 from ${tables} 
        <if test="qbuilder != null">
            <include refid="qbuilder"/>
        </if>
         limit 1
    </select>

</mapper>
