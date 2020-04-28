function f = pr3(N)
hold on;
axis square;
axis off; 
rectangle('Position',[0 0 1 1],'FaceColor','w');
nr=0;
for i=1:N
    xp=rand();
    yp=rand();
    
    %calculam laturile cu pdist
    pd=pdist([[xp,yp];[0,0]]);
    pa=pdist([[xp,yp];[0,1]]);
    pb=pdist([[xp,yp];[1,1]]);
    pc=pdist([[xp,yp];[1,0]]);
    
    %calculam cos de fiecare unghi cu teorema cosinusului
    cosA1 = (pa*pa+1-pd*pd)/2*pa;
    cosA2 = (1+pa*pa-pb*pb)/2*pa;
    cosB1 = (1+pb*pb-pa*pa)/2*pb;
    cosB2 = (1+pb*pb-pc*pc)/2*pb;
    cosC1 = (1+pc*pc-pb*pb)/2*pc;
    cosC2 = (1+pc*pc-pd*pd)/2*pc;
    cosD1 = (1+pd*pd-pa*pa)/2*pd;
    cosD2 = (1+pd*pd-pc*pc)/2*pd;
    cosP1 = (pa*pa+pd*pd -1)/2*pa*pd;
    cosP2 = (pa*pa+pb*pb-1)/2*pa*pb;
    cosP3 = (pb*pb+pc*pc -1)/2*pb*pc;
    cosP4 = (pc*pc + pd*pd - 1)/2*pc*pd;
    
    nrob=0;
    %calculam ungiurile cu arccos si verificam pentru fiecare triunghi daca
    % are un unghi obtuz
    
    A1 = acosd(cosA1);
    P1 = acosd(cosP1);
    D1 = acosd(cosD1);
    if(A1>90 || P1>90 || D1>90)
        nrob=nrob+1;
    end
    
    
    A2 = acosd(cosA2);
    P2 = acosd(cosP2);
    B1 = acosd(cosB1);
    if(A2>90 || P2>90 || B1>90)
        nrob=nrob+1;
    end
    
    B2 = acosd(cosB2);
    P3 = acosd(cosP3);
    C1 = acosd(cosC1);
    if(B2>90 || P3>90 || C1>90)
        nrob=nrob+1;
    end
    
    D2 = acosd(cosD2);
    C2 = acosd(cosC2);
    P4 = acosd(cosP4);
    if(D2>90 || C2>90 || P4>90)
        nrob=nrob+1;
    end
    
    if(nrob==2)
        nr=nr+1;
        plot(xp,yp,'or','MarkerSize',3,'MarkerFaceColor','r');
    end
end
f=nr/N;
end