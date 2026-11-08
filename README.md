# Minecraft Plugin: God Carrot

God Carrot is a Minecraft plugin for Paper 1.21.11 that adds a craftable custom item with powerful inventory effects, extra health, and custom enchantments. The plugin uses a shaped crafting recipe and grants Fire Resistance, Regeneration, Haste, Speed, Jump Boost, Saturation, and five extra hearts while the God Carrot is in a player's inventory.

## Install

1. Build the project with Java 21 and Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `plugin.jar` file from the `target` directory into your Paper server's `plugins` directory.

3. Start or restart the Paper 1.21.11 server.

4. Confirm that `GodCarrot` appears as enabled in the server console.

## Use

Craft the God Carrot in a normal crafting table using this pattern:

```text
WGW
ACA
WBW
```

Recipe ingredients:

- `W`: Wither Skeleton Skull
- `G`: Heavy Core
- `A`: Enchanted Golden Apple
- `C`: Carrot
- `B`: Breeze Rod

The crafted item is a custom Golden Carrot named `God Carrot`. Its enchantments are:

- Breach I
- Wind Burst III
- Sharpness V
- Lunge III
- Efficiency V

Keep the God Carrot anywhere in your inventory to receive:

- Fire Resistance I
- Regeneration I
- Haste I
- Speed I
- Jump Boost I
- Saturation I
- 5 extra hearts

The effects are maintained automatically while the item remains in the inventory. Removing it removes the extra health bonus and allows the active effects to expire.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/98fd47bc-44c8-4769-8b42-9c862c012393?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=98fd47bc-44c8-4769-8b42-9c862c012393
