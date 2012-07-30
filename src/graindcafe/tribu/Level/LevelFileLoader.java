/*******************************************************************************
 * Copyright or � or Copr. Quentin Godron (2011)
 * 
 * cafe.en.grain@gmail.com
 * 
 * This software is a computer program whose purpose is to create zombie 
 * survival games on Bukkit's server. 
 * 
 * This software is governed by the CeCILL-C license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-C
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 ******************************************************************************/
package graindcafe.tribu.Level;

import graindcafe.tribu.Package;
import graindcafe.tribu.Tribu;
import graindcafe.tribu.Configuration.Constants;
import graindcafe.tribu.Signs.TribuSign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LevelFileLoader {

	private Set<String> levels;
	private Tribu plugin;

	public LevelFileLoader(Tribu instance) {
		plugin = instance;
		levels = new HashSet<String>();
		levels.clear();
		File dir = new File(Constants.levelFolder);
		/*
		 * if (!dir.exists()) {
		 * plugin.LogInfo(plugin.getLocale("Info.LevelFolderDoesntExist"));
		 * String[] levelFolders = Constants.levelFolder.split("/"); String
		 * tmplevelFolder = ""; for (byte i = 0; i < levelFolders.length; i++) {
		 * tmplevelFolder = tmplevelFolder.concat(levelFolders[i] +
		 * File.separatorChar); dir = new File(tmplevelFolder); if(!dir.mkdir())
		 * plugin.LogSevere(plugin.getLocale("Severe.TribuCantMkdir")); } }
		 */
		File[] files = dir.listFiles();
		plugin.LogInfo(String.format(plugin.getLocale("Info.LevelFound"), String.valueOf(files == null ? 0 : files.length)));
		if (files != null) {
			for (File file : files) {
				levels.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
			}
		}

	}

	public String getWorldName(String levelName) {
		for (String level : levels) {
			if (level.equalsIgnoreCase(levelName))
				levelName = level;
		}
		File file = new File(Constants.levelFolder + levelName + ".lvl");
		if (!file.exists()) {
			return null;
		}
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			in.skipBytes(1);
			if (in.available() < 2) {
				fstream.close();
				in.close();
				plugin.LogSevere("Something wrong happened ... Level is empty");
				return null;
			}
			String worldName=in.readUTF();
			fstream.close();
			in.close();
			return worldName;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean deleteLevel(String name) {
		File file = new File(Constants.levelFolder + name + ".lvl");
		if (file.exists()) {
			boolean result = file.delete();
			if (!result) {
				plugin.LogWarning(plugin.getLocale("Warning.IOErrorOnFileDelete"));
			} else {
				levels.remove(name);
			}
			return result;
		}
		return false;
	}

	public boolean exists(String name) {
		File file = new File(Constants.levelFolder + name + ".lvl");
		return (file.exists());
	}

	public Set<String> getLevelList() {
		return levels;
	}

	public TribuLevel loadLevel(String name) {
		TribuLevel level = null;
		try {

			File file = new File(Constants.levelFolder + name + ".lvl");
			if (!file.exists()) {
				return null;
			}
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			int version = in.readByte();

			if (version == 1) {
				fstream.close();
				in.close();
				if (file.renameTo(new File(Constants.levelFolder + name + "." + version))) {
					file = new File(Constants.levelFolder + name + "." + version);
					fstream = new FileInputStream(file);
					in = new DataInputStream(fstream);

					File tempFile = new File(Constants.levelFolder + name + ".lvl");
					version = 3;
					DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
					// set the file version
					out.writeByte(version);
					int i = in.available() - 1;
					in.skipBytes(1);
					while (i > 0) {
						// Copy data
						out.write(in.read());
						i--;
					}
					// set sign count = 0
					out.writeInt(0);
					out.close(); // close data
					in.close();
					fstream.close();
					file.delete();
					file = tempFile;

				}
				fstream = new FileInputStream(file);
				in = new DataInputStream(fstream);
				version = in.readByte();
			}
			if (version == 2) {
				fstream.close();
				in.close();
				if (file.renameTo(new File(Constants.levelFolder + name + "." + version))) {
					file = new File(Constants.levelFolder + name + "." + version);
					fstream = new FileInputStream(file);
					in = new DataInputStream(fstream);

					File tempFile = new File(Constants.levelFolder + name + ".lvl");
					version = 3;
					DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
					// set the file version
					out.writeByte(version);
					int i = in.available() - 1;
					in.skipBytes(1);
					while (i > 0) {
						// Copy data
						out.write(in.read());
						i--;
					}
					// set sign count = 0
					out.writeInt(0);
					out.close(); // close data
					in.close();
					fstream.close();
					file.delete();
					file = tempFile;
				}
				fstream = new FileInputStream(file);
				in = new DataInputStream(fstream);
				version = in.readByte();
			}
			if (version != Constants.LevelFileVersion) {
				fstream.close();
				in.close();
				plugin.LogSevere(plugin.getLocale("Severe.WorldInvalidFileVersion"));
				return null;
			}
			if (in.available() < 2) {
				fstream.close();
				in.close();
				plugin.LogSevere("Something wrong happened ... Level is empty");
				return null;
			}
			World world = plugin.getServer().getWorld(in.readUTF());
			if (world == null) {
				fstream.close();
				in.close();
				plugin.LogSevere(plugin.getLocale("Severe.WorldDoesntExist"));
				return null;
			}
			double sx, sy, sz; // spawn coords
			double dx, dy, dz; // Death coords
			float sYaw, dYaw;
			sx = in.readDouble();
			sy = in.readDouble();
			sz = in.readDouble();
			sYaw = in.readFloat();

			dx = in.readDouble();
			dy = in.readDouble();
			dz = in.readDouble();
			dYaw = in.readFloat();

			Location spawn = new Location(world, sx, sy, sz, sYaw, 0.0f);
			Location death = new Location(world, dx, dy, dz, dYaw, 0.0f);

			level = new TribuLevel(name, spawn);
			level.setDeathSpawn(death);

			int spawncount = in.readInt();

			Location pos;
			String spawnName;

			for (int i = 0; i < spawncount; i++) {
				sx = in.readDouble();
				sy = in.readDouble();
				sz = in.readDouble();
				sYaw = in.readFloat();
				spawnName = in.readUTF();
				pos = new Location(world, sx, sy, sz, sYaw, 0.0f);
				level.addZombieSpawn(pos, spawnName);
			}
			int count = in.readInt();
			for (int i = 0; i < count; i++) {
				if (!level.addSign(TribuSign.LoadFromStream(plugin, world, in))) {
					plugin.LogWarning(plugin.getLocale("Warning.UnableToAddSign"));
				}
			}

			byte iCount;
			Package n;
			HashMap<Enchantment, Integer> ench = new HashMap<Enchantment, Integer>();
			int id;
			int enchNumber;
			short amount;
			short data;
			// Number of packages
			count = in.readShort();
			// Each packages
			for (int i = 0; i < count; i++) {
				// Init a new package
				n = new Package();
				// Package name length
				int strC = in.readByte();
				char[] c = new char[strC];
				byte k = 0;
				// Read each char
				while (k < strC) {
					c[k] = in.readChar();
					k++;
				}
				// Set the package name
				n.setName(new String(c));
				// Number of items
				iCount = in.readByte();
				// Each item
				for (k = 0; k < iCount; k++) {

					// Item type
					id = in.readInt();
					// Item data
					data = in.readShort();
					// Amount of item
					amount = in.readShort();
					// Number of enchantments
					enchNumber = in.readByte();
					// Clear previous enchantments
					ench.clear();
					// Each enchantment
					while (enchNumber != 0) {
						// Read enchantment type and enchantment level
						ench.put(Enchantment.getById(in.readInt()), in.readInt());
						enchNumber--;
					}
					// Add this item to the package with enchantments
					n.addItem(id, data, amount, ench);

				}
				// Add this package to the level
				level.addPackage(n);

			}

		} catch (Exception e) {
			plugin.LogSevere(String.format(plugin.getLocale("Severe.ErrorDuringLevelLoading"), Tribu.getExceptionMessage(e)));
			level = null;
		}

		return level;
	}

	public TribuLevel loadLevelIgnoreCase(String name) {
		for (String level : levels) {
			if (level.equalsIgnoreCase(name))
				name = level;
		}
		return loadLevel(name);
	}

	public TribuLevel newLevel(String name, Location spawn) {
		return new TribuLevel(name, spawn);
	}

	public boolean saveLevel(TribuLevel level) {
		if (level == null) {
			return true; // Sorta successful since a save isn't really needed
							// and nothing failed
		}

		if (!level.hasChanged()) {
			return true; // No need to save since the level hasn't changed
		}

		FileOutputStream out;
		DataOutputStream o;
		try {
			out = new FileOutputStream(Constants.levelFolder + level.getName() + ".lvl", false);
			o = new DataOutputStream(out);
			Location spawn = level.getInitialSpawn();
			Location death = level.getDeathSpawn();

			o.writeByte(Constants.LevelFileVersion);

			o.writeUTF(spawn.getWorld().getName());
			o.writeDouble(spawn.getX());
			o.writeDouble(spawn.getY());
			o.writeDouble(spawn.getZ());
			o.writeFloat(spawn.getYaw());

			o.writeDouble(death.getX());
			o.writeDouble(death.getY());
			o.writeDouble(death.getZ());
			o.writeFloat(death.getYaw());

			HashMap<String, Location> zombieSpawns = level.getSpawns();
			Set<Entry<String, Location>> set = zombieSpawns.entrySet();

			o.writeInt(set.size());
			for (Entry<String, Location> zspawn : set) {
				o.writeDouble(zspawn.getValue().getX());
				o.writeDouble(zspawn.getValue().getY());
				o.writeDouble(zspawn.getValue().getZ());
				o.writeFloat(zspawn.getValue().getYaw());
				o.writeUTF(zspawn.getKey());
			}
			TribuSign[] signs = level.getSigns();
			if (signs == null) {
				o.writeInt(0);
			} else {
				o.writeInt(signs.length);
				for (int i = 0; i < signs.length; i++) {
					signs[i].SaveToStream(o);
				}
			}
			// Number of packages
			o.writeShort((short) level.getPackages().size());
			// Each package
			for (Package n : level.getPackages()) {
				// Pck name length
				o.write(n.getName().length());
				// Pck name chars
				o.writeChars(n.getName());
				// Pck number of items
				o.write(n.getItemStacks().size());
				// Each item
				for (ItemStack is : n.getItemStacks()) {
					// Item id
					o.writeInt(is.getTypeId());
					// Durability = subid (getData() but on short (useful for
					// potions)
					o.writeShort(is.getDurability());
					// Amount
					o.writeShort(is.getAmount());
					// Number of enchantments for this item
					o.write(is.getEnchantments().size());
					// Each enchantment
					for (Entry<Enchantment, Integer> ench : is.getEnchantments().entrySet()) {
						// Enchantment type
						o.writeInt(ench.getKey().getId());
						// Enchantment level
						o.writeInt(ench.getValue());
					}
				}
			}
			o.flush();
			o.close();
			out.close();
		} catch (Exception e) {
			plugin.LogSevere(String.format(plugin.getLocale("Severe.ErrorDuringLevelSaving"), Tribu.getExceptionMessage(e)));
			return false;
		}
		levels.add(level.getName());
		return true;
	}
}
