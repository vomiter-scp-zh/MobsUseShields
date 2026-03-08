package com.vomiter.mobsuseshields.data;

import com.vomiter.mobsuseshields.Helpers;
import com.vomiter.mobsuseshields.MobsUseShields;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    ExistingFileHelper existingFileHelper;
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MobsUseShields.MOD_ID, exFileHelper);
        this.existingFileHelper =exFileHelper;

    }

    private void trackTexture(String namespace, String pathNoExt) {
        existingFileHelper.trackGenerated(
                Helpers.id(namespace, pathNoExt),
                PackType.CLIENT_RESOURCES,
                ".png",
                "textures"
        );
    }
    private void trackTexture(String pathNoExt){
        trackTexture(MobsUseShields.MOD_ID, pathNoExt);
    }

    private void trackModel(String namespace, String pathNoExt) {
        existingFileHelper.trackGenerated(
                Helpers.id(namespace, pathNoExt),
                PackType.CLIENT_RESOURCES,
                ".json",
                "models"
        );
    }
    private void trackModel(String pathNoExt){
        trackModel(MobsUseShields.MOD_ID, pathNoExt);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private ModelFile orientableModel(String name, ResourceLocation front, ResourceLocation side, ResourceLocation top) {
        return models()
                .withExistingParent("block/" + name, mcLoc("block/orientable"))
                .texture("front", front)
                .texture("side", side)
                .texture("top",  top);
    }

    private static int yFromHorizontal(Direction dir) {
        return switch (dir) {
            case NORTH -> 0;
            case EAST  -> 90;
            case SOUTH -> 180;
            case WEST  -> 270;
            default -> 0;
        };
    }

}
