package fragment

import adaptar.FavouriteRecyclerAdaptar
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.R
import database.BookDatabase
import database.BookEntity
import kotlinx.android.synthetic.main.fragment_favourite.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdaptar: FavouriteRecyclerAdaptar
    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite =view.findViewById(R.id.recyclerFavourite)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)

        layoutManager = GridLayoutManager(activity as Context , 2)
        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null){
            progressLayout.visibility =View.GONE
            recyclerAdaptar = FavouriteRecyclerAdaptar(activity as Context , dbBookList)
            recyclerFavourite.layoutManager = layoutManager
            recyclerFavourite.adapter = recyclerAdaptar

        }





        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void , Void ,List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {

            val db = Room.databaseBuilder(context , BookDatabase::class.java, "book-db").build()

            return db.bookDao().getAllBooks()

        }
    }

}
