package android.ella.assistant

import android.ella.assistant.databinding.ActivityMainBinding
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    lateinit var mBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initBottomNavigationView()



        
    }


    private fun initBottomNavigationView(){
        val navControl = findNavController(R.id.fragment_container)
        mBinding.bottomNavigationView.setupWithNavController(navControl)
    }




}