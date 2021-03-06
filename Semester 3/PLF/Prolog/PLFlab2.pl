
concat([],L2,L2).
concat([H|T],L2,[H|R]):-
    concat(T,L2,R).

replace([],_,_,[]).
replace([H|T],E,L,[H|R]):-
	H=\=E,!,
	replace(T,E,L,R).

replace([_|T],E,L,R):-
	replace(T,E,L,R1),
	concat(L,R1,R).


firstreplace([],_,[]).
firstreplace([H|T],L,[H|R]):-
	atomic(H),!,
	firstreplace(T,L,R).

firstreplace([H|T],L,[R1|R2]):-
	is_list(H),
	H=[A|_],
	replace(H,A,L,R1),
	firstreplace(T,L,R2).





