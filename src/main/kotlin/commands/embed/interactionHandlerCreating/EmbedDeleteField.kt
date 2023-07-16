package commands.embed.interactionHandlerCreating

import core.I18n
import core.Utils
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import java.time.Instant

class EmbedDeleteField : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.deleteField") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]
        val fields = oldEmbed.fields

        if(fields.isEmpty()) {
            return event.replyEmbeds(
                Embed {
                    title = tr.get("main.error-occurred")
                    color = Utils.errorColor
                    description = "```\n${tr.get("embedEditField.error.noFields")}\n```"
                    timestamp = Instant.now()
                }
            ).setEphemeral(true).queue()
        }

        val stringSelectMenu = StringSelectMenu.create("embedDeleteField.selectFieldMenu")
        stringSelectMenu.setPlaceholder(tr.get("embedEditField.selectFieldMenu.placeholder"))

        for (field in fields) {
            try {
                stringSelectMenu.addOption(
                    "${fields.indexOf(field) + 1} | ${field.name?.take(30)}",
                    fields.indexOf(field).toString(),
                    field.value?.take(50)!!
                )
            } catch (ignored: Exception) { }
        }

        stringSelectMenu.addOption(
            tr.get("main.cancel"),
            "cancel",
            tr.get("main.cancel.description")
        )

        val message = MessageCreateBuilder(content = tr.get("embedEditField.message.content"), embeds= listOf(oldEmbed))
        message.actionRow(stringSelectMenu.build())

        val newMessage = MessageEditBuilder.fromCreateData(message.build())

        event.editMessage(newMessage.build()).queue()
    }
}