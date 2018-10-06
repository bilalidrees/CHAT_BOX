package com.example.bilalidrees.chat_box.Admin

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bilalidrees.chat_box.Adapters.admin_seleciton_adapter
import com.example.bilalidrees.chat_box.R
import com.example.bilalidrees.chat_box.User.User_data
import com.example.bilalidrees.chat_box.Welcome
import com.example.bilalidrees.chat_box.chatlist.chat_list
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class Admin_seleciton_chat : AppCompatActivity() {

    private lateinit var  mMessageRecyclerview: RecyclerView

    private lateinit var  mMessageAdapter: RecyclerView.Adapter<*>


    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var  mMessageEditText: EditText
    private lateinit var  mSendButton: Button

    private lateinit var  mUsername:String

    private lateinit var  mFirebaseDatabase: FirebaseDatabase
    private lateinit var  mMessagesDatabaseReference: DatabaseReference
    private  var  mChildEventListener: ChildEventListener?=null

    private lateinit var  mFirebaseAuth: FirebaseAuth
    private var  mAuthStateListener: FirebaseAuth.AuthStateListener?= null

    var adminlist = ArrayList<User_data>()
    var userlist = ArrayList<User_data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_seleciton_chat)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        mMessageRecyclerview = findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(this)

        mMessageRecyclerview.layoutManager=layoutManager


        if(mFirebaseAuth.currentUser!!.email.equals("admin@gmail.com")){

            mMessageAdapter= admin_seleciton_adapter(this, R.layout.item_message,adminlist,{ position, user->

                Toast.makeText(this@Admin_seleciton_chat, "you  clicked "+position, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, chat_list::class.java)

//            intent.putExtra("postion",position)
                intent.putExtra("user_name",user.name)
                intent.putExtra("user_email",user.email)
                intent.putExtra("user_id",user.uid)



                startActivity(intent)
            })

        }
        else{
            mMessageAdapter= admin_seleciton_adapter(this, R.layout.item_message,userlist,{ position, user->

                Toast.makeText(this@Admin_seleciton_chat, "you  clicked "+position, Toast.LENGTH_SHORT).show()




                val intent = Intent(this, chat_list::class.java)
//            intent.putExtra("postion",position)
                intent.putExtra("user_name",user.name)
                intent.putExtra("user_email",user.email)
                intent.putExtra("user_id",user.uid)

                startActivity(intent)

            }

            )

        }




        mMessageRecyclerview .adapter=mMessageAdapter




        mAuthStateListener=FirebaseAuth.AuthStateListener {

            var  user=it.currentUser

            if(user!=null){

                mMessagesDatabaseReference = mFirebaseDatabase.reference.child("Users")
                Log.v("REFRENCE",mMessagesDatabaseReference.toString())

                Toast.makeText(this@Admin_seleciton_chat, "you are  signed in, welocme to todolist", Toast.LENGTH_SHORT).show()

                attachDatabaseReadListener()

            }
        }
    }

    override fun onBackPressed() {

        val a_builder = AlertDialog.Builder(this@Admin_seleciton_chat)
        a_builder.setMessage("Do you want to Close this App !!!")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which -> finish() }
                .setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alert = a_builder.create()
        alert.setTitle("Alert !!!")
        alert.show()


    }

    override fun onPause() {

        super.onPause()
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener!!)

        }
        detachDatabaseReadListener()
        adminlist.clear()
            userlist.clear()
    }


    override fun onResume() {

        super.onResume()

        mFirebaseAuth.addAuthStateListener(mAuthStateListener!!)
    }




    private fun attachDatabaseReadListener() {
        if (mChildEventListener == null) {

        mChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val fm = dataSnapshot.getValue<User_data>(User_data::class.java)

                if(mFirebaseAuth.currentUser!!.email.equals("admin@gmail.com")){


                    if(!mFirebaseAuth.currentUser!!.uid.equals(fm!!.uid)){
                        adminlist.add(fm!!)

                    }


                }
                else{

                    if(!mFirebaseAuth.currentUser!!.uid.equals(fm!!.uid)){

                        userlist.add(fm!!)
                    }

                   //

                }
                mMessageAdapter.notifyDataSetChanged()


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                val fm = dataSnapshot.getValue<User_data>(User_data::class.java)

                userlist.remove(fm)
                mMessageAdapter.notifyDataSetChanged()

                Toast.makeText(this@Admin_seleciton_chat, "data deleted", Toast.LENGTH_SHORT).show()

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener!!)
    }

    }


    private fun detachDatabaseReadListener() {

        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener!!)
            mChildEventListener = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_welcome, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Signout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, Welcome::class.java))

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
