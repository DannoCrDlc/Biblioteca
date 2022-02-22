package com.gdcdc.biblioteca

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    var id: String,
    var titulo: String,
    var autor: String,
    var editorial: String,
    var annio: String,
    var categoria: String,
    var precio: String,
    var foto: Uri
): Parcelable
