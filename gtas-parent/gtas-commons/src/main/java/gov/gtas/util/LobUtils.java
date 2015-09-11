package gov.gtas.util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialClob;

public class LobUtils {
    public static String convertClobToString(Clob clob) throws IOException, SQLException {
        Reader reader = clob.getCharacterStream();
        int c = -1;
        StringBuilder sb = new StringBuilder();
        while((c = reader.read()) != -1) {
             sb.append(((char)c));
        }

        return sb.toString();
    }

    public static Clob createClob(String s) throws SQLException {
        return new SerialClob(s.toCharArray());
    }
}
