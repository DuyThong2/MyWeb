/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.meal;

import dao.product.IngredientDAO;
import dao.product.MealDAO;
import dto.product.Ingredient;
import dto.product.Meal;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
@WebServlet(name = "MealInsert1", urlPatterns = {"/admin/meal/MealInsertController"})
public class MealInsertController extends HttpServlet {

    private static final String IMAGES_DIRECTORY = "images/meal/";
    private static final String PACKET_URL = "/AMainController?action=PacketInsertPage";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String currentProjectPath = getServletContext().getRealPath("/");

        String uploadPath = currentProjectPath.concat(IMAGES_DIRECTORY);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String id = null;
        String name = null;
        double price = 0.0;
        String description = null;
        String content = null;
        String category = null;
        String imgURL = null;
        MealDAO dao = new MealDAO();
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
                            id = fileItem.getString();
                            String errorMessage = dao.checkValidId(id);
                            if (errorMessage != null) {
                                throw new Exception(errorMessage);
                            }
                            break;
                        case "name":
                            name = fileItem.getString();
                            break;
                        case "description":
                            description = fileItem.getString();
                            break;
                        case "price":
                            try {
                                price = Double.parseDouble(fileItem.getString());

                            } catch (NumberFormatException e) {
                                price = 10;
                            }
                            break;
                        case "content":
                            content = fileItem.getString();
                            break;
                        case "mealCategory":
                            category = fileItem.getString();
                            break;
                        default:
                            break;
                    }
                } else {
                    try {
                        if (fileItem.getFieldName().equals("imgURL")) {
                            String fileName = Paths.get(fileItem.getName()).getFileName().toString();
                            String filePath = uploadPath + fileName;

//                        Files.deleteIfExists(Paths.get(filePath));
                            File file = new File(filePath);

                            fileItem.write(file);
                            imgURL = IMAGES_DIRECTORY + fileName;

                        }
                    } catch (Exception e) {
                        imgURL = "images/meal/example.png";
                    }
                    // Handle file uploads

                }
            }

            // Save the ingredient data to the database
            Meal meal = new Meal(id, name, description, price, false, 0, 0, content, category, imgURL, "active");
            dao.InsertToTable(meal);
            if (request.getParameter("status").matches("done")) {
                request.setAttribute("completeMessage", "successfully added meal");
                request.getRequestDispatcher("/AMainController?action=MealDetail&mealId="+id).forward(request, response);
            } else if (request.getParameter("status").matches("continue")) {
                HttpSession session = request.getSession();
                session.setAttribute("mealInfo", meal);
                request.getRequestDispatcher(PACKET_URL).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/AMainController?action=MealInsertPage").forward(request, response);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/AMainController?action=MealInsertPage").forward(request, response);
    }

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
