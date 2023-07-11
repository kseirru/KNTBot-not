package commands.embed

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

class EmbedCreateCommand : SlashCommand() {
    init {
        name = "create"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("embedCreate")
    }

    override fun execute(event: SlashCommandEvent) {
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())

        val defaultEmbed = Embed {
            title = tr.get("embedCreate.defaultEmbed.title")
            color = KNTBot.mainColor
            description = tr.get("embedCreate.defaultEmbed.description")
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
            // .addOption(tr.get("embedCreate.selectMenu.setAuthor"), "embedCreate.selectMenu.setAuthor")
            // .addOption(tr.get("embedCreate.selectMenu.setFooter"), "embedCreate.selectMenu.setFooter")
            .setPlaceholder(tr.get("embedCreate.selectMenu.placeholder"))

        // TODO: Сделать возможность указать автора и сделать сноску

        val submitButton = Button.success("embedCreate.submitButton", tr.get("embedCreate.submitButton.label"))

        val messageCreate = MessageCreateBuilder(embeds=listOf(defaultEmbed))
        messageCreate.actionRow(selectMenu.build())
        messageCreate.actionRow(submitButton)

        event.reply(messageCreate.build()).setEphemeral(true).queue()
    }
}