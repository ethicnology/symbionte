package com.ethicnology.symbionte

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface CellClickListener {
    fun onCellClickListener(data: String)
}

const val EXTRA_MESSAGE = "com.ethicnology.symbionte.MESSAGE"

class Incipit : AppCompatActivity(), CellClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.incipit)
        viewManager = LinearLayoutManager(this)

        viewAdapter = MyAdapter(arrayOf("Incipit", "Authentication", "PersonalDataManager", "FlatmatesLocation","Todo_List", "FlatshareManager", "Else"), this)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }


    override fun onCellClickListener(data: String) {
        Toast.makeText(this,data, Toast.LENGTH_SHORT).show()
        val activity = when(data){
            "Authentication" -> Authentication::class.java
            "PersonalDataManager" -> PersonalDataManager::class.java
            "Todo_List" -> Todo_List::class.java
            "FlatshareManager" -> FlatshareManager::class.java
            "FlatmatesLocation" -> FlatmatesLocation::class.java
            else -> Incipit::class.java
        }
        val intent = Intent(this, activity).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(intent)
    }
}

class MyAdapter(private val myDataset: Array<String>,private val cellClickListener: CellClickListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element[
        val data = myDataset[position]
        holder.textView.text = data
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}