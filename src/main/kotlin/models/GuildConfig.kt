package models

import core.KNTBot
import java.sql.DriverManager

class GuildConfig(private val guildId: String) {
    private var locale: String = "en-US"

    init {

        val database = DriverManager.getConnection("jdbc:sqlite:kntbot.db")

        val getGuildConfigStatement = database.prepareStatement("SELECT * FROM guildConfig WHERE guildId = ?")
        getGuildConfigStatement.setString(1, guildId)

        val resultSet = getGuildConfigStatement.executeQuery()
        if(!resultSet.next()) {
            val statement = database.prepareStatement("INSERT INTO guildConfig VALUES (?, ?)")
            statement.setString(1, guildId)
            statement.setString(2, "en-US")
            statement.execute()
            statement.close()
        } else {
            locale = resultSet.getString("locale")
        }
        resultSet.close()
        database.close()
    }

    fun getGuildId() : String {
        return this.guildId
    }

    fun getLocale() : String {
        return this.locale
    }
}