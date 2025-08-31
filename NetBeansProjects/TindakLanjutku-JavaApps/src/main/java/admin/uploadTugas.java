/*
    * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
    * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import admin.tugas;
import com.mycompany.tindaklanjutku.Koneksi;
import com.mycompany.tindaklanjutku.tugas.loginForm;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS VIVO
 */
public class uploadTugas extends javax.swing.JFrame {

    private String username;

    public uploadTugas(String username) {
        this.username = username;
        initComponents();
        loadPJ();
    }

    class pjItem {

        private int id;
        private String nama;

        public pjItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    private void loadPJ() {
        try {
            Connection conn = Koneksi.configDB();
            String sql = "SELECT pj.id_pj, u.namaUsr "
                    + "FROM penanggung_jawab pj "
                    + "JOIN user u ON pj.id_user = u.Id_usr";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultComboBoxModel model = new DefaultComboBoxModel();
            model.addElement("Pilih Penanggung Jawab");

            while (rs.next()) {
                int idPj = rs.getInt("id_pj");
                String nama = rs.getString("namaUsr");
                model.addElement(new pjItem(idPj, nama)); // Tambah item ke combo box
            }

            comboBoxPJ.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat Penanggung Jawab");
        }

    }

    private void uTugas() throws SQLException {
        String namaTugas = judul.getText().trim();
        String desk = deskripsi.getText().trim();
        java.util.Date tanggal = date.getDate();
        String kategoriStr = kategori1.getText().trim();

        Connection conn = Koneksi.configDB();
        String namaPjDipilih = comboBoxPJ.getSelectedItem().toString();
        String sqlIdUser = "SELECT * FROM user WHERE namaUsr = ?";
        PreparedStatement pstIDUSER = conn.prepareStatement(sqlIdUser);
        pstIDUSER.setString(1, namaPjDipilih);
        ResultSet rsIDUSER = pstIDUSER.executeQuery();

        Integer idUser = null;
        if (rsIDUSER.next()) {
            idUser = rsIDUSER.getInt("id_usr");
        } else {
            JOptionPane.showMessageDialog(this, "User tidak ditemukan!");
            return;
        }

        // Validasi input
        if (namaTugas.isEmpty() || desk.isEmpty() || tanggal == null || kategoriStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        if (comboBoxPJ.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih Penanggung Jawab!");
            return;
        }

        try {
            java.sql.Date sqlDeadline = new java.sql.Date(tanggal.getTime());
            pjItem selectedPJ = (pjItem) comboBoxPJ.getSelectedItem();

            try (conn) {
                conn.setAutoCommit(false); // mulai transaksi

                // 1. Cek apakah kategori sudah ada
                String cekKategori = "SELECT id_kategori FROM kategori WHERE nama_kategori = ?";
                PreparedStatement pstCek = conn.prepareStatement(cekKategori);
                pstCek.setString(1, kategoriStr);
                ResultSet rs = pstCek.executeQuery();

                int idKategori;
                if (rs.next()) {
                    // kategori sudah ada
                    idKategori = rs.getInt("id_kategori");
                } else {
                    // kategori belum ada, insert baru
                    String insertKategori = "INSERT INTO kategori (nama_kategori) VALUES (?)";
                    PreparedStatement pstInsertKat = conn.prepareStatement(insertKategori, Statement.RETURN_GENERATED_KEYS);
                    pstInsertKat.setString(1, kategoriStr);
                    pstInsertKat.executeUpdate();

                    ResultSet rsKeys = pstInsertKat.getGeneratedKeys();
                    if (rsKeys.next()) {
                        idKategori = rsKeys.getInt(1);
                    } else {
                        throw new SQLException("Gagal mendapatkan ID kategori baru.");
                    }
                }

                // 2. Insert tugas menggunakan idKategori
                String insertTugas = "INSERT INTO tugas (judul, deskripsi, deadline, id_pj, id_kategori, id_user) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(insertTugas);
                pst.setString(1, namaTugas);
                pst.setString(2, desk);
                pst.setDate(3, sqlDeadline);
                pst.setInt(4, selectedPJ.getId());
                pst.setInt(5, idKategori);
                pst.setInt(6, idUser);
                pst.executeUpdate();

                conn.commit(); // simpan transaksi

                JOptionPane.showMessageDialog(this, "Tugas berhasil diunggah!");
                // Reset form
                judul.setText("");
                deskripsi.setText("");
                date.setDate(null);
                comboBoxPJ.setSelectedIndex(0);
                kategori1.setText("");
                new tugas(username).setVisible(true);
                dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan tugas: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
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
        judul = new com.mycompany.tindaklanjutku.custom.RoundedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        date = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        roundedButton1 = new com.mycompany.tindaklanjutku.custom.RoundedButton();
        comboBoxPJ = new javax.swing.JComboBox<>();
        deskripsi = new com.mycompany.tindaklanjutku.custom.RoundedTextField();
        jLabel8 = new javax.swing.JLabel();
        kategori1 = new com.mycompany.tindaklanjutku.custom.RoundedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Upload Tugas");

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));

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
        tugasItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tugasItemActionPerformed(evt);
            }
        });

