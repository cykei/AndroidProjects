package com.cykei.birdview

import java.util.*
import kotlin.collections.ArrayList

data class ItemModel(
    var statusCode: Int,
    var body:ArrayList<Item>
)
data class Item(
    val id:Int,
    val price: String,
    val oily_score:Int?,
    val thumbnail_image:String,
    val title :String

)