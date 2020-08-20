package kr.hs.dgsw.dbook

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.hs.dgsw.dbook.ui.fragment.BookListFragment
import kr.hs.dgsw.dbook.ui.fragment.MyLibraryFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navagation)
        fm = supportFragmentManager
        val bnw = findViewById<BottomNavigationView>(R.id.nav_view)
        bnw.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = RecyclerViewFragment()
            transaction.replace(R.id.frame, fragment)
            transaction.commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_book_list -> fm.beginTransaction().replace(R.id.frame, BookListFragment()).commit()
            R.id.navigation_my_library -> fm.beginTransaction().replace(R.id.frame, MyLibraryFragment()).commit()
        }
        return true
    }
}