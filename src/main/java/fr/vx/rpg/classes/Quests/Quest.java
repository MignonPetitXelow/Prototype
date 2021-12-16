package fr.vx.rpg.classes.Quests;

import fr.vx.rpg.utils.MySql;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Quest
{
    public static void CreateAccount(Player player)
    {
        try {
            PreparedStatement sts = MySql.getConnection().prepareStatement("SELECT `1` FROM `quest` WHERE `uuid`='" + player.getUniqueId().toString() + "'");
            ResultSet rs = sts.executeQuery();
            if(!rs.next()) {
                sts.close();
                sts = MySql.getConnection().prepareStatement("INSERT INTO `quest` (uuid) VALUES ('"+ player.getUniqueId().toString() + "')");
                sts.executeUpdate();
                sts.close();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static boolean getQuest(int id, Player player)
    {
        boolean Quest = false;
        try
        {
            PreparedStatement sts = MySql.getConnection().prepareStatement("SELECT `"+id+"` FROM `quest` WHERE `uuid`= '" + player.getUniqueId().toString() + "' ");
            ResultSet rs = sts.executeQuery();
            if(rs.next()) {
                Quest = rs.getBoolean(id);
            }
        }
        catch (SQLException e)
        { e.printStackTrace(); }
        return Quest;
    }

    public static void setQuest(int id, Player player, int result)
    {
        try
        {
            PreparedStatement sts = MySql.getConnection().prepareStatement("UPDATE `quest` SET `"+id+"`= '"+result+"' WHERE `uuid`='" + player.getUniqueId().toString() +"'");
            sts.executeUpdate();
            sts.close();
        }
        catch (SQLException e)
        { e.printStackTrace(); }
    }
}
