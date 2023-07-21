package commands.infos

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import models.GuildConfig
import java.sql.DriverManager

class SetDescriptionCommand : SlashCommand() {
    init {
        name = "setdescription"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("userinfo.setdescription")
    }

    override fun execute(event: SlashCommandEvent) {
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val st = db.createStatement()

        val result = st.executeQuery("SELECT * FROM userDescription WHERE userId = '${event.user.id}'")
        val description: String?
        if(result.next()) {
            description = result.getString("description")
        } else {
            description = null
        }

        result.close()
        st.close()
        db.close()

        val modalBuilder = ModalBuilder("userinfo.setDescription.modal", tr.get("userinfo.modal.title"))
        modalBuilder.paragraph("userinfo.newDescription", tr.get("userinfo.newDescription.label"), false, description, tr.get("userinfo.newDescription.placeholder"), IntRange(0, 512))

        event.replyModal(modalBuilder.build()).queue()

    }
}