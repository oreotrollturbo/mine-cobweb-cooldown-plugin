package org.oreo.minecobwebcooldown.listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.oreo.minecobwebcooldown.Mine_cobweb_cooldown

class BlockPlaceListener(private val plugin: Mine_cobweb_cooldown) : Listener {

    private val mineDistance = plugin.config.getInt("mine-distance")
    private val cobwebDistance = plugin.config.getInt("cobweb-distance")

    private val mineCooldown = plugin.config.getInt("mine-cooldown")
    private val cobwebCooldown = plugin.config.getInt("cobweb-cooldown")

    @EventHandler
    fun placedBlock(e:BlockPlaceEvent){
        val block = e.block

        if (block.type == Material.WARPED_PRESSURE_PLATE){

            val player = e.player

            if (plugin.mineCooldown.contains(player)){
                e.isCancelled = true
                player.sendMessage("§cYou are in cooldown" )
                return
            }else{

                if (!checkForBlockInRadius(block,mineDistance,Material.WARPED_PRESSURE_PLATE)) {

                    plugin.mineCooldown.add(player)

                    // Schedule a task to remove the player from the cooldown after the delay
                    Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                        plugin.mineCooldown.remove(player)
                    }, 20 * mineCooldown.toLong()) //This is the delay
                }else{
                    e.isCancelled = true
                    player.sendMessage("§cA mine is too close" )
                }
            }

        }else if (block.type == Material.COBWEB){
            val player = e.player

            if (plugin.cobwebCooldown.contains(player)){
                e.isCancelled = true
                player.sendMessage("§cYou are in cooldown" )
                return
            }else{
                if (!checkForBlockInRadius(block,cobwebDistance,Material.COBWEB)) {

                    plugin.cobwebCooldown.add(player)

                    // Schedule a task to remove the player from the cooldown after the delay
                    Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                        plugin.cobwebCooldown.remove(player)
                    }, 20 * cobwebCooldown.toLong()) //This is the delay
                    return
                }else{
                    e.isCancelled = true
                    player.sendMessage("§cA cobweb is too close" )
                    return
                }
            }
        }
    }


    private fun checkForBlockInRadius(block: Block, radius: Int, targetBlockType: Material): Boolean {
        val centerLocation = block.location

        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    // Skip the center location
                    if (x == 0 && y == 0 && z == 0) {
                        continue
                    }
                    val blockCheck = centerLocation.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block
                    if (blockCheck.type == targetBlockType) {
                        return true
                    }
                }
            }
        }

        return false
    }

}