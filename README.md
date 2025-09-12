# MoreDensityFunctions (Check out the Wiki Page!)
 Adds more Density Functions for Terrain Creation


# Setting Up Dependencies
## For Datapack Devs:
When using a datapack, you cannot set-up a mod dependency. All you would do as a developer, is test it with a normal Minecraft instance with More Density Functions installed, and on whatever publishing platform you choose, either add a required dependency to More Density Functions, or link to it on the download page.

## For Mod Devs:
### Firstly:
Make sure your `repositories` block in your `build.gradle` file is set-up properly for CurseForge / Modrinth Maven
```
repositories {
    // ... your other repositories
    maven {
        url = "https://api.modrinth.com/maven"
    }
    maven {
        url = "https://cursemaven.com/"
    }
}
```

### Secondly:
Add it as an entry to your `dependencies` block in your `build.gradle` file

### Fabric:
```
dependencies {
    // CurseForge
    modImplementation "curse.maven:more-density-functions-1016653:6957342"

    // OR Modrinth
    modImplementation "maven.modrinth:more-density-functions:2.2.0"
}
```

### Forge:
```
dependencies {
    // CurseForge
    implementation fg.deobf("curse.maven:more-density-functions-1016653:6957342")

    // OR Modrinth
    implementation fg.deobf("maven.modrinth:more-density-functions:2.2.0")
}
```

### NeoForge:
```
dependencies {
    // CurseForge
    implementation "curse.maven:more-density-functions-1016653:6957342"

    // OR Modrinth
    implementation "maven.modrinth:more-density-functions:2.2.0"
}
```

**CurseForge Maven:**

The `modImplementation` snippet for CurseForge can be found on the download page for the specific loader (fabric, forge, etc.) and version (2.2.0 for mc1.20.1) that you need, like so:
<img width="1724" height="818" alt="image" src="https://github.com/user-attachments/assets/423ebca4-0aac-48a7-8322-ebdf51a65ced" />

**Modrinth Maven:**

For modrinth, the format is `maven.modrinth:slug:version`, so you just change `2.2.0` to whatever version of More Density Functions you are using (probably most recent), and it *should* automatically use port you fabric/forge/neoforge and mc1.20.1, mc1.21.1, etc. based on your loader environment. If this doesn't work though, someone please correct me (and use CurseForge in the meantime).

### Finally:
Add the specific version of More Density Functions as a dependency in the mod settings

**Fabric:**
Add it as an entry to your `depends` block in your `fabric.mod.json` file
```json
  // ... other fields
  "depends": {
    // ... other depends
    "moredensityfunctions": ">=2.2.0"
  }
```

**Forge/NeoForge:**
Add it a new `dependencies` block to your `mods.toml` file
```toml
[[dependencies.${mod_id}]]
modId = "moredensityfunctions"
mandatory = true
versionRange = "2.2.0"
ordering = "BEFORE" // note: order *shouldn't* matter, due to the API being in JSON, but just in case...
side = "SERVER"
```
