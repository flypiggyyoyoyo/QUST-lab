#include "ui.h"
#include "graph.h"

int main() {
    CreateGraph();          // 创建图
    initgraph(800, 600);    // 初始化界面

    bool showDesc[n + 1] = {false}; // 控制节点描述显示
    DrawGraph(showDesc);           // 绘制初始界面

    while (true) {
        MOUSEMSG msg = GetMouseMsg();
        if (msg.uMsg == WM_LBUTTONDOWN) {//WM_LBUTTONDOWN是windows的消息常量，代表鼠标左键按下
            int clickedNode = GetClickedNode(msg.x, msg.y);
            if (clickedNode != -1) {
                showDesc[clickedNode] = !showDesc[clickedNode];
                DrawGraph(showDesc);
            } else if (IsButtonClicked(msg.x, msg.y)) {
                ShowInputDialog();
            }
        }
        if (msg.uMsg == WM_MBUTTONDOWN) {
            break;
        }
    }

    closegraph();
    return 0;
}
