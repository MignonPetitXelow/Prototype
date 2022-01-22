package fr.vx.rpg.classes.Npc;

import fr.vx.rpg.classes.House.History.HistoryBranch;
import fr.vx.rpg.classes.Npc.Events.NpcRightClicked;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class NpcInteraction implements Listener
{
	
	public NpcInteraction() {}

	public abstract int HistoryLevel();
	public abstract HistoryBranch HistoryBranch();
	public abstract String NameOfNpc();
	protected abstract int action(Player player);

	@EventHandler
	public void a(NpcRightClicked event)
	{
		if(event.getName() == NameOfNpc())
		{
			action(event.getPlayer());
		}
	}
}