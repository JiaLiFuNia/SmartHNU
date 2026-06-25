package com.smart.htu.screens.main

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.smart.htu.screens.application.ApplicationEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

@Serializable
data class TaskEntity(
    val id: String,
    val type: TaskType,
    val title: String,
    val location: String,
    val remarkableInfo: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDateTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDateTime: LocalDateTime,
    val action: String? = null,
    val actionType: ApplicationEntity.RouteType? = null,
    val isAddToCalendar: Boolean = false
) {

    companion object {
        fun emptyTask(): TaskEntity {
            return TaskEntity(
                id = "",
                type = TaskType.Event,
                title = "",
                location = "",
                startDateTime = LocalDateTime.now(),
                endDateTime = LocalDateTime.now().plusHours(1),
            )
        }
    }

}

enum class TaskType(val label: String, val lightColor: TaskColor, val darkColor: TaskColor) {
    Course(
        label = "课程",
        lightColor = TaskColor(Color(0xFFE3EDFF), Color(0xFF4788FF)),
        darkColor = TaskColor(Color(0xFF14284A), Color(0xFF4788FF))
    ),
    Exam(
        label = "考试",
        lightColor = TaskColor(Color(0xFFEBE8FE), Color(0xFF8370FF)),
        darkColor = TaskColor(Color(0xFF29234F), Color(0xFF8370FF))
    ),
    Event(
        label = "活动",
        lightColor = TaskColor(Color(0xFFFFF1D9), Color(0xFFFFA30F)),
        darkColor = TaskColor(Color(0xFF452C04), Color(0xFFFFA30F))
    ),
    Reminder(
        label = "事件",
        lightColor = TaskColor(Color(0xFFFFEBEA), Color(0xFFFA4238)),
        darkColor = TaskColor(Color(0xFF4B1411), Color(0xFFFA4238))
    )
}

data class TaskColor(
    val containerColor: Color,
    val primaryColor: Color
)


object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}

fun Color.toHexString(): String {
    return String.format("#%06X", 0xFFFFFF and this.toArgb())
}