package core

import com.beust.klaxon.Klaxon
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class I18n(lang: String) {
    private var language: String = lang
    private var translator: MutableMap<String, String>
    private var status: Boolean = true

    init {
        var inputStream = ClassLoader.getSystemResourceAsStream(this.language + ".json")
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream("en-US.json")
            LoggerFactory.getLogger("i18n").error("InputStream is null! Language: ${this.language}")
        }

        this.translator = Klaxon().parse<MutableMap<String, String>>(inputStream!!)!!
        inputStream.close()
    }

    /**
     * Возвращает перевод по ключу
     * @param key Ключ перевода
     * @return Перевод в формате String, либо ошибку "Key not found!"
     */
    fun get(key: String) : String {
        if(!this.status) {
            LoggerFactory.getLogger("I18n").warn("Logger disabled!")
            return "Translator disabled"
        }
        var str = this.translator[key]
        if (str == null) {
            LoggerFactory.getLogger("I18n").error("Key not Found!\n====================\nKey:      [${key}]\nLanguage: [${this.language}.json]\n====================")
            str = "Key not found!"
        }
        return str
    }

    /**
     * Возвращает язык переводчика
     * @return Язык в виде String
     */
    fun getLanguage() : String {
        return this.language
    }

    /**
     * Возвращает переводчик в голом виде
     * @return Переводчик в виде Map<String, String>
     */
    fun getTranslator() : Map<String, String> {
        return this.translator
    }

    /**
     * Меняет язык переводчика
     * @param language Язык, на который нужно поменять переводчик.
     */
    fun changeLanguage(language: String) {
        if(!this.status) {
            LoggerFactory.getLogger("I18n").warn("Logger disabled!")
            return
        }
        this.language = language
        var inputStream = ClassLoader.getSystemResourceAsStream(this.language + ".json")
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream("en-US.json")
            LoggerFactory.getLogger("i18n").error("Language file not found!\nLanguage: [${this.language}]")
        }

        this.translator = Klaxon().parse<MutableMap<String, String>>(inputStream!!)!!
        inputStream.close()
    }

    /**
     * Очистить переводчик, сделать объект недоступным
     */
    fun close() {
        this.translator.clear()
        this.status = false
    }

}