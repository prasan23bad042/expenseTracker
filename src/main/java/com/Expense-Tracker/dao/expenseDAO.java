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
}
