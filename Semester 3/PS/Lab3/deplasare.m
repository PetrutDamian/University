function f = deplasare(m,k,p)
matrix=zeros(m,k);
for i=1:m
    nod=0;
x = my_bernoulli(p,1,k);
for j=1:length(x)
    if(x(j)==0)
        nod=nod-1;
    else
        nod=nod+1;
    end
    matrix(i,j)=nod;
end
end
f=matrix;
for i=1:m
   disp(matrix(i,k)) 
end
end