package commands.infos

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent

class UserCommand : SlashCommand() {
    init {
        name = "user"
        help = "."

        guildOnly = true

        children = arrayOf(UserInfoCommand(), SetDescriptionCommand())
    }

    override fun execute(event: SlashCommandEvent) {}
}