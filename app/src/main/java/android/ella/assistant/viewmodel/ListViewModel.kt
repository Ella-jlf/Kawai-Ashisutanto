package android.ella.assistant.viewmodel

import android.content.ContentValues
import android.ella.assistant.adapter.ListAdapter
import android.ella.assistant.entity.Assistant
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class ListViewModel : ViewModel() {

    companion object FirebaseDAO {
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private val dbRef: DatabaseReference = database.getReference("db_assistant")
        private val listRef: DatabaseReference = dbRef.child("assistants")
        private val storage = FirebaseStorage.getInstance()
        private val stRef: StorageReference = storage.getReference("st_assistant")
        private val imgRef = stRef.child("images")
        private val vdRef = stRef.child("videos")


        fun uploadImage(bitmap: Bitmap):String{
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos)
            val data: ByteArray = baos.toByteArray()

            val imageName = UUID.randomUUID().toString()
            val tempRef: StorageReference = imgRef.child(imageName)
            val uploadTask: UploadTask = tempRef.putBytes(data)
            uploadTask.addOnFailureListener {}
                .addOnSuccessListener { taskSnapshot -> // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    val downloadUrl: Uri? = taskSnapshot.uploadSessionUri
                }

            return imageName
        }
        fun downloadImage(imageName:String?){
            if (imageName == null) return
            val tempRef = imgRef.child(imageName)
            tempRef.getBytes(1024*1024).addOnSuccessListener { byteArray ->
                BitmapFactory.decodeByteArray(byteArray,0,byteArray.size).also {

                }
            }
        }

    }



    private fun uploadItems(){

        listRef.setValue(assistants.value)
    }



    private val listListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val tList = dataSnapshot.getValue<ArrayList<Assistant>>()
            if (tList != null) {
                list.clear()
                list.addAll(tList)
            }else{
                list.clear()
            }
            assistants.value = list
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
        }

    }.also {
        listRef.addValueEventListener(it)
    }


    private val assistants: MutableLiveData<ArrayList<Assistant>> by lazy {
        MutableLiveData<ArrayList<Assistant>>().also {
            it.value = fillList()
        }
    }
    val list  = ArrayList<Assistant>()


    fun getAssistants(): ArrayList<Assistant> {
        return assistants.value!!
    }

    fun getAssistantsLiveData():LiveData<ArrayList<Assistant>>{
        return assistants
    }

    fun addAssistant(a: Assistant) {
        list.add(a)
        assistants.value = list
        uploadItems()
    }


    private fun fillList(): ArrayList<Assistant> {
        list.add(Assistant(name = "Chika", description = "Wp"))
        list.add(Assistant(name = "Aqua", description = "Useless"))
        list.add(Assistant(name = "Megumin", description = "loli"))
        return list
    }

}