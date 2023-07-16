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
import org.slf4j.LoggerFactory
import java.time.Instant

class EmbedSetColorEdit  : ListenerAdapter() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.values[0] != "embedEdit.selectMenu.setColor") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        val modal = ModalBuilder("embedEdit.embedSetColor.modal", tr.get("embedSetColor.modal.title"))

        modal.short("embedSetColor.newColor", tr.get("embedSetColor.newColor.label"), false, oldEmbed.colorRaw.toHexString(HexFormat { number.prefix = "#"; upperCase = true; number.removeLeadingZeros = true}), tr.get("embedSetColor.newColor.placeholder"), IntRange(0, 10))

        event.replyModal(modal.build()).queue()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(event.modalId != "embedEdit.embedSetColor.modal") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())

        val newColor = event.getValue("embedSetColor.newColor")?.asString
        if(newColor == "") {
            newEmbed.setColor(0x6666CC)
        } else {
            try {
                newEmbed.setColor(newColor!!.hexToInt(HexFormat {
                    number.prefix = "#"; number.removeLeadingZeros = true
                }))
            } catch (e: Exception) {
                return event.replyEmbeds(
                    Embed {
                        title = tr.get("main.error-occurred")
                        color = Utils.errorColor
                        description = "```\n${tr.get("embedEdit.setColor.wrongColor")}\n```"
                        timestamp = Instant.now()
                    }
                ).setEphemeral(true).queue()
            }
        }

        event.editMessageEmbeds(newEmbed.build()).queue()

    }
}