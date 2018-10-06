package com.example.bilalidrees.chat_box.chatlist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bilalidrees.chat_box.Adapters.chat_list_adapter
import com.example.bilalidrees.chat_box.R
import com.example.bilalidrees.chat_box.User.User_data
import com.example.bilalidrees.chat_box.Welcome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class chat_list : AppCompatActivity() {

    private lateinit var  mMessageRecyclerview: RecyclerView

    private lateinit var  mMessageAdapter: RecyclerView.Adapter<*>

    internal lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var  mMessageEditText: EditText
    private lateinit var  mSendButton: Button

    private lateinit var  mUsername:String

    private lateinit var  mFirebaseDatabase: FirebaseDatabase
    private lateinit var  mMessagesDatabaseReference: DatabaseReference
    private  var  mChildEventListener: ChildEventListener?=null
    private  var  muserEventListener: ChildEventListener?=null

    private lateinit var mUserDatabaseReference: DatabaseReference
    private lateinit var  mFirebaseAuth: FirebaseAuth
    private lateinit var  mAuthStateListener:FirebaseAuth.AuthStateListener
    var msglist = ArrayList<chat_data>()


    private  var NNAME:String?=null
    private lateinit var position:String
    private lateinit var getuname:String
    private lateinit var getuemail:String
    private lateinit var getuid:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()


        //position= intent.extras.getString("postion")

        if(mFirebaseAuth.currentUser!!.email.equals("admin@gmail.com")){

            getuname= intent.extras.getString("user_name")
            getuemail= intent.extras.getString("user_email")
            getuid= intent.extras.getString("user_id")
        }
        else{
            getuname= intent.extras.getString("user_name")
            getuemail= intent.extras.getString("user_email")
            getuid= intent.extras.getString("user_id")

        }



        mMessageRecyclerview = findViewById(R.id.recyclerview) as RecyclerView
        layoutManager = LinearLayoutManager(this)
        mMessageRecyclerview.layoutManager=layoutManager


        mMessageEditText=findViewById(R.id.messageEditText) as EditText
        mSendButton = findViewById(R.id.sendButton) as Button



        //  var msg : List<Message>=Array
        mMessageAdapter=chat_list_adapter(this,R.layout.item_message,msglist,{
            Toast.makeText(this@chat_list, "you  clicked "+it, Toast.LENGTH_SHORT).show()

        })

        mMessageRecyclerview.adapter=mMessageAdapter


        mSendButton.setOnClickListener(View.OnClickListener {

            if(mFirebaseAuth.currentUser!!.email.equals("admin@gmail.com")){

                var   mymsg=chat_data("Admin",mMessageEditText.text.toString(),mFirebaseAuth.uid)

                mMessagesDatabaseReference.push().setValue(mymsg)
                mMessageEditText.setText("")
                Toast.makeText(this@chat_list, "data  pushed!", Toast.LENGTH_SHORT).show()

            }
            else{

                    push()
            }

        })

        mAuthStateListener=FirebaseAuth.AuthStateListener {
            var  user=it.currentUser

            if(user!=null){

                if(user.email.equals("admin@gmail.com")){
                    mMessagesDatabaseReference = mFirebaseDatabase.reference.child("Chat").child(getuid+""+user.uid)

                    Log.v("RRRREEEFRRREMNCE",mMessagesDatabaseReference.toString())
                    //Toast.makeText(this@MainActivity, "you are  signed in, welocme to todolist", Toast.LENGTH_SHORT).show()
                    attachDatabaseReadListener()
                }
                else{

                    if(getuid.equals("97nACSrARRhNlF8kFmEPnMuHlHo2")){

                        mMessagesDatabaseReference = mFirebaseDatabase.reference.child("Chat").child(user.uid+""+"97nACSrARRhNlF8kFmEPnMuHlHo2")
                        Log.v("RRRREEEFRRREMNCE",mMessagesDatabaseReference.toString())
                        //Toast.makeText(this@MainActivity, "you are  signed in, welocme to todolist", Toast.LENGTH_SHORT).show()
                        attachDatabaseReadListener()
                    }
                    else{

                        var get=user.uid.compareTo(getuid)
                        if(get<0){

                            mMessagesDatabaseReference = mFirebaseDatabase.reference.child("Chat").child(getuid+""+user.uid)
                            Log.v("RRRREEEFRRREMNCE",mMessagesDatabaseReference.toString())
                        }
                        else if(get>0){

                            mMessagesDatabaseReference = mFirebaseDatabase.reference.child("Chat").child(user.uid+""+getuid)
                            Log.v("RRRREEEFRRREMNCE",mMessagesDatabaseReference.toString())
                        }
                        attachDatabaseReadListener()
                    }


                }
            }
        }
    }


    private fun push(){

        mUserDatabaseReference = mFirebaseDatabase!!.getReference().child("Users")

        Log.v("GETTTTTETETTE",mUserDatabaseReference.toString())



            muserEventListener = object : ChildEventListener {

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val fm = dataSnapshot.getValue<User_data>(User_data::class.java)

                    if (fm!!.uid.equals(mFirebaseAuth.currentUser!!.uid)) {

                        NNAME= fm.name!!
                        Log.v("NNNNAAAAMMMEEE",NNAME)
                        push_data(NNAME!!)

                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            mUserDatabaseReference.addChildEventListener(muserEventListener!!)






    }
    private fun  push_data(name:String){

        var   mymsg=chat_data(name,mMessageEditText.text.toString(),mFirebaseAuth.uid)

        mMessagesDatabaseReference.push().setValue(mymsg)
        mMessageEditText.setText("")
        Toast.makeText(this@chat_list, "data  pushed!", Toast.LENGTH_SHORT).show()
    }



    private fun attachDatabaseReadListener() {
        if (mChildEventListener == null) {

            mChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val fm = dataSnapshot.getValue<chat_data>(chat_data::class.java)

                    msglist.add(fm!!)
                    mMessageAdapter.notifyDataSetChanged()


                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val fm = dataSnapshot.getValue<chat_data>(chat_data::class.java)

                    msglist.remove(fm)
                    mMessageAdapter.notifyDataSetChanged()


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

    override fun onPause() {

        super.onPause()
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener)

        }
        detachDatabaseReadListener()
        msglist.clear()

    }


    override fun onResume() {

        super.onResume()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
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
                finish()

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }



}
