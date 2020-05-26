#include "pch.h"

#include <queue>
#include <iostream>
#include <fstream>
#include <vector>
#include <utility>
#include <algorithm>
#include <tuple>
#include <unordered_map>
#include <stack>
#include <string>
using namespace std;
bool isIn(vector<int>& q, int nod) {

	for (int i = 1; i < int(q.size()); i++)
		if (q[i] == nod)
			return true;
	return false;
}
void afisPrim(vector<int>& key, vector<int>& p, int n) {
	cout << "\nArborele de acoperire de cost minim e alcatuit din urmatoarele muchii:\n";
	for (int i = 1; i <= n; i++)
		if (p[i] != 0)
			cout << p[i] << " " << i << endl;
}
void prim(vector<pair<int, int>>* list, int n, int r, vector<int>& key, vector<int>& p) {
	key.push_back(0);
	p.push_back(0);
	for (int i = 1; i <= n; i++)
		key.push_back(10000), p.push_back(0);
	key.at(r) = 0;
	vector<int> q;
	q.push_back(0);
	for (int i = 1; i <= n; i++)
		q.push_back(i);
	while (q.size() != 1) {
		int min = 10000;
		int nod = 0;
		for(int i=1;i<q.size();i++)
			if (min >= key[q[i]]) {
				min = key[q[i]];
				nod = i;
			}
		int value = q[nod];
		q.erase(q.begin() + nod);
		for (int i = 0; i < list[value].size(); i++) {
			if (isIn(q, list[value][i].first) && list[value][i].second < key[list[value][i].first]){
				p[list[value][i].first] = value;
				key[list[value][i].first] = list[value][i].second;
			}
		}
	}
}
void readPrim() {
	ifstream fin("Prim.txt");
	int n;
	fin >> n;
	vector<pair<int, int>>* list = new vector<pair<int,int>>[n+1];
	while (!fin.eof()) {
		int a, b, c;
		fin >> a >> b >> c;
		list[a].push_back(make_pair(b, c));
		list[b].push_back(make_pair(a, c));
	}
	vector<int> key;
	vector<int> p;
	prim(list, n,1,key,p);
	afisPrim(key, p,n);
}
int fminim(int v[],int s[],int n) {

	int x[100] = { 0 };
	for (int i = 1; i <= n; i++)
		if (s[i] == 0)
			x[v[i]] = 1;
	for (int i = 1; i <= n; i++)
		if (x[i] == 0 && s[i]==0)
			return i;
}
vector<int> decodarePrufer(vector<int> k, int n) {
	vector<int> t;
	for (int i = 0; i <= n; i++)
		t.push_back(0);
	for (int i = 1; i < n; i++) {
		int x = k[0];//parintele frunzei minime
		int v[100] = { 0 };
		int y = 0;
		for (int i = 0; i < k.size(); i++)
			v[k[i]] = 1;
		for(int i=1;i<=n;i++)//cel mai mic element care nu se gaseste in k(frunza minima)
			if (v[i] == 0) {
				y = i;
				break;
			}
		t[y] = x;
		k.erase(k.begin());
		k.push_back(y);
	}
	return t;
}
vector<int> codarePrufer(int v[], int n) {
	vector<int> k;
	int still[100] = { 0 };
	while(true){
		int f_min = fminim(v, still, n);
		if (f_min == 1)break;
		still[f_min] = 1;
		k.push_back(v[f_min]);
	}
	return k;
}
void readCodificarePrufer() {
	ifstream fin("CodificarePrufer.txt");
	int n;
	fin >> n;
	int v[100] = { 0 };
	while (!fin.eof()) {
		int a, b;
		fin >> a >> b;
		v[b] = a;
	}
	vector <int> k = codarePrufer(v, n);
	cout << endl;
	for (auto it = k.begin(); it != k.end(); it++)
		cout << *it << " ";
	cout << endl;
	vector<int> t = decodarePrufer(k, n);
	for (auto it = t.begin()+1; it != t.end(); it++)
		cout << *it << " ";
	cout << endl;
}
struct muchie{
	int a, b, w;
};
bool m_sort(muchie m1, muchie m2) {
	return m1.w < m2.w;
}
void merge(vector<int>& part, int x, int y) {
	int px = part[x];
	int py = part[y];
	for (unsigned int i = 1; i < part.size(); i++)
		if (part[i] == py)
			part[i] = px;
}
void Kruskal(vector <muchie>& list, int n) {
	sort(list.begin(), list.end(), m_sort);
	vector<int> part;
	part.push_back(0);
	for (int i = 1; i <= n; i++)
		part.push_back(i);
	auto it = list.begin();
	while (it != list.end()) {
		if (part[(*it).a] != part[(*it).b]) {
			merge(part,(*it).a,(*it).b);
			it++;
		}
		else
			it=list.erase(it);
	}
}
void readKruskal() {
	ifstream fin("Prim.txt");
	int n;
	fin >> n;
	vector <muchie> list;

	while (!fin.eof()) {
		int a, b, c;
		fin >> a >> b >> c;
		muchie m;
		m.a = a;
		m.b = b;
		m.w = c;
		list.push_back(m);
	}
	Kruskal(list, n);
	cout << "Lista muchii Kruskal:\n";
	for (auto it = list.begin(); it != list.end(); it++)
		cout << (*it).a << " " << (*it).b << endl;
}
class Node
{
public:
	char ch;
	int freq;
	Node *left, *right;
	Node(char _ch, int _freq, Node* _left, Node* _right):ch{_ch},freq{_freq},left{_left},right{_right}{}
};
class compareNodes
{	public:
	bool operator()(Node* left, Node* right)
	{
		return (left->freq > right->freq);
	}
};
Node* huffman(string c) {
	int n = c.size();
	std::priority_queue<Node*, vector<Node*>, compareNodes> Q;
	unordered_map<char, int> char_freq;
	for (char ch : c) {
		char_freq[ch]++;
	}
	for (auto chr : char_freq) {
		Q.push(new Node(chr.first, chr.second, nullptr, nullptr));
	}
	int m = Q.size();
	for (int i = 1; i <= m-1; i++) {
		Node *x = Q.top();
		Q.pop();
		Node *y = Q.top();
		Q.pop();
		Node* z = new Node('\0', x->freq + y->freq, x, y);
		Q.push(z);
	}
	return Q.top();
}
void encode(Node* root, string str, unordered_map<char, string> &huffmanCode)
{
	if (root == nullptr)
		return;

	if (!root->left && !root->right) {
		huffmanCode[root->ch] = str;
	}
	encode(root->left, str + "0", huffmanCode);
	encode(root->right, str + "1", huffmanCode);
}
void readHuffman() {
	ifstream fin("input_file.txt");
	string text;
	char txt[100];
	fin.getline(txt, 100);
	text = txt;
	cout << text << endl;
	Node* root = huffman(text);
	ofstream fout("input_file_huffman.txt");
	unordered_map<char, string> huffmanCode;
	encode(root, "", huffmanCode);
	for (auto p : huffmanCode) {
		cout << p.first << " " << p.second << endl;
	}
	for (auto it : text) {
		fout << huffmanCode[it];
	}
}
void Hierholzer(vector< vector<int> > adj)
{
	vector<int> edge_count;
	edge_count.push_back(0);

	for (int i = 1; i < adj.size(); i++)
		edge_count.push_back(adj[i].size());
	stack<int> path; 
	vector<int> eulerian; 
	path.push(1);
	int curr_v = 1;

	while (!path.empty())
	{
		if (edge_count[curr_v])
		{                      
			path.push(curr_v);
			int next_v = adj[curr_v].back();
			edge_count[curr_v]--;
			edge_count[next_v]--;
			adj[curr_v].pop_back();
			auto it = find_if(adj[next_v].begin(), adj[next_v].end(), [&curr_v](int& nr) {return nr == curr_v; });
			if (it != adj[next_v].end())
				adj[next_v].erase(it);
			curr_v = next_v;
		}
		else
		{
			eulerian.push_back(curr_v);
			curr_v = path.top();
			path.pop();
		}
	}
	for (int i = eulerian.size() - 1; i >= 1; i--)
	{
		cout << eulerian[i];
			cout << "->";
	}
	cout << eulerian[eulerian.size()-1];
}
void readHierholzer() {
	ifstream fin("Hierholzer.txt");
	int n;
	fin >> n;
	vector<vector<int>> adj;
	adj.resize(n + 1);
	while (!fin.eof())
	{
		int a, b;
		fin >> a >> b;
		adj[a].push_back(b);
		adj[b].push_back(a);
	}
	Hierholzer(adj);
}
void mesaj() {
	cout << "\nComenzi:\n0 : Exit\n1 : Prim\n2 : Codificare Prufer\n3 : Huffman\n4 : Kruskal\n5 : Hierholzer\n";
}
int main()
{
	int n;
	while (true){
		mesaj();
		cin >> n;
		switch (n) {
		case 0:
			return 0;
		case 1:
			readPrim();
			break;
		case 2:
			readCodificarePrufer();
			break;
		case 3:
			readHuffman();
			break;
		case 4:
			readKruskal();
			break;
		case 5:
			readHierholzer();
			break;
		}
	}
}