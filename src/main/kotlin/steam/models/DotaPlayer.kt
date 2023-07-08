package steam.models

@Suppress("UNCHECKED_CAST")
class DotaPlayer(private val accountId: String, private val heroId: String, private val bestHeroes: Map<String, Int>, private val mostPlayedHeroes: Map<String, Int>) {
    fun getAccountId() : String { return accountId }
    fun getHeroId() : String { return heroId }
    fun getBestHeroes() : Map<String, Int> { return bestHeroes }
    fun getMostPlayedHeroes() : Map<String, Int> { return mostPlayedHeroes }

}