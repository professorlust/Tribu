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
package graindcafe.tribu.Signs;

import graindcafe.tribu.Tribu;
import graindcafe.tribu.Events.SignClickEvent;
import graindcafe.tribu.Events.WaveStartEvent;
import graindcafe.tribu.Level.TribuLevel;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public class JoinSign extends TribuSign {

	public JoinSign(final Tribu plugin, final Location pos, final String[] lines) {
		super(plugin, pos);
		final Sign s = ((Sign) pos.getBlock().getState());
		s.setLine(1, "");
		s.setLine(2, "");
		s.setLine(3, "");
		s.update();
	}

	@Override
	public void finish() {

	}

	@Override
	protected String[] getSpecificLines() {
		final String[] lines = new String[4];
		TribuLevel l = plugin.getLevel();
		if (l == null) {
			lines[0] = "";
			lines[1] = "";
			lines[2] = "";
			lines[3] = "";
		} else {
			lines[1] = plugin.formatLocale("Sign.JoinLevelName", l.getName());
			lines[2] = plugin.formatLocale("Sign.JoinPlayerCount",
					String.valueOf(plugin.getPlayersCount()));
			lines[3] = plugin.formatLocale("Sign.JoinWaveNumber",
					String.valueOf(plugin.getWaveStarter().getWaveNumber()));
		}
		return lines;
	}

	@Override
	public void init() {

	}

	@Override
	public boolean isUsedEvent(final Event e) {
		return (pos.getBlock().getState() instanceof Sign)
				&& ((e instanceof SignClickEvent) || (e instanceof WaveStartEvent));
	}

	@Override
	public void raiseEvent(final Event ev) {
		if (ev instanceof SignClickEvent) {
			final SignClickEvent e = (SignClickEvent) ev;
			plugin.addPlayer(e.getPlayer());
		}
		final Sign s = ((Sign) pos.getBlock().getState());
		final String[] lines = getSpecificLines();
		s.setLine(1, lines[1]);
		s.setLine(2, lines[2]);
		s.setLine(3, lines[3]);
		s.update();
	}
}
