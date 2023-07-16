package commands.embed.interactionHandlerCreating

import core.I18n
import core.Utils
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import dev.minn.jda.ktx.messages.Embed
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.Instant

class EmbedSetTitle : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.setTitle") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedSetTitle.modal", tr.get("embedSetTitle.modal.title"))
        modal.short("embedSetTitle.newTitle", tr.get("embedSetTitle.newTitle.label"), false, oldEmbed.title, tr.get("embedSetTitle.newTitle.placeholder"), IntRange(0, 90))

        event.replyModal(modal.build()).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedSetTitle.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())

        val newTitle = event.getValue("embedSetTitle.newTitle")
        if (newTitle?.asString == "") {
            if (oldEmbed.description == null) {
                return event.replyEmbeds(
                    Embed {
                        title = tr.get("main.error-occurred")
                        color = Utils.errorColor
                        description = "```\n${tr.get("embedCreate.modal.error.noDescription")}\n```"
                        timestamp = Instant.now()
                    }
                ).setEphemeral(true).queue()
            }
            newEmbed.setTitle(null)
        } else {
            newEmbed.setTitle(newTitle?.asString)
        }

        if(oldEmbed.length > 5000) {
            return event.replyEmbeds(
                Embed {
                    title = tr.get("main.error-occurred")
                    color = Utils.errorColor
                    description = "```\n${tr.get("embedCreate.modal.error.symbolLimit")}\n```"
                    timestamp = Instant.now()
                }
            ).setEphemeral(true).queue()
        }
        
        event.editMessageEmbeds(newEmbed.build()).queue()

    }
}