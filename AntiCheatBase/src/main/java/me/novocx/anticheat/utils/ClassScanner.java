package me.novocx.anticheat.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
 * https://github.com/funkemunky/Atlas/blob/master/AtlasParent/Atlas/src/main/java/cc/funkemunky/api/utils/ClassScanner.java
 * Modified.
 */
@RequiredArgsConstructor
public class ClassScanner {
    private final Class<? extends Annotation> anno;

    public ArrayList<Class<?>> scan(Class<? extends Plugin> main) {
        Set<String> names = scanFile(null, main);
        ArrayList<Class<?>> result = new ArrayList<>();

        names.stream().map(s -> {
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }).filter(Objects::nonNull).forEach(result::add);

        return result;
    }

    private final PathMatcher CLASS_FILE = create("glob:*.class");
    private final PathMatcher ARCHIVE = create("glob:*.{jar}");

    public Set<String> scanFile(String file, File f) {
        URL[] urls;
        try {
            urls = new URL[]{f.toURI().toURL()};
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
        return scanFile(file, urls);
    }

    public Set<String> scanFile(String file, Class<?> clazz) {
        return scanFile(file, new URL[]{clazz.getProtectionDomain().getCodeSource().getLocation()});
    }

    public Set<String> scanFile(String file, URL[] urls) {
        Set<URI> sources = new HashSet<>();
        Set<String> plugins = new HashSet<>();


        for (URL url : urls) {
            if (!url.getProtocol().equals("file")) {
                continue;
            }

            URI source;
            try {
                source = url.toURI();
            } catch (URISyntaxException e) {
                continue;
            }

            if (sources.add(source)) {
                scanPath(file, Paths.get(source), plugins);
            }
        }

        return plugins;
    }

    private void scanPath(String file, Path path, Set<String> plugins) {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                scanDirectory(file, path, plugins);
            } else {
                scanZip(file, path, plugins);
            }
        }
    }

    private void scanDirectory(String file, Path dir, final Set<String> plugins) {
        try {
            Files.walkFileTree(dir, newHashSet(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (CLASS_FILE.matches(path.getFileName())) {
                        try (InputStream in = Files.newInputStream(path)) {
                            String plugin = findPlugin(file, in);
                            if (plugin != null) {
                                plugins.add(plugin);
                            }
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }


    private void scanZip(String file, Path path, Set<String> plugins) {
        if (!ARCHIVE.matches(path.getFileName())) {
            return;
        }

        try (ZipFile zip = new ZipFile(path.toFile())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }

                try (InputStream in = zip.getInputStream(entry)) {
                    String plugin = findPlugin(file, in);
                    if (plugin != null) {
                        plugins.add(plugin);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findPlugin(String file, InputStream in) {
        try {
            ClassReader reader = new ClassReader(in);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            String className = classNode.name.replace('/', '.');
            if (classNode.visibleAnnotations != null) {
                for (Object node : classNode.visibleAnnotations) {
                    AnnotationNode annotation = (AnnotationNode) node;
                    if ((file == null && annotation.desc.equals("L" + anno.getName().replace(".", "/") + ";"))
                            || (file != null && annotation.desc.equals("L" + file.replace(".", "/") + ";"))
                    ) return className;
                }
            }
            if (classNode.superName != null && (classNode.superName.equals(file))) return className;
        } catch (Exception e) {
            //System.out.println("Failed to scan: " + in.toString());
        }
        return null;
    }

    public PathMatcher create(String pattern) {
        return FileSystems.getDefault().getPathMatcher(pattern);
    }
}
