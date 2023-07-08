package commands.steam

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import core.Utils
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import steam.Errors.SteamException
import steam.Errors.SteamNotSupportedType
import steam.SteamAPI
import steam.models.SteamApp
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder

class SteamApp : SlashCommand() {
    init {
        name = "app"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("steamApp")

        options = listOf(
            OptionData(OptionType.STRING, "app", "Steam App", true)
                .setAutoComplete(true)
        )

    }

    override fun execute(event: SlashCommandEvent) {
        event.deferReply().setEphemeral(true).queue()
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())
        val appId = event.getOption("app")?.asString
        val steamAPI = SteamAPI()

        val app: SteamApp

        try {
            app = steamAPI.getAppInfo(appId!!)
        } catch (e: SteamException) {

            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "steamApp.error.no-found")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        } catch (e: SteamNotSupportedType) {
            return event.hook.editOriginalEmbeds(
                Utils.errorEmbed(event, "steamApp.error.not-supported-type")
            ).queue {
                it.delete().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        val appPlatforms = app.getPlatforms()

        val developers = StringBuilder()
        val publishers = StringBuilder()
        val platforms = StringBuilder()

        if(app.getDevelopers().isEmpty()) {
            developers.append("Unknown")
        } else {
            for (developer in app.getDevelopers()) {
                developers.append("${developer}\n")
            }
        }
        developers.removeSuffix("\n")

        if(app.getPublishers().isEmpty()) {
            publishers.append("Unknown")
        } else {
            for (publisher in app.getPublishers()) {
                publishers.append("${publisher}\n")
            }
        }

        publishers.removeSuffix("\n")

        try {
            if (appPlatforms["windows"] as Boolean) {
                platforms.append("Windows\n")
            }
            if (appPlatforms["mac"] as Boolean) {
                platforms.append("Mac\n")
            }
            if (appPlatforms["linux"] as Boolean) {
                platforms.append("Linux\n")
            }
        } catch (e: Exception) {
            platforms.append("Unknown")
        }

        platforms.removeSuffix("\n")

        val currentNumberOfPlayers = steamAPI.getCurrentNumberOfPlayers(appId)

        val embed = Embed {
            title = app.getName()
            url = app.getUrl()
            color = KNTBot.mainColor
            image = app.getImageUrl()

            description = app.getDescription()

            field(tr.get("steamApp.success.developers"), developers.toString(), true)
            field(tr.get("steamApp.success.publishers"), publishers.toString(), true)
            field(tr.get("steamApp.success.releaseDate"), app.getReleaseDate(), true)
            field(tr.get("steamApp.success.genres"), app.getGenres(), true)
            field(tr.get("steamApp.success.price"), app.getPrice(), true)
            field(tr.get("steamApp.success.platforms"), platforms.toString(), true)

            footer(name = "$currentNumberOfPlayers ${tr.get("steamApp.success.rightNow")}")
        }

        event.hook.editOriginalEmbeds(embed).queue()

    }
}