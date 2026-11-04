import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextArea inputArea;
    private JTable symbolJTable;
    private DefaultTableModel tableModel;
    private SymbolTable symbolTable;

    public MainFrame() {
        symbolTable = new SymbolTable();

        // Window configuration
        setTitle("Tenor Lexical Analyzer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top / Left Panel: Code Input Area
        inputArea = new JTextArea(10, 30);
        inputArea.setLineWrap(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Source Code / Lexemes"));

        // Right / Center Panel: Symbol Table Output
        String[] columnNames = {"Lexeme", "Token Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        symbolJTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(symbolJTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Symbol Table"));

        // Split view for Input and Output
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputScroll, tableScroll);
        splitPane.setResizeWeight(0.4);
        add(splitPane, BorderLayout.CENTER);

        // Bottom Panel: Control Action Button
        JButton analyzeButton = new JButton("Analyze / Tokenize");
        analyzeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        analyzeButton.addActionListener(e -> processInput());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(analyzeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void processInput() {
        // Split input by whitespace to extract raw lexemes
        String rawText = inputArea.getText().trim();
        if (rawText.isEmpty()) {
            tableModel.setRowCount(0);
            return;
        }

        String[] lexemes = rawText.split("\\s+");

        // Generate entries using your backend code
        symbolTable.generateTable(lexemes);

        // Populate Swing Table Model
        tableModel.setRowCount(0); // clear previous rows
        for (SymbolTable.SymbolEntry entry : symbolTable.getTableEntries()) {
            tableModel.addRow(new Object[]{entry.getLexeme(), entry.getTokenType()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}