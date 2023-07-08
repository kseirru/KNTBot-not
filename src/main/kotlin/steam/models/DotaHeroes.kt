package steam.models

enum class DotaHeroes(val id: Int, val heroName: String, val emoji: String) {
    ANTIMAGE(1, "Anti-Mage", "<:anti_mage:1121754358812528711>"),
    AXE(2, "Axe", "<:axe:1121754381751156770>"),
    BANE(3, "Bane", "<:bane:1121754392840904714>"),
    BLOODSEEKER(4, "Bloodseeker", "<:bloodseeker:1121754416832323666>"),
    CRYSTAL_MAIDEN(5, "Crystal Maiden", "<:crystal_maiden:1121754469386952714>"),
    DROW_RANGER(6, "Drow Ranger", "<:drow_ranger:1121757660556042240>"),
    EARTHSHAKER(7, "Earthshaker", "<:Earthshaker:1121757664796487700>"),
    JUGGERNAUT(8, "Juggernaut", "<:juggernaut:1121757766520930304>"),
    MIRANA(9, "Mirana", "<:mirana:1121758000441466940>"),
    MORPHLING(10, "Morphling", "<:morphling:1121758004459618354>"),
    NEVERMORE(11, "Shadow Fiend", "<:shadow_fiend:1121758212069265478>"),
    PHANTOM_LANCER(12, "Phantom Lancer", "<:phantom_lancer:1121758114794971247>"),
    PUCK(13, "Puck", "<:puck:1121758120415350784>"),
    PUDGE(14, "Pudge", "<:pudge:1121758122055311450>"),
    RAZOR(15, "Razor", "<:razor:1121758202544009358>"),
    SAND_KING(16, "Sand King", "<:sand_king:1121758207883362404>"),
    STORM_SPIRIT(17, "Storm Spirit", "<:storm_spirit:1121758318386487356>"),
    SVEN(18, "Sven", "<:sven:1121758319967744063>"),
    TINY(19, "Tiny", "<:tiny:1121758362762223677>"),
    VENGEFUL_SPIRIT(20, "Vengeful Spirit", "<:vengeful_spirit:1121758411420340254>"),
    WINDRUNNER(21, "Windranger", "<:windranger:1121758500314435594>"),
    ZEUS(22, "Zeus", "<:zeus:1121758507495075880>"),
    KUNKKA(23, "Kunkka", "<:kunkka:1121757768873938956>"),
    LINA(25, "Lina", "<:lina:1121757875107270716>"),
    LION(26, "Lion", "<:lion:1121757877749686282>"),
    SHADOW_SHAMAN(27, "Shadow Shaman", "<:shadow_shaman:1121758266704281690>"),
    SLARDAR(28, "Slardar", "<:slardar:1121758190435061874>"),
    TIDEHUNTER(29, "Tidehunter", "<:tidehunter:1121758357221544026>"),
    WITCH_DOCTOR(30, "Witch Doctor", "<:witch_doctor:1121758503237857415>"),
    LICH(31, "Lich", "<:lich:1121757870946533436>"),
    RIKI(32, "Riki", "<:riki:1121758203869405234>"),
    ENIGMA(33, "Enigma", "<:enigma:1121757747961155624>"),
    TINKER(34, "Tinker", "<:tinker:1121758361340366909>"),
    SNIPER(35, "Sniper", "<:sniper:1121758195673735229>"),
    NECROLYTE(36, "Necrophos", "<:necrophos:1121758012609146970>"),
    WARLOCK(37, "Warlock", "<:warlock:1121758497843974314>"),
    BEASTMASTER(38, "Beastmaster", "<:beastmaster:1121754408938655746>"),
    QUEENOFPAIN(39, "Queen of Pain", "<:queen_of_pain:1121758199901589574>"),
    VENOMANCER(40, "Venomancer", "<:venomancer:1121758414087917639>"),
    FACELESS_VOID(41, "Faceless Void", "<:faceless_void:1121757748971978865>"),
    SKELETON_KING(42, "Wraith King", "<:wraith_king:1121758506077409310>"),
    DEATH_PROPHET(43, "Death Prophet", "<:death_prophet:1121757651605409804>"),
    PHANTOM_ASSASSIN(44, "Phantom Assassin", "<:phantom_assasin:1121758112811061399>"),
    PUGNA(45, "Pugna", "<:pugna:1121758198496501820>"),
    TEMPLAR_ASSASSIN(46, "Templar Assassin", "<:templar_assasin:1121758354411360257>"),
    VIPER(47, "Viper", "<:viper:1121758415367180329>"),
    LUNA(48, "Luna", "<:luna:1121757955239452795>"),
    DRAGON_KNIGHT(49, "Dragon Knight", "<:dragon_knight:1121757657812979776>"),
    DAZZLE(50, "Dazzle", "<:dazzle:1121757649130749973>"),
    RATTLETRAP(51, "Clockwerk", "<:clockwerk:1121761528144728136>"),
    LESHRAC(52, "Leshrac", "<:leshrac:1121757772757860432>"),
    FURION(53, "Nature's Prophet", "<:natures_prophet:1121758010067402803>"),
    LIFE_STEALER(54, "Lifestealer", "<:lifestealer:1121757873630879764>"),
    DARK_SEER(55, "Dark Seer", "<:dark_seer:1121754484742291466>"),
    CLINKZ(56, "Clinkz", "<:clinkz:1121761525158379550>"),
    OMKNIIGHT(57, "Omniknight", "<:omniknight:1121758124328628304>"),
    ENCHANTRESS(58, "Enchantress", "<:enchantress:1121757746585411595>"),
    HUSKAR(59, "Huskar", "<:huskar:1121757758514008084>"),
    NIGHT_STALKER(60, "Night Stalker", "<:night_stalker:1121758014219759677>"),
    BROODMOTHER(61, "Broodmother", "<:broodmother:1121754454073548832>"),
    BOUNTY_HUNTER(62, "Bounty Hunter", "<:bounty_hunter:1121754425124462674>"),
    WEAVER(63, "Weaver", "<:weaver:1121758418076717067>"),
    JAKIRO(64, "Jakiro", "<:jakiro:1121757763966615593>"),
    BATRIDER(65, "Batrider", "<:batrider:1121754401317584996>"),
    CHEN(66, "Chen", "<:chen:1121761523669401711>"),
    SPECTRE(67, "Spectre", "<:spectre:1121758314506768434>"),
    ANCIENT_APPARITION(68, "Ancient Apparition", "<:ancient_appartion:1121754345378160740>"),
    DOOM_BRINGER(69, "Doom", "<:doom:1121757656051359785>"),
    URSA(70, "Ursa", "<:ursa:1121758409541287937>"),
    SPIRIT_BREAKER(71, "Spirit Breaker", "<:spirit_breaker:1121758315987341342>"),
    GYROCOPTER(72, "Gyrocopter", "<:gyrocopter:1121757754512658442>"),
    ALCHEMIST(73, "Alchemist", "<:alchemist:1121754331218198628>"),
    INVOKER(74, "Invoker", "<:invoker:1121757761135456336>"),
    SILENCER(75, "Silencer", "<:silencer:1121758269447344198>"),
    OBSIDIAN_DESTROYER(76, "Outworld Devourer", "<:outworld_destroyer:1121758108495130645>"),
    LYCAN(77, "Lycan", "<:lycan:1121757957391143013>"),
    BREWMASTER(78, "Brewmaster", "<:brewmaster:1121754433471123486>"),
    SHADOW_DEMON(79, "Shadow Demon", "<:shadow_demon:1121758209359749212>"),
    LONE_DRUID(80,"Lone Druid", "<:lone_druid:1121757952978735114>"),
    CHAOS_KNIGHT(81, "Chaos Knight", "<:chaos_knight:1121761521287049366>"),
    MEEPO(82, "Meepo", "<:meepo:1121757951141625971>"),
    TREANT(83, "Treant Protector", "<:treant_protector:1121758365098459206>"),
    OGRE_MAGI(84, "Ogre Magi", "<:ogre_magi:1121758038152458272>"),
    UNDYING(85, "Undying", "<:undying:1121758407544803408>"),
    RUBICK(86, "Rubick", "<:rubick:1121758205421305926>"),
    DISRUPTOR(87, "Disruptor", "<:disruptor:1121757654314930248>"),
    NYX_ASSASSIN(88, "Nyx Assassin", "<:nyx_assasin:1121758036759945216>"),
    NAGA_SIREN(89, "Naga Siren", "<:naga_siren:1121758008335147089>"),
    KEEPER_OF_THE_LIGHT(90, "Keeper of the Light", "<:keeper_of_the_light:1121757907344703549>"),
    WISP(91, "Io", "<:io:1121757762590871622>"),
    VISAGE(92, "Visage", "<:visage:1121758509852278875>"),
    SLARK(93, "Slark", "<:slark:1121758192226021406>"),
    MEDUSA(94, "Medusa", "<:medusa:1121757948436283422>"),
    TROLL_WARLORD(95, "Troll Warlord", "<:troll_warlord:1121758401806991441>"),
    CENTAUR(96, "Centaur Warrunner", "<:centaur_warrunner:1121754462436991046>"),
    MAGNATAUR(97, "Magnus", "<:magnus:1121757960121634816>"),
    SHREDDER(98, "Timbersaw", "<:timbersaw:1121758359801053244>"),
    BRISTLEBACK(99, "Bristleback", "<:bristleback:1121754446016299078>"),
    TUSK(100, "Tusk", "<:tusk:1121758404487151658>"),
    SKYWRATH_MAGE(101, "Skywrath Mage", "<:skywrath_mage:1121758217840636044>"),
    ABADDON(102, "Abaddon", "<:abaddon:1121754307398729798>"),
    ELDER_TITAN(103, "Elder Titan", "<:elder_titan:1121757666147049482>"),
    LEGION_COMMANDER(104, "Legion Commander", "<:legion_commander:1121757908795936868>"),
    TECHIES(105, "Techies", "<:techies:1121758351898984448>"),
    EMBER_SPIRIT(106, "Ember Spirit", "<:ember_spirit:1121757744093999185>"),
    EARTH_SPIRIT(107, "Earth Spirit", "<:earth_spirit:1121757662124724254>"),
    ABYSSAL_UNDERLORD(108, "Underlord", "<:underlord:1121758405913231490>"),
    TERRORBLADE(109, "Terrorblade", "<:terrorblade:1121758355866800219>"),
    PHOENIX(110, "Phoenix", "<:phoenix:1121758116527226960>"),
    ORACLE(111, "Oracle", "<:oracle:1121758125859545088>"),
    WINTER_WYVERN(112, "Winter Wyvern", "<:winter_wyvern:1121758501836963880>"),
    ARC_WARDEN(113, "Arc Warden", "<:arc_warden:1121754370829205524>"),
    MONKEY_KING(114, "Monkey King", "<:monkey_king:1121758003062911136>"),
    PANGOLIER(120, "Pangolier", "<:pangolier:1121758111238205492>"),
    DARK_WILLOW(119, "Dark Willow", "<:dark_willow:1121754492661141525>"),
    GRIMSTROKE(121, "Grimstroke", "<:grimstroke:1121757751836680252>"),
    MARS(129, "Mars", "<:mars:1121757946771157032>"),
    VOID_SPIRIT(126, "Void Spirit", "<:void_spirit:1121758511236382770>"),
    SNAPFIRE(128, "Snapfire", "<:snapfire:1121758194537070633>"),
    HOODWINK(123, "Hoodwink", "<:hoodwink:1121757757117313074>"),
    DAWNBREAKER(135, "Dawnbreaker", "<:dawnbreaker:1121757647729852506>"),
    MARCI(136, "Marci", "<:marci:1121757943373766657>"),
    PRIMAL_BEAST(137, "Primal Beast", "<:primal_beast:1121758118020390972>"),
    MUERTA(138, "Muerta", "<:muerta:1121758005613047829>");

    companion object {
        fun getHeroById(id: Int) = values().firstOrNull { it.id == id }
    }
}