package commands.infos

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.entities.MessageEmbed.Field
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import java.io.File
import java.sql.DriverManager


class UserInfoCommand : SlashCommand() {
    init {
        name = "info"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("userinfo")

        options = listOf(
            OptionData(OptionType.USER, "user", ".", false, false).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("userinfo", "user"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val user = if (event.getOption("user") == null) { event.user } else { event.getOption("user")!!.asUser }
        val userProfile = user.retrieveProfile().complete()
        val userAsMember = try { event.guild!!.retrieveMember(user).complete() } catch (e: Exception) { null }

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val st = db.createStatement()

        val result = st.executeQuery("SELECT * FROM userDescription WHERE userId = '${user.id}'")
        val userDescription: String?
        if(!result.next()) {
            userDescription = null
        } else {
            userDescription = result.getString("description")
        }

        result.close()
        st.close()
        db.close()


        val embed = Embed {
            title = "${user.name} | ${user.id}"
            color = userProfile?.accentColorRaw

            thumbnail = user.avatar?.url
            image = userProfile.banner?.url

            if (userDescription != null) {
                description = userDescription
            }

            field(tr.get("userinfo.registerDate"), "<t:${user.timeCreated.toEpochSecond()}>\n<t:${user.timeCreated.toEpochSecond()}:R>", true)
            if(userAsMember != null) {
                field(
                    tr.get("userinfo.joinedAt"),
                    "<t:${userAsMember.timeJoined.toEpochSecond()}>\n<t:${userAsMember.timeJoined.toEpochSecond()}:R>",
                    true
                )
            }

        }

        event.replyEmbeds(embed).queue()
    }
}