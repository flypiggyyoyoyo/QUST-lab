//written by flypiggy

//定义图逻辑：节点结构体+边结构体+存图+存节点

#ifndef GRAPH_H
#define GRAPH_H
#include <vector>
#include<string>

struct Node {
    std::string name;
    std::string desc;
    int x,y;
};

struct Edge {//目标节点+权重
    int to;
    int weight;
};

extern int n,m;
extern std::vector<std::vector<Edge> > adj;
extern std::vector<Node> nodes;

void CreateGraph();

std::vector<int> Dijkstra(int start);

std::vector<int> GetShortestPath(int start, int end);

#endif