package com.xhand.hnu.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.GnqListElement
import com.xhand.hnu.model.entity.XnxqList
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import kotlin.reflect.full.memberProperties

@Composable
fun CourseSearchTextField(
    index: Int,
    content: CourseSearchContentKeys,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    courseSearchViewModel: CourseSearchViewModel
) {
    val subArrowRotateDegrees: Float by animateFloatAsState(
        if (courseSearchViewModel.showDropDownMenuList[index] && index != 11) 180f else 0f,
        label = ""
    )
    if (content.show && content.name != "")
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            readOnly = content.readOnly,
            label = { Text(text = content.name) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trailingIcon = {
                if (content.icon != null)
                    IconButton(
                        onClick = {
                            if (onClick != null && content.key != "rq") {
                                onClick()
                            }
                            if (content.key == "rq") {
                                courseSearchViewModel.showDatePicker = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = content.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(subArrowRotateDegrees),
                        )
                    }
                if (content.courseIndex != "") {
                    val courseOptions = getSearchContentValueByKey(
                        courseSearchViewModel.searchCourseIndex,
                        content.courseIndex.toString()
                    ) as List<*>
                    DropdownMenu(
                        expanded = courseSearchViewModel.showDropDownMenuList[index],
                        onDismissRequest = {
                            courseSearchViewModel.showDropDownMenuList[index] = false
                        }
                    ) {
                        courseOptions.forEach { option ->
                            when (option) {
                                is GnqListElement -> {
                                    DropdownMenuItem(
                                        text = { Text(option.title) },
                                        onClick = {
                                            onValueChange(option.value)
                                        }
                                    )
                                }

                                is XnxqList -> {
                                    DropdownMenuItem(
                                        text = { Text(option.title) },
                                        onClick = {
                                            onValueChange(option.value)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        )
}

data class CourseSearchContentKeys(
    val key: String,
    val name: String,
    val icon: ImageVector? = Icons.Default.KeyboardArrowDown,
    val show: Boolean = true,
    val readOnly: Boolean = false,
    val courseIndex: String? = ""
)


fun getSearchContentValueByKey(
    searchCourseIndex: CourseSearchIndexEntity,
    courseSearchContentKey: String
): Any? {
    try {
        val property =
            CourseSearchIndexEntity::class.memberProperties.find { it.name == courseSearchContentKey }
        return property?.get(searchCourseIndex)
    } catch (e: Exception) {
        return null
    }
}