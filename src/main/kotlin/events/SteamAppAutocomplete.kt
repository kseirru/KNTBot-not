package events

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command
import steam.SteamAPI

class SteamAppAutocomplete : ListenerAdapter() {
    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if(event.focusedOption.name != "app") {return}
        val steamAPI = SteamAPI()
        val gamesList = steamAPI.getGames()

        if(gamesList.isEmpty()) {
            val opt = arrayOf("No results")
            val options = opt.asSequence()
                .filter{ it.lowercase().startsWith(event.focusedOption.value.lowercase()) }
                .map { Command.Choice(it, it) }
                .toList()
            event.replyChoices(options).queue()
            return
        }

        val optionsNotSorted = gamesList.asSequence()
            .filter { (it["name"] as String).lowercase().startsWith(event.focusedOption.value.lowercase()) }
            .map {
                try {
                    Command.Choice((it["name"] as String).take(70), (it["appid"] as Int).toString())
                } catch (_: Exception) {
                    Command.Choice((it["appid"] as Int).toString(), (it["appid"] as Int).toString())
                }
            }
            .toList()
        val options = optionsNotSorted.sortedWith(compareBy { it.name })
        event.replyChoices(options.take(25)).queue()

    }

}