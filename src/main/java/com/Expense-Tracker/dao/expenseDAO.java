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
    
    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT e.*, c.name as category_name FROM expenses e LEFT JOIN categories c ON e.cate_id = c.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("exp_id"),
                    rs.getInt("amount"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getInt("cate_id"),
                    rs.getString("category_name")
                );
                expenses.add(expense);
            }
        }
        return expenses;
    }
    
    public int createExpense(int amount, String category, String description) throws SQLException {
        String getCategoryIdSql = "SELECT id FROM categories WHERE name = ?";
        String insertExpenseSql = "INSERT INTO expenses (amount, description, cate_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            int categoryId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(getCategoryIdSql)) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    categoryId = rs.getInt("id");
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insertExpenseSql)) {
                pstmt.setInt(1, amount);
                pstmt.setString(2, description);
                pstmt.setInt(3, categoryId);
                pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                return pstmt.executeUpdate();
            }
        }
    }
    
    public int updateExpense(int id, int amount, String description, String category) throws SQLException {
        String getCategoryIdSql = "SELECT id FROM categories WHERE name = ?";
        String updateExpenseSql = "UPDATE expenses SET amount = ?, description = ?, cate_id = ? WHERE exp_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            int categoryId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(getCategoryIdSql)) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    categoryId = rs.getInt("id");
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(updateExpenseSql)) {
                pstmt.setInt(1, amount);
                pstmt.setString(2, description);
                pstmt.setInt(3, categoryId);
                pstmt.setInt(4, id);
                return pstmt.executeUpdate();
            }
        }
    }
    
    public int deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE exp_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }
}
