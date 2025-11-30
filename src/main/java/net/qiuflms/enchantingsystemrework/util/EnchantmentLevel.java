package net.qiuflms.enchantingsystemrework.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class EnchantmentLevel {
    private final int OVERWORLD;
    private final int NETHER;
    private final int END;

    public EnchantmentLevel(int overworld, int nether, int end){
        this.OVERWORLD = overworld;
        this.NETHER = nether;
        this.END = end;
    }

    public int getMinLevel(RegistryKey<World> world){
        if(world == World.END){
            return getNether() == 0 || getNether() == getEnd()? 1 : getNether() + 1 ;
        }

        if(world == World.NETHER){
            return getOverworld() == 0 || getOverworld() == getNether()? 1 : getOverworld() + 1 ;
        }

        return 1;
    }

    public int getOverworld(){
        return OVERWORLD;
    }

    public int getNether(){
        return NETHER;
    }

    public int getEnd(){
        return END;
    }

    public int getDimensionLevel(RegistryKey<World> world){
        if(world == World.OVERWORLD){
            return getOverworld();
        }

        if(world == World.NETHER){
            return getNether();
        }

        return getEnd();
    }
}
