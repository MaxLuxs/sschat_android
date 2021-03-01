package luxs.max.sschat_android.core.local

import java.util.*

data class Message (
        val id: String? = null,
        val name: String? = "",
        val text: String? = "",
        val time: Long = Date().time,
        val photoUrl:String? = "",
        val imageUrl:String? = null
    )