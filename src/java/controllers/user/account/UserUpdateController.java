/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.account;

import dao.account.UserDAO;
import dto.account.Address;
import dto.account.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Admin
 */
@WebServlet(name = "UserUpdateController", urlPatterns = {"/user/account/UserUpdateController"})
public class UserUpdateController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String IMAGES_DIRECTORY = "images/customer/";
    private static final String SUCCESS_URL = "/MainController?action=userDetail";
    private static final String ERROR_URL = "/MainController?action=error";
    private static final String LOGIN_URL ="/MainController?action=login";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LoginedUser");
        if (user == null){
            request.getRequestDispatcher(LOGIN_URL).forward(request, response);
        }else{
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

            User oldUser = dao.getUserById(user.getId());
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    // Handle regular form fields
                    switch (fileItem.getFieldName()) {
                        case "id":
                            id = Integer.parseInt(fileItem.getString());
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
            user = new User(id, email, oldUser.getPw(), name, address, phone, imgURL, oldUser.getStatus());
            dao.updateUser(user);
            session.setAttribute("LoginedUser", user);

            request.getRequestDispatcher(SUCCESS_URL + "&userId=" + user.getId()).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "there is error, please check the inputs");
            request.getRequestDispatcher(ERROR_URL).forward(request, response);
        }
        }
        
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/MainController?action=userUpdatePage").forward(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
