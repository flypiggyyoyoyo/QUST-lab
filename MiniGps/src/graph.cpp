//written by flypiggy

//实现图头文件

#include "graph.h"

int n,m;
std::vector<std::vector<Edge> > adj;
std::vector<Node> nodes;

void CreateGraph() {
    n=10;
    m=13;
    adj.resize(n+1);
    nodes.resize(n+1);

    //设计节点坐标
    nodes[1]={"北苑寝室","北苑学生休息的地方",50,100};
    nodes[2]={"北门","学校北门",150,50};
    nodes[3]={"北苑食堂","在北苑的食堂",200,150};
    nodes[4]={"学院楼","学院老师领导办公的地方",300,200};
    nodes[5]={"东大门","学校正门",400,300};
    nodes[6]={"明德楼","上课的地方",250,350};
    nodes[7]={"弘毅楼","上课的地方",150,350};
    nodes[8]={"操场","运动的地方",200,500};
    nodes[9]={"南苑食堂","在南苑的食堂",50,400};
    nodes[10]={"南苑寝室","南苑学生休息的地方",100,500};

    //建边
    adj[1].push_back({2,1});
    adj[2].push_back({1,1});

    adj[1].push_back({3,2});
    adj[3].push_back({1,2});

    adj[2].push_back({3,2});
    adj[3].push_back({2,2});

    adj[3].push_back({4,3});
    adj[4].push_back({3,3});

    adj[3].push_back({6,4});
    adj[6].push_back({3,4});

    adj[4].push_back({6,2});
    adj[6].push_back({4,2});

    adj[4].push_back({5,3});
    adj[5].push_back({4,3});

    adj[5].push_back({6,1});
    adj[6].push_back({5,1});

    adj[6].push_back({7,3});
    adj[7].push_back({6,3});

    adj[6].push_back({8,6});
    adj[8].push_back({6,6});

    adj[7].push_back({8,1});
    adj[8].push_back({7,1});

    adj[7].push_back({9,1});
    adj[9].push_back({7,1});

    adj[8].push_back({9,2});
    adj[9].push_back({8,2});

    adj[9].push_back({10,2});
    adj[10].push_back({9,2});

    adj[10].push_back({8,1});
    adj[8].push_back({10,1});
}
