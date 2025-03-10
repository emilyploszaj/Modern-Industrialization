/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.mixin;

import aztech.modern_industrialization.MIConfig;
import aztech.modern_industrialization.resource.GeneratedFolderPackResources;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldLoader.PackConfig.class)
public class PackConfigMixin {
    @ModifyVariable(method = "createResourceManager", at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/server/packs/repository/PackRepository.openAllSelected ()Ljava/util/List;"), index = 2)
    private List<PackResources> injectCreateReload(List<PackResources> resourcePacks) {
        if (MIConfig.getConfig().loadRuntimeGeneratedResources) {
            var mutableList = new ArrayList<>(resourcePacks);
            var generatedDirectory = FabricLoader.getInstance().getGameDir().resolve("modern_industrialization/generated_resources");
            mutableList.add(new GeneratedFolderPackResources(generatedDirectory.toFile(), PackType.SERVER_DATA));
            return mutableList;
        } else {
            return resourcePacks;
        }
    }
}
