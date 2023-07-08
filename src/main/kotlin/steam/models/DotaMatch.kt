package steam.models

@Suppress("UNCHECKED_CAST")
class DotaMatch(response: Map<String, Any>) {
    private val playersRaw = response["players"] as List<Map<String, Any>>
    val players: ArrayList<DotaMatchPlayer> = ArrayList<DotaMatchPlayer>()
    val winnerTeam = if (response["radiant_win"] as Boolean) DotaTeams.Radiant else DotaTeams.Dire
    val duration = response["duration"] as Int
    val startTime = response["start_time"] as Int // timestamp
    val endTime = startTime + duration // timestamp
    val matchId = response["match_id"] as Long
    val firstBloodTime = response["first_blood_time"] as Int

    init {
        for(playerRaw: Map<String, Any> in playersRaw) {
            players.add(DotaMatchPlayer(playerRaw))
        }
    }
}