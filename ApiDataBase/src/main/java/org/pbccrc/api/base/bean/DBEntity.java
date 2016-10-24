package org.pbccrc.api.base.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DBEntity implements Serializable{
	
	private static final long serialVersionUID = 8086219534241954878L;

	/** 表名 */
	private String tableName;
	
	/** 返回字段 */
	private Map<String, String> returnFidlds;
	
	/** 字段 */
	private List<String> fields;
	
	/** 值 */
	private List<String> values;
	
	/** sql语句 */
	private String sql;
	
	/** 查询字段 */
	private String[] selectItems;
	
	public String[] getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(String[] selectItems) {
		this.selectItems = selectItems;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public Map<String, String> getReturnFidlds() {
		return returnFidlds;
	}

	public void setReturnFidlds(Map<String, String> returnFidlds) {
		this.returnFidlds = returnFidlds;
	}
	
}
