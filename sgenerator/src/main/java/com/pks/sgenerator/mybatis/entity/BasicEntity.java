package com.pks.sgenerator.mybatis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BasicEntity implements Serializable {

	private static final long serialVersionUID = 51124911023734190L;

	private Long id;

	/** 创建时间 */
	private Date ctime;

	/** 更新时间 */
	private Date utime;

	/** 补全额外属性的值 */
	public void showFormat() {

	}

	public static void showFormat(List<? extends BasicEntity> list) {
		if (list != null && list.size() > 0) {
			for (BasicEntity g : list) {
				g.showFormat();
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

}
