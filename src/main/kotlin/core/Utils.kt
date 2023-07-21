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

        fun getRGBArr(pixel: Int): IntArray? {
            val alpha = pixel shr 24 and 0xff
            val red = pixel shr 16 and 0xff
            val green = pixel shr 8 and 0xff
            val blue = pixel and 0xff
            return intArrayOf(red, green, blue)
        }

        fun isGray(rgbArr: IntArray): Boolean {
            val rgDiff = rgbArr[0] - rgbArr[1]
            val rbDiff = rgbArr[0] - rgbArr[2]
            // Filter out black, white and grays...... (tolerance within 10 pixels)
            val tolerance = 10
            if (rgDiff > tolerance || rgDiff < -tolerance) if (rbDiff > tolerance || rbDiff < -tolerance) {
                return false
            }
            return true
        }


    }
}