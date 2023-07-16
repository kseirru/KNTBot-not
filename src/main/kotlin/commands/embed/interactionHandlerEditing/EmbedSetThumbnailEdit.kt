package commands.embed.interactionHandlerEditing

import core.I18n
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import models.GuildConfig
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EmbedSetThumbnailEdit : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedEdit.selectMenu.setThumbnail") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedCreate.setThumbnail.modal", tr.get("embedCreate.setThumbnail.modal.title"))
        modal.short("embedCreate.setThumbnail.url", tr.get("embedCreate.setThumbnail.url.label"), false, oldEmbed.thumbnail?.url, tr.get("embedCreate.setThumbnail.url.placeholder"))

        event.replyModal(modal.build()).queue()
    }

}