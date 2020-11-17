package kr.hs.dgsw.dbook

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.model.libraryResponse
import kr.hs.dgsw.dbook.ui.fragment.BookListFragment
import kr.hs.dgsw.dbook.ui.fragment.EXTRA_CATEGORY_ID
import kr.hs.dgsw.dbook.ui.fragment.MyLibraryFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var fm = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navagation)
        val api = (application as DBookApplication)
        // val view = findViewById<RecyclerView>(R.layout.fragment_book_list,savedInstanceState)
        val lm = LinearLayoutManager(applicationContext)
        //view.book_list_recyclerview.adapter = CustomAdapter(databaseList())
       // view.book_list_recyclerview.setHasFixedSize(true)
       // view.book_list_recyclerview.layoutManager = lm
        intent?.data?.asMultipart("test",contentResolver)
        fm = supportFragmentManager
        val bnw = findViewById<BottomNavigationView>(R.id.nav_view)
        bnw.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = BookListFragment()
            fragment.arguments = Bundle().apply{
                putString(EXTRA_CATEGORY_ID, "0")
            }
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }

        /*api.requestService()
                ?.getLibrary()
                ?.enqueue(object : Callback<libraryResponse>{
            override fun onResponse(call: Call<libraryResponse>, response: Response<libraryResponse>) {
              Log.d("success","success"+response.body()?.message)
            }

            override fun onFailure(call: Call<libraryResponse>, t: Throwable) {

            }

        })*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_book_list -> fm.beginTransaction().replace(R.id.nav_host_fragment, BookListFragment()).commit()
            R.id.navigation_my_library -> fm.beginTransaction().replace(R.id.nav_host_fragment, MyLibraryFragment()).commit()
        }
        return true
    }

}