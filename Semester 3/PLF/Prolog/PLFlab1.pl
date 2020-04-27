%Problema 7
apartine(X,[X|_]).
apartine(X,[_|L]):-apartine(X,L).
union(T,[],T).

union(A1,[H1|S1],R):-
    not(apartine(H1,A1)),
    union(A1,S1,U1),
    R=[H1|U1].

union(A,[H|S],U):-
    apartine(H,A),
    union(A,S,U).



concat([],L2,L2).
concat([H|T],L2,[H|R]):-
    concat(T,L2,R).




perechi(NR,[H],[[NR,H]]).
perechi(NR,[H|T],R1):-
    perechi(NR,T,R),
    concat([[NR,H]],R,R1),!.
combinari([],[]).
combinari([_],[]).
combinari([H|T],R3):-
    perechi(H,T,R1),
    combinari(T,R2),
    concat(R1,R2,R3),!.












