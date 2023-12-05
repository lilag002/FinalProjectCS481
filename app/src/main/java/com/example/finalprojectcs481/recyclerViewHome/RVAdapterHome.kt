package com.example.finalprojectcs481.recyclerViewHome

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.database.Post
import com.example.finalprojectcs481.postModelData.PostData
import com.squareup.picasso.Picasso


class RVAdapterHome(private val postList: List<Post>): RecyclerView.Adapter<RVAdapterHome.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_home_row,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = postList[position]
            holder.tvTITLE.text = item.title.toString()
            holder.tvUSERNAME.text = item.username.toString()
            holder.tvLIKES.text = item.likes.toString()
            holder.tvDISLIKES.text = item.dislikes.toString()
            Picasso.get().load(item.image).into(holder.ivPOST)
            // take in account for liked and disliked flag
            if(item.liked){
                holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24_click)
            }
            else if(item.disliked){
                holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24_click)
            }

            holder.btnLike.setOnClickListener{
                if(!item.liked){
                    if(item.disliked){
                        holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24)
                        item.disliked = false
                        holder.tvDISLIKES.text = (holder.tvDISLIKES.text.toString().toInt() - 1).toString()
                        item.dislikes--
                    }
                    holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24_click)
                    item.liked = true
                    holder.tvLIKES.text = (holder.tvLIKES.text.toString().toInt() + 1).toString()
                    item.likes++
                }
            }
            holder.btnDislike.setOnClickListener {
                if(!item.disliked){
                    if(item.liked){
                        holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24)
                        item.liked = false
                        holder.tvLIKES.text = (holder.tvLIKES.text.toString().toInt() - 1).toString()
                        item.likes--
                    }
                    holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24_click)
                    item.disliked = true
                    holder.tvDISLIKES.text = (holder.tvDISLIKES.text.toString().toInt() + 1).toString()
                    item.dislikes++
                }
            }
        }

        override fun getItemCount(): Int {
            return postList.size
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val tvTITLE: TextView = itemView.findViewById(R.id.textViewPostTitle)
            val tvUSERNAME: TextView = itemView.findViewById(R.id.textViewPostUsername)
            val ivPOST: ImageView = itemView.findViewById(R.id.imageViewPost)
            val tvLIKES: TextView = itemView.findViewById(R.id.likeCounter)
            val tvDISLIKES: TextView = itemView.findViewById(R.id.dislikeCounter)
            val btnLike: ImageButton = itemView.findViewById(R.id.likeButton)
            val btnDislike: ImageButton = itemView.findViewById(R.id.dislikeButton)
//            init {
//                itemView.setOnClickListener{
//                    val intent = Intent(itemView.context, ViewActivity::class.java)
//                    intent.putExtra("userId",dataList[adapterPosition].userId.toString())
//                    intent.putExtra("id",dataList[adapterPosition].id.toString())
//                    intent.putExtra("title",dataList[adapterPosition].title)
//                    intent.putExtra("cid",dataList[adapterPosition].cid)
//                    itemView.context.startActivity(intent)
//                }
//            }
        }
    }
