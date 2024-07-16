package controllers.admin.product.ingredient;

import dao.product.IngredientDAO;
import dto.product.Ingredient;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(urlPatterns = {"/admin/ingredient/insertIngredient"})
@MultipartConfig // Enable multipart/form-data processing
public class UploadIngredientServlet extends HttpServlet {

    private static final String IMAGES_DIRECTORY = "images/ingredient/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String currentProjectPath = getServletContext().getRealPath("/");
        System.out.println(currentProjectPath);
        String uploadPath = currentProjectPath.concat(IMAGES_DIRECTORY);
        System.out.println(uploadPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        int id = 0;
        String ingredientName = null;
        double price = 0.0;
        String unit = null;
        String imgURL = null;

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
                        case "ingredientName":
                            ingredientName = fileItem.getString();
                            break;
                        case "price":
                            try {
                                price = Double.parseDouble(fileItem.getString());
                                if (price <= 0){
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException e) {
                                price = 10;
                            }
                            break;
                        case "unit":
                            unit = fileItem.getString();
                            break;
                        default:
                            break;
                    }
                } else {
                    // Handle file uploads
                    try {
                        if (fileItem.getFieldName().equals("imgURL")) {
                            String fileName = Paths.get(fileItem.getName()).getFileName().toString();
                            String filePath = uploadPath + fileName;
                            System.out.println(filePath);
//                        Files.deleteIfExists(Paths.get(filePath));
                            File file = new File(filePath);
                            fileItem.write(file);
                            imgURL = IMAGES_DIRECTORY + fileName;
                        }
                    } catch (Exception e) {
                        imgURL = "images/ingredient/example.png";
                    }

                }
            }

            // Save the ingredient data to the database
            Ingredient ingredient = new Ingredient(id, ingredientName, unit, imgURL, price);
            IngredientDAO dao = new IngredientDAO();
            dao.InsertToTable(ingredient);
            request.getRequestDispatcher("/AMainController?action=ingredientManage").forward(request, response);
        } catch (Exception e) {
            System.out.println("hello");
            e.printStackTrace();
            request.setAttribute("errorMessage", "duplicate Id or duplicate name");
            System.out.println(e.getMessage());
            request.getRequestDispatcher("/AMainController?action=IngredientInsertPage").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/AMainController?action=IngredientInsertPage").forward(req, resp);
    }
    
    

}
