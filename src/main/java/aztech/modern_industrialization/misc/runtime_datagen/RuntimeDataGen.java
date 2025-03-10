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
package aztech.modern_industrialization.misc.runtime_datagen;

import aztech.modern_industrialization.ModernIndustrialization;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;

public class RuntimeDataGen {
    public static void run(Consumer<FabricDataGenerator> config) {
        try {
            runInner(config);
        } catch (Exception ex) {
            ModernIndustrialization.LOGGER.error("Failed to runtime datagen", ex);
        }
    }

    private static void runInner(Consumer<FabricDataGenerator> config) throws Exception {
        var miFolder = FabricLoader.getInstance().getGameDir().resolve("modern_industrialization");
        var dataOutput = miFolder.resolve("runtime_datagen");

        ModernIndustrialization.LOGGER.info("Starting MI runtime data generation");

        var modContainer = FabricLoader.getInstance().getModContainer(ModernIndustrialization.MOD_ID).get();
        var gen = new FabricDataGenerator(dataOutput, modContainer, true);
        config.accept(gen);
        gen.run();

        ModernIndustrialization.LOGGER.info("Starting MI runtime pack calculation");

        var cleanedOutput = miFolder.resolve("generated_resources");
        // Delete output folder first before copying, otherwise Windows will complain
        // Code from https://stackoverflow.com/questions/35988192/java-nio-most-concise-recursive-directory-delete
        if (Files.exists(cleanedOutput)) {
            try (Stream<Path> walk = Files.walk(cleanedOutput)) {
                walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }

        // Check caches to see which files need to be copied over
        try (var cacheStream = Files.walk(dataOutput.resolve(".cache"), 1)) {
            cacheStream.forEach(cachePath -> {
                if (Files.isDirectory(cachePath)) {
                    return;
                }

                var newCache = readCache(cachePath);
                var oldCache = readCache(modContainer.findPath(".cache/" + cachePath.getFileName()).get());

                for (var newEntry : newCache.entrySet()) {
                    var oldHash = oldCache.get(newEntry.getKey());

                    if (newEntry.getValue().equals(oldHash)) {
                        // File is unchanged, don't copy it
                        continue;
                    }

                    // File is changed, copy it
                    var newPath = cleanedOutput.resolve(newEntry.getKey());
                    try {
                        Files.createDirectories(newPath.getParent());
                        Files.copy(dataOutput.resolve(newEntry.getKey()), newPath);
                    } catch (IOException e) {
                        ModernIndustrialization.LOGGER.error("Failed to copy file " + newEntry.getKey(), e);
                    }
                }
            });
        }

        ModernIndustrialization.LOGGER.info("Successfully finished MI runtime data generation");
    }

    /**
     * Read cache, and return map from resource path to hash.
     */
    private static Map<String, String> readCache(Path path) {
        try (var reader = Files.newBufferedReader(path)) {
            Map<String, String> map = new HashMap<>();

            // Skip header line
            reader.readLine();

            reader.lines().forEach(line -> {
                String[] parts = line.split(" ", 2);
                map.put(parts[1], parts[0]);
            });

            return map;
        } catch (IOException e) {
            ModernIndustrialization.LOGGER.warn("Failed to read cache file " + path, e);
            return Map.of();
        }
    }
}
