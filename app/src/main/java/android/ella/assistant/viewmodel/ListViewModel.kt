package android.ella.assistant.viewmodel

import android.ella.assistant.entity.Assistant
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    private val assistants: MutableLiveData<ArrayList<Assistant>> by lazy {
        MutableLiveData<ArrayList<Assistant>>().also {
            it.value = fillList()
        }
    }


    fun getAssistants(): ArrayList<Assistant>?{
        return assistants.value
    }

    private fun fillList(): ArrayList<Assistant> {
        val list: ArrayList<Assistant> = ArrayList()
        list.add(Assistant(name = "Chika", description = "Wp"))
        list.add(Assistant(name = "Aqua", description = "Useless"))
        list.add(Assistant(name = "Megumin", description = "loli"))
        return list
    }

}