package luxs.max.sschat_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import luxs.max.sschat_android.R
import luxs.max.sschat_android.ui.home.EMPTY_TITLE
import luxs.max.sschat_android.ui.home.TAB_TITLES

class EmptyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(EMPTY_TITLE) }?.apply {
            val textView: TextView = view.findViewById(R.id.emptyTextView)
            textView.text = TAB_TITLES.getOrDefault(getInt(EMPTY_TITLE)-1, "camera")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EmptyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}