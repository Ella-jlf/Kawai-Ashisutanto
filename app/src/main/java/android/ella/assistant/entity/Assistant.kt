package android.ella.assistant.entity

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Assistant(
    var name: String? = null, var description: String? = null,
    var imageName: String? = null,
    @get:Exclude var videoUrl: Uri? = null,
    @get:Exclude var imageBitmap: Bitmap? = null,
    var latitude : Double? = null,
    var longitude: Double? = null
)
