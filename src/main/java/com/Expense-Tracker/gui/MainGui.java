import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import com.Expense-Tracker.dao.expenseDAO;
import com.Expense-Tracker.model.Category;

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

    private void initializeComponents(){

        titleField = new JTextField(20);
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        String[] columnNames = {"Id","Title"};

        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        categoryTable = new JTable(tableModel);

        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener( 
            e->{
            if(!e.getValueIsAdjusting()){
                loadSelectedCategory();
            }
        });
    }

    public void setupLayout(){
        setTitle("Category UI");

        setSize(1000,1000);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.add(new JLabel("Name"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(titleField,gbc);     

        JPanel buttonsPanel = new JPanel(new FlowLayout());

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(refreshButton);

        northPanel.add(inputPanel,BorderLayout.NORTH);
        northPanel.add(buttonsPanel,BorderLayout.CENTER);
        
        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(categoryTable),BorderLayout.CENTER);
    }
}
