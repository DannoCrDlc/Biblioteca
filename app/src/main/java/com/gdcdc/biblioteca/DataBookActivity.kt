package com.gdcdc.biblioteca

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide

class DataBookActivity : AppCompatActivity() {
    lateinit var book: Book
    lateinit var tituloTxt: EditText
    lateinit var autorTxt: EditText
    lateinit var editorialTxt: EditText
    lateinit var annioTxt: EditText
    lateinit var bookImage: ImageView
    lateinit var bundle: Bundle
    lateinit var guardarBtn: Button
    lateinit var tituloLbl: TextView
    private var uri: Uri =
        Uri.parse("https://www.nicepng.com/png/full/71-713335_yugioh-logo-yu-gi-oh.png")
    private val selectActivity: Int = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_book)
        bundle = intent.extras!!
        val intent = Intent(this, DataBookActivity::class.java)
        tituloTxt = findViewById(R.id.tituloTxt)
        autorTxt = findViewById(R.id.autorTxt)
        editorialTxt = findViewById(R.id.editorialTxt)
        annioTxt = findViewById(R.id.annioTxt)
        bookImage = findViewById(R.id.bookImage)
        guardarBtn = findViewById(R.id.guardarBtn)
        tituloLbl = findViewById(R.id.titleLbl)

        InitializationComponent()
    }

    private fun InitializationComponent() {
        book = bundle.getParcelable<Book>("book")!!
        if(book.foto.toString().indexOf("http") != -1){
            Glide.with(this).load(book.foto.toString()).into(bookImage)
        }else{
            bookImage.setImageURI(book.foto)
        }
        bookImage.setOnClickListener {
            ImageController.SelectFromGalery(this, selectActivity)
        }
        guardarBtn.setOnClickListener {
            saveData()
        }
        if(bundle.getString("mode") == "I"){
            tituloLbl.setText("Nuevo libro")
        }else{
            tituloLbl.setText("Editar libro")
        }
        tituloTxt.setText(book.titulo)
        autorTxt.setText(book.autor)
        editorialTxt.setText(book.editorial)
        annioTxt.setText(book.annio)
        uri = book.foto
    }

    private fun saveData() {
        book = Book(
            bundle.getString("idBook").toString(),
            tituloTxt.text.toString(),
            autorTxt.text.toString(),
            editorialTxt.text.toString(),
            annioTxt.text.toString(),
            uri
        )
        val intent = Intent(this, BooksActivity::class.java).apply {
            putExtra("save","1")
            putExtra("bookSave", book)
            putExtra("booksSaved", bundle.getParcelableArray("booksSaved"))
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, BooksActivity::class.java).apply {
            putExtra("save","2")
            putExtra("bookSave", book)
            putExtra("booksSaved", bundle.getParcelableArray("booksSaved"))
        }
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == selectActivity && resultCode == Activity.RESULT_OK -> {
                val imageUri = data!!.data
                uri = imageUri!!
                bookImage.setImageURI(imageUri)
            }
        }
    }
}