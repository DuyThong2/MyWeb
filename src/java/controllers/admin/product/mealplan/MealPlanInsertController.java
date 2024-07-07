/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.mealplan;

import dao.plan.MealPlanDAO;
import dto.plan.MealPlan;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "MealPlanInsertController", urlPatterns = {"/MealPlanInsertController"})
public class MealPlanInsertController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String IMAGE_URL = "images\\mealPlan\\";
    private final String DAYMEAL_URL = "/AMainController?action=DayPlanInsert";
    private final String MEALINSERT_URL = "/AMainController?action=MealPlanInsertPage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String currentPath = getServletContext().getRealPath("/");
        String uploadPath = currentPath.concat(IMAGE_URL);

        MealPlanDAO dao = new MealPlanDAO();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        MealPlan mealPlan = null;
        String id = null;
        String name = null;
        String content = null;
        String type = null;
        int status = 1;
        String imageUrl = IMAGE_URL;
        String errorMessage = null;
        String fileName = null;
        FileItem imageItem = null;

        try {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(currentPath.concat("web")));
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            if (fileItems != null) {
                for (FileItem fileItem : fileItems) {
                    if (fileItem.isFormField()) {
                        switch (fileItem.getFieldName()) {
                            case "id":
                                id = fileItem.getString();
                                errorMessage = dao.checkValidMealPlanId(id);
                                break;
                            case "name":
                                name = fileItem.getString();
                                break;
                            case "content":
                                content = fileItem.getString();
                                break;
                            case "type":
                                type = fileItem.getString();
                                break;
                            default:
                                break;
                        }
                    } else {
                        if (fileItem.getFieldName().equals("imgUrl")) {
                            System.out.println(fileItem.getName());
                            System.out.println(Paths.get(fileItem.getName()));
                            System.out.println(Paths.get(fileItem.getName()).toAbsolutePath().getFileName());
                            fileName = Paths.get(fileItem.getName()).getFileName().toString();
                            imageItem = fileItem;
                            if (fileName != null && !fileName.isEmpty()) {
                                imageUrl += fileName;
                            }
                        }
                    }
                }
            }

            if (errorMessage == null) {
                if (imageItem != null && fileName != null && !fileName.isEmpty()) {
                    File file = new File(uploadPath + fileName);
                    imageItem.write(file);
                }
                mealPlan = new MealPlan(id, name, type, content, imageUrl, status);
                dao.insertNewMealPlan(mealPlan);
                response.sendRedirect(request.getContextPath() + DAYMEAL_URL + "&id=" + mealPlan.getId());
                return;

            } else {
                mealPlan = new MealPlan(id, name, type, content, imageUrl, status);
                request.setAttribute("mealPlan", mealPlan);
                request.setAttribute("ERROR", errorMessage);
                request.getRequestDispatcher(MEALINSERT_URL).forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher(MEALINSERT_URL).forward(request, response);
            return;
        } finally {

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(MEALINSERT_URL).forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
