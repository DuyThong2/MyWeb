/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.account;

import dao.account.UserDAO;
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
        String address = null;
        String imgURL = null;
        UserDAO dao = new UserDAO();
        try {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(currentProjectPath.concat("web")));
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = fileUpload.parseRequest(request);

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
                        case "address":
                            address = fileItem.getString();
                            break;
                        default:
                            break;
                    }
                } else {
                    // Handle file uploads
                    if (fileItem.getFieldName().equals("imgURL")) {
                        String fileName = Paths.get(fileItem.getName()).getFileName().toString();
                        String filePath = uploadPath + fileName;
                        
                        File file = new File(filePath);
                        fileItem.write(file);
                        imgURL = IMAGES_DIRECTORY + fileName;
                    }
                }
            }

            User oldUser = dao.getUserById(id);
            // Save the user data to the database
            User user = new User(id, email, oldUser.getPw(), name, address, phone, imgURL, oldUser.getStatus());
            dao.updateUser(user);

            request.getRequestDispatcher(SUCCESS_URL + "&userId=" + user.getId()).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(ERROR_URL).forward(request, response);
        }
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
