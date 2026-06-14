package io.github.hello09x.devtools.core.version;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author tanyaofei
 * @since 2024/7/29
 **/
@Data
@AllArgsConstructor
public class Version implements Comparable<Version> {

    private final static Map<String, Integer> STAGES = Map.of(
            "a", 1000,
            "alpha", 1000,
            "b", 2000,
            "beta", 2000,
            "rc", 3000
    );

    private int major;

    private int minor;

    private int patch;

    private int stage;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.stage = Integer.MAX_VALUE;
    }

    public static Version parse(@NotNull String s) throws InvalidVersionException {
        try {
            var components = s.split("[.-]");
            if (components.length == 3) {
                return new Version(Integer.parseInt(components[0]), Integer.parseInt(components[1]), Integer.parseInt(components[2]));
            }

            if (components.length == 5) {
                // 1, 0, 0, rc, 6
                var major = Integer.parseInt(components[0]);
                var minor = Integer.parseInt(components[1]);
                var patch = Integer.parseInt(components[2]);

                int stage = STAGES.get(components[3]) + Integer.parseInt(components[4]);
                return new Version(major, minor, patch);
            }
        } catch (Exception e) {
            throw new InvalidVersionException();
        }

        throw new InvalidVersionException();
    }

    @Override
    public int compareTo(@NotNull Version other) {
        if (this == other) {
            return 0;
        }

        if (this.major > other.major) {
            return 1;
        } else if (this.major < other.major) {
            return -1;
        }

        if (this.minor > other.major) {
            return 1;
        } else if (this.minor < other.minor) {
            return -1;
        }

        if (this.patch > other.patch) {
            return 1;
        } else if (this.patch < other.patch) {
            return -1;
        }

        return Integer.compare(this.stage, other.stage);
    }
}
