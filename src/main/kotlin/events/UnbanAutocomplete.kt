package events

import core.I18n
import models.GuildConfig
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command

class UnbanAutocomplete : ListenerAdapter() {
    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        if(event.name != "unban" && event.focusedOption.name != "user") { return }

        val guild = event.guild!!
        val guildConfig = GuildConfig(guild.id)
        val tr = I18n(guildConfig.getLocale())

        try {
            guild.retrieveBanList().toList()
        } catch (e: Exception) {
            val words = arrayOf(tr.get("unban.autocomplete.error.missing-permissions"))
            val options: MutableList<Command.Choice> = mutableListOf()

            words.forEach {
                options.add(Command.Choice(it, it))
            }

            event.replyChoices(options).queue()
            return
        }

        val banList = guild.retrieveBanList().toList()
        val options: MutableList<Command.Choice> = mutableListOf()

        if(banList.isEmpty()) {
            options.add(Command.Choice(tr.get("unban.error.no-bans"), "0"))
        } else {

            banList.forEach {
                val firstValue = if (it.reason != null) {
                    it.user.name + " | " + it.reason
                } else it.user.name + " | " + tr.get("main.no-reason")
                options.add(Command.Choice(firstValue.take(50), it.user.id))
            }
        }

        val filteredOptions = options.filter {
            it.name.lowercase().startsWith(event.focusedOption.value.lowercase())
        }

        event.replyChoices(filteredOptions.take(25)).queue()

    }
}