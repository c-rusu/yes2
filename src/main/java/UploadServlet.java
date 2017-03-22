// Import required java libraries

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UploadServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;

    public void init( ){
        // Get the file location where it would be stored.
        filePath = getServletContext().getInitParameter("file-upload");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, java.io.IOException {

        Pattern filePattern = Pattern.compile("([^\\s]+(\\.(?i)(|txt|))$)");
        boolean isTXT = false;


        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );
        if( !isMultipart ){
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>No file uploaded</p>");
            out.println("</body>");
            out.println("</html>");
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.

        factory.setRepository(new File("c:\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );



        try{
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");
            while ( i.hasNext () )
            {
                FileItem fi = (FileItem)i.next();
                Matcher m = filePattern.matcher(fi.getName());
            if(m.matches()) {
                isTXT = true;
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();


//                        UUID uuid = UUID.randomUUID();
//                        String randomUUIDString = uuid.toString();

                        file = new File(filePath + "\\" +"data.txt");


                    fi.write(file);
                    out.println("Uploaded Filename: " + fileName + "<br>");

                }
                }
                else{
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet upload</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<p>Wrong File uploaded</p>");
                    out.println("</body>");
                    out.println("</html>");
                }

            }
            out.println("</body>");
            out.println("</html>");

            try {
                new ToXML().changeToXML();
                file.delete();
            }
            catch (Exception e){
                System.out.println(e);
            }

        }catch(Exception ex) {
            System.out.println(ex);
        }
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

        throw new ServletException("GET method used with " +
                getClass( ).getName( )+": POST method required.");
    }
}