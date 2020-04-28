function p2(m,sigma,n)
x=normrnd(m,sigma,1,n);
[m1,m2]=ci_mean(x,0.05,10)
[m1,m2]=ci_mean2(x,0.05)
[v1,v2]=ci_var(x,0.05)
[p1,p2]=ci_prop(155<x&x<165,0.05)

end