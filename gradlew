package com.digitalwebandmoney.myapplication.utils

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import co.intentservice.chatui.models.ChatMessage
import com.digitalwebandmoney.myapplication.FriendsListActivity
import com.digitalwebandmoney.myapplication.R
import com.digitalwebandmoney.myapplication.model.ChatMessages
import com.digitalwebandmoney.myapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.friend_row.view.*
import kotlinx.android.synthetic.main.user_row.view.*

class FriendItem(var user: User) : Item() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var messagesDatabase: DatabaseReference

    private  var chatsArrayList: ArrayList<ChatMessages> = ArrayList()


    override fun getLayout() = R.layout.friend_row

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user.photoUrl).into(viewHolder.itemView.userImageViewFriend)
        viewHolder.itemView.nameTextViewFriend.text = "${user.fullName}"

        //query the last conversation between current user and this user on the row
        firebaseAuth = FirebaseAuth.getInstance()
        messagesDatabase = FirebaseDatabase.getInstance().reference.child("ChatMessages")

        messagesDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var count = 0
                for (chatSnapshots in dataSnapshot.children) {
                    var chatMessage = chatSnapshots.getValue(ChatMessages::class.java)
                    try{
                        if((chatMessage!!.from == firebaseAuth.currentUser!!.uid && chatMessage.to == user.uid) || (chatMessage.to == firebaseAuth.currentUser!!.uid && chatMessage.from == user.uid )){
                            //then show this conversation
                            chatsArrayList.add(chatMessage)

                        }

                        if(!chatMessage.viewed && firebaseAuth.currentUser!!.uid != chatMessage!!.from){
                            viewHolder.itemView.lastMessageTextViewWithFriend.setTextColor(Color.GREEN)
                            viewHolder.itemView.lastMessageTextViewWithFriend.typeface = Typeface.DEFAULT_BOLD
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                }

                if(chatsArrayList.size > 0){
                    viewHolder.itemView.lastMessageTextViewWithFriend.text = chatsArrayList[chatsArrayList.size - 1].message
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

        //listen to friend row long item click
        //option to remove friend, remove and block, cancel
        viewHolder.itemView.setOnLongClickListener {
            // build alert dialog


        }






    }
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             