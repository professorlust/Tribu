package graindcafe.tribu;

import org.bukkit.ChatColor;

public class Constants {
	// 20 ticks = 1 second
	public static final int TickDelay = 1;
	public static final int SpawnDelay = 20;
	public static final int TicksBySecond = 20;
	public static final byte LevelFileVersion = 2;
	public static final byte LanguageFileVersion = 1;
	public static final byte ConfigFileVersion=1;
	public static final int VoteDelay = 20 * 30;
	public static final String levelFolder = "plugins/tribu/levels";
	public static final String languagesFolder = "plugins/tribu/languages/";

	public static String MessageMoneyPoints = ChatColor.GREEN + "Money: " + ChatColor.DARK_PURPLE + "%s $" + ChatColor.GREEN + " Points: "
			+ ChatColor.RED + "%s";
	public static String MessageZombieSpawnList = ChatColor.GREEN + "%s";
	
}
