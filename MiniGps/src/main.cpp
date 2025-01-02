// main.cpp
#include "ui.h"
#include "graph.h"
#include <iostream>
#include <vector>

int main() {
    CreateGraph();          // 创建图
    initgraph(800, 600);    // 初始化界面

    bool showDesc[n + 1] = {false}; // 控制节点描述显示
    DrawGraph(showDesc);           // 绘制初始界面

    int start = -1, end = -1; // 起点和终点
    while (true) {
        MOUSEMSG msg = GetMouseMsg();

        if (msg.uMsg == WM_LBUTTONDOWN) { // 如果是鼠标左键点击
            int clickedNode = GetClickedNode(msg.x, msg.y);
            if (clickedNode != -1) {

                showDesc[clickedNode] = !showDesc[clickedNode];
                DrawGraph(showDesc);
            } else if (IsButtonClicked(msg.x, msg.y)) {

                ShowInputDialog(start, end); // 将 start 和 end 作为引用传递

                if (start != -1 && end != -1) {

                    std::vector<int> dist = Dijkstra(start);
                    std::vector<int> path = GetShortestPath(start, end); // 获取路径

                    if (path.empty()) {
                        MessageBox(NULL, "路径不可达！", "最短路径查询", MB_OK | MB_ICONERROR);
                    } else {
                        std::string pathStr = "最短路径为：";
                        for (int node : path) {
                            pathStr += nodes[node].name + " -> ";
                        }
                        pathStr = pathStr.substr(0, pathStr.size() - 4);

                        MessageBox(NULL, pathStr.c_str(), "最短路径查询", MB_OK | MB_ICONINFORMATION);
                    }
                }
            }
        }

        // 处理中键点击退出
        if (msg.uMsg == WM_MBUTTONDOWN) {
            break;
        }
    }

    closegraph();
    return 0;
}
