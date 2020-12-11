package com.ethicnology.symbionte

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethicnology.symbionte.FirebaseUtils.auth
import com.ethicnology.symbionte.TodoList.Todo_List
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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

        val checkAuth = Firebase.auth.currentUser
        if (checkAuth == null){
            Toast.makeText(this, "Authentication needed", Toast.LENGTH_SHORT).show()
            val gotoAuthentication = Intent(this, Authentication::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
            startActivity(gotoAuthentication)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(arrayOf("My Flatshare", "My Data", "Flatmates Map", "Todo Lists", "Calendar"), this)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun buttonDisconnect(view: View){
        auth = Firebase.auth
        auth.signOut()
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
        val gotoAuthentication = Intent(this, Authentication::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(gotoAuthentication)
    }


    override fun onCellClickListener(data: String) {
        Toast.makeText(this,data, Toast.LENGTH_SHORT).show()
        val activity = when(data){
            "My Data" -> PersonalDataManager::class.java
            "My Flatshare" -> MyFlatshare::class.java
            "Flatmates Map" -> FlatmatesLocation::class.java
            "Todo Lists" -> Todo_List::class.java
            "Calendar" -> Calendar::class.java
            else -> Incipit::class.java
        }
        val intent = Intent(this, activity).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(intent)
    }
}

class MyAdapter(private val myDataset: Array<String>,private val cellClickListener: CellClickListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = myDataset[position]
        holder.textView.text = data
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }
    }

    override fun getItemCount() = myDataset.size
}