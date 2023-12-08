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
import com.example.finalprojectcs481.database.FirestorePostDao
import com.example.finalprojectcs481.database.FirestoreUserDao
import com.example.finalprojectcs481.database.Post
import com.example.finalprojectcs481.postModelData.PostData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class RVAdapterHome(private val postList: List<Post>): RecyclerView.Adapter<RVAdapterHome.ViewHolder>() {
        private val postDao = FirestorePostDao(FirebaseFirestore.getInstance())
        private val userDao = FirestoreUserDao(FirebaseFirestore.getInstance())
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
                CoroutineScope(Dispatchers.Main).launch {
                    if (!item.liked) {
                        if (item.disliked) {
                            holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24)
                            item.disliked = false
                            holder.tvDISLIKES.text =
                                (holder.tvDISLIKES.text.toString().toInt() - 1).toString()
                            postDao.decDislikePost(item.id) // database update
                            userDao.removeDislikedPost(item.id) // database update
                            item.dislikes--
                        }
                        holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24_click)
                        item.liked = true
                        holder.tvLIKES.text =
                            (holder.tvLIKES.text.toString().toInt() + 1).toString()
                        postDao.likePost(item.id)   // database update
                        userDao.addLikedPost(item.id)   // database update
                        item.likes++
                    }
                    else{
                        holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24)
                        postDao.decLikePost(item.id)    //database update
                        userDao.removeLikedPost(item.id)    // database update
                        item.likes--
                        holder.tvLIKES.text =
                            (holder.tvLIKES.text.toString().toInt() - 1).toString()
                        item.liked = false
                    }
                }
            }
            holder.btnDislike.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!item.disliked) {
                        if (item.liked) {
                            holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24)
                            item.liked = false
                            holder.tvLIKES.text =
                                (holder.tvLIKES.text.toString().toInt() - 1).toString()
                            postDao.decLikePost(item.id)
                            userDao.removeLikedPost(item.id)
                            item.likes--
                        }
                        holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24_click)
                        item.disliked = true
                        holder.tvDISLIKES.text =
                            (holder.tvDISLIKES.text.toString().toInt() + 1).toString()
                        postDao.dislikePost(item.id)
                        userDao.addDislikedPost(item.id)
                        item.dislikes++
                    }
                    else{
                        holder.btnDislike.setImageResource(R.drawable.baseline_thumb_down_alt_24)
                        postDao.decDislikePost(item.id)
                        userDao.removeDislikedPost(item.id)
                        item.dislikes--
                        holder.tvDISLIKES.text =
                            (holder.tvDISLIKES.text.toString().toInt() - 1).toString()
                        item.disliked = false
                    }
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
            init {
            }
        }
    }
