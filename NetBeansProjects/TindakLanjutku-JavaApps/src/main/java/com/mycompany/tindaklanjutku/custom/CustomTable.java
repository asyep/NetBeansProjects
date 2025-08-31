package com.mycompany.tindaklanjutku.custom;

import com.mycompany.tindaklanjutku.Koneksi;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTable extends JTable {
    // Cache untuk opsi status
    private String[] statusOptionsCache = null;
    private Map<String, String> statusMappingCache = null;
    
    // Warna untuk setiap status
    private static final Color BELUM_WARNA = new Color(255, 200, 200);   // Merah muda
    private static final Color PROGRESS_WARNA = new Color(255, 255, 150); // Kuning muda
    private static final Color SELESAI_WARNA = new Color(200, 255, 200); // Hijau muda

    public CustomTable() {
        // Model tabel dengan kolom contoh
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Judul Tugas", "Kategori", "PJ", "Deadline", "Status"}, 0
        );
        setModel(model);
        
        // Konfigurasi tampilan dasar
        setAutoCreateRowSorter(true);
        setRowHeight(30);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setGridColor(new Color(220, 220, 220));
        setShowGrid(true);
        
        // Atur renderer kustom
        setDefaultRenderer(Object.class, new CustomCellRenderer());
        
        // Atur header kustom
        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer());
        header.setReorderingAllowed(false);
        
        // Atur renderer dan editor khusus untuk kolom Status (indeks 4)
        int statusColumnIndex = 4;
        getColumnModel().getColumn(statusColumnIndex).setCellRenderer(new StatusCellRenderer());
        getColumnModel().getColumn(statusColumnIndex).setCellEditor(new StatusCellEditor());
    }

    // ================== Database Access Methods ================== //
    
    private String[] getStatusOptionsFromDatabase() {
        // Gunakan cache jika tersedia
        if (statusOptionsCache != null) {
            return statusOptionsCache;
        }
        
        List<String> statusList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Ganti dengan metode koneksi database Anda
            // conn = Koneksi.configDB();
            
            // Untuk contoh, kita gunakan dummy connection
            conn = Koneksi.configDB();
            
            // Query khusus untuk mendapatkan nilai ENUM
            String sql = "SHOW COLUMNS FROM tugas LIKE 'status'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                // Kolom 'Type' berisi definisi ENUM: enum('belum','progres','selesai')
                String typeDefinition = rs.getString("Type");
                
                // Parsing string ENUM
                if (typeDefinition != null && typeDefinition.startsWith("enum")) {
                    // Hapus bagian 'enum(' dan ')'
                    String options = typeDefinition.substring(5, typeDefinition.length() - 1);
                    
                    // Pisahkan nilai dan hapus petik tunggal
                    String[] values = options.split(",");
                    for (String value : values) {
                        statusList.add(value.trim().replaceAll("'", ""));
                    }
                }
            }
            
            // Jika tidak ada data, gunakan nilai default
            if (statusList.isEmpty()) {
                statusList.add("belum");
                statusList.add("progres");
                statusList.add("selesai");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error mengambil data status: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            
            // Gunakan nilai default jika terjadi error
            statusList.add("belum");
            statusList.add("progres");
            statusList.add("selesai");
        } finally {
            // Tutup resource
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        statusOptionsCache = statusList.toArray(new String[0]);
        return statusOptionsCache;
    }

    private Map<String, String> getStatusMapping() {
        if (statusMappingCache != null) {
            return statusMappingCache;
        }
        
        statusMappingCache = new HashMap<>();
        statusMappingCache.put("belum", "Belum Dimulai");
        statusMappingCache.put("progres", "Dalam Proses");
        statusMappingCache.put("selesai", "Selesai");
        
        return statusMappingCache;
    }

    // ================== Custom Renderers and Editors ================== //
    
    // Renderer untuk sel biasa
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, 
            boolean isSelected, boolean hasFocus, 
            int row, int column) {
            
            Component comp = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            // Warna baris bergantian
            if (!isSelected) {
                if (row % 2 == 0) {
                    comp.setBackground(new Color(240, 248, 255)); // AliceBlue
                } else {
                    comp.setBackground(Color.WHITE);
                }
            }
            
            // Styling teks
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            
            return comp;
        }
    }

    // Renderer untuk header
    private static class CustomHeaderRenderer extends DefaultTableCellRenderer {
        public CustomHeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(new Color(70, 130, 180)); // SteelBlue
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return this;
        }
    }

    // Renderer khusus untuk kolom Status dengan warna berbeda
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        private final Map<String, String> statusMapping = getStatusMapping();
        
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, 
            boolean isSelected, boolean hasFocus, 
            int row, int column) {
            
            // Konversi nilai ENUM ke teks yang lebih mudah dibaca
            String enumValue = value != null ? value.toString() : "";
            String displayValue = statusMapping.getOrDefault(enumValue, enumValue);
            
            Component comp = super.getTableCellRendererComponent(
                table, displayValue, isSelected, hasFocus, row, column);
            
            // Atur warna berdasarkan status
            if (!isSelected) {
                switch (enumValue) {
                    case "belum":
                        comp.setBackground(BELUM_WARNA);
                        break;
                    case "progres":
                        comp.setBackground(PROGRESS_WARNA);
                        break;
                    case "selesai":
                        comp.setBackground(SELESAI_WARNA);
                        break;
                    default:
                        // Tetap gunakan warna baris bergantian untuk status tidak dikenal
                        if (row % 2 == 0) {
                            comp.setBackground(new Color(240, 248, 255));
                        } else {
                            comp.setBackground(Color.WHITE);
                        }
                }
            } else {
                // Jika dipilih, gunakan warna default selection
                comp.setBackground(table.getSelectionBackground());
            }
            
            // Styling teks
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            
            return comp;
        }
    }

    // Editor khusus untuk kolom Status (menggunakan combo box)
    private class StatusCellEditor extends DefaultCellEditor {
        private final Map<String, String> statusMapping = getStatusMapping();
        private final Map<String, String> reverseMapping = new HashMap<>();
        private JComboBox<String> combo;
        
        public StatusCellEditor() {
            super(new JComboBox<>());
            combo = (JComboBox<String>) getComponent();
            
            // Buat reverse mapping
            for (Map.Entry<String, String> entry : statusMapping.entrySet()) {
                reverseMapping.put(entry.getValue(), entry.getKey());
            }
            
            // Isi combo box dengan opsi yang sudah dimapping
            for (String enumValue : getStatusOptionsFromDatabase()) {
                combo.addItem(statusMapping.get(enumValue));
            }
            
            // Styling combo box
            combo.setBackground(Color.WHITE); // Latar belakang putih saat edit
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            // Atur renderer untuk combo box agar menampilkan warna
            combo.setRenderer(new StatusComboRenderer());
        }
        
        @Override
        public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {
            
            // Konversi nilai ENUM ke teks untuk combo box
            String displayValue = value != null ? 
                statusMapping.getOrDefault(value.toString(), value.toString()) : "";
            
            combo.setSelectedItem(displayValue);
            return combo;
        }
        
        @Override
        public Object getCellEditorValue() {
            // Konversi kembali dari teks ke nilai ENUM
            String displayValue = (String) combo.getSelectedItem();
            return reverseMapping.getOrDefault(displayValue, displayValue);
        }
    }
    
    // Renderer khusus untuk combo box di editor
    private class StatusComboRenderer extends DefaultListCellRenderer {
        private final Map<String, String> statusMapping = getStatusMapping();
        
        @Override
        public Component getListCellRendererComponent(
            JList<?> list, Object value, 
            int index, boolean isSelected, boolean cellHasFocus) {
            
            Component comp = super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            
            // Atur warna berdasarkan nilai status
            if (value != null) {
                String displayValue = value.toString();
                String enumValue = getKeyFromValue(displayValue);
                
                if (enumValue != null) {
                    switch (enumValue) {
                        case "belum":
                            comp.setBackground(BELUM_WARNA);
                            break;
                        case "progres":
                            comp.setBackground(PROGRESS_WARNA);
                            break;
                        case "selesai":
                            comp.setBackground(SELESAI_WARNA);
                            break;
                    }
                }
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
            
            return comp;
        }
        
        private String getKeyFromValue(String displayValue) {
            for (Map.Entry<String, String> entry : statusMapping.entrySet()) {
                if (entry.getValue().equals(displayValue)) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    // ================== Contoh Penggunaan ================== //
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Table Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        
        CustomTable table = new CustomTable();
        
        // Tambahkan data contoh
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{"Laporan Keuangan", "Finance", "Budi", "2023-12-31", "belum"});
        model.addRow(new Object[]{"Update Sistem", "IT", "Siti", "2023-11-15", "progres"});
        model.addRow(new Object[]{"Pelatihan Karyawan", "HRD", "Ahmad", "2023-10-30", "selesai"});
        
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}