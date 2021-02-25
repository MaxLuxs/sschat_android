package luxs.max.sschat_android.ui.channel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import luxs.max.sschat_android.R

class ChannelFragment : Fragment() {

    //TODO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChannelFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}