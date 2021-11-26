package fr.vx.rpg.classes.Item;

import fr.vx.rpg.RPG;
import fr.vx.rpg.classes.mobs.Mob;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.WorldServer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Weapon extends Item implements Listener
{
    private Material tools;
    private String name;
    private Rarity rarity;
    private int attackdamage;
    private EntityType monster;
    private boolean dropable;

    public Weapon(Material tools, String name, Rarity rarity, int attackdamage)
    {
        super(tools, name, rarity, attackdamage);
        this.tools = tools;
        this.name = name;
        this.rarity = rarity;
        this.attackdamage = attackdamage;
    }

    public Weapon(Material tools, String name, Rarity rarity, int attackdamage, EntityType dropOn)
    {
        super(tools, name, rarity, attackdamage);
        this.tools = tools;
        this.name = name;
        this.rarity = rarity;
        this.attackdamage = attackdamage;
        this.monster = dropOn;
        this.dropable = true;
        Bukkit.getPluginManager().registerEvents(this, RPG.getPlugin(RPG.class));
    }

    @Override
    public ItemStack getItemStack()
    {
        ItemStack itemstack = new ItemStack(this.tools);
        ItemMeta itemMeta = itemstack.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", attackdamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemstack.setItemMeta(itemMeta);
        return itemstack;
    }

    @EventHandler
    public void DropOn(EntityDeathEvent e)
    {
        EntityType monster = e.getEntityType();
        Location location = e.getEntity().getLocation();
        if(dropable == true && monster.equals(this.monster))
        {
        	@SuppressWarnings("unchecked")
			EntityTypes<? extends EntityCreature> type = (EntityTypes<? extends EntityCreature>) ((CraftEntity) e.getEntity()).getHandle().getEntityType();
        	Mob mob = new Mob(type, e.getEntity().getLocation(), "YOO", 2);
        	WorldServer world = ((CraftWorld) e.getEntity().getWorld()).getHandle();
        	world.addEntity(mob);
            e.getDrops().clear(); e.getDrops().add(this.getItemStack());
        }
    }
}
