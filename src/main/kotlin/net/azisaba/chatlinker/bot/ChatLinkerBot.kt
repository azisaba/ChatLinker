package net.azisaba.net.azisaba.chatlinker.bot

import net.azisaba.net.azisaba.chatlinker.cache.LinkDataCache
import net.azisaba.net.azisaba.chatlinker.command.CommandManager
import net.azisaba.net.azisaba.chatlinker.database.CLDatabase
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.WebhookClient
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ChatLinkerBot : ListenerAdapter() {
    private lateinit var bot: JDA

    fun main() {
        val token = System.getenv("DISCORD_BOT_TOKEN")
        if (token.isEmpty()) {
            throw RuntimeException("Please set a DISCORD_BOT_TOKEN")
        }

        bot =
            JDABuilder
                .createDefault(token)
                .enableIntents(
                    listOf(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                    ),
                ).addEventListeners(this)
                .build()

        CLDatabase.init()
        LinkDataCache.init()

        CommandManager.init(this)
        logger.info("Initialized!")
    }

    override fun onReady(event: ReadyEvent) {
        bot.updateCommands().addCommands(CommandManager.getAllCommandData()).queue()
        logger.info("Logged in as ${bot.selfUser.asTag}")

        logger.info("Warming up now...")
        for (data in LinkDataCache.getAll()) {
            bot.getGuildById(data.fromGuildId)?.getChannelById(TextChannel::class.java, data.fromChannelId)
            bot.getGuildById(data.toGuildId)?.getChannelById(TextChannel::class.java, data.toChannelId)
        }
        logger.info("Warming up completed!")
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        CommandManager.onCommand(event)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot || event.isWebhookMessage) return
        val message = event.message
        val member = event.member ?: return
        val data = LinkDataCache.get(message.channelId) ?: return
        WebhookClient
            .createClient(
                event.jda,
                data.toChannelWebhook,
            ).sendMessage(MessageCreateBuilder.fromMessage(event.message).build())
            .setFiles(message.attachments.map { attachment -> attachment.proxy.downloadAsFileUpload(attachment.fileName) })
            .setUsername(member.nickname ?: member.effectiveName)
            .setAvatarUrl(member.effectiveAvatarUrl)
            .queue()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
