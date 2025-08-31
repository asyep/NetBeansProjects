package admin;

import com.mycompany.tindaklanjutku.custom.panelCustom;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CardCatatan extends panelCustom {
    private JLabel lblPengirim;
    private JLabel lblTanggal;
    private JLabel lblTugas;
    private JTextArea txtCatatan;
    private JLabel nilaiPengirim;
    private JLabel nilaiTanggal;
    private JLabel nilaiTugas;
    private JButton btnHapus;
    private String cardId;
    
    
    
    public CardCatatan(String nama, String isi, String tanggal, String tugas) {
        setBackground(new Color(102, 102, 255));
        setRoundBottomLeft(16);
        setRoundBottomRight(16);
        setRoundTopLeft(16);
        setRoundTopRight(16);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Komponen Label
        lblPengirim = new JLabel("Perngirim:");
        lblTanggal = new JLabel("Tanggal   :");
        lblTugas = new JLabel("Tugas       :");
        
        // Komponen Nilai
        nilaiPengirim = new JLabel(nama);
        nilaiTanggal = new JLabel(tanggal);
        nilaiTugas = new JLabel(tugas);
        
        txtCatatan = new JTextArea(isi);
        txtCatatan.setEditable(false);
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        txtCatatan.setBackground(new Color(153, 153, 255));
        txtCatatan.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnHapus = new JButton("Hapus");
        btnHapus.setBackground(new Color(255, 102, 102));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);
        btnHapus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Hilangkan scrollbar
        txtCatatan.setRows(5); // Atur jumlah baris yang sesuai
        txtCatatan.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtCatatan.getPreferredSize().height));
        
        // Styling
        Font poppins = new Font("Poppins", Font.PLAIN, 14);
        lblPengirim.setFont(poppins);
        lblTanggal.setFont(poppins);
        lblTugas.setFont(poppins);
        nilaiPengirim.setFont(poppins);
        nilaiTanggal.setFont(poppins);
        nilaiTugas.setFont(poppins);
        txtCatatan.setFont(poppins);
        
        lblPengirim.setForeground(Color.WHITE);
        lblTanggal.setForeground(Color.WHITE);
        lblTugas.setForeground(Color.WHITE);
        nilaiPengirim.setForeground(Color.WHITE);
        nilaiTanggal.setForeground(Color.WHITE);
        nilaiTugas.setForeground(Color.WHITE);
        
        // Layout
        JPanel panelPengirim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPengirim.setOpaque(false);
        panelPengirim.add(lblPengirim);
        panelPengirim.add(nilaiPengirim);
        
        JPanel panelTanggal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTanggal.setOpaque(false);
        panelTanggal.add(lblTanggal);
        panelTanggal.add(nilaiTanggal);
        
        JPanel panelTugas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTugas.setOpaque(false);
        panelTugas.add(lblTugas);
        panelTugas.add(nilaiTugas);
        
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setOpaque(false);
        panelButton.add(btnHapus);
        
        add(panelPengirim);
        add(Box.createVerticalStrut(10));
        add(txtCatatan); // Langsung tambahkan text area tanpa scroll pane
        add(Box.createVerticalStrut(10));
        add(panelTanggal);
        add(Box.createVerticalStrut(5));
        add(panelTugas);
        add(Box.createVerticalStrut(10));
        add(panelButton);
        
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    public void addHapusListener(ActionListener listener) {
        btnHapus.addActionListener(listener);
    }
    
    public void setCardId(String id) {
        this.cardId = id; // Store the ID
        btnHapus.setActionCommand(id);
    }

    public String getCardId() {
               return this.cardId;
    }
}