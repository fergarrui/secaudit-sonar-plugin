import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@MultipartConfig
class TestServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    // servlet file upload
    String desc = req.getParameter("desc");
    processPart(req.getPart("file"));
    Part filePart = req.getPart("file"); // Noncompliant

    List<Part> parts = req.getParts().stream().collect(Collectors.toList());
    for (Part part : parts) { // Noncompliant
      System.out.println(part.getName());
    }

    // apache commons-fileupload
    List<FileItem> items = null;
    try {
      items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
    } catch (FileUploadException e) { // nop}
      for (FileItem fileItem : items) { // Noncompliant
        System.out.println(fileItem.getName());
      }
    }
  }

  void processPart(Part file) { // Noncompliant
    System.out.println(file.getName());
  }
}

@Controller
class SpringController {

  @RequestMapping("/upload")
  public void handleUpload(@RequestParam("file") MultipartFile file) { // Noncompliant
    System.out.println(file.getName());
  }
}

@Path("/upload")
class JerseyUpload {
  public Response uploadFile1(@FormDataParam("file")InputStream uploadedFile, @FormDataParam("file")FormDataContentDisposition fileInfo) { // Noncompliant
    System.out.println(fileInfo.getName());
    return Response.status(200).build();
  }
  public Response uploadFile2(@FormDataParam("file")InputStream uploadedFile) { // Noncompliant
    return Response.status(200).build();
  }
  public Response uploadFile3(@FormDataParam("file")FormDataContentDisposition fileInfo) { // Noncompliant
    System.out.println(fileInfo.getName());
    return Response.status(200).build();
  }
  public Response uploadFile4() { // Compliant
    return Response.status(200).build();
  }
  public Response uploadFile5(@FormDataParam("file")String fileInfo) { // Compliant
    return Response.status(200).build();
  }
  public Response uploadFile5(FormDataContentDisposition fileInfo) { // Compliant
    return Response.status(200).build();
  }
}
