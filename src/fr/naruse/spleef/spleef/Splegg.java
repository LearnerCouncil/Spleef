package fr.naruse.spleef.spleef;

import fr.naruse.spleef.main.SpleefPlugin;
import fr.naruse.spleef.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Splegg extends Spleef {
    public Splegg(SpleefPlugin pl, int id, String name, boolean isOpened, int max, int min, Location arena, Location spawn, Location lobby) {
        super(pl, id, name, isOpened, max, min, arena, spawn, lobby);
    }

    @Override
    public void start() {
        time = pl.getConfig().getInt("timer.start");
        currentStatus = GameStatus.GAME;
        sendMessage(getFullName()+" "+pl.getMessageManager().get("gameStarts"));
        for (Player player : playerInGame) {
            player.teleport(arena);

            boolean flag1 = Utils.RANDOM.nextBoolean();
            boolean flag2 = Utils.RANDOM.nextBoolean();
            Vector vector = new Vector(
                    flag1 ? Utils.RANDOM.nextDouble()+0.3 : -Utils.RANDOM.nextDouble()+0.3, 1, flag2 ? Utils.RANDOM.nextDouble()+0.3 : -Utils.RANDOM.nextDouble()+0.3);
            player.setVelocity(vector);
            player.getInventory().addItem(Utils.SNOWBALL);
        }
    }

    @EventHandler
    public void shoot(ProjectileHitEvent e){
        if(e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player){
            Player p = (Player) e.getEntity().getShooter();
            if(!hasPlayer(p) || currentStatus == GameStatus.WAIT){
                return;
            }
            p.getInventory().addItem(Utils.SNOWBALL);
            if(e.getHitBlock() != null){
                Block block = e.getHitBlock();
                blocks.add(block);
                block.setType(Material.AIR);
            }
        }
    }

    @Override
    protected String getSignLine(String path) {
        return pl.getMessageManager().get("sign.splegg" +
                        "."+path, new String[]{"name", "size", "max", "min", "missing"},
                new String[]{getFullName(), playerInGame.size()+"", max+"", min+"", (min-playerInGame.size())+""});
    }
}