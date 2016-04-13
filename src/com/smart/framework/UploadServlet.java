package com.smart.framework;

import com.smart.framework.bean.Result;
import com.smart.framework.util.WebUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(name="upload", urlPatterns={"/upload.do"})
public class UploadServlet extends HttpServlet
{
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String pathName = request.getParameter("path");
    String filePath = WebUtil.getUploadFilePath(request, "app.www_pathupload/" + pathName);

    Part part = request.getPart("file");
    String fileName = WebUtil.getUploadFileName(request, part);

    part.write(filePath + "/" + fileName);

    Map data = new HashMap();
    data.put("file_name", fileName);
    data.put("file_type", part.getContentType());
    data.put("file_size", Long.valueOf(part.getSize()));
    WebUtil.writeJSON(response, new Result(true).data(data));
  }
}