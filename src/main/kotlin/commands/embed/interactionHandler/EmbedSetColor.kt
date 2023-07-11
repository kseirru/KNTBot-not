package commands.embed.interactionHandler

import core.I18n
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.text.HexFormat

class EmbedSetColor : ListenerAdapter() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedCreate.selectMenu.setColor") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedSetColor.modal", tr.get("embedSetColor.modal.title"))
        modal.short("embedSetColor.newColor", tr.get("embedSetColor.newColor.label"), false, oldEmbed.colorRaw.toHexString(HexFormat { number.prefix = "#"; upperCase = true; number.removeLeadingZeros = true}), tr.get("embedSetColor.newColor.placeholder"), IntRange(0, 7))

        event.replyModal(modal.build()).queue()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedSetColor.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())

        val newColor = event.getValue("embedSetColor.newColor")?.asString
        if(newColor == "") {
            newEmbed.setColor(0x6666CC)
        } else {
            newEmbed.setColor(newColor!!.hexToInt(HexFormat { number.prefix = "#"; number.removeLeadingZeros = true}))
        }

        event.editMessageEmbeds(newEmbed.build()).queue()

    }
}