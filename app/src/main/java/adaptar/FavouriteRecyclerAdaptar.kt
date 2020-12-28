package adaptar

import activity.DescriptionActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.R.*
import com.squareup.picasso.Picasso
import database.BookEntity
import kotlinx.android.synthetic.main.recycler_favourite_single_row.view.*


        class FavouriteRecyclerAdaptar(val context: Context, val bookList: List<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdaptar.FavouriteViewHolder>() {
            class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {



            val txtBookTitle : TextView = view.findViewById(R.id.txtFavBookTitle)
                val txtBookAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
                val txtBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
                val txtBookRating : TextView = view.findViewById(R.id.txtFavBookRating)
                val imgBookImage : ImageView = view.findViewById(R.id.imgFavBookImage)
                val llContent: LinearLayout = view.findViewById(R.id.llFavContent)


            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row , parent ,false)
                 return FavouriteViewHolder(view)

            }

            override fun getItemCount(): Int {
                return bookList.size
            }

            override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
                val book = bookList[position]

                holder.txtBookTitle.text = book.bookName
                holder.txtBookAuthor.text = book.bookAuthor
                holder.txtBookPrice.text = book.bookPrice
                holder.txtBookRating.text = book.bookRating
                Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
                println(book.bookName)



                holder.llContent.setOnClickListener {
                    Toast.makeText(context , "yes" , Toast.LENGTH_LONG).show()
                    println(book.book_id)
                    println(book.bookName)

                    var num:Int = book.book_id
                    var str:String = num. toString()


                    val intent = Intent(context , DescriptionActivity::class.java)
                    intent.putExtra("book_id" , str)
                    context.startActivity(intent)


                }



            }


        }



