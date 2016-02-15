package org.lostfan.ktv.dao.impl.postgre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.lostfan.ktv.dao.PeriodDAO;
import org.lostfan.ktv.utils.ConnectionManager;

public class PostGrePeriodDAO implements PeriodDAO {

    private Connection getConnection() {
        return ConnectionManager.getManager().getConnection();
    }

    public void savePeriod(LocalDate date) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "DELETE FROM  \"period\";" +
                    "INSERT INTO \"period\" (\"date\") VALUES(?)");
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public LocalDate getPeriod() {
        LocalDate date = LocalDate.of(2003,1,1);
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"period\" limit 1");

            while (resultSet.next()) {
                date = resultSet.getDate("date").toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }
}
