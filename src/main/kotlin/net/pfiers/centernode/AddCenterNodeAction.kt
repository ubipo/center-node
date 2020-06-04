package net.pfiers.centernode;

import org.openstreetmap.josm.actions.JosmAction
import org.openstreetmap.josm.command.AddCommand
import org.openstreetmap.josm.command.SelectCommand
import org.openstreetmap.josm.command.SequenceCommand
import org.openstreetmap.josm.data.UndoRedoHandler
import org.openstreetmap.josm.data.osm.Node
import org.openstreetmap.josm.data.osm.OsmPrimitive
import org.openstreetmap.josm.tools.I18n.tr
import java.awt.event.ActionEvent

class AddCenterNodeAction : JosmAction(
    ACTION_NAME, ICON_NAME,
    tr(
        "Add a node at the center / average of the selected nodes (minimum two). " +
                "Selecting a way uses all of its nodes as well."
    ),
    org.openstreetmap.josm.tools.Shortcut.registerShortcut(
        "tools:addcenternode",
        tr("Tool: {0}", ACTION_NAME),
        java.awt.event.KeyEvent.VK_C, org.openstreetmap.josm.tools.Shortcut.ALT_SHIFT
    ),
    true
) {
    override fun updateEnabledState() {
        updateEnabledStateOnCurrentSelection()
    }

    override fun updateEnabledState(selection: Collection<OsmPrimitive>?) {
        isEnabled = selection != null && getAllNodes(selection).filter(Node::isUsable).size >= 2
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (!isEnabled)
            return

        val ds = layerManager.editDataSet
        val selectedNodes = getAllNodes(ds.selected)
        val centerNode = Node(center(selectedNodes))

        val commands = listOf(
            AddCommand(ds, centerNode),
            SelectCommand(ds, listOf(centerNode))
        )
        UndoRedoHandler.getInstance().add(SequenceCommand(ACTION_NAME, commands))
    }

    companion object {
        val ACTION_NAME = tr("Add Center Node")
        val ICON_NAME = "addcenternode"
    }
}
