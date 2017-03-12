package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadServlet() {
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 保存上传文件的真实路径
		String storeFileDir = getServletContext().getRealPath("/uploadFiles");
		// 1.创建 DiskFileItemFactory 工厂，使用默认的缓存大小与临时文件存放位置
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2.判断表单格式是否 multipart/form-data
		//if (ServletFileUpload.isMultipartContent(req)) {
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			try {
				// 3.解析request对象，获取 FileItem 集合
				List<FileItem> list = fileUpload.parseRequest(req);
				for (FileItem fileItem : list) {
					if (fileItem.isFormField()) {
						System.out.println(fileItem.getFieldName() + "="
								+ fileItem.getString());
					} else {
						InputStream ips = fileItem.getInputStream();
						if (ips != null && ips.available() > 0) {
							// 获取上传文件的文件名：文件名有可能是（C:\\a\\b\\c.txt || c.txt）
							String filename = fileItem.getName();
							filename = filename.substring(filename
									.lastIndexOf("/") + 1); // c.txt
							// 保存文件
							FileUtils.copyInputStreamToFile(ips, new File(
									storeFileDir + "/" + filename));
						}
						ips.close();
						fileItem.delete();
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}

		//} else {
		//	throw new RuntimeException("表单的enctype不是  multipart/form-data 类型");
		//}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
