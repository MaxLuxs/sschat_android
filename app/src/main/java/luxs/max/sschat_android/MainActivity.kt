package luxs.max.sschat_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import luxs.max.sschat_android.ui.channel.ChannelFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun toChannel(view: View) {
        val navController = findNavController(R.id.nav_graph)
        navController.navigate(R.id.nav_channel)

    }
}