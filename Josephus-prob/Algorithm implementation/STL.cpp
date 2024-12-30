#include <iostream>
#include <list>
#include <utility> // 用于 std::pair
using namespace std;

void josephus(int n, int m, const int passwords[]) {
    // 创建链表并存储编号和密码作为键值对
    list<pair<int,int>> circle;
    for (int i = 0; i < n; ++i) {
        circle.emplace_back(i + 1, passwords[i]); // (编号, 密码)
    }

    // 初始化迭代器，指向链表开头
    auto it = circle.begin();

    cout << "Order of departure: ";

    // 模拟报数和出列过程
    while (!circle.empty()) {
        // 找到第 m 个节点
        for (int i = 1; i < m; ++i) {
            ++it;
            if (it == circle.end()) {
                it = circle.begin(); // 环形结构，回到开头
            }
        }

        // 输出出列编号
        cout << it->first; // 输出编号
        if (circle.size() > 1) cout << " -> ";

        // 更新 m 值为当前节点的密码
        m = it->second;

        // 删除当前节点
        it = circle.erase(it); // 删除当前节点，并更新迭代器
        if (it == circle.end()) {
            it = circle.begin(); // 如果删除的是最后一个元素，从头开始
        }
    }

    cout << endl;
}

int main() {
    int n, m;

    // 输入人数和初始报数上限值
    cout << "Enter number of people (n): ";
    cin >> n;
    cout << "Enter the initial m value: ";
    cin >> m;

    // 输入每个人的密码
    int *passwords = new int[n];
    cout << "Enter passwords for each person: ";
    for (int i = 0; i < n; ++i) {
        cin >> passwords[i];
    }

    // 调用约瑟夫环函数
    josephus(n, m, passwords);

    // 释放动态数组内存
    delete[] passwords;

    return 0;
}
