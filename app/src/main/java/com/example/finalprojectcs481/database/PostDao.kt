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

//        for (i in postList) {
//            Log.d("Post", i.disliked.toString())
//        }
//        for (i in likedSet) {
//            Log.d("likedSet", i.toString())
//        }
//        for (i in dislikedSet) {
//            Log.d("dislikedSet", i.toString())
//        }
        return postList
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