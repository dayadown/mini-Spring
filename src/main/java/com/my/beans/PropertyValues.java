package com.my.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性集
 */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<>();

	public void addPropertyValue(PropertyValue pv) {
		propertyValueList.add(pv);
	}

	/**
	 * 返回所有属性信息
	 * @return
	 */
	public PropertyValue[] getPropertyValues() {
		return this.propertyValueList.toArray(new PropertyValue[0]);
	}

	/**
	 * 查找属性名对应的属性信息
	 * @param propertyName
	 * @return
	 */
	public PropertyValue getPropertyValue(String propertyName) {
		for (int i = 0; i < this.propertyValueList.size(); i++) {
			PropertyValue pv = this.propertyValueList.get(i);
			if (pv.getName().equals(propertyName)) {
				return pv;
			}
		}
		return null;
	}
}
