package com.mygdx.game.data;

import com.mygdx.game.model.HitCircle;
import com.mygdx.game.model.HitObject;
import com.mygdx.game.model.Map;
import com.mygdx.game.model.Mapset;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MapLoader {

    private static final String TEMPORARY_DIRECTORY_PATH = System.getProperty("java.io.tmpdir") + "osuclonemap";

    public static void main(String[] args) {
        System.out.println(TEMPORARY_DIRECTORY_PATH);
    }

    private MapLoader() {
        // do no create instance
    }

    public static Map Load() {

        // test data

        HitCircle hitCircle1 = new HitCircle(201, 138, 705);
        HitCircle hitCircle2 = new HitCircle(400, 123, 1200);
        HitCircle hitCircle3 = new HitCircle(288, 155, 1781);
        HitCircle hitCircle4 = new HitCircle(195, 267, 2336);
        HitCircle hitCircle5 = new HitCircle(299, 127, 2989);

        List<HitObject> testData = new ArrayList<>(List.of(hitCircle1, hitCircle2, hitCircle3, hitCircle4, hitCircle5));

        return new Map(new Mapset(testData));
    }

    public static Map Load(String path) throws IOException {

        unzipFile(path);

        java.util.Map<String, String> filesMap = filterFilter();


        return null;
    }

    private static java.util.Map<String, String> filterFilter() {

        List<File> mapFiles = List.of(Objects.requireNonNull(new File(TEMPORARY_DIRECTORY_PATH).listFiles()));

        for (File mapFile : mapFiles) {
            if (!mapFile.isDirectory()) {
                String extension = getExtension(mapFile.getPath());
            }

        }

    }

    private static String getExtension(String path) {

    }


    private static void unzipFile(String path) throws IOException {
        File destDir = new File(TEMPORARY_DIRECTORY_PATH + File.separator);
        if (destDir.exists()) {
            deleteFolder(destDir);
        } else if (!destDir.exists()) {
            destDir.mkdir();
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
                    dir.mkdir();
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
                    f.delete();
                }
            }
        }
    }


}
