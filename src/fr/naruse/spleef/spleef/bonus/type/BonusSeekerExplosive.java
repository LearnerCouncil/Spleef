package fr.naruse.spleef.spleef.bonus.type;

import fr.naruse.spleef.spleef.bonus.BonusManager;
import fr.naruse.spleef.spleef.bonus.utils.MoveToGoal;
import org.bukkit.entity.*;

import java.util.Optional;

public class BonusSeekerExplosive extends BonusExplosive {

    public BonusSeekerExplosive(BonusManager bonusManager, Player p) {
        super(bonusManager, p, "§c§lSeeker Explosive Sheep");
    }

    @Override
    protected void onTick() {
        super.onTick();
        if(this.sheep != null && !this.sheep.isDead()){
            Optional<? extends Player> optional = getNearbyPlayers(this.sheep.getLocation(), 25, 25, 25).findFirst();

            if(!optional.isPresent()){
                return;
            }
            if(optional.get() == this.p){
                return;
            }

            this.runSync(() -> {
                this.sheep.setTarget(optional.get());
                new MoveToGoal(this.sheep, optional.get().getLocation()).execute(1.5);
            });
        }
    }
}
