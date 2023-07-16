package commands.embed.interactionHandlerEditing

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

class EmbedSetImageEdit : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedEdit.selectMenu.setImage") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedCreate.setImage.modal", tr.get("embedCreate.setImage.modal.title"))
        modal.short("embedCreate.setImage.url", tr.get("embedCreate.setImage.url.label"), false, oldEmbed.image?.url, tr.get("embedCreate.setImage.url.placeholder"))

        event.replyModal(modal.build()).queue()
    }
}