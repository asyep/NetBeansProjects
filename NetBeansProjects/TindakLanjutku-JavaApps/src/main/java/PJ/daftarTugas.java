/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PJ;

import auth.SessionManager;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import javax.swing.JOptionPane;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import com.mycompany.tindaklanjutku.Koneksi;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class daftarTugas extends javax.swing.JFrame {

    private Integer idPJ;
    private String username;
    public daftarTugas(Integer idPJ, String username) {
        this.username = username;
        this.idPJ = SessionManager.getInstance().getIdPJ();
        initComponents();
        loadTugasData();
        namausr.setText(username);
        
    }
    private void loadTugasData() {
        try (Connection conn = Koneksi.configDB();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT id_tugas, judul, deskripsi, deadline, status FROM tugas WHERE id_pj = ? ORDER BY deadline")) {

            pstmt.setInt(1, idPJ);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // hanya kolom status yang bisa diubah
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

            model.addColumn("ID");
            model.addColumn("Nama Tugas");
            model.addColumn("Deskripsi");
            model.addColumn("Deadline");
            model.addColumn("Status");

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id_tugas"),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getDate("deadline"),
                    rs.getString("status")
                });
            }

            daftarTugas.setModel(model);

            // Atur lebar kolom
            TableColumnModel columnModel = daftarTugas.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(300);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(80);

            // Renderer untuk penyesuaian tampilan
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            // Renderer khusus untuk tanggal
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
                @Override
                protected void setValue(Object value) {
                    if (value instanceof Date) {
                        setText(dateFormat.format((Date) value));
                    } else {
                        super.setValue(value);
                    }
                }
            };
            dateRenderer.setHorizontalAlignment(JLabel.CENTER);

            // Terapkan renderer ke semua kolom
            for (int i = 0; i < daftarTugas.getColumnCount(); i++) {
                if (i == 3) {
                    daftarTugas.getColumnModel().getColumn(i).setCellRenderer(dateRenderer);
                } else {
                    daftarTugas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
            }

            // Renderer khusus untuk status
            daftarTugas.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

            // ComboBox untuk status
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"belum", "progres", "selesai"});
            daftarTugas.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusComboBox));

            // Listener untuk update status
            daftarTugas.getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                        int row = e.getFirstRow();
                        int taskId = (int) daftarTugas.getValueAt(row, 0);
                        String newStatus = (String) daftarTugas.getValueAt(row, 4);
                        updateTaskStatus(taskId, newStatus);
                    }
                }
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTaskStatus(int taskId, String newStatus) {
        String lowerStatus = newStatus.toLowerCase();
        if (!lowerStatus.equals("belum") && !lowerStatus.equals("progres") && !lowerStatus.equals("selesai")) {
            JOptionPane.showMessageDialog(this, "Status tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Koneksi.configDB();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE tugas SET status = ? WHERE id_tugas = ? AND id_pj = ?")) {

            pstmt.setString(1, lowerStatus);
            pstmt.setInt(2, taskId);
            pstmt.setInt(3, idPJ);
            
            int updatedRows = pstmt.executeUpdate();
            
            if (updatedRows == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Tugas tidak ditemukan atau Anda tidak memiliki akses", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                loadTugasData(); // Refresh data setelah update
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update status: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderer khusus untuk status dengan warna berbeda
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            
            if (!isSelected && value != null) {
                String status = value.toString().toLowerCase();
                switch (status) {
                    case "belum":
                        c.setBackground(new Color(255, 153, 153)); // merah muda
                        break;
                    case "progres":
                        c.setBackground(new Color(255, 255, 153)); // kuning
                        break;
                    case "selesai":
                        c.setBackground(new Color(144, 238, 144)); // hijau muda
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        dashboardItem = new javax.swing.JButton();
        pjItem = new javax.swing.JButton();
        catatanKerjaItem = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        tugasItem1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        namausr = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        daftarTugas = new com.mycompany.tindaklanjutku.custom.CustomTable();
        DTugas = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Daftar Tugas");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setPreferredSize(new java.awt.Dimension(1111, 741));
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

        dashboardItem.setBackground(new java.awt.Color(78, 75, 209));
        dashboardItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        dashboardItem.setForeground(new java.awt.Color(255, 255, 255));
        dashboardItem.setText("Dashboard");
        dashboardItem.setBorder(null);
        dashboardItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardItemActionPerformed(evt);
            }
        });

        pjItem.setBackground(new java.awt.Color(78, 75, 209));
        pjItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        pjItem.setForeground(new java.awt.Color(255, 255, 255));
        pjItem.setText("Anggota");
        pjItem.setBorder(null);
        pjItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pjItemActionPerformed(evt);
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

        Logout.setBackground(new java.awt.Color(255, 51, 51));
        Logout.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        Logout.setForeground(new java.awt.Color(255, 255, 255));
        Logout.setText("Logout");
        Logout.setBorder(null);
        Logout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });

        tugasItem1.setBackground(new java.awt.Color(78, 75, 209));
        tugasItem1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tugasItem1.setForeground(new java.awt.Color(255, 255, 255));
        tugasItem1.setText("Daftar Tugas");
        tugasItem1.setBorder(null);
        tugasItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tugasItem1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
            .addComponent(dashboardItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pjItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(catatanKerjaItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Logout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tugasItem1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tugasItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pjItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
                .addComponent(Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Daftar Tugas");

        namausr.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        namausr.setForeground(new java.awt.Color(51, 51, 51));
        namausr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namausr.setText("Nama Anggota");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jScrollPane1.setViewportView(daftarTugas);

        DTugas.setBackground(new java.awt.Color(102, 102, 255));
        DTugas.setText("Distribusikan Tugas");
        DTugas.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        DTugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DTugasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelCustom1Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(384, 384, 384)
                            .addComponent(namausr, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator2)
                        .addComponent(jScrollPane1))
                    .addComponent(DTugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 71, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(namausr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(DTugas, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardItemActionPerformed
        new PJDashboard(username,idPJ).setVisible(true);
        dispose();
    }//GEN-LAST:event_dashboardItemActionPerformed

    private void pjItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjItemActionPerformed
        new Anggota(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_pjItemActionPerformed

    private void catatanKerjaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catatanKerjaItemActionPerformed
        new LogCatatan(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_catatanKerjaItemActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
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
    }//GEN-LAST:event_LogoutActionPerformed

    private void tugasItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tugasItem1ActionPerformed
        new daftarTugas(idPJ,username).setVisible(true);
        dispose();
    }//GEN-LAST:event_tugasItem1ActionPerformed

    private void DTugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DTugasActionPerformed
        try {
            if (!distribusiTugas.hasAvailableTugas(idPJ)) {
                JOptionPane.showMessageDialog(this, "Tidak ada tugas yang tersedia untuk didistribusikan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            new distribusiTugas(idPJ).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(daftarTugas.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Gagal membuka jendela distribusi tugas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_DTugasActionPerformed

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
            java.util.logging.Logger.getLogger(daftarTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(daftarTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(daftarTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(daftarTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new daftarTugas(3,"achmad").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mycompany.tindaklanjutku.custom.RoundedButton DTugas;
    private javax.swing.JButton Logout;
    private javax.swing.JButton catatanKerjaItem;
    private com.mycompany.tindaklanjutku.custom.CustomTable daftarTugas;
    private javax.swing.JButton dashboardItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel namausr;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private javax.swing.JButton tugasItem1;
    // End of variables declaration//GEN-END:variables
}
