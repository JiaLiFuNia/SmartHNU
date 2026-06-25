package com.smart.htu.api.module

import androidx.annotation.ColorInt
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class ExamEntity(
    var id: String? = "",
    val termCode: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    var duration: Float,
    val examName: String,
    val examRoom: String,
    val seatNumber: String,
    val examType: ExamType,
    val isAddToCalendar: Boolean = false
)

enum class ExamType(val type: String, @param:ColorInt val color: Int) {
    FINAL("期末考试", 0xFF4CAF50.toInt()),
    MIDTERM("期中考试", 0xFFFF9800.toInt()),
    CERTIFICATE("证书考试", 0xFF2196F3.toInt()),
    POSTGRADUATE("研究生考试", 0xFFE91E63.toInt())
}


object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}