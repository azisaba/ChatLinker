package net.azisaba.jdawrap.extension

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

fun SubcommandData.string(
    name: String,
    description: String,
    required: Boolean = false,
) {
    addOption(OptionType.STRING, name, description, required)
}

fun SubcommandData.number(
    name: String,
    description: String,
    required: Boolean = false,
    minValue: Double = 0.0,
) {
    addOptions(
        OptionData(OptionType.NUMBER, name, description, required).apply {
            setMinValue(minValue)
        },
    )
}

fun SubcommandData.user(
    name: String,
    description: String,
    required: Boolean = false,
) {
    addOption(OptionType.USER, name, description, required)
}

fun SubcommandData.channel(
    name: String,
    description: String,
    required: Boolean = false,
) {
    addOption(OptionType.CHANNEL, name, description, required)
}

fun SubcommandData.boolean(
    name: String,
    description: String,
    required: Boolean = false,
) {
    addOption(OptionType.BOOLEAN, name, description, required)
}
