package net.pfiers.centernode

import org.openstreetmap.josm.data.coor.LatLon
import org.openstreetmap.josm.data.osm.Node
import org.openstreetmap.josm.data.osm.OsmPrimitive
import org.openstreetmap.josm.data.osm.Way

fun getAllNodes(primitives: Collection<OsmPrimitive>): Set<Node> {
    val nodes = primitives.filterIsInstance<Node>()
    val wayNodes = primitives.filterIsInstance<Way>().flatMap(Way::getNodes)
    return wayNodes.union(nodes)
}

fun center(nodes: Set<Node>): LatLon {
    return LatLon(
            nodes.map(Node::lat).average(),
            nodes.map(Node::lon).average()
    )
}

/**
 * Public copy of Way::removeDouble
 *
 * @see Way.removeDouble
 */
fun removeDouble(nodes: List<Node>): List<Node>? {
    val copy = nodes.toMutableList()
    var last: Node? = null
    var count = copy.size
    var i = 0
    while (i < count && count > 2) {
        val n = copy[i]
        if (last === n) {
            copy.removeAt(i)
            --count
        } else {
            last = n
            ++i
        }
    }
    return copy
}
