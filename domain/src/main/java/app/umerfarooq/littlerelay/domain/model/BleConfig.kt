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
package app.umerfarooq.littlerelay.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val jsonConf = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

enum class BleRole{
    PERIPHERAL,
    CENTRAL
}

@Serializable
data class BleConfig(
    val bleRole: BleRole = BleRole.PERIPHERAL,
    val bleAdvertiseSetting: BleAdvertiseSetting = BleAdvertiseSetting()
){
    companion object{
        fun fromJson(json: String) = jsonConf.decodeFromString<BleConfig>(json)
    }
    fun toJson() = jsonConf.encodeToString(this)
}
