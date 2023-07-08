package commands.infos

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder


class InfoCommand : SlashCommand() {
    init {
        name = "info"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("info")

        guildOnly = true
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().setEphemeral(true).queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        var memberCount = 0

        for (guild in event.jda.guilds) {
            memberCount += guild.memberCount
        }

        val mainInfo = """```
                |• JDA: ${JDAInfo.VERSION}
                |• Kotlin: ${KotlinVersion.CURRENT}
                |• Ping: ${event.jda.gatewayPing} ms
                |• RAM: ${Runtime.getRuntime().totalMemory() / 1024 / 1024} Mb
                |```""".trimMargin()

        val botStats = """```
                |• Members: $memberCount
                |• Guilds: ${event.jda.guilds.size}
                |```""".trimMargin()

        val embed = Embed {
            title = "KNTBot | v${KNTBot.version}"
            color = KNTBot.mainColor
            description = tr.get("info.bot.description")


            field {
                name = tr.get("info.main.title")
                value = mainInfo
                inline = true
            }

            field {
                name = tr.get("info.stats.title")
                value = botStats
                inline = true
            }

            thumbnail = event.jda.selfUser.avatar?.url
        }

        val button = Button.link("https://discord.gg/rbPG9pQAGe", tr.get("info.developer.label"))
            .withEmoji(Emoji.fromUnicode("👨🏻‍💻"))

        val newMessage = MessageCreateBuilder()
            .addEmbeds(embed)
            .addActionRow(button)
            .build()

        val editMessage = MessageEditBuilder.fromCreateData(newMessage).build()

        event.hook.editOriginal(editMessage).queue()

    }

}