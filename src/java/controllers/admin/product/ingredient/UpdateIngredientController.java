package controllers.admin.product.ingredient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import dao.product.IngredientDAO;
import dto.product.Ingredient;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/ingredient/UpdateIngredient")
@MultipartConfig
public class UpdateIngredientController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);

                int id = Integer.parseInt(request.getParameter("id"));
                String ingredientName = null;
                double price = 0.0;
                String unit = null;
                String imgURL = null;

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        switch (item.getFieldName()) {
                            case "ingredientName":
                                ingredientName = item.getString();
                                break;
                            case "price":
                                try {
                                    price = Double.parseDouble(item.getString());

                                } catch (NumberFormatException e) {
                                    price = 10;
                                }
                                break;
                            case "unit":
                                unit = item.getString();
                                break;
                        }
                    } else {
                        try {
                            if (item.getFieldName().equals("imgURL")) {
                                String fileName = Paths.get(item.getName()).getFileName().toString();
                                String uploadPath = getServletContext().getRealPath("/") + "images/ingredient/";
                                File uploadDir = new File(uploadPath);
                                if (!uploadDir.exists()) {
                                    uploadDir.mkdirs();
                                }
                                String filePath = uploadPath + fileName;
                                File file = new File(filePath);

                                item.write(file);
                                imgURL = "images/ingredient/" + fileName;

                            }
                        } catch (Exception e) {
                            imgURL = "images/ingredient/example.png";
                        }

                    }
                }

                Ingredient newIngredient = new Ingredient(id, ingredientName, unit, imgURL, price);
                IngredientDAO dao = new IngredientDAO();
                dao.updateIngredient(newIngredient); // Assume there's a method to update ingredient

                request.getRequestDispatcher("/AMainController?action=ingredientManage").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "invalid id");
                e.printStackTrace();

                request.getRequestDispatcher("/AMainController?action=IngredientUpdatePage").forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        req.getRequestDispatcher("/AMainController?action=IngredientUpdatePage").forward(req, resp);
    }
    
    
}
