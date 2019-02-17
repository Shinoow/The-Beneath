package com.shinoow.beneath.common.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.entity.ai.EntityAIFleeLight;
import com.shinoow.beneath.common.entity.ai.EntityAIFollowPlayer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityShadow extends EntityMob {

	private EntityHand hand = null;

	public int cooldown = 0;

	public EntityShadow(World worldIn) {
		super(worldIn);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIFleeLight(this, 1.0D));
		tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(4, new EntityAIWander(this, 1.0D));
		tasks.addTask(4, new EntityAIFollowPlayer(this));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(5, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("Cooldown", cooldown);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		cooldown = tag.getInteger("Cooldown");
	}

	@Override
	public SoundEvent getAmbientSound(){
		SoundEvent sound = Beneath.beneath_normal;
		boolean dark = getName().equalsIgnoreCase("dark"+"osto") && rand.nextInt(4) == 0;

		switch(rand.nextInt(4)){
		case 0:
			sound = Beneath.beneath_normal;
			break;
		case 1:
			sound = Beneath.beneath_muffled;
			break;
		case 2:
			sound = Beneath.beneath_drawnout;
			break;
		case 3:
			sound = Beneath.deepdank;
			break;
		}
		return dark ? rand.nextBoolean() ? Beneath.dark2 : Beneath.dark1 : sound;
	}

	@Override
	public void onLivingUpdate()
	{
		if(dimension != Beneath.dim)
			world.removeEntity(this);

		if(Beneath.shadowHand) {
			if (cooldown > 0)
				cooldown--;
			if (!world.isRemote && cooldown <= 0 && rand.nextBoolean()) {
				List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(16, 16, 16));
				if(!players.isEmpty())
					for(EntityPlayer target : players)
						if (target != null && !target.capabilities.isCreativeMode && world.getLight(target.getPosition()) >= 5) {
							float distanceSq = (float)getDistanceSqToEntity(target);
							if (distanceSq > 9.0F && distanceSq < 100.0F && getEntitySenses().canSee(target)) {
								world.spawnEntity(new EntityHand(world, this, target));
								cooldown = 200 + rand.nextInt(32);
								break;
							}
						}
			}
		}
		super.onLivingUpdate();
	}

	protected boolean teleportRandomly()
	{
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return attemptTeleport(d0, d1, d2);
	}

	@Override
	public boolean attemptTeleport(double x, double y, double z)
	{
		double d0 = posX;
		double d1 = posY;
		double d2 = posZ;
		posX = x;
		posY = y;
		posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(this);
		World world = this.world;
		getRNG();

		if (world.isBlockLoaded(blockpos))
		{
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0)
			{
				BlockPos blockpos1 = blockpos.down();
				IBlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement())
					flag1 = true;
				else
				{
					--posY;
					blockpos = blockpos1;
				}
			}

			if (flag1)
			{
				setPositionAndUpdate(posX, posY, posZ);

				if (world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox()))
					flag = true;
			}
		}

		if (!flag)
		{
			setPositionAndUpdate(d0, d1, d2);
			return false;
		}
		else
		{

			if (this instanceof EntityCreature)
				((EntityCreature)this).getNavigator().clearPathEntity();

			return true;
		}
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		if(dimension != Beneath.dim)
			world.removeEntity(this);

		cooldown = rand.nextInt(200);

		return livingdata;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender() { return false; }

	@Override
	public boolean getAlwaysRenderNameTag() { return false; }

	@Override
	public boolean hasCustomName() { return false; }

	@Override
	protected ResourceLocation getLootTable()
	{
		return Beneath.shadow_loot_table;
	}

	@Override
	public String getName() {
		if (!getCustomNameTag().isEmpty())
			return getCustomNameTag();
		return super.getName();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) { teleportRandomly(); return false; }

	@Override
	public void addPotionEffect(PotionEffect potioneffectIn) {}
}