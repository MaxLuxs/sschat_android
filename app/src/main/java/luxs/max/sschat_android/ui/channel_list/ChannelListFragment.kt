package luxs.max.sschat_android.ui.channel_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import luxs.max.sschat_android.R
import luxs.max.sschat_android.ui.channel.ChannelFragment

class ChannelListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
                ChannelListFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val button = view.findViewById<Button>(R.id.buttontochat)
        button.setOnClickListener {
            toChannel(it)
        }

        super.onViewCreated(view, savedInstanceState)



    }


    fun toChannel(view: View) {
        val navController = findNavController(this)
        navController.navigate(R.id.nav_channel)
    }
}