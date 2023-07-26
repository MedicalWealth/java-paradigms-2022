:- load_library('alice.tuprolog.lib.DCGLibrary').

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], R) :- lookup(K, T, R).

ws --> [].
ws --> [' '], ws.

plus_ws --> [' '], ws.

opt_minus --> ['-'].
opt_minus --> [].

parse_list([]) --> [].
parse_list([H | T]) --> [H], parse_list(T).

parse_str(S) --> {atom_chars(S, Chars)}, parse_list(Chars).

op_p(op_not) --> ['!'].
op_p(op_and) --> ['&'], ['&'].
op_p(op_or) --> ['|'], ['|'].
op_p(op_xor) --> ['^'], ['^'].
op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> ['n'], ['e'], ['g'], ['a'], ['t'], ['e'].

name_p([], _) --> [].
name_p([H | T], Symbols) --> {member(H, Symbols)}, 
	[H],
	name_p(T, Symbols).

expr_p(variable(Name)) --> 
	ws, {nonvar(Name, atom_chars(Name, Chars))}, 
	name_p(Chars, [x, y, z, 'X', 'Y', 'Z']), 
	{Chars = [_ | _], atom_chars(Name, Chars)}, ws.

expr_p(const(Value)) --> ws,
  { nonvar(Value, number_chars(Value, Chars)) },
  name_p(Chars, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-']),
  { Chars = [_ | _], \+ Chars = ['-'], number_chars(Value, Chars) }, ws.

expr_p(operation(Op, A, B)) --> ws, ['('], ws, expr_p(A), plus_ws, op_p(Op), plus_ws, expr_p(B), ws, [')'], ws.
expr_p(operation(Op, A)) --> ws, op_p(Op), plus_ws, expr_p(A), ws.

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :-   atom(A), atom_chars(A, C), phrase(expr_p(E), C), !.

get_op(Op, R) :- phrase(op_p(Op), Chars), atom_chars(R, Chars), !.

bool(X) :- X > 0.

eval(op_not, A, R) :- (bool(A) -> R is 0; R is 1), !.
eval(op_and, A, B, R) :- (bool(A), bool(B) -> R is 1; R is 0), !.
eval(op_or, A, B, R) :- ((bool(A); bool(B)) -> R is 1; R is 0), !.
eval(op_xor, A, B, R) :- ((bool(A), bool(B); \+ bool(A), \+ bool(B))-> R is 0; R is 1), !.

eval(op_add, A, B, R) :- R is A + B, !.
eval(op_subtract, A, B, R) :- R is A - B, !.
eval(op_multiply, A, B, R) :- R is A * B, !.
eval(op_divide, A, B, R) :- (B == 0 -> R is 0; R is A / B), !.
eval(op_negate, A, R) :- R is -A, !.

evaluate(const(Value), Variables, Value) :- !.
evaluate(variable(Chars), Map, Val) :- atom_chars(Chars, [Var | T]), lookup(Var, Map, Val).

evaluate(operation(Op, A), Variables, Value) :-
	evaluate(A, Variables, AValue),
	eval(Op, AValue, Value).
evaluate(operation(Op, A, B), Variables, Value) :-
	evaluate(A, Variables, AValue), 
	evaluate(B, Variables, BValue),
	eval(Op, AValue, BValue, Value).