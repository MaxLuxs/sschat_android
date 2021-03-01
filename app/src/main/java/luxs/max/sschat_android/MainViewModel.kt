package luxs.max.sschat_android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import luxs.max.sschat_android.core.local.Message
import java.util.*

const val ANONYMOUS = "Anonymous"

class MainViewModel : ViewModel() {
    val database = Firebase.database
    val  firebaseAuth = FirebaseAuth.getInstance()

    //chat business:------------------------------------------
    fun sendMessage(message:String){
        database.reference.push().setValue(
                firebaseAuth.currentUser?.email?.let { it1 ->
                    Message(
                            null,
                            it1,
                            message,
                            Date().time
                    )
                }
        )
    }

    fun createMessagesListOptions(lifecyclerOwner: LifecycleOwner) : FirebaseListOptions<Message>{
        val query = database.reference;
        val options = FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message::class.java)
                .setLayout(R.layout.message_item)
                .setLifecycleOwner(lifecyclerOwner)
                .build()
        return options
    }

    fun getUserPhotoUrl(): String? {
        val user = firebaseAuth.currentUser
        return if (user != null && user.photoUrl != null) {
            user.photoUrl.toString()
        } else null
    }

    fun getUserName(): String? {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }
}