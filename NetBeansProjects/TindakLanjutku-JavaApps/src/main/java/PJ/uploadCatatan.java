package PJ;

import com.mycompany.tindaklanjutku.tugas.loginForm;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import com.mycompany.tindaklanjutku.Koneksi;
import java.util.HashMap;
import java.util.Map;
import auth.SessionManager;

public class uploadCatatan extends javax.swing.JFrame {

    private Map<Integer, String> tugasMap = new HashMap<>(); // Untuk menyimpan id_tugas dan judul
    private String username;

    public uploadCatatan() {
        initComponents();
        this.username = SessionManager.getInstance().getUsername();
        loadTugasFromDatabase();
        
        // Add event listener for kirim button
        kirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kirimActionPerformed(evt);
            }
        });
    }

private void loadTugasFromDatabase() {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        conn = Koneksi.configDB();
        // Corrected query based on your database schema
        String query = "SELECT t.id_tugas, t.judul FROM tugas t " +
                     "JOIN penanggung_jawab pj ON t.id_pj = pj.id_pj " +
                     "JOIN user u ON pj.id_user = u.Id_usr " +
                     "WHERE u.namaUsr = ?";
        pst = conn.prepareStatement(query);
        pst.setString(1, username);
        rs = pst.executeQuery();
        
        tugasMap.clear();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        while (rs.next()) {
            int idTugas = rs.getInt("id_tugas");
            String judulTugas = rs.getString("judul");
            tugasMap.put(idTugas, judulTugas);
            model.addElement(judulTugas);
        }
        
        comboTugas.setModel(model);
        
    } catch (SQLException e) {
        System.err.println("Error loading tugas: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Gagal memuat data tugas: " + e.getMessage(), 
                "Error Database", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
    
    private int getSelectedTugasId() {
        String selectedJudul = (String) comboTugas.getSelectedItem();
        if (selectedJudul == null) return -1;
        
        for (Map.Entry<Integer, String> entry : tugasMap.entrySet()) {
            if (entry.getValue().equals(selectedJudul)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void kirimActionPerformed(java.awt.event.ActionEvent evt) {
        // Input validation
        String catatan = Catatan.getText().trim();
        if (catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan tidak boleh kosong", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (comboTugas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih nama tugas terlebih dahulu", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idTugas = getSelectedTugasId();
        if (idTugas == -1) {
            JOptionPane.showMessageDialog(this, "ID Tugas tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            conn = Koneksi.configDB();
            String query = "INSERT INTO catatan_hasil (isi_catatan, id_tugas, tanggal, dibuat_oleh) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, catatan);
            pst.setInt(2, idTugas);
            pst.setDate(3, sqlDate);
            pst.setString(4, username);
            
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Catatan berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                Catatan.setText("");
                loadTugasFromDatabase(); // Refresh data
                new LogCatatan(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan catatan", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error saving catatan: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Gagal menyimpan catatan: " + e.getMessage(), 
                    "Error Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // ... (rest of the generated GUI code remains exactly the same)
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Catatan = new javax.swing.JTextArea();
        LabelTugas = new javax.swing.JLabel();
        comboTugas = new javax.swing.JComboBox<>();
        kirim = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Upload Catatan");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setPreferredSize(new java.awt.Dimension(690, 690));
        panelCustom1.setRoundBottomLeft(8);
        panelCustom1.setRoundBottomRight(8);
        panelCustom1.setRoundTopLeft(8);
        panelCustom1.setRoundTopRight(8);

        panelCustom2.setBackground(new java.awt.Color(166, 164, 232));
        panelCustom2.setRoundBottomLeft(16);
        panelCustom2.setRoundBottomRight(16);
        panelCustom2.setRoundTopLeft(16);
        panelCustom2.setRoundTopRight(16);

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Berikan Catatan");

        Catatan.setBackground(new java.awt.Color(204, 204, 255));
        Catatan.setColumns(20);
        Catatan.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        Catatan.setForeground(new java.awt.Color(51, 51, 51));
        Catatan.setRows(5);
        jScrollPane1.setViewportView(Catatan);

        LabelTugas.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        LabelTugas.setForeground(new java.awt.Color(255, 255, 255));
        LabelTugas.setText("Nama Tugas: ");

        comboTugas.setBackground(new java.awt.Color(204, 204, 255));
        comboTugas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        kirim.setBackground(new java.awt.Color(51, 204, 0));
        kirim.setText("Kirim");
        kirim.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        kirim.setPressedBackgroundColor(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustom2Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addComponent(LabelTugas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboTugas, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel1))
                    .addGroup(panelCustom2Layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(kirim, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelTugas)
                    .addComponent(comboTugas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(kirim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(uploadCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(uploadCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(uploadCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(uploadCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new uploadCatatan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Catatan;
    private javax.swing.JLabel LabelTugas;
    private javax.swing.JComboBox<String> comboTugas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.mycompany.tindaklanjutku.custom.RoundedButton kirim;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    // End of variables declaration//GEN-END:variables
}