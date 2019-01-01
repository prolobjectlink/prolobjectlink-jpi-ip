sister(X,Y) :-
	parent(Z,X),
	parent(Z,Y),
	female(X),
	different(X,Y).

predecessor(X,Z) :-
	parent(X,Z).

predecessor(X,Z) :-
	parent(X,Y),
	predecessor(Y,Z).

parent(pam,bob).

parent(tom,bob).

parent(tom,liz).

parent(bob,ann).

parent(bob,pat).

parent(pat,jim).

male(tom).

male(bob).

male(jim).

female(pam).

female(liz).

female(ann).

female(pat).

mother(X,Y) :-
	parent(X,Y),
	female(X).

grandparent(X,Z) :-
	parent(X,Y),
	parent(Y,Z).

offspring(X,Y) :-
	parent(X,Y).

different(X,X) :-
	!,
	fail.

different(X,Y).

