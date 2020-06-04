package net.pfiers.centernode

import org.openstreetmap.josm.gui.MainApplication
import org.openstreetmap.josm.gui.MainMenu
import org.openstreetmap.josm.plugins.Plugin
import org.openstreetmap.josm.plugins.PluginInformation

/**
 * Create a node at the center of selected elements.
 * https://github.com/ubipo/center-node
 *
 * @author Pieter Fiers (Ubipo)
 */
@Suppress("unused")
class CenterNode(info: PluginInformation?) : Plugin(info) {
    init {
        val toolsMenu = MainApplication.getMenu().toolsMenu
        MainMenu.add(toolsMenu, AddCenterNodeAction())
        MainMenu.add(toolsMenu, ReplaceWithCenterNodeAction())
    }
}
