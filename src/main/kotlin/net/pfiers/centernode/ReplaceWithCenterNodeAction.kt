package net.pfiers.centernode

import org.openstreetmap.josm.actions.JosmAction
import org.openstreetmap.josm.command.DeleteCommand
import org.openstreetmap.josm.command.MoveCommand
import org.openstreetmap.josm.command.SelectCommand
import org.openstreetmap.josm.command.SequenceCommand
import org.openstreetmap.josm.data.UndoRedoHandler
import org.openstreetmap.josm.data.osm.Node
import org.openstreetmap.josm.data.osm.OsmPrimitive
import org.openstreetmap.josm.tools.I18n
import java.awt.event.ActionEvent

class ReplaceWithCenterNodeAction : JosmAction(
        ACTION_NAME, ICON_NAME,
        I18n.tr("Replace the selected nodes (minimum two) with a node at the center / average. Selecting a way uses all of its nodes."),
        org.openstreetmap.josm.tools.Shortcut.registerShortcut(
                "tools:replacewithcenternode",
                I18n.tr("Tool: {0}", ACTION_NAME),
                java.awt.event.KeyEvent.VK_C, org.openstreetmap.josm.tools.Shortcut.ALT_CTRL_SHIFT
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
        val center = center(selectedNodes)
        val first = selectedNodes.first()
        val others = selectedNodes.toList().subList(1, selectedNodes.size)

        val commands = listOf(
                MoveCommand(first, center),
                DeleteCommand(ds, others),
                SelectCommand(ds, listOf(first))
        )
        UndoRedoHandler.getInstance().add(SequenceCommand(ACTION_NAME, commands))
    }

    companion object {
        val ACTION_NAME = I18n.tr("Replace with Center Node")
        val ICON_NAME = "replacewithcenternode"
    }
}
