package fr.vx.rpg.classes.Crafting;

import java.util.Arrays;
import java.util.List;
import fr.vx.rpg.classes.Jobs.JobRank;
import fr.vx.rpg.classes.Jobs.Wizard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import fr.vx.rpg.RPG;
import fr.vx.rpg.utils.CustomBlockData;
import fr.vx.rpg.utils.Serializer;

public class CauldronCraft implements Listener {

	private ItemStack result;
	private List<ItemStack> ingredients;
	private JobRank jobRank;
	private int xp;
	
	public CauldronCraft(ItemStack result, JobRank jobRank)
	{
		this.result = result;
		this.jobRank = jobRank;
		this.xp = xp;
	}
	
	public void register() {
		
		Bukkit.getPluginManager().registerEvents(this, RPG.getPlugin(RPG.class));
		
	}
	
	public void setIngredients(List<ItemStack> ingredients) {
		
		this.ingredients = ingredients;
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		Block block = event.getBlock();
		
		if (block.getType() == Material.CAULDRON) {
			
			CustomBlockData craftState = new CustomBlockData(block, RPG.getPlugin(RPG.class));
			craftState.clear();
			
		}
		
	}
	
	public static boolean isCrafted(List<ItemStack> ingredients, List<ItemStack> actualIngredients) {
		
		
		if (ingredients.size() != actualIngredients.size()) {	
			return (false);
		}
		
		
		int equal = 0;
		
		for (ItemStack item : ingredients) {
			
			for (ItemStack item2 : actualIngredients) {
				
				if (item.equals(item2)) {
					
					equal++;
					break;
					
				}
				
			}
			
		}
		
		return (equal == ingredients.size());
		
	}
	
	@EventHandler
	private void onCraft(PlayerDropItemEvent event) {
		
		if (event.getItemDrop() != null) {
			
			List<ItemStack> craftIngredients = this.ingredients;
			ItemStack result = this.result;
			Player player = event.getPlayer();
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					if (event.getItemDrop().isOnGround()) {
						
						ItemStack itemDropped = event.getItemDrop().getItemStack();
						Location location = event.getItemDrop().getLocation();	
						
						if (location.getBlock().getType() == Material.CAULDRON) {
								
				
							NamespacedKey key = new NamespacedKey(RPG.getPlugin(RPG.class), "cauldron-items");
							ItemStack[] new_ingredients;
							CustomBlockData craftState = new CustomBlockData(location.getBlock(), RPG.getPlugin(RPG.class));
							if (craftState.has(key, PersistentDataType.STRING)) {
									
								ItemStack[] ingredients = Serializer.convertStringToItemStackArray(craftState.get(key, PersistentDataType.STRING));
								new_ingredients = new ItemStack[ingredients.length + 1];
									
								for (int i = 0; i < ingredients.length; i++) {
									new_ingredients[i] = ingredients[i];
								}
									
								new_ingredients[ingredients.length] = itemDropped;
								craftState.set(key, PersistentDataType.STRING, Serializer.convertItemStackArrayToString(new_ingredients));
								
							} else {
									
								new_ingredients = new ItemStack[1];
								new_ingredients[0] = itemDropped;
								craftState.set(key, PersistentDataType.STRING, Serializer.convertItemStackArrayToString(new_ingredients));

							}
							
							if (CauldronCraft.isCrafted(craftIngredients, Arrays.asList(new_ingredients))) {
								if(jobRank.getLvlId() > Wizard.getLvl(player))
								{
									player.sendMessage(ChatColor.RED+"Vous avez essayez de faire une recette..");
									player.sendMessage(ChatColor.RED+"Mais malheuresement vous n'aviez pas le niveau, et vous l'avez ratez..");
									player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_HURT, 3.0F, 0.5F);
									craftState.clear();
									return;
								}
								location.getWorld().dropItemNaturally(location.add(0, 1, 0), result);
								craftState.clear();
								System.out.println("CRAFTE");
							}
							
							for (int i = 0; i < new_ingredients.length; i++) {
								System.out.println(new_ingredients[i].getType().toString());
							}
							event.getItemDrop().remove();
							
						}
						
						this.cancel();
						
					}

				}
				
			}.runTaskTimer(RPG.getPlugin(RPG.class), 0, 1);
			
		}
		
	}
	
}
