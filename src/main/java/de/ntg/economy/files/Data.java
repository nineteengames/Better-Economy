package de.ntg.economy.files;

import de.ntg.economy.main.BetterEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class Data {

    private JavaPlugin plugin;
    private File dataFile;
    private FileConfiguration dataConfig;
    private Connection connection;

    public Data(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void connect() {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            try {
                String host = BetterEconomy.getInstance().getPluginConfig().getDatabaseHost();
                String port = BetterEconomy.getInstance().getPluginConfig().getDatabasePort();
                String database = BetterEconomy.getInstance().getPluginConfig().getDatabaseDatabase();
                String username = BetterEconomy.getInstance().getPluginConfig().getDatabaseUsername();
                String password = BetterEconomy.getInstance().getPluginConfig().getDatabasePassword();


                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=UTF-8";


                connection = DriverManager.getConnection(url, username, password);

                createTableIfNotExists();

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("MYSQL can't connect");
            }
        } else {
            dataFile = new File(BetterEconomy.getInstance().getDataFolder(), "player_data.yml");
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);


            saveConfig();
        }

    }

    public void disconnect() {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }


    }

    public void createTableIfNotExists() throws SQLException {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            String query = "CREATE TABLE IF NOT EXISTS BetterEconomy_Data (`id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(36) NOT NULL , `money` INT NOT NULL, PRIMARY KEY (`id`));";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.execute();
            }

        }
    }

    public boolean userExists(UUID uuid) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `BetterEconomy_Data` WHERE `uuid` = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("id") != null;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    }

    public void createUser(Player player) {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            if (userExists(player.getUniqueId())) return;
            String query = "INSERT INTO BetterEconomy_Data(`uuid`, `money`) VALUES (?,?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setInt(2, BetterEconomy.getInstance().getPluginConfig().getStartMoney());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (!isPlayerExisting(player.getUniqueId())) {
                dataConfig.set(player.getUniqueId().toString(), BetterEconomy.getInstance().getPluginConfig().getStartMoney());
                saveConfig();
            }
        }
    }


    public int getBalance(Player player) {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            //mysql: get money
            String query = "SELECT money FROM BetterEconomy_Data WHERE uuid = ?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, player.getUniqueId().toString());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        return resultSet.getInt("money");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return dataConfig.getInt(player.getUniqueId().toString());
        }
        return 0;
    }

    public void setBalance(Player player, int balance) {

        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            //mysql: set money
            String query = "UPDATE BetterEconomy_Data SET money=? WHERE uuid=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, balance);
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            dataConfig.set(player.getUniqueId().toString(), balance);
            saveConfig();
        }


    }

    public int getBalance(UUID uuid) {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            //mysql: get money
            String query = "SELECT money FROM BetterEconomy_Data WHERE uuid = ?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid.toString());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        return resultSet.getInt("money");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return dataConfig.getInt(uuid.toString());
        }
        return 0;
    }

    public void setBalance(UUID uuid, int balance) {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            //mysql: set money
            String query = "UPDATE BetterEconomy_Data SET money=? WHERE uuid=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, balance);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            dataConfig.set(uuid.toString(), balance);
            saveConfig();
        }
    }

    public boolean isPlayerExisting(UUID uuid) {
        if (BetterEconomy.getInstance().getPluginConfig().getDatabaseEnable()) {
            return userExists(uuid);
        } else {
            return dataConfig.contains(uuid.toString());
        }
    }

    private void saveConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}