package commands.moderation

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import core.Utils
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.time.Instant
import java.util.concurrent.TimeUnit

class UnmuteCommand : SlashCommand() {
    init {
        name = "unmute"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("unmute")

        userPermissions = arrayOf(Permission.MODERATE_MEMBERS)
        guildOnly = true

        options = listOf(
            OptionData(OptionType.USER, "user", ".", true, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("mute", "user"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val userToUnmute = event.getOption("user")!!.asUser
        val moderator = event.user
        val guild = event.guild!!

        if(userToUnmute.id == moderator.id) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unmute.error.self-unmute")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(guild.isMember(userToUnmute)) {
            if(guild.getMember(event.jda.selfUser)?.canInteract(guild.getMember(userToUnmute)!!) == false) {
                return event.hook.editOriginalEmbeds(
                    Utils.errorEmbed(event, "unmute.error.cant-interact")
                ).queue {
                    it.delete().queueAfter(10, TimeUnit.SECONDS)
                }
            }
        } else {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unmute.error.user-not-found")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(guild.getMember(event.jda.selfUser)?.hasPermission(Permission.MODERATE_MEMBERS) == false) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unmute.error.missing-permissions")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        try {
            guild.removeTimeout(userToUnmute).reason(moderator.name).queue()

            val embed = Embed {
                title = tr.get("main.success")
                color = KNTBot.mainColor
                description = tr.get("unmute.success")

                field {
                    name = tr.get("main.moderator")
                    value = "${moderator.asMention} | ${moderator.name}"
                    inline = true
                }
                field {
                    name = tr.get("main.user")
                    value = "${userToUnmute.asMention} | ${userToUnmute.name}"
                    inline = true
                }

                timestamp = Instant.now()
            }

            event.hook.editOriginalEmbeds(embed).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }

        } catch (e: Exception) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "unmute.error.unknown")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

    }
}