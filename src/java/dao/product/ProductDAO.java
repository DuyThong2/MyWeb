package dao.product;

import Utility.JDBCUtil;
import dto.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {

    private static final String GET_PRODUCT_BY_ID_QUERY = "SELECT id, name, decription, isOnSale, discountID FROM Product WHERE id = ?";

    public static <T extends Product> T getProductById(String id) {
        if (id != null && id.matches("[MP][0-9]{3,4}")) {
            if (id.startsWith("M")) {
                MealDAO mealDAO = new MealDAO();
                return (T) mealDAO.getMealFromId(id);
            } else {
                IngredientPacketDAO packetDAO = new IngredientPacketDAO();
                return (T) packetDAO.getIngredientPacketFromId(id);
            }
        }

        return null;
    }
    
    public void addDiscountForProduct(){
        
    }
    
    
}
