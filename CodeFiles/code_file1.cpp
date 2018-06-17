 // codechefis deda s
 // i have seen in the future and you are not in it :))
 // pain trains character
 //MOGITYANT KOMPIUTERI ROMLITAC ATESTIREBTT YLEEBO DA BOZISHVILEBO
// idzite na xui vseee :))
// I LOVE SET <3 go to fuck you :)
 #define _CRT_SECURE_NO_DEPRECATE
 
#include <iostream>
#include <math.h>
#include<set>
#include <iomanip>
#include <algorithm>
#include <vector>
#include <map>
#include<stdio.h>
#include <queue>
#include <string>
#define cn(x) scanf("%d",&x);
#define INF 2143647
#define idinaxui return 0
#define PI 3.14159265358
#define mod9 1000000009
#define mod7 %1000000007
#define caut cout
#define md 1000000007
#define f first
#define s second
#define rtn return 0
#define time clock()/(double)CLOCKS_PER_SEC
#define Freopen freopen("input.txt","r",stdin); freopen("output.txt","w",stdout)
#define maxn 500000
#define ct(n) printf("%i64d ",n);
#define ctt(n) printf("%i64d\n",n);
#define cantinue continue
#define mk make_pair
# define ll long long
# define pp pair <int,int>
using namespace std;
int n,m;
int l[1000001],r[100001],p[100001],d[100001],c[1000001];
vector <int> lp[100001];
vector <int> g[1000001];
 int timer=0;
 struct o {int x,y;};o t[1000001];
 void dfs(int v,int p){
      ++timer;
       l[v]=timer;
       for (int k=0;k<g[v].size();++k)
         if (g[v][k]!=p)
          dfs(g[v][k],v);
          r[v]=timer;
         return;
         }      
      void upd (int v,int l,int r,int l1,int r1,int d){
            if (l>r1 || r<l1) return;
            if (l==l1 && r==r1){
                      t[v].x+=d;
                      if (t[v].x!=0)
                      t[v].y=r-l+1;
                      else t[v].y=t[2*v].x+t[2*v+1].y;
                      return;
                      }
            int mid=(l+r)/2;
             upd(2*v,l,mid,l1,min(r1,mid),d);
             upd(v*2+1,mid+1,r,max(mid+1,l1),r1,d);
             if (t[v].x!=0){
                            t[v].y=r-l+1;
                            }
                            else 
                            t[v].y=t[2*v].y+t[2*v+1].y;
             return;           
           }
      void DFS(int v,int u){
           for (int k=0;k<lp[v].size();++k)
             {
                    int t=lp[v][k];
                  upd(1,1,n,l[p[t]],r[p[t]],1); 
                  upd(1,1,n,l[d[t]],r[d[t]],1);
                    }
                   c[v]=max(t[1].y,1);
             for (int k=0;k<g[v].size();++k)
             if (u!=g[v][k])   
               DFS(g[v][k],v);
          for (int k=0;k<lp[v].size();++k)
             {
                    int t=lp[v][k];
                  upd(1,1,n,l[p[t]],r[p[t]],-1); 
                  upd(1,1,n,l[d[t]],r[d[t]],-1);
                    }   
                    return;   
      }
int main(){ 
//Freopen;
cin>>n>>m;
for (int k=1;k<=n-1;++k){
    int x,y;
    cin>>x>>y;
    g[x].push_back(y); 
    g[y].push_back(x);  
}
dfs(1,0);
for (int k=1;k<=m;++k){
      int x,y;
      cin>>x>>y;p[k]=x;d[k]=y;
      lp[x].push_back(k);
      lp[y].push_back(k);
}
DFS(1,0);
for (int k=1;k<=n;++k){
  cout<<c[k]-1<<' ';
}
 return 0;
}

