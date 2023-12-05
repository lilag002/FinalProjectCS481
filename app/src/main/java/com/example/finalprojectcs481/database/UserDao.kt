package com.example.finalprojectcs481.database

import android.util.Log
import com.example.finalprojectcs481.postModelData.PostData
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

interface UserDao {
//    suspend fun getPostById(postId: String): Post?
//    suspend fun getAllPosts(): List<Post>
//    suspend fun addPost(post: Post)
//    suspend fun updatePost(postId: String, post: Post)
//    suspend fun deletePost(postId: String)
    suspend fun getLikedPostIds(userId: String): Set<String>
    suspend fun getDislikedPostIds(userId: String): Set<String>
}

class FirestoreUserDao(private val db: FirebaseFirestore) : UserDao {
    private val userCollection: CollectionReference = db.collection("users")

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

//    override suspend fun getPostById(postId: String): Post? {
//        return postsCollection.document(postId).get().await().toObject(Post::class.java)
//    }
//
//    override suspend fun getAllPosts(): List<Post> {
//        return postsCollection.orderBy("date",Query.Direction.DESCENDING).get().await().toObjects(Post::class.java)
//    }
//
//    override suspend fun addPost(post: Post) {
//        postsCollection.add(post).await()
//    }
//
//    override suspend fun updatePost(postId: String, post: Post) {
//        postsCollection.document(postId).set(post).await()
//    }
//
//    override suspend fun deletePost(postId: String) {
//        postsCollection.document(postId).delete().await()
//    }

}