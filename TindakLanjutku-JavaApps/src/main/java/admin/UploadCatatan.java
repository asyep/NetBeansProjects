package admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import com.mycompany.tindaklanjutku.Koneksi;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import admin.UserSession;
import auth.SessionManager;


public class UploadCatatan extends javax.swing.JFrame {

    private Connection conn;
    private String username;
    private Map<Integer, String> tugasMap = new HashMap<>(); // Untuk menyimpan id_tugas dan judul

    public UploadCatatan(String username) {
        this.username = username;
        initComponents();
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
            String query = "SELECT id_tugas, judul FROM tugas";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            
            tugasMap.clear();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            while (rs.next()) {
                int idTugas = rs.getInt("id_tugas");
                String judulTugas = rs.getString("judul");
                tugasMap.put(idTugas, judulTugas);
                model.addElement(judulTugas);
            }
            
            jComboBox1.setModel(model);
            
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
        String selectedJudul = (String) jComboBox1.getSelectedItem();
        if (selectedJudul == null) return -1;
        
        return tugasMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(selectedJudul))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    private void kirimActionPerformed(java.awt.event.ActionEvent evt) {
        // Input validation
        String catatan = jTextArea1.getText().trim();
        if (catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan tidak boleh kosong", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (jComboBox1.getSelectedItem() == null) {
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
            String query = "INSERT INTO catatan_hasil (isi_catatan, id_tugas, tanggal,dibuat_oleh) VALUES (?, ?, ?,?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, catatan);
            pst.setInt(2, idTugas);
            pst.setDate(3, sqlDate);
            String username = SessionManager.getInstance().getUsername();
            pst.setString(4, username);
            
            
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Catatan berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                jTextArea1.setText("");
                loadTugasFromDatabase(); // Refresh data
                new CatatanAdmin(username).setVisible(true);
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

    // Generated UI code remains the same
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        kirim = new com.mycompany.tindaklanjutku.custom.RoundedButton();
    
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
        panelCustom2.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom2.setPreferredSize(new java.awt.Dimension(690, 690));
    
        panelCustom1.setBackground(new java.awt.Color(166, 164, 232));
        panelCustom1.setRoundBottomLeft(16);
        panelCustom1.setRoundBottomRight(16);
        panelCustom1.setRoundTopLeft(16);
        panelCustom1.setRoundTopRight(16);
    
        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Berikan Catatan");
    
        jTextArea1.setBackground(new java.awt.Color(204, 204, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(51, 51, 51));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
    
        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nama Tugas: ");
    
        jComboBox1.setBackground(new java.awt.Color(204, 204, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    
        kirim.setBackground(new java.awt.Color(78, 75, 209));
        kirim.setForeground(new java.awt.Color(255, 255, 255));
        kirim.setText("Kirim");
        kirim.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
    
        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustom1Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel1))
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(kirim, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(kirim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );
    
        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );
    
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    
        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UploadCatatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new UploadCatatan("admin").setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private com.mycompany.tindaklanjutku.custom.RoundedButton kirim;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
}
