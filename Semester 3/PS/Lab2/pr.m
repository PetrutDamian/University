function f = pr(N)
A=0;
r = 0.5;
xc=0.5;
yc=0.5;
hold on;
axis square;
axis off;
rectangle('Position',[0 0 1 1],'FaceColor','w');
rectangle('Position',[0 0 1 1],'Curvature',[1 1]);
for i=1:N
    xp=rand();
    yp=rand();
    dc=pdist([[xp,yp];[0.5,0.5]]);
    d1=pdist([[xp,yp];[1,1]]);
    d2=pdist([[xp,yp];[0,1]]);
    d3=pdist([[xp,yp];[1,0]]);
    d4=pdist([[xp,yp];[0,0]]);
    if(dc < d1 && dc<d2 && dc<d3 && dc<d4)
        A=A+1;
        plot(xp,yp,'or','MarkerSize',3,'MarkerFaceColor','r');
    end
end
disp(A/N)
    
end