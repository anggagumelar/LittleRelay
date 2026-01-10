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
package app.umerfarooq.littlerelay.ui.screens.dashboard.components


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme


@Composable
fun VerticalStep(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    lineColor: Color = MaterialTheme.colorScheme.tertiary,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: @Composable () -> Unit,
){

    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        val size = 24.dp
        val padding = 5.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .padding(padding)
                    .size(size)


            ) {
                icon()
            }
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            ){
                label()
            }

            Row {
                actions?.invoke(this)
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .width(size)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .animateContentSize()
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(lineColor)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-6).dp)
                            .animateContentSize()
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(lineColor)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                content()
            }
        }

    }

}


@Preview
@Composable
private fun VerticalStepPreview() {
    LittleRelayTheme {
        Column {
            VerticalStep(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text("MQTT Service")
                },
                actions = {
                    TextButton(
                        onClick = { }
                    ) { Text("START") }
                },
                content = {
                    Column() {
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                    }
                }
            )
        }

    }
}

@Preview(name = "Multiple steps")
@Composable
private fun VerticalStepPreview2() {
    LittleRelayTheme {
        Column {
            VerticalStep(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text("MQTT Service")
                },
                actions = {
                    TextButton(
                        onClick = { }
                    ) { Text("START") }
                },
                content = {
                 /*   Column() {
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                    }*/
                }
            )
            VerticalStep(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text("MQTT Service")
                },
                actions = {
                    TextButton(
                        onClick = { }
                    ) { Text("START") }
                },
                content = {
                    Column() {
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                        Text("Hello")
                    }
                }
            )
        }
    }
}