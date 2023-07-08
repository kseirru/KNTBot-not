package commands.moderation

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import core.Utils
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class MuteCommand : SlashCommand() {
    init {
        name = "mute"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("mute")

        userPermissions = arrayOf(Permission.MODERATE_MEMBERS)
        guildOnly = true

        val secondsChoice = Command.Choice("seconds", "seconds")
            .setNameLocalizations(KNTBot.getChoiceLocalizedName("mute", "seconds"))

        val minutesChoice = Command.Choice("minutes", "minutes")
            .setNameLocalizations(KNTBot.getChoiceLocalizedName("mute", "minutes"))

        val hoursChoice = Command.Choice("hours", "hours")
            .setNameLocalizations(KNTBot.getChoiceLocalizedName("mute", "hours"))

        val daysChoice = Command.Choice("days", "days")
            .setNameLocalizations(KNTBot.getChoiceLocalizedName("mute", "days"))



        val durationUnit = OptionData(OptionType.STRING, "duration_unit", ".", true, false)
            .setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("mute", "durationUnit"))
            .addChoices(secondsChoice, minutesChoice, hoursChoice, daysChoice)

        options = listOf(
            OptionData(OptionType.USER, "user", ".", true, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("mute", "user")),
            OptionData(OptionType.INTEGER, "duration_time", ".", true, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("mute", "durationTime")),
            durationUnit,
            OptionData(OptionType.STRING, "reason", ".", false, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("mute", "reason"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val reasonToMute = if(event.getOption("reason") == null) tr.get("main.no-reason") else event.getOption("reason")!!.asString
        val userToMute = event.getOption("user")!!.asUser
        val muteDurationTime = event.getOption("duration_time")!!.asInt
        val muteDurationUnit = event.getOption("duration_unit")!!.asString
        val moderator = event.user

        if(moderator == userToMute) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "mute.error.self-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(userToMute.id == event.jda.selfUser.id) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "mute.error.bot-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(userToMute == event.guild!!.owner?.user) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "mute.error.owner-purge")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(event.guild!!.isMember(userToMute)) {
            /*
            Check if bot can interact with this member
             */
            if(event.guild!!.getMember(event.jda.selfUser)?.canInteract(event.guild!!.getMember(userToMute)!!) == false) {
                return event.hook.editOriginalEmbeds(
                    Utils.errorEmbed(event, "mute.error.cant-interact")
                ).queue {
                    it.delete().queueAfter(10, TimeUnit.SECONDS)
                }
            }
        } else {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "mute.error.user-not-found")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        if(event.guild!!.getMember(event.jda.selfUser)?.hasPermission(Permission.KICK_MEMBERS) == false) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "mute.error.missing-permissions")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        var durationTimeInSeconds: Int = 0

        when (muteDurationUnit) {
            "seconds" -> durationTimeInSeconds = muteDurationTime
            "minutes" -> durationTimeInSeconds = muteDurationTime * 60
            "hours" -> durationTimeInSeconds = muteDurationTime * 60 * 60
            "days" -> durationTimeInSeconds = muteDurationTime * 60 * 60 * 24
        }

        try {
            event.guild!!.timeoutFor(userToMute, Duration.ofSeconds(durationTimeInSeconds.toLong())).reason(reasonToMute).queue()
            event.hook.editOriginalEmbeds(
                Embed {
                    title = tr.get("main.success")
                    description = tr.get("mute.success")
                    color = KNTBot.mainColor

                    field {
                        name = tr.get("main.moderator")
                        value = "${moderator.asMention} | ${moderator.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.user")
                        value = "${userToMute.asMention} | ${userToMute.name}"
                        inline = true
                    }

                    field {
                        name = tr.get("main.duration")
                        value = "<t:${Instant.now().epochSecond + durationTimeInSeconds}:f> | <t:${Instant.now().epochSecond + durationTimeInSeconds}:R>"
                        inline = false
                    }

                    field {
                        name = tr.get("main.reason")
                        value = "```\n${reasonToMute.replace("```", "'''").replace("`", "'")}\n```"
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