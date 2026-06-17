package com.vomiter.mobsuseshields.data;

import com.vomiter.mobsuseshields.Helpers;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModTagProviders {
    DataGenerator generator;
    PackOutput output;
    CompletableFuture<HolderLookup.Provider> lookupProvider;
    public ModTagProviders(GatherDataEvent event){
        generator = event.getGenerator();
        output = generator.getPackOutput();
        lookupProvider = event.getLookupProvider();

        var blockTags = new BlockTags();
        var itemTags = new ItemTags(blockTags);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, itemTags);

    }


    class BlockTags extends BlockTagsProvider {

        public BlockTags() {
            super(output, lookupProvider, MobsUseShields.MOD_ID);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {

        }
    }

    class ItemTags extends ItemTagsProvider {
        public ItemTags(BlockTags blockTags) {
            super(output, lookupProvider, MobsUseShields.MOD_ID);
        }
        static TagKey<Item> create(Identifier id){
            return TagKey.create(
                    Registries.ITEM,
                    id
            );
        }
        public static TagKey<Item> test = create(Helpers.id("test"));

        @Override
        protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
            //tag(test).add(Items.ACACIA_BOAT);
        }
    }


}
