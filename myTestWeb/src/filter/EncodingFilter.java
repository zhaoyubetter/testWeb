package filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter implements Filter {

	private final String encoding = "UTF-8";

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// POST请求中文问题
		request.setCharacterEncoding(encoding);
		// 响应输出编码和浏览器使用的编码
		response.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=" + encoding);
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			// 只对 get 方式请求有效
			// 创建 MyHttpServletRequest 对象，并赋值给 request对象，
			// 这样做是为了减少内部类MyHttpServletRequest的判断
			request = new MyHttpServletRequest(request);
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	/**
	 * 内部类： 包装HttpServletRequest对象，覆盖接收参数的方法，用于解决url中文参数的问题
	 * 
	 * @author zhaoyu
	 */
	private class MyHttpServletRequest extends HttpServletRequestWrapper {
		public MyHttpServletRequest(HttpServletRequest request) {
			super(request);
		}

		public String getParameter(String name) {
			String value = super.getParameter(name);
			if (value != null) {
				value = encodingValue(value);
			}
			return value;
		}

		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> map = super.getParameterMap();
			if (map != null) {
				for (Map.Entry<String, String[]> me : map.entrySet()) {
					String[] values = me.getValue();
					for (int i = 0; i < values.length; i++) {
						values[i] = encodingValue(values[i]); // 编码后，替换
					}
				}
			}
			return map;
		}

		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = encodingValue(values[i]); // 编码后，替换
				}
			}
			return values;
		}

		/**
		 * 返回编码后的字符串 @param value
		 */
		private String encodingValue(String value) {
			try {
				return new String(value.getBytes("ISO-8859-1"),
						super.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
