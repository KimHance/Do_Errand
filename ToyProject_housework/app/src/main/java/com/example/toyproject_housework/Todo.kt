package com.example.toyproject_housework

import android.graphics.drawable.Drawable

data class Todo(
    var todoImg : Drawable,
    var title : String,
    var userName : String,
    var todoContext : String,
    var add : Boolean,
    var date : String
)