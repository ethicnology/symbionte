package com.ethicnology.symbionte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethicnology.symbionte.FirebaseUtils.getCurrentFlatshare
import com.ethicnology.symbionte.FirebaseUtils.getUser

class MyFlatshare : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var listUser: List<User>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_flatshare)
        getCurrentFlatshare { flatshare ->
            updateFlatshareUI(flatshare)
            flatshare.admin?.let { user ->
                getUser(user){
                    updateAdminUI(it)
                }
            }
            flatshare.members?.forEach(){
                getUser(it){
                    listUser = listUser?.plus(it)
                    viewManager = LinearLayoutManager(this)
                    viewAdapter = listUser?.let { UserAdapter(it) }!!
                    recyclerView = findViewById<RecyclerView>(R.id.recycler_view2).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
            }
        }
    }

    private fun updateFlatshareUI(flatshare: Flatshare){
        findViewById<TextView>(R.id.textViewFlatshareName).text = flatshare.name
        findViewById<TextView>(R.id.textViewFlatshareId).text = flatshare.id
    }

    private fun updateAdminUI(admin: User){
        findViewById<TextView>(R.id.textViewAdminFirst).text = admin.first
        findViewById<TextView>(R.id.textViewAdminLast).text = admin.last
    }

    fun buttonFlatsharePlus(view: View){
        val gotoFlatshareManager = Intent(this, FlatshareManager::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(gotoFlatshareManager)
    }
}

class UserAdapter(private val dataSet: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userFirst: TextView = view.findViewById(R.id.userFirst)
        val userLast: TextView = view.findViewById(R.id.userLast)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.userFirst.text = dataSet[position].first
        viewHolder.userLast.text = dataSet[position].last
    }

    override fun getItemCount() = dataSet.size
}




