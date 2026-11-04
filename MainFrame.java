import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private JTextArea inputArea;
    private JTable symbolJTable;
    private DefaultTableModel tableModel;
    private SymbolTable symbolTable;

    public MainFrame() {
        symbolTable = new SymbolTable();

        // Window configuration
        setTitle("Tenor Lexical Analyzer");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Code input area
        inputArea = new JTextArea(10, 30);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        inputArea.setTabSize(4);
        inputArea.setLineWrap(false);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Source Code / Lexemes (.tnr)"));

        // Symbol table output
        String[] columnNames = {"No.", "Lexeme", "Token Type", "Line:Col"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        symbolJTable = new JTable(tableModel);
        symbolJTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        symbolJTable.setRowHeight(22);
        JScrollPane tableScroll = new JScrollPane(symbolJTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Symbol Table"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputScroll, tableScroll);
        splitPane.setResizeWeight(0.45);
        add(splitPane, BorderLayout.CENTER);

        // Control buttons
        JButton loadFileButton = new JButton("Load .tnr File");
        loadFileButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loadFileButton.addActionListener(e -> loadTnrFile());

        JButton analyzeButton = new JButton("Analyze / Tokenize");
        analyzeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        analyzeButton.addActionListener(e -> processInput());

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            tableModel.setRowCount(0);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(loadFileButton);
        bottomPanel.add(analyzeButton);
        bottomPanel.add(clearButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // File loader action
    private void loadTnrFile() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Select TENOR Source File (.tnr)");
        chooser.setFileFilter(new FileNameExtensionFilter("TENOR Files (*.tnr, *.txt)", "tnr", "txt"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
                String fileCode = InputScanner.loadFile(selectedFile.getAbsolutePath());
                inputArea.setText(fileCode);
                processInput();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not read file: " + ex.getMessage(),
                    "File Read Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Input scanner and table population
    private void processInput() {
        String rawText = inputArea.getText();
        if (rawText == null || rawText.isEmpty()) {
            tableModel.setRowCount(0);
            return;
        }

        InputScanner scanner = new InputScanner(rawText);
        ArrayList<Lexeme> lexemes = scanner.scanCode();

        symbolTable.generateTable(lexemes);

        tableModel.setRowCount(0);
        for (SymbolTable.SymbolEntry entry : symbolTable.getTableEntries()) {
            tableModel.addRow(new Object[]{
                entry.getNumber(),
                entry.getLexeme(),
                entry.getTokenType(),
                entry.getLocation()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
