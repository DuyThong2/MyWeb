package controllers.admin.account;

import dao.account.UserDAO;
import dto.account.Address;
import dto.account.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(name = "UpdateUserInfoController", urlPatterns = {"/admin/user/UpdateUserInfoController"})
public class UpdateUserInfoController extends HttpServlet {

    private static final String IMAGES_DIRECTORY = "images/customer/";
    private static final String SUCCESS_URL = "/AMainController?action=userDetail";
    private static final String ERROR_URL = "/AMainController?action=userUpdatePage";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String currentProjectPath = getServletContext().getRealPath("/");
        String uploadPath = currentProjectPath.concat(IMAGES_DIRECTORY);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        int id = 0;
        String name = null;
        String email = null;
        String phone = null;
        String city = null;
        String district = null;
        String ward = null;
        String street = null;
        String imgURL = null;
        UserDAO dao = new UserDAO();

        try {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(currentProjectPath.concat("web")));
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            User oldUser = null;
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    // Handle regular form fields
                    switch (fileItem.getFieldName()) {
                        case "id":
                            id = Integer.parseInt(fileItem.getString());
                            oldUser = dao.getUserById(id);
                            break;
                        case "name":
                            name = fileItem.getString();
                            break;
                        case "email":
                            email = fileItem.getString();
                            break;
                        case "phone":
                            phone = fileItem.getString();
                            break;
                        case "city":
                            city = fileItem.getString();
                            break;
                        case "district":
                            district = fileItem.getString();
                            break;
                        case "ward":
                            ward = fileItem.getString();
                            break;
                        case "street":
                            street = fileItem.getString();
                            break;
                        default:
                            break;
                    }
                } else {
                    // Handle file uploads
                    try{
                        if (fileItem.getFieldName().equals("imgURL")) {
                        String fileName = Paths.get(fileItem.getName()).getFileName().toString();
                        String filePath = uploadPath + fileName;
                        System.out.println(filePath);
                        File file = new File(filePath);
                        fileItem.write(file);
                        imgURL = IMAGES_DIRECTORY + fileName;
                    }
                    }catch(Exception e){
                        if (oldUser.getImgURL() == null){
                            imgURL = "images/customer/example.png";
                        }else{
                            imgURL = oldUser.getImgURL();
                        }
                    }
                    
                }
            }
            Address address = oldUser.getAddress();
            if (city!= null && ward!= null &&district!= null && street!= null){
                if (!city.isEmpty() && !ward.isEmpty() && !district.isEmpty() && !street.isEmpty()){
                    address = new Address(city, district, ward, street, id);
                }
            }

            
            // Save the user data to the database
            User user = new User(id, email, oldUser.getPw(), name,address, phone, imgURL, oldUser.getStatus());
            dao.updateUser(user);

            request.getRequestDispatcher(SUCCESS_URL + "&userId=" + user.getId()).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "there are error(s) check the input again");
            request.getRequestDispatcher(ERROR_URL).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/AMainController?action=userUpdatePage").forward(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
