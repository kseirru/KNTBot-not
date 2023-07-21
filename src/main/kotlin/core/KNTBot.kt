package core

import com.beust.klaxon.Klaxon
import com.jagrosh.jdautilities.command.CommandClientBuilder
import commands.embed.EmbedCommand
import commands.embed.interactionHandlerCreating.*
import commands.embed.interactionHandlerEditing.*
import commands.infos.*
import commands.moderation.*
import commands.steam.steam
import events.EmbedEditAutocomplete
import events.SteamAppAutocomplete
import events.UnbanAutocomplete
import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.LoggerFactory
import java.io.File
import java.sql.DriverManager

class KNTBot {
    companion object {

        const val mainColor: Int = 0x6666CC
        const val version: String = "0.5"
        const val steamKey: String = "17A9F0CFCF188E1288E101C801E7C6A1"
        val httpClient = HTTPClient()


        fun getCommandLocalizedHelp(commandName: String) : Map<DiscordLocale, String> {
            val resourcesFolder = "src/main/resources/localization"
            val resourcesDirectory = File(resourcesFolder)
            if (!resourcesDirectory.exists() || !resourcesDirectory.isDirectory) {
                LoggerFactory.getLogger("I18n KNTBOT").error("Resources folder not found.")
                return emptyMap()
            }

            val jsonFiles = resourcesDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".json")
            }

            val localizedHelps = HashMap<DiscordLocale, String>()

            if (jsonFiles != null) {
                for (file in jsonFiles) {
                    val inputStream = file.inputStream()
                    val loc = Klaxon().parse<Map<String, String>>(inputStream)

                    localizedHelps[DiscordLocale.from(file.name.split(".")[0])] = loc?.get("${commandName}.help")!!
                    inputStream.close()
                }
            }

            return localizedHelps
        }

        fun getOptionLocalizedDescription(commandName: String, optionName: String) : Map<DiscordLocale, String> {
            val resourcesFolder = "src/main/resources/localization"
            val resourcesDirectory = File(resourcesFolder)
            if (!resourcesDirectory.exists() || !resourcesDirectory.isDirectory) {
                LoggerFactory.getLogger("I18n KNTBOT").error("Resources folder not found.")
                return emptyMap()
            }

            val jsonFiles = resourcesDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".json")
            }

            val localizedHelps = HashMap<DiscordLocale, String>()

            if (jsonFiles != null) {
                for (file in jsonFiles) {
                    val inputStream = file.inputStream()
                    val loc = Klaxon().parse<Map<String, String>>(inputStream)

                    localizedHelps[DiscordLocale.from(file.name.split(".")[0])] = loc?.get("${commandName}.options.${optionName}.description")!!
                    inputStream.close()
                }
            }

            return localizedHelps
        }

        fun getChoiceLocalizedName(commandName: String, choiceName: String) : Map<DiscordLocale, String> {
            val resourcesFolder = "src/main/resources/localization"
            val resourcesDirectory = File(resourcesFolder)
            if (!resourcesDirectory.exists() || !resourcesDirectory.isDirectory) {
                LoggerFactory.getLogger("I18n KNTBOT").error("Resources folder not found.")
                return emptyMap()
            }

            val jsonFiles = resourcesDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".json")
            }

            val localizedChoiceName = HashMap<DiscordLocale, String>()

            if (jsonFiles != null) {
                for (file in jsonFiles) {
                    val inputStream = file.inputStream()
                    val loc = Klaxon().parse<Map<String, String>>(inputStream)

                    localizedChoiceName[DiscordLocale.from(file.name.split(".")[0])] = loc?.get("${commandName}.choices.${choiceName}.name")!!
                    inputStream.close()
                }
            }

            return localizedChoiceName
        }

    }

    init {
        // Database init \\
        val createGuildConfig = "CREATE TABLE IF NOT EXISTS guildConfig (guildId TEXT, locale TEXT)"
        val createUserDescription = "CREATE TABLE IF NOT EXISTS userDescription (userId TEXT, description TEXT)"

        val log = LoggerFactory.getLogger("KNTBot INIT")
        log.info("Loading database...")

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val statement = db.createStatement()
        statement.execute(createGuildConfig)
        statement.execute(createUserDescription)
        statement.close()
        db.close()

        log.info("Loading Steam app list...")
        // SteamAPI.loadGames()  TODO: Потом как нибудь обратно включить
        log.info("Steam app list disabled.")

        log.info("Loading bot...")
        val commandClientBuilder = CommandClientBuilder()
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.watching("for servers"))
            .setOwnerId(Dotenv.load()["owner_id"])
            .useHelpBuilder(false)

        // Loading mod commands \\

        commandClientBuilder.addSlashCommand(UnmuteCommand())
        commandClientBuilder.addSlashCommand(UnbanCommand())
        commandClientBuilder.addSlashCommand(KickCommand())
        commandClientBuilder.addSlashCommand(MuteCommand())
        commandClientBuilder.addSlashCommand(BanCommand())

        // Loading other commands \\
        commandClientBuilder.addSlashCommand(InfoCommand())
        commandClientBuilder.addSlashCommand(UserCommand())
        commandClientBuilder.addSlashCommand(steam())

        commandClientBuilder.addSlashCommand(EmbedCommand())

        val commandClient = commandClientBuilder.build()

        JDABuilder.create(Dotenv.load()["token"], GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
            .addEventListeners(commandClient, UnbanAutocomplete(), SteamAppAutocomplete())
            .addEventListeners(EmbedSetTitle(), EmbedSetColor(), EmbedSetDescription())
            .addEventListeners(EmbedAddField(), EmbedEditField(), EmbedEditFieldTwo())
            .addEventListeners(EmbedDeleteField(), EmbedDeleteFieldTwo(), EmbedSetThumbnail())
            .addEventListeners(EmbedSetImage(), ButtonSubmit(), EmbedEditAutocomplete())
            .addEventListeners(EmbedSetTitleEdit(), EmbedSetColorEdit(), EmbedSetDescriptionEdit())
            .addEventListeners(EmbedAddFieldEdit(), EmbedEditFieldEdit(), EmbedEditFieldEditTwo())
            .addEventListeners(EmbedDeleteFieldEdit(), EmbedDeleteFieldTwoEdit(), EmbedSetImageEdit())
            .addEventListeners(EmbedSetThumbnailEdit(), ButtonSubmitEdit(), SetDescriptionModal())
            .setEventPassthrough(true)
            .build()

        Message.suppressContentIntentWarning()

    }
}