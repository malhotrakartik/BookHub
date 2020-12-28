package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.example.bookhub.R.id.dashboard
import com.google.android.material.navigation.NavigationView
import fragment.AboutFragment
import fragment.DashboardFragment
import fragment.FavouriteFragment
import fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinateLayout : CoordinatorLayout
    lateinit var appToolbar : androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView : NavigationView

    var previousMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinatorLayout)
        appToolbar = findViewById(R.id.appToolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        setUpToolBar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity ,
        drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }
            it.isChecked = true
            this.previousMenuItem = it

            when(it.itemId){
                dashboard -> {
                    openDashboard()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()

                    supportActionBar?.title = "Favourites"
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Profile"
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            AboutFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "About"
                }

            }


            return@setNavigationItemSelectedListener true
        }

    }
    private fun setUpToolBar(){
        setSupportActionBar(appToolbar)
        supportActionBar?.title = "title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if(id == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()

             transaction
            .replace(R.id.frameLayout,fragment)

            .commit()
        drawerLayout.closeDrawers()

        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(dashboard)

    }

//

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }
    }


}
