package steam

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import core.KNTBot
import core.Utils
import steam.Errors.SteamException
import steam.models.DotaMatch
import steam.models.SteamApp
import steam.models.SteamUser
import java.io.File
import kotlin.math.round


@Suppress("UNCHECKED_CAST")
class SteamAPI {

    companion object {
        fun loadGames() {

            val jsonFile = File("appList.json")
            jsonFile.writeText("")

            val objectMapper = ObjectMapper()
            val objectWriter = objectMapper.writer(DefaultPrettyPrinter())

            val url = "https://api.steampowered.com/IStoreService/GetAppList/v1/?key=${KNTBot.steamKey}&include_games=true&include_dlc=false&include_software=false&include_hardware=false&max_results=50000"

            val response = KNTBot.httpClient.execute(url)?.get("response") as Map<*, *>

            objectWriter.writeValue(File("appList.json"), (response["apps"] as List<*>).distinct())
        }

        fun steamID3ToSteamID64(steamID3: String): String {
            return (steamID3.toInt() + 76561197960265728).toString()
        }

        fun steamID64ToSteamID3(steamID64: String): String {
            return ((steamID64.toLong()) - 76561197960265728).toString()
        }

        private fun Char.isEven(): Boolean = this.toInt() % 2 == 0

    }

    fun getSteamId(username: String) : String {
        if(Utils.containsOnlyNumbers(username)) {
            return username
        }

        val url = "https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/"
        val args = HashMap<String, String>()
        args["key"] = KNTBot.steamKey
        args["vanityurl"] = username

        val response = KNTBot.httpClient.execute(url, args)?.get("response") as Map<*, *>
        val isSuccess = response["success"] as Int == 1

        if(!isSuccess) {
            throw SteamException("Something went Wrong | getSteamId")
        }

        return response["steamid"] as String

    }

    fun getPlayerSummaries(steamId: String) : SteamUser {
        val url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/"
        val args = HashMap<String, String>()
        args["key"] = KNTBot.steamKey
        args["steamids"] = steamId
        args["format"] = "json"

        val response = KNTBot.httpClient.execute(url, args)?.get("response") as Map<*, *>
        val players = response["players"] as List<*>
        if (players.isEmpty()) {
            throw SteamException("Account not Found!")
        }

        val player = players[0] as Map<*, *>

        return SteamUser(player)
    }

    fun getRecentlyPlayedGames(steamId: String) : List<String> {

        val games = ArrayList<String>()

        val url = "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/"
        val args = HashMap<String, String>()
        args["key"] = KNTBot.steamKey
        args["steamid"] = steamId
        args["count"] = "3"

        val response = KNTBot.httpClient.execute(url, args)?.get("response") as Map<*, *>
        val responseGames = response["games"] as List<Map<*, *>>
        for (game: Map<*, *> in responseGames) {
            games.add("${game["name"]} | ${round((game["playtime_forever"] as  Int / 60).toDouble())}h Total | ${round((game["playtime_2weeks"] as Int / 60).toDouble())}h last 2 weeks")
        }

        return games

    }

    fun getAppInfo(appId: String) : SteamApp {
        val url = "https://store.steampowered.com/api/appdetails/"
        val args = HashMap<String, String>()
        args["appids"] = appId
        args["l"] = "english"

        val response = KNTBot.httpClient.execute(url, args)?.get(appId) as HashMap<*, *>
        if(!(response["success"] as Boolean)) {
            throw SteamException("App not Found!")
        }

        /*
        if((response["data"] as Map<String, *>)["type"] != "game") {
            throw SteamNotSupportedType("Type not support!")
        }
         */

        val app = SteamApp(response["data"] as Map<*, *>)

        return app
    }

    fun getGames(): List<Map<String, *>> {
        val jsonFile = File("appList.json")
        val objectMapper = ObjectMapper()

        return (objectMapper.readValue(jsonFile, List::class.java)) as List<Map<String, *>>
    }

    fun getDotaMatchHistory(steamId: String) : List<Map<String, Any>> {
        // Get Match History
        val url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v1"
        val args = HashMap<String, String>()
        args["key"] = KNTBot.steamKey
        args["account_id"] = steamID64ToSteamID3(steamId)
        args["matches_requested"] = "100"

        val response = KNTBot.httpClient.execute(url, args)?.get("result") as Map<String, Any>
        if((response["status"] as Int) != 1) {
            throw SteamException("Steam error")
        }
        return (response["matches"] as List<Map<String, Any>>)
    }

    fun getDotaMatch(matchId: String) : DotaMatch {
        val url = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v1"
        val args = HashMap<String, String>()
        args["key"] = KNTBot.steamKey
        args["match_id"] = matchId

        val response = KNTBot.httpClient.execute(url, args)?.get("result") as Map<String, Any>
        return DotaMatch(response)
    }

     fun getCurrentNumberOfPlayers(appid: String) : Int {
         val url = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1"
         val args = HashMap<String, String>()
         args["key"] = KNTBot.steamKey
         args["appid"] = appid

         val response = KNTBot.httpClient.execute(url, args)?.get("response") as Map<String, Any>
         return if ((response["result"] as Int) != 1) {
             0
         } else {
             response["player_count"] as Int
         }
     }

}