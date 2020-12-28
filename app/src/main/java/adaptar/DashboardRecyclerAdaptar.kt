package adaptar

import activity.DescriptionActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.squareup.picasso.Picasso
import model.Book

class DashboardRecyclerAdaptar(
    val context: Context,

    val itemList : ArrayList<Book>
) : RecyclerView.Adapter<DashboardRecyclerAdaptar.DashboardViewHolder>() {

 
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        println(itemList)
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookCost.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)

        holder.rlContent.setOnClickListener {



            val intent = Intent(context , DescriptionActivity::class.java)
            intent.putExtra("book_id" , book.bookId)
            context.startActivity(intent)


        }


    }
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val txtBookName : TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val txtBookCost : TextView = view.findViewById(R.id.txtBookCost)
        val txtBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val rlContent : RelativeLayout = view.findViewById(R.id.rlContent)


    }

}


