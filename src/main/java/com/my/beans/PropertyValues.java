package com.my.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性集
 */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<>();

	/**
	 * 属性列表中增加新的属性，重名属性会自动覆盖属性值
	 * @param pv
	 */
	public void addPropertyValue(PropertyValue pv) {
		//判断属性的name是否和现有的属性相同
		for (int i = 0; i < this.propertyValueList.size(); i++) {
			PropertyValue currentPv = this.propertyValueList.get(i);
			if (currentPv.getName().equals(pv.getName())) {
				//覆盖原有的属性值
				this.propertyValueList.set(i, pv);
				return;
			}
		}
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
