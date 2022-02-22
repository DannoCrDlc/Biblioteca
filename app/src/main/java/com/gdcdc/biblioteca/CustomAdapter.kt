package com.gdcdc.biblioteca

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter(
    val books: List<Book>,
    val context: Context,
    val activity: Activity,
    private val itemClickListener: OnDuelistClickListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnDuelistClickListener{
        fun OnDeleteClick(idBook: String)
        fun OnEditClick(idBook: String)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_books, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val book = books[i]
        Glide.with(context).load(book.foto).into(viewHolder.itemImagen);
        viewHolder.idBook.text = book.id
        viewHolder.itemNam.text = book.titulo
        viewHolder.tvAutor.text = book.autor
        viewHolder.tvEditorial.text = book.editorial
        viewHolder.tvAnnio.text = book.annio

    }

    override fun getItemCount(): Int {
        return books.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView
        var itemNam: TextView
        var tvAutor: TextView
        var tvEditorial: TextView
        var tvAnnio: TextView
        var buttonEdit: Button
        var idBook: TextView
        var mainLayout: ConstraintLayout

        init {
            itemImagen = itemView.findViewById(R.id.duelistImage)
            itemNam = itemView.findViewById(R.id.tvTitulo)
            tvAutor = itemView.findViewById(R.id.tvAutor)
            tvEditorial = itemView.findViewById(R.id.tvEditorial)
            tvAnnio = itemView.findViewById(R.id.tvAnnio)
            buttonEdit = itemView.findViewById(R.id.editBtn)
            idBook = itemView.findViewById(R.id.idDuelist)
            mainLayout = itemView.findViewById(R.id.mainLayout)
            buttonEdit.setOnClickListener{itemClickListener.OnDeleteClick(idBook.text.toString())}
            mainLayout.setOnClickListener { itemClickListener.OnEditClick(idBook.text.toString()) }
            itemImagen.setOnClickListener { itemClickListener.OnEditClick(idBook.text.toString()) }
            itemNam.setOnClickListener { itemClickListener.OnEditClick(idBook.text.toString()) }

        }
    }
}