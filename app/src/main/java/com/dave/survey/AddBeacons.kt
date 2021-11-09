package com.dave.survey

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_beacons.*
import kotlinx.android.synthetic.main.alert_dialog.*
import java.io.*


class AddBeacons : AppCompatActivity() {

    private val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference("uploads");
    private val mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("survey");
    private lateinit var mImageUri: Uri
    private var mUploadTask: StorageTask<*>? = null
    private lateinit var progressDialog: ProgressDialog
    private var PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beacons)

        progressDialog = ProgressDialog(this)

        val formatter = Formatter()
        formatter.customBottomNavigation(this)
        btnDrawText.setOnClickListener {

            showDialogText()

        }

        btnCapturePhotos.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }

//        uploadFile()

        btnSave.setOnClickListener {

            val beaconName = etBeaconName.text.toString()
            val beaconDetails = etBeaconDetails.text.toString()
            val beaconAdditionalDetails = etAdditionalDetails.text.toString()

            if (!TextUtils.isEmpty(beaconName)
                && !TextUtils.isEmpty(beaconDetails)
                && !TextUtils.isEmpty(beaconAdditionalDetails)
                && mImageUri != null){

                uploadFile()

            }else{

                Toast.makeText(this@AddBeacons, "Something is wrong", Toast.LENGTH_LONG).show()


            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data!!
            Picasso.get().load(mImageUri).into(imageView)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }
    private fun uploadFile() {
        if (mImageUri != null) {

            val beaconName = etBeaconName.text.toString()
            val beaconDetails = etBeaconDetails.text.toString()
            val beaconAdditionalDetails = etAdditionalDetails.text.toString()

            progressDialog.setTitle("Upload initiated.")
            progressDialog.setMessage("Please wait..")
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
                    Toast.makeText(this@AddBeacons, "Upload successful", Toast.LENGTH_LONG).show()

                    fileReference.downloadUrl.addOnSuccessListener {

                        val newPost = mDatabaseRef.child("beacon_details").push()

                        val beaconDetails1 = BeaconDetails(
                            beaconName, beaconDetails, beaconAdditionalDetails, it.toString(),"", ""
                        )

                        newPost.setValue(beaconDetails1)
                        progressDialog.dismiss()

                        etBeaconName.setText("")
                        etBeaconDetails.setText("")
                        etAdditionalDetails.setText("")

                    }

                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this@AddBeacons,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressDialog.setProgress(progress.toInt())
                }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialogText() {

        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.alert_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)


        val mSignaturePad: SignaturePad = promptsView.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared

            }
        })

        // set dialog message
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save Drawing") { dialog: DialogInterface?, id: Int ->

                val signatureBitmap = mSignaturePad.signatureBitmap
                mImageUri = getImageUri(signatureBitmap)
                Picasso.get().load(mImageUri).into(imageView)
                if (addJpgSignatureToGallery(signatureBitmap)){
                    Toast.makeText(this, "Drawing saved." , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Drawing not saved." , Toast.LENGTH_SHORT).show()

                }

            }
            .setNegativeButton(
                "Clear Drawing"
            ) { dialog: DialogInterface, id: Int -> dialog.cancel() }

        // create alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
    fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun getAlbulmStorageDir(albumName: String?): File? {
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
        }
        return file
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            val photo = File(
                getAlbulmStorageDir("SignaturePad"),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }
    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this@AddBeacons.sendBroadcast(mediaScanIntent)
    }
}