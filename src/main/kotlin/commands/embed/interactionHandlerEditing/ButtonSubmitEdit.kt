package commands.embed.interactionHandlerEditing

import core.I18n
import core.KNTBot
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditData
import java.sql.DriverManager
import java.time.Instant

class ButtonSubmitEdit : ListenerAdapter() {
    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.button.id != "embedEdit.submitButton") {
            return
        }

        val oldEmbed = event.message.embeds[0]
        val embed = EmbedBuilder.fromData(oldEmbed.toData())
        val channel = event.channel
        val messageId = oldEmbed.footer!!.text!!.split("ID: ")[1]

        val message = channel.retrieveMessageById(messageId).complete()

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val successEmbed = Embed {
            title = tr.get("main.success")
            color = KNTBot.mainColor
            description = tr.get("embedEdit.success")
            timestamp = Instant.now()
        }

        message.editMessageEmbeds(embed.setFooter(null).build()).queue()

        val successMessage = MessageCreateBuilder(embeds=listOf(successEmbed))
        event.editMessage(MessageEditData.fromCreateData(successMessage.build())).queue()

    }
}