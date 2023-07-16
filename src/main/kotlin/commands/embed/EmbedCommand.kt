package commands.embed

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import net.dv8tion.jda.api.Permission

class EmbedCommand : SlashCommand() {

    // TODO: Сделать команду для редактирования эмбеда
    // TODO: Сделать таблицу в БД MessageEmbeds (messageId: TEXT, channelId: TEXT)

    init {
        name = "embed"
        help = "Embed commands"

        userPermissions = arrayOf(Permission.MANAGE_CHANNEL)
        guildOnly = true

        children = arrayOf(EmbedCreateCommand(), EmbedEditCommand())
    }

    override fun execute(event: SlashCommandEvent?) {}
}