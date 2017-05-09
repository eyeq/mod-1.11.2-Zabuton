package eyeq.zabuton.item;

import eyeq.util.item.IItemSpawnEntity;
import eyeq.zabuton.entity.projectile.EntityZabuton;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZabuton extends Item implements IItemSpawnEntity {
    public ItemZabuton() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(8);
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(itemStack.getMetadata()).getUnlocalizedName();
    }

    @Override
    public Entity spawnEntity(ItemStack itemStack, World world, double x, double y, double z) {
        EntityZabuton entity = new EntityZabuton(world);
        entity.setPosition(x, y, z);
        entity.setColor(EnumDyeColor.byMetadata(itemStack.getMetadata()));
        world.spawnEntity(entity);
        return entity;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if(!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) {
            return EnumActionResult.FAIL;
        }
        Entity entity = this.spawnEntity(itemStack, world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        entity.rotationYaw = player.rotationYaw;
        if(!player.isCreative()) {
            itemStack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for(int i = 0; i < 16; i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }
}
