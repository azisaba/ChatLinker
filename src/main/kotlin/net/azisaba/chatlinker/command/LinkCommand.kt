package net.azisaba.net.azisaba.chatlinker.command

import net.azisaba.net.azisaba.chatlinker.extension.boolean
import net.azisaba.net.azisaba.chatlinker.extension.channel
import net.azisaba.net.azisaba.chatlinker.extension.optionBoolean
import net.azisaba.net.azisaba.chatlinker.extension.optionChannel
import net.azisaba.net.azisaba.chatlinker.extension.subCommand
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

class LinkCommand : Command() {
    override val commandData: CommandData
        get() =
            slashCommand("link", "チャンネル連携") {
                subCommand("add", "チャンネル連携を追加") {
                    channel("from", "送信元", true)
                    channel("to", "送信先", true)
                    boolean("non-twoway", "双方向の送信を無効にするか")
                }
            }

    override fun onCommand(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val member = event.member ?: return
        when (event.subcommandName) {
            "add" -> add(guild, member, event)
        }
    }

    fun add(
        guild: Guild,
        member: Member,
        event: SlashCommandInteractionEvent,
    ) {
        val channelFrom = event.optionChannel("from") ?: return
        val channelTo = event.optionChannel("to") ?: return
        val nonTwoWay = event.optionBoolean("non-twoway") ?: false
    }
}
