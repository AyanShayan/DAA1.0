package com.example.mst;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.example.mst.Graph.Edge;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputPath = "assign_3_input.json";
        if(args.length>0) inputPath = args[0];
        byte[] bytes = Files.readAllBytes(Paths.get(inputPath));
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(bytes);
        ArrayNode graphs = (ArrayNode) root.get("graphs");
        ArrayNode results = om.createArrayNode();

        for(JsonNode gnode : graphs){
            Graph g = om.treeToValue(gnode, Graph.class);
            ObjectNode out = om.createObjectNode();
            out.put("graph_id", g.id);
            ObjectNode stats = om.createObjectNode();
            stats.put("vertices", g.V());
            stats.put("edges", g.E());
            out.set("input_stats", stats);

            MSTPrim.Result p = MSTPrim.run(g);
            ObjectNode primNode = om.createObjectNode();
            ArrayNode primEdges = om.createArrayNode();
            for(Edge e: p.mstEdges){
                ObjectNode en = om.createObjectNode();
                en.put("from", e.from);
                en.put("to", e.to);
                en.put("weight", e.weight);
                primEdges.add(en);
            }
            primNode.set("mst_edges", primEdges);
            primNode.put("total_cost", p.totalCost);
            primNode.put("operations_count", p.operations);
            primNode.put("execution_time_ms", p.timeMs);
            out.set("prim", primNode);

            MSTKruskal.Result k = MSTKruskal.run(g);
            ObjectNode krNode = om.createObjectNode();
            ArrayNode krEdges = om.createArrayNode();
            for(Edge e: k.mstEdges){
                ObjectNode en = om.createObjectNode();
                en.put("from", e.from);
                en.put("to", e.to);
                en.put("weight", e.weight);
                krEdges.add(en);
            }
            krNode.set("mst_edges", krEdges);
            krNode.put("total_cost", k.totalCost);
            krNode.put("operations_count", k.operations);
            krNode.put("execution_time_ms", k.timeMs);
            out.set("kruskal", krNode);

            results.add(out);
        }

        ObjectNode finalOut = om.createObjectNode();
        finalOut.set("results", results);
        String outPath = "assign_3_output.json";
        om.writerWithDefaultPrettyPrinter().writeValue(new File(outPath), finalOut);
        System.out.println("Wrote results to " + outPath);
    }
}