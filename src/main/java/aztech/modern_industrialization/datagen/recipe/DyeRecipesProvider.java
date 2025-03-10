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
package aztech.modern_industrialization.datagen.recipe;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.recipe.json.MIRecipeJson;
import aztech.modern_industrialization.recipe.json.ShapedRecipeJson;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.DyeColor;

public class DyeRecipesProvider extends MIRecipesProvider {
    public DyeRecipesProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            String pathPrefix = "dyes/" + color.getName() + "/";
            // 16 item pipes with dye in the center
            var itemPipesDirect = new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_item_pipe", 16, "CCC", "GdG",
                    "CCC").addInput('C', "modern_industrialization:bronze_curved_plate").addInput('G', "#c:steel_gears").addInput('d',
                            "#c:" + color.getName() + "_dyes");
            itemPipesDirect.offerTo(consumer, pathPrefix + "craft/item_pipe_direct");
            itemPipesDirect.exportToAssembler().offerTo(consumer, pathPrefix + "assembler/item_pipe_direct");
            // 8 item pipes
            var eightItemPipes = new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_item_pipe", 8, "ppp", "pdp",
                    "ppp").addInput('d', "#c:" + color.getName() + "_dyes").addInput('p', "#modern_industrialization:item_pipes");
            eightItemPipes.offerTo(consumer, pathPrefix + "craft/item_pipe_8");
            eightItemPipes.exportToMachine(MIMachineRecipeTypes.MIXER, 2, 100, 1).offerTo(consumer, pathPrefix + "mixer/item_pipe_8");
            // 1 item pipe
            new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_item_pipe", 1, "pd")
                    .addInput('d', "#c:" + color.getName() + "_dyes").addInput('p', "#modern_industrialization:item_pipes")
                    .offerTo(consumer, pathPrefix + "craft/item_pipe_1");
            // 16 fluid pipes with stained glass
            var fluidPipesStainedGlass = new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_fluid_pipe", 16, "CCC",
                    "rPr", "CCC").addInput('C', "modern_industrialization:bronze_curved_plate").addInput('r', "modern_industrialization:copper_rotor")
                            .addInput('P', color.getName() + "_stained_glass_pane");
            fluidPipesStainedGlass.offerTo(consumer, pathPrefix + "craft/fluid_pipe_stained_glass");
            fluidPipesStainedGlass.exportToAssembler().offerTo(consumer, pathPrefix + "assembler/fluid_pipe_stained_glass");
            // 8 fluid pipes
            var eightFluidPipes = new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_fluid_pipe", 8, "ppp", "pdp",
                    "ppp").addInput('d', "#c:" + color.getName() + "_dyes").addInput('p', "#modern_industrialization:fluid_pipes");
            eightFluidPipes.offerTo(consumer, pathPrefix + "craft/fluid_pipe_8");
            eightFluidPipes.exportToMachine(MIMachineRecipeTypes.MIXER, 2, 100, 1).offerTo(consumer, pathPrefix + "mixer/fluid_pipe_8");
            // 1 fluid pipe
            new ShapedRecipeJson("modern_industrialization:" + color.getName() + "_fluid_pipe", 1, "pd")
                    .addInput('d', "#c:" + color.getName() + "_dyes").addInput('p', "#modern_industrialization:fluid_pipes")
                    .offerTo(consumer, pathPrefix + "craft/fluid_pipe_1");
            // generate dyes with synthetic oil
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 200).addFluidInput(MIFluids.SYNTHETIC_OIL, 100)
                    .addItemInput("#c:" + color.getName() + "_dyes", 1, 0).addItemOutput("minecraft:" + color.getName() + "_dye", 1)
                    .offerTo(consumer, pathPrefix + "mixer/synthetic_oil");
            // generate dyes with benzene
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 200).addFluidInput(MIFluids.BENZENE, 25)
                    .addItemInput("#c:" + color.getName() + "_dyes", 1, 0).addItemOutput("minecraft:" + color.getName() + "_dye", 1)
                    .offerTo(consumer, pathPrefix + "mixer/benzene");

            // wool
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#minecraft:wool", 8).addItemOutput("minecraft:" + color.getName() + "_wool", 8)
                    .offerTo(consumer, pathPrefix + "mixer/wool");

            // glass
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#c:glass_blocks", 8)
                    .addItemOutput("minecraft:" + color.getName() + "_stained_glass", 8)
                    .offerTo(consumer, pathPrefix + "mixer/glass");

            // glassPane
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#c:glass_panes", 8).addItemOutput("minecraft:" + color.getName() + "_stained_glass_pane", 8)
                    .offerTo(consumer, pathPrefix + "mixer/glass_pane");

            // shulker Box
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#c:shulker_box", 1).addItemOutput("minecraft:" + color.getName() + "_shulker_box", 1)
                    .offerTo(consumer, pathPrefix + "mixer/shulker_box");

            // bed
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#minecraft:beds", 1)
                    .addItemOutput("minecraft:" + color.getName() + "_bed", 1)
                    .offerTo(consumer, pathPrefix + "mixer/bed");

            // candle
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#minecraft:candles", 1).addItemOutput("minecraft:" + color.getName() + "_candle", 1)
                    .offerTo(consumer, pathPrefix + "mixer/candle");

            // carpet
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#minecraft:carpets", 8).addItemOutput("minecraft:" + color.getName() + "_carpet", 8)
                    .offerTo(consumer, pathPrefix + "mixer/carpet");

            // terracotta
            MIRecipeJson.create(MIMachineRecipeTypes.MIXER, 2, 100).addItemInput("#c:" + color.getName() + "_dyes", 1)
                    .addItemInput("#c:terracottas", 8).addItemOutput("minecraft:" + color.getName() + "_terracotta", 8)
                    .offerTo(consumer, pathPrefix + "mixer/terracotta");

            // glass pane cutting
            MIRecipeJson.create(MIMachineRecipeTypes.CUTTING_MACHINE, 2, 100).addFluidInput(MIFluids.LUBRICANT, 1)
                    .addItemInput("minecraft:" + color.getName() + "_stained_glass", 6)
                    .addItemOutput("minecraft:" + color.getName() + "_stained_glass_pane", 16)
                    .offerTo(consumer, pathPrefix + "cutting_machine/glass_pane");

            // carpet cutting
            MIRecipeJson.create(MIMachineRecipeTypes.CUTTING_MACHINE, 2, 100).addFluidInput(MIFluids.LUBRICANT, 1)
                    .addItemInput("minecraft:" + color.getName() + "_wool", 1)
                    .addItemOutput("minecraft:" + color.getName() + "_carpet", 4)
                    .offerTo(consumer, pathPrefix + "cutting_machine/carpet");
        }
    }
}
