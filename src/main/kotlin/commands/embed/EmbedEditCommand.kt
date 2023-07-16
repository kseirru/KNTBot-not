package commands.embed

import com.jagrosh.jdautilities.command.SlashCommand
import com.jagrosh.jdautilities.command.SlashCommandEvent
import core.I18n
import core.KNTBot
import core.Utils
import dev.minn.jda.ktx.messages.EmbedBuilder
import dev.minn.jda.ktx.messages.MessageCreateBuilder
import models.GuildConfig
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl
import java.sql.DriverManager
import java.util.concurrent.TimeUnit

class EmbedEditCommand : SlashCommand() {
    init {
        name = "edit"
        descriptionLocalization = KNTBot.getCommandLocalizedHelp("embedEdit")

        options = listOf(
            OptionData(OptionType.STRING, "embed", ".", true , true).setDescriptionLocalizations(KNTBot.getOptionLocalizedDescription("embedEdit", "embed"))
        )
    }

    override fun execute(event: SlashCommandEvent) {
        val guildConfig = GuildConfig(event.guild!!.id)
        val tr = I18n(guildConfig.getLocale())
        val messageId = event.getOption("embed")?.asString

        val db = DriverManager.getConnection("jdbc:sqlite:kntbot.db")
        val st = db.createStatement()

        val result = st.executeQuery("""SELECT * FROM embedStorage WHERE messageId = '${messageId}'""")
        if(!result.next()) {
            return event.replyEmbeds(
                Utils.errorEmbed(event, "embedEdit.error.not-found")
            ).queue {
                it.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
            }
        }

        val channelId = result.getString("channelId")

        result.close()
        st.close()
        db.close()
 
        val channel = event.guild!!.getTextChannelById(channelId)
        channel?.retrieveMessageById(messageId!!)?.queue {
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

            val embed = net.dv8tion.jda.api.EmbedBuilder.fromData(it.embeds[0].toData()).setFooter("ID: ${it.id}")

            val messageCreate = MessageCreateBuilder(embeds=listOf(embed.build()))
            messageCreate.actionRow(selectMenu.build())
            messageCreate.actionRow(submitButton)

            event.reply(messageCreate.build()).setEphemeral(true).queue()
        }

    }
}