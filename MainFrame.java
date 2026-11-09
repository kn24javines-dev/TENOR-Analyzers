import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private JTextArea inputArea;
    private JTable symbolJTable;
    private DefaultTableModel tableModel;
    private SymbolTable symbolTable;

    // custom color palette for TENOR
    private static final Color COLOR_BG_YELLOW     = new Color(0xFF, 0xF2, 0xC6); // #fff2c6
    private static final Color COLOR_GREEN         = new Color(0x8D, 0xBC, 0x72); // #8dbc72
    private static final Color COLOR_LOAD_YELLOW   = new Color(0xFF, 0xE4, 0x8A); // #ffe48a
    private static final Color COLOR_CLEAR_RED     = new Color(0xFF, 0x9B, 0x94); // #ff9b94
    private static final Color COLOR_TEXT_DARK     = new Color(60, 60, 60);
    private static final Color COLOR_PLACEHOLDER   = new Color(130, 130, 130);

    private static final String FONT_FAMILY = "SansSerif";
    private static final String PLACEHOLDER_TEXT = "Type a TENOR block of code here or load a .tnr file.";

    public MainFrame() {
        symbolTable = new SymbolTable();

        setTitle("TENOR Lexical Analyzer");
        setSize(1300, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(COLOR_BG_YELLOW);

        // top header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        headerPanel.setBackground(COLOR_BG_YELLOW);
        headerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel logoLabel = new JLabel();
        File logoFile = new File("logo.png");
        if (logoFile.exists()) {
            ImageIcon icon = new ImageIcon("logo.png");
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            logoLabel.setText("[ Logo ]");
            logoLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
            logoLabel.setForeground(COLOR_GREEN);
        }

        JLabel titleLabel = new JLabel("TENOR Lexical Analyzer");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 36));
        titleLabel.setForeground(COLOR_GREEN);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // center workspace
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(COLOR_BG_YELLOW);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // source code input area
        inputArea = new JTextArea(15, 30) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(COLOR_PLACEHOLDER);
                    g2.setFont(new Font("Monospaced", Font.ITALIC, 14));
                    Insets ins = getInsets();
                    g2.drawString(PLACEHOLDER_TEXT, ins.left + 5, ins.top + 20);
                    g2.dispose();
                }
            }
        };
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputArea.setTabSize(4);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(Color.WHITE);
        inputArea.setForeground(COLOR_TEXT_DARK);
        inputArea.setCaretColor(COLOR_TEXT_DARK);

        inputArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { repaint(); }
            @Override
            public void focusLost(FocusEvent e) { repaint(); }
        });
        
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createEmptyBorder());
        inputScroll.getViewport().setBackground(Color.WHITE);

        JPanel sourceWrapper = new JPanel(new BorderLayout());
        sourceWrapper.setBackground(Color.WHITE);
        sourceWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GREEN, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel sourceHeader = new JLabel("SOURCE CODE");
        sourceHeader.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        sourceHeader.setForeground(COLOR_GREEN);
        sourceHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        sourceWrapper.add(sourceHeader, BorderLayout.NORTH);
        sourceWrapper.add(inputScroll, BorderLayout.CENTER);

        // symbol table output area
        String[] columnNames = {"Line and Column Number", "Lexemes", "Tokens"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        symbolJTable = new JTable(tableModel);
        symbolJTable.setFont(new Font("Monospaced", Font.PLAIN, 13));
        symbolJTable.setRowHeight(26);
        symbolJTable.setShowGrid(true);
        symbolJTable.setGridColor(new Color(230, 235, 230));
        symbolJTable.setBackground(new Color(245, 245, 245));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < symbolJTable.getColumnCount(); i++) {
            symbolJTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader tableHeader = symbolJTable.getTableHeader();
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerLabel.setBackground(COLOR_GREEN);
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setHorizontalAlignment(JLabel.CENTER);
                headerLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 12));
                headerLabel.setOpaque(true);
                headerLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
                return headerLabel;
            }
        });

        JScrollPane tableScroll = new JScrollPane(symbolJTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setBackground(new Color(245, 245, 245));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GREEN, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel tableHeaderLabel = new JLabel("SYMBOL TABLE");
        tableHeaderLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        tableHeaderLabel.setForeground(COLOR_GREEN);
        tableHeaderLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        tableWrapper.add(tableHeaderLabel, BorderLayout.NORTH);
        tableWrapper.add(tableScroll, BorderLayout.CENTER);

        centerPanel.add(sourceWrapper);
        centerPanel.add(tableWrapper);
        add(centerPanel, BorderLayout.CENTER);

        // bottom action buttons
        JButton loadFileButton = createStandardButton("LOAD .tnr FILE", COLOR_LOAD_YELLOW, COLOR_TEXT_DARK);
        loadFileButton.addActionListener(e -> loadTnrFile());

        JButton analyzeButton = createStandardButton("ANALYZE", COLOR_GREEN, COLOR_TEXT_DARK);
        analyzeButton.addActionListener(e -> processInput());

        JButton clearButton = createStandardButton("CLEAR", COLOR_CLEAR_RED, COLOR_TEXT_DARK);
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            tableModel.setRowCount(0);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        bottomPanel.setBackground(COLOR_BG_YELLOW);
        bottomPanel.add(loadFileButton);
        bottomPanel.add(analyzeButton);
        bottomPanel.add(clearButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStandardButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 42));
        button.setBorder(BorderFactory.createLineBorder(bg.darker(), 1));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
        return button;
    }

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

    private void processInput() {
        String rawText = inputArea.getText();
        if (rawText == null || rawText.isEmpty() || rawText.equals(PLACEHOLDER_TEXT)) {
            tableModel.setRowCount(0);
            return;
        }

        InputScanner scanner = new InputScanner(rawText);
        ArrayList<Lexeme> lexemes = scanner.scanCode();

        symbolTable.generateTable(lexemes);

        tableModel.setRowCount(0);
        for (SymbolTable.SymbolEntry entry : symbolTable.getTableEntries()) {
            tableModel.addRow(new Object[]{
                entry.getLocation(), 
                entry.getLexeme(),   
                entry.getTokenType() 
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}