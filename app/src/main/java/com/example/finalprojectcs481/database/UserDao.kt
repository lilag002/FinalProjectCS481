package com.example.finalprojectcs481.database

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

interface UserDao {
    suspend fun getLikedPostIds(userId: String): Set<String>
    suspend fun getDislikedPostIds(userId: String): Set<String>
    suspend fun addLikedPost(postId: String)
    suspend fun removeLikedPost(postId: String)
    suspend fun addDislikedPost(postId: String)
    suspend fun removeDislikedPost(postId: String)
}

class FirestoreUserDao(private val db: FirebaseFirestore) : UserDao {
    private val userCollection: CollectionReference = db.collection("users")
    private val postsCollection: CollectionReference = db.collection("Posts")

    override suspend fun getLikedPostIds(userId: String): Set<String> {
        try {
            // Get the QuerySnapshot from the dislikedPosts collection
            val snapshot = userCollection.document(userId)
                .collection("likedPosts")
                .get()
                .await()

            val likedPostIds = mutableSetOf<String>()
            // Map the documents to their references
            for (document in snapshot.documents) {
                val postReference = document.get("post") as? DocumentReference
                postReference?.let {
                    val documentId = postReference.id // Retrieve the document ID
                    likedPostIds.add(documentId) // Add document ID to the set
                }
            }
            return likedPostIds
        } catch (e: Exception) {
            // Handle any potential exceptions here
            e.printStackTrace()
            // Return an empty list or throw the exception based on your error handling logic
            return emptySet()
        }
    }

    override suspend fun getDislikedPostIds(userId: String): Set<String> {
        try {
            // Get the QuerySnapshot from the dislikedPosts collection
            val snapshot = userCollection.document(userId)
                .collection("dislikedPosts")
                .get()
                .await()
            // Create a set to store document IDs
            val dislikedPostIds = mutableSetOf<String>()

            for (document in snapshot.documents) {
                val postReference = document.get("post") as? DocumentReference
                postReference?.let {
                    val documentId = postReference.id // Retrieve the document ID
                    dislikedPostIds.add(documentId) // Add document ID to the set
                }
            }
            return dislikedPostIds
        } catch (e: Exception) {
            // Handle any potential exceptions here
            e.printStackTrace()
            // Return an empty set or throw the exception based on your error handling logic
            return emptySet()
        }
    }

    override suspend fun addLikedPost(postId: String) {
        try {
            // Get the current user's ID (You might have a method to fetch this)
            val userId = FirebaseAuth.getInstance().uid.toString()

            // Create a reference to the likedPosts collection in the user's document
            val likedPostsCollection = userCollection.document(userId)
                .collection("likedPosts")

            // Create a reference to the post using postId
            val postReference = postsCollection.document(postId)

            // Create a document in the likedPosts collection with a field 'post' containing the postReference
            likedPostsCollection.add(mapOf("post" to postReference))
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeLikedPost(postId: String) {
        try {
            // Get the current user's ID
            val userId = FirebaseAuth.getInstance().uid.toString()

            // Reference to the likedPosts collection in the user's document
            val likedPostsCollection = userCollection.document(userId)
                .collection("likedPosts")

            // Query to find the specific document containing the 'post' field with postId reference
            val query = likedPostsCollection.whereEqualTo("post", postsCollection.document(postId))

            // Get the query snapshot to check if the document exists
            val snapshot = query.get().await()

            if (!snapshot.isEmpty) {
                // Loop through the documents found by the query (should be just one)
                for (document in snapshot.documents) {
                    // Delete the document(s) found in the query
                    likedPostsCollection.document(document.id).delete().await()
                }
            } else {
                Log.e("ERROR UserDAO","Post with cid $postId not found")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ERROR UserDAO",e.message.toString())
        }
    }

    override suspend fun addDislikedPost(postId: String) {
        try {
            // Get the current user's ID (You might have a method to fetch this)
            val userId = FirebaseAuth.getInstance().uid.toString()

            // Create a reference to the dislikedPosts collection in the user's document
            val likedPostsCollection = userCollection.document(userId)
                .collection("dislikedPosts")

            // Create a reference to the post using postId
            val postReference = postsCollection.document(postId)

            // Create a document in the dislikedPosts collection with a field 'post' containing the postReference
            likedPostsCollection.add(mapOf("post" to postReference))
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ERROR UserDAO",e.message.toString())
        }
    }

    override suspend fun removeDislikedPost(postId: String) {
        try {
            // Get the current user's ID
            val userId = FirebaseAuth.getInstance().uid.toString()

            // Reference to the dislikedPosts collection in the user's document
            val likedPostsCollection = userCollection.document(userId)
                .collection("dislikedPosts")

            // Query to find the specific document containing the 'post' field with postId reference
            val query = likedPostsCollection.whereEqualTo("post", postsCollection.document(postId))

            // Get the query snapshot to check if the document exists
            val snapshot = query.get().await()

            if (!snapshot.isEmpty) {
                // Loop through the documents found by the query (should be just one)
                for (document in snapshot.documents) {
                    // Delete the document(s) found in the query
                    likedPostsCollection.document(document.id).delete().await()
                }
            } else {
                Log.e("ERROR UserDAO","Post with cid $postId not found")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ERROR UserDAO",e.message.toString())
        }
    }



}