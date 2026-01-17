package com.smart.htu.screens.main

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
    val content: String,
    val location: String,
    val remarkableInfo: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDateTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDateTime: LocalDateTime,
    val action: String? = null,
    val actionType: ApplicationEntity.RouteType? = null,
    val isAddToCalendar: Boolean = false
)

enum class TaskType {
    Course,
    Exam,
    Event,
    Reminder
}

enum class TaskColor {

}


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