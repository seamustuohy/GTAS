package gov.gtas.util;

/**
 * Language.java Copyright (C) 2007, Richard Midwinter This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
 * Public License along with this program. if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite
 * 330, Boston, MA 02111-1307, USA.
 */

/**
 * Defines language information for the Google Translate API.
 * 
 * @author Richard Midwinter
 */

public enum Language
{
    ARABIC("ar", "العربية"), BULGARIAN("bg"), CATALAN("ca"), CHINESE("zh", "中文"), CHINESE_SIMPLIFIED("zh-CN", "简体中文"),
    CHINESE_TRADITIONAL("zh-TW", "正體中文"), CROATIAN("hr"), CZECH("cs"), DANISH("da"), DUTCH("nl"), ENGLISH("en", "English"),
    FILIPINO("tl"), FINNISH("fi"), FRENCH("fr", "Français"), GERMAN("de", "Deutsch"), GREEK("el", "Ελληνικά"), HEBREW("iw", "עברית"), HINDI("hi"),
    INDONESIAN("id"), ITALIAN("it", "Italiano"), JAPANESE("ja", "日本語"), KOREAN("ko", "한국어"), LATVIAN("lv"), LITHUANIAN("lt"), NORWEGIAN("no"),
    POLISH("pl"), PORTUGESE("pt"), ROMANIAN("ro"), RUSSIAN("ru"), SERBIAN("sr"), SLOVAK("sk"), SLOVENIAN("sl"),
    SPANISH("es"), SWEDISH("sv"), UKRANIAN("uk"), VIETNAMESE("vi", "Việt");

    Language(String language)
    {
        this(language, "");
    }

    Language(String language, String alias)
    {
        this.lang = language;
        this.alias = alias;
    }
    public static Language validate(String language)
    {
        for (Language item : Language.values())
            if (item.lang.equalsIgnoreCase(language))
                return item;
        return null;
    }

    /**
     * Checks a given language is available to use with Google Translate.
     * 
     * @param language The language code to check for.
     * @return true if this language is available to use with Google Translate, false otherwise.
     */
    public static boolean isValidLanguage(String language)
    {
        return (validate(language) != null);
    }

    /**
     * Return the global name of Language
     * @return the global name of Language
     */
    @Override
    public String toString() {
        return lang;
    }
    
    public String getAlias() {
        return alias.equals("") ?
                name().toUpperCase().charAt(0)
                + name().toLowerCase().substring(1)
                : alias;
    }
    private String lang, alias;
}
