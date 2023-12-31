package com.mygdx.game.data;

import com.mygdx.game.model.HitCircle;
import com.mygdx.game.model.HitObject;
import com.mygdx.game.model.Map;
import com.mygdx.game.model.Mapset;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MapLoader {

    private static final String TEMPORARY_DIRECTORY_PATH = System.getProperty("java.io.tmpdir") + "osuclonemap";

    private MapLoader() {  // do no create instance
    }

    public static Map Load(String path) throws IOException {

        unzipFile(path);

        java.util.Map<String, List<String>> filesMap = filterFiles();

        List<Mapset> loadedMapsets = new LinkedList<>();

        for (String mapsetPath : filesMap.get("MAPSET")) {
            loadedMapsets.add(createMapset(mapsetPath));
        }
        return new Map(loadedMapsets, filesMap.get("AUDIO").get(0), filesMap.get("BG").get(0));
    }

    private static Mapset createMapset(String mapsetPath) {
        List<HitObject> hitObjects = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(mapsetPath))) {
            String line;

            String version;
            String name = mapsetPath.substring(mapsetPath.lastIndexOf("\\") + 1, mapsetPath.lastIndexOf("."));
            double hpDrainRate;
            double circleSize;
            double approachRate;

            do { // reads difficulty (version in o.f.f.v14)
                line = reader.readLine();
            } while (!line.contains("Version"));
            version = line.substring(8);

            do { // reads hp drain rate
                line = reader.readLine();
            } while (!line.contains("HPDrainRate"));
            hpDrainRate = Double.parseDouble(line.substring(12));

            do { // reads circle size
                line = reader.readLine();
            } while (!line.contains("CircleSize"));
            circleSize = Double.parseDouble(line.substring(11));

            do { // reads approach rate
                line = reader.readLine();
            } while (!line.contains("ApproachRate"));
            approachRate = Double.parseDouble(line.substring(13));

            do { // waits for HitObjects
                line = reader.readLine();
            } while (!line.contains("[HitObjects]"));

            int counter = 1;
            Random random = new Random();
            int currentMax = random.nextInt(9) + 1; // simulates circle number for now(?...)

            while ((line = reader.readLine()) != null) { // reads hitObjects
                String[] splitLine = line.split(",");
                int osuPixelX = Integer.parseInt(splitLine[0]);
                int osuPixelY = Integer.parseInt(splitLine[1]);
                long time = Long.parseLong(splitLine[2]);

                HitCircle hitCircle = new HitCircle(osuPixelX, osuPixelY, time, counter);
                hitObjects.add(hitCircle);

                if (counter == currentMax) {
                    currentMax = random.nextInt(9) + 1;
                    counter = 1;
                    continue;
                }
                counter++;
            }
            return new Mapset(name, hitObjects, version, hpDrainRate, circleSize, approachRate);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static java.util.Map<String, List<String>> filterFiles() {

        File[] mapFiles = Objects.requireNonNull(new File(TEMPORARY_DIRECTORY_PATH).listFiles());
        java.util.Map<String, List<String>> fileMap = new HashMap<>();
        fileMap.put("AUDIO", new LinkedList<>());
        fileMap.put("HITSOUND", new LinkedList<>());
        fileMap.put("MAPSET", new LinkedList<>());
        fileMap.put("BG", new LinkedList<>());
        fileMap.put("OTHER", new LinkedList<>());

        for (File mapFile : mapFiles) {
            if (!mapFile.isDirectory()) {
                String extension = getExtension(mapFile.getPath());
                switch (extension) {
                    case "mp3":
                    case "ogg":
                        fileMap.get(MapFileType.AUDIO.name()).add(mapFile.getPath());
                        break;

                    case "wav":
                        fileMap.get(MapFileType.HITSOUND.name()).add(mapFile.getPath());
                        break;

                    case "osu":
                        fileMap.get(MapFileType.MAPSET.name()).add(mapFile.getPath());
                        break;

                    case "jpg":
                        fileMap.get(MapFileType.BG.name()).add(mapFile.getPath());
                        break;
                    case "OTHER":
                        fileMap.get(MapFileType.OTHER.name()).add(mapFile.getPath());
                        break;
                }
            }
        }
        return fileMap;
    }

    public static String getExtension(String path) {  //util for MenuScreen.java
        int lastDotIndex = path.lastIndexOf(".");

        if (lastDotIndex != -1 && lastDotIndex < path.length() - 1) {
            return path.substring(lastDotIndex + 1);
        }
        return MapFileType.OTHER.name();
    }

    private static void unzipFile(String path) throws IOException {
        File destDir = new File(TEMPORARY_DIRECTORY_PATH + File.separator);
        if (destDir.exists()) {
            deleteFolder(destDir);
        } else if (!destDir.exists()) {

            if (!destDir.mkdir()) System.out.println("Folder creation error");
        }

        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Paths.get(path)))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = TEMPORARY_DIRECTORY_PATH + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath)))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    File dir = new File(filePath);

                    if (!dir.mkdir()) System.out.println("Folder creation error");
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }

    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    if (!f.delete()) System.out.println("Folder deletion error");
                }
            }
        }
    }

    public static boolean isProperOszFile(String path) {

        String extension = MapLoader.getExtension(path);
        return extension.equals("osz");
    }

    enum MapFileType {
        AUDIO("mp3"),
        HITSOUND("wav"),
        MAPSET("osu"),
        BG("jpg"),
        OTHER("");
        final String extension;

        MapFileType(String extension) {
            this.extension = extension;
        }
    }

    public static void clean() {
        File tempDir = new File(TEMPORARY_DIRECTORY_PATH);
        if (tempDir.exists()) {
            deleteFolder(tempDir);
        }
    }
}
