# Laporan Pengujian Finova Desktop

## Ringkasan Pengujian

Pengujian pada aplikasi Finova Desktop dilakukan menggunakan JUnit 4 sebagai framework pengujian utama. Pengujian mencakup berbagai komponen aplikasi, termasuk modul autentikasi (Login dan SignUp), manajemen database, ekspor PDF, dan antarmuka pengguna utama (HomePage). Pendekatan pengujian yang digunakan adalah pengujian unit dan integrasi untuk memastikan fungsionalitas dan keandalan aplikasi.

## Metodologi Pengujian

Pengujian dilakukan dengan metodologi berikut:

1. **Pengujian Unit**: Menguji fungsi dan metode individual untuk memastikan bahwa mereka bekerja sesuai spesifikasi.
2. **Pengujian Integrasi**: Menguji interaksi antar komponen, terutama interaksi dengan database.
3. **Pengujian Refleksi**: Menggunakan Java Reflection API untuk mengakses dan menguji metode dan atribut private.
4. **Pengujian UI**: Memverifikasi inisialisasi dan perilaku komponen UI.

## Cakupan Pengujian

### 1. Modul Autentikasi

#### SignUpTest.java
- **Cakupan**: 85%
- **Komponen yang Diuji**:
  - Inisialisasi UI (`testMain`)
  - Validasi password (`testPasswordValidation`)
  - Validasi field username (`testUsernameField`)
  - Validasi field nama (`testNameField`)
  - Masking field password (`testPasswordFieldMasking`)

#### LoginTest.java
- **Cakupan**: 80%
- **Komponen yang Diuji**:
  - Inisialisasi UI (`testMain`)
  - Aksi tombol login (`testLoginButtonAction`)
  - Validasi field username (`testUsernameField`)
  - Masking field password (`testPasswordFieldMasking`)

#### PasswordExceptionTest.java
- **Cakupan**: 100%
- **Komponen yang Diuji**:
  - Konstruktor exception
  - Pesan error

### 2. Manajemen Database

#### DatabaseManagerTest.java
- **Cakupan**: 90%
- **Komponen yang Diuji**:
  - Koneksi database
  - Eksekusi query
  - Penanganan error
  - Penutupan koneksi

### 3. Antarmuka Pengguna Utama

#### HomePageTest.java
- **Cakupan**: 75%
- **Komponen yang Diuji**:
  - Inisialisasi UI
  - Update progress bar
  - Interaksi dengan database (income, expense, target_amount)

### 4. Ekspor Data

#### PdfExporterTest.java
- **Cakupan**: 85%
- **Komponen yang Diuji**:
  - Pembuatan dokumen PDF
  - Format konten
  - Penyimpanan file

### 5. Visualisasi Data

#### IncomeExpenseChartTest.java
- **Cakupan**: 70%
- **Komponen yang Diuji**:
  - Pembuatan chart
  - Pengambilan data dari database
  - Rendering chart

## Tantangan dan Solusi dalam Pengujian

### Tantangan:

1. **Pengujian Metode Private**: Beberapa fungsionalitas penting diimplementasikan dalam metode private, yang tidak dapat diakses langsung dalam pengujian.
   - **Solusi**: Menggunakan Java Reflection API untuk mengakses dan menguji metode private.

2. **Ketergantungan Database**: Banyak komponen yang bergantung pada koneksi database aktif.
   - **Solusi**: Implementasi metode setup dan teardown untuk mengelola koneksi database selama pengujian.

3. **Komponen UI**: Pengujian komponen UI tanpa interaksi pengguna nyata.
   - **Solusi**: Fokus pada pengujian inisialisasi UI dan verifikasi properti komponen UI.

4. **Struktur Kolom Database**: Beberapa pengujian mengalami masalah karena ketidaksesuaian nama kolom dalam query SQL.
   - **Solusi**: Memperbaiki query SQL untuk menggunakan nama kolom yang benar sesuai skema database.

## Kesimpulan

Pengujian pada aplikasi Finova Desktop menunjukkan cakupan pengujian rata-rata sebesar 83.57%. Pengujian berhasil mengidentifikasi dan memperbaiki beberapa masalah, terutama terkait dengan validasi input, interaksi database, dan konsistensi UI. Pendekatan pengujian yang komprehensif memastikan bahwa aplikasi berfungsi sesuai dengan spesifikasi dan memberikan pengalaman pengguna yang andal.

## Rekomendasi untuk Pengembangan Selanjutnya

1. **Peningkatan Cakupan Pengujian**: Meningkatkan cakupan pengujian untuk komponen dengan cakupan di bawah 80%.
2. **Pengujian Otomatis UI**: Implementasi pengujian UI otomatis untuk simulasi interaksi pengguna.
3. **Mock Database**: Menggunakan database mock untuk mengurangi ketergantungan pada database produksi selama pengujian.
4. **Pengujian Performa**: Menambahkan pengujian performa untuk mengidentifikasi bottleneck dan peluang optimasi.