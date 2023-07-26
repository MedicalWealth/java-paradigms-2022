assert_composites(N, D) :- max_val(Max), N > Max, !.
assert_composites(N, D) :- 
	assert(composite(N)), assert(min_div(N, D)), 
	T is N + D, assert_composites(T, D).

assert_table(N) :- max_val(Max), N * N > Max, !.
assert_table(H) :- \+ composite(H),
	Q is H * H, assert_composites(Q, H), !,
	T is H + 1, assert_table(T).
	
assert_table(H) :- T is H + 1, assert_table(T), !.

init(Q) :- assert(max_val(Q)), assert_table(2).

mult([N], N).
mult([H1, H2 | T], R) :- 
	H1 =< H2, 
	mult([H2 | T], R1), 
	R is H1 * R1.

prime(H) :- max_val(Max), 1 < H, H < Max, !, \+ composite(H).

min_div(N, N) :- prime(N).

prime_divisors(1, []).
prime_divisors(N, [H | T]) :-
	number(N),
	min_div(N, H), !,
	D is div(N, H), 
	prime_divisors(D, T).
	
prime_divisors(N, List) :- 
	list(List), 
	mult(List, R), N is R.


pow(_, 0, 1).
pow(A, B, R) :-
   B > 0, 1 is mod(B, 2),
   B1 is B - 1, pow(A, B1, R1),
   R is A * R1.
pow(A, B, R) :-
    B > 0, 0 is mod(B, 2),
    B2 is div(B, 2), pow(A, B2, R2),
    R is R2 * R2.


count_first_equals([N], (N, 1), []) :- !.
count_first_equals([H1, H2 | T], (H1, 1), [H2 | T]) :- H1 \= H2, !.
count_first_equals([H1, H1 | T], (H1, Cnt), Rem) :-
	count_first_equals([H1 | T], (N, Cnt1), Rem), 
	Cnt is Cnt1 + 1.

count_equals([], []) :- !.
count_equals(Input, [Cmp | Answer]) :-
	count_first_equals(Input, Cmp, Remainder), !, 
	count_equals(Remainder, Answer).

compact_prime_divisors(1, []) :- !.
compact_prime_divisors(N, CDs) :-
  number(N), !,
  prime_divisors(N, List),
  count_equals(List, CDs).

compact_prime_divisors(N, [(V, E)]) :- pow(V, E, N), !.
compact_prime_divisors(N, [(V1, E1), (V2, E2) | T]) :- 
	prime(V1), prime(V2), V1 < V2, 
	pow(V1, E1, R),
	compact_prime_divisors(N1, [(V2, E2) | T]), 
	N is N1 * R.
