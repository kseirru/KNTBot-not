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

class EmbedSetDescription : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.setDescription") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedSetDescription.modal", tr.get("embedSetDescription.modal.title"))
        modal.paragraph("embedSetDescription.newDescription", tr.get("embedSetDescription.newDescription.label"), false, oldEmbed.description, tr.get("embedSetDescription.newDescription.placeholder"), IntRange(0, 4000))

        event.replyModal(modal.build()).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedSetDescription.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())
        val newDescription = event.getValue("embedSetDescription.newDescription")?.asString

        if(newDescription == "") {
            if(oldEmbed.title == null) {
                return event.replyEmbeds(
                    Embed {
                        title = tr.get("main.error-occurred")
                        color = Utils.errorColor
                        description = "```\n${tr.get("embedCreate.modal.error.noTitle")}\n```"
                        timestamp = Instant.now()
                    }
                ).setEphemeral(true).queue()
            }

            newEmbed.setDescription(null)
        } else {
            newEmbed.setDescription(newDescription)
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