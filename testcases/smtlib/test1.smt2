(declare-fun x () Int)
(declare-fun y () Int)
(declare-fun z () Real)
(declare-fun z2 () Real)
(assert (< (+ x y) 3))
(assert (< x y))
(assert (< y x))
(assert (< (+ z z2) z))
(check-sat)
(pop 1)
(check-sat)
