package com.example.mystudyapp

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class PengaturanActivity : AppCompatActivity() {

    private lateinit var settings: SettingsManager
    private lateinit var tvRingkasan: TextView
    private lateinit var tvStatusNotifikasi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        settings = SettingsManager(this)

        tvRingkasan = findViewById(R.id.tvRingkasan)
        tvStatusNotifikasi = findViewById(R.id.tvStatusNotifikasi)

        val rgBahasa = findViewById<RadioGroup>(R.id.rgBahasa)
        val rgTema = findViewById<RadioGroup>(R.id.rgTema)
        val rgUkuran = findViewById<RadioGroup>(R.id.rgUkuran)
        val swNotifikasi = findViewById<SwitchCompat>(R.id.swNotifikasi)

        // 1) TAMPILKAN KEADAAN AWAL dari pengaturan yang tersimpan
        isiKeadaanAwal(rgBahasa, rgTema, rgUkuran, swNotifikasi)

        // 2) BARU pasang listener: setiap perubahan disimpan & diterapkan
        rgBahasa.setOnCheckedChangeListener { _, id ->
            settings.simpanBahasa(
                if (id == R.id.rbEnglish) SettingsManager.BAHASA_EN
                else SettingsManager.BAHASA_ID
            )
            SettingsManager.terapkanKeAplikasi(this)      // teks langsung berganti bahasa
        }

        rgTema.setOnCheckedChangeListener { _, id ->
            settings.simpanTema(
                if (id == R.id.rbGelap) SettingsManager.TEMA_GELAP
                else SettingsManager.TEMA_TERANG
            )
            SettingsManager.terapkanKeAplikasi(this)      // tampilan langsung berganti tema
        }

        rgUkuran.setOnCheckedChangeListener { _, id ->
            val nilai = when (id) {
                R.id.rbKecil -> SettingsManager.UKURAN_KECIL
                R.id.rbBesar -> SettingsManager.UKURAN_BESAR
                else         -> SettingsManager.UKURAN_SEDANG
            }
            settings.simpanUkuran(nilai)
            perbaruiRingkasan()
        }

        swNotifikasi.setOnCheckedChangeListener { _, aktif ->
            settings.simpanNotifikasi(aktif)
            perbaruiRingkasan()
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener { konfirmasiReset() }
    }

    override fun onResume() {
        super.onResume()
        perbaruiRingkasan()
    }

    // ================= keadaan awal =================
    private fun isiKeadaanAwal(
        rgBahasa: RadioGroup, rgTema: RadioGroup, rgUkuran: RadioGroup, swNotifikasi: SwitchCompat
    ) {
        rgBahasa.check(
            if (settings.ambilBahasa() == SettingsManager.BAHASA_EN) R.id.rbEnglish
            else R.id.rbIndonesia
        )
        rgTema.check(
            if (settings.ambilTema() == SettingsManager.TEMA_GELAP) R.id.rbGelap
            else R.id.rbTerang
        )
        rgUkuran.check(
            when (settings.ambilUkuran()) {
                SettingsManager.UKURAN_KECIL -> R.id.rbKecil
                SettingsManager.UKURAN_BESAR -> R.id.rbBesar
                else -> R.id.rbSedang
            }
        )
        swNotifikasi.isChecked = settings.ambilNotifikasi()
        perbaruiRingkasan()
    }

// ... perbaruiRingkasan() dan konfirmasiReset() ditulis pada bagian 2 di bawah ...
    // ===== LANJUTAN dari PengaturanActivity.kt - bagian 1 di atas =====

    // ================= ringkasan pengaturan aktif =================
    private fun perbaruiRingkasan() {
        tvRingkasan.text = RingkasanPengaturan.teks(this, settings)
        tvStatusNotifikasi.text = RingkasanPengaturan.statusNotifikasi(this, settings)
    }

    // ================= reset dengan konfirmasi =================
    private fun konfirmasiReset() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_reset_judul)
            .setMessage(R.string.dialog_reset_pesan)
            .setNegativeButton(R.string.dialog_batal, null)
            .setPositiveButton(R.string.dialog_ya) { _, _ ->
                settings.reset() // sekarang sync karena pakai commit()

                // 1. Terapkan default (id + light) ke seluruh app
                SettingsManager.terapkanKeAplikasi(this)

                // 2. Restart bersih activity ini, jangan recreate() langsung
                finish()
                startActivity(intent)

                // 3. Toast setelah restart biar pakai bahasa Indonesia
                Toast.makeText(
                    applicationContext, getString(R.string.toast_reset), Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }
}



