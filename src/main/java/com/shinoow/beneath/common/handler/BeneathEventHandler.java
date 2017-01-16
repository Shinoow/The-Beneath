package com.shinoow.beneath.common.handler;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;

public class BeneathEventHandler {

	@SubscribeEvent
	public void darkness(LivingUpdateEvent event){
		if(!Beneath.mode.equalsIgnoreCase("darkness")) return;
		if(event.getEntityLiving().world.isRemote) return;
		if(event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().world.provider.getDimension() == Beneath.dim){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if(player.capabilities.isCreativeMode) return;
			if(player.getBrightness(1.0F) <= 0.05F && player.ticksExisted % (Beneath.darkTimer * 20) == 0)
				player.attackEntityFrom(Beneath.darkness, Beneath.darkDamage);
		}
	}

	@SubscribeEvent
	public void boostStats(EntityJoinWorldEvent event){
		if(event.getEntity() instanceof EntityMob && event.getWorld().provider.getDimension() == Beneath.dim){
			IAttributeInstance damage = ((EntityMob)event.getEntity()).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			IAttributeInstance health = ((EntityMob)event.getEntity()).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			UUID uuid1 = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9");
			UUID uuid2 = UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC");
			AttributeModifier dmg = new AttributeModifier(uuid1, "The Beneath Attack Damage Boost", damage.getBaseValue(), 0);
			AttributeModifier hp = new AttributeModifier(uuid2, "The Beneath Health Boost", health.getBaseValue(), 0);
			damage.removeModifier(dmg);
			damage.applyModifier(dmg);
			health.removeModifier(hp);
			health.applyModifier(hp);
			((EntityMob)event.getEntity()).setHealth(((EntityMob)event.getEntity()).getMaxHealth());
		}
	}

	@SubscribeEvent
	public void endTeleportation(PlayerChangedDimensionEvent event){
		if(!Beneath.dimTeleportation) return;
		if(event.fromDim == 1 && event.toDim == Beneath.dim)
			if(!event.player.world.isRemote){
				World world = event.player.world;
				BlockPos pos = event.player.getPosition().down();
				if(world.getBlockState(pos).getBlock() != Beneath.teleporter){
					for(int x = -2; x < 3; x++)
						for(int y = -1; y < 3; y++)
							for(int z = -2; z < 3; z++)
								if(y > -1)
									world.setBlockToAir(pos.add(x, y, z));
								else if(world.isAirBlock(pos.add(x, y, z)))
									world.setBlockState(pos.add(x, y, z), Blocks.STONE.getDefaultState());
					world.setBlockState(pos, Beneath.teleporter.getDefaultState());
				}
				TileEntity te = world.getTileEntity(pos);
				if(te != null && te instanceof TileEntityTeleporterDeepDank)
					((TileEntityTeleporterDeepDank)te).setDimension(1);
			}
	}
}