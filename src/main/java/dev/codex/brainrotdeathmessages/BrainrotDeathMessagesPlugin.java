package dev.codex.brainrotdeathmessages;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public final class BrainrotDeathMessagesPlugin extends JavaPlugin implements Listener {

  private boolean messagesEnabled = true;

  // ── General / fallback ──────────────────────────────────────────────
  private static final List<String> GENERAL_MESSAGES = List.of(
      "<player> forgot to lock in.",
      "<player> got packed up respectfully.",
      "<player> disconnected from the sigma network.",
      "<player> lost all aura instantly.",
      "<player> became another NPC casualty.",
      "<player> got caught lacking in 4K.",
      "<player> thought they had plot armor.",
      "<player> got deleted from the timeline.",
      "<player> rage quit life itself.",
      "<player> failed the vibe check permanently."
  );

  // ── Fall ─────────────────────────────────────────────────────────────
  private static final List<String> FALL_MESSAGES = List.of(
      "<player> got mogged by gravity.",
      "<player> tested Minecraft physics personally.",
      "<player> took the L at terminal velocity.",
      "<player> forgot feather falling existed.",
      "<player> hit the ground like a dropped Nokia.",
      "<player> fell harder than their GPA.",
      "<player> became a pancake instantly.",
      "<player> missed the water bucket clutch.",
      "<player> found out gravity is pay to win.",
      "<player> folded on impact."
  );

  // ── Fire / lava ──────────────────────────────────────────────────────
  private static final List<String> FIRE_LAVA_MESSAGES = List.of(
      "<player> couldn't out-rizz lava.",
      "<player> became a cooked chicken.",
      "<player> got turned into a campfire snack.",
      "<player> discovered lava hurts.",
      "<player> got extra crispy.",
      "<player> tried swimming in orange Gatorade.",
      "<player> got BBQ'd instantly.",
      "<player> lost against hot Cheeto water.",
      "<player> became ash particles.",
      "<player> got flame broiled by Minecraft."
  );

  // ── Generic mob ──────────────────────────────────────────────────────
  private static final List<String> MOB_MESSAGES = List.of(
      "<player> got low diffed by a mob.",
      "<player> got comboed into another dimension.",
      "<player> got sent back to the lobby.",
      "<player> lost the 1v1 badly.",
      "<player> got absolutely farmed.",
      "<player> was not built for survival mode.",
      "<player> got folded by hostile AI.",
      "<player> got violated by local wildlife.",
      "<player> lost against pixels.",
      "<player> became free XP."
  );

  // ── Zombie variants ──────────────────────────────────────────────────
  private static final List<String> ZOMBIE_MESSAGES = List.of(
      "<player> got jumped by a goofy zombie.",
      "<player> got bit like a cheap horror movie.",
      "<player> couldn't survive the zombie apocalypse.",
      "<player> got folded by rotten meat.",
      "<player> got humbled by a walking corpse."
  );

  // ── Skeleton variants ────────────────────────────────────────────────
  private static final List<String> SKELETON_MESSAGES = List.of(
      "<player> got ratioed by a skeleton.",
      "<player> got sniped from another postcode.",
      "<player> got turned into a pin cushion.",
      "<player> met Minecraft Hawkeye.",
      "<player> got aimbotted by bones."
  );

  // ── Creeper ──────────────────────────────────────────────────────────
  private static final List<String> CREEPER_MESSAGES = List.of(
      "<player> got fanum taxed by a creeper.",
      "<player> experienced sudden unplanned disassembly.",
      "<player> got blown into 4K particles.",
      "<player> trusted the hiss sound.",
      "<player> became a crater."
  );

  // ── Spider ───────────────────────────────────────────────────────────
  private static final List<String> SPIDER_MESSAGES = List.of(
      "<player> got boxed by a spider.",
      "<player> lost to oversized shampoo residue.",
      "<player> got folded by eight legs.",
      "<player> underestimated nightmare fuel.",
      "<player> got web checked."
  );

  // ── Enderman ─────────────────────────────────────────────────────────
  private static final List<String> ENDERMAN_MESSAGES = List.of(
      "<player> looked directly at the opp.",
      "<player> lost the staring contest.",
      "<player> got teleported into a funeral.",
      "<player> learned eye contact is dangerous.",
      "<player> got folded by a tall shadow creature."
  );

  // ── Void ─────────────────────────────────────────────────────────────
  private static final List<String> VOID_MESSAGES = List.of(
      "<player> fell into the WiFi void.",
      "<player> exited reality permanently.",
      "<player> touched the forbidden darkness.",
      "<player> got erased from the server files.",
      "<player> noclipped out of existence."
  );

  // ── Drowning ─────────────────────────────────────────────────────────
  private static final List<String> DROWNING_MESSAGES = List.of(
      "<player> forgot how breathing works.",
      "<player> lost against water physics.",
      "<player> ran out of oxygen subscriptions.",
      "<player> became fish food.",
      "<player> discovered lungs are important."
  );

  // ── PvP ──────────────────────────────────────────────────────────────
  private static final List<String> PVP_MESSAGES = List.of(
      "<player> got skill issued by another player.",
      "<player> got clapped in PvP.",
      "<player> got sent to spectator mode.",
      "<player> lost the ranked match.",
      "<player> got diffed beyond recovery."
  );

  /* ================================================================== */

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    getLogger().info("Brainrot Death Messages enabled — lock in!");
  }

  @Override
  public void onDisable() {
    getLogger().info("Brainrot Death Messages disabled.");
  }

  /* ─── Death listener ─────────────────────────────────────────────── */

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerDeath(PlayerDeathEvent event) {
    if (!messagesEnabled) {
      return;
    }
    Player player = event.getEntity();
    String msg = formatMessage(selectTemplate(player), player);
    // Use Adventure Component API (modern Paper)
    event.deathMessage(Component.text(msg));
  }

  /* ─── Template selection ─────────────────────────────────────────── */

  private String selectTemplate(Player player) {
    EntityDamageEvent lastDamage = player.getLastDamageCause();
    if (lastDamage == null) {
      return random(GENERAL_MESSAGES);
    }

    DamageCause cause = lastDamage.getCause();

    // Environmental deaths
    if (cause == DamageCause.VOID)     return random(VOID_MESSAGES);
    if (cause == DamageCause.DROWNING) return random(DROWNING_MESSAGES);
    if (cause == DamageCause.FALL)     return random(FALL_MESSAGES);
    if (isFireOrLava(cause))           return random(FIRE_LAVA_MESSAGES);

    // PvP — player killer shortcut
    if (player.getKiller() != null) {
      return random(PVP_MESSAGES);
    }

    // Entity damage
    if (lastDamage instanceof EntityDamageByEntityEvent entityEvent) {
      Entity damager = unwrapProjectile(entityEvent.getDamager());
      if (damager instanceof Player) {
        return random(PVP_MESSAGES);
      }
      if (damager instanceof LivingEntity) {
        return selectMobMessage(damager.getType());
      }
    }

    return random(GENERAL_MESSAGES);
  }

  private String selectMobMessage(EntityType type) {
    return switch (type) {
      case ZOMBIE, HUSK, DROWNED, ZOMBIE_VILLAGER -> random(ZOMBIE_MESSAGES);
      case SKELETON, STRAY, WITHER_SKELETON       -> random(SKELETON_MESSAGES);
      case CREEPER                                 -> random(CREEPER_MESSAGES);
      case SPIDER, CAVE_SPIDER                     -> random(SPIDER_MESSAGES);
      case ENDERMAN                                -> random(ENDERMAN_MESSAGES);
      default                                      -> random(MOB_MESSAGES);
    };
  }

  /* ─── Helpers ────────────────────────────────────────────────────── */

  private static boolean isFireOrLava(DamageCause cause) {
    return cause == DamageCause.FIRE
        || cause == DamageCause.FIRE_TICK
        || cause == DamageCause.LAVA
        || cause == DamageCause.HOT_FLOOR;
  }

  private static Entity unwrapProjectile(Entity damager) {
    if (damager instanceof Projectile projectile) {
      ProjectileSource shooter = projectile.getShooter();
      if (shooter instanceof Entity entity) {
        return entity;
      }
    }
    return damager;
  }

  private static String formatMessage(String template, Player player) {
    return template.replace("<player>", player.getName());
  }

  private static String random(List<String> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }

  /* ─── /brainrot command ──────────────────────────────────────────── */

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("brainrot")) {
      return false;
    }
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("start")) {
        messagesEnabled = true;
        sender.sendMessage(Component.text("Brainrot death messages ")
            .color(NamedTextColor.GREEN)
            .append(Component.text("enabled").decorate(TextDecoration.BOLD))
            .append(Component.text(".").color(NamedTextColor.GREEN)));
        return true;
      }
      if (args[0].equalsIgnoreCase("stop")) {
        messagesEnabled = false;
        sender.sendMessage(Component.text("Brainrot death messages ")
            .color(NamedTextColor.RED)
            .append(Component.text("disabled").decorate(TextDecoration.BOLD))
            .append(Component.text(".").color(NamedTextColor.RED)));
        return true;
      }
    }
    sender.sendMessage(Component.text("Usage: /brainrot <start|stop>").color(NamedTextColor.YELLOW));
    return true;
  }
}
