package example.com.udacitymovie.data;

/**
 * Created by VPPL-10132 on 1/25/2016.
 */
public interface T {

    /* Data Type And Separator */
    String TYPE_INTEGER = " INTEGER ";
    String TYPE_BOOLEAN = " BOOLEAN ";
    String TYPE_TEXT = " TEXT ";
    String TYPE_REAL = " REAL ";
    String SEP_COMMA = " , ";

    String CLOSE_BRACE = " ) ";
    String OPEN_BRACE = " ( ";
    String SEMICOLON = " ; ";

    String DROP_TABLE = "DROP TABLE IF EXISTS ";
    String AUTO_INCREMENT = " AUTOINCREMENT ";
    String CREATE_TABLE = " CREATE TABLE ";

    /* Primary Constraints of Table*/
    String PRIMARY_KEY = " PRIMARY KEY ";
    String FOREIGN_KEY = " FOREIGN KEY ";
    String REFERENCES = " REFERENCES ";
    String NOT_NULL = " NOT NULL ";

    /* Constraints */
    String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE ";
    String ON_CONFLICT_IGNORE = " ON CONFLICT IGNORE ";
    String UNIQUE = " UNIQUE ";

}
