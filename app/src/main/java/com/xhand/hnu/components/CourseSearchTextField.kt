package com.xhand.hnu.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.xhand.hnu.model.entity.CourseSearchIndexEntity
import com.xhand.hnu.model.entity.GnqListElement
import com.xhand.hnu.model.entity.XnxqList
import com.xhand.hnu.viewmodel.CourseSearchViewModel
import kotlin.reflect.full.memberProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSearchDropDownTextField(
    content: CourseSearchContentKeys,
    value: String,
    onValueChange: (String) -> Unit,
    courseSearchViewModel: CourseSearchViewModel
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var displayValue by remember { mutableStateOf(value) }

    val hapticFeedback = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val courseOptions = getSearchContentValueByKey(
        courseSearchViewModel.searchCourseIndex,
        content.courseIndex.toString()
    ) as List<*>

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = {
            isDropdownExpanded = it
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .padding(horizontal = 20.dp),
    ) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = { },
            readOnly = true,
            label = { Text(text = content.name) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = PrimaryNotEditable, enabled = true),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isDropdownExpanded)
            }
        )

        ExposedDropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = isDropdownExpanded,
            onDismissRequest = {
                isDropdownExpanded = false
            }
        ) {
            courseOptions.forEach { option ->
                when (option) {
                    is GnqListElement -> {
                        DropdownMenuItem(
                            text = { Text(option.title) },
                            onClick = {
                                displayValue = option.title
                                onValueChange(option.value)
                                isDropdownExpanded = false
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                focusManager.clearFocus()
                            }
                        )
                    }

                    is XnxqList -> {
                        DropdownMenuItem(
                            text = { Text(option.title) },
                            onClick = {
                                displayValue = option.title
                                onValueChange(option.value)
                                isDropdownExpanded = false
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseSearchOutlineTextFiled(
    content: CourseSearchContentKeys,
    value: String,
    onValueChange: (String) -> Unit,
    courseSearchViewModel: CourseSearchViewModel
) {
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
            if (content.icon != null) {
                IconButton(
                    onClick = {
                        courseSearchViewModel.showDatePicker = true
                    }
                ) {
                    Icon(imageVector = content.icon, contentDescription = null)
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