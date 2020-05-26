#include <stdio.h>
#include <vector>
#include <utility>
#include <iostream>
#include <queue>
using namespace std;
void mesaj()
{
	printf("\n0 : exit\n1 : drum de cost minim - Bellman-Ford\n2 : drum de cost minim - Dijkstra\n3 : Johnson\n>>");
}
void afisDrum(int s, int i, int p[])
{
	if (i == s)
		printf("%d", s);
	else
	{
		afisDrum(s, p[i], p);
		printf("->%d", i);
	}
}
void adjust(int u, int v, int w, int d[], int p[])
{
	if (d[v] > d[u] + w)
	{
		p[v] = u;
		d[v] = d[u] + w;
	}
}
void dijkstra(int d[], int p[], int n, int s, vector< pair<int, int>> adj[])
{	//initializare
	for (int i = 1; i <= n; i++)
	{
		d[i] = 9999;
		p[i] = 0;
	}
	d[s] = 0;

	//initializare
	int q[100] = { 0 };
	int viz;
	do {
		viz = 0;
		int min = 999999;
		int u = 0;
		for (int i = 1; i <= n; i++)
			if (q[i] == 0 && min > d[i])
				min = d[i], u = i;
		if (u != 0)
		{
			q[u] = 1;
            viz  = 1;
			for (auto i = adj[u].begin(); i != adj[u].end(); i++)
				adjust(u, i->first, i->second, d, p);
		}
	} while (viz);
	
}
void readDijkstra()
{
	FILE* f = fopen("Dijkstra.txt", "r");
	int s, n = 0;
	cout << "Source node:";
	cin >> s;
	fscanf(f, "%d", &n);
	vector < pair<int, int> > adj[100];
	while (!feof(f))
	{
		int u, v, w;
		fscanf(f, "%d %d %d", &u, &v, &w);
		adj[u].push_back(make_pair(v, w));
	}
	int d[100], p[100];
	dijkstra(d, p, n, s,adj);
	//afisare
	for (int i = 1; i <= n; i++)
		if (i != s)
		{
			if (d[i] == 9999)
				printf("Nu se poate ajunge de la %d la %d\n", s, i);
			else {
				printf("Distanta de la %d la %d : %d Drum:", s, i, d[i]);
				afisDrum(s, i, p);
				printf("\n");
			}
		}
	//afisare
}
bool bellManFord(int d[], int p[], int n, int s,vector< pair<int,int>> adj[])
{
	//initializare
	for (int i = 1; i <= n; i++)
	{
		d[i] = 9999;
		p[i] = 0;
	}
	d[s] = 0;
	for (int i = 1; i < n; i++)
		for (int j = 1; j <= n; j++)
			for (auto k = adj[j].begin(); k != adj[j].end(); k++)
				adjust(j, k->first, k->second, d, p);

	for (int i = 1; i <= n; i++)
	{
		for (auto k = adj[i].begin(); k != adj[i].end(); k++)
			if (d[k->first] > d[i] + k->second)
				return false;
	}
	return true;
}
void readBellManFord()
{
	FILE* f = fopen("Bellman.txt", "r");
	int s, n=0;
	cout << "Source node:";
	cin >> s;
	fscanf(f, "%d", &n);
	vector < pair<int, int> > adj[100];
	while (!feof(f))
	{
		int u, v, w;
		fscanf(f, "%d %d %d", &u, &v, &w);
		adj[u].push_back(make_pair(v, w));
	}
	int d[100], p[100];
	bool bell = bellManFord(d, p, n, s,adj);
	char ss[100];
	strcpy(ss, "ssabcdefgh");
	for (int i = 1; i <= n; i++)
		cout << ss[i]<<" : "<<d[i] << " " << ss[p[i]] << endl;

}
void readJohnson()
{
	FILE* f = fopen("Johnson.txt", "r");
	int n = 0;
	fscanf(f, "%d", &n);
	vector < pair<int, int> > adj[100],adj1[100];
	while (!feof(f))
	{
		int u, v, w;
		fscanf(f, "%d %d %d", &u, &v, &w);
		adj[u].push_back(make_pair(v, w));
		adj1[u].push_back(make_pair(v, w));
	}
	int d[100], p[100];
	int n1 = n + 1;
	for (int i = 1; i <= n; i++)
		adj1[n1].push_back(make_pair(i, 0));
	if (bellManFord(d, p, n1, n1, adj1) == false)
		cout << "Ciclu negativ!\n";
	else
	{
		cout << "test\n";
		for (int i = 1; i <= n1; i++)
			cout << d[i] << " " << p[i] << endl;
		cout << "endTest\n";
		for (int i = 1; i <= n; i++)
		{
			for (auto j = adj1[i].begin(); j != adj1[i].end(); j++)
				j->second = j->second + d[i] - d[j->first];
		}
		int dd[100][100] = { 0 };
		int pp[100][100] = { 0 };
		for (int i = 1; i <= n; i++)
		{
			cout << "i=" << i << " ";
			for (auto j = adj1[i].begin(); j != adj1[i].end(); j++)
				cout << j->first << " " << j-> second << " ";
			cout << endl;
				
		}
		for (int i = 1; i <= n; i++)
		{
			dijkstra(dd[i], pp[i], n, i, adj1);
			for (int j = 1; j <= n; j++)
				if(dd[i][j]!=9999)
					dd[i][j] = dd[i][j] + d[j] - d[i];
		}
		//afisare
		for (int j = 1; j <= n; j++)
		{
			cout << "Distantele de la nodul " << j << ":" << endl;
			for (int i = 1; i <= n; i++)
				if (i != j)
				{
					if (dd[j][i] == 9999)
						printf("Nu se poate ajunge de la %d la %d\n", j, i);
					else {
						printf("Distanta de la %d la %d : %d Drum:", j, i, dd[j][i]);
						afisDrum(j, i, pp[j]);
						printf("\n");
					}
				}
			cout << endl;
		}
	}
}
int main()
{
	int x = 0;
	while (1)
	{
		mesaj();
		scanf("%d", &x);
		printf("\n");
		switch (x)
		{
		case 0: return 0;
		case 1:
			readBellManFord();
			break;
		case 2:
			readDijkstra();
			break;
		case 3:
			readJohnson();
			break;
		}
	}
	return 0;
}