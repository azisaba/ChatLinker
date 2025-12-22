package net.azisaba.net.azisaba.chatlinker

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import dev.kord.common.entity.Snowflake
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val linkMap: BiMap<Snowflake, Snowflake> = HashBiMap.create(),
)
