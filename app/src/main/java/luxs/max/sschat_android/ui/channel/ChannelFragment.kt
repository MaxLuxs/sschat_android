package luxs.max.sschat_android.ui.channel

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.database.FirebaseListAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_channel.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.message_item.*
import luxs.max.sschat_android.MainViewModel
import luxs.max.sschat_android.R
import luxs.max.sschat_android.core.local.Message
import java.util.*


private const val REQUEST_IMAGE = 11
private const val MESSAGES_CHILD = "messages"
private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

class ChannelFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    companion object{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayAllMessages()
        list_of_messages.focusSearch(ListView.FOCUS_DOWN)

        send_button.setOnClickListener {
            if (input_text_edit.text.toString() != ""){
                viewModel.sendMessage(input_text_edit.text.toString())
                input_text_edit.setText("")
            }
        }

        attach_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    private fun displayAllMessages() {
        list_of_messages.adapter = object : FirebaseListAdapter<Message>(viewModel.createMessagesListOptions(this)) {
            override fun populateView(v: View, model: Message, position: Int) {
                Log.e("!!!", model.toString())
                val leftview = v.findViewById<View>(R.id.leftView)
                val rightview = v.findViewById<View>(R.id.rightView)
                val messageLayout = v.findViewById<LinearLayout>(R.id.message_layout)
                if (model.name == viewModel.firebaseAuth.currentUser?.email){
                    leftview.visibility = View.VISIBLE
                    rightview.visibility = View.GONE
                    messageLayout.background = context?.let { ContextCompat.getDrawable(it, R.color.floating_message_create) }
                }else{
                    leftview.visibility = View.GONE
                    rightview.visibility = View.VISIBLE
                    messageLayout.background = context?.let { ContextCompat.getDrawable(it, R.color.tab_inactive) }
                }
                val messageUsername = v.findViewById<TextView>(R.id.message_username)
                val messageTime =v.findViewById<TextView>(R.id.message_time)
                val messageText = v.findViewById<TextView>(R.id.message_text)
                messageUsername.text = model.name
                messageText.text = model.text
                messageTime.text = DateFormat.format("dd-mm-yyyy HH:mm:ss", model.time)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("!!!", "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                val uri: Uri? = data.data
                Log.d("!!!", "Uri: " + uri.toString())
                val user = viewModel.firebaseAuth.currentUser
                val tempMessage = Message(
                        null,
                        viewModel.getUserName(),
                        viewModel.getUserPhotoUrl(),
                        Date().time,
                        LOADING_IMAGE_URL
                )
                viewModel.database.reference.child(MESSAGES_CHILD).push()
                        .setValue(tempMessage, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError != null) {
                                Log.w("!!!", "Unable to write message to database.",
                                        databaseError.toException())
                                return@CompletionListener
                            }

                            // Build a StorageReference and then upload the file
                            val key = databaseReference.key
                            val storageReference = user?.let {
                                uri?.lastPathSegment?.let { it1 ->
                                    FirebaseStorage.getInstance()
                                            .getReference(it.uid)
                                            .child(key!!)
                                            .child(it1)
                                }
                            }
                            putImageInStorage(storageReference!!, uri!!, key!!)
                        })
            }
        }

    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
                .addOnSuccessListener(requireActivity(), OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                    // and add it to the message.
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                val friendlyMessage = Message(
                                        null,
                                        viewModel.getUserName(),
                                        viewModel.getUserPhotoUrl(),
                                        Date().time,
                                        it.toString())
                                viewModel.database.reference
                                        .child(MESSAGES_CHILD)
                                        .child(key)
                                        .setValue(friendlyMessage)
                            }
                })
                .addOnFailureListener(requireActivity(), OnFailureListener { e -> Log.w("!!!", "Image upload task was not successful.", e) })
    }


}