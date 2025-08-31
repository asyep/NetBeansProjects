/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PJ;

import auth.SessionManager;
import com.mycompany.tindaklanjutku.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author ASUS VIVO
 */
public class distribusiTugas extends javax.swing.JFrame {

    private Integer idPJ;
    private String username;
    private Connection connection;

    public distribusiTugas(Integer idPJ) throws SQLException {
        try {
            this.username = SessionManager.getInstance().getUsername();
            this.idPJ = SessionManager.getInstance().getIdPJ();
            this.connection = Koneksi.configDB();
            initComponents();
            loadNamaComboBox();
            if (comboNama.getItemCount() > 0) {
                loadTugasComboBox();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(distribusiTugas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadNamaComboBox() {
        try {
            // Query untuk mengambil nama user yang satu divisi dengan PJ
            String query = "SELECT u.namaUsr FROM user u "
                    + "JOIN penanggung_jawab pj ON u.id_divisi = pj.id_divisi "
                    + "WHERE pj.id_pj = ? AND u.Id_usr != pj.id_user";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idPJ);

            ResultSet rs = stmt.executeQuery();

            List<String> namaList = new ArrayList<>();
            while (rs.next()) {
                namaList.add(rs.getString("namaUsr"));
            }

            comboNama.setModel(new javax.swing.DefaultComboBoxModel<>(namaList.toArray(new String[0])));

            if (namaList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada anggota dalam divisi Anda", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                try {
                    new distribusiTugas(idPJ).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(distribusiTugas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat daftar nama: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean hasAvailableTugas(int idPJ) {
        try (Connection conn = Koneksi.configDB(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM tugas t "
                + "WHERE t.id_pj = ? AND t.id_tugas NOT IN (SELECT id_tugas FROM tugas_user) LIMIT 1"
        )) {
            stmt.setInt(1, idPJ);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // True kalau masih ada tugas yang belum didistribusikan
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Anggap tidak tersedia kalau error
        }
    }

    private void loadTugasComboBox() {
        try {
            if (comboNama.getSelectedItem() == null) {
                return;
            }

            // Query untuk mengambil tugas yang dibuat oleh PJ ini dan belum didistribusikan
            String query = "SELECT t.judul FROM tugas t "
                    + "WHERE t.id_pj = ? AND t.id_tugas NOT IN "
                    + "(SELECT id_tugas FROM tugas_user)";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idPJ);

            ResultSet rs = stmt.executeQuery();

            List<String> tugasList = new ArrayList<>();
            while (rs.next()) {
                tugasList.add(rs.getString("judul"));
            }

            comboTugas1.setModel(new javax.swing.DefaultComboBoxModel<>(tugasList.toArray(new String[0])));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat daftar tugas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboNama = new javax.swing.JComboBox<>();
        SaveD = new com.mycompany.tindaklanjutku.custom.RoundedButton();
        comboTugas1 = new javax.swing.JComboBox<>();
        roundedButton1 = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Distribusi Tugas");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setRoundBottomLeft(8);
        panelCustom1.setRoundBottomRight(8);
        panelCustom1.setRoundTopLeft(8);
        panelCustom1.setRoundTopRight(8);

        panelCustom2.setBackground(new java.awt.Color(204, 204, 255));
        panelCustom2.setRoundBottomLeft(16);
        panelCustom2.setRoundBottomRight(16);
        panelCustom2.setRoundTopLeft(16);
        panelCustom2.setRoundTopRight(16);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 255));
        jLabel1.setText("Berika Tugas ke:");

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 255));
        jLabel2.setText("Nama   :");

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 255));
        jLabel3.setText("Tugas   :");

        comboNama.setBackground(new java.awt.Color(255, 255, 255));
        comboNama.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        comboNama.setForeground(new java.awt.Color(102, 102, 255));
        comboNama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNamaActionPerformed(evt);
            }
        });

        SaveD.setBackground(new java.awt.Color(102, 102, 255));
        SaveD.setText("Simpan");
        SaveD.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        SaveD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveDActionPerformed(evt);
            }
        });

        comboTugas1.setBackground(new java.awt.Color(255, 255, 255));
        comboTugas1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        comboTugas1.setForeground(new java.awt.Color(102, 102, 255));
        comboTugas1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        roundedButton1.setBackground(new java.awt.Color(255, 102, 102));
        roundedButton1.setText("Kembali");
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jLabel1))
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SaveD, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(comboNama, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboTugas1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(107, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustom2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(175, 175, 175))
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addComponent(comboNama, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(comboTugas1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel2)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel3)))
                .addGap(39, 39, 39)
                .addComponent(SaveD, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(161, Short.MAX_VALUE))
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

    private void SaveDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveDActionPerformed
        // Validasi pilihan
        if (comboNama.getSelectedItem() == null || comboTugas1.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Harap pilih nama dan tugas terlebih dahulu", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedNama = comboNama.getSelectedItem().toString();
        String selectedTugas = comboTugas1.getSelectedItem().toString();

        try {
            // 1. Dapatkan ID user berdasarkan nama
            String getUserQuery = "SELECT id_usr FROM user WHERE namaUsr = ?";
            PreparedStatement getUserStmt = connection.prepareStatement(getUserQuery);
            getUserStmt.setString(1, selectedNama);
            ResultSet userRs = getUserStmt.executeQuery();

            // 2. Dapatkan ID tugas berdasarkan judul
            String getTugasQuery = "SELECT id_tugas FROM tugas WHERE judul = ? AND id_pj = ?";
            PreparedStatement getTugasStmt = connection.prepareStatement(getTugasQuery);
            getTugasStmt.setString(1, selectedTugas);
            getTugasStmt.setInt(2, idPJ);
            ResultSet tugasRs = getTugasStmt.executeQuery();

            if (userRs.next() && tugasRs.next()) {
                int userId = userRs.getInt("id_usr");
                int tugasId = tugasRs.getInt("id_tugas");

                // 3. Cek apakah tugas sudah pernah didistribusikan ke user ini
                String checkQuery = "SELECT COUNT(*) FROM tugas_user WHERE id_user = ? AND id_tugas = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, tugasId);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Tugas ini sudah didistribusikan ke user tersebut", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    loadTugasComboBox();
                }

                // 4. Simpan distribusi tugas
                String insertQuery = "INSERT INTO tugas_user (id_user, id_tugas) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, tugasId);

                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Tugas berhasil didistribusikan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh daftar tugas setelah berhasil disimpan
                    loadTugasComboBox();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mendistribusikan tugas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Data user atau tugas tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_SaveDActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
        new daftarTugas(idPJ, username).setVisible(true);
        dispose();
    }//GEN-LAST:event_roundedButton1ActionPerformed

    private void comboNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboNamaActionPerformed

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
            java.util.logging.Logger.getLogger(distribusiTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(distribusiTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(distribusiTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(distribusiTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            try {
//                new distribusiTugas(3).setVisible(true);
//            } catch (SQLException ex) {
//                Logger.getLogger(distribusiTugas.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mycompany.tindaklanjutku.custom.RoundedButton SaveD;
    private javax.swing.JComboBox<String> comboNama;
    private javax.swing.JComboBox<String> comboTugas1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton1;
    // End of variables declaration//GEN-END:variables
}
