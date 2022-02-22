package com.gdcdc.biblioteca

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BooksActivity : AppCompatActivity(), CustomAdapter.OnDuelistClickListener, SearchView.OnQueryTextListener {

    lateinit var inflater: LayoutInflater
    lateinit var viewInflater: View
    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var nombreTxt: EditText
    lateinit var telefonoTxt: EditText
    lateinit var correoTxt: EditText
    lateinit var nuevoBtn: Button
    lateinit var guardarBtn: Button
    lateinit var bookImage: ImageView
    lateinit var searchView: SearchView
    var listBooks = mutableListOf<Book>()
    lateinit var booksSaved: Array<Parcelable>
    lateinit var response: Response
    lateinit var book: Book
    lateinit var idSelect: String
    lateinit var booksListRV: RecyclerView
    lateinit var adapter: CustomAdapter
    var listBookAux = mutableListOf<Book>()
    val stateUser: String = "booksSaved"

    private val selectActivity: Int = 50
    private var uri: Uri =
        Uri.parse("https://www.nicepng.com/png/full/71-713335_yugioh-logo-yu-gi-oh.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        InitializerComponents(this)
        if (intent.getStringExtra("save") == "1") {
            booksSaved = intent.getParcelableArrayExtra(stateUser)!!
            response = booksSaved[0] as Response
            listBooks = response.books
            val bookSave = intent.getParcelableExtra<Book>("bookSave")
            val indxBook: String = IndexBookBy(bookSave!!.id).toString()
            if (indxBook.toInt() == -1) {
                listBooks.add(bookSave)
            } else {
                listBooks[indxBook.toInt()] = bookSave
            }
        }else if(intent.getStringExtra("save") == "2"){
            booksSaved = intent.getParcelableArrayExtra(stateUser)!!
            response = booksSaved[0] as Response
            listBooks = response.books
        }
        listBookAux.addAll(listBooks)
        fillListDuelist(this)

        nuevoBtn.setOnClickListener {
            newDataDuelist()
        }
    }

    private fun newDataDuelist() {
        response = Response(listBooks)
        booksSaved = arrayOf(response)
        val intent = Intent(this, DataBookActivity::class.java).apply {
            putExtra("book", Book("","","","","","","",uri))
            putExtra("idBook", "${listBooks.size + 1}")
            putExtra("booksSaved", booksSaved)
            putExtra("mode", "I")
        }
        startActivity(intent)
        finish()
    }

    fun fillListDuelist(context: Context) {
        adapter = CustomAdapter(listBooks, context, Activity(), this)
        booksListRV.layoutManager = LinearLayoutManager(context)
        booksListRV.adapter = adapter
    }

    fun showDataBy(idBook: String) {
        response = Response(listBooks)
        booksSaved = arrayOf(response)
        val indxBook: String = IndexBookBy(idBook).toString()
        val intent = Intent(this, DataBookActivity::class.java).apply {
            putExtra("mode", "M")
            putExtra("book", listBooks[indxBook.toInt()])
            putExtra("idBook", idBook)
            putExtra("booksSaved", booksSaved)
        }
        startActivity(intent)
        finish()
    }

    private fun IndexBookBy(idBook: String): Int {
        var idxBook: Int = 0
        for (book in listBooks) {
            if (book.id == idBook) {
                break
            }
            idxBook = idxBook + 1
        }
        if (idxBook > (listBooks.size - 1)) {
            return -1
        }
        return idxBook
    }

    private fun InitializerComponents(context: Context) {
        inflater = getLayoutInflater()
        viewInflater = inflater.inflate(R.layout.data_book, null)
        nombreTxt = viewInflater.findViewById(R.id.nombreTxt)
        telefonoTxt = viewInflater.findViewById(R.id.telefonoTxt)
        correoTxt = viewInflater.findViewById(R.id.correoTxt)
        nuevoBtn = findViewById(R.id.nuevoBtn)
        guardarBtn = viewInflater.findViewById(R.id.guardarBtn2)
        booksListRV = findViewById(R.id.recyclerView)
        bookImage = viewInflater.findViewById(R.id.dataDuelistImage)
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(this);


        BuildFeatureDuelist()

        builder = AlertDialog.Builder(this)
        builder.setView(viewInflater)
            .setCancelable(false)
        alertDialog = builder.create()

        guardarBtn.setOnClickListener {
            book = Book(
                idSelect,
                nombreTxt.text.toString(),
                telefonoTxt.text.toString(),
                correoTxt.text.toString(),
                "",
                "",
                "",
                uri
            )
            if (idSelect.toInt() > listBooks.size) {
                listBooks.add(book)
            } else {
                listBooks[idSelect.toInt() - 1] = book
            }
            fillListDuelist(context)
            alertDialog.dismiss()
        }

        bookImage.setOnClickListener {
            ImageController.SelectFromGalery(this, selectActivity)
        }

    }

    private fun BuildFeatureDuelist() {
        listBooks.add(
            Book(
                "1",
                "Quijote",
                "Miguel de Cervantes",
                "Real academia española",
                "2012",
                "Novela clasica",
                "$300",
                Uri.parse("https://www.loqueleo.com/es/uploads/2016/02/resized/800_9788468025384.jpg")
            ),
        )
        listBooks.add(
            Book(
                "2",
                "Mil y una noches",
                "Anonimo",
                "Berlibro",
                "2002",
                "Cuentos",
                "$250",
                Uri.parse("https://www.elsotano.com/imagenes_grandes/9788445/978844590983.JPG")
            )
        )
        listBooks.add(
            Book(
                "3",
                "Batallas en el desierto",
                "Jose Emilio Pacheco",
                "ERA",
                "2006",
                "Novela",
                "$100",
                Uri.parse("https://http2.mlstatic.com/las-batallas-en-el-desierto-jose-emilio-pacheco-firmado-D_NQ_NP_192101-MLM20277256621_042015-F.jpg")
            )
        )
    }

    override fun OnDeleteClick(idBook: String) {
        Notification("¿Está seguro de borrar el libro?") { DeleteDataBy(idBook) }
    }

    override fun OnEditClick(idBook: String) {
        showDataBy(idBook)
    }

    private fun DeleteDataBy(idBook: String) {
        listBooks.removeAt(IndexBookBy(idBook))
        listBookAux.addAll(listBooks)
        fillListDuelist(this)
    }

    private fun Notification(message: String, function: () -> Unit) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setMessage(message)
            .setCancelable(true)
            .setNegativeButton("No") { view, _ -> }
            .setPositiveButton("Si") { view, _ -> function.invoke() }
            .create()
        alert.show()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0.equals("")!!){
            listBooks.clear()
            listBooks.addAll(listBookAux)
        }else {
            listBooks.clear()
            for (book in listBookAux) {
                if (book.titulo.indexOf(p0!!) != -1) {
                    if (listBooks.indexOf(book) == -1) {
                        listBooks.add(book)
                    }
                }
                if (book.autor.indexOf(p0!!) != -1) {
                    if (listBooks.indexOf(book) == -1) {
                        listBooks.add(book)
                    }
                }
                if (book.editorial.indexOf(p0!!) != -1) {
                    if (listBooks.indexOf(book) == -1) {
                        listBooks.add(book)
                    }
                }
                if (book.annio.indexOf(p0!!) != -1) {
                    if (listBooks.indexOf(book) == -1) {
                        listBooks.add(book)
                    }
                }
            }
        }
        fillListDuelist(this)
        return false
    }
}
