package commands.embed.interactionHandler

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

class EmbedAddField : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.addField") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        if(oldEmbed.fields.count() > 20) {
            return event.replyEmbeds(
                Embed {
                    title = tr.get("main.error-occurred")
                    color = Utils.errorColor
                    description = "```\n${tr.get("embedCreate.modal.error.fieldLimit")}\n```"
                    timestamp = Instant.now()
                }
            ).setEphemeral(true).queue()
        }

        val modal = ModalBuilder("embedAddField.modal", tr.get("embedAddField.modal.title"))
        modal.short("embedAddField.fieldName", tr.get("embedAddField.fieldName.label"), true, null, tr.get("embedAddField.fieldName.placeholder"), IntRange(1, 256))
        modal.paragraph("embedAddField.fieldValue", tr.get("embedAddField.fieldValue.label"), true, null, tr.get("embedAddField.fieldValue.placeholder"), IntRange(1, 1024))
        modal.short("embedAddField.inline", tr.get("embedAddField.inline.label"), true, "1", tr.get("embedAddField.inline.placeholder"), IntRange(1, 1))

        event.replyModal(modal.build()).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedAddField.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())

        val fieldName = event.getValue("embedAddField.fieldName")?.asString
        val fieldValue = event.getValue("embedAddField.fieldValue")?.asString
        val fieldInline = event.getValue("embedAddField.inline")?.asString == "1"

        newEmbed.addField(fieldName!!, fieldValue!!, fieldInline)

        for (field in oldEmbed.fields) {
            if(field.name == fieldName) {
                return event.replyEmbeds(
                    Embed {
                        title = tr.get("main.error-occurred")
                        color = Utils.errorColor
                        description = "```\n${tr.get("embedAddField.error.same-name")}\n```"
                        timestamp = Instant.now()
                    }
                ).setEphemeral(true).queue()
            }
        }

        if(newEmbed.length() > 5000) {
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