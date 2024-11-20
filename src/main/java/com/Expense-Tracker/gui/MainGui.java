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
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import com.Expense-Tracker.dao.expenseDAO;
import com.Expense-Tracker.model.Category;
import com.Expense-Tracker.model.Expense;

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

     public void setupEventListeners(){
        addButton.addActionListener((e)->{
            addCategory();
        });
        updateButton.addActionListener((e)->{
            updateCategory();
        });
        deleteButton.addActionListener((e)->{
            deleteCategory();
        });
        refreshButton.addActionListener((e)->{
            refreshCategory();
        });
    }

     private void updateTable(List<Category> category){
        tableModel.setRowCount(0);
        for(Category cate: category){
            Object row[] = {
                cate.getId(),
                cate.getName()
            };
            tableModel.addRow(row);
        }
    }

    private void loadCategory(){
        try{
            List<Category> categories = expenseDao.getAllCategories();
            updateTable(categories);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Database Error: "+e.getMessage(),"DataBase Error",JOptionPane.ERROR_MESSAGE);
        }
    }    

    private void addCategory(){
        String name = titleField.getText().trim();
        try{
            int rowsAffected = expenseDao.createCategory(name);
            if(rowsAffected > 0){
                JOptionPane.showMessageDialog(this,"Category Added Successfully", "Success",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this,"Failed to add category", "Failed",JOptionPane.ERROR_MESSAGE);
            }
            loadCategory();
            clearTable();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Database Failed","Database Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory(){
        int row = categoryTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Category to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)categoryTable.getValueAt(row,0);
        String categoryName = titleField.getText();

        if(categoryName == ""){
            JOptionPane.showMessageDialog(this,"Category name is emty!","Invaild Category Name",JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            if(expenseDao.updateCategory(id,categoryName) > 0){
                JOptionPane.showMessageDialog(this,"Category updated Successfully","Update Success",JOptionPane.INFORMATION_MESSAGE);
                loadCategory();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Category Update Failed","Update Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while Updating - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void deleteCategory(){
        int row = categoryTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a Category to update..","Invalid Update",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)categoryTable.getValueAt(row,0);
        try{
            if(expenseDao.deleteCategory(id) > 0){
                JOptionPane.showMessageDialog(this,"Category deleted Successfully","Delete Success",JOptionPane.INFORMATION_MESSAGE);
                loadCategory();
                clearTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Category Delete Failed","Delete Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed while deleting - "+e.getMessage(),"Databse failed",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCategory(){
        loadCategory();
        clearTable();
    }

    private void loadSelectedCategory(){
        int row = categoryTable.getSelectedRow();
        if(row != -1){
            String categoryName =  categoryTable.getValueAt(row, 1).toString();
            titleField.setText(categoryName);
        }
    }

    private  void clearTable(){
        titleField.setText("");
    }

}


class ExpenseGui extends JFrame {

    private JTextField amountField;
    private JTextArea descriptoinArea;
    private JButton addButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JComboBox<String> categoryComboBox;

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private ExpenseDAO expenseDao;

    public ExpenseGui(){
        expenseDao = new ExpenseDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadExpense();
    }

    public void initializeComponents(){

        amountField = new JTextField(20);
        descriptoinArea = new JTextArea(5,20);
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        List<String> categories = new ArrayList<>(); 
            try{
            List<Category> cate = expenseDao.getAllCategories();
            for(Category c: cate){
                categories.add(c.getName());
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Databse Failed : "+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }

        String[] categoriesArray = categories.toArray(new String[0]);


        categoryComboBox = new JComboBox<>(categoriesArray);

        String[] columnNames = {"Id","Amount","Description","Category","Created At"};

        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        expenseTable = new JTable(tableModel);

        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.getSelectionModel().addListSelectionListener( 
            e->{
            if(!e.getValueIsAdjusting()){
                loadSelectedExpense();
            }
        });


    }

    public void setupLayout(){
        setTitle("Expenses");
        setSize(1000,1200);

        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.add(new JLabel("Amount"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(amountField,gbc);     

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(descriptoinArea);


        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Category"),gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(categoryComboBox,gbc);


        JPanel buttonsPanel = new JPanel(new FlowLayout());

        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(refreshButton);

        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonsPanel,BorderLayout.SOUTH);
        
        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(expenseTable),BorderLayout.CENTER);


    }
}

