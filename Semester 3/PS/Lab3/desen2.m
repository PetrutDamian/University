function ff= desen2(p,m)
x1=geornd(p,1,m);
N1=histc(x1,0:max(x1));
subplot(1,2,1); hold on; title('Geornd');
bar(0:max(x1),N1/m,'hist','FaceColor','b');
bar(0:max(x1),geopdf(0:max(x1),p),'FaceColor','y');
legend('estimated probabilities','theoretical probabilities');
axis([-0.5 max(x1) 0 max([p,N1/m])])

x2=my_geornd(p,m);
N2=histc(x2,0:max(x2));
subplot(1,2,2); hold on; title('My geornd');
bar(0:max(x2),N2/m,'hist','FaceColor','b');
bar(0:max(x2),geopdf(0:max(x2),p),'FaceColor','y');
legend('estimated probabilities','theoretical probabilities');
axis([-0.5 max(x2) 0 max([p,N2/m])])
ff=0;
end