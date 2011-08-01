package graindcafe.tribu.executors;


import graindcafe.tribu.Tribu;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTribu implements CommandExecutor {
	private Tribu plugin;
	// use to confirm deletion of a level
	private String deletedLevel = "";

	public CmdTribu(Tribu instance) {
		plugin = instance;
	}

	// usage: /tribu ((create | load | delete) <name>) | enter | leave |
	// list | start [<name>] | stop | save
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			plugin.LogWarning("PAS D'ARGUMENTS");
			return false;
		}
		args[0] = args[0].toLowerCase();
		if (args[0].equalsIgnoreCase("enter") || args[0].equalsIgnoreCase("join")) {
			if (!(sender instanceof Player)) {
				plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));

			} else
				plugin.addPlayer((Player) sender);
			return true;
		} else if (args[0].equals("leave")) {
			if (!plugin.isDedicatedServer() || sender.isOp())
				if (!(sender instanceof Player)) {
					plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));

				} else {
					plugin.removePlayer((Player) sender);
				}
			return true;
		} else if (args[0].equals("new") || args[0].equals("create")) {
			if (args.length == 1 || !sender.isOp()) {
				return false;
			}

			if (!(sender instanceof Player)) {
				plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));
				return true;
			}
			Player player = (Player) sender;

			if (!plugin.getLevelLoader().saveLevel(plugin.getLevel())) {
				player.sendMessage(plugin.getLocale("Message.UnableToSaveCurrentLevel"));
				return true;
			}

			plugin.setLevel(plugin.getLevelLoader().newLevel(args[1],
					player.getLocation()));
			player.sendMessage(String.format(plugin.getLocale("Message.LevelCreated"),
					args[1]));

			return true;
		} else if (args[0].equals("delete")) {
			if (args.length == 1 || !sender.isOp()) {
				return false;
			}
			if (!deletedLevel.equals(args[1])) {
				deletedLevel = args[1];
				plugin.Message(sender, String.format(
						plugin.getLocale("Message.ConfirmDeletion"), args[1]));
				plugin.Message(sender,
						plugin.getLocale("Message.ThisOperationIsNotCancellable"));
				return true;
			} else {
				if (!plugin.getLevelLoader().deleteLevel(args[1])) {
					plugin.Message(sender, plugin.getLocale("Message.UnableToDeleteLevel"));
				} else {
					plugin.Message(sender, plugin.getLocale("Message.LevelDeleted"));
				}
				return true;
			}
		} else if (args[0].equals("save")) {
			if (!sender.isOp())
				return false;

			if (!plugin.getLevelLoader().saveLevel(plugin.getLevel())) {
				plugin.Message(sender,
						plugin.getLocale("Message.UnableToSaveCurrentLevel"));
			} else {
				plugin.Message(sender, plugin.getLocale("Message.LevelSaveSuccessful"));
			}
			return true;

		} else if (args[0].equals("load")) {
			if (args.length == 1 || !sender.isOp()) {
				return false;
			} else {
				plugin.getLevelSelector().ChangeLevel(args[1],
						sender instanceof Player ? (Player) sender : null);
				return true;
			}
		} else if (args[0].equals("unload")) {
			if (!sender.isOp())
				return false;
			plugin.setLevel(null);
			plugin.Message(sender, plugin.getLocale("Message.LevelUnloaded"));
			return true;

		} else if (args[0].equals("list")) {
			Set<String> levels = plugin.getLevelLoader().getLevelList();
			String msg = "";
			String separator = "";
			for (String level : levels) {
				msg += separator + level;
				separator = ", ";
			}
			plugin.Message(sender, String.format(plugin.getLocale("Message.Levels"), msg));

			return true;
		} else if (args[0].equals("start")) {
			if (!sender.isOp())
				return false;
			// if a level is given, load it before start
			if (args.length > 1
					&& plugin.getLevelLoader().getLevelList().contains(args[1])) {
				plugin.getLevelSelector().ChangeLevel(args[1],
						sender instanceof Player ? (Player) sender : null);
			} else if (plugin.getLevel() == null) {
				plugin.Message(sender, plugin.getLocale("Message.NoLevelLoaded"));
				plugin.Message(sender, plugin.getLocale("Message.NoLevelLoaded2"));
				return true;
			}
			plugin.Message(sender, plugin.getLocale("Message.ZombieModeEnabled"));
			plugin.startRunning();
			return true;
		} else if (args[0].equals("stop")) {
			if (!sender.isOp())
				return false;
			plugin.stopRunning();
			plugin.Message(sender, plugin.getLocale("Message.ZombieModeDisabled"));
			return true;

		} else if (args[0].equals("tpfz")) {
			Location loc = plugin.getSpawner().getFirstZombieLocation();
			if (loc != null)
				if (sender instanceof Player)
					((Player) sender).teleport(loc);
				else if (args.length > 1)
					plugin.getServer().getPlayer(args[1]).teleport(loc);
			return true;

		} else if (args[0].equals("vote")) {
			if (!(sender instanceof Player)) {
				plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));
				return true;
			}
			Player player = (Player) sender;

			if (args.length == 2) {
				plugin.getLevelSelector().castVote(player, args[1]);
				return true;
			}
		} else if (args[0].equals("vote1")) {
			if (!(sender instanceof Player)) {
				plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));
				return true;
			}

			plugin.getLevelSelector().castVote((Player) sender, "1");
			return true;

		} else if (args[0].equals("vote2")) {
			if (!(sender instanceof Player)) {
				plugin.LogWarning(plugin.getLocale("Warning.ThisCommandCannotBeUsedFromTheConsole"));
				return true;
			}

			plugin.getLevelSelector().castVote((Player) sender, "2");
			return true;

		} else {
			plugin.LogWarning("Argument inconnu : " + args[0]);
		}

		return false;
	}

}
