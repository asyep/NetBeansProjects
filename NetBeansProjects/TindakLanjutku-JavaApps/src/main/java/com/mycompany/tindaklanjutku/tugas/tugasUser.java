/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tindaklanjutku.tugas;
import com.mycompany.tindaklanjutku.Koneksi;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 *
 * @author ASUS VIVO
 */
public class tugasUser extends javax.swing.JFrame {

    /**
     * Creates new form tugasUser
     */
    public tugasUser() {
        initComponents();
        loadTugasData();
    }
    
private void loadTugasData() {
        try (Connection conn = Koneksi.configDB();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tugas");
             ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel() {
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // hanya kolom status
                }

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

            customTable1.setModel(model);

            // Atur kolom
            TableColumnModel columnModel = customTable1.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(300);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(80);

            // Renderer
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
                protected void setValue(Object value) {
                    if (value instanceof Date) {
                        setText(dateFormat.format((Date) value));
                    } else {
                        super.setValue(value);
                    }
                }
            };

            for (int i = 0; i < customTable1.getColumnCount(); i++) {
                if (i == 3) {
                    dateRenderer.setHorizontalAlignment(JLabel.CENTER);
                    customTable1.getColumnModel().getColumn(i).setCellRenderer(dateRenderer);
                } else {
                    customTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
            }

            // Renderer Status
            customTable1.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

            // ComboBox untuk status
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"belum", "progres", "selesai"});
            customTable1.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusComboBox));

            // Listener untuk update status
            customTable1.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                        int row = e.getFirstRow();
                        int taskId = (int) customTable1.getValueAt(row, 0);
                        String newStatus = (String) customTable1.getValueAt(row, 4);
                        updateTaskStatus(taskId, newStatus);
                    }
                }
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTaskStatus(int taskId, String newStatus) {
        String lowerStatus = newStatus.toLowerCase();
        if (!lowerStatus.equals("belum") && !lowerStatus.equals("progres") && !lowerStatus.equals("selesai")) {
            JOptionPane.showMessageDialog(this, "Status tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Koneksi.configDB();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE tugas SET status = ? WHERE id_tugas = ?")) {

            pstmt.setString(1, lowerStatus);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
            loadTugasData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal update status: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            if (!isSelected && value != null) {
                String status = value.toString().toLowerCase();
                switch (status) {
                    case "belum":
                        c.setBackground(new Color(255, 153, 153));
                        break;
                    case "progres":
                        c.setBackground(new Color(255, 255, 153));
                        break;
                    case "selesai":
                        c.setBackground(new Color(144, 238, 144));
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
            }
            return c;
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
        tugasItem = new javax.swing.JButton();
        pjItem = new javax.swing.JButton();
        kategoriItem = new javax.swing.JButton();
        catatanKerjaItem = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customTable1 = new com.mycompany.tindaklanjutku.custom.CustomTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelCustom1.setBackground(new java.awt.Color(255, 255, 255));
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

        tugasItem.setBackground(new java.awt.Color(78, 75, 209));
        tugasItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tugasItem.setForeground(new java.awt.Color(255, 255, 255));
        tugasItem.setText("Tugas");
        tugasItem.setBorder(null);

        pjItem.setBackground(new java.awt.Color(78, 75, 209));
        pjItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        pjItem.setForeground(new java.awt.Color(255, 255, 255));
        pjItem.setText("Penanggung Jawab");
        pjItem.setBorder(null);

        kategoriItem.setBackground(new java.awt.Color(78, 75, 209));
        kategoriItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        kategoriItem.setForeground(new java.awt.Color(255, 255, 255));
        kategoriItem.setText("Kategori");
        kategoriItem.setBorder(null);

        catatanKerjaItem.setBackground(new java.awt.Color(78, 75, 209));
        catatanKerjaItem.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        catatanKerjaItem.setForeground(new java.awt.Color(255, 255, 255));
        catatanKerjaItem.setText("Catatan Kerja");
        catatanKerjaItem.setBorder(null);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tugasItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pjItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kategoriItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catatanKerjaItem, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tugas");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Anggota");

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Manajemen Tugas");

        customTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Judul Tugas", "Kategori", "PJ", "Deadline", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customTable1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(customTable1);

        javax.swing.GroupLayout panelCustom1Layout = new javax.swing.GroupLayout(panelCustom1);
        panelCustom1.setLayout(panelCustom1Layout);
        panelCustom1Layout.setHorizontalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addComponent(panelCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCustom1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(41, 41, 41))
                    .addGroup(panelCustom1Layout.createSequentialGroup()
                        .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                            .addGroup(panelCustom1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(33, 33, 33))))
        );
        panelCustom1Layout.setVerticalGroup(
            panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCustom2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelCustom1Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(panelCustom1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(tugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tugasUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tugasUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton catatanKerjaItem;
    private com.mycompany.tindaklanjutku.custom.CustomTable customTable1;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton kategoriItem;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom1;
    private com.mycompany.tindaklanjutku.custom.panelCustom panelCustom2;
    private javax.swing.JButton pjItem;
    private javax.swing.JButton tugasItem;
    // End of variables declaration//GEN-END:variables
}
