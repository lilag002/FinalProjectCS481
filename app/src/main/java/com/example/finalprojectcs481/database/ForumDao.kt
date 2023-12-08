package com.example.finalprojectcs481.database

import RecyclerViewforProfilePage.ForumDataAPIItem
import android.widget.Toast

import android.util.Log
import com.example.finalprojectcs481.postModelData.PostData
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

interface ForumDao {
    suspend fun getAllForums():List<ForumDataAPIItem>
    suspend fun searchForums(query :String):List<ForumDataAPIItem>
    suspend fun getAllForumTitles():List<String>
}

class FirestoreForumDao(private val db: FirebaseFirestore) : ForumDao {
    private val formsCollection: CollectionReference = db.collection("Forums")

    override suspend fun getAllForums():List<ForumDataAPIItem> {
        try {
            val snapshot = formsCollection.get().await()

            val dataList = mutableListOf<ForumDataAPIItem>()

            for (document in snapshot) {
                val forumTitle = document.getString("Title") ?: ""
                val forumImageUrlsString = document.getString("image") ?: ""

                // Split the comma-separated string into a list of integers
                val forumImageUrls = forumImageUrlsString.split(",")

                Log.d("ProfileFragment", "ForumTitle: $forumTitle, image: $forumImageUrls")

                val forumDataItem = ForumDataAPIItem(forumTitle, forumImageUrls)
                dataList.add(forumDataItem)
            }
            return dataList
        }catch (e: Exception){
            Log.e("ERROR ForumDAO",e.message.toString())
            return emptyList()
        }
    }

    override suspend fun getAllForumTitles(): List<String> {
        try {
            val snapshot = formsCollection.get().await()

            val dataList = mutableListOf<String>()

            for (document in snapshot) {
                val forumTitle = document.getString("Title") ?: ""
                dataList.add(forumTitle)
            }
            return dataList
        }catch (e: Exception){
            Log.e("ERROR ForumDAO",e.message.toString())
            return emptyList()
        }
    }

    override suspend fun searchForums(query: String): List<ForumDataAPIItem> {
            val forumsList = mutableListOf<ForumDataAPIItem>()

            try {
                // Query Firestore for forums with a title that matches the search query
                val querySnapshot = formsCollection
                    .whereEqualTo("Title", query) // or use 'whereArrayContains' if it's an array of titles
                    .get()
                    .await()

                for (document in querySnapshot) {
                    val forumTitle = document.getString("Title") ?: ""
                    val forumImageUrlsString = document.getString("image") ?: ""

                    // Split the comma-separated string into a list of integers
                    val forumImageUrls = forumImageUrlsString.split(",")

                    Log.d("ProfileFragment", "ForumTitle: $forumTitle, image: $forumImageUrls")

                    val forumDataItem = ForumDataAPIItem(forumTitle, forumImageUrls)
                    forumsList.add(forumDataItem)
                }
            } catch (e: Exception) {
                Log.e("FirestoreDao", "Error searching forums", e)
            }

            return forumsList
    }
}