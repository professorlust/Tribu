package graindcafe.tribu.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class TribuInventory 
{
	protected HashMap<Player, List<ItemStack>> inventories;
	
	public TribuInventory() 
	{
		inventories = new HashMap<Player, List<ItemStack>>();
	}
	
	public void addInventory(Player p) 
	{
		PlayerInventory pInv = p.getInventory();
		List<ItemStack> lstInv = new LinkedList<ItemStack>(Arrays.asList(pInv.getContents().clone()));
		
		if (pInv.getBoots() != null)
			lstInv.add(pInv.getBoots());
		
		if (pInv.getChestplate() != null)
			lstInv.add(pInv.getChestplate());
		
		if (pInv.getHelmet() != null)
			lstInv.add(pInv.getHelmet());
		
		if (pInv.getLeggings() != null)
			lstInv.add(pInv.getLeggings());
		synchronized(inventories) 
		{
			inventories.put(p, lstInv);
		}
	}
	public void addInventories(Set<Player> players)
	{
		for(Player p: players)
		{
			addInventory(p);
		}
	}
	public void restoreInventories()
	{
		Set<Player> players=inventories.keySet();
		for(Player p: players)
		{
			uncheckedRestoreInventory(p);
		}
	}
	public void uncheckedRestoreInventory(Player p)
	{
		synchronized(inventories) 
		{
			Inventory pInv = p.getInventory();
			pInv.clear();
			HashMap<Integer, ItemStack> extra = new HashMap<Integer, ItemStack>();
			List<ItemStack> items=inventories.remove(p);
			for (ItemStack is : items) 
			{
				if (is != null) 
				{
					extra.putAll(pInv.addItem(is));
				}
			}
			for (ItemStack eIs : extra.values()) 
			{
				p.getWorld().dropItem(p.getLocation(), eIs);
			}
		}	
	}
	public void restoreInventory(Player p) 
	{
		if (inventories.containsKey(p)) 
		{
			uncheckedRestoreInventory(p);
		}
	}
}
