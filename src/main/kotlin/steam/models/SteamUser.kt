package steam.models

class SteamUser(response: Map<*, *>) {
    private var steamId: String = "0"
    private var userName: String = "Не указано"
    private var profileUrl: String = "https://steamcommunity.com"
    private var avatarUrl: String = "https://steamcommunity.com"
    private var realName: String = "Не указано"
    private var country: String = "UNKNOWN"
    private var timeCreated: Int = 0

    init {
        this.steamId = response["steamid"]?.toString().toString()
        this.userName = response["personaname"]?.toString().toString()
        this.profileUrl = "https://steamcommunity.com/profile/${this.steamId}"
        this.avatarUrl = response["avatarfull"]?.toString().toString()
        this.realName = response["realname"]?.toString().toString()
        this.country = response["loccountrycode"]?.toString().toString()
        this.timeCreated = response["timecreated"] as Int
    }

    fun getSteamId() : String {
        return steamId
    }

    fun getUserName() : String {
        return userName
    }

    fun getProfileUrl() : String {
        return profileUrl
    }

    fun getAvatarUrl() : String {
        return avatarUrl
    }

    fun getRealName() : String {
        return realName
    }

    fun getTimeCreated() : Int {
        return timeCreated
    }

    fun getLocCode() : String {
        return country
    }

}