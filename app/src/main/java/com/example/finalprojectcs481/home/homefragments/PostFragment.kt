package com.example.finalprojectcs481.home.homefragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.finalprojectcs481.R
import com.example.finalprojectcs481.database.FirestoreForumDao
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    private lateinit var image: ImageView
    private lateinit var uri: Uri
    private lateinit var title: EditText
    private var titles: ArrayList<String> = arrayListOf()
    private val forumDao = FirestoreForumDao(FirebaseFirestore.getInstance())
    private var imageSet = false
    private lateinit var forumSelection: String
    private lateinit var forumDocReference: DocumentReference




    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image = view.findViewById<ImageView>(R.id.imageViewUpload)
        title = view.findViewById<EditText>(R.id.textInputTitle)
        val spinner: Spinner = view.findViewById(R.id.spinnerDropdown)


        val storageRef = FirebaseStorage.getInstance()
        val firestoreRef = FirebaseFirestore.getInstance()


        lifecycleScope.launch{
            titles.addAll(forumDao.getAllForumTitles())
            titles.add(0,"No Forum")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle item selection here
                forumSelection = titles[position]
                // Do something with the selected item
                if(forumSelection != "No Forum"){
                    val firestoreRef = FirebaseFirestore.getInstance()

                    // Query Firestore to find the document by title
                    val forumsCollection = firestoreRef.collection("Forums")
                    forumsCollection.whereEqualTo("Title",forumSelection)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                // Assuming you want to set the "forum" field in a specific document
                                val forumDocument = documents.documents[0] // Assuming only one document matches the title

                                // Set the "forum" field in your Firestore document
                                 forumDocReference = forumDocument.reference
                                // Update the Firestore document using post map or however you're updating it
                            } else {
                                // Handle case where no document matches the title
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors that occur during the query
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }



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
                                        if(forumSelection != "No Forum") {
                                            post["forum"] = forumDocReference
                                        }
                                        post["image"] = imageUrl
                                        post["title"] = title.text.toString()
                                        post["username"] = username
                                        firestoreRef.collection("Posts")
                                            .add(post)
                                            .addOnSuccessListener {
                                                Log.d("dbfirebase success","saved ${post}")
                                                Toast.makeText(context, "successfully posted!", Toast.LENGTH_SHORT).show();
                                                createNotificationChannel("string")
                                                sendNotification(title.text.toString(),uri)
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
    private fun createNotificationChannel(title: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Post Channel"
            val descriptionText = "Channel for post notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("C10", name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
    private fun sendNotification(title: String, imageUri: Uri){
            val con = "Congratulations On Your Post: "

            val imageBitmap = uriToBitmap(imageUri)

            val builder = NotificationCompat.Builder(requireContext(), "C10")
                .setSmallIcon(R.drawable.navy)
                .setContentTitle(con+title)
                .setLargeIcon(imageBitmap)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(imageBitmap)
                    .bigLargeIcon(imageBitmap))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

           with(NotificationManagerCompat.from(requireContext())){
               notify(10,builder.build())
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
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








