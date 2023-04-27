package com.example.iskcon

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.iskcon.CategoryModel
import com.example.iskcon.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var epoxyControllerCategory: CategoryController
    val categories = ArrayList<String>()
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Loading")
        FirebaseQuery.firestore = FirebaseFirestore.getInstance()
        categories.add("New Student")
        categories.add("Take Attendance")
        categories.add("Attendance Record")
        categories.add("Feedback")
        setRecyclerView()
        getTotalStudents()
    }
    fun getTotalStudents(){
        progressDialog!!.show()
        val callback = object : FirebaseQuery.FirestoreCallback3 {
            override fun onDataReceived(data: Int) {
                // Do something with the data
                Log.d(ContentValues.TAG, "Data received: $data")
                binding.count.text=data.toString()
            }
        }
        FirebaseQuery.getTotal(callback,object : MyCompleteListener{
            override fun onSuccess() {
                progressDialog!!.dismiss()
            }

            override fun onFailure() {
                progressDialog!!.dismiss()
            }

        })
    }
    fun setRecyclerView() {
        binding.categoryList.apply {
            layoutManager = LinearLayoutManager(context)
        }
        epoxyControllerCategory = CategoryController(this)
        binding.categoryList.setController(epoxyControllerCategory)
        epoxyControllerCategory.setData(categories)
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//    private fun setFragment(fragment: Fragment) {
//            }

}

class CategoryController(private val context: MainActivity) :
    TypedEpoxyController<ArrayList<String>>() {
    override fun buildModels(data: ArrayList<String>) {
        data.forEach { myData ->
            CategoryModel_(context)
                .id(myData)
                .data(myData)
                .addTo(this)
        }
    }
}
