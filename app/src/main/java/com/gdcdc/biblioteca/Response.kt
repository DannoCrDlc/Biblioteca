package com.gdcdc.biblioteca

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(
    var books: MutableList<Book>
):Parcelable
