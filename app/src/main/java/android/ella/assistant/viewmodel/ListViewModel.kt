package android.ella.assistant.viewmodel

import android.R.attr.start
import android.content.ContentValues
import android.ella.assistant.entity.Assistant
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.ExecutionException
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

    }

    fun uploadImage(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos)
        val data: ByteArray = baos.toByteArray()

        val imageName = UUID.randomUUID().toString()
        val tempRef: StorageReference = imgRef.child(imageName)
        val uploadTask: UploadTask = tempRef.putBytes(data)
        uploadTask.addOnSuccessListener {
        }

        return imageName
    }

    private fun downloadImage(position: Int?, count: Int = 0) {
        if (position != null && position < list.size && position >= 0 && list[position].imageName != null) {
            val tempRef = imgRef.child(list[position].imageName!!)
            tempRef.getBytes(1024 * 1024).addOnSuccessListener { byteArray ->
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).also {
                    list[position].imageBitmap = it
                    assistants.value = list
                }
            }.addOnFailureListener {
                if (count < 3) {
                    downloadImage(position, count + 1)
                    runBlocking {
                        delay(1000)
                    }
                }
            }
        }
    }


    private fun uploadItems() {

        listRef.setValue(list)
    }


    private val listListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val tList = dataSnapshot.getValue<ArrayList<Assistant>>()
            if (tList != null) {
                list.clear()
                list.addAll(tList)
                for (i in 1..list.size) {
                    downloadImage(i - 1)
                }
            } else {
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

    val assistantPos: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            it.value = null
        }
    }


    val list = ArrayList<Assistant>()


    fun getAssistants(): ArrayList<Assistant> {
        return assistants.value!!
    }

    fun getAssistantsLiveData(): LiveData<ArrayList<Assistant>> {
        return assistants
    }

    fun addAssistant(a: Assistant) {
        a.imageName = uploadImage(a.imageBitmap!!)
        list.add(a)
        uploadItems()
    }

    fun removeAssistant() {
        list.removeAt(assistantPos.value!!)
        uploadItems()
    }

    fun editAssistant(a: Assistant) {
        val pos = assistantPos.value!!
        a.imageName = uploadImage(a.imageBitmap!!)
        list[pos] = a
        uploadItems()
    }

    private fun fillList(): ArrayList<Assistant> {
        return list
    }


}