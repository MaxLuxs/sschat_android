package luxs.max.sschat_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

const val SIGN_IN_CODE = 41

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)
        if (viewModel.firebaseAuth.currentUser == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE)
        }else{
            Snackbar.make(mainlayout, "Welcome SSChat!", Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_CODE){
            if(resultCode == RESULT_OK){
                mainlayout.let { Snackbar.make(it, "You are authorized!!", Snackbar.LENGTH_LONG).show() }
            }else{
                mainlayout.let { Snackbar.make(it, "You arn`t authorized!!", Snackbar.LENGTH_LONG).show() }
                this.finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.miCompose->{
                signOut()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun signOut() {
        viewModel.firebaseAuth.signOut()
//        signInClient.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}