package com.example.finalprojectcs481.home.homefragments

//import RecyclerViewforProfilePage.adapter


import RecyclerViewforProfilePage.ForumDataAPIItem
import RecyclerViewforProfilePage.ProfileRVAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.login.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    // Make a database instance
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("tag", "RIGHT HERE ASSHOLE");
        // Setting username
        db.collection("users").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.findViewById<TextView>(R.id.usernameTextView).text = it.result.data?.getValue("username").toString()
                }
            }
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.ProfileForumRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter here
        adapter = ProfileRVAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchDataFromFirestore()

        view.findViewById<TextView>(R.id.logOutTextView).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, WelcomeActivity::class.java)
            startActivity(intent)
        }

        /*
        val dataList = listOf(
            ForumDataAPIItem("FISHERS MEAT", listOf(R.drawable.fishing)),
            ForumDataAPIItem("Alla aTrout Fishin", listOf(R.drawable.fishing2)),
            ForumDataAPIItem("YELLOW SEA", listOf(R.drawable.fishing3)),
            ForumDataAPIItem("THAR SHE BLOWS", listOf(R.drawable.fishing4)),
            ForumDataAPIItem("Spear Fishers", listOf(R.drawable.fishing5)),
            //ForumDataAPIItem("LAND HOEs", listOf(R.drawable.fishing6))
            // Add more items as needed
        )
        val adapter = ProfileRVAdapter(dataList)

        // Create and set up the adapter and layout manager for the RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         */


    }



    // this is to grab the data!
    fun fetchDataFromFirestore() {
        db.collection("Forums")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val dataList = mutableListOf<ForumDataAPIItem>()

                for (document in querySnapshot) {
                    val forumTitle = document.getString("Title") ?: ""
                    val forumImageUrlsString = document.getString("image") ?: ""

                    // Split the comma-separated string into a list of integers
                    val forumImageUrls = forumImageUrlsString.split(",")

                    Log.d("ProfileFragment", "ForumTitle: $forumTitle, image: $forumImageUrls")

                    val forumDataItem = ForumDataAPIItem(forumTitle, forumImageUrls)
                    dataList.add(forumDataItem)
                }

                // Log the size and content of dataList
                Log.d("ProfileFragment", "DataList size: ${dataList.size}")
                Log.d("ProfileFragment", "DataList content: $dataList")

                // Update the adapter data and notify the changes
                adapter.setData(dataList)
                /*
                val adapter = ProfileRVAdapter(dataList)
                recyclerView.adapter = adapter

                 */
            }
            .addOnFailureListener { exception ->
                // Handle failures
                // For example, you can show an error message
                Toast.makeText(
                    requireContext(),
                    "Error fetching data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}