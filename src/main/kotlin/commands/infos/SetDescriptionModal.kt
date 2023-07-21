package commands.infos

import dev.minn.jda.ktx.messages.Embed
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.sql.DriverManager
import java.sql.PreparedStatement

class SetDescriptionModal : ListenerAdapter() {
    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "userinfo.setDescription.modal") { return }

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val st = db.createStatement()

        val result = st.executeQuery("SELECT * FROM userDescription WHERE userId = '${event.user.id}'")
        var preparedStatement: PreparedStatement? = null

        val newDescription = event.getValue("userinfo.newDescription")?.asString
        if(newDescription == "") {
            if(result.next()) {
                preparedStatement = db.prepareStatement("DELETE FROM userDescription WHERE userId = ?")
                preparedStatement.setString(1, event.user.id)
            } else {
            }
        } else {

            if (!result.next()) {
                preparedStatement =
                    db.prepareStatement("INSERT INTO userDescription (userId, description) VALUES (?, ?)")
                preparedStatement.setString(1, event.user.id)
                preparedStatement.setString(2, event.getValue("userinfo.newDescription")?.asString)
            } else {
                preparedStatement = db.prepareStatement("UPDATE userDescription SET description = ? WHERE userId = ?")
                preparedStatement.setString(1, event.getValue("userinfo.newDescription")?.asString)
                preparedStatement.setString(2, event.user.id)
            }
        }

        preparedStatement?.execute()

        event.reply("✅").setEphemeral(true).queue()
        preparedStatement?.close()
        result.close()
        st.close()
        db.close()

    }
}