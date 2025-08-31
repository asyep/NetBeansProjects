package PJ;

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

public class Anggota extends javax.swing.JFrame {

    private String username;
    private Integer idPJ;

    public Anggota(String username) {
        this.idPJ = SessionManager.getInstance().getIdPJ();
        this.username = username;
        initComponents();
        loadAnggotaData();
        namaAdmin.setText(username);
    }

    private void loadAnggotaData() {
        try (Connection conn = Koneksi.configDB()) {
            // Query untuk mendapatkan anggota yang satu divisi dengan penanggung jawab
            String query = "SELECT u.namaUsr, d.nama_divisi " +
                         "FROM user u " +
                         "JOIN divisi d ON u.id_divisi = d.id_divisi " +
                         "WHERE u.id_divisi = (SELECT id_divisi FROM penanggung_jawab WHERE id_user = " +
                         "(SELECT id_usr FROM user WHERE namaUsr = ?)) " +
                         "AND u.role = 'anggota'";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Tabel tidak bisa di-edit
                }
            };

            model.addColumn("Nama Anggota");
            model.addColumn("Divisi");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("namaUsr"),
                    rs.getString("nama_divisi")
                });
            }

            anggota.setModel(model);

            // Set lebar kolom
            TableColumnModel columnModel = anggota.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200); // Nama Anggota
            columnModel.getColumn(1).setPreferredWidth(150); // Divisi

            // Tengah semua kolom
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            for (int i = 0; i < anggota.getColumnCount(); i++) {
                anggota.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
        dashboardItem = new javax.swing.JButton();
        pjItem = new javax.swing.JButton();
        catatanKerjaItem = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        tugasItem1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        namaAdmin = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        anggota = new com.mycompany.tindaklanjutku.custom.CustomTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Daftar Anggota");

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
        dashboardItem.setToolTipText("");
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
        jLabel1.setText("Anggota");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        namaAdmin.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        namaAdmin.setForeground(new java.awt.Color(51, 51, 51));
        namaAdmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        namaAdmin.setText("Nama Anggota");

        jScrollPane1.setViewportView(anggota);

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(384, 384, 384)
                        .addComponent(namaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane1))
                .addGap(0, 82, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(namaAdmin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Anggota("USERNAME PJ").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private com.mycompany.tindaklanjutku.custom.CustomTable anggota;
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JButton dashboardItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel namaAdmin;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private javax.swing.JButton tugasItem1;
    // End of variables declaration//GEN-END:variables
}
