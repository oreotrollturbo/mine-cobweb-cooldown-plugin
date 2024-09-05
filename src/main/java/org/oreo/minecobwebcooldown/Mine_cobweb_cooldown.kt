package org.oreo.minecobwebcooldown

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.oreo.minecobwebcooldown.listeners.BlockPlaceListener

class Mine_cobweb_cooldown : JavaPlugin() {

    public var mineCooldown: MutableList<Player> = mutableListOf()
    public var cobwebCooldown: MutableList<Player> = mutableListOf()

    override fun onEnable() {

        saveDefaultConfig()

        server.pluginManager.registerEvents(BlockPlaceListener(this), this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
