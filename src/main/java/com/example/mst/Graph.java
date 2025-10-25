package com.example.mst;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Graph {
    public static class Edge {
        public String from;
        public String to;
        public int weight;
        public Edge() {}
        public Edge(String f, String t, int w){ from=f; to=t; weight=w; }
    }

    public int id;
    public List<String> nodes;
    public List<Edge> edges;

    public int V(){ return nodes==null?0:nodes.size(); }
    public int E(){ return edges==null?0:edges.size(); }

    public Map<String, Integer> nodeIndex(){
        Map<String,Integer> m = new HashMap<>();
        for(int i=0;i<nodes.size();i++) m.put(nodes.get(i), i);
        return m;
    }
}