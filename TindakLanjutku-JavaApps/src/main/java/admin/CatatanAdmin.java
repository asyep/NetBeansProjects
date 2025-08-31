/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CatatanAdmin extends javax.swing.JFrame {

    private String username;
    public CatatanAdmin(String username) {
        this.username = username;
        initComponents();
        tampilkanCatatan();
    }
    
    public panelCustom getCardCatatan() {
        return cardCatatan;
    }
    
    

public void tampilkanCatatan() {
    String sql = "SELECT c.*, t.judul FROM catatan_hasil c JOIN tugas t ON c.id_tugas = t.id_tugas";
    try (Connection conn = Koneksi.configDB();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {
        
        // Hitung jumlah catatan
        List<CardCatatan> cards = new ArrayList<>();
        while (rs.next()) {
            CardCatatan card = new CardCatatan(
                rs.getString("dibuat_oleh"),
                rs.getString("isi_catatan"),
                rs.getString("tanggal"),
                rs.getString("judul")
            );
            
            String idCatatan = rs.getString("id_catatan");
            card.setCardId(idCatatan);
            
            card.addHapusListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "Apakah Anda yakin ingin menghapus catatan ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    hapusCatatan(e.getActionCommand()); // Menggunakan ID dari action command
                    // Refresh tampilan setelah hapus
                    SwingUtilities.invokeLater(this::tampilkanCatatan);
                }
            });
            
            cards.add(card);
        }
        

        
        // Panel utama dengan grid 2 kolom
        JPanel containerDinamis = new JPanel();
        int rows = (int) Math.ceil(cards.size() / 2.0);
        containerDinamis.setLayout(new GridLayout(rows, 2, 15, 15)); // 2 kolom, spasi 15px
        
        // Atur background putih dan hapus border
        containerDinamis.setBackground(Color.WHITE);
        containerDinamis.setBorder(null); // Menghilangkan border
        
        if (cardCatatan.getComponentCount() > 0) {
            cardCatatan.removeAll();
        }

        cardCatatan.setLayout(new BorderLayout());
        
        for (CardCatatan card : cards) {
            containerDinamis.add(card);
        }
        
        // JScrollPane juga bisa diatur backgroundnya
        JScrollPane scrollPane = new JScrollPane(containerDinamis);
        scrollPane.setBorder(null); // Menghilangkan border scroll pane
        scrollPane.getViewport().setBackground(Color.WHITE); // Background viewport putih
        
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
        
        // Set parameter ID catatan yang akan dihapus
        pst.setString(1, idCatatan);
        
        // Eksekusi query
        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, 
                "Catatan berhasil dihapus", 
                "Informasi", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh tampilan setelah penghapusan
            tampilkanCatatan();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menghapus catatan. Data tidak ditemukan.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Error saat menghapus catatan: " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new com.mycompany.tindaklanjutku.custom.panelCustom();
        sidebar = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        dashboardItem = new javax.swing.JButton();
        pjItem = new javax.swing.JButton();
        DivisiItem = new javax.swing.JButton();
        catatanKerjaItem = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        tugasItem1 = new javax.swing.JButton();
        cardCatatan = new com.mycompany.tindaklanjutku.custom.panelCustom();
        roundedButton1 = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Catatan Admin");

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setPreferredSize(new java.awt.Dimension(1111, 741));
        mainPanel.setRoundBottomLeft(8);
        mainPanel.setRoundBottomRight(8);
        mainPanel.setRoundTopLeft(8);
        mainPanel.setRoundTopRight(8);

        sidebar.setBackground(new java.awt.Color(78, 75, 209));
        sidebar.setMaximumSize(new java.awt.Dimension(253, 741));
        sidebar.setMinimumSize(new java.awt.Dimension(253, 741));
        sidebar.setPreferredSize(new java.awt.Dimension(253, 0));
        sidebar.setRoundBottomLeft(8);
        sidebar.setRoundTopLeft(8);

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
        pjItem.setText("Daftar User");
        pjItem.setBorder(null);
        pjItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pjItemActionPerformed(evt);
            }
        });

        DivisiItem.setBackground(new java.awt.Color(78, 75, 209));
        DivisiItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        DivisiItem.setForeground(new java.awt.Color(255, 255, 255));
        DivisiItem.setText("Divisi");
        DivisiItem.setBorder(null);
        DivisiItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DivisiItemActionPerformed(evt);
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

        tugasItem1.setBackground(new java.awt.Color(78, 75, 209));
        tugasItem1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tugasItem1.setForeground(new java.awt.Color(255, 255, 255));
        tugasItem1.setText("Tugas");
        tugasItem1.setBorder(null);
        tugasItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tugasItem1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
            .addComponent(dashboardItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pjItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(DivisiItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(catatanKerjaItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tugasItem1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidebarLayout.setVerticalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
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
                .addComponent(DivisiItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        cardCatatan.setBackground(new java.awt.Color(102, 102, 255));
        cardCatatan.setPreferredSize(new java.awt.Dimension(1111, 421));

        javax.swing.GroupLayout cardCatatanLayout = new javax.swing.GroupLayout(cardCatatan);
        cardCatatan.setLayout(cardCatatanLayout);
        cardCatatanLayout.setHorizontalGroup(
            cardCatatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 805, Short.MAX_VALUE)
        );
        cardCatatanLayout.setVerticalGroup(
            cardCatatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
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

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cardCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 805, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cardCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pjItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pjItemActionPerformed
        new MenambahkanRole(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_pjItemActionPerformed

    private void DivisiItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DivisiItemActionPerformed
        new uploadDivisi(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_DivisiItemActionPerformed

    private void catatanKerjaItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catatanKerjaItemActionPerformed
        new CatatanAdmin(username).setVisible(true);
        dispose(); // Close current window after opening new one
    }//GEN-LAST:event_catatanKerjaItemActionPerformed

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

    private void dashboardItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardItemActionPerformed
        new AdminDashboard(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_dashboardItemActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
        new UploadCatatan(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_roundedButton1ActionPerformed

    private void tugasItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tugasItem1ActionPerformed
        try {
            new tugas(username).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(CatatanAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            java.util.logging.Logger.getLogger(CatatanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CatatanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CatatanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CatatanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Ganti "admin" dengan username default atau ambil dari input jika perlu
                new CatatanAdmin("admin").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DivisiItem;
    private com.mycompany.tindaklanjutku.custom.panelCustom cardCatatan;
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JButton dashboardItem;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private com.mycompany.tindaklanjutku.custom.panelCustom mainPanel;
    private javax.swing.JButton pjItem;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton1;
    private com.mycompany.tindaklanjutku.custom.panelCustom sidebar;
    private javax.swing.JButton tugasItem1;
    // End of variables declaration//GEN-END:variables
}
