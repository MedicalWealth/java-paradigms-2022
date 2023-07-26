node(X, Val, node(X, Y, Val, [null, null])) :- rand_int(1000000, Y).
node(X, Y, Val, Sons, node(X, Y, Val, Sons)).
node(node(X, Y, Val, Sons), node(X, Y, Val, Sons)).

split(null, K, null, null).

split(node(X, Y, Val, [S0, S1]), K, NewT, T2) :-
	K > X, !, split(S1, K, T1, T2), 
	node(X, Y, Val, [S0, T1], NewT).

split(node(X, Y, Val, [S0, S1]), K, T1, NewT) :-
  split(S0, K, T1, T2),
	node(X, Y, Val, [T2, S1], NewT).

merge(T1, null, T1) :- !.
merge(null, T2, T2) :- !.
merge(node(T1X, T1Y, T1Val, [T1S0, T1S1]), node(T2X, T2Y, T2Val, [T2S0, T2S1]), Result) :-
	(T1Y > T2Y -> 
		merge(T1S1, node(T2X, T2Y, T2Val, [T2S0, T2S1]), NewT1S1), 
		node(T1X, T1Y, T1Val, [T1S0, NewT1S1], Result), !
		;
		merge(node(T1X, T1Y, T1Val, [T1S0, T1S1]), T2S0, NewT2S0),
		node(T2X, T2Y, T2Val, [NewT2S0, T2S1], Result)).

put(_, null, Key, Value, Result) :- node(Key, Value, Result), !.

put(Change, Node, Key, Value, Result) :-
	split(Node, Key, T1, T2), Key1 is Key + 1,
	split(T2, Key1, CurrentNode, Right), 
	((Change = true; CurrentNode = null) -> node(Key, Value, NewNode); node(CurrentNode, NewNode)), !,
	merge(NewNode, Right, NewT2),
	merge(T1, NewT2, Result).
	
map_put(Node, Key, Value, Result) :-
	put(true, Node, Key, Value, Result).

map_putIfAbsent(Node, Key, Value, Result) :-
	put(fail, Node, Key, Value, Result).

map_get(node(X, Y, Val, [S0, S1]), X, Val) :-	!.

map_get(node(X, Y, _, [S0, S1]), Key, Val) :-
	Key > X -> map_get(S1, Key, Val); map_get(S0, Key, Val).

map_build([], null) :- !.
map_build([(Key, Value) | T], ResultTree) :-
	map_build(T, Tree), !, map_put(Tree, Key, Value, ResultTree).

map_remove(null, X, null) :- !.

map_remove(node(X, Y, Val, [S0, S1]), X, Result) :-
	merge(S0, S1, Result), !.

map_remove(node(X, Y, Val, [S0, S1]), X0, Result) :-	
	(X0 > X -> map_remove(S1, X0, T1), node(X, Y, Val, [S0, T1], Result);
	map_remove(S0, X0, T1), node(X, Y, Val, [T1, S1], Result)).
	
		
	
	
	
