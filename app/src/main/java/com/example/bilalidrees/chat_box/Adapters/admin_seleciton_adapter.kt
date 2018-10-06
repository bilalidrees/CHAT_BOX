package com.example.bilalidrees.chat_box.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bilalidrees.chat_box.R
import com.example.bilalidrees.chat_box.User.User_data
//,private var longClick:()->Unit

class admin_seleciton_adapter (context: Context, resource: Int, var items: ArrayList<User_data>, private var lambda:(Int, User_data)->Unit) : RecyclerView.Adapter<admin_seleciton_adapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            =ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false))



    override fun getItemCount(): Int {
        return if (this.items!= null) {
            this.items.size

        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user=items[position]

        holder.name!!.setText(user.name)


        holder.itemView.setOnClickListener{
            lambda(position,user)
        }




    }






    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to

        var name : TextView? =view.findViewById(R.id.nameTextView)


    }



}