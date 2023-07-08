package commands.steam

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent

class steam : SlashCommand() {
    init {
        name = "steam"
        help = "Steam commands"

        children = arrayOf(SteamApp())
    }

    override fun execute(event: SlashCommandEvent?) {}
}