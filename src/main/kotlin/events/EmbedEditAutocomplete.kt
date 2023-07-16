package events

import core.I18n
import models.GuildConfig
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command
import java.sql.DriverManager

class EmbedEditAutocomplete: ListenerAdapter() {
    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if(event.fullCommandName != "embed edit") { return }

        val focusedOption = event.focusedOption
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())
        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val st = db.createStatement()

        val result = st.executeQuery("""SELECT * FROM embedStorage WHERE channelId = '${event.channel!!.id}'""")

        val options: MutableList<Command.Choice> = mutableListOf()
        while(result.next()) {
            try {
                val messageId = result.getString("messageId")
                val message = event.channel!!.asTextChannel().retrieveMessageById(messageId).complete()
                options.add(Command.Choice(message.embeds[0].title?.take(25)!!, messageId))
            } catch (ignored: Exception) { }  // TODO: Сделать событие, удаляющее messageId из БД в случае удаления сообщения
        }

        val filteredOptions = options.filter {
            it.name.lowercase().startsWith(event.focusedOption.value.lowercase())
        }

        return event.replyChoices(filteredOptions).queue()

    }
}