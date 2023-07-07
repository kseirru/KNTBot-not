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

class KickCommand : SlashCommand() {
    init {
        name = "kick"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("kick")

        userPermissions = arrayOf(Permission.KICK_MEMBERS)
        guildOnly = true

        options = listOf(
            OptionData(OptionType.USER, "user", ".", true, false)
                .setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("kick", "user")),

            OptionData(OptionType.STRING, "reason", ".", false, false)
                .setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("kick", "reason"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val reasonToKick = if(event.getOption("reason") == null) tr.get("main.no-reason") else event.getOption("reason")!!.asString
        val userToKick = event.getOption("user")?.asUser
        val moderator = event.user

        /*
        If moderator tries to ban self
         */
        if(moderator == userToKick) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.self-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        /*
        If moderator tries to ban this bot
        */
        if(userToKick?.id == event.jda.selfUser.id) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.bot-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        /*
        If moderator tries to ban guild owner
         */
        if(userToKick == event.guild!!.owner?.user) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.owner-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        /*
        Check if user is member of guild
         */
        if(event.guild!!.isMember(userToKick!!)) {
            /*
            Check if bot can interact with this member
             */
            if(event.guild!!.getMember(event.jda.selfUser)?.canInteract(event.guild!!.getMember(userToKick)!!) == false) {
                return event.hook.editOriginalEmbeds(
                    Utils.errorEmbed(event, "kick.error.cant-interact")
                ).queue {
                    it.delete().queueAfter(10, TimeUnit.SECONDS)
                }
            }
        } else {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.user-not-found")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        /*
        Check bot permissions
         */
        if(event.guild!!.getMember(event.jda.selfUser)?.hasPermission(Permission.KICK_MEMBERS) == false) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.missing-permissions")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        try {
            event.guild!!.kick(userToKick).reason(reasonToKick).queue()

            event.hook.editOriginalEmbeds(
                Embed {
                    title = tr.get("main.success")
                    description = tr.get("kick.success")
                    color = KNTBot.mainColor

                    field {
                        name = tr.get("main.moderator")
                        value = "${moderator.asMention} | ${moderator.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.user")
                        value = "${userToKick.asMention} | ${userToKick.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.reason")
                        value = "```\n${reasonToKick.replace("```", "'''").replace("`", "'")}\n```"
                        inline = false
                    }

                    timestamp = Instant.now()
                }
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }

        } catch (e: Exception) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "kick.error.unknown")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }


    }
}