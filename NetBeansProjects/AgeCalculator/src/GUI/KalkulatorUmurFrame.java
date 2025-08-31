package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class KalkulatorUmurFrame extends JFrame {

    private JTextField tfTanggalLahir;
    private JLabel lblHasil;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public KalkulatorUmurFrame() {
        setTitle("Kalkulator Umur");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 245, 255));

        JLabel lblTitle = new JLabel("Kalkulator Umur");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(110, 10, 200, 30);
        panel.add(lblTitle);

        JLabel lblTanggal = new JLabel("Tanggal Lahir (dd-MM-yyyy):");
        lblTanggal.setBounds(50, 60, 200, 20);
        panel.add(lblTanggal);

        tfTanggalLahir = new JTextField();
        tfTanggalLahir.setBounds(50, 85, 300, 30);
        panel.add(tfTanggalLahir);

        JButton btnHitung = new JButton("Hitung Umur");
        btnHitung.setBounds(50, 130, 130, 30);
        panel.add(btnHitung);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(220, 130, 130, 30);
        panel.add(btnReset);

        lblHasil = new JLabel("Umur Anda akan tampil di sini.");
        lblHasil.setFont(new Font("Arial", Font.PLAIN, 14));
        lblHasil.setBounds(50, 180, 320, 30);
        panel.add(lblHasil);

        btnHitung.addActionListener(e -> hitungUmur());

        btnReset.addActionListener(e -> {
            tfTanggalLahir.setText("");
            lblHasil.setText("Umur Anda akan tampil di sini.");
        });

        add(panel);
    }

    private void hitungUmur() {
        String inputTanggal = tfTanggalLahir.getText().trim();

        try {
            LocalDate tanggalLahir = LocalDate.parse(inputTanggal, formatter);
            LocalDate hariIni = LocalDate.now();

            if (tanggalLahir.isAfter(hariIni)) {
                lblHasil.setText("Tanggal lahir tidak boleh di masa depan.");
                return;
            }

            Period umur = Period.between(tanggalLahir, hariIni);

            lblHasil.setText(String.format("Umur Anda: %d tahun, %d bulan, %d hari.",
                    umur.getYears(), umur.getMonths(), umur.getDays()));

        } catch (DateTimeParseException e) {
            lblHasil.setText("Format salah. Gunakan dd-MM-yyyy (misal: 02-05-2000)");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KalkulatorUmurFrame().setVisible(true));
    }
}
