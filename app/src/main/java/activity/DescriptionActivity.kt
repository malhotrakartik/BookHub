package activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.squareup.picasso.Picasso
import database.BookDatabase
import database.BookEntity
import kotlinx.android.synthetic.main.activity_description.*
import org.json.JSONObject
import util.ConnectionManager
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName : TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating : TextView
    lateinit var imgBookImage : ImageView
    lateinit var txtBookDesc : TextView
    lateinit var btnAddToFav : Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    var bookId : String? ="100"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        imgBookImage = findViewById(R.id.imgBookImage)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"



        if(intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else{
            finish()
            Toast.makeText(this@DescriptionActivity , "null error occured" ,Toast.LENGTH_LONG).show()
        }
        if(bookId == "100"){
            finish()
            Toast.makeText(this@DescriptionActivity , "bookId 100 error occured" ,Toast.LENGTH_LONG).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)


        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")

                        if (success) {
                            val bookJSONObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            Picasso.get().load(bookJSONObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)

                            val bookImageUrl = bookJSONObject.getString("image")
                            txtBookName.text = bookJSONObject.getString("name")
                            txtBookAuthor.text = bookJSONObject.getString("author")
                            txtBookPrice.text = bookJSONObject.getString("price")
                            txtBookRating.text = bookJSONObject.getString("rating")
                            txtBookDesc.text = bookJSONObject.getString("description")

                            val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext , bookEntity , mode = 1).execute()
                            val isFav = checkFav.get()
                            if(isFav){
                                btnAddToFav.text = "Remove From Faviourites"
                                val favColor = ContextCompat.getColor(applicationContext , R.color.colorFavourite)

                                btnAddToFav.setBackgroundColor(favColor)
                            }else
                            {
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor = ContextCompat.getColor(applicationContext , R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)

                            }
                            btnAddToFav.setOnClickListener {

                                if(!DBAsyncTask(applicationContext , bookEntity, mode = 1).execute().get()){

                                    val async = DBAsyncTask(applicationContext , bookEntity,mode = 2).execute()
                                    val result = async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity , "Book Added to Favourites" , Toast.LENGTH_LONG).show()
                                        btnAddToFav.text = "Remove From Favourites"
                                        val favColor = ContextCompat.getColor(applicationContext , R.color.colorFavourite)

                                        btnAddToFav.setBackgroundColor(favColor)

                                    }else{
                                        Toast.makeText(this@DescriptionActivity , "unexpected error occured" ,Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    val async = DBAsyncTask(applicationContext , bookEntity,mode = 3).execute()
                                    val result = async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity , "Book Removed From Favourites" , Toast.LENGTH_LONG).show()
                                        btnAddToFav.text = "Add To Favourites"
                                        val noFavColor = ContextCompat.getColor(applicationContext , R.color.colorPrimary)

                                        btnAddToFav.setBackgroundColor(noFavColor)

                                    }else{
                                        Toast.makeText(this@DescriptionActivity , "unexpected error occured" ,Toast.LENGTH_LONG).show()
                                    }

                                }
                            }


                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "this error occured",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        println("unlucky")
                        Toast.makeText(
                            this@DescriptionActivity,
                            "some error occured",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley error occured $it",
                        Toast.LENGTH_LONG
                    ).show()


                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d8fe3166127045"
                        return headers

                    }

                }
            queue.add(jsonRequest)
        }else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("success")
            dialog.setMessage("Internet NOT Connection Found")
            dialog.setPositiveButton("Open Settings"){
                    text,listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()



            }
            dialog.setNegativeButton("Cancel"){
                    text,listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }



    }
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity ,val mode : Int) : AsyncTask<Void ,Void, Boolean>(){

          /*mode 1 = check db if book is fav or not
           mode 2 = add to fav
           mode 3 = remove from fav*/

        val db = Room.databaseBuilder(context , BookDatabase::class.java, "book-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1 ->{

                    val book : BookEntity? =db.bookDao().getBooksById(bookEntity.book_id.toString())
                    db.close()
                    return book!= null        //will retunr false if no book is present

                }

                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }
                3->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }


            }
            return false
        }

    }
}


