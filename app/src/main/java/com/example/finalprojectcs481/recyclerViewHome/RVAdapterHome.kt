package com.example.finalprojectcs481.recyclerViewHome

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.postModelData.PostData
import com.squareup.picasso.Picasso


class RVAdapterHome(private val postList: List<PostData>): RecyclerView.Adapter<RVAdapterHome.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_home_row,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = postList[position]
//            holder.tvID.text = "ID: "+item.id.toString()
//            holder.tvUSERID.text = "userID: "+item.userId.toString()
//            holder.tvTITLE.text = "Title: "+item.title.toString()
//            holder.tvCOMPLETED.text = "Completed: "+item.completed.toString()
            holder.tvTITLE.text = item.title.toString()
            holder.tvUSERNAME.text = item.username.toString()
            Picasso.get().load(item.image).into(holder.ivPOST)

        }

        override fun getItemCount(): Int {
            return postList.size
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//            val tvID: TextView = itemView.findViewById(R.id.tvid);
//            val tvUSERID: TextView = itemView.findViewById(R.id.tvuserid);
//            val tvTITLE: TextView = itemView.findViewById(R.id.tvtitle);
//            val tvCOMPLETED: TextView = itemView.findViewById(R.id.tvcompleted)
            val tvTITLE: TextView = itemView.findViewById(R.id.textViewPostTitle)
            val tvUSERNAME: TextView = itemView.findViewById(R.id.textViewPostUsername)
            val ivPOST: ImageView = itemView.findViewById(R.id.imageViewPost)
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
