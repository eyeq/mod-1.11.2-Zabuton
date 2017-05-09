package eyeq.zabuton;

import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.dispenser.UBehaviorDispenseEntity;
import eyeq.util.oredict.UOreDictionary;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import eyeq.zabuton.entity.projectile.EntityZabuton;
import eyeq.zabuton.client.renderer.entity.RenderZabuton;
import eyeq.zabuton.item.ItemZabuton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;

import static eyeq.zabuton.Zabuton.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class Zabuton {
    public static final String MOD_ID = "eyeq_zabuton";

    @Mod.Instance(MOD_ID)
    public static Zabuton instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static Item zabuton;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        registerEntities();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        registerEntityRenderings();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        zabuton = new ItemZabuton().setUnlocalizedName("zabuton");

        GameRegistry.register(zabuton, resource.createResourceLocation("zabuton"));

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(zabuton, new UBehaviorDispenseEntity().setSoundId(1002));
    }

    public static void addRecipes() {
        for(EnumDyeColor color : EnumDyeColor.values()) {
            int meta = color.getMetadata();
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(zabuton, 1, meta), "Y ", "XX",
                    'X', new ItemStack(Blocks.WOOL, 1, meta), 'Y', UOreDictionary.OREDICT_STRING));
        }
    }

    public static void registerEntities() {
        EntityRegistry.registerModEntity(resource.createResourceLocation("Zabuton"), EntityZabuton.class, "Zabuton", 0, instance, 80, 3, true);
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        for(EnumDyeColor color : EnumDyeColor.values()) {
            int meta = color.getMetadata();
            ModelLoader.setCustomModelResourceLocation(zabuton, meta, resource.createModelResourceLocation(color.getName() + "_zabuton"));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityZabuton.class, RenderZabuton::new);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-Zabuton");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, "item.zabuton.black.name", "Black Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.black.name", "黒色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.red.name", "Red Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.red.name", "赤色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.green.name", "Green Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.green.name", "緑色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.brown.name", "Brown Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.brown.name", "茶色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.blue.name", "Blue Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.blue.name", "青色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.purple.name", "Purple Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.purple.name", "紫色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.cyan.name", "Cyan Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.cyan.name", "水色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.silver.name", "Silver Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.silver.name", "薄灰色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.gray.name", "Gray Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.gray.name", "灰色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.pink.name", "Pink Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.pink.name", "桃色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.lime.name", "Lime Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.lime.name", "黄緑色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.yellow.name", "Yellow Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.yellow.name", "黄色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.lightBlue.name", "Light Blue Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.lightBlue.name", "空色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.magenta.name", "Magenta Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.magenta.name", "赤紫色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.orange.name", "Orange Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.orange.name", "橙色の座布団");
        language.register(LanguageResourceManager.EN_US, "item.zabuton.white.name", "White Zabuton");
        language.register(LanguageResourceManager.JA_JP, "item.zabuton.white.name", "白色の座布団");

        language.register(LanguageResourceManager.EN_US, EntityZabuton.class, "Zabuton");
        language.register(LanguageResourceManager.JA_JP, EntityZabuton.class, "座布団");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, zabuton, ItemmodelJsonFactory.ItemmodelParent.GENERATED);

        for(EnumDyeColor color : EnumDyeColor.values()) {
            UModelCreator.createItemJson(project, resource.createResourceLocation(color.getName() + "_zabuton"), ItemmodelJsonFactory.ItemmodelParent.GENERATED, resource.createResourceLocation("zabuton_" + color.getName()));
        }
    }
}
