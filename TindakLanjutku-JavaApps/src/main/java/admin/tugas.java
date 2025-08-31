/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import com.mycompany.tindaklanjutku.Koneksi;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ASUS VIVO
 */
public class tugas extends javax.swing.JFrame {

    private String username;
    public tugas(String username) throws SQLException {
        this.username = username;
        initComponents();
        loadTugasData();
    }
    
    private void loadTugasData() {
    // Gunakan try-with-resources untuk auto-close resources
    try (Connection conn = Koneksi.configDB();
         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tugas");
         ResultSet rs = pstmt.executeQuery()) {
        
        // Buat model tabel
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom status yang bisa diedit
                return column == 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 3: return Date.class;
                    default: return String.class;
                }
            }
        };
        
        // Tambahkan kolom
        model.addColumn("ID");
        model.addColumn("Nama Tugas");
        model.addColumn("Deskripsi");
        model.addColumn("Deadline");
        model.addColumn("Status");

        // Isi data
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_tugas"),
                rs.getString("judul"),
                rs.getString("deskripsi"),
                rs.getDate("deadline"),
                rs.getString("status")
            });
        }

        // Set model ke tabel
        tableTugasAdmin.setModel(model);
        
        // Atur lebar kolom
        TableColumnModel columnModel = tableTugasAdmin.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // ID
        columnModel.getColumn(1).setPreferredWidth(200); // Nama Tugas
        columnModel.getColumn(2).setPreferredWidth(300); // Deskripsi
        columnModel.getColumn(3).setPreferredWidth(100); // Deadline
        columnModel.getColumn(4).setPreferredWidth(80);  // Status

        // Format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        // Renderer khusus untuk kolom tanggal
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Date) {
                    super.setValue(dateFormat.format((Date) value));
                } else {
                    super.setValue(value);
                }
            }
        };
        
        // Buat renderer untuk rata tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Terapkan renderer ke semua kolom
        for (int i = 0; i < tableTugasAdmin.getColumnCount(); i++) {
            if (i == 3) {
                // Kolom tanggal menggunakan dateRenderer
                dateRenderer.setHorizontalAlignment(JLabel.CENTER);
                tableTugasAdmin.getColumnModel().getColumn(i).setCellRenderer(dateRenderer);
            } else {
                // Kolom lainnya menggunakan centerRenderer
                tableTugasAdmin.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Terapkan custom renderer untuk status (dengan warna)
        tableTugasAdmin.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        
        // Buat custom editor untuk kolom status (combo box)
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"belum", "progres", "selesai"});
        DefaultCellEditor statusEditor = new DefaultCellEditor(statusComboBox);
        tableTugasAdmin.getColumnModel().getColumn(4).setCellEditor(statusEditor);
        
        // Tambahkan listener untuk menyimpan perubahan status
        tableTugasAdmin.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int row = e.getFirstRow();
                    String newStatus = (String) tableTugasAdmin.getModel().getValueAt(row, 4);
                    int taskId = (int) tableTugasAdmin.getModel().getValueAt(row, 0);
                    updateTaskStatus(taskId, newStatus);
                }
            }
        });
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading data: " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
    
