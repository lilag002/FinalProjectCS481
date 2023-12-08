package com.example.finalprojectcs481.home.homefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.finalprojectcs481.R
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.database.FirestorePostDao
import com.example.finalprojectcs481.database.FirestoreUserDao
import com.example.finalprojectcs481.database.Post
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForumPosts.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForumPosts : Fragment() {
    private var posts: ArrayList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.buttonBackSearch).setOnClickListener {
            val newFragment = SearchFragment() // Replace with the fragment you want to navigate to
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fContainerView, newFragment) // Replace R.id.fragment_container with the ID of your fragment container
            transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            transaction.commit()
        }



        val forumTitle = arguments?.getString("Forum")

        view.findViewById<TextView>(R.id.ForumPostsTitle).text = (forumTitle+"\nCommunity Forum")

        val recyclerView: RecyclerView = view.findViewById(R.id.forumPostRV)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        lifecycleScope.launch {
            try {
                val postDao = FirestorePostDao(FirebaseFirestore.getInstance())
                val myPostList = forumTitle?.let { postDao.getAllPostsByForum(it) }

                posts.clear()
                if (myPostList != null) {
                    posts.addAll(myPostList)
                }
                for(i in posts){
                    Log.d("POST",i.title)
                }

                val adapter = RVAdapterHome(posts)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged() // Notify adapter of data changes
            } catch (e: Exception) {
                Log.e("ERROR", e.toString())
            }
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForumPosts.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForumPosts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}