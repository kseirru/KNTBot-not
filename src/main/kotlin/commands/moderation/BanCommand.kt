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

class BanCommand : SlashCommand() {
    init {
        name = "ban"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("ban")

        userPermissions = arrayOf(Permission.BAN_MEMBERS)
        guildOnly = true

        options = listOf(
            OptionData(OptionType.USER, "user", "User to ban", true, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("ban", "user")),
            OptionData(OptionType.STRING, "reason", "Reason to ban", false, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("ban", "reason"))
        )


    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val reasonToBan = if(event.getOption("reason") == null) tr.get("main.no-reason") else event.getOption("reason")!!.asString
        val userToBan = event.getOption("user")?.asUser
        val moderator = event.user

        // TODO: Сделать автоудаление сообщений через секунд 10-15

        if(moderator == userToBan) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "ban.error.self-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(userToBan?.id == event.jda.selfUser.id) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "ban.error.bot-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(userToBan == event.guild!!.owner?.user) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "ban.error.owner-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        // If user in guild
        if(event.guild!!.isMember(userToBan!!)) {
            if(event.guild!!.getMember(event.jda.selfUser)?.canInteract(event.guild!!.getMember(userToBan)!!) == false) {
                return event.hook.editOriginalEmbeds(
                    Utils.errorEmbed(event, "ban.error.cant-interact")
                ).queue {
                    it.delete().queueAfter(10, TimeUnit.SECONDS)
                }
            }
        }

        if(event.guild!!.getMember(event.jda.selfUser)?.hasPermission(Permission.BAN_MEMBERS) == false) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "ban.error.missing-permissions")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        try {
            event.guild!!.ban(userToBan, 0, TimeUnit.DAYS)
                .reason(reasonToBan)
                .queue()

            event.hook.editOriginalEmbeds(
                Embed {
                    title = tr.get("main.success")
                    description = tr.get("ban.success")
                    color = KNTBot.mainColor

                    field {
                        name = tr.get("main.moderator")
                        value = "${moderator.asMention} | ${moderator.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.user")
                        value = "${userToBan.asMention} | ${userToBan.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.reason")
                        value = "```\n${reasonToBan.replace("```", "'''").replace("`", "'")}\n```"
                        inline = false
                    }

                    timestamp = Instant.now()
                }
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }

        } catch (e: Exception) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "ban.error.unknown")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

    }
}