package commands.embed.interactionHandlerCreating

import core.I18n
import core.Utils
import dev.minn.jda.ktx.interactions.components.ModalBuilder
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import java.time.Instant

class EmbedEditFieldTwo : ListenerAdapter() {
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if(event.selectMenu.id != "embedEditField.selectFieldMenu") { return }

        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message.embeds[0]
        val fields = oldEmbed.fields

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
        val field = fields[indexOfField]

        val modal = ModalBuilder("embedEditFieldTwo.modal.${indexOfField}", tr.get("embedEditFieldTwo.modal.title"))
        modal.short("embedEditFieldTwo.newFieldName", tr.get("embedEditFieldTwo.newFieldName.label"), true, field.name, tr.get("embedEditFieldTwo.newFieldName.placeholder"), IntRange(1, 256))
        modal.paragraph("embedEditFieldTwo.newFieldValue", tr.get("embedEditFieldTwo.newFieldValue.label"), true, field.value, tr.get("embedEditFieldTwo.newFieldValue.placeholder"), IntRange(1, 1024))

        val isInline = if(field.isInline) { "1" } else { "0" }

        modal.short("embedEditFieldTwo.newFieldInline", tr.get("embedEditFieldTwo.newFieldInline.label"), true, isInline, tr.get("embedEditFieldTwo.newFieldInline.placeholder"), IntRange(1, 1))

        event.replyModal(modal.build()).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if(!event.modalId.startsWith("embedEditFieldTwo.modal")) { return }


        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val oldEmbed = event.message!!.embeds[0]
        val newEmbed = EmbedBuilder.fromData(oldEmbed.toData())
        val fields = oldEmbed.fields
        val indexOfField = event.modalId.split(".")[2].toInt()

        newEmbed.clearFields()

        for (field in fields) {
            if(newEmbed.fields.count() == indexOfField) {
                newEmbed.addField(
                    event.getValue("embedEditFieldTwo.newFieldName")!!.asString,
                    event.getValue("embedEditFieldTwo.newFieldValue")!!.asString,
                    event.getValue("embedEditFieldTwo.newFieldInline")!!.asString == "1"
                )
            } else {
                newEmbed.addField(
                    field.name!!, field.value!!, field.isInline
                )
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

        var fieldCount = 0

        for (field in newEmbed.fields) {
            if(field.name == event.getValue("embedEditFieldTwo.newFieldName")!!.asString) {
                fieldCount++
            }
            if (fieldCount > 1) {
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