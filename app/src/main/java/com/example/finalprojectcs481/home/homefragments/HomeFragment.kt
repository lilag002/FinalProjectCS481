package com.example.finalprojectcs481.home.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.database.FirestorePostDao
import com.example.finalprojectcs481.database.Post
import com.example.finalprojectcs481.postModelData.PostData
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var posts: ArrayList<PostData> = ArrayList()
    //private val recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val db = FirebaseFirestore.getInstance()
        db.collection("Posts").orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener{
                val result: StringBuffer = StringBuffer()
                if(it.isSuccessful){
                    posts.clear()
                    for(document in it.result!!){
                        posts.add(PostData(document.data.getValue("title").toString(),
                            document.data.getValue("username").toString(),document.data.getValue("image").toString()))
                        Log.d("document",document.data.getValue("image").toString())
                    }
                    recyclerView.adapter = RVAdapterHome(posts)
                }
            }

        //recyclerView.adapter = RVAdapterHome(posts)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

//    private fun getPosts() {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("Posts").orderBy("date", Query.Direction.ASCENDING)
//            .get()
//            .addOnCompleteListener{
//                val result: StringBuffer = StringBuffer()
//                if(it.isSuccessful){
//                    posts.clear()
//                    for(document in it.result!!){
//                        posts.add(PostData(document.data.getValue("title").toString(),
//                            document.data.getValue("username").toString(),document.data.getValue("image").toString()))
//                        Log.d("document",document.data.getValue("image").toString())
//                    }
//                }
//            }
//
//
//    }

//    private fun getPosts(){
//        for ( i in 1..10){
//            posts.add(PostData("Test Title","Test_User",R.drawable.goku))
//        }
//        val db = FirebaseFirestore.getInstance()
//        val postDao = FirestorePostDao(db)
//
//        val myPostList : List<Post> = postDao.getAllPosts()
//    }

}