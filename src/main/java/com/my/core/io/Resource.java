package com.my.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源接口，须实现资源的访问
 */
public interface Resource {

	InputStream getInputStream() throws IOException;

}
