#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <cmath>
#include <stack>
#include <fstream>
#include <vector>
using namespace std;
vector<int> colorareGraf(int n, vector<vector<int>>& adj) {
	vector<int> colorat;
	for (int i = 0; i <= n; i++)
		colorat.push_back(0);
	for (int i = 1; i <= n; i++)
		if (!colorat[i])
		{
			vector<bool> culori;
			for (int i = 0; i <= n; i++)
				culori.push_back(false);
			for (int j : adj[i])
				culori[colorat[j]] = true;
			for(int j=1;j<=n;j++)
				if (!culori[j])
				{
					colorat[i] = j;
					break;
				}
		}
	return colorat;
}
void readColorareGraf() {
	ifstream fin("Colorare.txt");
	vector<vector<int>> adj;
	int n;
	fin >> n;
	adj.resize(n+1);
	while (!fin.eof()) {
		int a, b;
		fin >> a >> b;
		adj[a].push_back(b);
		adj[b].push_back(a);
	}
	vector<int> colored;
	colored = colorareGraf(n, adj);
	cout << "Nod  Culoare\n";
	for (int i = 1; i <= n; i++)
		cout << i << "     " << colored.at(i) << "\n";
}
vector<vector<pair<int, int>>> formeazaRezidual(int n, vector<vector<pair<int, pair<int, int>>>>& adj) {
	vector<vector<pair<int, int>>> rez;
	rez.resize(n + 1);
	for (int i = 0; i <= n; i++) {
		for (auto j : adj[i]) {
			if ((j.second).first != (j.second).second)
				rez[i].push_back(make_pair(j.first, ((j.second).second - (j.second).first)));
			if ((j.second).first != 0)
				rez[j.first].push_back(make_pair(i,(j.second).first));
		}
	}
	return rez;
}
void dfs(int s, int n, vector<vector<pair<int,int>>>& rez,vector<bool>& marked, vector<int>& pi) {
		
	marked[s] = true;
	if (s == n)
		return;
	for (auto i : rez[s]) {
		if (!marked[i.first]) {
			pi[i.first] = s;
			dfs(i.first, n, rez, marked, pi);
		}
	}
}
int find_min(int n, vector<int>& pi, vector<vector<pair<int, int>>>& rez) {
	int crt = n;
	int min = 999999;
	while (crt != 0) {
		int p = pi[crt];
		bool da = false;
		for(auto i : rez[p])
			if (i.first == crt)
			{
				da = true;
				if (min > i.second)
					min = i.second;
				break;
			}
		if (!da)
			for (auto i : rez[crt])
				if (i.first == p && min > i.second)
				{
					min = i.second;
					break;
				}
		crt = p;
	}
	return min;
}
void cresteFlux(int min, int n, vector<vector<pair<int, pair<int, int>>>>& adj,vector<int> pi) {
	int crt = n;
	while (crt != 0) {
		int p = pi[crt];
		bool da = false;
		for (auto& i : adj[p])
			if (i.first == crt) {
				da = true;
				(i.second).first += min;
				break;
			}
		if(!da)
			for(auto& i :adj[crt])
				if (i.first == p)
				{
					i.second.first -= min;
					break;
				}
		crt = p;
	}
}
void fordFulkerson(int n, vector<vector<pair<int, pair<int, int>>>>& adj) {

	while (true) {
		vector<vector<pair<int,int>>> rez = formeazaRezidual(n,adj);
		vector<bool> marked;
		vector<int> pi;
		for (int i = 0; i <= n; i++)
			marked.push_back(false),pi.push_back(-1);
		dfs(0, n, rez, marked, pi);
		if (pi[n] == -1)
			break;
		int min = find_min(n,pi,rez);
		cresteFlux(min, n, adj,pi);
	}
}
void readFordFulkerson() {
	ifstream fin("Ford.txt");
	vector< vector< pair<int,pair<int,int> > > >adj;
	int n;
	fin >> n;
	adj.resize(n + 1);
	while (!fin.eof()) {
		int x, y, z;
		fin >> x >> y >> z;
		adj[x].push_back(make_pair(y, make_pair(0, z)));
	}
	fordFulkerson(n, adj);
	int max = 0;
	cout << "Muchiile din graful cu flux maxim sunt:\n";
	for (int i=0;i<n;i++)
		for (auto j : adj[i]) {
			cout << i << " " << j.first << " " << (j.second).first << "/" << (j.second).second << "\n";
			if (j.first == n)
				max += j.second.first;
		}
	cout << "\n";
	cout << "Fluxul maxim este: " << max << endl;
}
int min(int a1, int a2)
{
	if (a1 < a2) return a1;
	return a2;
}
struct Edge
{
	// To store current flow and capacity of edge 
	int flow, capacity;

	// An edge u--->v has start vertex as u and end 
	// vertex as v. 
	int u, v;

	Edge(int flow, int capacity, int u, int v)
	{
		this->flow = flow;
		this->capacity = capacity;
		this->u = u;
		this->v = v;
	}
};

// Represent a Vertex 
struct Vertex
{
	int h, e_flow;

	Vertex(int h, int e_flow)
	{
		this->h = h;
		this->e_flow = e_flow;
	}
};

// To represent a flow network 
class Graph
{
	int V;    // No. of vertices 
	vector<Vertex> ver;
	vector<Edge> edge;

