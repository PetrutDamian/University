
lungime([],0).
lungime([_|T],R):-
	lungime(T,R1),
	R is R1+1.
sir([],[]).

sir([_|T],R):-
	sir(T,R).

sir([H|T],R1):-
	sir(T,R),
	R =[H1|_],
	H<H1,
	R1 = [H|R].
sir([H|T],[H|R]):-
	sir(T,R),
	R =[].

siruri(L,R):-findall(S,(sir(L,S),lungime(S,LL),LL>=2),R).
