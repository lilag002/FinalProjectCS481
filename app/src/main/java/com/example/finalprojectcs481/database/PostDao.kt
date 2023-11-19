package com.example.finalprojectcs481.database

import android.util.Log
import com.example.finalprojectcs481.postModelData.PostData
import com.example.finalprojectcs481.recyclerViewHome.RVAdapterHome
import com.google.firebase.Timestamp
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

    override suspend fun getPostById(postId: String): Post? {
        return postsCollection.document(postId).get().await().toObject(Post::class.java)
    }

    override suspend fun getAllPosts(): List<Post> {
        return postsCollection.orderBy("date",Query.Direction.ASCENDING).get().await().toObjects(Post::class.java)
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
    val username: String = "",
    val date: Timestamp? = null,
    val title: String = "",
    val image: String = "",
    val forum: DocumentReference? = null,
    val likes: Int = 0,
    val dislikes: Int = 0
) {
    // No-argument constructor
    constructor() : this(null, "", null, "", "", null, 0, 0)
}