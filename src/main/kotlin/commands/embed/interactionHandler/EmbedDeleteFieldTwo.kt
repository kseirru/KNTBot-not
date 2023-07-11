package commands.embed.interactionHandler

import core.I18n
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder

class EmbedDeleteFieldTwo : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.selectMenu.id != "embedDeleteField.selectFieldMenu") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        if(event.values[0] == "cancel") {
            val selectMenu = StringSelectMenu.create("embedCreate.selectMenu")
                .addOption(tr.get("embedCreate.selectMenu.setTitle"), "embedCreate.selectMenu.setTitle")
                .addOption(tr.get("embedCreate.selectMenu.setColor"), "embedCreate.selectMenu.setColor")
                .addOption(tr.get("embedCreate.selectMenu.setDescription"), "embedCreate.selectMenu.setDescription")
                .addOption(tr.get("embedCreate.selectMenu.addField"), "embedCreate.selectMenu.addField")
                .addOption(tr.get("embedCreate.selectMenu.editField"), "embedCreate.selectMenu.editField")
                .addOption(tr.get("embedCreate.selectMenu.deleteField"), "embedCreate.selectMenu.deleteField")
                .addOption(tr.get("embedCreate.selectMenu.setThumbnail"), "embedCreate.selectMenu.setThumbnail")
                .addOption(tr.get("embedCreate.selectMenu.setImage"), "embedCreate.selectMenu.setImage")
                .addOption(tr.get("embedCreate.selectMenu.setAuthor"), "embedCreate.selectMenu.setAuthor")
                .addOption(tr.get("embedCreate.selectMenu.setFooter"), "embedCreate.selectMenu.setFooter")
                .setPlaceholder(tr.get("embedCreate.selectMenu.placeholder"))

            val submitButton = Button.success("embedCreate.submitButton", tr.get("embedCreate.submitButton.label"))

            val messageCreate = MessageCreateBuilder(embeds=listOf(oldEmbed))
            messageCreate.actionRow(selectMenu.build())
            messageCreate.actionRow(submitButton)

            val message = MessageEditBuilder.fromCreateData(messageCreate.build())

            return event.editMessage(message.build()).queue()
        }

        val indexOfField = event.values[0].toInt()
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())
        newEmbed.fields.removeAt(indexOfField)

        val selectMenu = StringSelectMenu.create("embedCreate.selectMenu")
            .addOption(tr.get("embedCreate.selectMenu.setTitle"), "embedCreate.selectMenu.setTitle")
            .addOption(tr.get("embedCreate.selectMenu.setColor"), "embedCreate.selectMenu.setColor")
            .addOption(tr.get("embedCreate.selectMenu.setDescription"), "embedCreate.selectMenu.setDescription")
            .addOption(tr.get("embedCreate.selectMenu.addField"), "embedCreate.selectMenu.addField")
            .addOption(tr.get("embedCreate.selectMenu.editField"), "embedCreate.selectMenu.editField")
            .addOption(tr.get("embedCreate.selectMenu.deleteField"), "embedCreate.selectMenu.deleteField")
            .addOption(tr.get("embedCreate.selectMenu.setThumbnail"), "embedCreate.selectMenu.setThumbnail")
            .addOption(tr.get("embedCreate.selectMenu.setImage"), "embedCreate.selectMenu.setImage")
            .addOption(tr.get("embedCreate.selectMenu.setAuthor"), "embedCreate.selectMenu.setAuthor")
            .addOption(tr.get("embedCreate.selectMenu.setFooter"), "embedCreate.selectMenu.setFooter")
            .setPlaceholder(tr.get("embedCreate.selectMenu.placeholder"))

        val submitButton = Button.success("embedCreate.submitButton", tr.get("embedCreate.submitButton.label"))

        val messageCreate = MessageCreateBuilder(embeds=listOf(newEmbed.build()))
        messageCreate.actionRow(selectMenu.build())
        messageCreate.actionRow(submitButton)

        val message = MessageEditBuilder.fromCreateData(messageCreate.build())

        event.editMessage(message.build()).queue()
    }
}