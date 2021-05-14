package android.ella.assistant.entity

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Assistant(
    var name: String? = null, var description: String? = null,
    @get:Exclude var ImageUrl: Uri? = null,
    @get:Exclude var VideoUrl: Uri? = null,
    @get:Exclude var ImageBitmap: Bitmap? = null
)
