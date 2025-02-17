package com.generator.pageone.enum

enum class WebOpenerDB {
    DATABASE_NAME { override fun getValue() = "WebOpenerDatabase" },
    TABLE_RANGE { override fun getValue() = "Table_Range" },
    TABLE_WORDPRESS { override fun getValue() = "Table_Wordpress" },
    TABLE_URL { override fun getValue() = "Table_Url" },
    TABLE_FACTOR { override fun getValue() = "Table_Factor" },
    TABLE_SHEET_SETTING { override fun getValue() = "Table_Sheet_Setting" };

    abstract fun getValue() : String
}

object Table{

    enum class Table_Sheet_Setting {
        SHEET_NAME { override fun getValue() = "sheet_name" };
        abstract fun getValue() : String
    }

    enum class Table_Factor {
        FACTOR { override fun getValue() = "factor" };
        abstract fun getValue() : String
    }

    enum class Table_Url {
        URL { override fun getValue() = "url" },
        PAGES { override fun getValue() = "pages" };
        abstract fun getValue() : String
    }

    enum class Table_Range{
        RANGE_TO_LOAD { override fun getValue() = "range_to_load" },
        RANGE_OF_POST { override fun getValue() = "range_of_post" };
        abstract fun getValue() : String
    }

    enum class Table_Wordpress {
        TITLE { override fun getValue() = "title" },
        DATE { override fun getValue() = "date" },
        GROUP { override fun getValue() = "source" },
        LINK { override fun getValue() = "link" };
        abstract fun getValue() : String
    }
}