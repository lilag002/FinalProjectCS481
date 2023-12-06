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
import kotlinx.coroutines.tasks.await

interface PostDao {
    suspend fun getPostById(postId: String): Post?
    suspend fun getAllPosts(): List<Post>
    suspend fun addPost(post: Post)
    suspend fun updatePost(postId: String, post: Post)
    suspend fun deletePost(postId: String)
    suspend fun dislikePost(postId: String)
    suspend fun likePost(postId: String)
    suspend fun decLikePost(postId: String)
    suspend fun decDislikePost(postId: String)
}

class FirestorePostDao(private val db: FirebaseFirestore) : PostDao {
    private val postsCollection: CollectionReference = db.collection("Posts")
    private val userDao = FirestoreUserDao(FirebaseFirestore.getInstance())

    override suspend fun getPostById(postId: String): Post? {
        return postsCollection.document(postId).get().await().toObject(Post::class.java)
    }

    override suspend fun getAllPosts(): List<Post> {
        val likedSet = userDao.getLikedPostIds(FirebaseAuth.getInstance().uid.toString())
        val dislikedSet = userDao.getDislikedPostIds(FirebaseAuth.getInstance().uid.toString())
        val postList = postsCollection.orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
            .documents.map { document ->
                val postId = document.id
                var post = document.toObject(Post::class.java)
                post = post?.copy(id = postId) ?: Post(id = postId) // Update the Post object with the id

                // Modify the post based on liked and disliked sets
                when {
                    likedSet.contains(document.id) -> post.copy(liked = true)
                    dislikedSet.contains(document.id) -> post.copy(disliked = true)
                    else -> post // Return the original post object if neither liked nor disliked
                }
            }
//        }
        return postList
    }

    override suspend fun dislikePost(postId: String) {
        try {
            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get().await()

            // Get current value of dislikes
            var currentDislikes = postSnapshot.getLong("dislikes") ?: 0

            // Increment dislikes by 1
            currentDislikes++

            // Update dislikes field in Firestore
            postRef.update("dislikes", currentDislikes).await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            Log.e("Error PostDAO",e.message.toString())
        }
    }

    override suspend fun likePost(postId: String) {
        try {
            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get().await()

            // Get current value of likes
            var currentLikes = postSnapshot.getLong("likes") ?: 0

            // Increment likes by 1
            currentLikes++

            // Update likes field in Firestore
            postRef.update("likes", currentLikes).await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            Log.e("Error PostDAO",e.message.toString())
        }
    }

    override suspend fun decDislikePost(postId: String) {
        try {
            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get().await()

            // Get current value of dislikes
            var currentDislikes = postSnapshot.getLong("dislikes") ?: 0

            // Decrement dislikes by 1
            currentDislikes--

            // Update dislikes field in Firestore
            postRef.update("dislikes", currentDislikes).await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            Log.e("Error PostDAO",e.message.toString())
        }
    }

    override suspend fun decLikePost(postId: String) {
        try {
            val postRef = postsCollection.document(postId)
            val postSnapshot = postRef.get().await()

            // Get current value of likes
            var currentLikes = postSnapshot.getLong("likes") ?: 0

            // Decrement dislikes by 1
            currentLikes--

            // Update likes field in Firestore
            postRef.update("likes", currentLikes).await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            Log.e("Error PostDAO",e.message.toString())
        }
    }

    override suspend fun addPost(post: Post) {
        postsCollection.add(post).await()
    }

    override suspend fun updatePost(postId: String, post: Post) {
        postsCollection.document(postId).set(post).await()
    }

    override suspend fun deletePost(postId: String) {
        postsCollection.document(postId).delete().await()
    }


}

data class Post(
    val author: DocumentReference? = null,
    val id: String = "",
    val username: String = "",
    val date: Timestamp? = null,
    val title: String = "",
    val image: String = "",
    val forum: DocumentReference? = null,
    var likes: Int = 0,
    var dislikes: Int = 0,
    var liked: Boolean = false,
    var disliked: Boolean = false
) {
    // No-argument constructor
    constructor() : this(null, "","", null, "", "", null, 0, 0,false,false)
}