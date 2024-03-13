package com.my.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * url资源，实现资源接口，即可以获取资源的输入流
 */
public class UrlResource implements Resource {

	private final URL url;

	public UrlResource(URL url) {
		this.url = url;
	}

	/**
	 * 获取url返回的数据流
	 * @return
	 * @throws IOException
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		//连接url
		URLConnection con = this.url.openConnection();
		try {
			return con.getInputStream();
		} catch (IOException ex) {
			throw ex;
		}
	}
}
