package com.cykei.birdview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class ItemAdapter(val context:Context, val itemList:ArrayList<Item>):RecyclerView.Adapter<ItemAdapter.Holder>(){
    //lateinit var queue: RequestQueue

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position],context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemThumbnail = itemView?.findViewById<ImageView>(R.id.item_image)
        val itemTitle = itemView.findViewById<TextView>(R.id.item_name)
        val itemPrice = itemView.findViewById<TextView>(R.id.item_price)

        //val itemBorder= context.getDrawable(R.drawable.item_border) as GradientDrawable


        fun bind (item: Item, context: Context) {
            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (item.thumbnail_image != "") {
                Picasso.get().load(item.thumbnail_image)
                    .into(itemThumbnail)
               // itemThumbnail.background(itemBorder)
              //  itemThumbnail.clipToOutline(true)
            } else {

                itemThumbnail.setImageResource(R.mipmap.ic_launcher)
            }

            /* 나머지 TextView와 String 데이터를 연결한다. */
            itemTitle.text = item.title
            itemPrice.text = item.price


        }

    }

}