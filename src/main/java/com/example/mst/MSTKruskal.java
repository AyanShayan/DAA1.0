package com.example.mst;

import java.util.*;
import com.example.mst.Graph.Edge;

public class MSTKruskal {
    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public long totalCost = 0;
        public long operations = 0;
        public long timeMs = 0;
    }

    static class DSU {
        int[] p, r;
        long ops=0;
        DSU(int n){ p=new int[n]; r=new int[n]; for(int i=0;i<n;i++) p[i]=i; }
        int find(int a){ ops++; if(p[a]==a) return a; return p[a]=find(p[a]); }
        boolean union(int a,int b){
            ops++;
            a=find(a); b=find(b);
            if(a==b) return false;
            if(r[a]<r[b]) p[a]=b; else if(r[b]<r[a]) p[b]=a; else {p[b]=a; r[a]++;}
            return true;
        }
    }

    public static Result run(Graph g){
        Result res = new Result();
        long start = System.nanoTime();
        Map<String,Integer> idx = g.nodeIndex();
        List<Edge> edges = new ArrayList<>(g.edges);
        edges.sort(Comparator.comparingInt(e -> e.weight));
        DSU dsu = new DSU(g.V());
        for(Edge e: edges){
            int u = idx.get(e.from), v = idx.get(e.to);
            res.operations++;
            if(dsu.union(u,v)){
                res.mstEdges.add(e);
                res.totalCost += e.weight;
            }
        }
        res.operations += dsu.ops;
        res.timeMs = (System.nanoTime()-start)/1_000_000;
        return res;
    }
}