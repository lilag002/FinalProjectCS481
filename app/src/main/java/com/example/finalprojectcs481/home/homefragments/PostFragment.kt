package com.example.finalprojectcs481.home.homefragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var uri: Uri
    private lateinit var title: EditText
    private var imageSet = false

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image = view.findViewById<ImageView>(R.id.imageViewUpload)
        title = view.findViewById<EditText>(R.id.textInputTitle)


        val storageRef = FirebaseStorage.getInstance()
        val firestoreRef = FirebaseFirestore.getInstance()


        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            imageSet = true
            image.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        view.findViewById<Button>(R.id.buttonBrowse).setOnClickListener {
            galleryImage.launch("image/*")
        }
        view.findViewById<Button>(R.id.buttonUpload).setOnClickListener {
            if(imageSet && title.text.isNotEmpty()) {
                var imageUrl = ""
                var username = ""
                storageRef.getReference("posts").child(System.currentTimeMillis().toString())
                    .putFile(uri)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                imageUrl = it.toString()
                                firestoreRef.collection("users").document(FirebaseAuth.getInstance().uid.toString()).get()
                                    .addOnCompleteListener{ it1->
                                        val result: StringBuffer = StringBuffer()
                                        if(it1.isSuccessful){
                                            username = it1.result.data?.getValue("username").toString()
                                        }
                                        val post: MutableMap<String,Any> = HashMap()
                                        post["author"] = firestoreRef.collection("users").document(FirebaseAuth.getInstance().uid.toString())
                                        post["date"] = Timestamp.now()
                                        post["dislikes"] = 0
                                        post["likes"] = 0
                                        //post["forum"] = firestoreRef.collection("Forums").document("xJW49CFXDkkpLUs3zVWL")
                                        post["image"] = imageUrl
                                        post["title"] = title.text.toString()
                                        post["username"] = username
                                        firestoreRef.collection("Posts")
                                            .add(post)
                                            .addOnSuccessListener {
                                                Log.d("dbfirebase success","saved ${post}")
                                                Toast.makeText(context, "successfully posted!", Toast.LENGTH_SHORT).show();
                                                image.setImageResource(R.drawable.white)
                                                imageSet = false
                                                title.text.clear()
                                            }
                                            .addOnFailureListener{
                                                Log.d("dbfirebase failed","${post}")
                                                Toast.makeText(context, "database save failure", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                            }
                    }


            }
            else{
                Toast.makeText(context,"Please fill out the fields",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

}