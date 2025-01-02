//written by flypiggy

#ifndef UI_H
#define UI_H

#include <graphics.h>
#include <vector>
#include "graph.h"

// 常量定义
const int BUTTON_X1 = 500;
const int BUTTON_Y1 = 400;
const int BUTTON_X2 = 600;
const int BUTTON_Y2 = 430;

// UI 相关函数声明
void DrawGraph(bool showDesc[]);
void DrawButton();
int GetClickedNode(int mouseX, int mouseY);
bool IsButtonClicked(int mouseX, int mouseY);
void ShowInputDialog(int &start,int &end);
int GetNode(std::string str);
#endif // UI_H
