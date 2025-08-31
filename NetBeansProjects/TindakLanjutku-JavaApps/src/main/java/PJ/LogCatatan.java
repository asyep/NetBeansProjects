/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PJ;

import admin.CardCatatan;
import auth.SessionManager;
import com.mycompany.tindaklanjutku.Koneksi;
import com.mycompany.tindaklanjutku.custom.panelCustom;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author ASUS VIVO
 */
public class LogCatatan extends javax.swing.JFrame {

    private String username;
    private Integer idPJ;
    
    public LogCatatan(String username) {
        this.idPJ = SessionManager.getInstance().getIdPJ();
        this.username = username;
        initComponents();
        tampilkanCatatanPJ();
    }
    
    public panelCustom getCardCatatan() {
        return cardCatatan;
    }
    
public void tampilkanCatatanPJ() {
    String sql = "SELECT c.*, t.judul FROM catatan_hasil c " +
                 "JOIN tugas t ON c.id_tugas = t.id_tugas " +
                 "JOIN penanggung_jawab pj ON t.id_pj = pj.id_pj " +
                 "JOIN user u ON pj.id_user = u.Id_usr " +
                 "WHERE u.namaUsr = ?";
    
    try (Connection conn = Koneksi.configDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();
        
        JPanel containerDinamis = new JPanel();
        containerDinamis.setLayout(new GridLayout(0, 2, 15, 15));
        containerDinamis.setBackground(Color.WHITE);
        
        cardCatatan.removeAll();
        
        while (rs.next()) {
            CardCatatan card = new CardCatatan(
                rs.getString("dibuat_oleh"),
                rs.getString("isi_catatan"),
                rs.getString("tanggal"),
                rs.getString("judul")
            );
            
            // Set ID card
            String idCatatan = rs.getString("id_catatan");
            card.setCardId(idCatatan);
            
            // Tambahkan action listener untuk tombol hapus
            card.addHapusListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin menghapus catatan ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusCatatan(card.getCardId());
                }
            });
            
            containerDinamis.add(card);
        }
        
        JScrollPane scrollPane = new JScrollPane(containerDinamis);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        cardCatatan.setLayout(new BorderLayout());
        cardCatatan.add(scrollPane, BorderLayout.CENTER);
        cardCatatan.revalidate();
        cardCatatan.repaint();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
}

private void hapusCatatan(String idCatatan) {
    String sql = "DELETE FROM catatan_hasil WHERE id_catatan = ?";
    
    try (Connection conn = Koneksi.configDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        pst.setString(1, idCatatan);
        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, 
                "Catatan berhasil dihapus", 
                "Informasi", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh tampilan
            tampilkanCatatanPJ();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menghapus catatan", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Error database: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
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
        cardCatatan = new com.mycompany.tindaklanjutku.custom.panelCustom();
        roundedButton1 = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Catatan Admin");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setPreferredSize(new java.awt.Dimension(1111, 741));

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

        cardCatatan.setBackground(new java.awt.Color(78, 75, 209));

        javax.swing.GroupLayout cardCatatanLayout = new javax.swing.GroupLayout(cardCatatan);
        cardCatatan.setLayout(cardCatatanLayout);
        cardCatatanLayout.setHorizontalGroup(
            cardCatatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 725, Short.MAX_VALUE)
        );
        cardCatatanLayout.setVerticalGroup(
            cardCatatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );

        roundedButton1.setBackground(new java.awt.Color(0, 204, 0));
        roundedButton1.setText("Tambah Catatan");
        roundedButton1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        roundedButton1.setPressedBackgroundColor(new java.awt.Color(51, 255, 51));
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(cardCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(98, Short.MAX_VALUE))))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardItemActionPerformed
        new PJDashboard(username, idPJ).setVisible(true);
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

    }//GEN-LAST:event_tugasItem1ActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
        new uploadCatatan().setVisible(true);
    }//GEN-LAST:event_roundedButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(LogCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LogCatatan("username_default").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private com.mycompany.tindaklanjutku.custom.panelCustom cardCatatan;
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JButton dashboardItem;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton1;
    private javax.swing.JButton tugasItem1;
    // End of variables declaration//GEN-END:variables
}