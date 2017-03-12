package servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class DownServlet
 */
@WebServlet("/DownServlet")
public class DownServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DownServlet() {
		super();
	}
	
	/**
	 * 获取存储目录
	 * @param storeFileDir 父目录
	 * @param filename	存储的文件名
	 * @return	 文件的存储目录*/
	private String makeDirs(String storeFileDir, String filename) {
		int hashCode = filename.hashCode();
		int dir1 = hashCode & 0xf;		
		int dir2 = (hashCode & 0xf0) >> 4;	
		String newDir = dir1 + "/" + dir2;
		File file = new File(storeFileDir, newDir);
		if(!file.exists()) {
			file.mkdirs();
		}
		return newDir;
	}


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String storeFileDir = getServletContext().getRealPath("/uploadFiles");
		String filename = "HeadFirst.pdf";
		
		//String filename = "11.jpg";
		
		// String filePath = makeDirs(storeFileDir, filename);
		File downFile = new File(storeFileDir + "/", filename);
		if (downFile.exists()) {
			// 得到原文件名
			//String oldName = filename.substring(filename.indexOf("_") + 1);
			// 使用common io 工具类，将文件读入 内存数组中
			byte[] bytes = FileUtils.readFileToByteArray(downFile);
			// 通知客户端已下载的方式
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader(
					"Content-Disposition",
					"attachment;filename="
							+ URLEncoder.encode(filename, response.getCharacterEncoding()));
			// 使中文文件名不出现乱码
			response.setContentLength((int)downFile.length());
			response.getOutputStream().write(bytes);
			response.getOutputStream().write("下载成功".getBytes(response.getCharacterEncoding()));
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
