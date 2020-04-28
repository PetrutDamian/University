function f = p2i(N)
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
    if(sqrt((xp-xc)*(xp-xc)+(yp-yc)*(yp-yc))<=r)
        A=A+1;
        plot(xp,yp,'or','MarkerSize',3,'MarkerFaceColor','r');
    end
end
disp(A/N)
    
end