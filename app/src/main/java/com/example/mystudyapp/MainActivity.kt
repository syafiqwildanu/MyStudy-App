package com.example.mystudyapp

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var settings: SettingsManager

    // view yang ikut berubah ukurannya
    private lateinit var tvSubJudul: TextView
    private lateinit var tvMateriIsi: TextView
    private lateinit var tvJadwalIsi: TextView
    private lateinit var tvTugasIsi: TextView
    private lateinit var tvRingkasan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) siapkan pengelola pengaturan
        settings = SettingsManager(this)

        // 2) ambil view
        tvSubJudul  = findViewById(R.id.tvSubJudul)
        tvMateriIsi = findViewById(R.id.tvMateriIsi)
        tvJadwalIsi = findViewById(R.id.tvJadwalIsi)
        tvTugasIsi  = findViewById(R.id.tvTugasIsi)
        tvRingkasan = findViewById(R.id.tvRingkasan)

        // 3) tombol menuju halaman pengaturan
        findViewById<Button>(R.id.btnPengaturan).setOnClickListener {
            startActivity(Intent(this, PengaturanActivity::class.java))
        }
    }

    /** Dijalankan setiap kali halaman ini tampil kembali
     *  — termasuk setelah kembali dari halaman pengaturan. */
    override fun onResume() {
        super.onResume()
        terapkanPengaturanTampilan()
    }

    private fun terapkanPengaturanTampilan() {
        // 1) terapkan ukuran teks pengaturan ke semua kalimat isi
        val ukuran = settings.ukuranTeksSp()
        listOf(tvSubJudul, tvMateriIsi, tvJadwalIsi, tvTugasIsi).forEach {
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, ukuran)
        }

        // 2) tampilkan ringkasan pengaturan yang sedang aktif
        tvRingkasan.text = RingkasanPengaturan.teks(this, settings)
    }
}