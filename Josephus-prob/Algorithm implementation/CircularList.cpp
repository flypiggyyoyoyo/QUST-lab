#include <iostream>
using namespace std;

// 定义链表节点
struct Node
{
    int id;       // 编号
    int password; // 密码
    Node *next;   // 指向下一个节点

    Node(int id, int password) : id(id), password(password), next(nullptr) {}
};

// 创建单向循环链表
Node *createCircularList(int n, int passwords[])
{
    Node *head = new Node(1, passwords[0]); // 创建头节点
    Node *prev = head;

    for (int i = 2; i <= n; i++)
    {
        Node *newNode = new Node(i, passwords[i - 1]);
        prev->next = newNode;
        prev = newNode;
    }
    prev->next = head; // 形成循环链表
    return head;
}

void simulate(int n,int password[],int m){

}

int main()
{
    int n;
    cout << "plz input n: ";
    cin >> n;

    int m;
    cout << "plz input m:";
    cin >> m;

    int *passwords = new int[n];
    cout << "plz input passw: ";
    for (int i = 0; i < n; i++)
    {
        cin >> passwords[i];
    }

    simulate(n, passwords,m);

    delete[] passwords; // 释放动态数组
    return 0;
}
