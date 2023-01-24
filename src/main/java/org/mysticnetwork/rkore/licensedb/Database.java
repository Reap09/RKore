package org.mysticnetwork.rkore.licensedb;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.mysticnetwork.rkore.settings.Settings;
import org.mysticnetwork.rkore.utils.ColorUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;

import static org.bukkit.Bukkit.getServer;


public class Database {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();

    private Connection connection;

    public Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }

        String url = "jdbc:mysql://159.223.98.121:3306/s1_rkore";
        String user = "u1_xNLUgy2pK9";
        String password = "iW=P7ZM@y7U6PH@4m=3PLPQC";

        Connection connection = DriverManager.getConnection(url, user, password);

        this.connection = connection;

        console.sendMessage(ColorUtils.translateColorCodes("[&dRKore licensing&r] &aConnected to license database!"));

        return connection;
    }
    InetAddress licenseHostIps;


    public boolean initializeDatabase() throws SQLException {
        try {
            licenseHostIps = InetAddress.getLocalHost();

        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        String ips = licenseHostIps.getHostAddress();
        console.sendMessage(ColorUtils.translateColorCodes("[&dRKore licensing&r] Current server IP:&d " + ips));

        Statement stmt = getConnection().createStatement();

        String sql1 = "CREATE VIEW IF NOT EXISTS license_view_" + Settings.LICENSE_KEY+ " AS \n" +
                "SELECT `key` FROM s1_rkore.license WHERE \n" +
                "`key` = CAST(" + Settings.LICENSE_KEY + " AS CHAR) \n" +
                "AND `ips` LIKE CONCAT('%', '" + ips + "', '%') LIMIT 1;";
        stmt.execute(sql1);
        String sql2 = "SELECT * FROM license_view_" + Settings.LICENSE_KEY + ";";
        ResultSet rs = stmt.executeQuery(sql2);

        boolean keyFound = false;
        while (rs.next()) {
            String qKey = rs.getString("key");
            if (qKey.equals(String.valueOf(Settings.LICENSE_KEY))) {
                keyFound = true;
            }

        }

        String sql3 = "DROP VIEW IF EXISTS license_view_" + Settings.LICENSE_KEY + ";";
        stmt.executeUpdate(sql3);

        if (keyFound) {
            console.sendMessage(ColorUtils.translateColorCodes("[&dRKore licensing&r] &aSuccessfully Connected to license key and whitelisted IP!"));
        } else {
            console.sendMessage(ColorUtils.translateColorCodes("[&dRKore licensing&r] &cLicense key or IP whitelist invalid!"));
            throw new Error ();
        }

        stmt.close();
        return true;
    }

}