#include<iostream>
#define Past_Dream super_FW
using namespace std;

struct ListNode{
    int value;
    int num;
    ListNode* pre;
    ListNode* next;
    ListNode(int val, int number): value(val),num(number),pre(nullptr),next(nullptr){}
};

struct de_io{
    int m;
    ListNode* baseNode;
    de_io(int m, ListNode* baseNode): m(m), baseNode(baseNode){}
};

class LinkedList {
public:
    ListNode* head;
    ListNode* tail;
    int size;
    LinkedList() : head(nullptr), tail(nullptr), size(0){}
    void insertNode(int value, int number);
    de_io* deleteNode(de_io* baseNode);
};

void LinkedList::insertNode(int value,int number){
    ListNode* newNode = new ListNode(value,number);
    if(head == nullptr){
        head = newNode;
        tail = newNode;
        newNode->pre = newNode;
        newNode->next = newNode;
    }
    else{
        tail->next = newNode;
        newNode->pre = tail;
        tail = newNode;
        newNode->next = head;
        head->pre = newNode;
    }
    size ++ ;
    return;
}

de_io* LinkedList::deleteNode(de_io* input_data){
    int m = input_data->m;
    ListNode* baseNode = input_data->baseNode;
    if(m == 0){
        baseNode->pre->next = baseNode->next;
        baseNode->next->pre = baseNode->pre;
        cout << baseNode->num <<endl;
        size -- ;
        de_io* output_data = new de_io(baseNode->value, baseNode->next);
        if(head == baseNode) head = baseNode->next;
        else if(tail == baseNode) tail = baseNode->pre;
        if(size == 0){
            head = nullptr;
            tail = nullptr;
            return nullptr;
        }
        delete baseNode;
        return output_data;
    }
    input_data->m = m - 1;
    input_data->baseNode = baseNode->next;
    return deleteNode(input_data);
}

int main(){
    int m , n;
    cout << "Input m and n:" << endl;
    cin >> m >> n;
    LinkedList list;
    cout << "Input the value of each node:" << endl;
    for(int i = 1; i <= n; i++){
        int value;
        cin >> value;
        list.insertNode(value,i);
    }
    de_io* input_data = new de_io(m, list.head);
    while(list.size > 0){
        input_data->m = (input_data->m-1)%(list.size);
        input_data = list.deleteNode(input_data);
    }
    return 0;
}