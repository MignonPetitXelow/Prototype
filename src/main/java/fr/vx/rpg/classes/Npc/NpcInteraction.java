package fr.vx.rpg.classes.Npc;

import fr.vx.rpg.classes.History.HistoryBranch;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class NpcInteraction implements Listener
{

	public abstract int MinimumHistoryLevel();
	public abstract boolean PlayerDontHaveHlMessage();
	public abstract HistoryBranch HistoryBranch();
	public abstract String NameOfNpc();
	protected abstract int action(Player player);

	public void interact(Player player)
	{
		action(player);
	}


}
