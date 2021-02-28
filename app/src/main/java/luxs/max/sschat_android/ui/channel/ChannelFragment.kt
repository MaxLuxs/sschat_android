package luxs.max.sschat_android.ui.channel

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
//import com.firebase.ui.database.FirebaseListOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_channel.*
import luxs.max.sschat_android.MainActivity
import luxs.max.sschat_android.R
import luxs.max.sschat_android.core.local.Message
import java.util.*

class ChannelFragment : Fragment() {

    val SIGN_IN_CODE = 41
    lateinit var messageAdapter: FirebaseListAdapter<Message>
    private val database = Firebase.database
    val query = database.reference

    companion object{
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_button.setOnClickListener {
            if (input_text_edit.text.toString() != ""){
                database.reference.push().setValue(
                    FirebaseAuth.getInstance().currentUser?.email?.let { it1 ->
                        Message(
                            it1,
                            input_text_edit.text.toString(),
                            Date().time
                        )
                    }
                )
                input_text_edit.setText("")
            }
        }

        if (FirebaseAuth.getInstance().currentUser == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE)
        }else{
            Snackbar.make(view, "Welcome to chat!", Snackbar.LENGTH_LONG).show()
            displayAllMessages()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_CODE){
            if(resultCode == RESULT_OK){
                view?.let { Snackbar.make(it, "You are authorized!!", Snackbar.LENGTH_LONG).show() }
            }else{
                view?.let { Snackbar.make(it, "You arn`t authorized!!", Snackbar.LENGTH_LONG).show() }
                activity?.finish()
            }
        }
    }

    private fun displayAllMessages() {
        val query = FirebaseDatabase.getInstance().reference;
        val options = FirebaseListOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .setLayout(R.layout.message_item)
            .setLifecycleOwner(this)
            .build()
        list_of_messages.adapter = object : FirebaseListAdapter<Message>(options) {
            override fun populateView(v: View, model: Message, position: Int) {
                Log.e("!!!", model.toString())
                val messageUsername = v.findViewById<TextView>(R.id.message_username)
                val messageTime =v.findViewById<TextView>(R.id.message_time)
                val messageText = v.findViewById<TextView>(R.id.message_text)
                messageUsername.text = model.name
                messageText.text = model.text
                messageTime.text = DateFormat.format("dd-mm-yyyy HH:mm:ss", model.time)
            }
        }
//        list_of_messages.adapter = object : FirebaseListAdapter<Message>(
//            activity,
//            Message::class.java,
//            R.layout.message_item,
//            FirebaseDatabase.getInstance().reference
//        ){
//            override fun populateView(v: View, model: Message, position: Int) {
//                val authorName = v.findViewById<TextView>(R.id.username_text)
//                val messageTime =v.findViewById<TextView>(R.id.time_text)
//                val messageText = v.findViewById<TextView>(R.id.message_text)
//
//                authorName.text = model.name
//                messageText.text = model.text
//                messageTime.text = DateFormat.format("dd-mm-yyyy HH:mm:ss", model.time)
//            }
//
//        }
    }


}