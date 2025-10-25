package com.example.mst;

import java.util.*;
import com.example.mst.Graph.Edge;

public class MSTPrim {
    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public long totalCost = 0;
        public long operations = 0;
        public long timeMs = 0;
    }

    public static Result run(Graph g){
        Result res = new Result();
        long start = System.nanoTime();
        Map<String,Integer> idx = g.nodeIndex();
        int n = g.V();
        List<List<Edge>> adj = new ArrayList<>();
        for(int i=0;i<n;i++) adj.add(new ArrayList<>());
        for(Edge e: g.edges){
            int u = idx.get(e.from), v = idx.get(e.to);
            adj.get(u).add(new Edge(e.from, e.to, e.weight));
            adj.get(v).add(new Edge(e.to, e.from, e.weight));
        }
        boolean[] used = new boolean[n];
        if(n==0){ res.timeMs = (System.nanoTime()-start)/1_000_000; return res; }
        int startIdx = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a->a[0]));
        used[startIdx]=true;
        for(Edge e: adj.get(startIdx)){
            pq.add(new int[]{e.weight, idx.get(e.to), startIdx});
        }
        while(!pq.isEmpty()){
            int[] cur = pq.poll();
            res.operations++;
            int w = cur[0], to = cur[1], fromIdx = cur[2];
            if(used[to]) continue;
            used[to]=true;
            String from = g.nodes.get(fromIdx), toName = g.nodes.get(to);
            res.mstEdges.add(new Edge(from, toName, w));
            res.totalCost += w;
            for(Edge e: adj.get(to)){
                if(!used[idx.get(e.to)]) pq.add(new int[]{e.weight, idx.get(e.to), to});
            }
        }
        res.timeMs = (System.nanoTime()-start)/1_000_000;
        return res;
    }
}