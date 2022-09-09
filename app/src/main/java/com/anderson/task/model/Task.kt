package com.anderson.task.model

import android.os.Parcelable
import com.anderson.task.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var title: String = "",
    var status: Int = 0
): Parcelable{
    // toda vez que salva uma coisa nova vai ter que ser gerado um id
    init {
        this.id = FirebaseHelper.getDataBase().push().key ?: ""
    }
}