package com.gdcdc.biblioteca

import android.app.Activity
import android.content.Intent

object ImageController {
    fun SelectFromGalery(activity: Activity, code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }
}