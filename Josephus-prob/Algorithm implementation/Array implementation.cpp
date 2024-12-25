#include<stdio.h>

int main(){
    int m , n , temp , p = 1;
    scanf("%d %d",&m,&n);
    int num[n+1][3];
    for(int i = 1 ; i <= n ; i++){
        scanf("%d", &num[i][0]);
        num[i][1] = i;
        num[i][2] = i + 1;
    }
    num[n][2] = 1;
    while(n != 0){
        p = (p + m - 1) % n ;
        if(p == 0) p = n ;
        m = num[p][0];
        printf("%d\n", num[p][1]);

        for(int i = p ; i <= n - 1 ; i++){
            num[i][0] = num[i + 1][0];
            num[i][1] = num[i + 1][1];
        }
        
        n -- ;
    }
    return 0;
}
