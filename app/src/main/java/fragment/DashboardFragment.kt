package fragment

import adaptar.DashboardRecyclerAdaptar
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.lang.reflect.Array
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdaptar: DashboardRecyclerAdaptar


    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar : ProgressBar



    val bookInfoList = arrayListOf<Book>()

    val ratingComparator = Comparator<Book>{book1, book2 ->
       if(book1.bookRating.compareTo(book2.bookRating , true)==0){
           book1.bookName.compareTo(book2.bookName , true)
       }else{
           book1.bookRating.compareTo(book2.bookRating , true)
       }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)

        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE













        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null, Response.Listener {

                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if(success){
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )


                            bookInfoList.add(bookObject)

                            recyclerAdaptar =
                                DashboardRecyclerAdaptar(activity as Context , bookInfoList)

                            recyclerDashboard.adapter = recyclerAdaptar
                            recyclerDashboard.layoutManager = layoutManager







                        }

                    }else{
                        Toast.makeText(activity as Context,"Some Error Occured!",Toast.LENGTH_LONG).show()
                    }

                }catch (e : JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected Error Occured!",Toast.LENGTH_LONG).show()
                }


            }, Response.ErrorListener {
                if(activity != null){
                Toast.makeText(activity as Context,"Volley Error Occurred" , Toast.LENGTH_LONG).show()}

            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String , String>()

                    headers["Content-type"] = "application/json"
                    headers["token"] = "d8fe3166127045"
                    return headers



                }
            }
            queue.add(jsonObjectRequest)


        }else
        {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("success")
            dialog.setMessage("Internet NOT Connection Found")
            dialog.setPositiveButton("Open Settings"){
                    text,listener ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    activity?.finish()



            }
            dialog.setNegativeButton("Cancel"){
                    text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList , ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdaptar.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }



}
