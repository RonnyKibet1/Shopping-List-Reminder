package com.digitalwebandmoney.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import co.intentservice.chatui.ChatView
import co.intentservice.chatui.models.ChatMessage
import com.android.volley.toolbox.JsonObjectRequest
import com.digitalwebandmoney.myapplication.model.ChatMessages
import com.digitalwebandmoney.myapplication.model.User
import com.digitalwebandmoney.myapplication.utils.FriendItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.ibm.watson.language_translator.v3.model.TranslationResult
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.model.IdentifyOptions


class ChatWithFriendActivity : AppCompatActivity() {

    var UID: String = ""
    var NAME: String = ""

    var currentUserName: String = ""

    private lateinit var chatView: ChatView

    private lateinit var messagesDatabase: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAA7T4tvYk:APA91bEdhtY_oqRhAMYmGlgzlc-XDy-yAcHsEftbN-PRN1QWqeb4SuU_n4tuSHhCdWFQvw4KQn04BaOe7vJCgvy-pmdU4JXiH-K7_A1sldQV5NCULfhgrKju-iEuZVBuTWkIFopl5Cfn"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }
    private lateinit var database: DatabaseReference
    private lateinit var  translatedMessage: St