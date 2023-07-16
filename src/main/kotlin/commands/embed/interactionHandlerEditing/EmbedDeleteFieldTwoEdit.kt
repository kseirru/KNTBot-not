package commands.embed.interactionHandlerEditing

import core.I18n
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder

class EmbedDeleteFieldTwoEdit : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.selectMenu.id != "embedDeleteField.selectFieldMenu.edit") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]

        if(event.values[0] == "cancel") {
            val selectMenu = StringSelectMenu.create("embedEdit.selectMenu")
                .addOption(tr.get("embedCreate.selectMenu.setTitle"), "embedEdit.selectMenu.setTitle")
                .addOption(tr.get("embedCreate.selectMenu.setColor"), "embedEdit.selectMenu.setColor")
                .addOption(tr.get("embedCreate.selectMenu.setDescription"), "embedEdit.selectMenu.setDescription")
                .addOption(tr.get("embedCreate.selectMenu.addField"), "embedEdit.selectMenu.addField")
                .addOption(tr.get("embedCreate.selectMenu.editField"), "embedEdit.selectMenu.editField")
                .addOption(tr.get("embedCreate.selectMenu.deleteField"), "embedEdit.selectMenu.deleteField")
                .addOption(tr.get("embedCreate.selectMenu.setThumbnail"), "embedEdit.selectMenu.setThumbnail")
                .addOption(tr.get("embedCreate.selectMenu.setImage"), "embedEdit.selectMenu.setImage")
                // .addOption(tr.get("embedCreate.selectMenu.setAuthor"), "embedEdit.selectMenu.setAuthor")
                .setPlaceholder(tr.get("embedCreate.selectMenu.placeholder"))

            // TODO: Сделать возможность указать автора и сделать сноску

            val submitButton = Button.success("embedEdit.submitButton", tr.get("embedEdit.submitButton.label"))

            val messageCreate = MessageCreateBuilder(embeds=listOf(oldEmbed))
            messageCreate.actionRow(selectMenu.build())
            messageCreate.actionRow(submitButton)

            val message = MessageEditBuilder.fromCreateData(messageCreate.build())

            return event.editMessage(message.build()).queue()
        }

        val indexOfField = event.values[0].toInt()
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())
        newEmbed.fields.removeAt(indexOfField)

        val selectMenu = StringSelectMenu.create("embedEdit.selectMenu")
            .addOption(tr.get("embedCreate.selectMenu.setTitle"), "embedEdit.selectMenu.setTitle")
            .addOption(tr.get("embedCreate.selectMenu.setColor"), "embedEdit.selectMenu.setColor")
            .addOption(tr.get("embedCreate.selectMenu.setDescription"), "embedEdit.selectMenu.setDescription")
            .addOption(tr.get("embedCreate.selectMenu.addField"), "embedEdit.selectMenu.addField")
            .addOption(tr.get("embedCreate.selectMenu.editField"), "embedEdit.selectMenu.editField")
            .addOption(tr.get("embedCreate.selectMenu.deleteField"), "embedEdit.selectMenu.deleteField")
            .addOption(tr.get("embedCreate.selectMenu.setThumbnail"), "embedEdit.selectMenu.setThumbnail")
            .addOption(tr.get("embedCreate.selectMenu.setImage"), "embedEdit.selectMenu.setImage")
            // .addOption(tr.get("embedCreate.selectMenu.setAuthor"), "embedEdit.selectMenu.setAuthor")
            .setPlaceholder(tr.get("embedCreate.selectMenu.placeholder"))

        // TODO: Сделать возможность указать автора и сделать сноску

        val submitButton = Button.success("embedEdit.submitButton", tr.get("embedEdit.submitButton.label"))

        val messageCreate = MessageCreateBuilder(embeds=listOf(newEmbed.build()))
        messageCreate.actionRow(selectMenu.build())
        messageCreate.actionRow(submitButton)

        val message = MessageEditBuilder.fromCreateData(messageCreate.build())

        event.editMessage(message.build()).queue()
    }
}