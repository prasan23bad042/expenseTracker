import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.Expense-Tracker.model.Category;
import com.Expense-Tracker.model.Expense;
import com.Expense-Tracker.util.DatabaseConnection;
import java.time.LocalDateTime;

public class ExpenseDAO{
    private static final String SELECT_ALL_EXPENSES = "SELECT * FROM expenses";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM categories";
    
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CATEGORIES)) {
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        }
        return categories;
    }
    
    public int createCategory(String name) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate();
        }
    }
    
    public int updateCategory(int id, String name) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate();
        }
    }
    
    public int deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }
}
