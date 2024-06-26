package com.servlet.verifycode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.verifycode.widget.CodeView;

public class GenerateCode extends HttpServlet {

	public GenerateCode() {
		super();
	}

	public void init() throws ServletException {
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String char_type = request.getParameter("char_type");
		String disturber_type = request.getParameter("disturber_type");
		CodeView codeView = new CodeView(); // 创建一个验证码视图
		codeView.setSize(200, 50); // 设置验证码视图的宽高
		codeView.setCodeType(char_type, disturber_type); // 设置验证码视图的字符类型和干扰类型
		// 创建验证码图片的文件对象
		File imageFile = new File(getFilePath()+codeView.getCodeNumber()+".jpg");
		System.out.println("file_path="+imageFile.getAbsolutePath());
		try {
			// 把验证码视图展示的图像保存到图片文件中
			ImageIO.write(codeView.getCodeImage(), "jpg", imageFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		returnImage(response, imageFile.getAbsolutePath());
	}

	private void returnImage(HttpServletResponse response, String imagePath) {
		System.out.println("returnImage=" + imagePath);
		try (OutputStream os = response.getOutputStream()) {
			if (imagePath.toLowerCase().endsWith(".gif")) {
				response.setContentType("image/gif");
			} else if (imagePath.toLowerCase().endsWith(".png")) {
				response.setContentType("image/png");
			} else {
				response.setContentType("image/jpeg");
			}

			try (FileInputStream fis = new FileInputStream(imagePath)) {
				BufferedImage image = ImageIO.read(fis);
				ImageIO.setUseCache(false);
				ImageIO.write(image, "jpg", os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("response.getContentType=" + response.getContentType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public String getFilePath() {
		String rootPath = getClass().getResource("/").getFile().toString();
		String filePath = rootPath.substring(0, rootPath.lastIndexOf("WEB-INF"));
		filePath = filePath.replace("file:/", "");
		System.out.println("filePath="+filePath);
		return filePath;
    }
    
}
