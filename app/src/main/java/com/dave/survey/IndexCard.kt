package com.dave.survey

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_beacons.*
import kotlinx.android.synthetic.main.activity_index_card.*
import kotlinx.android.synthetic.main.activity_index_card.btnSave

class IndexCard : AppCompatActivity() {

    private var PICK_IMAGE_REQUEST = 1

    private lateinit var mImageUri: Uri
    private lateinit var progressDialog: ProgressDialog
    private val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("uploads");
    private val mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("index_cards");
    private var mUploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index_card)

        progressDialog = ProgressDialog(this)


        btnSave.setOnClickListener {

            val indexCardName = etIndexName.text.toString()

            if (!TextUtils.isEmpty(indexCardName)){
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }else
                etIndexName.error = "Index card name cannot be empty."

        }

        findViewById<Button>(R.id.btnView).setOnClickListener {

            val intent = Intent(this, ViewIndexCard::class.java)
            startActivity(intent)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data!!
            Picasso.get().load(mImageUri).into(imageView1)

            uploadPhoto(mImageUri)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadPhoto(mImageUri: Uri) {

        val indexCardName = etIndexName.text.toString()

        progressDialog.setTitle("Upload initiated.")
        progressDialog.setMessage("Please wait..")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val fileReference = mStorageRef.child(
            System.currentTimeMillis()
                .toString() + "." + getFileExtension(mImageUri)
        )

        mUploadTask = fileReference.putFile(mImageUri)
            .addOnSuccessListener {
                val handler = Handler()
                handler.postDelayed({
                    progressDialog.setProgress(0)
                }, 500)
                Toast.makeText(this@IndexCard, "Upload successful", Toast.LENGTH_LONG).show()

                fileReference.downloadUrl.addOnSuccessListener {

                    val newPost = mDatabaseRef.push()

                    newPost.child("index_card_name").setValue(indexCardName)
                    newPost.child("index_card_url").setValue(it.toString())
                    progressDialog.dismiss()

                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@IndexCard,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                progressDialog.setProgress(progress.toInt())
            }


    }
}