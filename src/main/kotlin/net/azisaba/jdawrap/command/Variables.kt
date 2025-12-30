package net.azisaba.jdawrap.command

import net.dv8tion.jda.api.interactions.InteractionContextType
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

fun slashCommand(
    name: String,
    description: String,
    guildOnly: Boolean = true,
    adminOnly: Boolean = true,
    slashCommandBuilder: SlashCommandData.() -> Unit = {},
): SlashCommandData =
    Commands.slash(name, description).apply {
        if (guildOnly) {
            setContexts(InteractionContextType.GUILD)
        }
        if (adminOnly) {
            defaultPermissions = DefaultMemberPermissions.DISABLED
        }
        slashCommandBuilder()
    }
