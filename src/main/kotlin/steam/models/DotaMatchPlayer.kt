package steam.models

class DotaMatchPlayer(response: Map<String, Any>) {

    companion object {
        val emptyData = mapOf(
            Pair("account_id", "0"),
            Pair("hero_id", 0),
            Pair("kills", 0),
            Pair("deaths", 0),
            Pair("assists", 0),
            Pair("last_hits", 0),
            Pair("denies", 0),
            Pair("gold_per_min", 0),
            Pair("xp_per_min", 0),
            Pair("level", 0),
            Pair("networth", 0),
            Pair("aghanims_scepter", 0),
            Pair("aghanims_shard", 0),
            Pair("moonshard", 0),
            Pair("hero_damage", 0),
            Pair("tower_damage", 0),
            Pair("hero_healing", 0),
            Pair("leaver_status", 0),
            Pair("team_number", 0)
        )
    }

    val accountId = response["account_id"].toString()
    val heroId = response["hero_id"] as Int
    val heroString = DotaHeroes.getHeroById(heroId)?.heroName
    val kills = response["kills"] as Int?
    val deaths = response["deaths"] as Int?
    val assists = response["assists"] as Int?
    val lastHits = response["last_hits"] as Int? // Добиваний крипов
    val denies = response["denies"] as Int? // Добиваний союзных крипов
    val goldPerMinute = response["gold_per_min"] as Int? // GPM
    val xpPerMinute = response["xp_per_min"] as Int? // XPM
    val heroLevel = response["level"] as Int?
    val netWorth = response["networth"] as Int?
    val hasAghanimsScepter = (response["aghanims_scepter"] as Int?) == 1
    val hasAghanimsShard = (response["aghanims_shard"] as Int?) == 1
    val hasMoonShardEffect = (response["moonshard"] as Int?) == 1
    val heroDamage = response["hero_damage"] as Int?
    val towerDamage = response["tower_damage"] as Int?
    var heroHealing = response["hero_healing"] as Int?
    val isLeaver = (response["leaver_status"] as Int?) == 1
    val team: DotaTeams = if ((response["team_number"] as Int?) == 0) DotaTeams.Radiant else DotaTeams.Dire
}