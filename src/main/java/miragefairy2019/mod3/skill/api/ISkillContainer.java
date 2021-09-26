package miragefairy2019.mod3.skill.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ISkillContainer {

    /**
     * Server World Only
     */
    public void load(EntityPlayer player);

    /**
     * Server World Only
     */
    public void save(EntityPlayer player);

    /**
     * Server World Only
     */
    public void send(EntityPlayerMP player);

    //

    public int getMasteryLevel(IMastery mastery);

    public void setMasteryLevel(IMastery mastery, int masteryLevel);

    public ISkillVariables getVariables();

}
