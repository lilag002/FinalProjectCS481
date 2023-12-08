package com.example.finalprojectcs481.home.homefragments

import RecyclerViewforProfilePage.ForumDataAPIItem
import RecyclerViewforProfilePage.ProfileRVAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.database.FirestoreForumDao
import com.example.finalprojectcs481.database.FirestoreUserDao
import com.example.finalprojectcs481.database.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileRVAdapter
    private val forumDao = FirestoreForumDao(FirebaseFirestore.getInstance())
    private var forums: ArrayList<ForumDataAPIItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            recyclerView = view.findViewById(R.id.searchRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            forums.clear()

            forums.addAll(forumDao.getAllForums())

            adapter = ProfileRVAdapter(forums, requireActivity().supportFragmentManager)
            recyclerView.adapter = adapter
        }

        view.findViewById<ImageButton>(R.id.imageButtonSearch).setOnClickListener {
            lifecycleScope.launch {
                val query = view.findViewById<TextView>(R.id.textInputSearch).text.toString()
                if(query.isNotEmpty()){
                    forums.clear()
                    forums.addAll(forumDao.searchForums(query))
                    adapter.setData(forums)
                    adapter.notifyDataSetChanged()
                }
                else{
                    lifecycleScope.launch {
                        forums.clear()
                        forums.addAll(forumDao.getAllForums())
                        adapter.setData(forums)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


}