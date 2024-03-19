package com.my.context;

import java.util.EventObject;

/**
 * 事件类,source即事件最初发生的地方
 */
public abstract class ApplicationEvent extends EventObject {

	public ApplicationEvent(Object source) {
		super(source);
	}
}
