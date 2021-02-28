package luxs.max.sschat_android.core.local

import java.util.*

data class Message (
    val name: String = "",
    val text: String = "",
    val time: Long = Date().time
    )