private void updateTaskStatus(int taskId, String newStatus) {
    // Validasi nilai status yang valid
    String lowerStatus = newStatus.toLowerCase();
    if (!lowerStatus.equals("belum") && 
        !lowerStatus.equals("progres") && 
        !lowerStatus.equals("selesai")) {
        JOptionPane.showMessageDialog(this, 
            "Status tidak valid: " + newStatus,
            "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection conn = Koneksi.configDB();
         PreparedStatement pstmt = conn.prepareStatement(
             "UPDATE tugas SET status = ? WHERE id_tugas = ?")) {

        pstmt.setString(1, lowerStatus);
        pstmt.setInt(2, taskId);
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            loadTugasData();
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error updating status: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


// Custom renderer untuk kolom status (dengan warna)
private static class StatusRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(JLabel.CENTER);
        
        if (value != null) {
            String status = value.toString().toLowerCase();
            if (isSelected) {
                // Tetap gunakan warna seleksi jika baris terpilih
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                switch (status) {
                    case "selesai":
                        c.setBackground(new Color(144, 238, 144)); // hijau muda
                        c.setForeground(Color.BLACK);
                        break;
                    case "progres":
                        c.setBackground(new Color(255, 255, 153)); // kuning muda
                        c.setForeground(Color.BLACK);
                        break;
                    case "belum":
                        c.setBackground(new Color(255, 153, 153)); // merah muda
                        c.setForeground(Color.BLACK);
                        break;
                    default:
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                }
            }
        }
        return c;
    }
}

private void deleteTask(int taskId) {
    try (Connection conn = Koneksi.configDB();
         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tugas WHERE id_tugas = ?")) {
        
        pstmt.setInt(1, taskId);
        int affectedRows = pstmt.executeUpdate();
        
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Tugas berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTugasData(); // Refresh tabel setelah hapus
        } else {
            JOptionPane.showMessageDialog(this, "Tugas tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Gagal menghapus tugas: " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        dashboarItem = new javax.swing.JButton();
        tugasItem = new javax.swing.JButton();
        pjItem = new javax.swing.JButton();
        kategoriItem = new javax.swing.JButton();
        catatanKerjaItem = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        roundedButton1 = new com.mycompany.tindaklanjutku.custom.RoundedButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTugasAdmin = new com.mycompany.tindaklanjutku.custom.CustomTable();
        roundedButton2 = new com.mycompany.tindaklanjutku.custom.RoundedButton();
        roundedButton3 = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tugas Admin");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setRoundBottomLeft(8);
        panelCustom1.setRoundBottomRight(8);
        panelCustom1.setRoundTopLeft(8);
        panelCustom1.setRoundTopRight(8);

        panelCustom2.setBackground(new java.awt.Color(78, 75, 209));
        panelCustom2.setRoundBottomLeft(8);
        panelCustom2.setRoundTopLeft(8);

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/list.png"))); // NOI18N
        jLabel2.setText("Tindak Lanjutku");

        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        dashboarItem.setBackground(new java.awt.Color(78, 75, 209));
        dashboarItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        dashboarItem.setForeground(new java.awt.Color(255, 255, 255));
        dashboarItem.setText("Dashboard");
        dashboarItem.setBorder(null);
        dashboarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboarItemActionPerformed(evt);
            }
        });

        tugasItem.setBackground(new java.awt.Color(78, 75, 209));
        tugasItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tugasItem.setForeground(new java.awt.Color(255, 255, 255));
        tugasItem.setText("Tugas");
        tugasItem.setBorder(null);

        pjItem.setBackground(new java.awt.Color(78, 75, 209));
        pjItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        pjItem.setForeground(new java.awt.Color(255, 255, 255));
        pjItem.setText("Daftar User");
        pjItem.setBorder(null);
        pjItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pjItemActionPerformed(evt);
            }
        });

        kategoriItem.setBackground(new java.awt.Color(78, 75, 209));
        kategoriItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        kategoriItem.setForeground(new java.awt.Color(255, 255, 255));
        kategoriItem.setText("Divisi");
        kategoriItem.setBorder(null);
        kategoriItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kategoriItemActionPerformed(evt);
            }
        });

        catatanKerjaItem.setBackground(new java.awt.Color(78, 75, 209));
        catatanKerjaItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        catatanKerjaItem.setForeground(new java.awt.Color(255, 255, 255));
        catatanKerjaItem.setText("Catatan Kerja");
        catatanKerjaItem.setBorder(null);
        catatanKerjaItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catatanKerjaItemActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 51, 51));
        jButton6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Logout");
        jButton6.setBorder(null);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
            .addComponent(dashboarItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tugasItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pjItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kategoriItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(catatanKerjaItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dashboarItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tugasItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pjItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kategoriItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tugas");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Anggota");

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Manajemen Tugas");

        roundedButton1.setBackground(new java.awt.Color(78, 75, 209));
        roundedButton1.setText("Tambah Tugas");
        roundedButton1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedButton1.setHoverBackgroundColor(new java.awt.Color(102, 102, 255));
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });

        tableTugasAdmin.setForeground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setViewportView(tableTugasAdmin);

        roundedButton2.setBackground(new java.awt.Color(255, 51, 51));
        roundedButton2.setText("Hapus Tugas");
        roundedButton2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedButton2.setHoverBackgroundColor(new java.awt.Color(255, 153, 153));
        roundedButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton2ActionPerformed(evt);
            }
        });

        roundedButton3.setBackground(new java.awt.Color(0, 204, 0));
        roundedButton3.setText("Edit Tugas");
        roundedButton3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedButton3.setHoverBackgroundColor(new java.awt.Color(0, 255, 51));
        roundedButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCustom1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(8, 8, 8))
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(roundedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(roundedButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(47, 47, 47))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roundedButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(149, 149, 149))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Apakah Anda yakin ingin logout?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            
            // Close current window
            this.dispose();
            
            // Open login form
            java.awt.EventQueue.invokeLater(() -> {
                new loginForm().setVisible(true);
                dispose();
            });
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, 
                "Error saat logout: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
        new uploadTugas(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_roundedButton1ActionPerformed

    private void roundedButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton2ActionPerformed
            int selectedRow = tableTugasAdmin.getSelectedRow();

    if (selectedRow != -1) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus tugas ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Ambil ID tugas dari kolom ke-0
            int idTugas = (int) tableTugasAdmin.getValueAt(selectedRow, 0);

            try (Connection conn = Koneksi.configDB();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tugas WHERE id_tugas = ?")) {
                
                pstmt.setInt(1, idTugas);
                pstmt.executeUpdate();
                
                // Refresh tabel (lebih aman daripada hanya hapus dari model)
                loadTugasData();

                JOptionPane.showMessageDialog(this, "Tugas berhasil dihapus.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus tugas: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Silakan pilih tugas yang ingin dihapus terlebih dahulu.");
    }

    }//GEN-LAST:event_roundedButton2ActionPerformed

    private void roundedButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton3ActionPerformed
            int selectedRow = tableTugasAdmin.getSelectedRow();
    if (selectedRow != -1) {
        // Ambil ID tugas dari kolom ke-0
        int idTugas = (int) tableTugasAdmin.getValueAt(selectedRow, 0);
        new EditTugas(idTugas,username).setVisible(true); // Kirim ID tugas
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Silakan pilih tugas yang ingin diubah terlebih dahulu.");
    }
    }//GEN-LAST:event_roundedButton3ActionPerformed

    private void pjItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjItemActionPerformed
        new MenambahkanRole(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_pjItemActionPerformed

    private void kategoriItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kategoriItemActionPerformed
        new uploadDivisi(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_kategoriItemActionPerformed

    private void catatanKerjaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catatanKerjaItemActionPerformed
        new CatatanAdmin(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_catatanKerjaItemActionPerformed

    private void dashboarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboarItemActionPerformed
        new AdminDashboard(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_dashboarItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JButton dashboarItem;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton kategoriItem;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton1;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton2;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton3;
    private com.mycompany.tindaklanjutku.custom.CustomTable tableTugasAdmin;
    private javax.swing.JButton tugasItem;
    // End of variables declaration//GEN-END:variables
}