	// Function to push excess flow from u 
	bool push(int u);

	// Function to relabel a vertex u 
	void relabel(int u);

	// This function is called to initialize 
	// preflow 
	void preflow(int s);

	// Function to reverse edge 
	void updateReverseEdgeFlow(int i, int flow);

public:
	Graph(int V);  // Constructor 

	// function to add an edge to graph 
	void addEdge(int u, int v, int w);

	// returns maximum flow from s to t 
	int getMaxFlow(int s, int t);
};

Graph::Graph(int V)
{
	this->V = V;

	// all vertices are initialized with 0 height 
	// and 0 excess flow 
	for (int i = 0; i < V; i++)
		ver.push_back(Vertex(0, 0));
}

void Graph::addEdge(int u, int v, int capacity)
{
	// flow is initialized with 0 for all edge 
	edge.push_back(Edge(0, capacity, u, v));
}

void Graph::preflow(int s)
{
	// Making h of source Vertex equal to no. of vertices 
	// Height of other vertices is 0. 
	ver[s].h = ver.size();

	// 
	for (int i = 0; i < edge.size(); i++)
	{
		// If current edge goes from source 
		if (edge[i].u == s)
		{
			// Flow is equal to capacity 
			edge[i].flow = edge[i].capacity;

			// Initialize excess flow for adjacent v 
			ver[edge[i].v].e_flow += edge[i].flow;

			// Add an edge from v to s in residual graph with 
			// capacity equal to 0 
			edge.push_back(Edge(-edge[i].flow, 0, edge[i].v, s));
		}
	}
}

// returns index of overflowing Vertex 
int overFlowVertex(vector<Vertex> & ver)
{
	for (int i = 1; i < ver.size() - 1; i++)
		if (ver[i].e_flow > 0)
			return i;

	// -1 if no overflowing Vertex 
	return -1;
}

// Update reverse flow for flow added on ith Edge 
void Graph::updateReverseEdgeFlow(int i, int flow)
{
	int u = edge[i].v, v = edge[i].u;

	for (int j = 0; j < edge.size(); j++)
	{
		if (edge[j].v == v && edge[j].u == u)
		{
			edge[j].flow -= flow;
			return;
		}
	}

	// adding reverse Edge in residual graph 
	Edge e = Edge(0, flow, u, v);
	edge.push_back(e);
}

// To push flow from overflowing vertex u 
bool Graph::push(int u)
{
	// Traverse through all edges to find an adjacent (of u) 
	// to which flow can be pushed 
	for (int i = 0; i < edge.size(); i++)
	{
		// Checks u of current edge is same as given 
		// overflowing vertex 
		if (edge[i].u == u)
		{
			// if flow is equal to capacity then no push 
			// is possible 
			if (edge[i].flow == edge[i].capacity)
				continue;

			// Push is only possible if height of adjacent 
			// is smaller than height of overflowing vertex 
			if (ver[u].h > ver[edge[i].v].h)
			{
				// Flow to be pushed is equal to minimum of 
				// remaining flow on edge and excess flow. 
				int flow = min(edge[i].capacity - edge[i].flow,
					ver[u].e_flow);

				// Reduce excess flow for overflowing vertex 
				ver[u].e_flow -= flow;

				// Increase excess flow for adjacent 
				ver[edge[i].v].e_flow += flow;

				// Add residual flow (With capacity 0 and negative 
				// flow) 
				edge[i].flow += flow;

				updateReverseEdgeFlow(i, flow);

				return true;
			}
		}
	}
	return false;
}

// function to relabel vertex u 
void Graph::relabel(int u)
{
	// Initialize minimum height of an adjacent 
	int mh = INT_MAX;

	// Find the adjacent with minimum height 
	for (int i = 0; i < edge.size(); i++)
	{
		if (edge[i].u == u)
		{
			// if flow is equal to capacity then no 
			// relabeling 
			if (edge[i].flow == edge[i].capacity)
				continue;

			// Update minimum height 
			if (ver[edge[i].v].h < mh)
			{
				mh = ver[edge[i].v].h;

				// updating height of u 
				ver[u].h = mh + 1;
			}
		}
	}
}

// main function for printing maximum flow of graph 
int Graph::getMaxFlow(int s, int t)
{
	preflow(s);

	// loop untill none of the Vertex is in overflow 
	while (overFlowVertex(ver) != -1)
	{
		int u = overFlowVertex(ver);
		if (!push(u))
			relabel(u);
	}

	// ver.back() returns last Vertex, whose 
	// e_flow will be final maximum flow 
	return ver.back().e_flow;
}
void readPomparePreflux() {
	ifstream fin("Pompare.txt");
	int n,u,v,w;
	fin >> n;
	Graph g(n);
	while (!fin.eof()) {
		fin >> u >> v >> w;
		g.addEdge(u, v, w);
	}
	cout << "Flux maxim: " << g.getMaxFlow(0, n-1) << "\n\n";

}
void mesaj() {
	printf("1: Colorare\n2:Ford Fulkerson\n3: Pompare Preflux");
}
int main() {
	int n;
	while (true) {
		mesaj();
		cin >> n;
		switch (n) {
		case 0:
			return 0;
		case 1:
			readColorareGraf();
			break;
		case 2:
			readFordFulkerson();
			break;
		case 3:
			readPomparePreflux();
			break;
	
		}
	}



	return 0;
}