import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;

public class MainGui extends JFrame {
    private JButton expenseButton;
    private JButton categoryButton;
    
    public MainGui() {
        initializeComponenets();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponenets() {
        setTitle("Main UI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200,1000);
        setLocationRelativeTo(null);

        expenseButton = new JButton("Expense");
        categoryButton = new JButton("Category");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(expenseButton);       
        buttonPanel.add(categoryButton);

        panel.add(buttonPanel,gbc);
        add(panel,BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        expenseButton.addActionListener(e -> {
            new ExpenseGui().setVisible(true);
        });

        categoryButton.addActionListener(e -> {
            new CategoryGui().setVisible(true);
        });
    }
}

class CategoryGui extends JFrame{
    private JTextField titleField;
    private JButton addButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private ExpenseDAO expenseDao;

    public CategoryGui(){
        initializeComponents();
        setupLayout();
        setupEventListeners();
        expenseDao = new ExpenseDAO();
        loadCategory();
    }
}
