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
package graindcafe.tribu.BlockTrace;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class Node {
	protected Location location;
	protected int id;
	protected byte data;
	protected boolean finalState=false;
	
	/**
	 * Create a regular node based on the block to save
	 * @param Block to save
	 */
	public Node(BlockState element)
	{
		location=element.getLocation().clone();
		id=element.getTypeId();
		data=element.getData().getData();
		finalState=true;
	}
	public Node(Block element)
	{
		location=element.getLocation().clone();
		id=element.getTypeId();
		data=element.getData();
		finalState=true;
	}
	protected Node()
	{
	}
	protected Node(Location loc, int id, byte data)
	{
		this.location=loc;
		this.id=id;
		this.data=data;
	}
	public Material getType()
	{
		return Material.getMaterial(id);
	}
	public void reverse()
	{
		this.location.getBlock().setTypeIdAndData(id, data, false);
	}
	public Location getLocation()
	{
		return location;
	}
}
