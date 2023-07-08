package core

import com.beust.klaxon.Klaxon
import com.jagrosh.jdautilities.command.CommandClientBuilder
import commands.infos.InfoCommand
import commands.moderation.BanCommand
import commands.moderation.KickCommand
import commands.moderation.MuteCommand
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
        const val version: String = "0.2"


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

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val statement = db.createStatement()
        statement.execute(createGuildConfig)
        statement.close()
        db.close()


        val commandClientBuilder = CommandClientBuilder()
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.watching("for servers"))
            .setOwnerId(Dotenv.load()["owner_id"])
            .useHelpBuilder(false)

        // Loading mod commands \\

        commandClientBuilder.addSlashCommand(KickCommand())
        commandClientBuilder.addSlashCommand(MuteCommand())
        commandClientBuilder.addSlashCommand(BanCommand())

        // Loading other commands \\
        commandClientBuilder.addSlashCommand(InfoCommand())

        val commandClient = commandClientBuilder.build()

        JDABuilder.create(Dotenv.load()["token"], GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
            .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
            .addEventListeners(commandClient)
            .setEventPassthrough(true)
            .build()

        Message.suppressContentIntentWarning()

    }
}