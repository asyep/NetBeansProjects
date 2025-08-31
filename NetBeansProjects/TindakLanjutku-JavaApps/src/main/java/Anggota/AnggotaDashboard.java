package Anggota;

import auth.SessionManager;
import com.mycompany.tindaklanjutku.Koneksi;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class AnggotaDashboard extends javax.swing.JFrame {

    private String username;
    private Integer ID;

    public AnggotaDashboard(String username) {
        this.username = username;
        this.ID = SessionManager.getInstance().getIdPJ();
        initComponents();
        loadTugasData();
        namaAnggota.setText(username);
    }

private void loadTugasData() {
    try (Connection conn = Koneksi.configDB()) {
        // Query untuk mendapatkan tugas yang diberikan kepada anggota yang sedang login
        String query = "SELECT u.namaUsr, t.judul, t.deskripsi, t.deadline " +
                      "FROM tugas_user tu " +
                      "JOIN user u ON tu.id_user = u.id_usr " +
                      "JOIN tugas t ON tu.id_tugas = t.id_tugas " +
                      "WHERE u.namaUsr = ?";
        
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa di-edit
            }
        };

        // Tambahkan kolom-kolom yang dibutuhkan
        model.addColumn("Nama Anggota");
        model.addColumn("Judul Tugas");
        model.addColumn("Deskripsi");
        model.addColumn("Deadline");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("namaUsr"),
                rs.getString("judul"),
                rs.getString("deskripsi"),
                rs.getString("deadline")
            });
        }

        tableTugas.setModel(model);

        // Set lebar kolom
        TableColumnModel columnModel = tableTugas.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150); // Nama Anggota
        columnModel.getColumn(1).setPreferredWidth(200); // Judul Tugas
        columnModel.getColumn(2).setPreferredWidth(300); // Deskripsi
        columnModel.getColumn(3).setPreferredWidth(100); // Deadline

        // Tengah semua kolom
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0; i < tableTugas.getColumnCount(); i++) {
            tableTugas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Jika tidak ada data, tampilkan pesan
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"Tidak ada tugas", "", "", ""});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error loading data: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        catatanKerjaItem = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        tugasItem1 = new javax.swing.JButton();
        LabelTugas = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        namaAnggota = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard Angota");

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
                .addComponent(tugasItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 430, Short.MAX_VALUE)
                .addComponent(Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        LabelTugas.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        LabelTugas.setForeground(new java.awt.Color(51, 51, 51));
        LabelTugas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LabelTugas.setText("Daftar Tugas");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        namaAnggota.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        namaAnggota.setForeground(new java.awt.Color(51, 51, 51));
        namaAnggota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namaAnggota.setText("Nama Anggota");

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addComponent(LabelTugas, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(384, 384, 384)
                        .addComponent(namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2))
                .addGap(0, 82, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelTugas)
                    .addComponent(namaAnggota))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardItemActionPerformed

    private void pjItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pjItemActionPerformed

    private void catatanKerjaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catatanKerjaItemActionPerformed
        new CatatanAnggota(username,ID).setVisible(true);
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
        new AnggotaDashboard(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_tugasItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(AnggotaDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnggotaDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnggotaDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnggotaDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnggotaDashboard("USERNAME PJ").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelTugas;
    private javax.swing.JButton Logout;
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel namaAnggota;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton tugasItem1;
    // End of variables declaration//GEN-END:variables
}