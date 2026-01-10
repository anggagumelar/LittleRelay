/*
 *     This file is a part of LittleRelay (https://www.github.com/UmerCodez/LittleRelay)
 *     Copyright (C) 2026 Umer Farooq (umerfarooq2383@gmail.com)
 *
 *     LittleRelay is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     LittleRelay is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LittleRelay. If not, see <https://www.gnu.org/licenses/>.
 *
 */
package app.umerfarooq.littlerelay.ui.screens.topic.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults.itemShape
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.Topic
import app.umerfarooq.littlerelay.domain.model.TopicFilter
import app.umerfarooq.littlerelay.domain.model.TopicPublish
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicItem(
    topic: Topic,
    modifier: Modifier = Modifier,
    onDeleteClick: ((Topic) -> Unit)? = null,
    onUpdateClick: ((Topic) -> Unit)? = null
) {
    var showModal by remember{ mutableStateOf(false) }

    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(topic.topic)
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Subscriptions,
                contentDescription = "Edit"
            )
        },
        supportingContent = {
            Text("Qos: ${topic.qos}")
        },
        trailingContent = {
            Row {
                IconButton(
                    onClick = { showModal = true },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(
                    onClick = { onDeleteClick?.invoke(topic) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    )

    if (showModal) {
        ModalBottomSheet(
            modifier = modifier.fillMaxWidth(),
            onDismissRequest = { showModal = false }
        ) {
            SheetContent(
                topic = topic,
                onUpdateClick = {
                    onUpdateClick?.invoke(it)
                    showModal = false
                }
            )
        }
    }

}

@Preview
@Composable
private fun Preview() {
    LittleRelayTheme{
        TopicItem(
            topic = TopicFilter(
                topic = "test",
                qos = 0
            )
        )
    }
}


@Composable
private fun SheetContent(
    topic: Topic,
    onUpdateClick: ((Topic) -> Unit)? = null
) {
    var topic by remember { mutableStateOf(topic) }
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                when (topic) {
                    is TopicFilter -> "Topic Filter"
                    is TopicPublish -> "Topic"
                }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = topic.topic,
                isError = topic.topic.isEmpty(),
                onValueChange = {
                    topic = when (topic) {
                        is TopicFilter -> (topic as TopicFilter).copy(topic = it)
                        is TopicPublish -> (topic as TopicPublish).copy(topic = it)
                    }
                },
                shape = RoundedCornerShape(30.dp),
                supportingText = {
                    if (topic.topic.isEmpty()) {
                        Text("Topic cannot be empty")
                    }
                },
                singleLine = true

            )

            SingleChoiceSegmentedButtonRow {
                val qosOptions = listOf(0, 1, 2)
                qosOptions.forEachIndexed { index, qosValue ->
                    SegmentedButton(
                        selected = topic.qos == qosValue,
                        onClick = {
                            topic = when (topic) {
                                is TopicFilter -> (topic as TopicFilter).copy(qos = qosValue)
                                is TopicPublish -> (topic as TopicPublish).copy(qos = qosValue)
                            }
                        },
                        shape = itemShape(index = index, count = qosOptions.size)
                    ) {
                        Text(qosValue.toString())
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            TextButton(
                onClick = {
                    onUpdateClick?.invoke(topic)
                },
                enabled = !topic.topic.isEmpty()
            ) { Text("Update") }
        }
    }
}

@Preview
@Composable
private fun SheetContentPreview() {
    LittleRelayTheme{
        SheetContent(
            topic = TopicFilter(topic = "test", qos = 0)
        )
    }
}