        pjItem.setBackground(new java.awt.Color(78, 75, 209));
        pjItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        pjItem.setForeground(new java.awt.Color(255, 255, 255));
        pjItem.setText("Daftar user");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Upload Tugas");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Anggota");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Nama Tugas");

        judul.setBackground(new java.awt.Color(255, 255, 255));
        judul.setForeground(new java.awt.Color(51, 51, 51));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Kategori");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Penanggung Jawab");

        date.setBackground(new java.awt.Color(255, 255, 255));
        date.setForeground(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Pilih Deadline");

        roundedButton1.setBackground(new java.awt.Color(78, 75, 209));
        roundedButton1.setText("Simpan");
        roundedButton1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        roundedButton1.setHoverBackgroundColor(new java.awt.Color(102, 102, 255));
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });

        comboBoxPJ.setBackground(new java.awt.Color(255, 255, 255));
        comboBoxPJ.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        comboBoxPJ.setForeground(new java.awt.Color(51, 51, 51));
        comboBoxPJ.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4", "item 5", " " }));
        comboBoxPJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxPJActionPerformed(evt);
            }
        });

        deskripsi.setBackground(new java.awt.Color(255, 255, 255));
        deskripsi.setForeground(new java.awt.Color(51, 51, 51));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Deskripsi");

        kategori1.setBackground(new java.awt.Color(255, 255, 255));
        kategori1.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
                        .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(336, 336, 336))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCustom1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(266, 266, 266)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelCustom1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(429, 429, 429)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(282, 282, 282)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(comboBoxPJ, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelCustom1Layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(590, 590, 590))
                                .addGroup(panelCustom1Layout.createSequentialGroup()
                                    .addComponent(kategori1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(45, 45, 45)
                                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(29, 29, 29)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(comboBoxPJ))
                .addGap(18, 18, 18)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kategori1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(panelCustom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed
            try {
                uTugas();
            } catch (SQLException ex) {
                Logger.getLogger(uploadTugas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//GEN-LAST:event_roundedButton1ActionPerformed

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

    private void dashboarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboarItemActionPerformed
        new AdminDashboard(username).setVisible(true);
        dispose();
    }//GEN-LAST:event_dashboarItemActionPerformed

    private void tugasItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tugasItemActionPerformed
        try {
            new tugas(username).setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(uploadTugas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tugasItemActionPerformed

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

    private void comboBoxPJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPJActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxPJActionPerformed

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
            java.util.logging.Logger.getLogger(uploadTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(uploadTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(uploadTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(uploadTugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new uploadTugas("admin").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton catatanKerjaItem;
    private javax.swing.JComboBox<String> comboBoxPJ;
    private javax.swing.JButton dashboarItem;
    private com.toedter.calendar.JDateChooser date;
    private com.mycompany.tindaklanjutku.custom.RoundedTextField deskripsi;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private com.mycompany.tindaklanjutku.custom.RoundedTextField judul;
    private com.mycompany.tindaklanjutku.custom.RoundedTextField kategori1;
    private javax.swing.JButton kategoriItem;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private com.mycompany.tindaklanjutku.custom.RoundedButton roundedButton1;
    private javax.swing.JButton tugasItem;
    // End of variables declaration//GEN-END:variables
}
