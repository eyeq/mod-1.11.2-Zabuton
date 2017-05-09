package eyeq.zabuton.entity.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import eyeq.zabuton.Zabuton;

public class EntityZabuton extends Entity implements IProjectile {
    private static final DataParameter<Boolean> DISPENSED = EntityDataManager.createKey(EntityZabuton.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntityZabuton.class, DataSerializers.BYTE);

    public EntityZabuton(World world) {
        super(world);
        this.setSize(0.8F, 0.2F);
        this.preventEntitySpawning = true;
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float d0 = MathHelper.sqrt(x * x + y * y + z * z);
        x /= d0;
        y /= d0;
        z /= d0;
        x += this.rand.nextGaussian() * 0.0075 * inaccuracy;
        y += this.rand.nextGaussian() * 0.0075 * inaccuracy;
        z += this.rand.nextGaussian() * 0.0075 * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.setVelocity(x, y, z);
        float d1 = MathHelper.sqrt(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, d1) * 180 / Math.PI);
        this.setDispensed(true);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(DISPENSED, Boolean.FALSE);
        this.dataManager.register(DYE_COLOR, (byte) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.setColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("Color", (byte) this.getColor().getMetadata());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.world.isRemote) {
            for(Entity entity : this.getPassengers()) {
                if(entity instanceof EntityPlayer) {
                    this.motionX += entity.motionX * 0.2;
                    this.motionZ += entity.motionZ * 0.2;
                }
            }
            double maxSpeed = isDispensed() ? 10.0 : 0.35;
            double speed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if(speed > maxSpeed) {
                maxSpeed /= speed;
                this.motionX *= maxSpeed;
                this.motionZ *= maxSpeed;
            }
        }

        this.motionY -= 0.08;
        if(this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
            this.setDispensed(false);
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.99;
        this.motionY *= 0.95;
        this.motionZ *= 0.99;

        IBlockState state = this.world.getBlockState(this.getPosition());
        if(state.getMaterial().isLiquid()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
            return;
        }
        for(Entity entity : this.getPassengers()) {
            if(entity.isDead) {
                this.removePassenger(entity);
            } else if(entity instanceof EntityLiving) {
                ((EntityLiving) entity).enablePersistence();
            }
        }

        if(this.world.isRemote) {
            return;
        }
        double dx = this.prevPosX - this.posX;
        double dz = this.prevPosZ - this.posZ;
        float yaw;
        if(dx * dx + dz * dz > 0.001) {
            yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI);
        } else {
            yaw = this.rotationYaw;
        }
        yaw = MathHelper.wrapDegrees(yaw - this.rotationYaw);
        if(yaw > 20F) {
            yaw = 20F;
        } else if(yaw < -20F) {
            yaw = -20F;
        }
        this.setRotation(this.rotationYaw + yaw, 0.0F);

        for(EntityZabuton entity : this.world.getEntitiesWithinAABB(EntityZabuton.class, this.getEntityBoundingBox().expand(0.17, 0.0, 0.17))) {
            entity.applyEntityCollision(this);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.world.isRemote || this.isDead) {
            return true;
        }
        this.setBeenAttacked();
        this.entityDropItem(new ItemStack(Zabuton.zabuton, 1, this.getColor().getMetadata()), 1.0F);
        this.setDead();
        if(isBeingRidden()) {
            this.removePassengers();
        }
        return true;
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if(this.world.isRemote || this.isPassenger(entity)) {
            return;
        }
        if((entity instanceof EntityLiving) && !(entity instanceof EntityPlayer) && entity.getRidingEntity() == null) {
            entity.startRiding(this);
            if(entity instanceof EntityTameable) {
                ((EntityTameable) entity).setSitting(true);
            }
        }
        super.applyEntityCollision(entity);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if(!this.world.isRemote && !this.isBeingRidden()) {
            player.startRiding(this);
        }
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    public EnumDyeColor getColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
    }

    public void setColor(EnumDyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getMetadata() & 15));
    }

    public boolean isDispensed() {
        return this.dataManager.get(DISPENSED);
    }

    public void setDispensed(boolean isDispensed) {
        this.dataManager.set(DISPENSED, isDispensed);
    }
}
