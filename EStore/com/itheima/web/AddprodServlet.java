package com.itheima.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.itheima.domain.Product;
import com.itheima.factory.BasicFactory;
import com.itheima.service.ProductService;
import com.itheima.util.IOUtils;
import com.itheima.util.PicUtils;

public class AddprodServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = BasicFactory.getFactory().getService(
				ProductService.class);
		// 1.�ϴ�ͼƬ
		try {
			String encode = this.getServletContext().getInitParameter("encode");
			Map<String, String> paramMap = new HashMap<String, String>();
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 100);
			factory.setRepository(new File(this.getServletContext()
					.getRealPath("/WEB-INF/upload")));
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding(encode);
			fileUpload.setFileSizeMax(1024 * 1024 * 1);
			fileUpload.setSizeMax(1024 * 1024 * 10);

			if (!fileUpload.isMultipartContent(request)) {
				throw new RuntimeException("��ʹ����ȷ�ı������ύ");
			}
			List<FileItem> list = fileUpload.parseRequest(request);
			for (FileItem fileItem : list) {
				if (fileItem.isFormField()) {
					// ��ͨ�ֶ�
					String name = fileItem.getFieldName();
					String value = fileItem.getString(encode);
					paramMap.put(name, value);
				} else {
					// �ļ��ϴ���
					String realname = fileItem.getName();
					String uuidname = UUID.randomUUID().toString() + "_"
							+ realname;
					String hash = Integer.toHexString(uuidname.hashCode());
					String upload = this.getServletContext().getRealPath(
							"/WEB-INF/upload");
					String imgurl = "/WEB-INF/upload";
					for (char c : hash.toCharArray()) {
						upload += "/" + c;
						imgurl += "/" + c;
					}
					imgurl += "/" + uuidname;
					paramMap.put("imgurl", imgurl);

					File uploadFile = new File(upload);
					uploadFile.mkdirs();

					InputStream is = fileItem.getInputStream();
					OutputStream os = new FileOutputStream(new File(upload,
							uuidname));

					IOUtils.In2Out(is, os);
					IOUtils.close(is, os);

					fileItem.delete();
					
					//��������ͼ
					PicUtils picu = new PicUtils(this.getServletContext().getRealPath(imgurl));
					picu.resizeByHeight(140);
				}
			}
			// 2.����service���ṩ�ķ����������ݿ��������Ʒ
			Product prod = new Product();
			BeanUtils.populate(prod, paramMap);
			service.addProd(prod);
			// 3.��ʾ�ɹ����ص���ҳ
			response.getWriter().write("��ϲ�������Ʒ�ɹ���3��󷵻���ҳ..");
			response.setHeader("Refresh", "3;url=/index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
