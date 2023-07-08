package commands.moderation

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import core.Utils
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.util.concurrent.TimeUnit

class UnbanCommand : SlashCommand() {
    init {
        name = "unban"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("unban")

        userPermissions = arrayOf(Permission.BAN_MEMBERS)
        guildOnly = true

        options = listOf(
            OptionData(OptionType.STRING, "user", ".", true, true).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("unban", "user"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())
        val guild = event.guild!!
        val moderator = event.user
        val userIdToUnban = event.getOption("user")!!.asString

        try {
            guild.retrieveBanList().toList()
        } catch (e: Exception) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unban.autocomplete.error.missing-permissions")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        val banList = guild.retrieveBanList().toList()

        if (banList.isEmpty()) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unban.error.no-bans")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        val ban = banList.filter { it.user.id == userIdToUnban }
        if (ban.isEmpty()) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unban.error.no-banned")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        val banMomentXDD = ban[0]

        val reasonToBan = if (banMomentXDD.reason != null) {
            banMomentXDD!!.reason?.replace("```", "'''")?.replace("`", "'")
        } else {
            tr.get("main.no-reason")
        }

        try {
            guild.unban(UserSnowflake.fromId(userIdToUnban)).reason(moderator.name).queue()

            val embed = Embed {
                title = tr.get("main.success")
                color = KNTBot.mainColor
                description = tr.get("unban.success")

                field {
                    name = tr.get("main.moderator")
                    value = "${moderator.asMention} | ${moderator.name}"
                    inline = true
                }

                field {
                    name = tr.get("main.user")
                    value = "<@${userIdToUnban}>"
                    inline = true
                }

                field {
                    name = tr.get("unban.success.reason-was")
                    value = "```\n${reasonToBan}\n```"
                    inline = false
                }
            }

            return event.hook.editOriginalEmbeds(embed).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        } catch (e: Exception) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unban.error.unknown")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }


    }
}