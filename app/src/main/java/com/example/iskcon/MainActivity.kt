package com.example.iskcon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.iskcon.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var main_frame: FrameLayout? = null
    private val onNavigationItemSelectedListener =
        label@ NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_devotee -> {
                    setFragment(CreateDevoteeProfile())
                    return@OnItemSelectedListener true
                }
                R.id.navigation_attendance -> {
                    setFragment(TakeAttendance())
                    return@OnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    setFragment(ProfileFragment())
                    return@OnItemSelectedListener true
                }
                else -> {}
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        FirebaseQuery.firestore= FirebaseFirestore.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setTitle("ISKCON")
        main_frame = findViewById(R.id.main_frame)
        val bottom_NavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottom_NavigationView.setOnItemSelectedListener(onNavigationItemSelectedListener)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


//        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
//        navigationView.setNavigationItemSelectedListener(this)

//        val drawerProfileName =
//            navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_name)
//        val drawerProfileText =
//            navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_text_img)

        //val name: String = DbQuery.myProfile.getName()
        //drawerProfileName.text = name

        //drawerProfileText.text = name.uppercase(Locale.getDefault()).substring(0, 1)

        setFragment(CreateDevoteeProfile())

//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )

    }
    fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_home) {
            setFragment(CreateDevoteeProfile())
        } else if (id == R.id.navigation_attendance) {
            setFragment(TakeAttendance())
        } else if (id == R.id.navigation_profile) {
            setFragment(ProfileFragment())
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
    main_frame?.let { transaction.replace(it.getId(), fragment) }
        transaction.commit()
    }

}