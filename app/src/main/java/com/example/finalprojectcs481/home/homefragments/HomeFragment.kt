package com.example.finalprojectcs481.home.homefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.postModelData.PostData
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var posts: ArrayList<PostData> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.homeRecyclerView)
        getPosts()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = RVAdapterHome(posts)
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

    private fun getPosts(){
        for ( i in 1..10){
            posts.add(PostData("Test Title","Test_User",R.drawable.goku))
        }
    }

}