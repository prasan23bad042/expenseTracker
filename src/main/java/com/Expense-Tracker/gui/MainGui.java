import javax.swing.JButton;
import javax.swing.JFrame;

public class MainGui extends JFrame {
    private JButton expenseButton;
    private JButton categoryButton;
    
    public MainGui() {
        initializeComponenets();
        setupLayout();
        setupEventListeners();
    }
}
