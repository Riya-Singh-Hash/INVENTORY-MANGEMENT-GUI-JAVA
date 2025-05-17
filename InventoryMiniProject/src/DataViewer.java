// Import statements (keep them at the top of your file)
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DataViewer extends JFrame {
    private JComboBox<String> tableSelector;
    private JList<String> columnSelectorList;
    private JCheckBox showAllColumnsCheck;
    private JComboBox<String> collectionSelector;
    private JButton loadButton;
    private JTable dataTable;
    private JLabel statusLabel;

    private final Map<String, String[]> tableColumns = new HashMap<>();

    public DataViewer() {
        setTitle("Inventory Management Viewer");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tableColumns.put("Inventory", new String[]{"itemId", "itemName", "price", "category", "inStock"});
        tableColumns.put("Supplier", new String[]{"supplierId", "supplierName", "rating", "region", "active"});

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tableSelector = new JComboBox<>(new String[]{"Inventory", "Supplier"});
        columnSelectorList = new JList<>();
        columnSelectorList.setVisibleRowCount(5);
        columnSelectorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        showAllColumnsCheck = new JCheckBox("Display All Columns", true);
        collectionSelector = new JComboBox<>(new String[]{"ArrayList", "LinkedList", "HashSet", "TreeSet"});
        loadButton = new JButton("Load Data");

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Select Table:"), gbc);
        gbc.gridx = 1;
        controlPanel.add(tableSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(showAllColumnsCheck, gbc);
        gbc.gridx = 1;
        controlPanel.add(new JScrollPane(columnSelectorList), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Select Collection:"), gbc);
        gbc.gridx = 1;
        controlPanel.add(collectionSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        controlPanel.add(loadButton, gbc);

        add(controlPanel, BorderLayout.NORTH);

        dataTable = new JTable();
        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);

        tableSelector.addActionListener(e -> updateColumns());
        showAllColumnsCheck.addActionListener(e -> columnSelectorList.setEnabled(!showAllColumnsCheck.isSelected()));
        loadButton.addActionListener(e -> loadData());
        updateColumns();
    }

    private void updateColumns() {
        String table = (String) tableSelector.getSelectedItem();
        if (table != null) {
            String[] cols = tableColumns.get(table);
            columnSelectorList.setListData(cols);
            columnSelectorList.setSelectionInterval(0, cols.length - 1); // select all by default
        }
    }

    private void loadData() {
        String selectedTable = (String) tableSelector.getSelectedItem();
        String collectionType = (String) collectionSelector.getSelectedItem();
        boolean displayAll = showAllColumnsCheck.isSelected();

        List<String> selectedColumns = displayAll
                ? List.of(tableColumns.get(selectedTable))
                : columnSelectorList.getSelectedValuesList();

        statusLabel.setText("Loading data from " + selectedTable + "...");

        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + selectedTable)) {

            List<Object> dataCollection = switch (collectionType) {
                case "LinkedList" -> new LinkedList<>();
                case "HashSet" -> new ArrayList<>(new HashSet<>());
                case "TreeSet" -> new ArrayList<>(new TreeSet<>());
                default -> new ArrayList<>();
            };

            DefaultTableModel model = new DefaultTableModel();
            for (String col : selectedColumns) model.addColumn(col);

            while (rs.next()) {
                if ("Inventory".equals(selectedTable)) {
                    InventoryItem item = new InventoryItem(
                            rs.getInt("itemId"),
                            rs.getString("itemName"),
                            rs.getFloat("price"),
                            rs.getString("category").charAt(0),
                            rs.getBoolean("inStock"));
                    dataCollection.add(item);
                } else {
                    Supplier s = new Supplier(
                            rs.getInt("supplierId"),
                            rs.getString("supplierName"),
                            rs.getFloat("rating"),
                            rs.getString("region").charAt(0),
                            rs.getBoolean("active"));
                    dataCollection.add(s);
                }
            }

            for (Object obj : dataCollection) {
                List<Object> row = new ArrayList<>();
                if (obj instanceof InventoryItem item) {
                    for (String col : selectedColumns) {
                        row.add(switch (col) {
                            case "itemId" -> item.getItemId();
                            case "itemName" -> item.getItemName();
                            case "price" -> item.getPrice();
                            case "category" -> item.getCategory();
                            case "inStock" -> item.isInStock();
                            default -> null;
                        });
                    }
                } else if (obj instanceof Supplier s) {
                    for (String col : selectedColumns) {
                        row.add(switch (col) {
                            case "supplierId" -> s.getSupplierId();
                            case "supplierName" -> s.getSupplierName();
                            case "rating" -> s.getRating();
                            case "region" -> s.getRegion();
                            case "active" -> s.isActive();
                            default -> null;
                        });
                    }
                }
                model.addRow(row.toArray());
            }

            dataTable.setModel(model);
            statusLabel.setText("Loaded " + dataCollection.size() + " rows from " + selectedTable);

        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
