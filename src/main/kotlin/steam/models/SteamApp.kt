package steam.models

import java.lang.StringBuilder

class SteamApp(response: Map<*, *>) {
    private var appId: String = ""
    private var name: String = ""
    private var isFree: Boolean = true
    private var description: String = "'"
    private var developers: List<String> = ArrayList<String>()
    private var publishers: List<String> = ArrayList<String>()
    private var platforms: Map<String, Boolean> = HashMap<String, Boolean>()
    private var genres: String = "unknown"
    private var price: String = "unknown"
    private var imageUrl: String = "https://steamcommunity.com"
    private var url: String = "https://steamcommunity.com"
    private var releaseDate: String = "unknown"

    init {
        appId = response["steam_appid"].toString()
        name = response["name"].toString()
        isFree = response["is_free"] as Boolean
        description = response["short_description"].toString()
        developers = response["developers"] as List<String>
        publishers = response["publishers"] as List<String>
        platforms = response["platforms"] as Map<String, Boolean>
        val genresBuilder = StringBuilder()
        imageUrl = response["header_image"].toString()
        if("?" in imageUrl) {
            imageUrl = imageUrl.split("?")[0]
        }
        url = "https://store.steampowered.com/app/${appId}"

        for (genre in (response["genres"] as List<Map<*, *>>)) {
            genresBuilder.append("${genre["description"]}\n")
        }
        genresBuilder.removeSuffix("\n")
        genres = genresBuilder.toString()

        if(!isFree) {
            val priceOverview = response["price_overview"] as Map<*, *>
            price = if (priceOverview["discount_percent"] as Int != 0) {
                "~~${priceOverview["initial_formatted"]}~~ ${priceOverview["final_formatted"]}"
            } else {
                priceOverview["final_formatted"].toString()
            }
        } else {
            price = "Free"
        }

        val releaseDateMap = response["release_date"] as Map<*, *>
        releaseDate = if(releaseDateMap["coming_soon"] as Boolean) {
            "Coming soon..."
        } else {
            releaseDateMap["date"].toString()
        }

    }

    fun getAppId() : String {
        return appId
    }

    fun getName() : String {
        return name;
    }

    fun isFree() : Boolean {
        return isFree
    }

    fun getDescription() : String {
        return description
    }

    fun getDevelopers() : List<String> {
        return developers
    }

    fun getPublishers() : List<String> {
        return publishers
    }

    fun getPlatforms() : Map<String, Boolean> {
        return platforms
    }

    fun getGenres() : String {
        return genres
    }

    fun getImageUrl() : String {
        return imageUrl
    }

    fun getUrl() : String {
        return url
    }

    fun getReleaseDate() : String {
        return releaseDate
    }

    fun getPrice() : String {
        return price
    }
}