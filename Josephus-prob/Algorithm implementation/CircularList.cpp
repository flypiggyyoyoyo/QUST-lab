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
    //创建节点
    Node *head = new Node(1, passwords[0]); // 创建头节点
    Node *prev = head;//尾节点&中间操作节点

    //循环建表
    for (int i = 2; i <= n; i++)
    {
        Node *newNode = new Node(i, passwords[i - 1]);
        prev->next = newNode;
        prev = newNode;
    }

    prev->next = head; // 形成循环链表
    return head;//返回头
}

void simulate(int n,int passwords[],int m){

    Node *head = createCircularList(n, passwords); // 创建循环链表，得到头节点

    Node *prev = nullptr;                          // 用于追踪删除节点的前一个节点 

    cout << "Order of departure: ";

    //循环删除
    while (n > 0)
    {
        // 找到第 m 个节点
        for (int i = 1; i < m; i++)
        {
            prev = head;
            head = head->next;//从头开始迭代
        }

        // 输出出列编号
        cout << head->id;
        if (n > 1)
            cout << " -> ";

        // 更新 m 值为当前节点的密码
        m = head->password;

        // 删除当前节点
        prev->next = head->next;
        Node *temp = head;
        head = head->next; // 移动到下一个节点
        delete temp;//安全释放内存

        n--; // 剩余人数减少
    }

    cout << endl;
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
