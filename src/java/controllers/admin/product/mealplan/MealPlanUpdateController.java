package controllers.admin.product.mealplan;

import dao.plan.MealPlanDAO;
import dto.plan.MealPlan;
import java.io.File;
import java.io.IOException;
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
 * Servlet implementation class MealPlanUpdateController
 */
@WebServlet(name = "MealPlanUpdateController", urlPatterns = {"/admin/MealPlanUpdateController"})
public class MealPlanUpdateController extends HttpServlet {

    private final String IMAGE_URL = "images/mealPlan/";
    private final String DAYMEAL_URL = "/AMainController?action=DayPlanInsert";
    private final String MEALINSERT_URL = "/AMainController?action=MealPlanUpdatePage";

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
        String id = request.getParameter("id");
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/AMainController?action=MealPlan");
            return;
        } else {
            mealPlan = dao.getMealPlanById(id);
            if (mealPlan == null) {
                response.sendRedirect(request.getContextPath() + "/AMainController?action=MealPlan");
                return;
            }
        }
        String name = mealPlan.getName();
        String content = mealPlan.getContent();
        String type = mealPlan.getType();
        int status = mealPlan.getStatus();
        String oldImageUrl = mealPlan.getImgUrl();
        String newImageUrl = oldImageUrl; // Default to old image URL
        String fileName = null;

        try {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(currentPath.concat("web")));
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            if (fileItems != null) {
                for (FileItem fileItem : fileItems) {
                    if (fileItem.isFormField()) {
                        switch (fileItem.getFieldName()) {
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
                        if (fileItem.getFieldName().equalsIgnoreCase("imgUrl")) {
                            fileName = fileItem.getName();
                            if (fileName != null && !fileName.isEmpty()) {
                                File file = new File(uploadPath + fileName);
                                fileItem.write(file);
                                newImageUrl = IMAGE_URL + fileName; // Update the new image URL
                            }
                        }
                    }
                }
            }

            // Update meal plan in the database
            mealPlan = new MealPlan(id, name, type, content, newImageUrl, status);
            boolean result = dao.updateMealPlan(mealPlan);
            if (result) {
                response.sendRedirect(request.getContextPath() + DAYMEAL_URL + "&id=" + mealPlan.getId());
            } else {
                request.setAttribute("mealPlan", mealPlan);
                request.getRequestDispatcher(MEALINSERT_URL).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher(MEALINSERT_URL).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "MealPlanUpdateController";
    }
}
