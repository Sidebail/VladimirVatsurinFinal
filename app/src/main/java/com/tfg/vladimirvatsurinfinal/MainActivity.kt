package com.tfg.vladimirvatsurinfinal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_nav.*

class MainActivity : AppCompatActivity() {

    val user = FirebaseAuth.getInstance().currentUser

    var db = FirebaseFirestore.getInstance()

    private var adapter: PlaceAdapter? = null;

    private lateinit var markerLocations: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar2)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.color.colorPrimary))

        ib_logout.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    // redirect to SignInActivity
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }
        }

        recyclerView.setLayoutManager(LinearLayoutManager(this));

        val query = db.collection("places").orderBy("name", Query.Direction.ASCENDING);

        val options = FirestoreRecyclerOptions.Builder<Place>().setQuery(query, Place::class.java).build();


        adapter = PlaceAdapter(options);

        recyclerView.adapter = adapter

        tv_welcome.setText("Welcome, " + user!!.displayName)
        tv_email.setText(""+user!!.email)

        markerLocations = ArrayList()
    }
    override fun onStart() {
        super.onStart()
        adapter!!.startListening();
    }

    override fun onStop(){
        super.onStop();
        adapter!!.stopListening()
    }

    inner class PlaceViewHolder internal constructor(private val view: View): RecyclerView.ViewHolder(view){

        /**
        internal fun displayArtist(artistName: String, artistGenre: String){
        val textViewName = view.findViewById<TextView>(R.id.tv_name)
        val textViewGenre = view.findViewById<TextView>(R.id.tv_genre)

        textViewGenre.text = artistGenre;
        textViewName.text = artistName;

        }
         */
    }

    private inner class PlaceAdapter internal constructor(options: FirestoreRecyclerOptions<Place>) : FirestoreRecyclerAdapter<Place,
            PlaceViewHolder>(options){
        override fun onBindViewHolder(p0: PlaceViewHolder, p1: Int, p2: Place) {
            p0.itemView.findViewById<TextView>(R.id.id_itemName).text = p2.name;
            p0.itemView.findViewById<TextView>(R.id.b_itemUrl).setOnClickListener {
                val url = p2.url.toString()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            p0.itemView.findViewById<TextView>(R.id.b_itemMap).setOnClickListener {
                val intent = Intent(applicationContext, MapsActivity::class.java)
                intent.putExtra("markerLat",p2.latitude)
                intent.putExtra("markerLong",p2.longitude)
                intent.putExtra("markerName",p2.name)

                startActivity(intent)
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.partial_item, parent, false)
            return PlaceViewHolder(view) }
    }


}
