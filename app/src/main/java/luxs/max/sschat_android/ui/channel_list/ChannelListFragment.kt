package luxs.max.sschat_android.ui.channel_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.getstream.chat.android.client.models.User
import luxs.max.sschat_android.databinding.FragmentChannelListBinding
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel

const val API_KEY = "s2dxdhpxd94g"
const val USER_ID = "empty-queen-5"
const val USER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiZW1wdHktcXVlZW4tNSJ9.RJw-XeaPnUBKbbh71rV1bYAKXp6YaPARh68O08oRnOU"


class ChannelListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContext = activity!!.applicationContext


        binding.channelList.setOnChannelClickListener { channel ->
            findNavController().navigate(
                    HomeFragmentDirections.navHomeToChannel(channel.type, channel.id)
            )
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChannelListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}