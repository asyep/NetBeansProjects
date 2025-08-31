-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 07 Jun 2025 pada 17.26
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tindaklanjutku`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `catatan_hasil`
--

CREATE TABLE `catatan_hasil` (
  `id_catatan` int(11) NOT NULL,
  `id_tugas` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `isi_catatan` varchar(400) NOT NULL,
  `dibuat_oleh` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `catatan_hasil`
--

INSERT INTO `catatan_hasil` (`id_catatan`, `id_tugas`, `tanggal`, `isi_catatan`, `dibuat_oleh`) VALUES
(37, 2, '2025-06-06', 'hi', 'ardi'),
(38, 2, '2025-06-06', 'halo', 'ardi'),
(39, 2, '2025-06-06', 'hai hai halo', 'ardi');

-- --------------------------------------------------------

--
-- Struktur dari tabel `divisi`
--

CREATE TABLE `divisi` (
  `id_divisi` int(11) NOT NULL,
  `nama_divisi` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `divisi`
--

INSERT INTO `divisi` (`id_divisi`, `nama_divisi`) VALUES
(3, 'Acara'),
(5, 'Media Visual'),
(6, 'Hubungan Masyarakat');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
(14, 'deqwdqw'),
(15, 'afdsafadsfgasdfg');

-- --------------------------------------------------------

--
-- Struktur dari tabel `penanggung_jawab`
--

CREATE TABLE `penanggung_jawab` (
  `id_pj` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_divisi` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `penanggung_jawab`
--

INSERT INTO `penanggung_jawab` (`id_pj`, `id_user`, `id_divisi`) VALUES
(5, 15, 3);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tugas`
--

CREATE TABLE `tugas` (
  `id_tugas` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL,
  `deadline` date NOT NULL,
  `status` enum('belum','progres','selesai','') NOT NULL DEFAULT 'belum',
  `id_pj` int(11) NOT NULL,
  `id_kategori` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tugas`
--

INSERT INTO `tugas` (`id_tugas`, `judul`, `deskripsi`, `deadline`, `status`, `id_pj`, `id_kategori`, `id_user`) VALUES
(2, 'membuat kopi', 'adfsdgasgfhfh', '2025-06-11', 'belum', 5, 15, 15);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tugas_user`
--

CREATE TABLE `tugas_user` (
  `id` int(11) NOT NULL,
  `id_tugas` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tugas_user`
--

INSERT INTO `tugas_user` (`id`, `id_tugas`, `id_user`) VALUES
(4, 2, 16);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `Id_usr` int(11) NOT NULL,
  `id_divisi` int(11) DEFAULT NULL,
  `namaUsr` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pw` varchar(255) NOT NULL,
  `role` enum('anggota','admin','pj') NOT NULL DEFAULT 'anggota'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`Id_usr`, `id_divisi`, `namaUsr`, `email`, `pw`, `role`) VALUES
(14, 3, 'admin', 'admin@upi.edu', '$2a$10$AOe0uNPKSOHB7rYHcpozau10maFf6eWEp0YPkZ9VjzixZ3JH9W./.', 'admin'),
(15, 3, 'ardi', 'ardi@upi.edu', '$2a$10$se6AW4E.LsL6afDQ6PvZq.hOaX285gJJxnWdfMRwiSJtlVcolTqU.', 'pj'),
(16, 3, 'soe', 'soe@upi.edu', '$2a$10$LH4.DDxbmUHoEUY3Wfb3YO5DnmHfisq22VtUblOMsQWxrwqIzXsCm', 'anggota');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `catatan_hasil`
--
ALTER TABLE `catatan_hasil`
  ADD PRIMARY KEY (`id_catatan`),
  ADD KEY `id_tugas` (`id_tugas`);

--
-- Indeks untuk tabel `divisi`
--
ALTER TABLE `divisi`
  ADD PRIMARY KEY (`id_divisi`);

--
-- Indeks untuk tabel `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indeks untuk tabel `penanggung_jawab`
--
ALTER TABLE `penanggung_jawab`
  ADD PRIMARY KEY (`id_pj`),
  ADD KEY `penanggung_jawab_ibfk_1` (`id_user`),
  ADD KEY `penanggung_jawab_ibfk_2` (`id_divisi`);

--
-- Indeks untuk tabel `tugas`
--
ALTER TABLE `tugas`
  ADD PRIMARY KEY (`id_tugas`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `tugas_ibfk_1` (`id_kategori`),
  ADD KEY `tugas_ibfk_2` (`id_pj`);

--
-- Indeks untuk tabel `tugas_user`
--
ALTER TABLE `tugas_user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_tugas` (`id_tugas`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`Id_usr`),
  ADD KEY `id_divisi` (`id_divisi`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `catatan_hasil`
--
ALTER TABLE `catatan_hasil`
  MODIFY `id_catatan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT untuk tabel `divisi`
--
ALTER TABLE `divisi`
  MODIFY `id_divisi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT untuk tabel `penanggung_jawab`
--
ALTER TABLE `penanggung_jawab`
  MODIFY `id_pj` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `tugas`
--
ALTER TABLE `tugas`
  MODIFY `id_tugas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `tugas_user`
--
ALTER TABLE `tugas_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `Id_usr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `catatan_hasil`
--
ALTER TABLE `catatan_hasil`
  ADD CONSTRAINT `catatan_hasil_ibfk_1` FOREIGN KEY (`id_tugas`) REFERENCES `tugas` (`id_tugas`);

--
-- Ketidakleluasaan untuk tabel `penanggung_jawab`
--
ALTER TABLE `penanggung_jawab`
  ADD CONSTRAINT `penanggung_jawab_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`Id_usr`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `penanggung_jawab_ibfk_2` FOREIGN KEY (`id_divisi`) REFERENCES `divisi` (`id_divisi`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tugas`
--
ALTER TABLE `tugas`
  ADD CONSTRAINT `tugas_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tugas_ibfk_2` FOREIGN KEY (`id_pj`) REFERENCES `penanggung_jawab` (`id_pj`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tugas_ibfk_3` FOREIGN KEY (`id_user`) REFERENCES `user` (`Id_usr`);

--
-- Ketidakleluasaan untuk tabel `tugas_user`
--
ALTER TABLE `tugas_user`
  ADD CONSTRAINT `tugas_user_ibfk_1` FOREIGN KEY (`id_tugas`) REFERENCES `tugas` (`id_tugas`),
  ADD CONSTRAINT `tugas_user_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`Id_usr`);

--
-- Ketidakleluasaan untuk tabel `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_divisi`) REFERENCES `divisi` (`id_divisi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
