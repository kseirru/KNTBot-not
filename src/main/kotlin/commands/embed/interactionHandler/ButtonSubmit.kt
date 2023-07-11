package commands.embed.interactionHandler

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
import java.time.Instant

class ButtonSubmit : ListenerAdapter() {
    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if(event.button.id != "embedCreate.submitButton") { return }

        val embed = event.message.embeds[0]
        val channel = event.channel

        channel.sendMessageEmbeds(embed).queue()

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val successEmbed = Embed {
            title = tr.get("main.success")
            color = KNTBot.mainColor
            description = tr.get("embedCreate.success")
            timestamp = Instant.now()
        }

        val message = MessageCreateBuilder(embeds=listOf(successEmbed))

        event.editMessage(MessageEditData.fromCreateData(message.build())).queue()

        // TODO: Сделать сохранение айди сообщения с эмбедом в БД для дальнейшего редактирования

    }
}