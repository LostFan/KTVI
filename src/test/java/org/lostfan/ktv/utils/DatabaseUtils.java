package org.lostfan.ktv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseUtils {

    private DatabaseUtils() { }

    public static void executeQuery(String query) throws SQLException {
        Statement stmt = ConnectionManager.getManager().getConnection().createStatement();
        stmt.execute(query);
    }

    public static void executeSqlFile(String filePath) throws IOException, SQLException {
        InputStream is = DatabaseUtils.class.getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        StringBuilder queryBuild = new StringBuilder();

        String line = reader.readLine();
        while (line != null) {
            queryBuild.append(line);
            line = reader.readLine();
        }
        executeQuery(queryBuild.toString());
    }
}
