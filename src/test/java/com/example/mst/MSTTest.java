package com.example.mst;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import com.fasterxml.jackson.databind.*;

public class MSTTest {
    @Test
    public void testPrimKruskalEqualCost() throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(Files.readAllBytes(Paths.get("assign_3_input.json")));
        for(JsonNode gnode : root.get("graphs")){
            Graph g = om.treeToValue(gnode, Graph.class);
            MSTPrim.Result p = MSTPrim.run(g);
            MSTKruskal.Result k = MSTKruskal.run(g);
            assertEquals(p.totalCost, k.totalCost, "Prim and Kruskal should have same total cost for graph " + g.id);
            assertTrue(p.mstEdges.size() == g.V()-1 || g.V()==0, "Prim MST edge count");
            assertTrue(k.mstEdges.size() == g.V()-1 || g.V()==0, "Kruskal MST edge count");
            assertTrue(p.timeMs >= 0);
            assertTrue(k.timeMs >= 0);
            assertTrue(p.operations >= 0);
            assertTrue(k.operations >= 0);
        }
    }
}