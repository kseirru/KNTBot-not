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
import org.apache.commons.validator.routines.UrlValidator
import java.time.Instant

class EmbedSetThumbnail : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.setThumbnail") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedCreate.setThumbnail.modal", tr.get("embedCreate.setThumbnail.modal.title"))
        modal.short("embedCreate.setThumbnail.url", tr.get("embedCreate.setThumbnail.url.label"), false, oldEmbed.thumbnail?.url, tr.get("embedCreate.setThumbnail.url.placeholder"))

        event.replyModal(modal.build()).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedCreate.setThumbnail.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())
        val newEmbed = EmbedBuilder.fromData(event.message!!.embeds[0].toData())
        var newThumbnailUrl = event.getValue("embedCreate.setThumbnail.url")?.asString

        if(newThumbnailUrl == "") {
            newThumbnailUrl = null
        } else {
            val urlValidator = UrlValidator()

            if(!urlValidator.isValid(newThumbnailUrl)) {
                return event.replyEmbeds(
                    Embed {
                        title = tr.get("main.error-occurred")
                        color = Utils.errorColor
                        description = "```\n${tr.get("embedCreate.modal.error.wrongUrl")}\n```"
                        timestamp = Instant.now()
                    }
                ).setEphemeral(true).queue()
            }
        }


        newEmbed.setThumbnail(newThumbnailUrl)

        event.editMessageEmbeds(newEmbed.build()).queue()

    }
}