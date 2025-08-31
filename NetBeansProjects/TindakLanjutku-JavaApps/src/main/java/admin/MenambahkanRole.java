/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import com.mycompany.tindaklanjutku.Koneksi;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ASUS VIVO
 */
public class MenambahkanRole extends javax.swing.JFrame {

    private Map<Integer, String> divisiMap = new HashMap<>();
    private String username;

    public MenambahkanRole(String username) {
        this.username = username;
        initComponents();
        loadDivisiData(); // Load data divisi terlebih dahulu
        loadUserData();
    }

    private void loadDivisiData() {
        try (Connection conn = Koneksi.configDB()) {
            PreparedStatement pst = conn.prepareStatement("SELECT id_divisi, nama_divisi FROM divisi");
            ResultSet rs = pst.executeQuery();

            divisiMap.clear();
            while (rs.next()) {
                divisiMap.put(rs.getInt("id_divisi"), rs.getString("nama_divisi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading divisi data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserData() {
        try (Connection conn = Koneksi.configDB()) {
            // Query dengan JOIN ke tabel divisi
            String query = "SELECT u.namaUsr, u.email, u.role, u.id_divisi, d.nama_divisi "
                    + "FROM user u LEFT JOIN divisi d ON u.id_divisi = d.id_divisi";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 2 || column == 4; // Kolom Role dan Divisi (tampilan) bisa di-edit
                }
            };

            model.addColumn("Nama User");
            model.addColumn("Email");
            model.addColumn("Role");
            model.addColumn("id_divisi");   // Kolom tersembunyi (index 3)
            model.addColumn("Divisi");      // Kolom tampilan (index 4)

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("namaUsr"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getObject("id_divisi"), // Bisa null
                    rs.getString("nama_divisi") // Bisa null
                });
            }

            daftarUsr.setModel(model);

            // Sembunyikan kolom id_divisi (kolom index 3)
            daftarUsr.removeColumn(daftarUsr.getColumnModel().getColumn(3));

            // Lebar kolom
            TableColumnModel columnModel = daftarUsr.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200); // Nama User
            columnModel.getColumn(1).setPreferredWidth(250); // Email
            columnModel.getColumn(2).setPreferredWidth(100); // Role
            columnModel.getColumn(3).setPreferredWidth(150); // Divisi (tampilan) - sekarang index 3 setelah remove

            // Tengah semua kolom
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            for (int i = 0; i < daftarUsr.getColumnCount(); i++) {
                daftarUsr.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Editor combobox untuk kolom Role
            JComboBox<String> roleCombobox = new JComboBox<>(new String[]{"anggota", "pj", "admin"});
            DefaultCellEditor roleEditor = new DefaultCellEditor(roleCombobox);
            daftarUsr.getColumnModel().getColumn(2).setCellEditor(roleEditor);

            // Editor combobox untuk kolom Divisi
            JComboBox<String> divisiComboBox = new JComboBox<>();
            divisiComboBox.addItem(""); // Untuk nilai null
            for (String namaDivisi : divisiMap.values()) {
                divisiComboBox.addItem(namaDivisi);
            }
            DefaultCellEditor divisiEditor = new DefaultCellEditor(divisiComboBox);
            daftarUsr.getColumnModel().getColumn(3).setCellEditor(divisiEditor); // Index 3 setelah remove

            // Listener untuk perubahan data
            daftarUsr.getModel().addTableModelListener(e -> {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    String email = (String) daftarUsr.getValueAt(row, 1);

                    if (col == 2) { // Kolom Role
                        String newRole = (String) daftarUsr.getValueAt(row, 2);
                        updateUserRole(email, newRole);
                    } else if (col == 4) { // Kolom Divisi tampilan di model (index 4 di underlying model)
                        // Ambil nilai divisi yang dipilih dari tampilan table
                        String selectedDivisi = (String) daftarUsr.getValueAt(row, 3); // Index 3 di tampilan

                        // Dapatkan id_divisi yang sesuai dengan nama divisi
                        Integer idDivisi = null;
                        if (selectedDivisi != null && !selectedDivisi.trim().isEmpty()) {
                            idDivisi = getDivisiIdByName(selectedDivisi);
                            if (idDivisi == -1) {
                                idDivisi = null; // Jika tidak ditemukan, set null
                            }
                        }

                        // Update nilai di underlying model (kolom tersembunyi)
                        DefaultTableModel tableModel = (DefaultTableModel) daftarUsr.getModel();
                        tableModel.setValueAt(idDivisi, row, 3); // Update kolom id_divisi di underlying model

                        updateUserDivisi(email, idDivisi);
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDivisiIdByName(String namaDivisi) {
        if (namaDivisi == null || namaDivisi.trim().isEmpty()) {
            return -1;
        }

        for (Map.Entry<Integer, String> entry : divisiMap.entrySet()) {
            if (entry.getValue().equals(namaDivisi)) {
                return entry.getKey();
            }
        }
        return -1; // Tidak ditemukan
    }

    private void updateUserRole(String email, String newRole) {
        try (Connection conn = Koneksi.configDB()) {
            conn.setAutoCommit(false); // Mulai transaction

            try {
                // 1. Update role di tabel user
                PreparedStatement updateUser = conn.prepareStatement(
                        "UPDATE user SET role = ? WHERE email = ?");
                updateUser.setString(1, newRole);
                updateUser.setString(2, email);
                updateUser.executeUpdate();

                // 2. Jika role baru adalah "pj", tambahkan ke tabel penanggung_jawab
                if ("pj".equalsIgnoreCase(newRole)) {
                    // Cek apakah user sudah ada di tabel penanggung_jawab
                    PreparedStatement checkPJ = conn.prepareStatement(
                            "SELECT id_user FROM penanggung_jawab WHERE id_user = (SELECT id_usr FROM user WHERE email = ?)");
                    checkPJ.setString(1, email);
                    ResultSet rs = checkPJ.executeQuery();

                    if (!rs.next()) { // Jika belum ada, ambil id_divisi dari user dan insert
                        // Ambil id_divisi dari tabel user
                        PreparedStatement getUserDivisi = conn.prepareStatement(
                                "SELECT id_divisi FROM user WHERE email = ?");
                        getUserDivisi.setString(1, email);
                        ResultSet rsDivisi = getUserDivisi.executeQuery();

                        if (rsDivisi.next()) {
                            int idDivisi = rsDivisi.getInt("id_divisi");

                            // Insert ke penanggung_jawab dengan id_divisi yang sama
                            PreparedStatement insertPJ = conn.prepareStatement(
                                    "INSERT INTO penanggung_jawab (id_user, id_divisi) VALUES ((SELECT id_usr FROM user WHERE email = ?), ?)");
                            insertPJ.setString(1, email);
                            insertPJ.setInt(2, idDivisi);
                            insertPJ.executeUpdate();
                        } else {
                            throw new SQLException("User tidak memiliki id_divisi yang valid!");
                        }
                    }
                } else {
                    // Jika role diubah dari "pj" ke role lain, hapus dari tabel penanggung_jawab
                    PreparedStatement deletePJ = conn.prepareStatement(
                            "DELETE FROM penanggung_jawab WHERE id_user = (SELECT id_usr FROM user WHERE email = ?)");
                    deletePJ.setString(1, email);
                    deletePJ.executeUpdate();
                }

                conn.commit(); // Commit jika semua berhasil
                JOptionPane.showMessageDialog(this, "Role berhasil diupdate!");

            } catch (SQLException e) {
                conn.rollback(); // Rollback jika error
                JOptionPane.showMessageDialog(this,
                        "Gagal mengupdate role: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Koneksi database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUserDivisi(String email, Integer idDivisi) {
        try (Connection conn = Koneksi.configDB()) {
            PreparedStatement pst = conn.prepareStatement(
                    "UPDATE user SET id_divisi = ? WHERE email = ?");

            if (idDivisi != null && idDivisi > 0) {
                pst.setInt(1, idDivisi);
            } else {
                pst.setNull(1, java.sql.Types.INTEGER);
            }

            pst.setString(2, email);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Divisi berhasil diupdate!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate divisi: User tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengupdate divisi: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustom1 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        panelCustom2 = new com.mycompany.tindaklanjutku.custom.panelCustom();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        Dashboard = new javax.swing.JButton();
        daftarUser = new javax.swing.JButton();
        DivisiItem2 = new javax.swing.JButton();
        catatanKerjaItem2 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        tugasItem3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        daftarUsr = new com.mycompany.tindaklanjutku.custom.CustomTable();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        roundedButton2 = new com.mycompany.tindaklanjutku.custom.RoundedButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TindakLanjutKu | Admin");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
        panelCustom1.setPreferredSize(new java.awt.Dimension(1111, 741));
        panelCustom1.setRoundBottomLeft(8);
        panelCustom1.setRoundBottomRight(8);
        panelCustom1.setRoundTopLeft(8);
        panelCustom1.setRoundTopRight(8);

        panelCustom2.setBackground(new java.awt.Color(78, 75, 209));
        panelCustom2.setRoundBottomLeft(8);
        panelCustom2.setRoundTopLeft(8);

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/list.png"))); // NOI18N
        jLabel6.setText("Tindak Lanjutku");

        jSeparator4.setForeground(new java.awt.Color(204, 204, 204));

        Dashboard.setBackground(new java.awt.Color(78, 75, 209));
        Dashboard.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        Dashboard.setForeground(new java.awt.Color(255, 255, 255));
        Dashboard.setText("Dashboard");
        Dashboard.setBorder(null);
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });

        daftarUser.setBackground(new java.awt.Color(78, 75, 209));
        daftarUser.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        daftarUser.setForeground(new java.awt.Color(255, 255, 255));
        daftarUser.setText("Daftar User");
        daftarUser.setBorder(null);
        daftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daftarUserActionPerformed(evt);
            }
        });

        DivisiItem2.setBackground(new java.awt.Color(78, 75, 209));
        DivisiItem2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        DivisiItem2.setForeground(new java.awt.Color(255, 255, 255));
        DivisiItem2.setText("Divisi");
        DivisiItem2.setBorder(null);
        DivisiItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DivisiItem2ActionPerformed(evt);
            }
        });

        catatanKerjaItem2.setBackground(new java.awt.Color(78, 75, 209));
        catatanKerjaItem2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        catatanKerjaItem2.setForeground(new java.awt.Color(255, 255, 255));
        catatanKerjaItem2.setText("Catatan Kerja");
        catatanKerjaItem2.setBorder(null);
        catatanKerjaItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catatanKerjaItem2ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 51, 51));
        jButton8.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Logout");
        jButton8.setBorder(null);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        tugasItem3.setBackground(new java.awt.Color(78, 75, 209));
        tugasItem3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tugasItem3.setForeground(new java.awt.Color(255, 255, 255));
        tugasItem3.setText("Tugas");
        tugasItem3.setBorder(null);
        tugasItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tugasItem3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom2Layout = new javax.swing.GroupLayout(panelCustom2);
        panelCustom2.setLayout(panelCustom2Layout);
        panelCustom2Layout.setHorizontalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
            .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(daftarUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(DivisiItem2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(catatanKerjaItem2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tugasItem3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelCustom2Layout.setVerticalGroup(
            panelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tugasItem3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(daftarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DivisiItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(daftarUsr);

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Daftar User");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tugas");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Anggota");

        roundedButton2.setBackground(new java.awt.Color(255, 51, 51));
        roundedButton2.setText("Hapus User");
        roundedButton2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedButton2.setHoverBackgroundColor(new java.awt.Color(255, 153, 153));
        roundedButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCustom1Layout.createSequentialGroup()
                            .addGap(688, 688, 688)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelCustom1Layout.createSequentialGroup()
                            .addGap(59, 59, 59)
                            .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustom1Layout.createSequentialGroup()
                            .addGap(65, 65, 65)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(roundedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCustom1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void roundedButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton2ActionPerformed
        int selectedRow = daftarUsr.getSelectedRow();
        if (selectedRow != -1) {
            String email = daftarUsr.getValueAt(selectedRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Koneksi.configDB()) {
                    PreparedStatement pst = conn.prepareStatement("DELETE FROM user WHERE email = ?");
                    pst.setString(1, email);
                    pst.executeUpdate();
                    ((DefaultTableModel) daftarUsr.getModel()).removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih user yang ingin dihapus.");
        }

    }//GEN-LAST:event_roundedButton2ActionPerformed

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

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        new AdminDashboard(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void tugasItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tugasItem3ActionPerformed
        try {
            new tugas(username).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(MenambahkanRole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tugasItem3ActionPerformed

    private void daftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daftarUserActionPerformed
        new MenambahkanRole(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_daftarUserActionPerformed

    private void DivisiItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DivisiItem2ActionPerformed
        new uploadDivisi(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_DivisiItem2ActionPerformed

    private void catatanKerjaItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catatanKerjaItem2ActionPerformed
        new CatatanAdmin(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_catatanKerjaItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(MenambahkanRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenambahkanRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenambahkanRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenambahkanRole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenambahkanRole("admin").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DivisiItem2;
    private javax.swing.JButton catatanKerjaItem2;
    private javax.swing.JButton daftarUser;
    private com.mycompany.tindaklanjutku.custom.CustomTable daftarUsr;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton2;
    private javax.swing.JButton tugasItem3;
    // End of variables declaration//GEN-END:variables
}
