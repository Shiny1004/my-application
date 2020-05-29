package com.jc.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.jc.foodapp.R
import com.jc.foodapp.fragment.*
import kotlinx.android.synthetic.main.drawer_header.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView:NavigationView
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var maintoolbar: androidx.appcompat.widget.Toolbar
    lateinit var bottomnavigationbar:BottomNavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var nameglobal:String
    lateinit var useridglobal:String
    lateinit var txtnamedrawerheader:TextView
    lateinit var txtnodrawerheader:TextView
    lateinit var txtiddrawerheader:TextView

    var previousMenuItem: MenuItem?=null//can be null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(getString(R.string.preferences_login),Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        maintoolbar=findViewById(R.id.maintoolbar)
        bottomnavigationbar=findViewById(R.id.bottomnavigationbar)

        val userid=intent.getStringExtra("user_id")
        val name = intent.getStringExtra("name")
        val email=intent.getStringExtra("email")
        val mobilenumber=intent.getStringExtra("mobile_number")
        val address=intent.getStringExtra("address")

        useridglobal=userid
        nameglobal=name

       val hview=navigationView.getHeaderView(0)
        txtnamedrawerheader=hview.findViewById<TextView>(R.id.txtnamedrawerheader)
        txtnodrawerheader=hview.findViewById(R.id.txtnodrawerheader)
        txtiddrawerheader=hview.findViewById(R.id.txtiddrawerheader)

        txtnamedrawerheader.text=name
        txtnodrawerheader.text="+91-${mobilenumber}"
        txtiddrawerheader.text="Unique-ID: ${userid}"

        setUpToolbar(name)//setting up the toolbar we created in layoutfile

        val b=Bundle()
        b.putString("user_id",userid)
        val f = HomeFragment()
        f.arguments=b
        f.setHasOptionsMenu(false)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, f)
            .commit()
        bottomnavigationbar.menu.findItem(R.id.txthome).setChecked(true)
        
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,R.string.close_drawer)//actionbar toggle

        drawerLayout.addDrawerListener(actionBarDrawerToggle)//adds a click listener to the home button
        actionBarDrawerToggle.syncState()//syncs the home button with the navigation drawer

        navigationView.setNavigationItemSelectedListener {
            //adding click listeners to the menu items on navigation drawer
            if(previousMenuItem != null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            //highlights the currently clicked menuitem
            previousMenuItem=it

            bottomnavigationbar.menu.findItem(R.id.notchecked).setChecked(true)

            when(it.itemId)
            {
                R.id.txtallrestaurants ->{
                    val bundle=Bundle()
                    bundle.putString("user_id",userid)
                    val frag=AllRestaurantsFragment()
                    frag.arguments=bundle
                    frag.setHasOptionsMenu(true)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,frag)
                        .commit()
                    supportActionBar?.show()
                   supportActionBar?.title="All Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.txtfavorites->
                {
                    val bundle=Bundle()
                    bundle.putString("user_id",userid)
                    val frag=FavoritesFragment()
                    frag.arguments=bundle
                    frag.setHasOptionsMenu(false)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,frag)
                        .commit()
                    supportActionBar?.show()
                   supportActionBar?.title="Favorites"
                    drawerLayout.closeDrawers()
                }
                R.id.txtorderhistory->
                {
                    val bundle=Bundle()
                    bundle.putString("user_id",userid)
                    val frag = OrderHistoryFragment()
                    frag.arguments=bundle
                    frag.setHasOptionsMenu(false)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,frag)
                        .commit()
                    supportActionBar?.show()
                    supportActionBar?.title="Order history"
                    drawerLayout.closeDrawers()

                }
                R.id.txtfaq->
                {
                    val frag=FaqFragment()
                    frag.setHasOptionsMenu(false)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,frag)
                        .commit()
                    supportActionBar?.show()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()

                }
                R.id.txtlogout->
                {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Logout")
                    dialog.setMessage("Are you sure you want to logout?")
                    dialog.setPositiveButton("Yes"){text,Listener->

                        //clearing sharedPreferences
                        sharedPreferences.edit().remove("name").apply()
                        sharedPreferences.edit().remove("email").apply()
                        sharedPreferences.edit().remove("mobile_number").apply()
                        sharedPreferences.edit().remove("address").apply()
                        sharedPreferences.edit().remove("isLoggedIn").apply()

                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){text,Listener->
                        //do nothing - closes the ALERT
                        it.isChecked=false
                    }
                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        bottomnavigationbar.setOnNavigationItemSelectedListener {
            if(previousMenuItem != null)
            {
                previousMenuItem?.isChecked=false
            }
            when(it.itemId)
            {
                R.id.txthome->
                {
                    val bundle=Bundle()
                    bundle.putString("user_id",userid)
                    val frag = HomeFragment()
                    frag.arguments=bundle
                    frag.setHasOptionsMenu(false)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, frag)
                        .commit()
                    supportActionBar?.show()
                    supportActionBar?.title="Hi $name !"
                }
                R.id.txtsearch->
                {
                    val bundle=Bundle()
                    bundle.putString("user_id",userid)
                    val frag = SearchFragment()
                    frag.arguments=bundle
                    frag.setHasOptionsMenu(false)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, frag)
                        .commit()
                    supportActionBar?.hide()
                }
                R.id.txtabout->
                {
                    val frag=AboutFragment()
                    frag.setHasOptionsMenu(false)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,frag )
                        .commit()
                    supportActionBar?.show()
                    supportActionBar?.title=""
                }
                R.id.txtprofile->
                {
                    val bundle = Bundle()
                    bundle.putString("name",name)
                    bundle.putString("email",email)
                    bundle.putString("mobile_number",mobilenumber)
                    bundle.putString("address",address)
                    val  f = ProfileFragment()
                    f.arguments=bundle
                    f.setHasOptionsMenu(false)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,f)
                        .commit()
                    supportActionBar?.show()
                    supportActionBar?.title=""
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
    fun setUpToolbar(name:String?)
    {
        setSupportActionBar(maintoolbar)
        supportActionBar?.title="Hi $name !"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Makes navigation drawer to slide from left
        val id=item.itemId

        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed()
    {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(frag)
        {
            !is HomeFragment ->
            {
                val bundle=Bundle()
                bundle.putString("user_id",useridglobal)
                val f=HomeFragment()
                f.arguments=bundle
                f.setHasOptionsMenu(false)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, f)
                    .commit()
                supportActionBar?.title="Hi $nameglobal !"
                supportActionBar?.show()
                bottomnavigationbar.menu.findItem(R.id.txthome).isChecked=true
                if(previousMenuItem != null)
                {
                    previousMenuItem?.isChecked=false
                }

            }
            else-> super.onBackPressed()
        }

    }
}
