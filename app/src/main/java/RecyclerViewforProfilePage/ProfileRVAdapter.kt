package RecyclerViewforProfilePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.squareup.picasso.Picasso

class ProfileRVAdapter(private var dataList: List<ForumDataAPIItem>) : RecyclerView.Adapter<ProfileRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.recycler_view_row_profilepage, parent, false) // grabs the row layout!!!
        return ViewHolder(view)
    }

    // for binding our data to views: gets called whenever the recycler view needs to display data.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.tvFORUMTITLE.text = "${item.forumTitle} Community Forum" //+ item.forumTitle.toString() // located in DataAPIItem

        // Clear the existing image views in the container
        holder.imageContainer.removeAllViews()

        // Assuming you have an ImageView in your ViewHolder for displaying images
        // You may need to customize this part based on your layout structure
        for (imageUrl in item.forumImageUrls) {
            val imageView = ImageView(holder.itemView.context)
            Picasso.get().load(imageUrl).into(imageView)//imageView.setImageResource(imageResId)
            holder.imageContainer.addView(imageView)

        }
    }

    // for how many items we have to display
    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvFORUMTITLE: TextView = itemView.findViewById(R.id.tvForumTitle)
        var imageContainer: LinearLayout = itemView.findViewById(R.id.imageContainer)

        init {
            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "You have clicked ${dataList[adapterPosition].forumTitle}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setData(newData: List<ForumDataAPIItem>) {
        val diffResult = DiffUtil.calculateDiff(ProfileDiffCallback(dataList, newData))
        dataList = newData
        diffResult.dispatchUpdatesTo(this)
    }
    inner class ProfileDiffCallback(
        private val oldList: List<ForumDataAPIItem>,
        private val newList: List<ForumDataAPIItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].forumTitle == newList[newItemPosition].forumTitle
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}