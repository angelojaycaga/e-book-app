package com.example.ebookv1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView

class MainActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private lateinit var pageText: TextView
    private lateinit var nextBtn: Button
    private lateinit var prevBtn: Button
    private lateinit var bookmarkBtn: Button
    private lateinit var darkBtn: Button

    private var currentPage = 0
    private var darkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfView = findViewById(R.id.pdfView)
        pageText = findViewById(R.id.pageText)
        nextBtn = findViewById(R.id.nextBtn)
        prevBtn = findViewById(R.id.prevBtn)
        bookmarkBtn = findViewById(R.id.bookmarkBtn)
        darkBtn = findViewById(R.id.darkBtn)

        currentPage = loadPage()

        loadPDF()

        nextBtn.setOnClickListener {
            if (currentPage < pdfView.pageCount - 1) {
                pdfView.jumpTo(currentPage + 1)
            }
        }

        prevBtn.setOnClickListener {
            if (currentPage > 0) {
                pdfView.jumpTo(currentPage - 1)
            }
        }

        bookmarkBtn.setOnClickListener {
            saveBookmark(currentPage)
        }

        darkBtn.setOnClickListener {
            darkMode = !darkMode
            loadPDF() // Reload to apply night mode settings
        }
    }

    private fun loadPDF() {
        pdfView.fromAsset("book.pdf")
            .defaultPage(currentPage)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .nightMode(darkMode)
            .onPageChange { page, pageCount ->
                currentPage = page
                pageText.text = "Page ${page + 1} / $pageCount"
                savePage(page)
            }
            .load()
    }

    private fun savePage(page: Int) {
        val prefs = getSharedPreferences("reader", MODE_PRIVATE)
        prefs.edit().putInt("page", page).apply()
    }

    private fun loadPage(): Int {
        val prefs = getSharedPreferences("reader", MODE_PRIVATE)
        return prefs.getInt("page", 0)
    }

    private fun saveBookmark(page: Int) {
        val prefs = getSharedPreferences("reader", MODE_PRIVATE)
        prefs.edit().putInt("bookmark", page).apply()
    }
}
