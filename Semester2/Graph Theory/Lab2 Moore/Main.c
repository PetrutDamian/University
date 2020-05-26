#include <stdio.h>
#include <stdlib.h>
void mesaj()
{
	printf("\nAvailable commands:\n0 - exit application\n1 - Moore\n2 - Inchidere tranzitiva\n3 - Labirint\n");
}
void afisDrum(int s, int v, int p[])
{
	if (v == s)
	{
		printf("%d", s);
		return;
	}
	if (p[v] == 0)
	{
		printf("Nu se poate ajunge din %d la %d", s, v);
		return;
	}
		afisDrum(s, p[v], p);
		printf("->%d", v);
}
void drum(int s, int n,int p[])
{
	for (int i = 1; i <= n; i++)
		if (i != s)
		{
			printf("Drum %d - %d: ", s, i);
			afisDrum(s, i, p);
			printf("\n");
		}
}
void moore(int n, int ma[][100], int s)
{
	int l[100], p[100] = { 0 }, q[100];
	int st = 0, dr = 0;
	q[0] = s;
	for (int i = 1; i <= n; i++)
		l[i] = 9999;
	l[s] = 0;
	while (st <= dr)
	{
		for (int i = 1; i <= n; i++)
			if (ma[q[st]][i] != 0 && l[i] == 9999)
			{
				p[i] = q[st];
				l[i] = l[q[st]] + 1;
				dr++;
				q[dr] = i;
			}
		st++;
	}
	drum(s, n, p);
}
void readMoore(int n, int ma[][100])
{
	printf("Introduceti nodul sursa:");
	int s;
	scanf("%d", &s);
	moore(n,ma,s);
}
int produs(int n,int lant[][100])
{
	int da = 0;
	for (int i = 1; i<=n;i++)
		for (int j = 1; j <= n; j++)
		{
			int t = 0;
			for (int k = 1; k <= n; k++)
				t = (t || (lant[i][k] && lant[k][j]));
			if (t != lant[i][j])
				da = 1;
			lant[i][j] = t;
		}
	return da;
}
void readInchidereTranzitiva(int n, int ma[][100])
{
	int lant[100][100];
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= n; j++)
			lant[i][j] = ma[i][j];
	while (1)
	{
		if (!produs(n, lant))
			break;
	}
	for (int i = 1; i <= n; i++)
	{
		for (int j = 1; j <= n; j++)
			printf("%d ", lant[i][j]);
		printf("\n");
	}
}
void initializare(int n, int ma[][100])
{
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= n; j++)
			ma[i][j] = 0;
}
void bfs(int n,int ma[][100], int add[][900], int s, int f, int l ,int c)
{
	
	int p[900] = { 0 };
	int viz[900] = { 0 };
	viz[s] = 1;
	int st = 1, dr = 1;
	int q[900];
	q[1] = s;
	while (st<=dr && viz[f]==0)
	{
		for (int i = 1; i <= n; i++)
			if (add[q[st]][i] == 1 && viz[i] == 0)
			{
				p[i] = q[st];
				viz[i] = 1;
				dr++;
				q[dr] = i;
			}
		st++;
	}
	int k = f;
	while (k != s)
	{
		int lin = k / c ;
		int col = k % c;
		if (col == 0)
			col = c;
		else
			lin++;
		ma[lin][col] = 2;
		k = p[k];
	}
	for (int i = 1; i <= l; i++)
	{
		for (int j = 1; j <= c; j++)
		{
			if (ma[i][j] == 1)
				printf("1");
			if (ma[i][j] == 0)
				printf(" ");
			if (ma[i][j] == 2)
				printf("o");
		}
		printf("\n");
	}
}
void readLabirint(int l,int c,int ma[][100], int s1, int s2, int f1, int f2)
{
	int n = l * c;
	int add[900][900] = { 0 };
	for (int i = 1; i <= l; i++)
		for (int j = 1; j <= c; j++)
			if (ma[i][j] != 1)
			{
				if (ma[i][j + 1] != 1)
					add[(i - 1)*c + j][(i - 1)*c + j + 1] = 1;
				if (ma[i][j -1] != 1)
					add[(i - 1)*c + j][(i - 1)*c + j - 1] = 1;
				if (ma[i+1][j] != 1)
					add[(i - 1)*c + j][i*c + j] = 1;
				if (ma[i-1][j] != 1)
					add[(i - 1)*c + j][(i - 2)*c + j] = 1;
			}
	int s = (s1 - 1)*c + s2;
	int f = (f1 - 1)*c + f2;
	bfs(n,ma, add, s, f,l,c);
}
int main()
{
	FILE* f;
	f = fopen("graf.txt", "r");
	int n, ma[100][100] = { 0 };
	int a, b;
	fscanf(f, "%d", &n);
	while (!feof(f))
	{
		fscanf(f,"%d %d", &a, &b);
		ma[a][b] = 1;
	}
	fclose(f);

	f = fopen("graf1.txt", "r");
	int m, ma1[100][100] = { 0 };
	fscanf(f, "%d", &m);
	while (!feof(f))
	{
		fscanf(f, "%d %d", &a, &b);
		ma1[a][b] = 1;
	}
	for (int i = 1; i <= n; i++)
		ma1[i][i] = 1;
	fclose(f);

	int cmd, ma2[100][100] = { 0 };
	f = fopen("graf2.txt", "r");
	int lin=1,col=1;
	int s1, s2, f1, f2;
	while (!feof(f))
	{
		char c;
		fscanf(f, "%c", &c);
		if (c == '\n')
		{
			col = 1;
			lin++;
		}
		else
		{
			if(c == '1')
				ma2[lin][col] = 1;
			if (c == ' ')
				ma2[lin][col] = 0;
			if (c == 'S')
				s1 = lin, s2 = col;
			if (c == 'F')
				f1 = lin, f2 = col;
			col++;
		}
	}
	col -= 2;
	for (int i = 1; i <= col; i++)
		ma2[0][i] = 1, ma2[lin + 1][i] = 1;
	for (int i = 1; i <= lin; i++)
		ma2[i][0] = 1, ma2[i][col + 1] = 1;

	while (1)
	{
		mesaj();
		printf(">>");
		scanf("%d", &cmd);
		if (cmd == 0)break;
		switch (cmd)
		{
		case 0:
			return 0;
		case 1:
			readMoore(n, ma);
			break;
		case 2:
			readInchidereTranzitiva(m,ma1);
			break;
		case 3:
			readLabirint(lin,col,ma2,s1,s2,f1,f2);
			break;
		}
	}
	return 0;
}