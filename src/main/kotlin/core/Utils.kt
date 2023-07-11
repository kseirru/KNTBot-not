package core

import com.jagrosh.jdautilities.command.SlashCommandEvent
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.entities.MessageEmbed
import java.time.Instant

class Utils {
    companion object {
        const val errorColor: Int = 0xe74c3c

        fun errorEmbed(event: SlashCommandEvent, key: String) : MessageEmbed {
            val guildConfig = GuildConfig(event.guild!!.id)
            val tr = I18n(guildConfig.getLocale())
            return Embed {
                title = tr.get("main.error-occurred")
                color = errorColor
                description = "```\n${tr.get(key)}\n```"
                timestamp = Instant.now()
            }
        }

        fun containsOnlyNumbers(string: String): Boolean {
            return string.all { it.isDigit() }
        }


    }
}