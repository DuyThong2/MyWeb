/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.meal;

import dao.product.IngredientDAO;
import dao.product.IngredientPacketDAO;
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
@WebServlet(name = "UpdateMealInfoController", urlPatterns = {"/admin/meal/UpdateMealInfoController"})
public class UpdateMealInfoController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final String IMAGES_DIRECTORY = "images/meal/";
    private static final String PACKET_UPDATE_URL = "/AMainController?action=PacketUpdate";
    private static final String PACKET_INSERT_URL ="/AMainController?action=PacketInsert";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
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
                            break;
                        case "name":
                            name = fileItem.getString();
                            break;
                         case "description":
                            description = fileItem.getString();
                            break;  
                        case "price":
                            price = Double.parseDouble(fileItem.getString());
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
                    // Handle file uploads
                    if (fileItem.getFieldName().equals("imgURL")) {
                        String fileName = Paths.get(fileItem.getName()).getFileName().toString();
                        String filePath = uploadPath + fileName;
                        System.out.println(filePath);
//                        Files.deleteIfExists(Paths.get(filePath));
                        File file = new File(filePath);
                        
                        fileItem.write(file);
                        imgURL = IMAGES_DIRECTORY + fileName;
                    }
                }
            }

            // Save the ingredient data to the database
            Meal meal = new Meal(id,name, description,price,false, 0 ,content, category, imgURL, "active");
            dao.updateMeal(meal);
            if (request.getParameter("status").matches("done")){
                request.setAttribute("completeMessage", "successfully added meal");
                request.getRequestDispatcher("/AMainController?action=success").forward(request, response);
            }else if (request.getParameter("status").matches("continue")){
                IngredientPacketDAO packetDAO = new IngredientPacketDAO();
                if (packetDAO.getIngredientPacketFromId(name)!= null){
                    response.sendRedirect(request.getContextPath() + PACKET_UPDATE_URL+"&packetId=P"+meal.getId().substring(1));
                }else{
                    response.sendRedirect(request.getContextPath() + PACKET_INSERT_URL+"&packetId=P"+meal.getId().substring(1));
                }
                        
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/AMainController?action=error").forward(request, response);
        }
        
    }
}
